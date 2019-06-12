package com.bond.testgithub.content.github;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;


public class ProviderGitHub extends ContentProvider {
  // Use an int for each URI we will run, this represents the different queries
  private static final int CASE_QUERY1 = 100;
  private static final int CASE_QUERY1_ID = 101;
  private static final String GIT_QUERY_URL = "https://api.github.com/search/repositories?q=";
  private static final UriMatcher sUriMatcher = buildUriMatcher();


  @Override
  public boolean onCreate() {
    return true;
  }

  /**
   * Builds a UriMatcher that is used to determine witch database request is being made.
   */
  public static UriMatcher buildUriMatcher()  {
    String content = ContractGitHub.CONTENT_AUTHORITY;

    // All paths to the UriMatcher have a corresponding code to return
    // when a match is found (the ints above).
    // "/#"  == Any numeric
    // "/*"  == Any string
    UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    matcher.addURI(content, ContractGitHub.PATH_CASE_QUERY1, CASE_QUERY1);
    matcher.addURI(content, ContractGitHub.PATH_CASE_QUERY1 + "/#", CASE_QUERY1_ID);

    return matcher;
  }

  @Override
  public String getType(Uri uri) {
    switch(sUriMatcher.match(uri)){
      case CASE_QUERY1:
        return ContractGitHub.CaseStringEntry.CONTENT_TYPE;
      case CASE_QUERY1_ID:
        return ContractGitHub.CaseStringEntry.CONTENT_ITEM_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    //final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    Cursor retCursor = null;
    switch(sUriMatcher.match(uri)){
      case CASE_QUERY1:
        int size = 0;
        if (null  != selectionArgs && selectionArgs.length > 0) {
          retCursor = new GitHubCursor(selectionArgs[0]);
//          try {
//            size = Integer.parseInt(selectionArgs[0]);
//          } catch (Exception e) {}
        }

        break;
      case CASE_QUERY1_ID:
        int size2 = 0;
        if (null  != selectionArgs && selectionArgs.length > 0) {
          try {
            size2 = Integer.parseInt(selectionArgs[0]);
          } catch (Exception e) {}
        }
        retCursor = null;//new GitHubCursor(size2);
        long _id = ContentUris.parseId(uri);
        //HA-HA-HA: Cursor Interface want's int:
        retCursor.moveToPosition((int)_id);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Set the notification URI for the cursor to the one passed into the function. This
    // causes the cursor to register a content observer to watch for changes that happen to
    // this URI and any of it's descendants. By descendants, we mean any URI that begins
    // with this path.
    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return retCursor;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    return null;
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values,
                    @Nullable String selection, @Nullable String[] selectionArgs) {
    return 0;
  }

  public class GitHubCursor implements Cursor {
    final String TAG = "GitHubCursor";
    volatile  int  curPos  =  0;
    volatile  int  itemsCount  =  0;
    volatile  boolean  go_load_tail  =  false;
    public final ConditionVariable conditionVariable  =  new ConditionVariable();
    //https://stackoverflow.com/questions/21623714/what-is-cursor-setnotificationuri-used-for
    final Handler guiHandler = new Handler(Looper.getMainLooper());
    volatile DataSetObserver dataSetObserver = null;
    volatile int  startID  =  0;
    Thread thread;
    LocalThread local;
    final ConcurrentHashMap<Integer, String> data = new ConcurrentHashMap<>();


    void runOnGUIthread (Runnable r) {
        try {
          guiHandler.post(r);
        } catch (Exception e) {
          Log.e(TAG, "runOnGUIthread error:", e );
        }
    }

    final Runnable publishData = new Runnable() {
      @Override
      public void run() {
        if (null  != dataSetObserver) {
          try {
            dataSetObserver.onChanged();
            //dataSetObserver.onInvalidated();
          } catch (Exception e) {
            Log.e(TAG, "publishData():", e);
          }
        }
      }
    };


    void local_start(String search_string)  {
      ++startID;
      synchronized (TAG) {
        local = new LocalThread(startID, search_string);
        thread = new Thread(local);
        thread.start();
      }
    }

    void local_stop() {
      ++startID;
      conditionVariable.open();
      synchronized (TAG) {
        local = null;
        thread = null;
      }
    }

    public GitHubCursor(String search_string) {
      try {
        local_start(search_string);
      } catch (Exception e) {
        Log.e(TAG, "local_start():", e);
      }
    }

    @Override
    public int getCount() {
      return itemsCount;
    }

    @Override
    public int getPosition() {
      return curPos;
    }

    @Override
    public boolean move(int offset) {
      int newPos = curPos + offset;
      if (newPos >= 0 && newPos < itemsCount) {
        curPos = newPos;
        return true;
      }
      return false;
    }

    @Override
    public boolean moveToPosition(int position) {
      if (position >= 0 && position < itemsCount) {
        curPos = position;
        if (curPos > (itemsCount >> 1)) {
          go_load_tail = true;
          conditionVariable.open();
        }
        return true;
      }
      return false;
    }

    @Override
    public boolean moveToFirst() {
      curPos = 0;
      return true;
    }

    @Override
    public boolean moveToLast() {
      curPos = itemsCount - 1;
      return true;
    }

    @Override
    public boolean moveToNext() {
      return move(1);
    }

    @Override
    public boolean moveToPrevious() {
      return move(-1);
    }

    @Override
    public boolean isFirst() {
      Log.w(TAG, "isFirst");
      return   0 == curPos;
    }

    @Override
    public boolean isLast() {
      Log.w(TAG, "isLast");
      return (itemsCount - 1) == curPos;
    }

    @Override
    public boolean isBeforeFirst() {
      Log.w(TAG, "isBeforeFirst");
      return false;
    }

    @Override
    public boolean isAfterLast() {
      Log.w(TAG, "isAfterLast");

      return false;
    }

    @Override
    public int getColumnIndex(String columnName) {
      Log.w(TAG, "getColumnIndex");
      return 0;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
      Log.w(TAG, "getColumnIndexOrThrow");
      return 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
      Log.w(TAG, "getColumnName");
      return ContractGitHub.CaseStringEntry.COLUMN_NAME;
    }

    @Override
    public String[] getColumnNames() {
      return new String[] { ContractGitHub.CaseStringEntry.COLUMN_NAME };
    }

    @Override
    public int getColumnCount() {
      return 1;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
      return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
      return data.get(curPos);
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
      Log.w(TAG, "copyStringToBuffer");
    }

    @Override
    public short getShort(int columnIndex) {
      return 0;
    }

    @Override
    public int getInt(int columnIndex) {
      return 0;
    }

    @Override
    public long getLong(int columnIndex) {
      return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
      return 0;
    }

    @Override
    public double getDouble(int columnIndex) {
      return 0;
    }

    @Override
    public int getType(int columnIndex) {
      Log.w(TAG, "getType");
      return FIELD_TYPE_STRING;
    }

    @Override
    public boolean isNull(int columnIndex) {
      Log.w(TAG, "isNull");
      return false;
    }

    @Override
    public void deactivate() {
      Log.w(TAG, "deactivate");
    }

    @Override
    public boolean requery() {
      Log.w(TAG, "requery");
      return false;
    }

    @Override
    public void close() {
      local_stop();
      Log.w(TAG, "close");
    }

    @Override
    public boolean isClosed() {
      Log.w(TAG, "isClosed");
      return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
      Log.w(TAG, "registerContentObserver");
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
      Log.w(TAG, "unregisterContentObserver");
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
      dataSetObserver  =  observer;
      Log.w(TAG, "registerDataSetObserver");
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
      dataSetObserver  =  null;
      Log.w(TAG, "unregisterDataSetObserver");
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {
      Log.w(TAG, "setNotificationUri");
    }

    @Override
    public Uri getNotificationUri() {
      Log.w(TAG, "getNotificationUri");
      return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
      Log.w(TAG, "getWantsAllOnMoveCalls");
      return false;
    }

    @Override
    public void setExtras(Bundle extras) {
      Log.w(TAG, "setExtras");
    }

    @Override
    public Bundle getExtras() {
      Log.w(TAG, "getExtras");
      return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
      Log.w(TAG, "respond");
      return null;
    }

    class LocalThread  implements Runnable {
      final int myID;
      String search;
      public LocalThread(int startID, String search_string) {
        search = search_string;
        myID = startID;

      }

      @Override
      public void run() {
        boolean was_error = false;
          try {
            String pointer_to_next_result_page = null;
            //Base query
            if (myID == startID) {
              URL url = new URL(GIT_QUERY_URL + search);
              HttpURLConnection conn=(HttpURLConnection) url.openConnection();
              conn.setConnectTimeout(10000); // timing out in 10sec
              ///conn.setConnectTimeout(60000); // timing out in a minute
              BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              String str;
              StringBuilder sb = new StringBuilder(4096);
              while ((str = in.readLine()) != null) {
                sb.append(str);
              }  //  read new line
              pointer_to_next_result_page = parseJSON(sb.toString());
            }

            //Tail query
            while (null != pointer_to_next_result_page  &&  myID == startID) {
              conditionVariable.close();
              //TODO загрузить оставшиеся JSON страницы при пересечении
              //юзером проловины результатов при скроллинге
              //если RAM не хватит, то кэшировать в SQLite
//              URL url = new URL(GIT_QUERY_URL + search);
//              HttpURLConnection conn=(HttpURLConnection) url.openConnection();
//              conn.setConnectTimeout(10000); // timing out in 10sec
//              ///conn.setConnectTimeout(60000); // timing out in a minute
//              BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//              String str;
//              StringBuilder sb = new StringBuilder(4096);
//              while ((str = in.readLine()) != null) {
//                sb.append(str);
//              }  //  read new line

              conditionVariable.block(StaticConsts.MSEC_KEEP_ALIVE);
            }
          } catch (Exception e) {
            was_error = true;
            Log.e(TAG, "LocalThread.run():", e);
          }
          if (was_error) {
            //TODO Прокинуть в GUI почему не вышло
          }
      } // run()

      String parseJSON(String json) throws  Exception {
        String re = null;
        JSONObject main_obj = new JSONObject(json);
        boolean incomplete_results = main_obj.getBoolean("incomplete_results");
        int total_count = main_obj.getInt("total_count");
        if (total_count > 0) {
          JSONArray items = main_obj.getJSONArray("items");
          int  items_count  =  items.length();
          if (total_count != items_count) {
            Log.e(TAG, "Wrong result from GitHub: total_count="
              + String.valueOf(total_count)
                + " vs items_count=" + String.valueOf(items_count));
          }
          if (items_count > 0) {
            for (int  j = 0 ;  j  <  items_count;  ++j) {
              JSONObject repo_obj = items.getJSONObject(j);
              data.put(itemsCount, repo_obj.toString());
              ++itemsCount;
            }
          }
          runOnGUIthread(publishData);
        }
        return re;
      }  // parseJSON
    }
  }  //GitHubCursor

}

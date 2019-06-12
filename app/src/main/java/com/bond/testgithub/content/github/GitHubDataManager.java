package com.bond.testgithub.content.github;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.ICallBackWithRecyclerDataItem;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISimpleObserver;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.FragmentKey;
import com.bond.testgithub.ui.main.UserSettings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubDataManager implements IRecyclerDataManager {
  final String TAG = "GitHubDataManager";
  String  cur_query = "tetris+language:assembly&sort=stars&order=desc";
  long last_time_query = 0l;
  Cursor cursor = null;
  ISimpleObserver observer = null;

  @Override
  public void registerSimpleObserver(ISimpleObserver observer) {
    this.observer = observer;
  }

  @Override
  public int getItemCount() {
    int re = 0;
    if (null != cursor) {
      try {
        re = cursor.getCount();
      } catch (Exception e) {
        Log.e(TAG, "setSearchString():", e);
      }
    }
    return re;
  }

  @Override
  public RecyclerDataItem getItemAtPos(int position) {
    RecyclerDataItem re = null;
    if (null != cursor) {
      try {
        if (cursor.moveToPosition(position)) {
          re = UserSettings.jsonToRecyclerDataItem(cursor.getString(0));
        }
      } catch (Exception e) {
        Log.e(TAG, "setSearchString():", e);
      }
    }
    return re;
  }

  @Override
  public void reloadItem(RecyclerDataItem old_item, ICallBackWithRecyclerDataItem callBackWithRecyclerDataItem) {
    final ICallBackWithRecyclerDataItem f_callBackWithRecyclerDataItem =  callBackWithRecyclerDataItem;
    final RecyclerDataItem f_old_item = old_item;
    Runnable simpleHere = new Runnable() {
      @Override
      public void run() {
        RecyclerDataItem re = null;
        try {
          JSONObject obj = null;
          if (null != f_old_item.jsonParsed) {
            obj = f_old_item.jsonParsed.get();
          }
          if (null == obj) {
            obj = new JSONObject(f_old_item.json);
          }
          String str_url = obj.getString("url");
          if (null != str_url && !str_url.isEmpty()) {
            URL url = new URL(str_url);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000); // timing out in 10sec
            ///conn.setConnectTimeout(60000); // timing out in a minute
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            StringBuilder sb = new StringBuilder(4096);
            while ((str = in.readLine()) != null) {
              sb.append(str);
            }  //  read new line
            re = UserSettings.jsonToRecyclerDataItem(sb.toString());
          }
        } catch (Exception e) {
          Log.e(TAG, "reloadItem():", e);
        }

        try {
          final RecyclerDataItem f_re = re;
          SpecTheme.iActivity.runOnGUIthreadDelay(
              new Runnable() {
                @Override
                public void run() {
                  f_callBackWithRecyclerDataItem.onReadyRecyclerDataItem(f_re);
                }
              } , 0l
          );
        } catch (Exception e) {
          Log.e(TAG, "onReadyRecyclerDataItem():", e);
        }
      }
    };
    new Thread(simpleHere).start();
  }

  @Override
  public void onClickItem(Object object) {
    try {
      SpecTheme.iActivity.showMainView(
          new FragmentKey(StaticConsts.MainViewDetail)
          , StaticConsts.M_GO_FRAG_UI_MSG
          , object
          );
    } catch (Exception e) {
      Log.e(TAG, "setSearchString():", e);
    }
  }

  @Override
  public String getLastSearchString() {
    return cur_query;
  }

  @Override
  public void setSearchString(String query) {
    if (cur_query.equals(query)) {
      if (System.currentTimeMillis() - last_time_query
          < StaticConsts.MSEC_GITHUB_LIMIT) {  return ; }
    }
    cur_query = query;
    try {
      if (null != cursor) {
        cursor.close();
      }
      if (null == query || query.isEmpty()) {  return; }
      last_time_query = System.currentTimeMillis();
      cursor = SpecTheme.context.getContentResolver().query(
          ContractGitHub.CaseStringEntry.CONTENT_URI,
          null,
          null,
          new String[] { query },
          null
      );
      if (null != cursor)  {
        cursor.registerDataSetObserver(new DataSetObserver() {
          @Override
          public void onChanged() {
            super.onChanged();
            if (null != observer) {
              observer.onSimpleChange();
            } else {
              Log.e(TAG, "null == observer.onSimpleChange()");
            }
          }
        });
        if (null != observer) {
          observer.onSimpleChange();
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "setSearchString():", e);
    }
  }

  @Override
  public void onScreen() {

  }
}

package com.bond.testgithub.content.local;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.ICallBackWithRecyclerDataItem;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISimpleObserver;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.FragmentKey;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class LocalGitDataManager
    implements IRecyclerDataManager, ILocalDB  {
  final String TAG = "LocalGitDataManager";
  String cur_login = null;
  String cur_search_string = "%";
  FutureTask<SQLiteDbAdapter> futureDB = null;
  FutureTask<Cursor> futureCursor = null;
  ISimpleObserver observer = null;

  @Override
  public void setCurScheme(String login) {
    closeDB();
    cur_login = login;
    final String f_login = login;
    final Context f_context = SpecTheme.context;
    Callable task = new Callable() {
       @Override
       public Object call() throws Exception {
         return SQLiteDbAdapter.openScheme(f_context, f_login);
       }
     };
    futureDB = new FutureTask<>(task);
    new Thread(futureDB).start();
  }

  void  closeCursor() {
    if (null != futureCursor) {
      try {
        Cursor cursor = futureCursor.get();
        if (null != cursor) {
          cursor.close();
        }
        futureCursor = null;
      } catch (Exception e) {
        Log.e(TAG, "closeCursor():", e);
      }
    }
  }

  void closeDB() {
    closeCursor();
    if (null != futureDB) {
      try {
        SQLiteDbAdapter db = futureDB.get();
        if (null != db) {
          db.close();
        }
        futureDB = null;
      } catch (Exception e) {
        Log.e(TAG, "closeDB():", e);
      }
    }
  }


  @Override
  public void onDestroy() {
    closeDB();
  }

  @Override
  public IRecyclerDataManager getIRecyclerDataManager() {
    return this;
  }

  @Override
  public void registerSimpleObserver(ISimpleObserver observer) {
    synchronized (TAG) {
      this.observer = observer;
    }
  }

  @Override
  public int getItemCount() {
    int re = 0;
    if (null == futureCursor) {  return re ; }
    try {
        re = futureCursor.get().getCount();
    } catch (Exception e) {
      Log.e(TAG, "getItemCount()" , e);
    }
    return re;
  }

  @Override
  public RecyclerDataItem getItemAtPos(int position) {
    RecyclerDataItem re = null;
    if (null == futureCursor) {  return re ; }
    try {
      Cursor cur = futureCursor.get();
      if (cur.moveToPosition(position)) {
        String owner = cur.getString(cur.getColumnIndex("owner"));
        String repo = cur.getString(cur.getColumnIndex("repo"));
        String url = cur.getString(cur.getColumnIndex("url"));
        String json = cur.getString(cur.getColumnIndex("json"));
        re = new RecyclerDataItem();
        re.str1 = owner;
        re.str2 = repo;
        re.json = json;
        re.img_url = url;
      }
    } catch (Exception e) {
      Log.e(TAG, "getItemCount()" , e);
    }
    return re;
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
    return cur_search_string;
  }

  @Override
  public void setSearchString(String query) {
    if (null == futureDB ) {  return; }
    closeCursor();
    cur_search_string = query;
    final String f_query = query;
    Callable task = new Callable() {
      @Override
      public Object call() throws Exception {
        Cursor cur = futureDB.get().selectGithub(f_query);
        SpecTheme.iActivity.runOnGUIthreadDelay(publish, StaticConsts.MSEC_SLEEP);
        return cur;
      }
    };
    futureCursor = new FutureTask<>(task);
    new Thread(futureCursor).start();
  }

  final Runnable publish = new Runnable() {
    @Override
    public void run() {
      try {
        if (null != observer) {
          observer.onSimpleChange();
        }
      } catch (Exception e) {
        Log.e(TAG, "publish.run():", e);
      }
    }
  };

  @Override
  public boolean existGithub(String owner, String repo) {
    if (null == futureDB ) {  return false; }
    boolean re = false;
    try {
      re = futureDB.get().existGithub(owner, repo);
    } catch (Exception e) {
      Log.e(TAG, "existGithub()", e);
    }
    return re;
  }

  @Override
  public void insertGithub(String owner, String repo, String url, String json) {
    if (null == futureDB ) {  return; }
    closeCursor();
    final String f_owner = owner;
    final String f_repo = repo;
    final String f_url   = url;
    final String f_json = json;
    final String f_query = cur_search_string;
    Callable task = new Callable() {
      @Override
      public Object call() throws Exception {
        futureDB.get().insertGithub(f_owner, f_repo, f_url, f_json);
        Cursor cur = futureDB.get().selectGithub(f_query);
        SpecTheme.iActivity.runOnGUIthreadDelay(publish, StaticConsts.MSEC_SLEEP);
        return cur;
      }
    };
    futureCursor = new FutureTask<>(task);
    new Thread(futureCursor).start();
  }

  @Override
  public void updateGithub(String owner, String repo, String url, String json) {
    if (null == futureDB ) {  return; }
    closeCursor();
    final String f_owner = owner;
    final String f_repo = repo;
    final String f_url = url;
    final String f_json = json;
    final String f_query = cur_search_string;
    Callable task = new Callable() {
      @Override
      public Object call() throws Exception {
        futureDB.get().updateGithub(f_owner, f_repo, f_url, f_json);
        Cursor cur = futureDB.get().selectGithub(f_query);
        SpecTheme.iActivity.runOnGUIthreadDelay(publish, StaticConsts.MSEC_SLEEP);
        return cur;
      }
    };
    futureCursor = new FutureTask<>(task);
    new Thread(futureCursor).start();
  }

  @Override
  public void deleteGithub(String owner, String repo) {
    if (null == futureDB ) {  return; }
    closeCursor();
    final String f_owner = owner;
    final String f_repo = repo;
    final String f_query = cur_search_string;
    Callable task = new Callable() {
      @Override
      public Object call() throws Exception {
        futureDB.get().deleteGithub(f_owner, f_repo);
        Cursor cur = futureDB.get().selectGithub(f_query);
        SpecTheme.iActivity.runOnGUIthreadDelay(publish, StaticConsts.MSEC_SLEEP);
        return cur;
      }
    };
    futureCursor = new FutureTask<>(task);
    new Thread(futureCursor).start();
  }

  @Override
  public void onScreen() {
    setSearchString(cur_search_string);
  }

  @Override
  public void reloadItem(RecyclerDataItem old_item, ICallBackWithRecyclerDataItem callBackWithRecyclerDataItem) {

  }
}

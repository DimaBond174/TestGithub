package com.bond.testgithub.content.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDbAdapter extends SQLiteOpenHelper {
  final String TAG = "SQLiteDbAdapter";
  static final int DATABASE_VERSION = 1;

  static SQLiteDbAdapter openScheme(Context context
      , String login) throws  Exception {
    SQLiteDbAdapter re = new SQLiteDbAdapter(context, login);
    re.getWritableDatabase();
    return re;
  }

  SQLiteDbAdapter(Context context, String login) {
    super(context,  login, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL( "create table IF NOT EXISTS t_github (owner text NOT NULL, "
        + "repo text NOT NULL, "
        + "url text NOT NULL, "
        + "json text NOT NULL, " //Общение всегда внутри группы
        + "PRIMARY KEY (owner, repo));");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public Cursor selectGithub(String query) throws Exception {
    query = query
        .replaceAll(";", "")
        .replaceAll("\"", "")
        .replaceAll("'","");
    SQLiteDatabase db = getReadableDatabase();
    StringBuilder sb = new StringBuilder(512);
    sb.append("select  *  from t_github where json like '%")
        .append(query)
        .append("%'");
    return db.rawQuery(sb.toString(), null);
  }

  public boolean existGithub(String owner, String repo) {
    boolean re = false;
    //faux loop
    do {
      String owner_db = owner
          .replaceAll(";", "")
          .replaceAll("\"", "")
          .replaceAll("'","");
      if (!owner.equals(owner_db)) { break; }
      String repo_db = repo
          .replaceAll(";", "")
          .replaceAll("\"", "")
          .replaceAll("'","");
      if (!repo.equals(repo_db)) { break; }
      try {
        StringBuilder sb = new StringBuilder(512);
        sb.append("select  owner  from t_github where owner ='")
            .append(owner_db)
            .append("' and repo='").append(repo_db)
            .append("'");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur=db.rawQuery(sb.toString(), null);
        if (cur != null) {
          re = cur.getCount() > 0;
          cur.close();
        }
      } catch (Exception e) {
        Log.e(TAG, "existGithub()", e);
      }
    } while (false);
    return re;
  }

  public void deleteGithub(String owner, String repo) {
    String owner_db = owner
        .replaceAll(";", "")
        .replaceAll("\"", "")
        .replaceAll("'","");
    String repo_db = repo
        .replaceAll(";", "")
        .replaceAll("\"", "")
        .replaceAll("'","");
    try {
      StringBuilder sb = new StringBuilder(512);
      sb.append("owner ='")
          .append(owner_db)
          .append("' and repo='").append(repo_db)
          .append("'");
      SQLiteDatabase db = getWritableDatabase();
      db.delete("t_github", sb.toString(), null);
    } catch (Exception e) {
      Log.e(TAG, "deleteGithub()", e);
    }
    return ;
  } // deleteGithub

  public void insertGithub(String owner, String repo, String url, String json) {
      String owner_db = owner
          .replaceAll(";", "")
          .replaceAll("\"", "")
          .replaceAll("'","");
      String repo_db = repo
          .replaceAll(";", "")
          .replaceAll("\"", "")
          .replaceAll("'","");
      try {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("owner", owner_db);
        args.put("repo", repo_db);
        args.put("url", url);
        args.put("json", json);
        db.insert("t_github",null, args);
      } catch (Exception e) {
        Log.e(TAG, "existGithub()", e);
      }
    return ;
  }


  public void updateGithub(String owner, String repo, String url, String json) {
    String owner_db = owner
        .replaceAll(";", "")
        .replaceAll("\"", "")
        .replaceAll("'","");
    String repo_db = repo
        .replaceAll(";", "")
        .replaceAll("\"", "")
        .replaceAll("'","");
    try {
      SQLiteDatabase db = getWritableDatabase();
      ContentValues args = new ContentValues();
      args.put("url", url);
      args.put("json", json);
      StringBuilder sb = new StringBuilder(512);
      sb.append("owner ='")
          .append(owner_db)
          .append("' and repo='").append(repo_db)
          .append("'");
      db.update("t_github",args, sb.toString(), null);
    } catch (Exception e) {
      Log.e(TAG, "updateGithub()", e);
    }
    return ;
  }

}
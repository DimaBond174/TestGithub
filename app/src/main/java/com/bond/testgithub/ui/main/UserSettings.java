package com.bond.testgithub.ui.main;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.bond.testgithub.i.ISearchControl;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;


import org.json.JSONObject;

import java.lang.ref.SoftReference;

import static android.content.Context.MODE_PRIVATE;

public class UserSettings implements IUserSettings {
  static final String TAG = "UserSettings";
  final String login;
  final SparseArray<String> map_strings = new SparseArray<>();
  final SparseIntArray map_ints = new SparseIntArray();
  public UserSettings(String login) {
    this.login = login;
  }

  @Override
  public void onDestroy() {
    try {
      SharedPreferences appPrefs =
          SpecTheme.context.getSharedPreferences(login, MODE_PRIVATE);
      SharedPreferences.Editor editor = appPrefs.edit();
      int size = map_strings.size();
      for(int i = 0; i < size;  ++i) {
        int key = map_strings.keyAt(i);
        // get the object by the key.
        String val = map_strings.get(key);
        editor.putString(String.valueOf(key),  val);
      }
      size = map_ints.size();
      for(int i = 0; i < size;  ++i) {
        int key = map_ints.keyAt(i);
        // get the object by the key.
        int val = map_ints.get(key);
        editor.putInt(String.valueOf(key),  val);
      }
      editor.commit();
    } catch (Exception e) {
      Log.e(TAG, "putString()", e);
    }
  }

  @Override
  public String getString(int key, String def) {
    String re = map_strings.get(key);
    if (null == re) {
      try {
        SharedPreferences appPrefs =
            SpecTheme.context.getSharedPreferences(login, MODE_PRIVATE);
        re  =  appPrefs.getString(String.valueOf(key),  def);
        map_strings.put(key, re);
      } catch (Exception e) {
        Log.e(TAG, "getString()", e);
      }
    }
    return re;
  }

  @Override
  public void putString(int key, String value) {
    map_strings.put(key, value);
  }

  @Override
  public int getInt(int key, int  def) {
    int re = map_ints.get(key, -1);
    if (-1 == re) {
      try {
        SharedPreferences appPrefs =
            SpecTheme.context.getSharedPreferences(login, MODE_PRIVATE);
        re  =  appPrefs.getInt(String.valueOf(key),  def);
        map_ints.put(key, re);
      } catch (Exception e) {
        Log.e(TAG, "getInt()", e);
      }
    }
    return re;
  }

  @Override
  public void putInt(int key, int value) {
    map_ints.put(key, value);
  }

  @Override
  public void setISearchControl(ISearchControl iSearchControl) {
    this.iSearchControl = iSearchControl;
  }

  @Override
  public ISearchControl getISearchControl() {
    return iSearchControl;
  }


  public static RecyclerDataItem jsonToRecyclerDataItem(String json) {
    RecyclerDataItem re = null;
    if (null != json && !json.isEmpty()) {
      try {
        JSONObject obj = new JSONObject(json);
        RecyclerDataItem item = new RecyclerDataItem();
        item.json = json;
        item.str2 = obj.getString("name");
        JSONObject  owner = obj.getJSONObject("owner");
        item.str1 = owner.getString("login");
        item.img_url = owner.getString("avatar_url");
        item.jsonParsed = new SoftReference<>(obj);
        re = item;
      } catch (Exception e) {
        Log.e(TAG, "jsonToRecyclerDataItem():", e);
      }
    }
    return re;
  }

  // Private
  /////////////////////////////////
  ISearchControl iSearchControl = null;

}

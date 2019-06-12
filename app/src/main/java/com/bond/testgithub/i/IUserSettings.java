package com.bond.testgithub.i;

public interface IUserSettings {
  String getString(int  key, String def);
  void putString(int  key, String value);
  int getInt(int  key, int  def);
  void putInt(int  key, int value);
  void onDestroy();

  void setISearchControl (ISearchControl iSearchControl);
  ISearchControl getISearchControl();
}

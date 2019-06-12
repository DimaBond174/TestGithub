package com.bond.testgithub.i;

import android.os.Handler;

import com.bond.testgithub.ui.main.FragmentKey;

public interface IActivity {
  void  showMessage(String  str);
  void  goBack();
  Handler getGuiHandler();
  void runOnGUIthreadDelay (Runnable r,  long delay);
  void showMainView(FragmentKey fragmentKey, int msgType, Object obj);
  ILocalDB getLocalDB();
  IRecyclerDataManager getGlobalDB();
  IUserSettings getIUserSettings();
}

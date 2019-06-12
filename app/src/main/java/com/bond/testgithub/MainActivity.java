package com.bond.testgithub;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.IActivity;
import com.bond.testgithub.i.IAuthCallback;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IMainViewFrag;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.frags.*;
import com.bond.testgithub.ui.main.ActivityViewModel;
import com.bond.testgithub.ui.main.FragmentKey;
import com.bond.testgithub.ui.main.MainSettings;
import com.bond.testgithub.ui.main.MainWindow;
import com.bond.testgithub.ui.main.UserSettings;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
    implements IActivity, IAuthCallback {

  final String TAG = "MainActivity";
  final GuiHandler guiHandler = new GuiHandler(Looper.getMainLooper());
  final FragmentKey FirstFragKey
      =  new FragmentKey(StaticConsts.FirstFragTAG);
//  final Map<FragmentKey, IMainViewFrag> uiFrags
//      =  new HashMap<>();
  final Deque<FragmentKey> uiFragsControl
      =  new ArrayDeque<FragmentKey>();
  IMainViewFrag curActiveFrag  =  null;
  MainWindow mainWindow  =  null;
  boolean guiNotStarted  =  true;


  ActivityViewModel activityViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SpecTheme.context = MainActivity.this;
    setContentView(R.layout.activity_inject_point);
    mainWindow = (MainWindow) findViewById(R.id.mainWindow);
    activityViewModel
        = ViewModelProviders.of(this).get(ActivityViewModel.class);
    restoreState(savedInstanceState);
    onNewIntent(getIntent());
  }


  @Override
  public void goBack() {
    onBackPressed();
  }



  @Override
  public void showMessage(String str) {
    Snackbar.make(mainWindow, str, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
  }


  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (null  !=  curActiveFrag) {
      outState.putString("curActiveFrag.fragTAG",
          curActiveFrag.getFragmentKey().fragTAG);
      activityViewModel.savedInstanceState = outState;
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    restoreState (savedInstanceState);
  }

  private void restoreState (Bundle savedInstanceState) {
    activityViewModel.savedInstanceState  =  savedInstanceState;
    if  (null  ==  savedInstanceState) {  return ;  }
    String fragTAG = savedInstanceState.getString("curActiveFrag.fragTAG");
    if  (null  !=  fragTAG)  {
      setCurActiveFrag(new FragmentKey(fragTAG));
    }//if (null!=fragTAG)
  }

  @Override
  protected void onActivityResult(int requestCode,
      int resultCode, Intent data)  {
    boolean go_next = true;
    if (requestCode == StaticConsts.RQS_AUTH) {
      //Это заказывал iAuth и он либо на экране:
      if (null != mainWindow.getActiveFrag()) {
        go_next = !mainWindow.getActiveFrag()
            .onActivityResultMainView(requestCode,  resultCode, data);
      }
      //либо не на экране:
      if (go_next) {
        if (null != activityViewModel.iAuth) {
          go_next = !activityViewModel.iAuth
              .onActivityResultMainView(requestCode,  resultCode, data);
        }
      }
    }  else  {
      // Обработка заказов неAuth виджетов
      if (null  !=  curActiveFrag)  {
        go_next = !curActiveFrag.onActivityResultMainView(requestCode,
            resultCode,  data);
      }
    }
    if  (go_next) {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void setCurActiveFrag(FragmentKey key)  {
    if  (null  ==  key)  return;
    IMainViewFrag frg  =  null; //uiFrags.get(key);
    if  (null  ==  frg )  {
      frg  =  createUiFragment(key);
    }
    if  (frg  !=  curActiveFrag)  {
      if (null  !=  curActiveFrag )  {
        curActiveFrag.onStopMainView(activityViewModel.iUserSettings);
        mainWindow.checkDelCurFrag(curActiveFrag);
       // uiFrags.remove(curActiveFrag.getFragmentKey());
        uiFragsControl.remove(curActiveFrag.getFragmentKey());
      }
      curActiveFrag = frg;
      mainWindow.setCurActiveFrag(curActiveFrag);

      if (curActiveFrag.getFragmentKey()
          .fragTAG.equals(StaticConsts.FirstFragTAG)) {
        clearUiFrags(FirstFragKey);
      }

     // uiFrags.put(curActiveFrag.getFragmentKey(), curActiveFrag);
      uiFragsControl.add(curActiveFrag.getFragmentKey());
    }

    if (null  !=  curActiveFrag)  {
      curActiveFrag.onStartMainView(activityViewModel.iUserSettings);
    }
  }

  IMainViewFrag createUiFragment(FragmentKey key)  {
    Context context  =  MainActivity.this;
    IMainViewFrag frg  =  null;
    switch (key.fragTAG) {
      case StaticConsts.MainViewDetail:
        frg = new MainViewDetail(context);
        break;
      case StaticConsts.FirstFragTAG:
      default:
        frg  = new MainView2Tabs(context);
        break;
    }
    return frg;
  }


  @Override
  public Handler getGuiHandler() {
    return  guiHandler;
  }


  @Override
  public void onPause() {
    if (null  !=  curActiveFrag) {
      curActiveFrag.onStopMainView(activityViewModel.iUserSettings);
    }
    super.onPause();
  }

  private void onFirstStart() {
    FragmentKey key = uiFragsControl.peekLast();
    if (null  ==   key) {
      //TestPresenter.onFirstStart();
      setCurActiveFrag(FirstFragKey);
    } else {
      setCurActiveFrag(key);
    }
    guiNotStarted  =  false;
    if (Build.VERSION.SDK_INT  < 23) {
      repairSSL_onAndroid4();
    }
  }

  @Override
  public void onStart() {
    SpecTheme.applyMetrics(MainActivity.this);
    super.onStart();

    if (guiNotStarted) {
      onFirstStart();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (null == activityViewModel.iAuth) {
      activityViewModel.iAuth = MainSettings.getAuth(this);
    }
    /*
    Возвращаясь к работе всегда проверяем валидность сессии.
    Если у модуля аутентификации есть возможность проверить
     сессию скрытно, то он должен вернуть true
     (если позднее у модуля возникнут проблемы,
     то он через IAuthCallback заблокирует работу и выведет Logon UI)
     */
    if (activityViewModel.iAuth.isAuthStillValid(this,this)) {
      checkLocalDB();
      if (null  !=  curActiveFrag) {
        if (mainWindow.getActiveFrag() != curActiveFrag) {
          mainWindow.setCurActiveFrag(curActiveFrag);
        }
        curActiveFrag.onStartMainView(activityViewModel.iUserSettings);
      }  else {
        onFirstStart();
      }
    }  else  {
      mainWindow.setCurActiveFrag(
          activityViewModel.iAuth.getNewUI(this));
      mainWindow.getActiveFrag().onStartMainView(activityViewModel.iUserSettings);
    }
  }

  void checkLocalDB() {
    if (null == activityViewModel.iGlobalDB)  {
      activityViewModel.iGlobalDB = MainSettings.getGitHubManager();
    }

    if (null == activityViewModel.iLocalDB)  {
      activityViewModel.iLocalDB = MainSettings.getLocalDB();
    }
    activityViewModel.iLocalDB.setCurScheme(activityViewModel.iAuth.getUserLogin());
    activityViewModel.iUserSettings = new UserSettings(activityViewModel.iAuth.getUserLogin());
  }

  @Override
  public void onStop() {
    if  (null  !=  curActiveFrag)  {
      curActiveFrag.onStopMainView(activityViewModel.iUserSettings);
    }
    guiNotStarted  =  true;
    //TestPresenter.onGUIstop();
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    //exitMain();
    clearUiFrags();
    if (null != activityViewModel.iUserSettings)  {
      activityViewModel.iUserSettings.onDestroy();
    }
    if (null != activityViewModel.iLocalDB)  {
      activityViewModel.iLocalDB.onDestroy();
    }
    SpecTheme.onDestroy();
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (null == curActiveFrag) {
      super.onBackPressed();
    }  else if (curActiveFrag.getFragmentKey().fragTAG
        .equals(StaticConsts.FirstFragTAG)) {
      curActiveFrag = null;
      clearUiFrags();
      if (null == activityViewModel.iAuth) {
        activityViewModel.iAuth = MainSettings.getAuth(this);
      }
      mainWindow.setCurActiveFrag(
          activityViewModel.iAuth.getNewUI(this));
      mainWindow.getActiveFrag().onStartMainView(activityViewModel.iUserSettings);
    }  else  {
      setCurActiveFrag(FirstFragKey);
    }
//    if (null  !=  curActiveFrag
//        && curActiveFrag.getFragmentKey().fragTAG
//        .equals(StaticConsts.FirstFragTAG)) {
//      //exitMain();
//      super.onBackPressed();
//    }  else  {
//      setCurActiveFrag(FirstFragKey);
//    }
  }


//  private void exitMain()  {
//    try {
//      guiNotStarted = true;
//      mainWindow.onDestroy();
//      clearUiFrags();
//      SpecTheme.onDestroy();
//      finish();
//      System.gc();
//    } catch (Exception e) {}
//    try {
//      super.onBackPressed();
//      System.gc();
//    } catch (Exception e) {}
//  }

  private void clearUiFrags(FragmentKey exeptFragKey)  {
    for (FragmentKey fragKey : uiFragsControl)  {
      if (fragKey.equals(exeptFragKey)) {
        continue;
      }
//      IMainViewFrag frag  =  uiFrags.get(fragKey);
//      if (null  !=  frag)  {
//        mainWindow.checkDelCurFrag(frag);
//        frag.onStopMainView(activityViewModel.iUserSettings);
//      }
    }
    //uiFrags.clear();
    uiFragsControl.clear();
  }

  private void clearUiFrags() {
//    for (Map.Entry<FragmentKey, IMainViewFrag> entry : uiFrags.entrySet()) {
//      entry.getValue().onStopMainView(activityViewModel.iUserSettings);
//    }
    uiFragsControl.clear();
//    uiFrags.clear();
    curActiveFrag = null;
  }

  class GuiHandler extends Handler {
    public GuiHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      try {
        switch (msg.what) {
          case StaticConsts.M_GO_FRAG_UI_MSG:
            /* Доставка сообщения во фрагмент (с пересозданием если его нет) */
            //if (null != msg.obj) goFragmentMsg((MsgTemplate) msg.obj);
            break;

          default:
            super.handleMessage(msg);
        }
      } catch (Exception e) {
        Log.e(TAG, "GuiHandler: error Message handling",e);
      }
    }
  }

  @Override
  public void onSuccessAuth() {
    mainWindow.setCurActiveFrag(curActiveFrag);
  }

  @Override
  public void runOnGUIthreadDelay(Runnable r, long delay) {
    try {
      if (0l == delay) {
        guiHandler.post(r);
      } else {
        guiHandler.postDelayed(r, delay);
      }
    } catch (Exception e) {
      Log.e(TAG, "runOnGUIthread error:", e );
    }
  }

  @Override
  public void showMainView(FragmentKey fragmentKey, int msgType, Object obj) {
    setCurActiveFrag(fragmentKey);
    mainWindow.getActiveFrag().onMessageToMainView(msgType,  obj);
  }

  @Override
  public ILocalDB getLocalDB() {
    return activityViewModel.iLocalDB;
  }

  @Override
  public IUserSettings getIUserSettings() {
    return activityViewModel.iUserSettings;
  }

  @Override
  public IRecyclerDataManager getGlobalDB() {
    return activityViewModel.iGlobalDB;
  }

  void repairSSL_onAndroid4() {
    new Thread(new Runnable() {
      @Override
      public void run() {

        // Required to support Android 4.x.x (patches for OpenSSL from Google-Play-Services)
        try {
          ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {

          // Indicates that Google Play services is out of date, disabled, etc.
          e.printStackTrace();
          // Prompt the user to install/update/enable Google Play services.
          GooglePlayServicesUtil.showErrorNotification(
              e.getConnectionStatusCode(), getApplicationContext());
          return;

        } catch (GooglePlayServicesNotAvailableException e) {
          // Indicates a non-recoverable error; the ProviderInstaller is not able
          // to install an up-to-date Provider.
          e.printStackTrace();
          return;
        }
      }
    }).start();
  }
}
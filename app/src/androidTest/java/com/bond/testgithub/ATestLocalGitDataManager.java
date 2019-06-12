package com.bond.testgithub;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.content.local.LocalGitDataManager;
import com.bond.testgithub.i.IActivity;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISimpleObserver;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.FragmentKey;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ATestLocalGitDataManager {
  final  String TAG = "ATestLocalGitDataManager";
  volatile int stage = 0;
  static final int SUCCESS_STAGE = 1111111;
  static final int ERROR_STAGE = 1313131;
  LocalGitDataManager localGitDataManager = null;

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();
    //Контекст преднамеренно хранится в одном месте чтобы контроллировать
    //возможную утечку контекста также в одном месте:
    SpecTheme.context = appContext;
    SpecTheme.iActivity = new LocalTesterIActivity(appContext);
    localGitDataManager = new LocalGitDataManager();
    localGitDataManager.setCurScheme("testUser");
    localGitDataManager.registerSimpleObserver(new LocalAsyncTester());
    stage = 0;
    localGitDataManager.insertGithub(owner, repo, url, json);
    long start = System.currentTimeMillis();
    try {
      while (stage < SUCCESS_STAGE
          && (System.currentTimeMillis() - start) < StaticConsts.MSEC_ISDEAD) {
        Thread.sleep(StaticConsts.MSEC_SLEEP);
      }
    } catch (Exception e) {}
    assertEquals(stage, SUCCESS_STAGE);

    SpecTheme.context = null;
    SpecTheme.iActivity = null;
  }

  class LocalAsyncTester implements ISimpleObserver {
    @Override
    public void onSimpleChange() {
      boolean res = false;
      switch (stage) {
        case 0:
          res = localGitDataManager.existGithub(owner, repo);
          if (!res) { stage = ERROR_STAGE; } else { ++stage; }
          assertEquals(res, true);
          if (res) {
            localGitDataManager.deleteGithub(owner, repo);
          }
          break;
        case 1:
          res = localGitDataManager.existGithub(owner, repo);
          if (res) { stage = ERROR_STAGE; } else { stage = SUCCESS_STAGE; }
          assertEquals(res, false);
          break;
      }
    }
  }

  final String owner = "test_owner";
  final String repo = "test_repo";
  final String url = "https://avatars2.githubusercontent.com/u/4355665?v=4";
  final String json = "{\n" +
      "  \"name\": \"specnet\",\n" +
      "  \"owner\": {\n" +
      "    \"login\": \"WheezePuppet\",\n" +
      "    \"avatar_url\": \"https://avatars2.githubusercontent.com/u/4355665?v=4\",\n" +
      "  },\n" +
      "  \"url\": \"https://api.github.com/repos/WheezePuppet/specnet\",\n" +
      "}";

  class LocalTesterIActivity implements IActivity {
    Handler guiHandler;
    public LocalTesterIActivity(Context context)  {
       guiHandler = new Handler(context.getMainLooper());
    }

    @Override
    public void showMessage(String str) {

    }

    @Override
    public void goBack() {

    }

    @Override
    public Handler getGuiHandler() {
      return null;
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

    }

    @Override
    public ILocalDB getLocalDB() {
      return null;
    }

    @Override
    public IRecyclerDataManager getGlobalDB() {
      return null;
    }

    @Override
    public IUserSettings getIUserSettings() {
      return null;
    }
  }

}

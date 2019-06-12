package com.bond.testgithub.ui.frags;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.bond.testgithub.R;
import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.ICallBackWithRecyclerDataItem;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IMainViewFrag;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.FragmentKey;
import com.bond.testgithub.ui.main.UserSettings;
import com.bond.testgithub.ui.widgets.BottomButtons;
import com.bond.testgithub.ui.widgets.WidGithub_for_detail;
import com.bond.testgithub.ui.widgets.WinProgressBar;

public class MainViewDetail extends FrameLayout
    implements IMainViewFrag {
  public static final String TAG = "MainViewDetail";
  FragmentKey  fragmentKey;
  RecyclerDataItem curRecyclerDataItem = null;
  WidGithub_for_detail detailView;
  ScrollView scrollView;
  View controlsView = null;
  WinProgressBar progressBar;



  public MainViewDetail(Context context)  {
    super(context);
    fragmentKey = new FragmentKey(TAG);

    scrollView  =  new ScrollView(context);
    scrollView.setScrollbarFadingEnabled(false);

    detailView  =  new WidGithub_for_detail(context);
    scrollView.addView(detailView, new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT));

    addView(scrollView, new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT));



    //progressBarIn.setVisibility(GONE);
    progressBar = new WinProgressBar(context);
    progressBar.setVisibility(GONE);
    addView(progressBar, new LayoutParams(SpecTheme.dpProgress,
        SpecTheme.dpProgress));
  }

  @Override
  public void onStartMainView(IUserSettings iUserSettings) {
    if (null != iUserSettings && null == curRecyclerDataItem) {
      curRecyclerDataItem = UserSettings
          .jsonToRecyclerDataItem(iUserSettings.getString(
              StaticConsts.PARM_MainViewDetail_RecyclerDataItem, ""));
      detailView.setData(curRecyclerDataItem);
    }
    if (null != SpecTheme.iActivity && null != curRecyclerDataItem) {
      ILocalDB localDB = SpecTheme.iActivity.getLocalDB();
      removeControlsView();
      if (null != localDB) {
        setControlsView(localDB.existGithub(curRecyclerDataItem.str1, curRecyclerDataItem.str2));
      }
    }
    //PARM_MainViewDetail_RecyclerDataItem
  }

  @Override
  public void onStopMainView(IUserSettings iUserSettings) {
    if (null != iUserSettings && null != curRecyclerDataItem) {
      iUserSettings.putString(StaticConsts.PARM_MainViewDetail_RecyclerDataItem,
          curRecyclerDataItem.json);
    }
  }

  @Override
  public View getMainView() {
    return this;
  }


  @Override
  public boolean onActivityResultMainView(int requestCode, int resultCode, Intent data) {
    return false;
  }

  @Override
  public FragmentKey getFragmentKey() {
    return fragmentKey;
  }


  @Override
  public void onMessageToMainView(int msgType, Object obj) {
    try {
      switch (msgType) {
        case StaticConsts.M_GO_FRAG_UI_MSG:
          onM_GO_FRAG_UI_MSG((RecyclerDataItem)obj);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      Log.e(TAG, "onMessageToMainView():", e);
    }
  }

  void onM_GO_FRAG_UI_MSG(RecyclerDataItem recyclerDataItem) {
    curRecyclerDataItem = recyclerDataItem;
    detailView.setData(recyclerDataItem);
    ILocalDB localDB = SpecTheme.iActivity.getLocalDB();
    removeControlsView();
    if (null != localDB) {
      setControlsView(localDB.existGithub(recyclerDataItem.str1, recyclerDataItem.str2));
    }
  }

  void removeControlsView() {
    if (null != controlsView) {
      removeView(controlsView);
      controlsView = null;
    }
  }

  void setControlsView (boolean exist) {
    if (exist)  {
      BottomButtons bottomButtons = new BottomButtons(SpecTheme.context);
      bottomButtons.init(SpecTheme.SDarkColor, SpecTheme.KeyBoardColor, SpecTheme.HiLightColor
          , SpecTheme.dpPaint1Line, SpecTheme.dpBottomButtons,
          SpecTheme.dpButtonSmImgSize, 14, new BottomButtons.BottomButtonsCallback() {
            @Override
            public void onBottomButtonClick(int rIconID) {
              switch (rIconID) {
                case R.drawable.ic_delete_forever_black_24dp:
                  onDELETEClick();
                  break;
                case R.drawable.ic_update_black_24dp:
                  onLOADclick();
                  break;
                case R.drawable.ic_save_black_24dp:
                  onUPDATEclick();
                  break;
                default:
                  break;
              }
            }
          });

      bottomButtons.addButton(R.drawable.ic_delete_forever_black_24dp, R.string.caption_delete);
      bottomButtons.addButton(R.drawable.ic_update_black_24dp, R.string.caption_update);
      bottomButtons.addButton(R.drawable.ic_save_black_24dp, R.string.caption_save);
      addView(bottomButtons, new LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT));
      controlsView = bottomButtons;
    }  else  {
      BottomButtons bottomButtons = new BottomButtons(SpecTheme.context);
      bottomButtons.init(SpecTheme.SDarkColor, SpecTheme.KeyBoardColor, SpecTheme.HiLightColor
          , SpecTheme.dpPaint1Line, SpecTheme.dpBottomButtons,
          SpecTheme.dpButtonSmImgSize, 14, new BottomButtons.BottomButtonsCallback() {
            @Override
            public void onBottomButtonClick(int rIconID) {
              switch (rIconID) {
                case R.drawable.ic_save_black_24dp:
                  onSAVEclick();
                  break;
                default:
                  break;
              }
            }
          });

      bottomButtons.addButton(R.drawable.ic_save_black_24dp, R.string.caption_save);
      addView(bottomButtons, new LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT));
      controlsView = bottomButtons;
    }
    requestLayout();
  } //setControlsView

  void onSAVEclick() {
    ILocalDB localDB = SpecTheme.iActivity.getLocalDB();
    if (null != localDB && null != curRecyclerDataItem) {
      localDB.insertGithub(curRecyclerDataItem.str1,
          curRecyclerDataItem.str2,
          curRecyclerDataItem.img_url,
          curRecyclerDataItem.json);
    }
    SpecTheme.iActivity.goBack();
  }

  void onUPDATEclick() {
    ILocalDB localDB = SpecTheme.iActivity.getLocalDB();
    if (null != localDB && null != curRecyclerDataItem) {
      localDB.updateGithub(curRecyclerDataItem.str1,
          curRecyclerDataItem.str2,
          curRecyclerDataItem.img_url,
          curRecyclerDataItem.json);
    }
    SpecTheme.iActivity.goBack();
  }

  void onLOADclick() {
    IRecyclerDataManager iRecyclerDataManager
        = SpecTheme.iActivity.getGlobalDB();
    if (null != iRecyclerDataManager) {
      progressBar.setVisibility(VISIBLE);
      progressBar.setProgress(10);
      requestLayout();
      iRecyclerDataManager.reloadItem(curRecyclerDataItem
          , new ICallBackWithRecyclerDataItem() {
            @Override
            public void onReadyRecyclerDataItem(RecyclerDataItem recyclerDataItem) {
              try {
                progressBar.setVisibility(GONE);
                requestLayout();
                if (null != recyclerDataItem) {
                  if (null != curRecyclerDataItem) {
                    if (curRecyclerDataItem.str1.equals(recyclerDataItem.str1)
                        && curRecyclerDataItem.str2.equals(recyclerDataItem.str2)) {
                      curRecyclerDataItem = recyclerDataItem;
                      detailView.setData(recyclerDataItem);
                    }
                  }
                } else {
                  SpecTheme.iActivity.showMessage(
                      SpecTheme.context.getString(R.string.err_load));
                }
              } catch (Exception e) {
                Log.e(TAG, "onReadyRecyclerDataItem()", e);
              }
            }
          }
      );
    }
  }

  void onDELETEClick() {
    ILocalDB localDB = SpecTheme.iActivity.getLocalDB();
    if (null != localDB && null != curRecyclerDataItem) {
      localDB.deleteGithub(curRecyclerDataItem.str1,
          curRecyclerDataItem.str2);
    }
    SpecTheme.iActivity.goBack();
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = MeasureSpec.getSize(heightMeasureSpec);
    int widht = MeasureSpec.getSize(widthMeasureSpec);

    /* Выделим место под кнопки снизу */
    if (null != controlsView) {
      measureChildWithMargins(controlsView, widthMeasureSpec, 0,
          heightMeasureSpec, 0);
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(
          height - controlsView.getMeasuredHeight(), MeasureSpec.AT_MOST);
    }

    if (VISIBLE == progressBar.getVisibility()) {
      int size = MeasureSpec.makeMeasureSpec(
          SpecTheme.dpProgress, MeasureSpec.EXACTLY);
      measureChildWithMargins(progressBar, size, 0,
          size, 0);
    }

    measureChildWithMargins(scrollView, widthMeasureSpec, 0,
        heightMeasureSpec, 0);

    /* Скажем наверх насколько мы большие */
    setMeasuredDimension(widht, height);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    int width = right - left;
    int height = bottom - top;
    if (null != controlsView) {
      int h = controlsView.getMeasuredHeight();
      controlsView.layout(0, height - h, width, height);
      height -= h;
    }
    scrollView.layout(0, 0, width, height);
    if (VISIBLE == progressBar.getVisibility()) {
      int centerX = (right - left) >> 1;
      int centerY = (bottom - top) >> 1;
      progressBar.layout(centerX - SpecTheme.dpProgressHalf,
          centerY - SpecTheme.dpProgressHalf,
          centerX + SpecTheme.dpProgressHalf,
          centerY + SpecTheme.dpProgressHalf);
    }
  }

}

package com.bond.testgithub.ui.frags;

import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bond.testgithub.R;
import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.IMainViewFrag;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISearchControl;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.FragmentKey;
import com.bond.testgithub.ui.widgets.WidRecyclerView;

public class MainView2Tabs extends FrameLayout
    implements IMainViewFrag, ISearchControl {
  public static final String TAG = "MainView2Tabs";
  FragmentKey  fragmentKey;
  View mainView;
  android.support.design.widget.TextInputEditText searchEdit;
  IRecyclerDataManager curRecyclerDataManager = null;
  FloatingActionButton fab;
  android.support.design.widget.AppBarLayout appBarLayout;
  ViewPager viewPager;

  public MainView2Tabs(Context context)  {
    super(context);
    fragmentKey = new FragmentKey(TAG);
    LayoutInflater inflater = LayoutInflater.from(context);
    mainView = (View)inflater.inflate(R.layout.activity_tab, null);
    addView(mainView, new LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT));

//    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
//      fragmentManager.popBackStack();
//    }
//    SectionsPagerAdapter sectionsPagerAdapter
//      = new SectionsPagerAdapter(
//        ((AppCompatActivity)context).getSupportFragmentManager());


    appBarLayout = mainView.findViewById(R.id.app_bar);
    searchEdit = mainView.findViewById(R.id.search_github);
    searchEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      if ((searchEdit.getInputType() & InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) != InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) {
        searchEdit.setInputType(searchEdit.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
      }
    }
    searchEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (null != SpecTheme.iActivity) {
          IUserSettings iUserSettings = SpecTheme.iActivity.getIUserSettings();
          if (null != iUserSettings) {
            iUserSettings.putString(0==pageChangeListener.getCurrentPage()?
                    StaticConsts.PARM_MainView2Tabs_searchTab0
                    : StaticConsts.PARM_MainView2Tabs_searchTab1,
                s.toString());
          }
        }
      }
    });

    viewPager = mainView.findViewById(R.id.view_pager);
    //viewPager.setAdapter(sectionsPagerAdapter);
    viewPager.setAdapter(pagerAdapter);
    viewPager.addOnPageChangeListener(pageChangeListener);

    TabLayout tabs = mainView.findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
    fab = mainView.findViewById(R.id.fab);
    Drawable play_icon = ContextCompat.getDrawable(context, R.drawable.ic_youtube_searched_for_black_24dp);
    play_icon.setColorFilter(new LightingColorFilter( 0, 0xffffffff));
    fab.setImageDrawable(play_icon);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onFABclick(view);
      }
    });
  }

  void setSearchString(IUserSettings iUserSettings) {
    if (null != iUserSettings) {
      int curTab = pageChangeListener.getCurrentPage();
      if (null == curRecyclerDataManager) {
        local_setRecyclerDataManager(curTab);
      }
      if (null != curRecyclerDataManager) {
        String search_str = iUserSettings.getString(
            0==pageChangeListener.getCurrentPage()?
                StaticConsts.PARM_MainView2Tabs_searchTab0
                : StaticConsts.PARM_MainView2Tabs_searchTab1,
            curRecyclerDataManager.getLastSearchString());
        curRecyclerDataManager.setSearchString(search_str);
        searchEdit.setText(search_str);
        curRecyclerDataManager.onScreen();
      } else {
        String search_str = iUserSettings.getString(
            0==pageChangeListener.getCurrentPage()?
                StaticConsts.PARM_MainView2Tabs_searchTab0
                : StaticConsts.PARM_MainView2Tabs_searchTab1,
            "");
        searchEdit.setText(search_str);
      }
    }
  }

  @Override
  public void setRecyclerDataManager(IRecyclerDataManager iRecyclerDataManager) {
    curRecyclerDataManager = iRecyclerDataManager;
    setSearchString(SpecTheme.iActivity.getIUserSettings());
  }

  void saveCurState(IUserSettings iUserSettings) {
    if (null != iUserSettings) {
      int curTab = pageChangeListener.getCurrentPage();
      iUserSettings.putInt(StaticConsts.PARM_MainView2Tabs_curTab,
          curTab);
      iUserSettings.putString(0==curTab?
          StaticConsts.PARM_MainView2Tabs_searchTab0
          : StaticConsts.PARM_MainView2Tabs_searchTab1
          , searchEdit.getText().toString());
    }
  }

  void onFABclick (View view) {
    if (null != curRecyclerDataManager) {
      curRecyclerDataManager.setSearchString(searchEdit.getText().toString());
      saveCurState(SpecTheme.iActivity.getIUserSettings());
    }
    Snackbar.make(view, SpecTheme.context.getText(R.string.str_search_accepted)
        , Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
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
  public void onStartMainView(IUserSettings iUserSettings) {
    if (null != iUserSettings) {
      iUserSettings.setISearchControl(this);
    }
    //Рабочий бубен против клавиатуры:
    searchEdit.clearFocus();
    //+     <android.support.design.widget.AppBarLayout
    //        android:id="@+id/app_bar"
    //        android:focusable="true"
    //        android:focusableInTouchMode="true"
    if (null != iUserSettings) {
      int curTab = iUserSettings.getInt(
          StaticConsts.PARM_MainView2Tabs_curTab, 0);
      viewPager.setCurrentItem(curTab);
      setSearchString(iUserSettings);
    }

  }

  @Override
  public void onStopMainView(IUserSettings iUserSettings) {
    saveCurState(iUserSettings);
  }

  @Override
  public void onMessageToMainView(int msgType, Object obj) {

  }

  final PagerAdapter pagerAdapter = new PagerAdapter() {
    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    private final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      String re = "";
      if (null != SpecTheme.context) {
        re = SpecTheme.context.getResources().getString(TAB_TITLES[position]);
      }
      return re;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      WidRecyclerView root = new WidRecyclerView(SpecTheme.context);
      IRecyclerDataManager iRecyclerDataManager =
          0 == position? SpecTheme.iActivity.getGlobalDB()
          : SpecTheme.iActivity.getLocalDB().getIRecyclerDataManager();
      root.setIRecyclerDataManager(iRecyclerDataManager);
      //root.setBackgroundColor(0 == position?SpecTheme.SLightColor:SpecTheme.PColor);
      container.addView(root,
          new LayoutParams(LayoutParams.MATCH_PARENT,
              LayoutParams.MATCH_PARENT));
      return root;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
      ((ViewPager) collection).removeView((View) view);
    }

  } ;

//  final ViewPager.SimpleOnPageChangeListener pageChangeListener
//      = new ViewPager.SimpleOnPageChangeListener() {
//    private int currentPage;
//
//    @Override
//    public void onPageSelected(int position) {
//      currentPage = position;
//      if (null != SpecTheme.iActivity) {
//        if (0 == position) {
//          setRecyclerDataManager(SpecTheme.iActivity.getGlobalDB());
//        } else {
//          setRecyclerDataManager(SpecTheme.iActivity.getLocalDB().getIRecyclerDataManager());
//        }
//      }
//      return;
//    }
//
//    public final int getCurrentPage() {
//      return currentPage;
//    }
//  };

  void local_setRecyclerDataManager(int id) {
    if (null != SpecTheme.iActivity) {
      if (0 == id) {
        setRecyclerDataManager(SpecTheme.iActivity.getGlobalDB());
      } else {
        setRecyclerDataManager(SpecTheme.iActivity.getLocalDB().getIRecyclerDataManager());
      }
    }
    return;
  }

  final LocalOnPageChangeListener pageChangeListener
      = new LocalOnPageChangeListener();

  class LocalOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
    private int currentPage = 0;

    @Override
    public void onPageSelected(int position) {
      currentPage = position;
      local_setRecyclerDataManager(position);
      return;
    }

    public final int getCurrentPage() {
      return currentPage;
    }
  }
}

package com.bond.testgithub.ui.main;


import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bond.testgithub.R;
import com.bond.testgithub.ui.SpecTheme;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
//public class SectionsPagerAdapter extends FragmentPagerAdapter {
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

  @StringRes
  private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

  public SectionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Parcelable saveState()
  {
    return null;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    return PlaceholderFragment.newInstance(position + 1);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    String re = "";
    if (null != SpecTheme.context) {

      re = SpecTheme.context.getResources().getString(TAB_TITLES[position]);
    }
    return re;
  }

  @Override
  public int getCount() {
    // Show 2 total pages.
    return 2;
  }


}
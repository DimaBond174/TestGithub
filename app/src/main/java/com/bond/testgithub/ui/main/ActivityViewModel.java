package com.bond.testgithub.ui.main;


import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import com.bond.testgithub.i.IAuth;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.IUserSettings;


public class ActivityViewModel extends ViewModel {
  public Bundle savedInstanceState  =  null;
  public IAuth iAuth = null;
  public ILocalDB iLocalDB = null;
  public IRecyclerDataManager iGlobalDB = null;
  public IUserSettings iUserSettings = null;

//
//  private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
//  private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
//    @Override
//    public String apply(Integer input) {
//      return "Hello world from section: " + input;
//    }
//  });
//
//  public void setIndex(int index) {
//    mIndex.setValue(index);
//  }
//
//  public LiveData<String> getText() {
//    return mText;
//  }
}
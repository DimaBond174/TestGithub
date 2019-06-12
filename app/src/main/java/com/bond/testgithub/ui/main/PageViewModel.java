package com.bond.testgithub.ui.main;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.bond.testgithub.i.IRecyclerDataManager;

public class PageViewModel extends ViewModel {

  private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
  private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
    @Override
    public String apply(Integer input) {
      return "Hello world from section: " + input;
    }
  });


  public void setIndex(int index) {
    mIndex.setValue(index);
  }

  public int getIndex() {
    return mIndex.getValue();
  }

  public LiveData<String> getText() {
    return mText;
  }
}
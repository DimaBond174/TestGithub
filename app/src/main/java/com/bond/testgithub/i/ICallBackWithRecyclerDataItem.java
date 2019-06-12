package com.bond.testgithub.i;

import com.bond.testgithub.objs.RecyclerDataItem;

/*
* Коллбек для тех кто долго данные готовит
* */
public interface ICallBackWithRecyclerDataItem {
  void onReadyRecyclerDataItem(RecyclerDataItem  recyclerDataItem);
}

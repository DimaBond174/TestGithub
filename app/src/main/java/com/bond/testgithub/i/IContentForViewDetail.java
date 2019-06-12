package com.bond.testgithub.i;

import android.view.View;

import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.main.ActivityViewModel;

public interface IContentForViewDetail {
  /**
   * Выдать данные для демонстрации
   * @return
   */
  RecyclerDataItem getRecyclerDataItem();

  /**
   * Выдать кнопки для управления
    * @return
   */
  View getControls();

  /**
   * Синхронизация с жизненным циклом
   * @param activityViewModel
   */
  void  onSaveMainView(ActivityViewModel activityViewModel);
  void  onRestoreMainView(ActivityViewModel activityViewModel);
}

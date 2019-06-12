package com.bond.testgithub.i;

import com.bond.testgithub.objs.RecyclerDataItem;

/**
 *  Поставщик данных для RVAdapter
 *  + обработчик результата выбора
 */
public interface IRecyclerDataManager {
  /**
   * Интерфейс извещения об изменениях
   * @param observer
   */
  void registerSimpleObserver(ISimpleObserver observer);

  /**
   * Количество элементов в списке
   * @return
   */
  int getItemCount();

  /**
   * Получение элемента для позиции
   * @param position
   * @return
   */
  RecyclerDataItem getItemAtPos(int position);

  /**
   * Перезагрузка элемента
   * @param old_item
   * @param callBackWithRecyclerDataItem
   */
  void reloadItem(RecyclerDataItem old_item,
      ICallBackWithRecyclerDataItem callBackWithRecyclerDataItem) ;

  /**
   * Обработчик выбора элемента
   */
  void onClickItem(Object object);

  /**
   * Настройка строки поиска
   * @return
   */
  String getLastSearchString();
  void setSearchString(String query);

  /**
   * Убедиться что есть что показывать,
   * если это необходимо: например в локальную базу
   * можно сходить без запроса пользователя:
    */
  void onScreen();
}

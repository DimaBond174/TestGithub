package com.bond.testgithub.i;

import android.view.View;

public interface ILocalDB {
  /**
   *  Получение интерфейса для демострации данных на экране:
   * @return
   */
  IRecyclerDataManager getIRecyclerDataManager();

  /**
   * Установка схемы/СУБД/файла - хранилища для текущего юзера
   * @param login
   */
  void setCurScheme(String login);

  void onDestroy();

  boolean existGithub(String owner, String repo);
  void insertGithub(String owner, String repo, String url, String json);
  void updateGithub(String owner, String repo, String url, String json);
  void deleteGithub(String owner, String repo);
}

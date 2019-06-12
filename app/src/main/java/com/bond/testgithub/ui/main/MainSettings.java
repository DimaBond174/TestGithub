package com.bond.testgithub.ui.main;

import android.content.Context;

import com.bond.testgithub.content.github.GitHubDataManager;
import com.bond.testgithub.content.local.LocalGitDataManager;
import com.bond.testgithub.i.IAuth;
import com.bond.testgithub.i.ILocalDB;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.ui.auth.google.AuthGoogle;

/**
 * В этом классе все основные настройки приложения
 * Сейчас HardCode заглушка.
 * TODO сделать UI редактирвоания настроек:
 *  - метод аутентификации
 *  - максимальный объём подгружаемых сообщений с Github
 *  - максимальный хранимый объём на диске
 *  ..
 */
public class MainSettings {
  public static IAuth getAuth(Context context) {
    //TODO switch согласно юзерским предпочтениям какой метод:
    return new AuthGoogle(context);
  }

  public static ILocalDB getLocalDB() {
    //TODO switch согласно юзерским предпочтениям какая СУБД:
    return new LocalGitDataManager();
  }

  public static IRecyclerDataManager getGitHubManager() {
    return new GitHubDataManager();
  }

}

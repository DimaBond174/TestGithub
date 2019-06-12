package com.bond.testgithub.i;

import android.content.Context;
import android.content.Intent;

/**
 * Интерфейс модуля аутентификации
 */
public interface IAuth {
  /**
   *  Есть ли смысл продолжать с текущей сессией аутентификации
   Возвращаясь к работе всегда проверяем валидность сессии.
   Если у модуля аутентификации есть возможность проверить
   сессию скрытно, то он должен вернуть true
   (если позднее у модуля возникнут проблемы,
   то он через IAuthCallback заблокирует работу и выведет Logon UI
   * @param iAuthCallback
   * @return = true - то можно работать пока проверяю
   *   false - гарантированно надо перелогиниться, например по таймауту
   */
  boolean isAuthStillValid(Context  context, IAuthCallback  iAuthCallback);

  /**
   * У каждого модуля аутентификации может быть свой UI..
   * или не быть.
   * Модуль может пережить пересоздание Activity ( он во ViewModel),
   * а вот UI уже был прикручен к той старой Activity и для каждой
   * нужен новый. Новый UI брать отсюда:
   * @return
   */
  IMainViewFrag  getNewUI(Context  context);

  /**
   * Если модуль заказывал ЭТО
   * то должно вернуть true - иначе обработка уйдёт в super
   * @param requestCode
   * @param resultCode
   * @param data
   * @return
   */
  boolean  onActivityResultMainView(int requestCode,
    int resultCode, Intent data);

  String getUserLogin();
}

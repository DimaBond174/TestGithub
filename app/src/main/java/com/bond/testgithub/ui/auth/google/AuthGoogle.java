package com.bond.testgithub.ui.auth.google;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.IAuth;
import com.bond.testgithub.i.IAuthCallback;
import com.bond.testgithub.i.IMainViewFrag;
import com.bond.testgithub.ui.main.MainSettings;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AuthGoogle implements IAuth {
  static final String TAG  = "AuthGoogle";
  GoogleSignInClient mGoogleSignInClient;
  GoogleSignInAccount account = null;
  volatile int auth_status_code = CommonStatusCodes.API_NOT_CONNECTED;

  public AuthGoogle(Context context)  {
    // [START configure_signin]
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    //https://androidclarified.com/google-signin-android-example/
    //https://support.google.com/googleplay/android-developer/answer/7384423?hl=en-GB
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(MainSettings.getAuth_api_token())
            .requestEmail()
            .build();
    // [END configure_signin]
    // [START build_client]
    // Build a GoogleSignInClient with the options specified by gso.
    mGoogleSignInClient = GoogleSignIn.getClient(
        context, gso);
    // [END build_client]

  }


  @Override
  public boolean isAuthStillValid(Context context, IAuthCallback iAuthCallback) {
    //    https://stackoverflow.com/questions/34900956/silent-sign-in-to-retrieve-token-with-googleapiclient
    account = GoogleSignIn.getLastSignedInAccount(context);
    if (null != account) {
      return true;
    }
    return false;
  }

  @Override
  public IMainViewFrag getNewUI(Context context) {
    return new MainViewGoogleAuth(context, this);
  }

  // [START signIn]
  void signIn(AppCompatActivity context) {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    context.startActivityForResult(signInIntent, StaticConsts.RQS_AUTH);
  }
  // [END signIn]

  // [START signOut]
   void signOut(AppCompatActivity context,
      OnCompleteListener<Void> callback) {
    mGoogleSignInClient.signOut()
        .addOnCompleteListener(context, callback);
  }
  // [END signOut]

  // [START revokeAccess]
  void revokeAccess(AppCompatActivity  context,
      OnCompleteListener<Void> callback) {
    mGoogleSignInClient.revokeAccess()
        .addOnCompleteListener(context, callback);
  }
  // [END revokeAccess]


  @Override
  public boolean onActivityResultMainView(int requestCode, int resultCode, Intent data) {
    if (requestCode == StaticConsts.RQS_AUTH) {
      handleSignInResultI(data);
      return true;
    }
    return false;
  }

  void handleSignInResultI(Intent intent) {
    // The Task returned from this call is always completed, no need to attach
    // a listener.
    Task<GoogleSignInAccount> completedTask
        = GoogleSignIn.getSignedInAccountFromIntent(intent);
    try {
      account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      //updateUI(account);
    } catch (ApiException e) {
      account = null;
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      auth_status_code = e.getStatusCode();
      Log.w(TAG, "signInResult:failed code=" + auth_status_code);
      //updateUI(null);
    }
  }

  @Override
  public String getUserLogin() {
    String re  =  null;
    try {
      re = account.getEmail();
    } catch (Exception e) {
      Log.e(TAG, "getUserLogin()", e);
    }
    if (null == re) {
      re = "account";
    }
    return re;
  }
}

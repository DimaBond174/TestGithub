package com.bond.testgithub.ui.auth.google;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bond.testgithub.R;
import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.IMainViewFrag;
import com.bond.testgithub.i.IUserSettings;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.main.ActivityViewModel;
import com.bond.testgithub.ui.main.FragmentKey;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainViewGoogleAuth extends FrameLayout
    implements IMainViewFrag, View.OnClickListener {

  public static final String TAG = "MainViewGoogleAuth";

  FragmentKey  fragmentKey;
  TextView mStatusTextView;
  View mainView;
  AuthGoogle authGoogle;

  public MainViewGoogleAuth(Context context, AuthGoogle authGoogle) {
    super(context);
    this.authGoogle = authGoogle;
    fragmentKey = new FragmentKey(TAG);
    LayoutInflater inflater = LayoutInflater.from(context);
    mainView = (View)inflater.inflate(R.layout.activity_auth_google, null);
    addView(mainView, new LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT));

    // Views
    mStatusTextView = mainView.findViewById(R.id.status);

    // Button listeners
    //Это кнопа com.google.android.gms.common.SignInButton
    mainView.findViewById(R.id.sign_in_button).setOnClickListener(this);

    mainView.findViewById(R.id.sign_out_button).setOnClickListener(this);
    mainView.findViewById(R.id.disconnect_button).setOnClickListener(this);

    // [START customize_button]
    // Set the dimensions of the sign-in button.
    SignInButton signInButton = findViewById(R.id.sign_in_button);
    signInButton.setSize(SignInButton.SIZE_STANDARD);
    signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    // [END customize_button]
  }



  @Override
  public View getMainView() {
    return this;
  }

  @Override
  public void onStartMainView(IUserSettings iUserSettings) {
    GoogleSignInAccount account = GoogleSignIn
        .getLastSignedInAccount(SpecTheme.context);
    updateUI(account);
  }

  @Override
  public void onStopMainView(IUserSettings iUserSettings) {

  }

  @Override
  public boolean onActivityResultMainView(int requestCode, int resultCode, Intent data) {
    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    try {
      if (requestCode == StaticConsts.RQS_AUTH) {
        authGoogle.handleSignInResultI(data);
        if (null == authGoogle.account) {
          updateUI(null);
          handleSignError();
        } else {
          updateUI(authGoogle.account);
        }
        return true;
      }
    } catch (Exception e) {
      Log.e(TAG, "onActivityResultMainView() error: ", e);
    }
    return false;
  }


  // [START handleSignInResult]
  private void handleSignError() {
    String str;
    switch (authGoogle.auth_status_code) {
      case CommonStatusCodes.API_NOT_CONNECTED:
        str = SpecTheme.context.getString(R.string.auth_not_connected);
        break;
      case CommonStatusCodes.DEVELOPER_ERROR:
        str = SpecTheme.context.getString(R.string.auth_developer_err);
        break;
       default:
         str = SpecTheme.context.getString(R.string.auth_default_err)
             + String.valueOf(authGoogle.auth_status_code);
         break;
    }
    SpecTheme.iActivity.showMessage(str);
  }
  // [END handleSignInResult]


  private void updateUI(@Nullable GoogleSignInAccount account) {
    if (account != null) {
      mStatusTextView.setText(
          SpecTheme.context.getString(
              R.string.signed_in_fmt, account.getDisplayName()));

      mainView.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
      mainView.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
    } else {
      mStatusTextView.setText(R.string.signed_out);

      mainView.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
      mainView.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
    }
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.sign_in_button:
        authGoogle.signIn(((AppCompatActivity)SpecTheme.context));
        break;
      case R.id.sign_out_button:
        authGoogle.signOut(((AppCompatActivity)SpecTheme.context)
            , new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            // [START_EXCLUDE]
            updateUI(null);
            // [END_EXCLUDE]
          }
        });
        break;
      case R.id.disconnect_button:
        authGoogle.revokeAccess(((AppCompatActivity)SpecTheme.context)
            , new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
              }
            });
        break;
    }
  }

  @Override
  public FragmentKey getFragmentKey() {
    return fragmentKey;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildWithMargins(mainView,
        widthMeasureSpec, 0,
        heightMeasureSpec, 0);
    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    //super.onLayout(changed, left, top, right, bottom);
    mainView.layout(0, 0,  right - left,  bottom - top);
  }

  @Override
  public void onMessageToMainView(int msgType, Object obj) {

  }
}

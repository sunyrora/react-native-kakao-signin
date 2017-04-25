
package com.sunyrora.kakaosignin;

import android.app.Activity;
import android.util.Log;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.exception.KakaoException;


public class RNKaKaoSigninModule extends ReactContextBaseJavaModule {

  private static final int KAKAO_LOGIN_REQUEST = Session.AUTHORIZATION_CODE_REQUEST;
  private final ReactApplicationContext reactContext;
  private KakaoSignInManager mKakaoSignInManager;
  private static final String TAG = RNKaKaoSigninModule.class.getName();
  private Promise mLoginPromise;

  public RNKaKaoSigninModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
    this.mKakaoSignInManager = new KakaoSignInManager(reactContext);
  }

  @Override
  public String getName() {
    return "RNKaKaoSignin";
  }

  @ReactMethod
  public void signOut(final Promise promise) {
    mKakaoSignInManager.logOut(new LogoutResponseCallback() {
      @Override
      public void onCompleteLogout() {
        promise.resolve("Kakao SignOut Success");
      }
    });
  }

  @ReactMethod
  public void signIn(Promise promise) {
    mLoginPromise = promise;
    try {
      if(mKakaoSignInManager.isLoggedin()) {
        promise.resolve(mKakaoSignInManager.getAccessToken());
        return;
      }

      mKakaoSignInManager.init(new SessionCallback());
      mKakaoSignInManager.startLogin();

    } catch (Exception e) {
      promise.reject("KAKAO SIGNIN ERROR", e);
    }
  }

  private class SessionCallback implements ISessionCallback {
    @Override
    public void onSessionOpened() {
      String accessToken = mKakaoSignInManager.getAccessToken();
      if (mLoginPromise != null) {
        mLoginPromise.resolve(accessToken);
        mLoginPromise = null;
      }
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
      if(exception != null) {
        Log.e(TAG, exception.toString());
        if (mLoginPromise != null) {
          mLoginPromise.reject("KAKAO SGININ ERROR", exception.toString());
          mLoginPromise = null;
        }
      }
    }
  }

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == KAKAO_LOGIN_REQUEST) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, intent)) {
          return;
        }
        super.onActivityResult(activity, requestCode, resultCode, intent);
      }
    }
  };
}
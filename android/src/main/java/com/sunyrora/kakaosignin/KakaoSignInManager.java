package com.sunyrora.kakaosignin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.content.Context;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.helper.StoryProtocol;
import com.kakao.util.helper.TalkProtocol;
import com.kakao.util.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunyrora on 2017. 4. 21.
 */

class KakaoSignInManager {

    private final ReactApplicationContext reactContext;

    public KakaoSignInManager(ReactApplicationContext context) {
        this.reactContext = context;
    }

    public boolean isLoggedin() {

        if(KakaoSDK.getAdapter() == null) return false;

        Session currentSession = Session.getCurrentSession();
        if(currentSession == null) return false;

        return currentSession.isOpened();
    }

    public String getAccessToken() {
        return Session.getCurrentSession().getAccessToken();
    }

    protected void init(ISessionCallback sessionCallback) {
        init();
        Session.getCurrentSession().addCallback(sessionCallback);
    }

    protected void init() {
        if(KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(new KakaoAdapter() {
                @Override
                public IApplicationConfig getApplicationConfig() {
                    return new IApplicationConfig() {
                        @Override
                        public Context getApplicationContext() {
                            return reactContext.getBaseContext();
                        }
                    };
                };
            });
        }
    }

    protected void logOut(LogoutResponseCallback callback) {
        if(callback== null) {
            callback = new LogoutResponseCallback() {
              @Override
              public void onCompleteLogout() {
                Log.e("LOGOUT", "Kakao Logout Success");
              }
            };
        }

        UserManagement.requestLogout(callback);
    }

    protected void startLogin() {
        init();

        // 카톡 또는 카스가 존재하면 옵션을 보여주고, 존재하지 않으면 바로 직접 로그인창.
        final List<AuthType> authTypes = getAuthTypes();
        if(authTypes.size() == 1){
            Session.getCurrentSession().open(authTypes.get(0), reactContext.getCurrentActivity());
        } else {
            startLoginWithAuthTypes(authTypes);
        }
    }

    private List<AuthType> getAuthTypes() {
        Context context = reactContext.getBaseContext();
        final List<AuthType> availableAuthTypes = new ArrayList<AuthType>();
        if(TalkProtocol.existCapriLoginActivityInTalk(context, Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            availableAuthTypes.add(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN);
        }
        if(StoryProtocol.existCapriLoginActivityInStory(context, Session.getCurrentSession().isProjectLogin())){
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }
        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);

        final AuthType[] selectedAuthTypes = Session.getCurrentSession().getAuthTypes();
        availableAuthTypes.retainAll(Arrays.asList(selectedAuthTypes));

        // 개발자가 설정한 것과 available 한 타입이 없다면 직접계정 입력이 뜨도록 한다.
        if(availableAuthTypes.size() == 0){
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }
        return availableAuthTypes;
    }

    private void startLoginWithAuthTypes(final List<AuthType> authTypes){
        Activity activity = reactContext.getCurrentActivity();

        final List<Item> itemList = new ArrayList<Item>();
        if(authTypes.contains(AuthType.KAKAO_TALK)) {
            itemList.add(new Item(R.string.com_kakao_kakaotalk_account, R.drawable.kakaotalk_icon, R.string.com_kakao_kakaotalk_account_tts, AuthType.KAKAO_TALK));
        }
        if(authTypes.contains(AuthType.KAKAO_STORY)) {
            itemList.add(new Item(R.string.com_kakao_kakaostory_account, R.drawable.kakaostory_icon, R.string.com_kakao_kakaostory_account_tts, AuthType.KAKAO_STORY));
        }
        if(authTypes.contains(AuthType.KAKAO_ACCOUNT)){
            itemList.add(new Item(R.string.com_kakao_other_kakaoaccount, R.drawable.kakaoaccount_icon, R.string.com_kakao_other_kakaoaccount_tts, AuthType.KAKAO_ACCOUNT));
        }
        itemList.add(new Item(R.string.com_kakao_account_cancel, 0, R.string.com_kakao_account_cancel_tts, null)); //no icon for this one

        final Item[] items = itemList.toArray(new Item[itemList.size()]);

        final ListAdapter adapter = new ArrayAdapter<Item>(
                activity,
                android.R.layout.select_dialog_item,
                android.R.id.text1, items){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                tv.setText(items[position].textId);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(15);
                tv.setGravity(Gravity.CENTER);
                tv.setContentDescription(getContext().getString(items[position].contentDescId));
                if(position == itemList.size() -1) {
                    tv.setBackgroundResource(R.drawable.kakao_cancel_button_background);
                } else {
                    tv.setBackgroundResource(R.drawable.kakao_account_button_background);
                }
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                DisplayMetrics dm = new DisplayMetrics();
                reactContext.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int dp5 = (int) (5 * dm.density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };


        new AlertDialog.Builder(activity)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int position) {
                        final AuthType authType = items[position].authType;
                        if (authType != null) {
                            Session.getCurrentSession().open(authType, reactContext.getCurrentActivity());
                        }

                        dialog.dismiss();
                    }
                }).create().show();

    }

    private static class Item {
        public final int textId;
        public final int icon;
        public final int contentDescId;
        public final AuthType authType;
        public Item(final int textId, final Integer icon, final int contentDescId, final AuthType authType) {
            this.textId = textId;
            this.icon = icon;
            this.contentDescId = contentDescId;
            this.authType = authType;
        }
    }
}

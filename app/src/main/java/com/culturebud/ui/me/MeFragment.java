package com.culturebud.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.BaseFragment;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.MeContract;
import com.culturebud.presenter.MePresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(MePresenter.class)
public class MeFragment extends BaseFragment<MeContract.Presenter> implements MeContract.View,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout srlRefresh;
    private Button btnLogin;
    private View userInfoView;
    private TextView tvNick, tvDesc;
    private SimpleDraweeView sdvFace;
    private SettingItemView sivFriends, sivCollect, sivMsg, sivFeedback, sivAbout, sivSetting;
    private RelativeLayout rlMe;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.me);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        rlMe = (RelativeLayout) view.findViewById(R.id.rl_me);
        sivFriends = (SettingItemView) view.findViewById(R.id.siv_my_friends);
        sivCollect = (SettingItemView) view.findViewById(R.id.siv_my_favorite);
        sivMsg = (SettingItemView) view.findViewById(R.id.siv_my_msg);
        sivFeedback = (SettingItemView) view.findViewById(R.id.siv_feelback);
        sivAbout = (SettingItemView) view.findViewById(R.id.siv_about);
        sivSetting = (SettingItemView) view.findViewById(R.id.siv_settings);

        btnLogin = (Button) view.findViewById(R.id.btn_login);
        userInfoView = view.findViewById(R.id.ll_user_info);
        tvNick = (TextView) view.findViewById(R.id.tv_nick);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        sdvFace = (SimpleDraweeView) view.findViewById(R.id.sdv_face);

        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            try {
                mUser = user.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            showUser(user);
        }

        setListeners();
        return view;
    }

    private void setListeners() {
        srlRefresh.setOnRefreshListener(this);
        rlMe.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        sivFriends.setOnClickListener(this);
        sivCollect.setOnClickListener(this);
        sivMsg.setOnClickListener(this);
        sivFeedback.setOnClickListener(this);
        sivAbout.setOnClickListener(this);
        sivSetting.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle("我");
        User user = BaseApp.getInstance().getUser();
        if (user == null || (user != null && !user.equals(mUser))) {
            showUser(user);
        }
        if (user != null) {
            sdvFace.setImageURI(user.getAvatar());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_me: {
                if (BaseApp.getInstance().getUser() != null) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    presenter.login();
                }
                break;
            }
            case R.id.btn_login:
                presenter.login();
                break;
            case R.id.siv_my_friends: {
                startActivity(new Intent(getActivity(), MyFriendsActivity.class));
                break;
            }
            case R.id.siv_my_favorite: {
                startActivity(new Intent(getActivity(), MyFavoritesActivity.class));
                break;
            }
            case R.id.siv_my_msg: {
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
                break;
            }
            case R.id.siv_feelback: {
                if (BaseApp.getInstance().getUser() == null) {
                    onToLogin();
                    return;
                }
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.siv_about: {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.siv_settings: {
                if (BaseApp.getInstance().getUser() == null) {
                    onToLogin();
                    return;
                }
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void showLoginPage() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    public void showLoginButton() {
        userInfoView.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUser(User user) {
        if (user != null) {
            btnLogin.setVisibility(View.GONE);
            userInfoView.setVisibility(View.VISIBLE);
            tvNick.setText(user.getNickname());
            tvDesc.setText(user.getAutograph());
            sdvFace.setImageURI(user.getAvatar());

            if (user.getSex() == 1) {
                Drawable drawable = getResources().getDrawable(R.mipmap.setting_me_female_icon);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvNick.setCompoundDrawables(null, null, drawable, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.mipmap.setting_me_male_icon);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvNick.setCompoundDrawables(null, null, drawable, null);
            }
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            userInfoView.setVisibility(View.GONE);
            tvNick.setText("");
            tvDesc.setText("");
            //直接使用本地的图标，不用去服务器下载默认图标
            Uri defaultUri = Uri.parse("res:///" + R.mipmap.me_default_icon);
            sdvFace.setImageURI(defaultUri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra("res", false)) {
                        try {
                            mUser = BaseApp.getInstance().getUser().clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        presenter.processLoginResult(BaseApp.getInstance().getUser());
                    }
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        User user = BaseApp.getInstance().getUser();
        if (user == null || (user != null && !user.equals(mUser))) {
            showUser(user);
        }
    }

    @Override
    public void onRefresh() {
        srlRefresh.setRefreshing(false);
    }
}

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
import android.widget.LinearLayout;
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
import com.culturebud.util.ShareHelper;
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
    private LinearLayout llConcer;
    private TextView tvConcern;
    private LinearLayout llFan;
    private TextView tvFan;
    private SettingItemView sivCollect, sivMsg, sivFeedback, sivAbout, sivSetting, sivinviteFriend;
    private RelativeLayout rlMe;

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

        llConcer = (LinearLayout) view.findViewById(R.id.ll_concer);
        llFan = (LinearLayout) view.findViewById(R.id.ll_fan);
        tvConcern = (TextView) view.findViewById(R.id.tv_concernNum);
        tvFan = (TextView) view.findViewById(R.id.tv_fanNum);

        sivCollect = (SettingItemView) view.findViewById(R.id.siv_my_favorite);
        sivMsg = (SettingItemView) view.findViewById(R.id.siv_my_msg);
        sivFeedback = (SettingItemView) view.findViewById(R.id.siv_feelback);
        sivAbout = (SettingItemView) view.findViewById(R.id.siv_about);
        sivSetting = (SettingItemView) view.findViewById(R.id.siv_settings);
        sivinviteFriend = (SettingItemView) view.findViewById(R.id.invitefriend);

        btnLogin = (Button) view.findViewById(R.id.btn_login);
        userInfoView = view.findViewById(R.id.ll_user_info);
        tvNick = (TextView) view.findViewById(R.id.tv_nick);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        sdvFace = (SimpleDraweeView) view.findViewById(R.id.sdv_face);

        setListeners();

        return view;
    }

    private void setListeners() {
        srlRefresh.setOnRefreshListener(this);
        rlMe.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        llConcer.setOnClickListener(this);
        llFan.setOnClickListener(this);

        sivCollect.setOnClickListener(this);
        sivMsg.setOnClickListener(this);
        sivFeedback.setOnClickListener(this);
        sivAbout.setOnClickListener(this);
        sivSetting.setOnClickListener(this);
        sivinviteFriend.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle(getString(R.string.me));
        presenter.processUser(BaseApp.getInstance().getUser());
    }


    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId != R.id.siv_about && viewId != R.id.invitefriend) {
            if (BaseApp.getInstance().getUser() == null) {
                onToLogin();
                return;
            }
        }

        switch (viewId) {
            case R.id.rl_me: {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login:
                presenter.login();
                break;
            case R.id.ll_concer: {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("is_concern", true);
                intent.putExtra("title", getString(R.string.my_concern_pagetitle));
                startActivity(intent);
                break;
            }
            case R.id.ll_fan: {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("is_concern", false);
                intent.putExtra("title", getString(R.string.my_concerned_pagetitle));
                startActivity(intent);
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
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.invitefriend: {
                ShareHelper.share(getActivity(), getString(R.string.invitefriend_title), getString(R.string.invitefriend_content), CommonConst.APPDOWNLOADQRCODE_IMAGEURL, null);
                break;
            }
        }
    }

    @Override
    public void showLoginUser(User user) {
        btnLogin.setVisibility(View.GONE);
        userInfoView.setVisibility(View.VISIBLE);

        tvNick.setText(user.getNickname());
        tvDesc.setText(user.getAutograph());
        sdvFace.setImageURI(user.getAvatar());

        tvConcern.setText(String.valueOf(user.getConcernNum()));
        tvFan.setText(String.valueOf(user.getFanNum()));

        if (user.getSex() == 1) {
            Drawable drawable = getResources().getDrawable(R.mipmap.setting_me_female_icon);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvNick.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.setting_me_male_icon);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvNick.setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override
    public void showLoginOut() {
        btnLogin.setVisibility(View.VISIBLE);
        userInfoView.setVisibility(View.GONE);
        tvNick.setText("");
        tvDesc.setText("");
        Uri defaultUri = Uri.parse("res:///" + R.mipmap.me_default_icon);
        sdvFace.setImageURI(defaultUri);
        tvConcern.setText("0");
        tvFan.setText("0");
    }

    @Override
    public void showLoginPage() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra("res", false)) {
                        presenter.processUser(BaseApp.getInstance().getUser());
                    }
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        presenter.processUser(BaseApp.getInstance().getUser());
    }

    @Override
    public void onRefresh() {
        srlRefresh.setRefreshing(false);
    }
}

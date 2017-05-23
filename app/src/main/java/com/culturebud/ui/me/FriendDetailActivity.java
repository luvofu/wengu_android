package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.FriendDetailContract;
import com.culturebud.presenter.FriendDetailPresenter;
import com.culturebud.ui.bhome.UserBookHomeActivity;
import com.culturebud.util.ClassUtil;
import com.culturebud.util.SystemParameterUtil;
import com.culturebud.widget.FormItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ADD_FRIEND;

/**
 * Created by XieWei on 2016/12/27.
 */

@PresenterInject(FriendDetailPresenter.class)
public class FriendDetailActivity extends BaseActivity<FriendDetailContract.Presenter> implements FriendDetailContract.View {
    private SimpleDraweeView sdvBg, sdvFace;
    private TextView tvNick;
    private FormItemView fivSex, fivRegion, fivSign;
    private Button btnEnterBookHome, btnAddFriend;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);
        presenter.setView(this);
        sdvBg = obtainViewById(R.id.sdv_bg);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick);
        fivSex = obtainViewById(R.id.fiv_sex);
        fivRegion = obtainViewById(R.id.fiv_region);
        fivSign = obtainViewById(R.id.fiv_sign);
        btnEnterBookHome = obtainViewById(R.id.btn_enter_book_home);
        btnAddFriend = obtainViewById(R.id.btn_add_friend);

        View view = obtainViewById(R.id.toolbarContent);
        int topMargin = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            int statusBarheight = SystemParameterUtil.getStatusHeight(this);
            topMargin = statusBarheight;
        }
        ClassUtil.setMargins(view, 0, topMargin, 0, 0);

        initData();
    }

    private void initData() {
        long userId = getIntent().getLongExtra("user_id", -1);
        presenter.getFriendDetail(userId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bcback:
                finish();
                break;
            case R.id.btn_add_friend: {
                if (user == null) {
                    return;
                }
                Intent intent = new Intent(this, InviteFriendActivity.class);
                intent.putExtra("user_id", user.getUserId());
                startActivityForResult(intent, REQUEST_CODE_ADD_FRIEND);
                break;
            }
            case R.id.btn_enter_book_home: {
                if (user == null) {
                    return;
                }
                Intent intent = new Intent(this, UserBookHomeActivity.class);
                intent.putExtra("user_id", user.getUserId());
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onFriend(User user) {
        this.user = user;
        if (!TextUtils.isEmpty(user.getBackground())) {
            sdvBg.setImageURI(user.getBackground());
        }
        if (!TextUtils.isEmpty(user.getAvatar())) {
            sdvFace.setImageURI(user.getAvatar());
        }
        if (!TextUtils.isEmpty(user.getNickname())) {
            tvNick.setText(user.getNickname());
        }
        fivSex.setContent(user.getSex() == 0 ? "男" : "女");
        String province = TextUtils.isEmpty(user.getProvince()) ? "" : user.getProvince();
        String city = TextUtils.isEmpty(user.getCity()) ? "" : user.getCity();
        String county = TextUtils.isEmpty(user.getCounty()) ? "" : user.getCounty();
        fivRegion.setContent(province + " " + city + " " + county);
        fivSign.setContent(user.getAutograph());
        if (user.getRelationType() == CommonConst.RelationType.STRANGER) {
            btnAddFriend.setVisibility(View.VISIBLE);
        } else {
            btnAddFriend.setVisibility(View.GONE);
        }
    }
}

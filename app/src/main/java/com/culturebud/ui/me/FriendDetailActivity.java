package com.culturebud.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.FriendDetailContract;
import com.culturebud.presenter.FriendDetailPresenter;
import com.culturebud.widget.FormItemView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by XieWei on 2016/12/27.
 */

@PresenterInject(FriendDetailPresenter.class)
public class FriendDetailActivity extends BaseActivity<FriendDetailContract.Presenter> implements FriendDetailContract.View {
    private ImageView ivBack;
    private SimpleDraweeView sdvBg, sdvFace;
    private TextView tvNick;
    private FormItemView fivSex, fivRegion, fivSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);
        presenter.setView(this);
        ivBack = obtainViewById(R.id.iv_back);
        sdvBg = obtainViewById(R.id.sdv_bg);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick);
        fivSex = obtainViewById(R.id.fiv_sex);
        fivRegion = obtainViewById(R.id.fiv_region);
        fivSign = obtainViewById(R.id.fiv_sign);
        ivBack.setOnClickListener(this);

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
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onFriend(User user) {
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
        fivRegion.setContent(user.getProvince() + " " + user.getCity());
        fivSign.setContent(user.getTag());
    }
}

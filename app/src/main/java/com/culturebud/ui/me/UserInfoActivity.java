package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.UserInfoContract;
import com.culturebud.presenter.UserInfoPresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_EMAIL;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_NICK;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_PROFILE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;

/**
 * Created by XieWei on 2016/11/2.
 */

@PresenterInject(UserInfoPresenter.class)
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter>
        implements UserInfoContract.View, OptionsPickerView.OnOptionsSelectListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();
    private SimpleDraweeView sdvFace;
    private LinearLayout llFace;
    private SettingItemView sivNick, sivCulturebudName, sivSex, sivEmail, sivRegion, sivProfile;
    private OptionsPickerView<String> opvSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        presenter.setView(this);
        showTitlebar();
        llFace = obtainViewById(R.id.ll_face);
        sdvFace = obtainViewById(R.id.sdv_face);
        sivNick = obtainViewById(R.id.siv_nick);
        sivCulturebudName = obtainViewById(R.id.siv_culturebud_name);
        sivSex = obtainViewById(R.id.siv_sex);
        sivEmail = obtainViewById(R.id.siv_email);
        sivRegion = obtainViewById(R.id.siv_region);
        sivProfile = obtainViewById(R.id.siv_profile);
        setTitle(R.string.user_info);
        setListeners();

        initData();
        sivRegion.setVisibility(View.GONE);//暂隐藏
    }

    private void setListeners() {
        llFace.setOnClickListener(this);
        sivNick.setOnClickListener(this);
        sivSex.setOnClickListener(this);
        sivEmail.setOnClickListener(this);
        sivRegion.setOnClickListener(this);
        sivProfile.setOnClickListener(this);
    }

    private void initSexOpera() {
        if (opvSex == null) {
            opvSex = new OptionsPickerView(this);
            ArrayList<String> items = new ArrayList<>();
            items.add("男");
            items.add("女");
            items.add("保密");
            opvSex.setPicker(items);
            opvSex.setOnoptionsSelectListener(this);
            opvSex.setCyclic(false);
        }
    }

    private void showSexOpera() {
        initSexOpera();
        if (!opvSex.isShowing()) {
            User user = BaseApp.getInstance().getUser();
            if (user != null) {
                opvSex.setSelectOptions(user.getSex());
            }
            opvSex.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        User user = BaseApp.getInstance().getUser();
        if (null != user) {
            String avatar = user.getAvatar();
            if (!TextUtils.isEmpty(avatar)) {
                sdvFace.setImageURI(avatar);
            }
            String nick = user.getNickname();
            if (!TextUtils.isEmpty(nick)) {
                sivNick.setRightInfo(nick);
            } else {
                sivNick.setRightInfo(R.string.no_fill);
            }
            String culturebudName = user.getUserName();
            if (!TextUtils.isEmpty(culturebudName)) {
                sivCulturebudName.setRightInfo(culturebudName);
            } else {
                sivCulturebudName.setRightInfo(R.string.no_fill);
            }
            int sex = user.getSex();
            sivSex.setRightInfo(sex == 0 ? "男" : "女");
            String email = user.getMailbox();
            if (!TextUtils.isEmpty(email)) {
                sivEmail.setRightInfo(email);
            } else {
                sivEmail.setRightInfo(R.string.no_fill);
            }
            String region = user.getCity();
            if (!TextUtils.isEmpty(region)) {
                sivRegion.setRightInfo(region);
            } else {
                sivRegion.setRightInfo(R.string.no_fill);
            }
            String profile = user.getAutograph();
            if (!TextUtils.isEmpty(profile)) {
                sivProfile.setDesc(profile);
            } else {
                sivProfile.setDesc(R.string.no_fill);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_face:
                aspectX = 1;
                aspectY = 1;
                outX = 480;
                outY = 480;
                showPhotoDialog();
                break;
            case R.id.siv_nick://修改昵称
            {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("content", BaseApp.getInstance().getUser().getNickname());
                intent.putExtra("type", 0);
                startActivityForResult(intent, REQUEST_CODE_ALTER_NICK);
                break;
            }
            case R.id.siv_email://修改邮箱
            {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", "邮箱");
                intent.putExtra("content", BaseApp.getInstance().getUser().getMailbox());
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE_ALTER_EMAIL);
                break;
            }
            case R.id.siv_profile://修改个性签名
            {
                Intent intent = new Intent(this, GeneralEditorActivity.class);
                intent.putExtra("title", "修改签名");
                intent.putExtra("content", BaseApp.getInstance().getUser().getAutograph());
                intent.putExtra("type", 2);
                startActivityForResult(intent, REQUEST_CODE_ALTER_PROFILE);
                break;
            }
            case R.id.siv_sex://性别
            {
                showSexOpera();
                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvFace.setImageURI(photoUri);
                    User user = BaseApp.getInstance().getUser();
                    presenter.editAvatar(user.getUserId(), photoUri, true);
                }
                break;
            case REQUEST_CODE_ALTER_NICK: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivNick.setRightInfo(content);
                    presenter.editNick(content);
                }
                break;
            }
            case REQUEST_CODE_ALTER_EMAIL: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivEmail.setRightInfo(content);
                    presenter.editEmail(content);
                }
                break;
            }
            case REQUEST_CODE_ALTER_PROFILE: {
                if (resultCode == RESULT_OK && data.hasExtra("content")) {
                    String content = data.getStringExtra("content");
                    sivProfile.setDesc(content);
                    presenter.editAutograph(content);
                }
                break;
            }
        }
    }

    @Override
    public void onEditImg(String newUrl) {
        sdvFace.setImageURI(newUrl);
    }

    @Override
    public void onNick(String nick) {

    }

    @Override
    public void onEmail(String email) {

    }

    @Override
    public void onAutograph(String autograph) {

    }

    @Override
    public void onSex(int sex) {
        if (opvSex != null && opvSex.isShowing()) {
            opvSex.setSelectOptions(sex);
        }
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        switch (options1) {
            case 0://男
                sivSex.setRightInfo("男");
                break;
            case 1://女
                sivSex.setRightInfo("女");
                break;
            case 2://保密
                sivSex.setRightInfo("保密");
                break;
        }
        presenter.editSex(options1);
    }
}

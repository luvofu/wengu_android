package com.culturebud.ui.me;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.TextEditorFragment;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.UserInfoContract;
import com.culturebud.presenter.UserInfoPresenter;
import com.culturebud.util.TxtUtil;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_EMAIL;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_NICK;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ALTER_PROFILE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_PHOTO_CROP;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_WY_ACCOUNT;

/**
 * Created by XieWei on 2016/11/2.
 */

@PresenterInject(UserInfoPresenter.class)
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter>
        implements UserInfoContract.View, OptionsPickerView.OnOptionsSelectListener, TextEditorFragment
        .OnFragmentInteractionListener {
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
        sivCulturebudName.setOnClickListener(this);
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
                sivProfile.setRightInfo(profile);
            } else {
                sivProfile.setRightInfo(R.string.no_fill);
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
                outX = 300;
                outY = 300;
                showPhotoDialog();
                break;
            case R.id.siv_nick://修改昵称
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TextEditorFragment fragment = TextEditorFragment.newInstance(REQUEST_CODE_ALTER_NICK,
                        "昵称", BaseApp.getInstance().getUser().getNickname(),
                        "昵称", 24, CommonConst.TextEditorInputType.DEFAULT_INPUT_TYPE,
                        false, null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.contain_view, fragment, TextEditorFragment.getFragmentTag());
                fragmentTransaction.commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_email://修改邮箱
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TextEditorFragment fragment = TextEditorFragment.newInstance(REQUEST_CODE_ALTER_EMAIL,
                        "邮箱", BaseApp.getInstance().getUser().getMailbox(),
                        getString(R.string.culturebud_name), 50, CommonConst.TextEditorInputType.EMAIL_INPUT_TYPE,
                        false, null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.contain_view, fragment, TextEditorFragment.getFragmentTag());
                fragmentTransaction.commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_profile://修改个性签名
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TextEditorFragment fragment = TextEditorFragment.newInstance(REQUEST_CODE_ALTER_PROFILE,
                        "修改签名", BaseApp.getInstance().getUser().getAutograph(),
                        getString(R.string.culturebud_name), 50, CommonConst.TextEditorInputType.MULTI_LINE_INPUT_TYPE,
                        false, null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.contain_view, fragment, TextEditorFragment.getFragmentTag());
                fragmentTransaction.commit();

                hideTitlebar();

                break;
            }
            case R.id.siv_sex://性别
            {
                showSexOpera();
                break;
            }
            case R.id.siv_culturebud_name: //文芽号
            {
                /*
                    1. 判断是否已经有文芽号了，如果已经有了，不允许再修改.
                    2. 如果没有，打开输入框，需要提示文芽号的规则，需要判断文芽号是否符合规定.
                 */

                if (TextUtils.isEmpty(BaseApp.getInstance().getUser().getUserName())) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TextEditorFragment fragment = TextEditorFragment.newInstance(REQUEST_CODE_WY_ACCOUNT,
                            getString(R.string.culturebud_name), null,
                            getString(R.string.culturebud_name), 20, CommonConst.TextEditorInputType.DEFAULT_INPUT_TYPE,
                            true, getString(R.string.wy_account_rules));
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.contain_view, fragment, TextEditorFragment.getFragmentTag());
                    fragmentTransaction.commit();

                    hideTitlebar();
                }

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
    public void onBackPressed() {
        if (TextEditorFragment.isShowing(this)) {
            //移除.
            onExist();
        } else {
            finish();
        }
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
        }
    }

    @Override
    public void onEditImg(String newUrl) {
        sdvFace.setImageURI(newUrl);
    }

    @Override
    public void onNick(String nick) {
        sivNick.setRightInfo(nick);

        //退出编辑框.
        onExist();
    }

    @Override
    public void onEmail(String email) {
        sivEmail.setRightInfo(email);

        //退出编辑框.
        onExist();
    }

    @Override
    public void onAutograph(String autograph) {
        sivProfile.setRightInfo(autograph);

        //退出编辑框.
        onExist();
    }

    @Override
    public void onSex(int sex) {
        if (opvSex != null && opvSex.isShowing()) {
            opvSex.setSelectOptions(sex);
        }
    }

    @Override
    public void onUsername(String username) {
        sivCulturebudName.setRightInfo(username);

        //退出编辑框.
        onExist();
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


    @Override
    public void onConfirmSubmission(String content, int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_ALTER_NICK: {
                presenter.editNick(content);
                break;
            }
            case REQUEST_CODE_ALTER_EMAIL: {
                //判断邮箱是否合法.
                boolean isMatch = TxtUtil.isValidateEmail(content);

                if (!isMatch) {
                    onErrorTip("邮箱格式不正确");
                } else {
                    presenter.editEmail(content);
                }

                break;
            }
            case REQUEST_CODE_ALTER_PROFILE: {
                presenter.editAutograph(content);
                break;
            }
            case REQUEST_CODE_WY_ACCOUNT: {
                //判断文芽号是否符合要求.
                Boolean isMatch = TxtUtil.isMatchWenyaAccountRule(content);

                if (!isMatch) {
                    //是否需要报错.
                    onErrorTip(getString(R.string.wy_account_error_message));
                } else {
                    //提交网络请求.
                    presenter.editUsername(content);
                }
                break;
            }
        }
    }

    @Override
    public void onExist() {
        //退出.
        FragmentManager fragmentManager = getFragmentManager();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(TextEditorFragment.getFragmentTag());
        if (fragment != null) {
            //移除.
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove
                    (fragment).commit();

            //显示activity的title
            showTitlebar();
        }
    }
}

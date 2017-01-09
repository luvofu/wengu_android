package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.PublishDynamicContract;
import com.culturebud.presenter.PublishDynamicPresenter;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by XieWei on 2016/11/2.
 */

@PresenterInject(PublishDynamicPresenter.class)
public class PublishDynamicActivity extends BaseActivity<PublishDynamicContract.Presenter>
        implements OptionsPickerView.OnOptionsSelectListener, PublishDynamicContract.View {
    private static final String TAG = PublishDynamicActivity.class.getSimpleName();
    private OptionsPickerView<String> permissionOpts;
    private SettingItemView sivPermission;
    private int permission;
    private SimpleDraweeView sdvAdd;
    private ImageView ivDel;
    private EditText etContent;
    private int linkType;
    private long linkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_dynamic);
        presenter.setView(this);
        showTitlebar();
        setBgColor(R.color.litter_gray_bg_border);
        setTitle(getString(R.string.publish_dynamic));
        showBack();
        setOperasText("发布");
        showOperas();
        sivPermission = obtainViewById(R.id.siv_permission);
        sdvAdd = obtainViewById(R.id.sdv_img);
        ivDel = obtainViewById(R.id.iv_del);
        etContent = obtainViewById(R.id.et_content);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.siv_permission:
                initPermissionDialog(permission);
                if (!permissionOpts.isShowing()) {
                    permissionOpts.show();
                }
                break;
            case R.id.sdv_img:
                showPhotoDialog();
                break;
            case R.id.iv_del:
                sdvAdd.setImageURI("");
                ivDel.setVisibility(View.GONE);
                break;
        }
    }


    private void initPermissionDialog(int permission) {
        if (permissionOpts == null) {
            permissionOpts = new OptionsPickerView<>(this);
            ArrayList<String> items = new ArrayList<>();
            items.add("公开");
            items.add("好友");
            items.add("私密");
            permissionOpts.setPicker(items);
            permissionOpts.setSelectOptions(permission);
            permissionOpts.setCyclic(false);
            permissionOpts.setOnoptionsSelectListener(this);
        } else {
            permissionOpts.setSelectOptions(permission);
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        presenter.publish(etContent.getText().toString(), photoUri, permission, linkType, linkId);
    }

    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        permission = options1;
        switch (options1) {
            case 0:
                sivPermission.setRightInfo("公开");
                break;
            case 1:
                sivPermission.setRightInfo("好友");
                break;
            case 2:
                sivPermission.setRightInfo("私密");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    sdvAdd.setImageURI(photoUri);
                    ivDel.setVisibility(View.VISIBLE);
                }
                break;
            case REQUEST_CODE_PHOTO_CROP:
                if (resultCode == RESULT_OK) {
                    sdvAdd.setImageURI(photoUri);
                    ivDel.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onPublishResult(boolean result) {
        if (result) {
            setResult(RESULT_OK);
            finish();
        } else {

        }
    }
}
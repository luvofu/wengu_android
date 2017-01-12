package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.culturebud.BaseActivity;
import com.culturebud.CommonConst.ContentPermission;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.PublishDynamicContract;
import com.culturebud.presenter.PublishDynamicPresenter;
import com.culturebud.ui.search.SelectBookActivity;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by XieWei on 2016/11/2.
 */

@PresenterInject(PublishDynamicPresenter.class)
public class PublishDynamicActivity extends BaseActivity<PublishDynamicContract.Presenter>
        implements OptionsPickerView.OnOptionsSelectListener, PublishDynamicContract.View, BaseActivity.OnSoftKeyboardStateChangedListener {
    private static final String TAG = PublishDynamicActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_BOOK = 1019;
    private OptionsPickerView<String> permissionOpts;
    private SettingItemView sivPermission;
    private int permission = ContentPermission.PERMISSION_PUBLIC;
    private SimpleDraweeView sdvAdd;
    private ImageView ivDel;
    private EditText etContent;

    private LinearLayout llAddBook;
    private SimpleDraweeView sdvBookCover;
    private TextView tvBookTitle;
    private ImageView ivDelBook;

    private int linkType;
    private long linkId;
    private PopupWindow pwOperas;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_dynamic);
        presenter.setView(this);
        showTitlebar();
        setBgColor(R.color.litter_gray_bg_border);
        setTitle(R.string.publish_dynamic);
        showBack();
        setOperasText(R.string.publish);
        showOperas();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        sivPermission = obtainViewById(R.id.siv_permission);
        sdvAdd = obtainViewById(R.id.sdv_img);
        ivDel = obtainViewById(R.id.iv_del);
        etContent = obtainViewById(R.id.et_content);

        llAddBook = obtainViewById(R.id.ll_added_book);
        sdvBookCover = obtainViewById(R.id.sdv_book_cover);
        tvBookTitle = obtainViewById(R.id.tv_book_name);
        ivDelBook = obtainViewById(R.id.iv_del_book);
        if (linkType == 0) {
            llAddBook.setVisibility(View.GONE);
        } else {
            llAddBook.setVisibility(View.VISIBLE);
        }

        initPopupWindow();
        addSoftKeyboardChangedListener(this);
        etContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.et_content:
                if (!pwOperas.isShowing()) {
                    showPop();
                }
                break;
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
                photoUri = null;
                sdvAdd.setImageURI("");
                ivDel.setVisibility(View.GONE);
                break;
            case R.id.iv_at_friend:
                break;
            case R.id.iv_select_book: {
                Intent intent = new Intent(this, SelectBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_BOOK);
                break;
            }
            case R.id.iv_del_book: {
                linkType = 0;
                linkId = 0;
                llAddBook.setVisibility(View.GONE);
                break;
            }
        }
    }


    private void initPermissionDialog(int permission) {
        if (permissionOpts == null) {
            permissionOpts = new OptionsPickerView<>(this);
            ArrayList<String> items = new ArrayList<>();
            items.add(getString(R.string.permission_public));
            items.add(getString(R.string.permission_friend));
            items.add(getString(R.string.permission_personal));
            permissionOpts.setPicker(items);
            permissionOpts.setSelectOptions(permission);
            permissionOpts.setCyclic(false);
            permissionOpts.setOnoptionsSelectListener(this);
        } else {
            permissionOpts.setSelectOptions(permission);
        }
    }

    private void initPopupWindow() {
        if (pwOperas == null) {
            pwOperas = new PopupWindow(this, null, R.style.PopupWindow);
            View view = getLayoutInflater().inflate(R.layout.publish_dynamic_operas_pop, null);
            pwOperas.setContentView(view);
            pwOperas.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            pwOperas.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
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
            case ContentPermission.PERMISSION_PUBLIC:
                sivPermission.setRightInfo(R.string.permission_public);
                break;
            case ContentPermission.PERMISSION_FRIEND:
                sivPermission.setRightInfo(R.string.permission_friend);
                break;
            case ContentPermission.PERMISSION_PERSONAL:
                sivPermission.setRightInfo(R.string.permission_personal);
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
            case REQUEST_CODE_SELECT_BOOK:
                if (resultCode == RESULT_OK) {
                    long bookId = data.getLongExtra("book_id", -1);
                    String bookTitle = data.getStringExtra("book_title");
                    String bookCover = data.getStringExtra("book_cover");
                    if (bookId != -1) {
                        linkType = 1;
                        linkId = bookId;
                        llAddBook.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(bookCover)) {
                            sdvBookCover.setImageURI(bookCover);
                        }
                        if (!TextUtils.isEmpty(bookTitle)) {
                            tvBookTitle.setText(bookTitle);
                        }
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeSoftKeyboardChangedListener(this);
    }

    private void showPop() {
        View dv = getWindow().getDecorView();
        pwOperas.showAtLocation(dv, Gravity.NO_GRAVITY, 0, dv.getHeight() / 2);
    }

    @Override
    public void onSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
        if (isKeyBoardShow) {
            if (!pwOperas.isShowing()) {
                showPop();
            }
            pwOperas.update(0, screenHeight - (keyboardHeight + pwOperas.getContentView().getMeasuredHeight()),
                    pwOperas.getWidth(), pwOperas.getHeight(), true);
        } else {
            pwOperas.dismiss();
        }
    }
}
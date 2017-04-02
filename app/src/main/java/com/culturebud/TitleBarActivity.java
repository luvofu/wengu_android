package com.culturebud;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by XieWei on 2016/11/1.
 */

public abstract class TitleBarActivity extends MyAppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tvBack;
    private EditText etTitle;
    private TextView tvOperas;
    private ViewGroup vgContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_titlebar);

        vgContent = obtainViewById(R.id.content);
        toolbar = obtainViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            toolbar.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.titlebar_height_large));
        }
        tvBack = obtainViewById(R.id.tv_back);
        etTitle = obtainViewById(R.id.et_title);
        tvOperas = obtainViewById(R.id.tv_operas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initListener();
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    public void addOperaWidget(int index, View widget) {
        toolbar.addView(widget, index);
    }

    public int getTitleBarHeight() {
        return getSupportActionBar().getHeight();
    }

    public void showBack() {
        tvBack.setVisibility(View.VISIBLE);
    }

    public void hideBack() {
        tvBack.setVisibility(View.GONE);
    }

    public void showOperas() {
        tvOperas.setVisibility(View.VISIBLE);
    }

    public void hideOpears() {
        tvOperas.setVisibility(View.GONE);
    }

    public void setBgColor(@ColorRes int resId) {
        vgContent.setBackgroundResource(resId);
    }

    public void showTitlebar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideTitlebar() {
        toolbar.setVisibility(View.GONE);
    }

    public void enableSearch() {
        etTitle.setEnabled(true);
        etTitle.setFocusable(true);
        etTitle.setFocusableInTouchMode(true);
        etTitle.requestFocus();
        etTitle.findFocus();
        etTitle.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.max_search_width));
        etTitle.setMinWidth(getResources().getDimensionPixelSize(R.dimen.min_search_width));
        etTitle.setMinHeight(getResources().getDimensionPixelSize(R.dimen.min_search_height));
        etTitle.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        etTitle.setBackgroundResource(R.drawable.white_circle_bg);
        etTitle.setTextColor(getResources().getColor(R.color.title_font_default));
        Drawable left = getResources().getDrawable(R.mipmap.search_icon);
        left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        etTitle.setCompoundDrawables(left, null, null, null);
        int padding = getResources().getDimensionPixelSize(R.dimen.common_padding_small);
        etTitle.setPadding(padding, padding, padding, padding);
    }

    public void setSearchHint(CharSequence hint) {
        if (!TextUtils.isEmpty(hint)) {
            etTitle.setHint(hint);
        }
    }

    public void setOnTitleWatcher(TextWatcher textWatcher) {
        etTitle.addTextChangedListener(textWatcher);
    }

    public CharSequence getInputContent() {
        return etTitle.getText();
    }

    public void setOnTitleEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        etTitle.setOnEditorActionListener(onEditorActionListener);
    }

    public void setSearchHint(@StringRes int resId) {
        etTitle.setHint(resId);
    }

    public void disableSearch() {
        etTitle.setEnabled(false);
        etTitle.clearFocus();
        etTitle.setFocusable(false);
        etTitle.setTextColor(Color.WHITE);
        etTitle.setBackgroundResource(android.R.color.transparent);
    }

    public void setBackText(CharSequence backText) {
        if (TextUtils.isEmpty(backText)) {
            tvBack.setText("");
        } else {
            tvBack.setText(backText);
        }
    }

    public void setBackText(@StringRes int resId) {
        tvBack.setText(resId);
    }

    public void setBackTextColor(@ColorInt int color) {
        tvBack.setTextColor(color);
    }

    public void setOperasTextColor(@ColorInt int color) {
        tvOperas.setTextColor(color);
    }

    public void setOperasText(CharSequence operasText) {
        if (TextUtils.isEmpty(operasText)) {
            tvOperas.setText("");
        } else {
            tvOperas.setText(operasText);
        }
    }

    public void setOperasText(@StringRes int resId) {
        tvOperas.setText(resId);
    }

    public TextView getOperasView() {
        return tvOperas;
    }

    public void setBackDrawable(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setBackDrawable(drawable);
    }

    public void setBackDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvBack.setCompoundDrawables(drawable, null, null, null);
    }

    public void setOperasDrawable(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        setOperasDrawable(drawable);
    }

    public void setOperasDrawable(Drawable drawable) {
        if (drawable == null) {
            tvOperas.setCompoundDrawables(null, null, null, null);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvOperas.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void setBackGroundColor(int color) {
        vgContent.setBackgroundColor(color);
    }

    private void initListener() {
        etTitle.setOnClickListener(this);
        etTitle.setEnabled(true);
        etTitle.setInputType(InputType.TYPE_NULL);
        tvBack.setOnClickListener(this);
        tvOperas.setOnClickListener(this);
    }

    public void setTitle(CharSequence title) {
        etTitle.setText(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        etTitle.setText(titleId);
    }

    public void setTitleRightIcon(@DrawableRes int resId) {
        Drawable[] drawables = etTitle.getCompoundDrawables();
        Drawable right = getResources().getDrawable(resId);
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        drawables[2] = right;
        etTitle.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    @Override
    public void setTitleColor(@ColorRes int colorResId) {
        etTitle.setTextColor(getResources().getColor(colorResId));
    }

    @Override
    public void setContentView(int layoutResID) {
        View.inflate(this, layoutResID, vgContent);
    }

    @Override
    public void setContentView(View view) {
        vgContent.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        vgContent.addView(view, params);
    }

    protected void onBack() {

    }

    @CallSuper
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_title:
                onTitleOptions(v);
                break;
            case R.id.tv_back:
                onBack();
                break;
            case R.id.tv_operas:
                onOptions(v);
                break;
        }
    }

    protected void onOptions(View view) {

    }

    protected void onTitleOptions(View view) {

    }
}

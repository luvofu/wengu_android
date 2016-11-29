package com.culturebud;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by XieWei on 2016/11/1.
 */

public abstract class TitleBarActivity extends MyAppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tvBack;
    private TextView tvTitle;
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
        tvTitle = obtainViewById(R.id.tv_title);
        tvOperas = obtainViewById(R.id.tv_operas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initListener();
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

    public void setBackText(CharSequence backText) {
        if (TextUtils.isEmpty(backText)) {
            tvBack.setText("");
        } else {
            tvBack.setText(backText);
        }
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
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvOperas.setCompoundDrawables(null, null, drawable, null);
    }

    public void setBackGroundColor(int color) {
        vgContent.setBackgroundColor(color);
    }

    private void initListener() {
        tvBack.setOnClickListener(this);
        tvOperas.setOnClickListener(this);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
}

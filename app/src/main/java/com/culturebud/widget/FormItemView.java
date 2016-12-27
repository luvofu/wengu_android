package com.culturebud.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.util.WidgetUtil;

/**
 * Created by XieWei on 2016/12/27.
 */

public class FormItemView extends LinearLayout {
    private TextView tvLabel;
    private EditText etContent;
    @ColorInt
    private int labelFontColor;
    private int labelFontSize;
    @ColorInt
    private int contentFontColor;
    private int contentFontSize;
    @DrawableRes
    private int contentBackground;

    private CharSequence label, content;
    private boolean contentEditable;

    public FormItemView(Context context) {
        super(context);
        init(null);
    }

    public FormItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        tvLabel = new TextView(getContext());
        etContent = new EditText(getContext());

        labelFontColor = getResources().getColor(R.color.light_font_black);
        labelFontSize = getResources().getDimensionPixelSize(R.dimen.content_font_default);
        contentFontColor = getResources().getColor(R.color.title_font_default);
        contentFontSize = getResources().getDimensionPixelSize(R.dimen.content_font_default);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FormItemView);
            if (ta != null) {
                labelFontColor = ta.getColor(R.styleable.FormItemView_label_font_color, labelFontColor);
                labelFontSize = ta.getDimensionPixelSize(R.styleable.FormItemView_label_font_size, labelFontSize);
                contentFontColor = ta.getColor(R.styleable.FormItemView_content_font_color, contentFontColor);
                contentFontSize = ta.getDimensionPixelSize(R.styleable.FormItemView_content_font_size, contentFontSize);
                contentEditable = ta.getBoolean(R.styleable.FormItemView_content_editable, false);
                label = ta.getString(R.styleable.FormItemView_label);
                content = ta.getString(R.styleable.FormItemView_content);
                int contentBgResId = ta.getResourceId(R.styleable.FormItemView_content_background, android.R.color.transparent);
                etContent.setBackgroundResource(contentBgResId);
                Drawable background = ta.getDrawable(R.styleable.FormItemView_item_background);
                if (background != null) {
                    setBackgroundDrawable(background);
                }
                ta.recycle();
            }
        }
        LinearLayout.LayoutParams labelParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        labelParams.weight = 1;
        tvLabel.setLayoutParams(labelParams);

        LinearLayout.LayoutParams contentParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        contentParams.weight = 3;
        etContent.setLayoutParams(contentParams);

        addView(tvLabel);
        addView(etContent);

        WidgetUtil.setRawTextSize(tvLabel, labelFontSize);
        tvLabel.setTextColor(labelFontColor);
        WidgetUtil.setRawTextSize(etContent, contentFontSize);
        etContent.setTextColor(contentFontColor);
        tvLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        etContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        if (!TextUtils.isEmpty(label)) {
            tvLabel.setText(label);
        }
        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
        }
        setContentEditable(contentEditable);
    }

    public void setContentEditable(boolean editable) {
        etContent.setFocusable(editable);
        if (editable) {
            etContent.setFocusableInTouchMode(true);
            etContent.requestFocus();
            etContent.findFocus();
            etContent.setBackgroundResource(contentBackground);
        } else {
            etContent.clearFocus();
            etContent.setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setContent(CharSequence content) {
        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
        }
    }
}

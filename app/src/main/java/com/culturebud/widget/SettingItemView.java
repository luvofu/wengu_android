package com.culturebud.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.R;

import java.lang.reflect.Method;

/**
 * Created by XieWei on 2016/10/20.
 */

public class SettingItemView extends LinearLayout {
    private static final String TAG = SettingItemView.class.getSimpleName();
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvArrow;

    public SettingItemView(Context context) {
        super(context);
        init(null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        int pl = getResources().getDimensionPixelSize(R.dimen.setting_item_padding_left);
        int pr = getResources().getDimensionPixelSize(R.dimen.setting_item_padding_right);
        setPadding(pl, getPaddingTop(), pr, getPaddingBottom());
        View view = View.inflate(getContext(), R.layout.setting_item_view, this);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        tvArrow = (TextView) view.findViewById(R.id.tv_arrow);

        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView);
            boolean hasIcon = arr.getBoolean(R.styleable.SettingItemView_has_icon, true);

            Drawable bgDrawable = arr.getDrawable(R.styleable.SettingItemView_background);
            if (bgDrawable != null) {
                int bgResId = arr.getResourceId(R.styleable.SettingItemView_background, -1);
                if (bgResId != -1) {
                    if (bgDrawable.getCurrent() instanceof LayerDrawable  && !hasIcon) {
                        LayerDrawable tmp = ((LayerDrawable) bgDrawable.getCurrent());
                        switch (bgResId) {
                            case R.drawable.setting_item_last_bg_selector:
                                tmp.setLayerInset(2, pl, 0, 0, 1);
                                break;
                            case R.drawable.setting_item_first_bg_selector:
                                tmp.setLayerInset(2, pl, 1, 0, 0);
                                break;
                        }
                        setBackgroundDrawable(bgDrawable);
                    } else {
                        setBackgroundResource(bgResId);
                    }
                }
            }

            Drawable icon = arr.getDrawable(R.styleable.SettingItemView_icon);
            if (icon != null) {
                ivIcon.setImageDrawable(icon);
            }
            int iconWidth = arr.getDimensionPixelSize(R.styleable.SettingItemView_icon_width,
                    getResources().getDimensionPixelSize(R.dimen.setting_icon_default_small));
            int iconHeight = arr.getDimensionPixelSize(R.styleable.SettingItemView_icon_height,
                    getResources().getDimensionPixelSize(R.dimen.setting_icon_default_small));

            ViewGroup.LayoutParams lp = ivIcon.getLayoutParams();
            lp.width = iconWidth;
            lp.height = iconHeight;
            ivIcon.setLayoutParams(lp);
            if (hasIcon) {
                ivIcon.setVisibility(VISIBLE);
            } else {
                ivIcon.setVisibility(GONE);
            }

            Drawable smallIcon = arr.getDrawable(R.styleable.SettingItemView_small_icon);
            if (smallIcon != null) {
                smallIcon.setBounds(0, 0, smallIcon.getIntrinsicWidth(), smallIcon.getIntrinsicHeight());

                tvName.setCompoundDrawables(null, null, smallIcon, null);
            }

            boolean hasArrow = arr.getBoolean(R.styleable.SettingItemView_has_arrow, true);
            if (hasArrow) {
                Drawable arrow = arr.getDrawable(R.styleable.SettingItemView_arrow);
                if (arrow != null) {
                    arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                    tvArrow.setCompoundDrawables(null, null, arrow, null);
                }
            } else {
                Drawable[] drawables = tvArrow.getCompoundDrawables();
                tvArrow.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
            }

            String name = arr.getString(R.styleable.SettingItemView_name);
            if (name != null) {
                tvName.setText(name);
            }

            String desc = arr.getString(R.styleable.SettingItemView_desc);
            if (desc != null) {
                tvDesc.setText(desc);
            }

            int nameColor = arr.getColor(R.styleable.SettingItemView_name_color,
                    getResources().getColor(R.color.title_font_default));
            tvName.setTextColor(nameColor);

            int nameSize = arr.getDimensionPixelSize(R.styleable.SettingItemView_name_size,
                    getResources().getDimensionPixelSize(R.dimen.setting_item_name_font));
            setRawTextSize(tvName, nameSize);

            String info = arr.getString(R.styleable.SettingItemView_info);
            if (info != null) {
                tvArrow.setText(info);
            }
            int infoColor = arr.getColor(R.styleable.SettingItemView_info_color,
                    getResources().getColor(R.color.gray_bg_border));
            tvArrow.setTextColor(infoColor);
            int infoSize = arr.getDimensionPixelSize(R.styleable.SettingItemView_info_size,
                    getResources().getDimensionPixelSize(R.dimen.setting_item_name_font));
            setRawTextSize(tvArrow, infoSize);

            boolean needShowDesc = arr.getBoolean(R.styleable.SettingItemView_show_desc, false);
            if (needShowDesc) {
                int descSize = arr.getDimensionPixelSize(R.styleable.SettingItemView_desc_size,
                        getResources().getDimensionPixelSize(R.dimen.setting_item_desc_font));
                setRawTextSize(tvDesc, descSize);

                int descColor = arr.getColor(R.styleable.SettingItemView_desc_color,
                        getResources().getColor(R.color.title_font_default));
                tvDesc.setTextColor(descColor);
            } else {
                tvDesc.setVisibility(View.GONE);
            }

            arr.recycle();
        }
    }

    private void setRawTextSize(TextView view, int size) {
        Class tvNameCls = view.getClass();
        if (view instanceof AppCompatTextView) {
            tvNameCls = view.getClass().getSuperclass();
        }
        try {
            Method setRawTS = tvNameCls.getDeclaredMethod("setRawTextSize", float.class);
            setRawTS.setAccessible(true);
            setRawTS.invoke(view, size);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    public void setIconImage(@DrawableRes int resId) {
        ivIcon.setImageResource(resId);
    }

    public void setIconImage(Drawable iconImage) {
        ivIcon.setImageDrawable(iconImage);
    }

    public void setIconImage(Bitmap iconImage) {
        ivIcon.setImageBitmap(iconImage);
    }

    public void setSmallIcon(@DrawableRes int resId) {
        tvName.setCompoundDrawables(null, null, getResources().getDrawable(resId), null);
    }

    public void setSmallIcon(Drawable icon) {
        tvName.setCompoundDrawables(null, null, icon, null);
    }

    public void setSmqllIcon(Bitmap icon) {
        BitmapDrawable drawable = new BitmapDrawable(icon);
        tvName.setCompoundDrawables(null, null, drawable, null);
    }

    public void setArrow(@DrawableRes int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvArrow.setCompoundDrawables(null, null, drawable, null);
    }

    public void setArrow(Drawable arrow) {
        arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
        tvArrow.setCompoundDrawables(null, null, arrow, null);
    }

    public void setArrow(Bitmap arrow) {
        Drawable drawable = new BitmapDrawable(arrow);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvArrow.setCompoundDrawables(null, null, drawable, null);
    }

    public void setRightInfo(CharSequence info) {
        tvArrow.setText(info);
    }

    public void setRightInfo(@StringRes int resId) {
        tvArrow.setText(resId);
    }

    public void setInfoColor(@ColorInt int color) {
        tvArrow.setTextColor(color);
    }

    public void setInfoSize(@DimenRes int resId) {
        setRawTextSize(tvArrow, getResources().getDimensionPixelSize(resId));
    }

    public void setInfoSize(float size) {
        tvArrow.setTextSize(size);
    }

    public void setSettingName(CharSequence name) {
        tvName.setText(name);
    }

    public void setSettingName(@StringRes int resId) {
        tvName.setText(resId);
    }

    public void setDesc(@StringRes int resId) {
        tvDesc.setText(resId);
    }

    public void setDesc(CharSequence desc) {
        tvDesc.setText(desc);
    }

    public void setNameColor(@ColorInt int color) {
        tvName.setTextColor(color);
    }

    public void setDescColor(@ColorInt int color) {
        tvDesc.setTextColor(color);
    }

    public void setNameSize(@DimenRes int resId) {
//        tvName.setTextSize(getResources().getDimensionPixelSize(resId));
        setRawTextSize(tvName, getResources().getDimensionPixelSize(resId));
    }

    public void setNameSize(float size) {
        tvName.setTextSize(size);
    }

    public void setDescSize(@DimenRes int resId) {
//        tvDesc.setTextSize(getResources().getDimensionPixelSize(resId));
        setRawTextSize(tvDesc, getResources().getDimensionPixelSize(resId));
    }

    public void setDescSize(float size) {
        tvDesc.setTextSize(size);
    }

    public boolean needShowDesc() {
        return tvDesc.getVisibility() == View.VISIBLE;
    }

    public void setNeedShowDesc(boolean needShowDesc) {
        if (needShowDesc) {
            tvDesc.setVisibility(View.VISIBLE);
        } else {
            tvDesc.setVisibility(View.GONE);
        }
    }

    public void setHasArrow(boolean hasArrow) {
        if (hasArrow) {
            Drawable drawable = getResources().getDrawable(R.mipmap.right_arrow);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvArrow.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable[] drawables = tvArrow.getCompoundDrawables();
            tvArrow.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
        }
    }

    public ViewGroup.LayoutParams getIconLayoutParams() {
        return ivIcon.getLayoutParams();
    }

    public void setIconLayoutParams(ViewGroup.LayoutParams params) {
        ivIcon.setLayoutParams(params);
    }
}


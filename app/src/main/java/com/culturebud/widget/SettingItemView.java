package com.culturebud.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culturebud.R;
import com.culturebud.util.WidgetUtil;

/**
 * Created by XieWei on 2016/10/20.
 */

public class SettingItemView extends LinearLayout {
    private static final String TAG = SettingItemView.class.getSimpleName();
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDesc;
    private EditText etArrow;

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
        setOrientation(VERTICAL);
        int pl = getResources().getDimensionPixelSize(R.dimen.setting_item_padding_left);
        int pr = getResources().getDimensionPixelSize(R.dimen.setting_item_padding_right);
        setPadding(pl, getPaddingTop(), pr, getPaddingBottom());
        View view = View.inflate(getContext(), R.layout.setting_item_view, this);
        ivIcon = WidgetUtil.obtainViewById(view, R.id.iv_icon);
        tvName = WidgetUtil.obtainViewById(view, R.id.tv_name);
        tvDesc = WidgetUtil.obtainViewById(view, R.id.tv_desc);
        etArrow = WidgetUtil.obtainViewById(view, R.id.et_arrow);

        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.SettingItemView);
            boolean hasIcon = arr.getBoolean(R.styleable.SettingItemView_has_icon, true);

            Drawable bgDrawable = arr.getDrawable(R.styleable.SettingItemView_background);
            if (bgDrawable != null) {
                int bgResId = arr.getResourceId(R.styleable.SettingItemView_background, -1);
                if (bgResId != -1) {
                    if (bgDrawable.getCurrent() instanceof LayerDrawable && !hasIcon) {
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
                    etArrow.setCompoundDrawables(null, null, arrow, null);
                }
            } else {
                Drawable[] drawables = etArrow.getCompoundDrawables();
                etArrow.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
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


            boolean canEditInfo = arr.getBoolean(R.styleable.SettingItemView_can_edit_info, false);
            if (!canEditInfo) {
                etArrow.setFocusable(false);
                etArrow.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SettingItemView.this.performClick();
                    }
                    return true;
                });
            }
            String hintInfo = arr.getString(R.styleable.SettingItemView_hint_info);
            if (hintInfo != null) {
                etArrow.setHint(hintInfo);
            }
            String info = arr.getString(R.styleable.SettingItemView_info);
            if (info != null) {
                etArrow.setText(info);
            }
            int infoColor = arr.getColor(R.styleable.SettingItemView_info_color,
                    getResources().getColor(R.color.gray_bg_border));
            etArrow.setTextColor(infoColor);
            int infoSize = arr.getDimensionPixelSize(R.styleable.SettingItemView_info_size,
                    getResources().getDimensionPixelSize(R.dimen.setting_item_name_font));
            setRawTextSize(etArrow, infoSize);

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
//        if (view instanceof AppCompatTextView) {
//            tvNameCls = view.getClass().getSuperclass();
//        }
        if (tvNameCls != TextView.class) {
            tvNameCls = getViewSupperCls(tvNameCls);
        }
        try {
            if (tvNameCls == TextView.class) {
//                Method setRawTS = tvNameCls.getDeclaredMethod("setRawTextSize", float.class);
//                setRawTS.setAccessible(true);
//                setRawTS.invoke(view, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private Class getViewSupperCls(Class<? extends View> cls) {
        Class c = cls.getSuperclass();
        if (c != null && c != TextView.class) {
            getViewSupperCls(c);
        }
        return c;
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
        etArrow.setCompoundDrawables(null, null, drawable, null);
    }

    public void setArrow(Drawable arrow) {
        arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
        etArrow.setCompoundDrawables(null, null, arrow, null);
    }

    public void setArrow(Bitmap arrow) {
        Drawable drawable = new BitmapDrawable(arrow);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        etArrow.setCompoundDrawables(null, null, drawable, null);
    }

    public void setCanEditInfo(boolean canEditInfo) {
        etArrow.setEnabled(canEditInfo);
    }

    public void setInfoInputType(int inputType) {
        etArrow.setInputType(inputType);
    }

    public void setRightInfo(CharSequence info) {
        etArrow.setText(info);
    }

    public void setRightInfo(@StringRes int resId) {
        etArrow.setText(resId);
    }

    public void setInfoColor(@ColorInt int color) {
        etArrow.setTextColor(color);
    }

    public void setInfoSize(@DimenRes int resId) {
        setRawTextSize(etArrow, getResources().getDimensionPixelSize(resId));
    }

    public void setInfoSize(float size) {
        etArrow.setTextSize(size);
    }

    public String getInfo() {
        return etArrow.getText().toString();
    }

    public void setSettingName(CharSequence name) {
        tvName.setText(name);
    }

    public void setSettingName(@StringRes int resId) {
        tvName.setText(resId);
    }

    public String getName() {
        return tvName.getText().toString();
    }

    public void setDesc(@StringRes int resId) {
        tvDesc.setText(resId);
    }

    public void setDesc(CharSequence desc) {
        tvDesc.setText(desc);
    }

    public String getDesc() {
        return tvDesc.getText().toString();
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
            etArrow.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable[] drawables = etArrow.getCompoundDrawables();
            etArrow.setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
        }
    }

    public ViewGroup.LayoutParams getIconLayoutParams() {
        return ivIcon.getLayoutParams();
    }

    public void setIconLayoutParams(ViewGroup.LayoutParams params) {
        ivIcon.setLayoutParams(params);
    }

}


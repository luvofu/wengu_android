package com.culturebud.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.culturebud.R;
import com.culturebud.util.WidgetUtil;

/**
 * Created by XieWei on 2016/10/28.
 */

public class StepperView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final String TAG = StepperView.class.getSimpleName();
    private ImageView ivAdd, ivSub;
    private Drawable addDrawable, subDrawable;
    private EditText etNum;
    private boolean needNum;
    private int stepValue = 1;
    private int maxValue = Integer.MAX_VALUE;

    public StepperView(Context context) {
        super(context);
        init(null);
    }

    public StepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StepperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepperView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ivAdd = new ImageView(getContext());
        ivSub = new ImageView(getContext());
        etNum = new EditText(getContext());

        ivAdd.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivSub.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNum.setGravity(Gravity.CENTER);
        etNum.setSingleLine(true);

        addDrawable = getResources().getDrawable(R.mipmap.plus_icon);
        addDrawable.setBounds(0, 0, addDrawable.getIntrinsicWidth(), addDrawable.getIntrinsicHeight());
        subDrawable = getResources().getDrawable(R.mipmap.minus_icon);
        subDrawable.setBounds(0, 0, subDrawable.getIntrinsicWidth(), subDrawable.getIntrinsicHeight());

        ivAdd.setImageDrawable(addDrawable);
        ivSub.setImageDrawable(subDrawable);

        ivAdd.setClickable(true);
        ivSub.setClickable(true);
        ivAdd.setOnClickListener(this);
        ivSub.setOnClickListener(this);
        etNum.addTextChangedListener(this);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.StepperView);
            needNum = ta.getBoolean(R.styleable.StepperView_show_number, false);
            stepValue = ta.getInteger(R.styleable.StepperView_step_value, 1);
            if (stepValue < 1) {
                stepValue = 1;
            }
            if (needNum) {
                flushStepValue();
            }
            ta.recycle();
        }
        if (!needNum) {
            etNum.setEnabled(false);
            etNum.setMinimumWidth(2);
            etNum.setBackgroundColor(getResources().getColor(R.color.login_btn_normal));
        } else {
            etNum.setEnabled(true);
            etNum.setMinimumWidth(32);
            etNum.setPadding(1, 1, 1, 1);
            etNum.setBackgroundResource(R.drawable.stepper_number_bg);
            WidgetUtil.setRawTextSize(etNum, getResources().getDimensionPixelSize(R.dimen.font_web));
        }
        addView(ivSub);
        addView(etNum);
        addView(ivAdd);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!needNum) {
            ivSub.setMinimumWidth(getMeasuredWidth() / 2);
            ivAdd.setMinimumWidth(getMeasuredWidth() / 2);
        } else {
            etNum.setMinimumWidth(getMeasuredWidth() / 3);
            etNum.setMaxWidth(getMeasuredWidth() / 3);
            ivSub.setMinimumWidth(getMeasuredWidth() / 3);
            ivAdd.setMinimumWidth(getMeasuredWidth() / 3);
        }
        etNum.setMinimumHeight(getMeasuredHeight());
        ivAdd.setMinimumHeight(getMeasuredHeight());
        ivSub.setMinimumHeight(getMeasuredHeight());
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (stepValue > maxValue) {
            stepValue = maxValue;
            flushStepValue();
        }
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick()" + v);
        if (v == ivAdd) {
            if (stepValue < maxValue) {
                ++stepValue;
                Log.e(TAG, "stepValue = " + stepValue);
                if (needNum) {
                    flushStepValue();
                }
                if (listener != null) {
                    listener.onValueChanged(stepValue, true);
                }
            }
        } else if (v == ivSub) {
            if (stepValue > 1) {
                --stepValue;
                Log.e(TAG, "stepValue = " + stepValue);
                if (needNum) {
                    flushStepValue();
                }
                if (listener != null) {
                    listener.onValueChanged(stepValue, false);
                }
            }
        }
    }

    public int getStepValue() {
        return stepValue;
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        this.listener = listener;
    }

    private OnValueChangedListener listener;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString()) && s.length() <= 5) {
            int value = Integer.valueOf(s.toString());
            if (value < 1) {
                value = 1;
                etNum.setText(String.valueOf(value));
            }
            int oldValue = stepValue;
            stepValue = value;
            if (listener != null) {
                if (oldValue > stepValue) {
                    listener.onValueChanged(stepValue, false);
                } else if (oldValue < stepValue) {
                    listener.onValueChanged(stepValue, true);
                }
            }
        } else {
            flushStepValue();
        }
    }

    private void flushStepValue() {
        etNum.setText(String.valueOf(stepValue));
        etNum.setSelection(String.valueOf(stepValue).length());
    }

    public void setStep(int stepValue) {
        this.stepValue = stepValue;
        flushStepValue();
    }
    public interface OnValueChangedListener {
        void onValueChanged(int value, boolean isPlus);
    }
}

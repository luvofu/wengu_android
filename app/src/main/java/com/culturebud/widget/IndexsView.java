package com.culturebud.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.culturebud.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by XieWei on 2016/12/7.
 */
public class IndexsView extends View {
    private List<String> indexs;
    private Paint paint;

    public IndexsView(Context context) {
        super(context);
        init();
    }

    public IndexsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndexsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        indexs = new ArrayList<>();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.title_font_default));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.titlebar_font_size));
    }

    public void setIndexs(List<String> indexs) {
        if (indexs != null && !indexs.isEmpty()) {
            this.indexs.addAll(indexs);
            Collections.sort(this.indexs, (idx01, idx02) -> {
                char i01 = idx01.charAt(0);
                char i02 = idx02.charAt(0);
                if (i01 == i02) {
                    return 0;
                } else if (i01 < i02) {
                    return -1;
                } else {
                    return 1;
                }
            });
            requestLayout();
            invalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int index = (int) ((event.getY() / getHeight()) * indexs.size());
        if (index > indexs.size() - 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onIndexChangedListener != null) {
                    onIndexChangedListener.onIndexChanged(index, indexs.get(index));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (onIndexChangedListener != null) {
                    onIndexChangedListener.onIndexChanged(index, indexs.get(index));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (onIndexChangedListener != null) {
                    onIndexChangedListener.onIndexChanged(index, indexs.get(index));
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 50 * indexs.size() + 50;
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indexs.size() <= 0) {
            return;
        }
        //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        float lineHeight = (getHeight() / indexs.size());

        float y = lineHeight / 2;
        float lineY = lineHeight;
        Rect txtRect = new Rect();
        for (String s : indexs) {
            paint.getTextBounds(s, 0, s.length(), txtRect);
            canvas.drawText(s, getWidth() / 2, y + txtRect.height() / 2, paint);
            y += lineHeight;
            //canvas.drawLine(0, lineY, getWidth(), lineY, paint);
            //lineY += lineHeight;
        }
    }

    private OnIndexChangedListener onIndexChangedListener;

    public OnIndexChangedListener getOnIndexChangedListener() {
        return onIndexChangedListener;
    }

    public void setOnIndexChangedListener(OnIndexChangedListener onIndexChangedListener) {
        this.onIndexChangedListener = onIndexChangedListener;
    }

    public interface OnIndexChangedListener {
        void onIndexChanged(int index, String content);
    }
}

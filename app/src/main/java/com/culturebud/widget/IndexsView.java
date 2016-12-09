package com.culturebud.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.culturebud.R;

import java.util.ArrayList;
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
        paint.setDither(true);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.title_font_default));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.titlebar_font_size));
    }

    public void setIndexs(List<String> indexs) {
        if (indexs != null && !indexs.isEmpty()) {
            this.indexs.addAll(indexs);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = 0;
        for (String s : indexs) {
            canvas.drawText(s, 0F, height, paint);
            height += 20;
        }
    }
}

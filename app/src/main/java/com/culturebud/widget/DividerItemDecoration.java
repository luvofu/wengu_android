package com.culturebud.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by czp on 16/8/19.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    private int orientation;
    private int lineColor = 0xffe6e6e6;
    private int padding;
    private int dividerHeight = 1;
    private boolean isLastItemShowDivider = false;
    private Context ctx;
    private Paint dividerPaint;

    public DividerItemDecoration(Context ctx, int orientation, @ColorRes int resColorId, int
            dividerHeight, int padding, boolean isLastItemShowDivider) {
        this.ctx = ctx;
        this.orientation = orientation;
        this.lineColor = ctx.getResources().getColor(resColorId);
        this.dividerHeight = dividerHeight;
        this.padding = padding;
        this.isLastItemShowDivider = isLastItemShowDivider;
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("orientation error (HORIZONTAL VERTICAL)");
        }
        initDividerPaint();
    }

    public DividerItemDecoration(Context ctx, int orientation, @ColorRes int resColorId, int dividerHeight) {
        this.ctx = ctx;
        this.orientation = orientation;
        this.lineColor = ctx.getResources().getColor(resColorId);
        this.dividerHeight = dividerHeight;
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("orientation error (HORIZONTAL VERTICAL)");
        }
        initDividerPaint();
    }

    public DividerItemDecoration(Context ctx, int orientation, boolean isLastItemShowDivider) {
        this.ctx = ctx;
        this.orientation = orientation;
        this.isLastItemShowDivider = isLastItemShowDivider;
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("orientation error (HORIZONTAL VERTICAL)");
        }
        initDividerPaint();
    }

    private void initDividerPaint() {
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(lineColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            final int top = padding + parent.getPaddingTop() + params.topMargin;
            final int left = childView.getRight() + params.rightMargin + parent.getPaddingLeft();
            final int right = left + dividerHeight;
            final int bottom = childView.getBottom() - parent.getPaddingBottom() - params.bottomMargin;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = padding + parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight() - padding;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == parent.getChildCount() - 1 && !isLastItemShowDivider) {
                break;
            }
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == HORIZONTAL) {
            outRect.set(0, 0, dividerHeight, 0);
        } else if (orientation == VERTICAL) {
            outRect.set(0, 0, 0, dividerHeight);
        }
    }
}

package com.culturebud.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by XieWei on 2016/11/8.
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    public FlowLayoutManager() {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);
        int itemWidth = 0, itemHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            itemWidth += getDecoratedMeasuredWidth(child);
            itemHeight = getDecoratedMeasuredHeight(child);

        }
    }
}

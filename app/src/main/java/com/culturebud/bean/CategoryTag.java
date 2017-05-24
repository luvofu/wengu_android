package com.culturebud.bean;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class CategoryTag extends Category {
    boolean selected = false;

    public CategoryTag(Category category, boolean selected) {
        setCategory(category.getCategory());
        setStatis(category.getStatis());
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}

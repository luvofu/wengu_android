package com.culturebud.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2017/3/31.
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

    public static List<CategoryTag> loadCatTags(List<Category> datas, boolean isCurrType, String currCategory) {
        List<CategoryTag> categoryTags = new ArrayList<>();
        for (Category category : datas) {
            categoryTags.add(new CategoryTag(category, isCurrType && category.equals(currCategory)));
        }
        return categoryTags;
    }
}

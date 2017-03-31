package com.culturebud.bean;

/**
 * Created by XieWei on 2017/3/31.
 */

public class Category {
    private String category;//"文学",
    private long categoryId;//"51",
    private int statis;//"0"
    private int type;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatis() {
        return statis;
    }

    public void setStatis(int statis) {
        this.statis = statis;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category='" + category + '\'' +
                ", categoryId=" + categoryId +
                ", statis=" + statis +
                ", type=" + type +
                '}';
    }
}

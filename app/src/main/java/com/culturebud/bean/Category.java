package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.type;

/**
 * Created by XieWei on 2017/3/31.
 */

public class Category {
    @SerializedName("category")
    public String category;//"文学",
    @SerializedName("categoryId")
    private long categoryId;//"51",
    @SerializedName("statis")
    private int statis;//"0"

    public Category() {
    }


    public Category(String category, int statis, long categoryId) {
        this.category = category;
        this.categoryId = categoryId;
        this.statis = statis;
    }

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

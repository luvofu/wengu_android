package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2017/3/31.
 */

public class Category {
    @SerializedName("category")
    private String category;//"文学",
    @SerializedName("categoryId")
    private long categoryId;//"51",
    @SerializedName("statis")
    private int statis;//"0"
    @SerializedName("type")
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

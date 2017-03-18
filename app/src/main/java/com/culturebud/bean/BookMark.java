package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2017/3/18.
 */

public class BookMark {

    @SerializedName("bookmarkId")
    private long bookmarkId;//	number	书签id
    @SerializedName("cover")
    private String cover;//	string	书籍封面
    @SerializedName("name")
    private String name;//	string	书签名
    @SerializedName("pages")
    private int pages;//	number	在读页数
    @SerializedName("totalPage")
    private int totalPage;//	number	藏书总页数
    @SerializedName("updatedTime")
    private long updatedTime;//	number	书签更新时间

    public long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "BookMark{" +
                "bookmarkId=" + bookmarkId +
                ", cover='" + cover + '\'' +
                ", name='" + name + '\'' +
                ", pages=" + pages +
                ", totalPage=" + totalPage +
                ", updatedTime=" + updatedTime +
                '}';
    }
}

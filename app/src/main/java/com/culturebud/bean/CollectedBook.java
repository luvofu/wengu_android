package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CollectedBook {

    @SerializedName("userBookId")
    private long userBookId;//用户书籍id

    @SerializedName("bookId")
    private long bookId;//书籍id

    @SerializedName("cover")
    private String cover;//书籍封面

    @SerializedName("title")
    private String title;//书名

    @SerializedName("subTitle")
    private String subTitle;//副标题

    public long getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(long userBookId) {
        this.userBookId = userBookId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "CollectedBook{" +
                "userBookId=" + userBookId +
                ", bookId=" + bookId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}

package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/11/7.
 */

@DatabaseTable(tableName = "t_sheet_book")
public class SheetBook {

    @SerializedName("sheetBookId")
    @DatabaseField(id = true, columnName = "sheet_book_id")
    private long sheetBookId;//书单书籍id

    @SerializedName("bookId")
    @DatabaseField(columnName = "book_id")
    private long bookId;//书籍id

    @SerializedName("cover")
    @DatabaseField(columnName = "cover")
    private String cover;//书籍封面

    @SerializedName("title")
    @DatabaseField(columnName = "title")
    private String title;//书名

    @SerializedName("subTitle")
    @DatabaseField(columnName = "sub_title")
    private String subTitle;//副标题

    @SerializedName("author")
    @DatabaseField(columnName = "author")
    private String author;//作者

    @SerializedName("rating")
    @DatabaseField(columnName = "rating")
    private float rating;//评分

    @SerializedName("recommend")
    @DatabaseField(columnName = "recommend")
    private String recommend;//推荐语

    public long getSheetBookId() {
        return sheetBookId;
    }

    public void setSheetBookId(long sheetBookId) {
        this.sheetBookId = sheetBookId;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "SheetBook{" +
                "sheetBookId=" + sheetBookId +
                ", bookId=" + bookId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                ", recommend='" + recommend + '\'' +
                '}';
    }
}

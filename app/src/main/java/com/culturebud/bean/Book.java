package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/10/24.
 */

@DatabaseTable(tableName = "t_book")
public class Book {
    @SerializedName("bookId")
    @DatabaseField(id = true, columnName = "book_id")
    private long bookId; //"bookId": 583940,

    @SerializedName("author")
    @DatabaseField(columnName = "author")
    private String author;//"author": "二星 天",

    @SerializedName("cover")
    @DatabaseField(columnName = "cover")
    private String cover;// "cover": "http://www.mywengu.com/img/book/cover/lpic/58/583940_250x250.jpg",

    @SerializedName("pubDate")
    @DatabaseField(columnName = "pub_date")
    private String pubDate;// "pubDate": "2013-7-12",

    @SerializedName("publisher")
    @DatabaseField(columnName = "publisher")
    private String publisher;// "publisher": "秋田書店",

    @SerializedName("rating")
    @DatabaseField(columnName = "rating")
    private float rating;// "rating": 0,

    @SerializedName("remarkNum")
    @DatabaseField(columnName = "remark_num")
    private long remarkNum;// "remarkNum": 0,

    @SerializedName("subTitle")
    @DatabaseField(columnName = "sub_title")
    private String subTitle; //"subTitle": "",

    @SerializedName("tag")
    @DatabaseField(columnName = "tag")
    private String tag;// "tag": "",

    @SerializedName("title")
    @DatabaseField(columnName = "title")
    private String title;// "title": "京都ゆうても端のほう １",

    @SerializedName("translator")
    @DatabaseField(columnName = "translator")
    private String translator;// "translator": "",

    @SerializedName("updatedTime")
    @DatabaseField(columnName = "updated_time")
    private long updatedTime;// "updatedTime": 1477107737000

    @SerializedName("contain")
    @DatabaseField(columnName = "contain")
    private boolean contain; //被收藏状态

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getRemarkNum() {
        return remarkNum;
    }

    public void setRemarkNum(long remarkNum) {
        this.remarkNum = remarkNum;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isContain() {
        return contain;
    }

    public void setContain(boolean contain) {
        this.contain = contain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return bookId == book.bookId;

    }

    @Override
    public int hashCode() {
        return (int) (bookId ^ (bookId >>> 32));
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", publisher='" + publisher + '\'' +
                ", rating=" + rating +
                ", remarkNum=" + remarkNum +
                ", subTitle='" + subTitle + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", translator='" + translator + '\'' +
                ", updatedTime=" + updatedTime +
                ", contain=" + contain +
                '}';
    }
}

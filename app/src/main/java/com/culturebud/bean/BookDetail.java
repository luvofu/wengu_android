package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/11/7.
 */

@DatabaseTable(tableName = "t_book_detail")
public class BookDetail {

    @SerializedName("bookId")
    @DatabaseField(id = true, columnName = "book_id")
    private long bookId;    //书籍id

    @SerializedName("communityId")
    @DatabaseField(columnName = "community_id")
    private long communityId;   //社区id

    @SerializedName("title")
    @DatabaseField(columnName = "title")
    private String title;   //书名

    @SerializedName("subTitle")
    @DatabaseField(columnName = "sub_title")
    private String subTitle;    //副标题

    @SerializedName("author")
    @DatabaseField(columnName = "author")
    private String author;  //作者

    @SerializedName("rating")
    @DatabaseField(columnName = "rating")
    private float rating;   //评分

    @SerializedName("tag")
    @DatabaseField(columnName = "tag")
    private String tag; //标签

    @SerializedName("summary")
    @DatabaseField(columnName = "summary")
    private String summary; //简介

    @SerializedName("price")
    @DatabaseField(columnName = "price")
    private String price;   //价格

    @SerializedName("cover")
    @DatabaseField(columnName = "cover")
    private String cover;   //封面

    @SerializedName("pages")
    @DatabaseField(columnName = "pages")
    private String pages;   //页数

    @SerializedName("authorInfo")
    @DatabaseField(columnName = "author_info")
    private String authorInfo;  //作者简介

    @SerializedName("translator")
    @DatabaseField(columnName = "translator")
    private String translator;  //译者

    @SerializedName("publisher")
    @DatabaseField(columnName = "publisher")
    private String publisher;   //出版社

    @SerializedName("pubDate")
    @DatabaseField(columnName = "pub_date")
    private String pubDate; //出版日期

    @SerializedName("collectionNum")
    @DatabaseField(columnName = "collection_num")
    private long collectionNum; //收藏数

    @SerializedName("isbn10")
    @DatabaseField(columnName = "isbn_10")
    private String isbn10;  //ISBN十位码

    @SerializedName("isbn13")
    @DatabaseField(columnName = "isbn_13")
    private String isbn13;  //ISBN十三位码

    @SerializedName("binding")
    @DatabaseField(columnName = "binding")
    private String binding; //装订方式

    @SerializedName("heat")
    @DatabaseField(columnName = "heat")
    private float heat; //热度

    @SerializedName("updatedTime")
    @DatabaseField(columnName = "updated_time")
    private long updatedTime;   //出版时间

    @SerializedName("collect")
    @DatabaseField(columnName = "collect")
    private boolean collect;    //用户收藏状态

    @SerializedName("remarkNum")
    @DatabaseField(columnName = "remark_num")
    private long remarkNum; //书评总数

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public long getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(long collectionNum) {
        this.collectionNum = collectionNum;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public float getHeat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public long getRemarkNum() {
        return remarkNum;
    }

    public void setRemarkNum(long remarkNum) {
        this.remarkNum = remarkNum;
    }

    @Override
    public String toString() {
        return "BookDetail{" +
                "bookId=" + bookId +
                ", communityId=" + communityId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                ", tag='" + tag + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                ", cover='" + cover + '\'' +
                ", pages='" + pages + '\'' +
                ", authorInfo='" + authorInfo + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", collectionNum=" + collectionNum +
                ", isbn10='" + isbn10 + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                ", binding='" + binding + '\'' +
                ", heat=" + heat +
                ", updatedTime=" + updatedTime +
                ", collectAdd=" + collect +
                ", remarkNum=" + remarkNum +
                '}';
    }
}

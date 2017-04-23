package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2017/3/30.
 */

public class CheckedBook {
    @SerializedName("bookCheckId")
    private long bcId;//书籍审核id
    @SerializedName("checkStatus")
    private int checkStatus;//审核状态
    @SerializedName("checkInfo")
    private String checkInfo;//审核信息
    @SerializedName("title")
    private String title;//书名
    @SerializedName("originTitle")
    private String originTitle;//原标题
    @SerializedName("subTitle")
    private String subTitle;//副标题
    @SerializedName("author")
    private String author;//作者
    @SerializedName("summary")
    private String summary;//简介
    @SerializedName("price")
    private String price;//价格
    @SerializedName("cover")
    private String cover;//封面
    @SerializedName("pages")
    private String pages;//页数
    @SerializedName("authorInfo")
    private String authorInfo;//作者信息
    @SerializedName("translator")
    private String translator;//译者
    @SerializedName("publisher")
    private String publisher;//出版社
    @SerializedName("pubDate")
    private String pubDate;//出版日期
    @SerializedName("isbn13")
    private String isbn13;//13位isbn
    @SerializedName("binding")
    private String binding;//装订方式
    @SerializedName("bookId")
    private long bookId;

    public long getBcId() {
        return bcId;
    }

    public void setBcId(long bcId) {
        this.bcId = bcId;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
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

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "CheckedBook{" +
                "bcId=" + bcId +
                ", checkStatus=" + checkStatus +
                ", checkInfo='" + checkInfo + '\'' +
                ", title='" + title + '\'' +
                ", originTitle='" + originTitle + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                ", cover='" + cover + '\'' +
                ", pages='" + pages + '\'' +
                ", authorInfo='" + authorInfo + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                ", binding='" + binding + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}

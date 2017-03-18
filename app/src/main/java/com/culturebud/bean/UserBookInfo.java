package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2017/3/2.
 */

public class UserBookInfo {

    @SerializedName("bookId")
    private long bookId;//书籍 id
    @SerializedName("bookType")
    private int bookType;//书籍类型
    @SerializedName("dyaRentGold")
    private float dayRentGold;//日租金
    @SerializedName("enterType")
    private int enterType;//录入类型
    @SerializedName("evaluation")
    private float evaluation;//估价
    @SerializedName("getType")
    private int getType; //获取方式
    @SerializedName("getPlace")
    private String getPlace;//获取地点
    @SerializedName("getTime")
    private long getTime;//获取时间
    @SerializedName("lease")
    private boolean lease;//可租借
    @SerializedName("permission")
    private int permission; //权限
    @SerializedName("rating")
    private float rating;//评分
    @SerializedName("readStatus")
    private int readStatus;//阅读状态
    @SerializedName("readPlace")
    private String readPlace;//阅读地点
    @SerializedName("readTime")
    private long readTime;//阅读时间
    @SerializedName("remark")
    private String remark;//书评
    @SerializedName("remarkId")
    private long remarkId;//书评 id
    @SerializedName("sale")
    private boolean sale;//可出售
    @SerializedName("tag")
    private String tag; //标签 "文学|文字|艺术",
    @SerializedName("updateTime")
    private long updatedTime;// 1487658047000,
    @SerializedName("userBookId")
    private long userBookId;//
    @SerializedName("description")
    private String description;//书籍描述
    @SerializedName("other")
    private String other;

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getBookType() {
        return bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public float getDayRentGold() {
        return dayRentGold;
    }

    public void setDayRentGold(float dayRentGold) {
        this.dayRentGold = dayRentGold;
    }

    public int getEnterType() {
        return enterType;
    }

    public void setEnterType(int enterType) {
        this.enterType = enterType;
    }

    public float getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(float evaluation) {
        this.evaluation = evaluation;
    }

    public int getGetType() {
        return getType;
    }

    public void setGetType(int getType) {
        this.getType = getType;
    }

    public boolean isLease() {
        return lease;
    }

    public void setLease(boolean lease) {
        this.lease = lease;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(long remarkId) {
        this.remarkId = remarkId;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getUserBookId() {
        return userBookId;
    }

    public void setUserBookId(long userBookId) {
        this.userBookId = userBookId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReadPlace() {
        return readPlace;
    }

    public void setReadPlace(String readPlace) {
        this.readPlace = readPlace;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public String getGetPlace() {
        return getPlace;
    }

    public void setGetPlace(String getPlace) {
        this.getPlace = getPlace;
    }

    public long getGetTime() {
        return getTime;
    }

    public void setGetTime(long getTime) {
        this.getTime = getTime;
    }

    @Override
    public String toString() {
        return "UserBookInfo{" +
                "bookId=" + bookId +
                ", bookType=" + bookType +
                ", dayRentGold=" + dayRentGold +
                ", enterType=" + enterType +
                ", evaluation=" + evaluation +
                ", getType=" + getType +
                ", getPlace='" + getPlace + '\'' +
                ", getTime=" + getTime +
                ", lease=" + lease +
                ", permission=" + permission +
                ", rating=" + rating +
                ", readStatus=" + readStatus +
                ", readPlace='" + readPlace + '\'' +
                ", readTime=" + readTime +
                ", remark='" + remark + '\'' +
                ", remarkId=" + remarkId +
                ", sale=" + sale +
                ", tag='" + tag + '\'' +
                ", updatedTime=" + updatedTime +
                ", userBookId=" + userBookId +
                ", description='" + description + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}

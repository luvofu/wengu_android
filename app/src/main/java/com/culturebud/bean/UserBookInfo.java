package com.culturebud.bean;

/**
 * Created by XieWei on 2017/3/2.
 */

public class UserBookInfo {

    private long bookId;//书籍 id
    private int bookType;//书籍类型
    private float dayRentGold;//日租金
    private int enterType;//录入类型
    private float evaluation;//估价
    private int getType; //获取方式
    private boolean lease;//可租借
    private int permission; //权限
    private float rating;//评分
    private int readStatus;//阅读状态
    private String remark;//书评
    private long remarkId;//书评 id
    private boolean sale;//可出售
    private String tag; //标签 "文学|文字|艺术",
    private long updatedTime;// 1487658047000,
    private long userBookId;//
    private String description;//书籍描述

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

    @Override
    public String toString() {
        return "UserBookInfo{" +
                "bookId=" + bookId +
                ", bookType=" + bookType +
                ", dayRentGold=" + dayRentGold +
                ", enterType=" + enterType +
                ", evaluation=" + evaluation +
                ", getType=" + getType +
                ", lease=" + lease +
                ", permission=" + permission +
                ", rating=" + rating +
                ", readStatus=" + readStatus +
                ", remark='" + remark + '\'' +
                ", remarkId=" + remarkId +
                ", sale=" + sale +
                ", tag='" + tag + '\'' +
                ", updatedTime=" + updatedTime +
                ", userBookId=" + userBookId +
                ", description='" + description + '\'' +
                '}';
    }
}

package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by XieWei on 2016/11/7.
 */
@DatabaseTable(tableName = "t_book_sheet_detail")
public class BookSheetDetail {

    @SerializedName("sheetId")
    @DatabaseField(id = true, columnName = "sheet_id")
    private long sheetId;//书单id

    @SerializedName("name")
    @DatabaseField(columnName = "name")
    private String name;//书单名

    @SerializedName("cover")
    @DatabaseField(columnName = "cover")
    private String cover;//书单封面

    @SerializedName("description")
    @DatabaseField(columnName = "description")
    private String description;//书单描述

    @SerializedName("tag")
    @DatabaseField(columnName = "tag")
    private String tag;//标签

    @SerializedName("userId")
    @DatabaseField(columnName = "user_id")
    private long userId;//用户id

    @SerializedName("nickname")
    @DatabaseField(columnName = "nickname")
    private String nickname;//昵称

    @SerializedName("avatar")
    @DatabaseField(columnName = "avatar")
    private String avatar;//头像

    @SerializedName("bookNum")
    @DatabaseField(columnName = "book_num")
    private long bookNum;//书单书籍数

    @SerializedName("collectionNum")
    @DatabaseField(columnName = "collection_num")
    private long collectionNum;//收藏数

    @SerializedName("createdTime")
    @DatabaseField(columnName = "created_time")
    private long createdTime;//创建时间

    @SerializedName("updatedTime")
    @DatabaseField(columnName = "updated_time")
    private long updatedTime;//更新时间

    @SerializedName("collect")
    @DatabaseField(columnName = "collectAdd")
    private boolean collect;//收藏状态

    @SerializedName("sheetBookList")
    @ForeignCollectionField(eager = true)
    private List<SheetBook> sheetBookList;//书单书籍列表

    public long getSheetId() {
        return sheetId;
    }

    public void setSheetId(long sheetId) {
        this.sheetId = sheetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBookNum() {
        return bookNum;
    }

    public void setBookNum(long bookNum) {
        this.bookNum = bookNum;
    }

    public long getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(long collectionNum) {
        this.collectionNum = collectionNum;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
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

    public List<SheetBook> getSheetBookList() {
        return sheetBookList;
    }

    public void setSheetBookList(List<SheetBook> sheetBookList) {
        this.sheetBookList = sheetBookList;
    }

    @Override
    public String toString() {
        return "BookSheetDetail{" +
                "sheetId=" + sheetId +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bookNum=" + bookNum +
                ", collectionNum=" + collectionNum +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", collectAdd=" + collect +
                ", sheetBookList=" + sheetBookList +
                '}';
    }
}

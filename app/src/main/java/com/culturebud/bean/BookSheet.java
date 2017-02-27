package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/10/24.
 */

@DatabaseTable(tableName = "t_book_sheet")
public class BookSheet {

    @SerializedName("sheetId")
    @DatabaseField(id = true, columnName = "sheet_id")
    private long sheetId; //"sheetId": 22

    @SerializedName("collectionNum")
    @DatabaseField(columnName = "collection_num")
    private long collectionNum; //"collectionNum": 9,

    @SerializedName("cover")
    @DatabaseField(columnName = "cover")
    private String cover;//  "cover": "http://www.mywengu.com/img/booksheet/cover/lpic/0/22_1476517353353_300x300.jpg",

    @SerializedName("name")
    @DatabaseField(columnName = "name")
    private String name;//  "name": "跟习总学读书",

    @SerializedName("nickname")
    @DatabaseField(columnName = "nick_name")
    private String nickname;// "nickname": "不止步的风",

    @SerializedName("userId")
    @DatabaseField(columnName = "user_id")
    private long userId; //用户id

    @SerializedName("createdTime")
    @DatabaseField(columnName = "created_time")
    private long createdTime; //创建时间

    @SerializedName("bookNum")
    @DatabaseField(columnName = "book_num")
    private long bookNum;//书单书籍数

    @SerializedName("description")
    private String description;//描述


    public long getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(long collectionNum) {
        this.collectionNum = collectionNum;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getSheetId() {
        return sheetId;
    }

    public void setSheetId(long sheetId) {
        this.sheetId = sheetId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getBookNum() {
        return bookNum;
    }

    public void setBookNum(long bookNum) {
        this.bookNum = bookNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BookSheet{" +
                "sheetId=" + sheetId +
                ", collectionNum=" + collectionNum +
                ", cover='" + cover + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userId=" + userId +
                ", createdTime=" + createdTime +
                ", bookNum=" + bookNum +
                ", description='" + description + '\'' +
                '}';
    }
}

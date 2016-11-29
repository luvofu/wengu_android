package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/10/24.
 */

@DatabaseTable(tableName = "t_comment")
public class Comment {

    @SerializedName("author")
    @DatabaseField(columnName = "author")
    private String author;//"author": "刘瑞琳",

    @SerializedName("avatar")
    @DatabaseField(columnName = "avatar")
    private String avatar;// "avatar": "http://www.mywengu.com/img/user/avatar/lpic/10/100004_1476066850469_100x100.jpg",

    @SerializedName("commentId")
    @DatabaseField(id = true, columnName = "comment_id")
    private long commentId;//        "commentId": 2,

    @SerializedName("communityId")
    @DatabaseField(columnName = "community_id")
    private long communityId;//  "communityId": 314013,

    @SerializedName("content")
    @DatabaseField(columnName = "content")
    private String content;//"content":"温故而知新，可以为师矣！",

    @SerializedName("createdTime")
    @DatabaseField(columnName = "created_time")
    private long createdTime;//    "createdTime":1475075882000,

    @SerializedName("good")
    @DatabaseField(columnName = "good")
    private boolean good;//"good":false,

    @SerializedName("goodNum")
    @DatabaseField(columnName = "good_num")
    private long goodNum; //"goodNum":4,

    @SerializedName("nickname")
    @DatabaseField(columnName = "nick_name")
    private String nickname;//"nickname":"不止步的风",

    @SerializedName("replyNum")
    @DatabaseField(columnName = "reply_num")
    private long replyNum;//"replyNum":15,

    @SerializedName("subTitle")
    @DatabaseField(columnName = "sub_title")
    private String subTitle;//"subTitle":"",

    @SerializedName("title")
    @DatabaseField(columnName = "title")
    private String title;//"title":"温故",

    @SerializedName("userId")
    @DatabaseField(columnName = "user_id")
    private long userId;//"userId":100004

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public long getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(long goodNum) {
        this.goodNum = goodNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(long replyNum) {
        this.replyNum = replyNum;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author='" + author + '\'' +
                ", avatar='" + avatar + '\'' +
                ", commentId=" + commentId +
                ", communityId=" + communityId +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", good=" + good +
                ", goodNum=" + goodNum +
                ", nickname='" + nickname + '\'' +
                ", replyNum=" + replyNum +
                ", subTitle='" + subTitle + '\'' +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                '}';
    }
}

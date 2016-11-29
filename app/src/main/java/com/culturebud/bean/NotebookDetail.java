package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/20.
 */

public class NotebookDetail {
    @SerializedName("notebookId")
    private long notebookId; //笔记本id

    @SerializedName("userId")
    private long userId; //用户id

    @SerializedName("avatar")
    private String avatar; //用户头像

    @SerializedName("nickname")
    private String nickname; //昵称

    @SerializedName("bookId")
    private long bookId; //书籍id

    @SerializedName("title")
    private String title; //书名

    @SerializedName("subTitle")
    private String subTitle; //副标题

    @SerializedName("name")
    private String name; //笔记本名

    @SerializedName("cover")
    private String cover; //笔记本封面

    @SerializedName("description")
    private String description; //笔记本描述

    @SerializedName("noteNum")
    private long noteNum; //笔记数

    @SerializedName("permission")
    private int permission; //权限

    @SerializedName("createdTime")
    private long createdTime; //创建时间

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
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

    public long getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(long noteNum) {
        this.noteNum = noteNum;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "NotebookDetail{" +
                "notebookId=" + notebookId +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", bookId=" + bookId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", noteNum=" + noteNum +
                ", permission=" + permission +
                ", createdTime=" + createdTime +
                '}';
    }
}

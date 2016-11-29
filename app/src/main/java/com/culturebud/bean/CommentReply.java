package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/9.
 */

public class CommentReply {

    @SerializedName("replyId")
    private long replyId;//回复id

    @SerializedName("content")
    private String content;//内容

    @SerializedName("createdTime")
    private long createdTime;//创建时间

    @SerializedName("userId")
    private long userId;//用户id

    @SerializedName("avatar")
    private String avatar;//头像

    @SerializedName("nickname")
    private String nickname;//昵称

    @SerializedName("replyType")
    private int replyType;//回复类型

    @SerializedName("replyObjId")
    private long replyObjId;//回复对象id

    @SerializedName("replyObjContent")
    private String replyObjContent;//回复对象内容

    @SerializedName("replyObjUserId")
    private long replyObjUserId;//回复对象用户id

    @SerializedName("replyObjNickname")
    private String replyObjNickname;//回复对象用户昵称

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
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

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public long getReplyObjId() {
        return replyObjId;
    }

    public void setReplyObjId(long replyObjId) {
        this.replyObjId = replyObjId;
    }

    public String getReplyObjContent() {
        return replyObjContent;
    }

    public void setReplyObjContent(String replyObjContent) {
        this.replyObjContent = replyObjContent;
    }

    public long getReplyObjUserId() {
        return replyObjUserId;
    }

    public void setReplyObjUserId(long replyObjUserId) {
        this.replyObjUserId = replyObjUserId;
    }

    public String getReplyObjNickname() {
        return replyObjNickname;
    }

    public void setReplyObjNickname(String replyObjNickname) {
        this.replyObjNickname = replyObjNickname;
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "replyId=" + replyId +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", replyType=" + replyType +
                ", replyObjId=" + replyObjId +
                ", replyObjContent='" + replyObjContent + '\'' +
                ", replyObjUserId=" + replyObjUserId +
                ", replyObjNickname='" + replyObjNickname + '\'' +
                '}';
    }
}

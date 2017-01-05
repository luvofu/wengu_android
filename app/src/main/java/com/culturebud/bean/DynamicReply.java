package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XieWei on 2016/10/28.
 */

public class DynamicReply {

    @SerializedName("replyId")
    private long replyId;//回复id

    @SerializedName("replyType")
    private int replyType;//回复类型

    @SerializedName("content")
    private String content;//回复内容

    @SerializedName("createdTime")
    private long createdTime;//回复时间

    @SerializedName("userId")
    private long userId;//用户id

    @SerializedName("nickname")
    private String nickname;//用户昵称

    @SerializedName("avatar")
    private String avatar;//用户头像

    @SerializedName("replyObj")
    private DynamicReplyObj replyObj;//回复对象

    private List<DynamicReply> replies;

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
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

    public DynamicReplyObj getReplyObj() {
        return replyObj;
    }

    public void setReplyObj(DynamicReplyObj replyObj) {
        this.replyObj = replyObj;
    }

    public List<DynamicReply> getReplies() {
        return replies;
    }

    public void setReplies(List<DynamicReply> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "DynamicReply{" +
                "replyId=" + replyId +
                ", replyType=" + replyType +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", replyObj=" + replyObj +
                '}';
    }
}

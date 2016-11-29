package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/10/28.
 */

public class DynamicReplyObj {

    @SerializedName("replyId")
    private long replyId;//回复id

    @SerializedName("content")
    private String content;//回复内容

    @SerializedName("userId")
    private long userId;//用户id

    @SerializedName("nickname")
    private String nickname;//用户昵称

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

    @Override
    public String toString() {
        return "DynamicReplyObj{" +
                "replyId=" + replyId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}

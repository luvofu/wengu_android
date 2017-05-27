package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/16.
 */

public class UserMessage {

    @SerializedName("messageId")
    private long messageId;//消息id

    @SerializedName("messageType")
    private int messageType;//消息类型：好友邀请FriendInvite(0),书桌邀请DeskInvite(1),
    // 社区回复CommunityReply(2),书圈回复BookCircleReply(3);

    @SerializedName("messageObjId")
    private long messageObjId;//消息对象id

    @SerializedName("content")
    private String content;//内容

    @SerializedName("readStatus")
    private int readStatus;//读状态：未读NotRead(0),已读Read(1);

    @SerializedName("dealStatus")
    private int dealStatus;//处理状态：未处理NotDeal(0),接受Accept(1),拒绝Refuse(2);

    @SerializedName("createdTime")
    private long createdTime;//创建时间

    //发送消息用户id
    @SerializedName("sendUserId")
    private long sendUserId;//发送消息用户id

    @SerializedName("avatar")
    private String avatar;//头像

    @SerializedName("nickname")
    private String nickname;//昵称

    @SerializedName("msgLinkType")
    private Integer msgLinkType;

    @SerializedName("msgLinkId")
    private Long msgLinkId;

    @SerializedName("msgLinkContent")
    private String msgLinkContent;

    @SerializedName("msgLinkImage")
    private String msgLinkImage;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getMessageObjId() {
        return messageObjId;
    }

    public void setMessageObjId(long messageObjId) {
        this.messageObjId = messageObjId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(int dealStatus) {
        this.dealStatus = dealStatus;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(long sendUserId) {
        this.sendUserId = sendUserId;
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

    public Integer getMsgLinkType() {
        return msgLinkType;
    }

    public void setMsgLinkType(Integer msgLinkType) {
        this.msgLinkType = msgLinkType;
    }

    public Long getMsgLinkId() {
        return msgLinkId;
    }

    public void setMsgLinkId(Long msgLinkId) {
        this.msgLinkId = msgLinkId;
    }

    public String getMsgLinkContent() {
        return msgLinkContent;
    }

    public void setMsgLinkContent(String msgLinkContent) {
        this.msgLinkContent = msgLinkContent;
    }

    public String getMsgLinkImage() {
        return msgLinkImage;
    }

    public void setMsgLinkImage(String msgLinkImage) {
        this.msgLinkImage = msgLinkImage;
    }
}

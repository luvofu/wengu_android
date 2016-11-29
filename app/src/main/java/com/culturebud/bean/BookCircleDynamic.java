package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XieWei on 2016/10/28.
 */

public class BookCircleDynamic {
    //dynamic
    @SerializedName("dynamicId")
    private long dynamicId;//动态id

    @SerializedName("content")
    private String content;//内容

    @SerializedName("image")
    private String image;//图片

    @SerializedName("location")
    private String location;//位置

    @SerializedName("dynamicType")
    private int dynamicType ;//动态类型：自定义Personal(0), 系统System(1);

    @SerializedName("goodNum")
    private long goodNum ;//点赞数

    @SerializedName("replyNum")
    private long replyNum ;//回复数

    @SerializedName("createdTime")
    private long createdTime;//创建时间

    //user
    @SerializedName("userId")
    private long userId;//动态用户id

    @SerializedName("nickname")
    private String nickname;//昵称

    @SerializedName("avatar")
    private String avatar;//头像

    //linkcontent
    @SerializedName("linkType")
    private int linkType;//链接类型：普通Common(0),书籍Book(1),书单BookSheet(2),评论Comment(3);

    @SerializedName("linkId")
    private long linkId;//链接对象id

    //book
    @SerializedName("title")
    private String title;//书名

    @SerializedName("subTitle")
    private String subTitle;//副标题

    @SerializedName("author")
    private String author;//作者

    @SerializedName("bookCover")
    private String bookCover;//书籍封面

    //booksheet
    @SerializedName("name")
    private String name;//书单名

    @SerializedName("bookSheetCover")
    private String bookSheetCover;//书单封面

    //comment
    @SerializedName("commentId")
    private long commentId;//评论id

    @SerializedName("commentDelete")
    private boolean commentDelete;//删除状态

    @SerializedName("commentContent")
    private String commentContent;//评论内容

    @SerializedName("commentUserId")
    private long commentUserId;//评论用户id

    @SerializedName("commentNickname")
    private String commentNickname;//评论用户昵称

    @SerializedName("communityId")
    private long communityId;//社区id

    @SerializedName("communityTitle")
    private String communityTitle;//社区书名

    @SerializedName("communitySubTitle")
    private String communitySubTitle;//社区副标题

    @SerializedName("communityAuthor")
    private String communityAuthor;//社区书籍作者
    //good
    @SerializedName("good")
    private boolean good ;//用户点赞状态

    List<DynamicReply> dynamicReplyList;//回复列表

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public long getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(long goodNum) {
        this.goodNum = goodNum;
    }

    public long getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(long replyNum) {
        this.replyNum = replyNum;
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

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookSheetCover() {
        return bookSheetCover;
    }

    public void setBookSheetCover(String bookSheetCover) {
        this.bookSheetCover = bookSheetCover;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public boolean isCommentDelete() {
        return commentDelete;
    }

    public void setCommentDelete(boolean commentDelete) {
        this.commentDelete = commentDelete;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public long getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(long commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentNickname() {
        return commentNickname;
    }

    public void setCommentNickname(String commentNickname) {
        this.commentNickname = commentNickname;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityTitle() {
        return communityTitle;
    }

    public void setCommunityTitle(String communityTitle) {
        this.communityTitle = communityTitle;
    }

    public String getCommunitySubTitle() {
        return communitySubTitle;
    }

    public void setCommunitySubTitle(String communitySubTitle) {
        this.communitySubTitle = communitySubTitle;
    }

    public String getCommunityAuthor() {
        return communityAuthor;
    }

    public void setCommunityAuthor(String communityAuthor) {
        this.communityAuthor = communityAuthor;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public List<DynamicReply> getDynamicReplyList() {
        return dynamicReplyList;
    }

    public void setDynamicReplyList(List<DynamicReply> dynamicReplyList) {
        this.dynamicReplyList = dynamicReplyList;
    }

    @Override
    public String toString() {
        return "BookCircleDynamic{" +
                "dynamicId=" + dynamicId +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", location='" + location + '\'' +
                ", dynamicType=" + dynamicType +
                ", goodNum=" + goodNum +
                ", replyNum=" + replyNum +
                ", createdTime=" + createdTime +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", linkType=" + linkType +
                ", linkId=" + linkId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", author='" + author + '\'' +
                ", bookCover='" + bookCover + '\'' +
                ", name='" + name + '\'' +
                ", bookSheetCover='" + bookSheetCover + '\'' +
                ", commentId=" + commentId +
                ", commentDelete=" + commentDelete +
                ", commentContent='" + commentContent + '\'' +
                ", commentUserId=" + commentUserId +
                ", commentNickname='" + commentNickname + '\'' +
                ", communityId=" + communityId +
                ", communityTitle='" + communityTitle + '\'' +
                ", communitySubTitle='" + communitySubTitle + '\'' +
                ", communityAuthor='" + communityAuthor + '\'' +
                ", good=" + good +
                ", dynamicReplyList=" + dynamicReplyList +
                '}';
    }
}

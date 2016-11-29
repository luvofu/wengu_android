package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/2.
 */

public class BookCommunityDetail {

    @SerializedName("communityId")
    private long communityId;//社区id

    @SerializedName("title")
    private String title;//书名

    @SerializedName("subTitle")
    private String subTitle;//副标题

    @SerializedName("author")
    private String author;//作者

    @SerializedName("commentNum")
    private int commentNum;//评论数

    @SerializedName("heat")
    private float heat;//热度

    @SerializedName("background")
    private String background;//社区背景图url

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public float getHeat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "BookCommunityDetail{" +
                "communityId=" + communityId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", author='" + author + '\'' +
                ", commentNum=" + commentNum +
                ", heat=" + heat +
                ", background='" + background + '\'' +
                '}';
    }
}

package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/23.
 */

public class Note {
    @SerializedName("noteId")
    private long noteId; //笔记id

    @SerializedName("chapter")
    private String chapter; //章节

    @SerializedName("pages")
    private int pages; //页码

    @SerializedName("otherLocation")
    private String otherLocation; //其他位置信息

    @SerializedName("image")
    private String image;

    @SerializedName("content")
    private String content;

    @SerializedName("updatedTime")
    private long updatedTime;

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getOtherLocation() {
        return otherLocation;
    }

    public void setOtherLocation(String otherLocation) {
        this.otherLocation = otherLocation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", chapter='" + chapter + '\'' +
                ", pages=" + pages +
                ", otherLocation='" + otherLocation + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}

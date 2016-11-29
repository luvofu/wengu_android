package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/11/20.
 */

public class Notebook {

    @SerializedName("notebookId")
    private long notebookId; //笔记本id

    @SerializedName("cover")
    private String cover; //笔记本封面

    @SerializedName("title")
    private String title; //书名

    @SerializedName("name")
    private String name; //笔记本名

    @SerializedName("noteNum")
    private long noteNum; //笔记数

    @SerializedName("createdTime")
    private long createdTime; //创建时间

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(long noteNum) {
        this.noteNum = noteNum;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "notebookId=" + notebookId +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", noteNum=" + noteNum +
                ", createdTime=" + createdTime +
                '}';
    }
}

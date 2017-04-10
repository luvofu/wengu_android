package com.culturebud.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2017/4/10.
 */

@DatabaseTable(tableName = "t_history_tag")
public class HistoryTag {
    @DatabaseField(columnName = "_id", generatedId = true)
    private long id;
    @DatabaseField(columnName = "t_type")
    private byte type;//0表示书籍历史标签，1表示书单历史标签
    @DatabaseField(columnName = "tag_name")
    private String tag;
    @DatabaseField(columnName = "user_id")
    private long userId;

    public HistoryTag() {
    }

    public HistoryTag(byte type, String tag) {
        this.type = type;
        this.tag = tag;
    }

    public HistoryTag(long id, byte type, String tag) {
        this.id = id;
        this.type = type;
        this.tag = tag;
    }

    public HistoryTag(byte type, String tag, long userId) {
        this.type = type;
        this.tag = tag;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "HistoryTag{" +
                "id=" + id +
                ", type=" + type +
                ", tag='" + tag + '\'' +
                ", userId=" + userId +
                '}';
    }
}

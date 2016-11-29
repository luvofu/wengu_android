package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/11/5.
 */
@DatabaseTable(tableName = "t_search_keyword")
public class SearchKeyword {

    @SerializedName("id")
    @DatabaseField(columnName = "_id", generatedId = true)
    private int id;

    @SerializedName("type")
    @DatabaseField(columnName = "search_type")
    private int type;

    @SerializedName("keyword")
    @DatabaseField(columnName = "search_keyword", unique = true)
    private String keyword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "SearchKeyword{" +
                "id=" + id +
                ", type=" + type +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public static class SKType {
        public static final int TYPE_BOOK = 0;
        public static final int TYPE_COMMUNITY = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchKeyword that = (SearchKeyword) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        return keyword.equals(that.keyword);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type;
        result = 31 * result + keyword.hashCode();
        return result;
    }
}

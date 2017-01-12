package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XieWei on 2017/1/12.
 */

public class BookCircleDynamicRelationMe {
    @SerializedName("dynamic")
    private BookCircleDynamic dynamic;
    @SerializedName("newReply")
    private DynamicReply newReply;
    @SerializedName("dynamicReplyList")
    private List<DynamicReply> replies;

    public BookCircleDynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(BookCircleDynamic dynamic) {
        this.dynamic = dynamic;
    }

    public DynamicReply getNewReply() {
        return newReply;
    }

    public void setNewReply(DynamicReply newReply) {
        this.newReply = newReply;
    }

    public List<DynamicReply> getReplies() {
        return replies;
    }

    public void setReplies(List<DynamicReply> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "BookCircleDynamicRelationMe{" +
                "dynamic=" + dynamic +
                ", newReply=" + newReply +
                ", replies=" + replies +
                '}';
    }
}

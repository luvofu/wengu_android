package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XieWei on 2016/11/19.
 */

public class MyRelatedComment {
    @SerializedName("newReply")
    private CommentReply newReply;

    @SerializedName("comment")
    private Comment comment;

    @SerializedName("commentReplyList")
    private List<CommentReply> commentReplyList;

    public CommentReply getNewReply() {
        return newReply;
    }

    public void setNewReply(CommentReply newReply) {
        this.newReply = newReply;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<CommentReply> getCommentReplyList() {
        return commentReplyList;
    }

    public void setCommentReplyList(List<CommentReply> commentReplyList) {
        this.commentReplyList = commentReplyList;
    }

    @Override
    public String toString() {
        return "MyRelatedComment{" +
                "newReply=" + newReply +
                ", comment=" + comment +
                ", commentReplyList=" + commentReplyList +
                '}';
    }
}

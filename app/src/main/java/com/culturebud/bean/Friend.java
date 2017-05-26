package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XieWei on 2016/10/25.
 */
public class Friend {

    @SerializedName("userId")
    private long userId;//用户id

    @SerializedName("nickname")
    private String nickname;//nic

    @SerializedName("avatar")
    private String avatar;//头像


    @SerializedName("concernNum")
    private long concernNum;    //	关注数

    @SerializedName("fanNum")
    private long fanNum;//	粉丝数

    @SerializedName("concernStatus")
    private int concernStatus;//关注状态：未相互关注NOEachConcern(0), 单向关注SingleConcern(1), 单向被关注SingleBeConcerned(2), 互相关注MutualConcern(3);


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

    public long getConcernNum() {
        return concernNum;
    }

    public void setConcernNum(long concernNum) {
        this.concernNum = concernNum;
    }

    public long getFanNum() {
        return fanNum;
    }

    public void setFanNum(long fanNum) {
        this.fanNum = fanNum;
    }

    public int getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(int concernStatus) {
        this.concernStatus = concernStatus;
    }
}

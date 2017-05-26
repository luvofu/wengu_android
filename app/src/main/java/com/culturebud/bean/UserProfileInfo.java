package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Dana on 17/5/26.
 */

public class UserProfileInfo extends User {
    @SerializedName("concernStatus")
    private int concernStatus;  //关注状态. 默认是未关注.

    public int getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(int concernStatus) {
        this.concernStatus = concernStatus;
    }

}

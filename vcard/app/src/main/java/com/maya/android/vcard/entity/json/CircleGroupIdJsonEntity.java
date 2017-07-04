package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 群讨论组Id 实体类
 * Created by Administrator on 2015/10/28.
 */
public class CircleGroupIdJsonEntity {
    /** 讨论组id **/
    @SerializedName("messageGroupId")
    private long groupId;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}

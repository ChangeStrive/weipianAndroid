package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2015/11/9.
 */
public class CreateCircleGroupResultEntity {
    /** 聊天组Id **/
    @SerializedName("messageGroupId")
    private long messageGroupId;

    public long getMessageGroupId() {
        return messageGroupId;
    }

    public void setMessageGroupId(long messageGroupId) {
        this.messageGroupId = messageGroupId;
    }
}

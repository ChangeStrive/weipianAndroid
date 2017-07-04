package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.result.MessageResultEntity;

import java.util.ArrayList;

/**
 * 转发名片请求类
 * Created by Administrator on 2015/11/10.
 */
public class CardcaseChooseFrowardVCardJsonEntity {
    @SerializedName("messageParamList")
    private ArrayList<MessageResultEntity> messageParamList;

    public ArrayList<MessageResultEntity> getMessageParamList() {
        return messageParamList;
    }

    public void setMessageParamList(ArrayList<MessageResultEntity> messageParamList) {
        this.messageParamList = messageParamList;
    }
}

package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 请求名片联系人实体类
 * Created by Administrator on 2015/11/4.
 */
public class UserInfoContactJsonEntity {
    @SerializedName("cardId")
    private long cardId;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}

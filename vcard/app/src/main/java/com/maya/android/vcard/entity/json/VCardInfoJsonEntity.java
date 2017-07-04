package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 请求联系人详细信息
 * Created by Administrator on 2015/11/17.
 */
public class VCardInfoJsonEntity {
    @SerializedName("cardId")
    private long cardId;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}

package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 附近的人脉请求实体类
 * Created by Administrator on 2015/11/3.
 */
public class UserNearbyPeopleJsonEntity {
    @SerializedName("cardId")
    private long cardId;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}

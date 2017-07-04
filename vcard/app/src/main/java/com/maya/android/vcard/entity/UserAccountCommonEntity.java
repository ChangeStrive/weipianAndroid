package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2015/11/6.
 */
public class UserAccountCommonEntity {
    @SerializedName("accountId")
    private long accountId;
    @SerializedName("cardId")
    private long cardId;
    @SerializedName("headImg")
    private String headImg;
    @SerializedName("displayName")
    private String displayName;


    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}

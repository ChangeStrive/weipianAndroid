package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 交换返回列表实体类
 * Created by Administrator on 2015/11/6.
 */
public class SwapCardCommitResultEntity {
    @SerializedName("message")
    private String message;
    @SerializedName("usedCardCount")
    private int userCardCount;
    @SerializedName("cardIdList")
    private ArrayList<Long> cardIdList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserCardCount() {
        return userCardCount;
    }

    public void setUserCardCount(int userCardCount) {
        this.userCardCount = userCardCount;
    }

    public ArrayList<Long> getCardIdList() {
        return cardIdList;
    }

    public void setCardIdList(ArrayList<Long> cardIdList) {
        this.cardIdList = cardIdList;
    }
}

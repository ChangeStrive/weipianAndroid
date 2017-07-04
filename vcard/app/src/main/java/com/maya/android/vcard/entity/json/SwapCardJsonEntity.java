package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * JsonEntity：我的名片：名片交换
 * Created by Administrator on 2015/11/6.
 */
public class SwapCardJsonEntity {

    @SerializedName("cardId")
    private long cardId;
    @SerializedName("cardIdList")
    private ArrayList<Long> cardIdList;
    @SerializedName("swapWay")
    private int swapWay;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public ArrayList<Long> getCardIdList() {
        return cardIdList;
    }

    public void setCardIdList(ArrayList<Long> cardIdList) {
        this.cardIdList = cardIdList;
    }

    public int getSwapWay() {
        return swapWay;
    }

    public void setSwapWay(int swapWay) {
        this.swapWay = swapWay;
    }
}

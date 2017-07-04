package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 *  短信推荐  or 分享 请求链接的 接口实体
 * Created by Administrator on 2015/11/5.
 */
public class MessInviteJsonEntity {

    public static final int SHARE_TYPE_SMS = 0;
    public static final int SHARE_TYPE_CARD = 1;

    /**推荐人名片id**/
    @SerializedName("cardId")
    private long cardId;
    /**好友手机，获取短信推荐链接时需要传好友的手机号码**/
    @SerializedName("fMobileArr")
    private String[] fMobileArr;
    /**0.表示短信推荐，1.表示分享我的名片**/
    @SerializedName("shareType")
    private int shareType;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String[] getfMobileArr() {
        return fMobileArr;
    }

    public void setfMobileArr(String[] fMobileArr) {
        this.fMobileArr = fMobileArr;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }
}

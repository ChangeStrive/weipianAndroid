package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 发布微公告实体类
 * Created by Administrator on 2015/10/28.
 */
public class UnitNotifyPublishJsonEntity {
    /**
     * 企业Id
     */
    @SerializedName("enterpriseId")
    private long enterpriseId;
    /**
     * 公告文字内容
     */
    @SerializedName("content")
    private String content;
    /**
     * 公告图片
     */
    @SerializedName("image")
    private String image;

    /**
     * 成员角色
     */
    @SerializedName("cardId")
    private long cardId;

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}

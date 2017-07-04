package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 单位简介发布实体类
 * Created by Administrator on 2015/10/28.
 */
public class UnitDetailIntroJsonEntity {
    @SerializedName("enterpriseId")
    protected Long enterpriseId;
    /** 企业简介  */
    @SerializedName("enterpriseAbout")
    protected String enterpriseAbout;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseAbout() {
        return enterpriseAbout;
    }

    public void setEnterpriseAbout(String enterpriseAbout) {
        this.enterpriseAbout = enterpriseAbout;
    }
}

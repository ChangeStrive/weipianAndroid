package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 单位公告上传参数实体类
 * Created by Administrator on 2015/10/27.
 */
public class UnitNoticeJsonEntity {

    @SerializedName("enterpriseId")
    protected Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}

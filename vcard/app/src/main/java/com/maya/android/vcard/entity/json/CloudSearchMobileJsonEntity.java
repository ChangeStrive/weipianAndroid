package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 通过手机号码查找
 * Created by Administrator on 2015/11/9.
 */
public class CloudSearchMobileJsonEntity {
    @SerializedName("mobileTel")
    private String mobileTel;

    public String getMobileTel() {
        return mobileTel;
    }

    public void setMobileTel(String mobileTel) {
        this.mobileTel = mobileTel;
    }
}

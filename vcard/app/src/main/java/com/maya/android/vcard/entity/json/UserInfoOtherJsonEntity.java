package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 别人详细资料上传实体类
 * Created by Administrator on 2015/11/3.
 */
public class UserInfoOtherJsonEntity {
    @SerializedName("vcardNo")
    private String vcardNo;

    public String getVcardNo() {
        return vcardNo;
    }

    public void setVcardNo(String vcardNo) {
        this.vcardNo = vcardNo;
    }
}

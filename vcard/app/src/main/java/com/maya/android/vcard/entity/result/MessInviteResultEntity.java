package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 接收服务端返回的 短信推荐的链接实体
 * Created by Administrator on 2015/11/5.
 */
public class MessInviteResultEntity {
    /** 名片链接 **/
    @SerializedName("url")
    private String url;
    /** 手机号 **/
    @SerializedName("mobile")
    private String mobile;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

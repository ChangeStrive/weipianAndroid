package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 获取短信分享的链接实体
 * Created by Administrator on 2015/11/5.
 */
public class MessInviteListResultEntity {
    @SerializedName("urlList")
    private ArrayList<MessInviteResultEntity> urlList;

    public ArrayList<MessInviteResultEntity> getUrlList() {
        return urlList;
    }

    public void setUrlList(ArrayList<MessInviteResultEntity> urlList) {
        this.urlList = urlList;
    }
}

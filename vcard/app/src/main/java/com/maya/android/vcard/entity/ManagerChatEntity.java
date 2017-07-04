package com.maya.android.vcard.entity;

/**
 * 聊天记录管理实体类
 * Created by Administrator on 2015/11/11.
 */
public class ManagerChatEntity {
    /** 聊天头像 **/
    private String headImg;
    /** 聊天名称 **/
    private String displayName;
    /** 聊天内容大小 **/
    private String size;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}

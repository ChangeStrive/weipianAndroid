package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 应用中心实体类
 * Created by Administrator on 2015/8/31.
 */
public class UserAppCenterEntity implements Cloneable{
    /** 应用图标Id **/
    @SerializedName("iconId")
    private int iconId;
    /** 不可点击应用图标Id **/
    @SerializedName("iconGayId")
    private int iconGayId;
    /** 应用图标 链接**/
    private String iconUrl;
    /** 应用名称Id **/
    @SerializedName("name")
    private String name;
    /** 应用内容介绍 */
    @SerializedName("description")
    private String description;
    /** 应用地址 **/
    @SerializedName("url")
    private String url;
    /** 操作图标资源 id**/
    private int operateResId;


    public int getIconGayId() {
        return iconGayId;
    }

    public void setIconGayId(int iconGayId) {
        this.iconGayId = iconGayId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOperateResId() {
        return operateResId;
    }

    public void setOperateResId(int operateResId) {
        this.operateResId = operateResId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

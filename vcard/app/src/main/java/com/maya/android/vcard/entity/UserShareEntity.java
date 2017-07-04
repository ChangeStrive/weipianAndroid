package com.maya.android.vcard.entity;

/**
 * 用户分享 名片、软件实体类
 * Created by Administrator on 2015/9/18.
 */
public class UserShareEntity implements Cloneable{
    private int drawableId;
    private int shareNameId;
    private String shareContent;
    private String activityName;
    private String packageName;

    private String type;//判断属于什么类型1、短信分享   2、腾讯    3.新浪

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public UserShareEntity(){

    }

    public UserShareEntity(int drawableId, int shareName, String shareContent, String activityName, String packageName){
        this.drawableId = drawableId;
        this.shareContent = shareContent;
        this.shareNameId =shareName;
        this.activityName = activityName;
        this.packageName = packageName;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }


    public int getShareNameId() {
        return shareNameId;
    }

    public void setShareNameId(int shareNameId) {
        this.shareNameId = shareNameId;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

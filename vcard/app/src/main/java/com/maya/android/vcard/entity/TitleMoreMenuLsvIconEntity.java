package com.maya.android.vcard.entity;

/**
 * 标题栏右侧更多 带Icon图标信息实体类
 * Created by Administrator on 2015/9/6.
 */
public class TitleMoreMenuLsvIconEntity implements Cloneable{
    private int iconId;
    private int iconGayId;
    private int businessName;

    public TitleMoreMenuLsvIconEntity(){

    }

    public TitleMoreMenuLsvIconEntity(int businessName,int iconId){
        this.iconId = iconId;
        this.businessName = businessName;
    }

    public TitleMoreMenuLsvIconEntity(int businessName,int iconId, int iconGayId){
        this.iconId = iconId;
        this.businessName = businessName;
        this.iconGayId = iconGayId;
    }

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

    public int getBusinessName() {
        return businessName;
    }

    public void setBusinessName(int businessName) {
        this.businessName = businessName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

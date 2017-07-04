package com.maya.android.vcard.entity;

/**
 * 选择对话框 带Icon图标信息实体类
 * Created by Administrator on 2015/9/6.
 */
public class CustomLsvIconEntity implements Cloneable{
    private int iconId;
    private String businessName;

    public CustomLsvIconEntity(){

    }

    public CustomLsvIconEntity(int iconId, String businessName){
        this.iconId = iconId;
        this.businessName = businessName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

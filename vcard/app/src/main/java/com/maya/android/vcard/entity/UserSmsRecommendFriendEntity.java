package com.maya.android.vcard.entity;

/**
 * 短信推荐好友
 * Created by Administrator on 2015/10/21.
 */
public class UserSmsRecommendFriendEntity implements Cloneable{
    /** 电话号码 **/
    private String phoneNumber;
    /** 勾选 **/
    private Boolean isChecked;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

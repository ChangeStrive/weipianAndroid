package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YongJi on 2015/11/25.
 */
public class LoginSimpleInfoListEntity {
    @SerializedName("loginSimpleInfoEntityList")
    private ArrayList<LoginSimpleInfoEntity> loginSimpleInfoEntityList;

    public ArrayList<LoginSimpleInfoEntity> getLoginSimpleInfoEntityList() {
        return loginSimpleInfoEntityList;
    }

    public void setLoginSimpleInfoEntityList(ArrayList<LoginSimpleInfoEntity> loginSimpleInfoEntityList) {
        this.loginSimpleInfoEntityList = loginSimpleInfoEntityList;
    }
}

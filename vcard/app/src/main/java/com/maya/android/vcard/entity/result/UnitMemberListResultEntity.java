package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/16.
 */
public class UnitMemberListResultEntity {
    @SerializedName("memberList")
    private ArrayList<UnitMemberResultEntity> memberList;

    public ArrayList<UnitMemberResultEntity> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<UnitMemberResultEntity> memberList) {
        this.memberList = memberList;
    }
}

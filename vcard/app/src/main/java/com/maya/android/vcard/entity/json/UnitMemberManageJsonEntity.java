package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * 成员管理实体类
 * Created by Administrator on 2015/10/28.
 */
public class UnitMemberManageJsonEntity {

    /** 单位id **/
    @SerializedName("enterpriseId")
    private long enterpriseId;
    /** 角色操作 **/
    @SerializedName("roleVal")
    private int role;
    /** 成员id 列表 **/
    @SerializedName("memberIdList")
    private ArrayList<Long> memberIdList;

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public ArrayList<Long> getMemberIdList() {
        return memberIdList;
    }

    public void setMemberIdList(ArrayList<Long> memberIdList) {
        this.memberIdList = memberIdList;
    }
}

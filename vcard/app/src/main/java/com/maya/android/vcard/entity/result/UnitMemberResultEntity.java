package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 企业成员信息实体类
 * Created by Administrator on 2015/9/7.
 */
public class UnitMemberResultEntity {
    /** 成员ID **/
    @SerializedName("id")
    private long id;
    /** 成员角色 角色说明：0-普通成员；1-管理员；2-超级管理员（群组创建者） **/
    @SerializedName("role")
    private int role;
    /** 成员微片帐户ID **/
    @SerializedName("accountId")
    private long accountId;
    /** 姓氏 **/
    @SerializedName("surname")
    private String surname;
    /** 名字 **/
    @SerializedName("firstName")
    private String firstName;
    /** 昵称 **/
    @SerializedName("nickName")
    private String nickName;
    /** 成员名片ID **/
    @SerializedName("cardId")
    private long cardId;
    /** 成员群昵称、备注 **/
    @SerializedName("description")
    private String description;
    /** 头像 **/
    @SerializedName("headImg")
    private String headImg;
    /** 职位 **/
    @SerializedName("job")
    private String job;
    /** 微片号 **/
    @SerializedName("vcardNumber")
    private String vcardNumber;
    /** 企业Id **/
    @SerializedName("enterpriseId")
    private long enterpriseId;

    /**
     * 获取成员名字(如果有昵称则选择昵称)
     * @return
     */
    public String getDisplayName() {
        String displayName = "";
        if(ResourceHelper.isNotEmpty(this.nickName)){
            displayName = this.nickName;
        }else{
            if(ResourceHelper.isNotEmpty(this.surname)){
                displayName += this.surname;
            }
            if(ResourceHelper.isNotEmpty(this.firstName)){
                displayName += this.firstName;
            }
        }
        return displayName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getVcardNumber() {
        return vcardNumber;
    }

    public void setVcardNumber(String vcardNumber) {
        this.vcardNumber = vcardNumber;
    }

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}

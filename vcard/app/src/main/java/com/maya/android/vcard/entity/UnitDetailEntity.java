package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 单位信息实体类
 * Created by Administrator on 2015/9/16.
 */
public class UnitDetailEntity {
    /** 企业头像墙  */
    @SerializedName("headImg")
    private String headImg;
    /**  等级  */
    @SerializedName("grade")
    private Integer grade;

    /** 企业名称  */
    @SerializedName("enterpriseName")
    private String enterpriseName;

    /**  创建时间 */
    @SerializedName("createTime")
    private String createTime;
    /** 创始人  */
    @SerializedName("creater")
    private String creater;
    /** 是否公开  */
    @SerializedName("isOpen")
    private boolean isOpen;
    /**
     * 是否认证
     * -1:未认证
     * 0:未审核
     * 1:已认证
     */
    @SerializedName("approvalStatus")
    private int approval;

    /** 成员数量  */
    @SerializedName("memberCount")
    private int memberCount;
    /** 聊天组id **/
    @SerializedName("messageGroupId")
    private long messageGroupId;
    /** 已请求成员次数  */
    @SerializedName("hadRequestMember")
    private int hadRequestMember;
    @SerializedName("enterpriseId")
    protected Long enterpriseId;
    /** 地址 */
    @SerializedName("address")
    protected String address;
    /** 分类标签  (没修改传-1)*/
    @SerializedName("classLabel")
    protected Integer classLabel;
    /** 企业简介  */
    @SerializedName("enterpriseAbout")
    protected String enterpriseAbout;
    /** 公告 */
    @SerializedName("announcement")
    protected String announcement;
    /** 单位网址 **/
    @SerializedName("enterpriseWeb")
    protected String enterpriseWeb;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public long getMessageGroupId() {
        return messageGroupId;
    }

    public void setMessageGroupId(long messageGroupId) {
        this.messageGroupId = messageGroupId;
    }

    public int getHadRequestMember() {
        return hadRequestMember;
    }

    public void setHadRequestMember(int hadRequestMember) {
        this.hadRequestMember = hadRequestMember;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnterpriseAbout() {
        return enterpriseAbout;
    }

    public void setEnterpriseAbout(String enterpriseAbout) {
        this.enterpriseAbout = enterpriseAbout;
    }

    public Integer getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(Integer classLabel) {
        this.classLabel = classLabel;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getEnterpriseWeb() {
        return enterpriseWeb;
    }

    public void setEnterpriseWeb(String enterpriseWeb) {
        this.enterpriseWeb = enterpriseWeb;
    }
}

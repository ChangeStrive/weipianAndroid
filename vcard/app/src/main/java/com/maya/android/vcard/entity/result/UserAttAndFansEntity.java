package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * ResultEntity:粉丝/关注 实体
 * Created by Administrator on 2015/8/31.
 */
public class UserAttAndFansEntity {
    @SerializedName("vcardNo")
    private String vcardNo;
    @SerializedName("surnname")
    private String surnname;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("headImg")
    private String headImg;
    @SerializedName("companyName")
    private String companyName;
    @SerializedName("job")
    private String job;
    @SerializedName("buisness")
    private int buisness;
    @SerializedName("attentionStatus")
    private int attentionStatus;

    public String getVcardNo() {
        return vcardNo;
    }

    public void setVcardNo(String vcardNo) {
        this.vcardNo = vcardNo;
    }

    public String getSurnname() {
        return surnname;
    }

    public void setSurnname(String surnname) {
        this.surnname = surnname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getAttentionStatus() {
        return attentionStatus;
    }

    public void setAttentionStatus(int attentionStatus) {
        this.attentionStatus = attentionStatus;
    }

    public int getBuisness() {
        return buisness;
    }

    public void setBuisness(int buisness) {
        this.buisness = buisness;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

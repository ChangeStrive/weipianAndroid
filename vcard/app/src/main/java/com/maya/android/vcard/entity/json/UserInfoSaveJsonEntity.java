package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;

/**
 * JsonEntity:保存用户资料
 * Created by YongJi on 2015/8/27.
 */
public class UserInfoSaveJsonEntity {
    /**
     * 姓氏
     */
    @SerializedName("surname")
    private String surname;
    /**
     * 名字
     */
    @SerializedName("firstName")
    private String firstName;
    /**
     * 个性签名
     */
    @SerializedName("selfSign")
    private String selfSign;

    /**
     * 血型
     */
    @SerializedName("bloodType")
    private int bloodType;

    /**
     * 学历
     */
    @SerializedName("eduBackground")
    private String eduBackground;

    /**
     * 性别
     */
    @SerializedName("sex")
    private int sex;

    /**
     * 毕业院校
     */
    @SerializedName("school")
    private String school;

    /**
     * 个人简介
     */
    @SerializedName("intro")
    private String intro;

    /**
     * 出生日期
     */
    @SerializedName("birthday")
    private String birthday;

    /**
     * 头像
     */
    @SerializedName("headImg")
    private String headImg;

    /**
     * 省份
     */
    @SerializedName("province")
    private String province;

    /**
     * 城市
     */
    @SerializedName("country")
    private String country;

    /**
     * 籍贯
     */
    @SerializedName("nativeplace")
    private String nativeplace;

    /**
     * 授权的access token
     */
    @SerializedName("accessToken")
    private String accessToken;

    /**
     * QQ号
     */
    @SerializedName("tencentQQNo")
    private String tencentQQNo;

    /**
     * QQ链接
     */
    @SerializedName("tencentQQUrl")
    private String tencentQQUrl;

    /**
     * 新浪微博号
     */
    @SerializedName("sinaWeiboNo")
    private String sinaWeiboNo;

    /**
     * 新浪微博链接
     */
    @SerializedName("sinaWeiboUrl")
    private String sinaWeiboUrl;

    /**
     * 腾讯微博
     */
    @SerializedName("tencentWeiboNo")
    private String tencentWeiboNo;

    /**
     * 腾讯微博链接
     */
    @SerializedName("tencentWeiboUrl")
    private String tencentWeiboUrl;

    /**
     * 工作经验
     */
    @SerializedName("workHistory")
    private String workHistory;

    /**
     * 教育信息
     */
    @SerializedName("eduInfo")
    private String eduInfo;

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

    public String getSelfSign() {
        return selfSign;
    }

    public void setSelfSign(String selfSign) {
        this.selfSign = selfSign;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getEduBackground() {
        return eduBackground;
    }

    public void setEduBackground(String eduBackground) {
        this.eduBackground = eduBackground;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTencentQQUrl() {
        return tencentQQUrl;
    }

    public void setTencentQQUrl(String tencentQQUrl) {
        this.tencentQQUrl = tencentQQUrl;
    }

    public String getTencentQQNo() {
        return tencentQQNo;
    }

    public void setTencentQQNo(String tencentQQNo) {
        this.tencentQQNo = tencentQQNo;
    }

    public String getSinaWeiboNo() {
        return sinaWeiboNo;
    }

    public void setSinaWeiboNo(String sinaWeiboNo) {
        this.sinaWeiboNo = sinaWeiboNo;
    }

    public String getSinaWeiboUrl() {
        return sinaWeiboUrl;
    }

    public void setSinaWeiboUrl(String sinaWeiboUrl) {
        this.sinaWeiboUrl = sinaWeiboUrl;
    }

    public String getTencentWeiboNo() {
        return tencentWeiboNo;
    }

    public void setTencentWeiboNo(String tencentWeiboNo) {
        this.tencentWeiboNo = tencentWeiboNo;
    }

    public String getTencentWeiboUrl() {
        return tencentWeiboUrl;
    }

    public void setTencentWeiboUrl(String tencentWeiboUrl) {
        this.tencentWeiboUrl = tencentWeiboUrl;
    }

    public String getWorkHistory() {
        return workHistory;
    }

    public void setWorkHistory(String workHistory) {
        this.workHistory = workHistory;
    }

    public String getEduInfo() {
        return eduInfo;
    }

    public void setEduInfo(String eduInfo) {
        this.eduInfo = eduInfo;
    }

    public UserInfoSaveJsonEntity(){}

    public UserInfoSaveJsonEntity(UserInfoResultEntity entity){
        this.surname = entity.getSurname();
        this.firstName = entity.getFirstName();
        this.selfSign = entity.getSelfSign();
        this.bloodType = entity.getBloodType();
        this.eduBackground = entity.getEduBackground();
        this.sex = entity.getSex();
        this.school = entity.getSchool();
        this.intro = entity.getIntro();
        this.birthday = entity.getBirthday();
        this.headImg = entity.getHeadImg();
        this.province = entity.getProvince();
        this.country = entity.getCountry();
        this.nativeplace = entity.getNativeplace();
        this.tencentQQNo = entity.getTencentQQNo();
        this.tencentQQUrl = entity.getTencentQQUrl();
        this.sinaWeiboNo = entity.getSinaBlogNo();
        this.sinaWeiboUrl = entity.getSinaBlogUrl();
        this.tencentWeiboNo = entity.getTencentBlogNo();
        this.tencentWeiboUrl = entity.getTencentBlogUrl();
        this.workHistory = entity.getWorkHistory();
        this.eduInfo = entity.getEduInfo();
    }
}

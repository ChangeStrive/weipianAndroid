package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 微公告实体类
 * Created by Administrator on 2015/9/2.
 */
public class WeNoticeEntity {
    private int id;
    /**
     * 头像
     */
    @SerializedName("headImg")
    private String headImg;
    /**
     * 公告文字内容
     */
    @SerializedName("content")
    private String content;
    /**
     * 公告图片
     */
    @SerializedName("image")
    private String image;
    /**
     * 发布时间
     */
    @SerializedName("publicTime")
    private String publicTime;
    /**
     * 微片号
     */
    @SerializedName("vcardNo")
    private long vcardNo;
    /**
     * 成员角色
     */
    @SerializedName("role")
    private int role;
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
     * 昵称
     */
    @SerializedName("nickName")
    private String nickName;


    /**
     * 获取姓名（如果昵称不为空则返回昵称）
     * @return String
     */
    public String getDisplayName(){
        String disName = "";
        if(ResourceHelper.isNotEmpty(nickName)){
            disName = nickName;
        }else{
            if(ResourceHelper.isNotEmpty(surname)){
                disName += surname;
            }
            if(ResourceHelper.isNotEmpty(firstName)){
                disName += firstName;
            }
        }
        return disName;
    }

    public WeNoticeEntity(){

    }

    public WeNoticeEntity(int id,String headImg,String content,String image,String publicTime, long vcardNo, int role, String surname, String firstName, String nickName){
        this.id = id;
        this.headImg = headImg;
        this.content = content;
        this.image = image;
        this.publicTime = publicTime;
        this.vcardNo = vcardNo;
        this.role = role;
        this.surname  = surname;
        this.firstName = firstName;
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public long getVcardNo() {
        return vcardNo;
    }

    public void setVcardNo(long vcardNo) {
        this.vcardNo = vcardNo;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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
}

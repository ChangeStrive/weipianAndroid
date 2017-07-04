package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 单位认证上传信息实体类
 * Created by Administrator on 2015/10/30.
 */
public class UnitAuthJsonEntity {
    /** 企业id**/
    @SerializedName("enterpriseId")
    private long enterpriseId;
    /** 企业名称  **/
    @SerializedName("enterpriseName")
    private String enterpriseName;
    /**行业类别 **/
    @SerializedName("classLabel")
    private int classLabel;
    /** 单位性质 **/
    @SerializedName("property")
    private int property;
    /** 法人代表 **/
    @SerializedName("legalPerson")
    private String legalPerson;
    /** 联系电话  **/
    @SerializedName("telephone")
    private String telephone;
    /** 企业邮箱  **/
    @SerializedName("email")
    private String email;
    /** 单位地址 **/
    @SerializedName("address")
    private String address;
    /** 营业执照副本 **/
    @SerializedName("licenseCopy")
    private String licenseCopy;
    /** 组织机构代码证 **/
    @SerializedName("taxCertificate")
    private String taxCertificate;
    /** 申请人名片id **/
    @SerializedName("cardId")
    private long applyCardId;
    /** 管理员 **/
    @SerializedName("validAdmins")
    private Long[] adminCardIds;

    public long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public int getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(int classLabel) {
        this.classLabel = classLabel;
    }

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseCopy() {
        return licenseCopy;
    }

    public void setLicenseCopy(String licenseCopy) {
        this.licenseCopy = licenseCopy;
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate;
    }

    public long getApplyCardId() {
        return applyCardId;
    }

    public void setApplyCardId(long applyCardId) {
        this.applyCardId = applyCardId;
    }

    public Long[] getAdminCardIds() {
        return adminCardIds;
    }

    public void setAdminCardIds(Long[] adminCardIds) {
        this.adminCardIds = adminCardIds;
    }
}

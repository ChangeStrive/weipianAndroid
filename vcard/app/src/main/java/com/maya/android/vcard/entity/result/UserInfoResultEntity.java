package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.Helper;

/**
 * ResultEntity:用户资料：我的信息
 */
public class UserInfoResultEntity {
	
	/** 帐户ID */
	@SerializedName("accountId")
	private long id;// 账号id
	/** true 用户名，必须为邮箱/手机号/微片号 */
	@SerializedName("username")
	private String username;
	/** 登录密码 **/
	@SerializedName("password")
	private String password;
	/** 姓氏 */
	@SerializedName("surname")
	protected String surname;
	/** 名字 */
	@SerializedName("firstName")
	protected String firstName;
	/** 英文名 */
	@SerializedName("enName")
	private String enName;
	/** 操作密码 */
	@SerializedName("encodePwd")
	private String encodePwd;
	/** 服务权限级别 */
	@SerializedName("auth")
	private int auth;
	/** 毕业院校 */
	@SerializedName("school")
	protected String school;
	/** 个性签名 */
	@SerializedName("selfSign")
	protected String selfSign;
	/** 血型  0 未知 */
	@SerializedName("bloodType")
	protected int bloodType = 0;
	/** 学历 */
	@SerializedName("eduBackground")
	protected String eduBackground;
	/** 性别,0女，1男，-1是未知(没有未知的) */
	@SerializedName("sex")
	protected int sex = 1;
	/** 出生日期 */
	@SerializedName("birthday")
	protected String birthday;
	/** 注册时间 */
	@SerializedName("createTime")
	private String createTime;
	/** 星座 */
	@SerializedName("constellation")
	private String constellation;
	/** 昵称 */
	@SerializedName("nickName")
	private String nickName;
	/** 头像图片(头像墙，可表示五张头像图片的URL，以“#”分隔) */
	@SerializedName("headImg")
	protected String headImg;
	/** 籍贯 */
	@SerializedName("nativeplace")
	protected String nativeplace;
	/** 推荐数 */
	@SerializedName("recommend")
	private int recommend=0;
	/** 积分 */
	@SerializedName("integral")
	private int integral=0;
	/** 微币 */
	@SerializedName("vCount")
	private int vCount=0;
	/** 国家 */
	@SerializedName("country")
	protected String country;
	/** 省 */
	@SerializedName("province")
	protected String province;
	/** 城市 */
	@SerializedName("city")
	private String city;
	/** 微片号等级 */
	@SerializedName("grade")
	private int grade;
	/** 微片号 */
	@SerializedName("vcardNo")
	private String vcardNo;
	/**
	 * 合作伙伴ID
	 * 1腾讯   2:新浪
	 */
	private int partnerId;
	/** 绑定方式(0无绑定，1—手机绑定，2—邮箱绑定， 3-手机、邮箱都绑定) */
	@SerializedName("bindWay")
	private int bindWay;
	/** 手机号 */
	@SerializedName("mobileTel")
	private String mobileTel;
	/** 绑定邮箱 */
	@SerializedName ("email")
	private String email;
	/** 个人简介 */
	@SerializedName("intro")
	protected String intro;
	/** 社交信息--腾讯微博号码 **/
	@SerializedName("tencentWeiboNo")
	private String tencentBlogNo;
	/** 社交信息--qq号码  **/
	@SerializedName("tencentQQNo")
	private String tencentQQNo;
	/** 社交信息--新浪微博号码   **/
	@SerializedName("sinaWeiboNo")
	private String sinaBlogNo;
	/** 社交信息--腾讯微博链接地址 **/
	@SerializedName("tencentWeiboUrl")
	private String tencentBlogUrl;
	/** 社交信息--腾讯空间链接地址  **/
	@SerializedName("tencentQQUrl")
	private String tencentQQUrl;
	/** 社交信息--新浪微博链接地址   **/
	@SerializedName("sinaWeiboUrl")
	private String sinaBlogUrl;
	/** 工作经历   **/
	@SerializedName("workHistory")
	private String workHistory;
	/** 教育信息 **/
	@SerializedName("eduInfo")
	private String eduInfo;
	/** 粉丝数量 **/
	@SerializedName("fansNum")
	private int fansNum;
	/** 关注数量 **/
	@SerializedName("attentionNum")
	private int attentionNum;
	/** 密码修改次数 **/
	@SerializedName("updNameTime")
	private int updNameTime;

	public int getAttentionNum() {
		return attentionNum;
	}

	public void setAttentionNum(int attentionNum) {
		this.attentionNum = attentionNum;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public int getBindWay() {
		return bindWay;
	}

	public void setBindWay(int bindWay) {
		this.bindWay = bindWay;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getBloodType() {
		return bloodType;
	}

	public void setBloodType(int bloodType) {
		this.bloodType = bloodType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEduBackground() {
		return eduBackground;
	}

	public void setEduBackground(String eduBackground) {
		this.eduBackground = eduBackground;
	}

	public String getEduInfo() {
		return eduInfo;
	}

	public void setEduInfo(String eduInfo) {
		this.eduInfo = eduInfo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncodePwd() {
		return encodePwd;
	}

	public void setEncodePwd(String encodePwd) {
		this.encodePwd = encodePwd;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getMobileTel() {
		return mobileTel;
	}

	public void setMobileTel(String mobileTel) {
		this.mobileTel = mobileTel;
	}

	public String getNativeplace() {
		return nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSelfSign() {
		return selfSign;
	}

	public void setSelfSign(String selfSign) {
		this.selfSign = selfSign;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getSinaBlogNo() {
		return sinaBlogNo;
	}

	public void setSinaBlogNo(String sinaBlogNo) {
		this.sinaBlogNo = sinaBlogNo;
	}

	public String getSinaBlogUrl() {
		return sinaBlogUrl;
	}

	public void setSinaBlogUrl(String sinaBlogUrl) {
		this.sinaBlogUrl = sinaBlogUrl;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTencentBlogNo() {
		return tencentBlogNo;
	}

	public void setTencentBlogNo(String tencentBlogNo) {
		this.tencentBlogNo = tencentBlogNo;
	}

	public String getTencentBlogUrl() {
		return tencentBlogUrl;
	}

	public void setTencentBlogUrl(String tencentBlogUrl) {
		this.tencentBlogUrl = tencentBlogUrl;
	}

	public String getTencentQQNo() {
		return tencentQQNo;
	}

	public void setTencentQQNo(String tencentQQNo) {
		this.tencentQQNo = tencentQQNo;
	}

	public String getTencentQQUrl() {
		return tencentQQUrl;
	}

	public void setTencentQQUrl(String tencentQQUrl) {
		this.tencentQQUrl = tencentQQUrl;
	}

	public int getUpdNameTime() {
		return updNameTime;
	}

	public void setUpdNameTime(int updNameTime) {
		this.updNameTime = updNameTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVcardNo() {
		return vcardNo;
	}

	public void setVcardNo(String vcardNo) {
		this.vcardNo = vcardNo;
	}

	public int getvCount() {
		return vCount;
	}

	public void setvCount(int vCount) {
		this.vCount = vCount;
	}

	public String getWorkHistory() {
		return workHistory;
	}

	public void setWorkHistory(String workHistory) {
		this.workHistory = workHistory;
	}

	/**
	 * 获取全称
	 * @return String
	 */
	public String getDisplayName(){
		String disName = "";
		if(Helper.isNotNull(surname)){
			disName += surname;
		}
		if(Helper.isNotNull(firstName)){
			disName += firstName;
		}
		if("".equals(disName)){
			disName = enName;
		}
		return disName;
	}
}

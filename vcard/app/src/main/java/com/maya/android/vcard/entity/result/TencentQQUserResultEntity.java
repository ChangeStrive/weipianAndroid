package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * qq授权登录返回的 用户信息
 * @author zheng_cz
 * @since 2014年5月7日 下午3:46:51
 */
public class TencentQQUserResultEntity {
	/** qq 小头像 **/
	@SerializedName("figureurl_qq_1")
	/** qq 大头像 **/
	private String figureurlQq1;
	@SerializedName("figureurl_qq_2")
	private String figureurlQq2;
	/** 昵称 **/
	@SerializedName("nickname")
	private String nickName;
	/** 性别 **/
	@SerializedName("gender")
	private String sex;
	public String getFigureurlQq1() {
		return figureurlQq1;
	}
	public void setFigureurlQq1(String figureurlQq1) {
		this.figureurlQq1 = figureurlQq1;
	}
	public String getFigureurlQq2() {
		return figureurlQq2;
	}
	public void setFigureurlQq2(String figureurlQq2) {
		this.figureurlQq2 = figureurlQq2;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}

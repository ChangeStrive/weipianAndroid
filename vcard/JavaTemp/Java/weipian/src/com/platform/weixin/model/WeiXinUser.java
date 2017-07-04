package com.platform.weixin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 微信用户关注列表
 * 
 *
 */
@Entity
@Table(name = "weixin_user")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WeiXinUser {
	
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户的标识，对当前公众号唯一
	 */
	@Column(length = 100,unique=true)
	private String fdOpenId;
	
	/**
	 * 用户的昵称
	 */
	@Column(length = 100)
	private String fdNickName;
	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	@Column(length = 11)
	private String fdSex;
	
	/**
	 * 用户所在城市
	 */
	@Column(length = 50)
	private String fdCity;
	
	/**
	 * 用户所在国家
	 */
	@Column(length = 50)
	private String fdCountry;
	
	/**
	 * 用户所在省份
	 */
	@Column(length = 50)
	private String fdProvince;
	
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	 */
	@Column(length = 200)
	private String fdHeadImgUrl;

	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	 */
	@Column(length = 50)
	private String fdSubscribeTime;
	
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdOpenId() {
		return fdOpenId;
	}

	public void setFdOpenId(String fdOpenId) {
		this.fdOpenId = fdOpenId;
	}

	public String getFdNickName() {
		return fdNickName;
	}

	public void setFdNickName(String fdNickName) {
		this.fdNickName = fdNickName;
	}

	public String getFdSex() {
		return fdSex;
	}

	public void setFdSex(String fdSex) {
		this.fdSex = fdSex;
	}

	public String getFdCity() {
		return fdCity;
	}

	public void setFdCity(String fdCity) {
		this.fdCity = fdCity;
	}

	public String getFdCountry() {
		return fdCountry;
	}

	public void setFdCountry(String fdCountry) {
		this.fdCountry = fdCountry;
	}

	public String getFdProvince() {
		return fdProvince;
	}

	public void setFdProvince(String fdProvince) {
		this.fdProvince = fdProvince;
	}

	public String getFdHeadImgUrl() {
		return fdHeadImgUrl;
	}

	public void setFdHeadImgUrl(String fdHeadImgUrl) {
		this.fdHeadImgUrl = fdHeadImgUrl;
	}

	public String getFdSubscribeTime() {
		return fdSubscribeTime;
	}

	public void setFdSubscribeTime(String fdSubscribeTime) {
		this.fdSubscribeTime = fdSubscribeTime;
	}
}

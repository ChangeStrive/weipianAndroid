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
 * 微信配置
 * 
 *
 */

@Entity
@Table(name = "weixin_config")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WeiXinConfig {
	
	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 公众账号名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 原始ID
	 */
	@Column(length = 100)
	private String fdRawId;
	
	/**
	 * 公众账号APPID
	 */
	@Column(length = 100)
	private String fdAppId;
	
	/**
	 * 公众账号APPSECRET
	 */
	@Column(length = 100)
	private String fdAppSecret;

	@Column(length = 200)
	private String fdAccessToken;
	
	@Column(length = 200)
	private String fdJsapiTicket;
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdRawId() {
		return fdRawId;
	}

	public void setFdRawId(String fdRawId) {
		this.fdRawId = fdRawId;
	}

	public String getFdAppId() {
		return fdAppId;
	}

	public void setFdAppId(String fdAppId) {
		this.fdAppId = fdAppId;
	}

	public String getFdAppSecret() {
		return fdAppSecret;
	}

	public void setFdAppSecret(String fdAppSecret) {
		this.fdAppSecret = fdAppSecret;
	}

	public String getFdAccessToken() {
		return fdAccessToken;
	}

	public void setFdAccessToken(String fdAccessToken) {
		this.fdAccessToken = fdAccessToken;
	}

	public String getFdJsapiTicket() {
		return fdJsapiTicket;
	}

	public void setFdJsapiTicket(String fdJsapiTicket) {
		this.fdJsapiTicket = fdJsapiTicket;
	}
	
}

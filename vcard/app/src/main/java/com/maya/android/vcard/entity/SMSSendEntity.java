package com.maya.android.vcard.entity;
/**
 * 系统短信实体对象
 * @author zheng_cz
 * @since 2014年4月23日 下午6:27:47
 */
public class SMSSendEntity {

	/**
	 * 系统短信路径
	 */
	public static final String CONTENT_URI = "content://sms";
	/** 接收方号码 **/
	private String address;
	/** 发送内容 **/
	private String body;
	/** 1-收 2-发送 **/
	private int type;
	/** 阅读状态 0-未读 1-已读 **/
	private int read;
	/** 发送时间 **/
	private long date;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
}

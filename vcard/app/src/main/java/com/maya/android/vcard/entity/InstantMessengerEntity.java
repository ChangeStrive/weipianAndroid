package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 即时通讯实体类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-2-18
 *
 */
public class InstantMessengerEntity implements Cloneable{
	/**
	 * 及时通讯类型 
	 * 常量类  TableConstan.ImProtocol
	 */
	@SerializedName("protocol")
	private int protocol;
	/**
	 * 标签key(英文字符 + 下划线)
	 * 常量类  TableConstan.ImProtocolLabel
	 */
	@SerializedName("label")
	private String label;
	/**
	 * 名称
	 */
	@SerializedName("name")
	private String name;
	/** 帐户*/
	@SerializedName("value")
	private String value;
	public int getProtocol() {
		return protocol;
	}
	public void setProcol(int protocol) {
		this.protocol = protocol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public InstantMessengerEntity(){
		
	}
	public InstantMessengerEntity(String name, String value, int protocol){
		this.name = name;
		this.value = value;
		this.protocol = protocol;
	}
	public InstantMessengerEntity(String name, String value, int protocol, String label){
		this.name = name;
		this.value = value;
		this.protocol = protocol;
		this.label = label;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
}

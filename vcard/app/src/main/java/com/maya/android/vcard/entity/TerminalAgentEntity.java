package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 请求的头部参数
 */
public class TerminalAgentEntity {
	/**
	 * 经度
	 * true
	 */
	@SerializedName("longitude")
	private double longitude;
	/**
	 * 纬度
	 * true
	 */
	@SerializedName("latitude")
	private double latitude;
	/**
	 * 客户端版本类型
	 * false
	 */
	@SerializedName("clientVerType")
	private String clientVerType; 
	/**
	 * 客户端版本号
	 * fasle
	 */
	@SerializedName("clientVerNum")
	private String clientVerNum;
	/**
	 * 客户端渠道类型
	 * false
	 */
	@SerializedName("clientChannelType")
	private String clientChannelType;
	/**
	 * 基站ID信息
	 * true
	 */
	@SerializedName("baseStationInfo")
	private String baseStationInfo;
	/**
	 * 操作系统类型
	 * false
	 */
	@SerializedName("osType")
	private String osType;
	/**
	 * 菜单版本号
	 * false
	 */
	@SerializedName("menuVerNum")
	private String menuVerNum;
	/**
	 * SIM卡IMSI号
	 * false
	 */
	@SerializedName("imsi")
	private String imsi; 
	/**
	 * 手机IMEI编号
	 * true
	 */
	@SerializedName("imei")
	private String imei;
	/**
	 * 手机号
	 * true
	 */
	@SerializedName("mobile")
	private String mobile; 
	/**
	 * 城市编码
	 * true
	 */
	@SerializedName("cityCode")
	private String cityCode;
	/**
	 * 运营商名称
	 * true
	 */
	@SerializedName("operatorName")
	private String operatorName;
	/**
	 * 运营商编号
	 * true
	 */
	@SerializedName("operatorNum")
	private String operatorNum;
	/**
	 * 网络类型
	 * 1-wifi,2-3G,3其它
	 * false
	 */
	@SerializedName("netType")
	private int netType;
	/**
	 * 手机mac地址
	 */
	@SerializedName("mac")
	private String macAddress;
	/**
	 * 个信推送
	 */
	@SerializedName("clientId")
	private String clientId;

	/**
	 *
	 * @param clientVerType
	 * @param clientVerNum
	 * @param clientChannelType
	 * @param osType
	 * @param menuVerNum
	 * @param imsi
	 * @param imei
	 * @param mobile
	 * @param operatorName
	 * @param operatorNum
	 * @param macAddress
	 * @param clientId
	 */
	public TerminalAgentEntity(String clientVerType, String clientVerNum,
			String clientChannelType, String osType, String menuVerNum,
			String imsi, String imei, String mobile, String operatorName,
			String operatorNum, String macAddress, String clientId) {
		this.clientVerType = clientVerType;
		this.clientVerNum = clientVerNum;
		this.clientChannelType = clientChannelType;
		this.osType = osType;
		this.menuVerNum = menuVerNum;
		this.imsi = imsi;
		this.imei = imei;
		this.mobile = mobile;
		this.operatorName = operatorName;
		this.operatorNum = operatorNum;
		this.macAddress = macAddress;
		this.clientId = clientId;
	}

	/**
	 * 构造全体成员变量
	 * @param longitude
	 * @param latitude
	 * @param clientVerType
	 * @param clientVerNum
	 * @param clientChannelType
	 * @param baseStationInfo
	 * @param osType
	 * @param menuVerNum
	 * @param imsi
	 * @param imei
	 * @param mobile
	 * @param cityCode
	 * @param operatorName
	 * @param operatorNum
	 * @param netType
	 */
	public TerminalAgentEntity(double longitude, double latitude,
			String clientVerType, String clientVerNum, String clientChannelType,
			String baseStationInfo, String osType, String menuVerNum, String imsi,
			String imei, String mobile, String cityCode, String operatorName,
			String operatorNum, int netType, String macAddress, String clientId) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.clientVerType = clientVerType;
		this.clientVerNum = clientVerNum;
		this.clientChannelType = clientChannelType;
		this.baseStationInfo = baseStationInfo;
		this.osType = osType;
		this.menuVerNum = menuVerNum;
		this.imsi = imsi;
		this.imei = imei;
		this.mobile = mobile;
		this.cityCode = cityCode;
		this.operatorName = operatorName;
		this.operatorNum = operatorNum;
		this.netType = netType;
		this.macAddress = macAddress;
		this.clientId = clientId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getClientVerType() {
		return clientVerType;
	}

	public void setClientVerType(String clientVerType) {
		this.clientVerType = clientVerType;
	}

	public String getClientVerNum() {
		return clientVerNum;
	}

	public void setClientVerNum(String clientVerNum) {
		this.clientVerNum = clientVerNum;
	}

	public String getClientChannelType() {
		return clientChannelType;
	}

	public void setClientChannelType(String clientChannelType) {
		this.clientChannelType = clientChannelType;
	}

	public String getBaseStationInfo() {
		return baseStationInfo;
	}

	public void setBaseStationInfo(String baseStationInfo) {
		this.baseStationInfo = baseStationInfo;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getMenuVerNum() {
		return menuVerNum;
	}

	public void setMenuVerNum(String menuVerNum) {
		this.menuVerNum = menuVerNum;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorNum() {
		return operatorNum;
	}

	public void setOperatorNum(String operatorNum) {
		this.operatorNum = operatorNum;
	}

	public int getNetType() {
		return netType;
	}

	public void setNetType(int netType) {
		this.netType = netType;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}

package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Entity:设置Entity
 */
public class SettingEntity {
	/** 是否关联通讯录 **/
	@SerializedName("accountLinkAddressBook")
	private boolean accountLinkAddressBook = true;
	/** 名片交换播放的音效（0为静音，1京剧脸谱（默认），2 Sunset Journey） **/
	@SerializedName("swapCardSoundResourceId")
	private int swapCardSoundResourceId = 1;
	/** 摇晃灵敏度 **/
	@SerializedName("rockRate")
	private float rockRate = 30;
	/** 新消息提醒 **/
	@SerializedName("alertNewMessage")
	private boolean alertNewMessage = true;
	/** 群聊新消息提醒 **/
	@SerializedName("alertNewMessageForGroup")
	private boolean alertNewMessageForGroup = true;
	/** 新消息提醒是否震动（默认为非震动） **/
	@SerializedName("alertNewMessageShock")
	private boolean alertNewMessageShock = false;
	/** 新消息展示 **/
	@SerializedName("alertNewMessageShow")
	private boolean alertNewMessageShow = true;
	/** 接受离线消息 **/
	@SerializedName("alertNewMessageOffLine")
	private boolean alertNewMessageOffLine = true;
	/** 允许好友转发名片 **/
	@SerializedName("allowFriendTransmitCard")
	private int allowFriendTransmitCard = 0;
	/** 隐藏手机号码 **/
	@SerializedName("hideMobileNumber")
	private boolean hideMobileNumber = false;
	/** 删除名片需要输入密码 **/
	@SerializedName("delCardNeedPassword")
	private boolean delCardNeedPassword = true;
	/** 对好友展示社交信息 **/
	@SerializedName("showSocialContactToFriend")
	private boolean showSocialContactToFriend = true;
	/** 开启云端备份(默认关闭) **/
	@SerializedName("openBackups")
	private boolean openBackups = true;
	/** 云端备份间隔（1：1天，2:1周，3：一月，4:1季度） **/
	@SerializedName("backupsTime")
	private int backupsTime = 2;
	/** 云端备份方式（2：合并云端和本机，1：本机为准覆盖云端） **/
	@SerializedName("backupsType")
	private int backupsType = 1;
	/** 仅在WIFI下备份 **/
	@SerializedName("backupsInWIFI")
	private boolean backupsInWIFI = false;
	/** 来电显示是否展示名片（或头像） **/
	@SerializedName("addressListShowCard")
	private int addressListShowCard = 0;
	/** 自动添加名片信息到通讯录 **/
	@SerializedName("cardInfo2AddressList")
	private boolean cardInfo2AddressList = true;
	/** 是否允许云端查找到我 **/
	@SerializedName("allowCloudFindMe")
	private boolean allowCloudFindMe = true;
	/** 云端查找 结果信息显示方式  0-头像**/
	@SerializedName("cloudFindShowType")
	private int cloudFindShowType = 0;
	/** 语言类型 **/
	@SerializedName("languageType")
	private int languageType = 0;
	/** 新消息提示音 **/
	@SerializedName("newMessageSoundName")
	private String newMessageSoundName ;
	/** 主页：0：微片；1：名片夹 **/
	@SerializedName("homePage")
	private int homePage = 0;
	
	public boolean isAccountLinkAddressBook() {
		return accountLinkAddressBook;
	}
	public void setAccountLinkAddressBook(boolean accountLinkAddressBook) {
		this.accountLinkAddressBook = accountLinkAddressBook;
	}
	public int getSwapCardSoundResourceId() {
		return swapCardSoundResourceId;
	}
	public void setSwapCardSoundResourceId(int swapCardSoundResourceId) {
		this.swapCardSoundResourceId = swapCardSoundResourceId;
	}
	public float getRockRate() {
		return rockRate;
	}
	public void setRockRate(float rockRate) {
		this.rockRate = rockRate;
	}
	public boolean isAlertNewMessage() {
		return alertNewMessage;
	}
	public void setAlertNewMessage(boolean alertNewMessage) {
		this.alertNewMessage = alertNewMessage;
	}
	public boolean isAlertNewMessageForGroup() {
		return alertNewMessageForGroup;
	}
	public void setAlertNewMessageForGroup(boolean alertNewMessageForGroup) {
		this.alertNewMessageForGroup = alertNewMessageForGroup;
	}
	public boolean isAlertNewMessageShock() {
		return alertNewMessageShock;
	}
	public void setAlertNewMessageShock(boolean alertNewMessageShock) {
		this.alertNewMessageShock = alertNewMessageShock;
	}
	public boolean isAlertNewMessageShow() {
		return alertNewMessageShow;
	}
	public void setAlertNewMessageShow(boolean alertNewMessageShow) {
		this.alertNewMessageShow = alertNewMessageShow;
	}
	public boolean isAlertNewMessageOffLine() {
		return alertNewMessageOffLine;
	}
	public void setAlertNewMessageOffLine(boolean alertNewMessageOffLine) {
		this.alertNewMessageOffLine = alertNewMessageOffLine;
	}
	public int getAllowFriendTransmitCard() {
		return allowFriendTransmitCard;
	}
	public void setAllowFriendTransmitCard(int allowFriendTransmitCard) {
		this.allowFriendTransmitCard = allowFriendTransmitCard;
	}
	public boolean isHideMobileNumber() {
		return hideMobileNumber;
	}
	public void setHideMobileNumber(boolean hideMobileNumber) {
		this.hideMobileNumber = hideMobileNumber;
	}
	public boolean isDelCardNeedPassword() {
		return delCardNeedPassword;
	}
	public void setDelCardNeedPassword(boolean delCardNeedPassword) {
		this.delCardNeedPassword = delCardNeedPassword;
	}
	public boolean isShowSocialContactToFriend() {
		return showSocialContactToFriend;
	}
	public void setShowSocialContactToFriend(boolean showSocialContactToFriend) {
		this.showSocialContactToFriend = showSocialContactToFriend;
	}
	public boolean isOpenBackups() {
		return openBackups;
	}
	public void setOpenBackups(boolean openBackups) {
		this.openBackups = openBackups;
	}
	public int getBackupsTime() {
		return backupsTime;
	}
	public void setBackupsTime(int backupsTime) {
		this.backupsTime = backupsTime;
	}
	public int getBackupsType() {
		return backupsType;
	}
	public void setBackupsType(int backupsType) {
		this.backupsType = backupsType;
	}
	public boolean isBackupsInWIFI() {
		return backupsInWIFI;
	}
	public void setBackupsInWIFI(boolean backupsInWIFI) {
		this.backupsInWIFI = backupsInWIFI;
	}
	public int getAddressListShowCard() {
		return addressListShowCard;
	}
	public void setAddressListShowCard(int addressListShowCard) {
		this.addressListShowCard = addressListShowCard;
	}
	public boolean isCardInfo2AddressList() {
		return cardInfo2AddressList;
	}
	public void setCardInfo2AddressList(boolean cardInfo2AddressList) {
		this.cardInfo2AddressList = cardInfo2AddressList;
	}
	public boolean isAllowCloudFindMe() {
		return allowCloudFindMe;
	}
	public void setAllowCloudFindMe(boolean allowCloudFindMe) {
		this.allowCloudFindMe = allowCloudFindMe;
	}
	public int getCloudFindShowType() {
		return cloudFindShowType;
	}
	public void setCloudFindShowType(int cloudFindShowType) {
		this.cloudFindShowType = cloudFindShowType;
	}
	public int getLanguageType() {
		return languageType;
	}
	public void setLanguageType(int languageType) {
		this.languageType = languageType;
	}
	public String getNewMessageSoundName() {
	    return newMessageSoundName;
	}
	public void setNewMessageSoundName(String newMessageSoundName) {
	    this.newMessageSoundName = newMessageSoundName;
	}
	public int getHomePage() {
		return homePage;
	}
	public void setHomePage(int homePage) {
		this.homePage = homePage;
	}
	
}

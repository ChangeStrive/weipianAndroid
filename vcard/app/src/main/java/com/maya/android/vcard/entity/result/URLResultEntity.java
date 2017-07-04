package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
/**
 * ResultEntity:地址列表
 */
public class URLResultEntity {
	/** 云端验证页面 */
	@SerializedName("VCARD_MOBILE_CLOUD_REGISTER")
	private String mobileCloudRegister;
	/** 微片WAP服务基地址 http://m.vcard2012.com */
	@SerializedName("VCARD_MOBILE_BASE_SERVICE")
	private String mobileBaseService;
	/** 二维码扫描 */
	@SerializedName("VCARD_QR")
	private String qrCode;
	/** 通过手机号码查询用户资料 */
	@SerializedName("VCARD_MOBILE_PERSON")
	private String mobilePerson;
	/** 注销 */
	@SerializedName("VCARD_LOGOUT")
	private String logout;
	/** 通讯录匹配 */
	@SerializedName("VCARD_CONTACTS_VALID")
	private String contactsValid;
	/** 分组 */
	@SerializedName("VCARD_GROUP_LIST")
	private String vcardGroupList;
	/** 地址列表 */
	@SerializedName("VCARD_ADDRESS_LIST")
	private String addressList;
	/** 保存我的信息 */
	@SerializedName("VCARD_MYINFO_SAVE")
	private String myInfoSave;
	/** 名片模板 */
	@SerializedName("VCARD_CARD_TEMPLATE")
	private String cardTemplate;
	/** 消息推送标签 */
	@SerializedName("VCARD_MSG_PUSH_SINGLE")
	private String msgPushSingle;
	/** 转发名片  */
	@SerializedName("VCARD_MSG_CARD_SHARE")
	private String msgVcardShare;
	/** 合作伙伴列表 */
	@SerializedName("VCARD_PARTNER_LIST")
	private String partnerList;
	/** 我的联系人移动 */
	@SerializedName("VCARD_MYCONTACT_MOVE")
	private String myContactMove;
	/** 闪屏页/欢迎页 */
	@SerializedName("VCARD_COVER")
	private String cover;
	/** 保存我的设置  */
	@SerializedName("VCARD_MYSETTING_SAVE")
	private String mySettingSave;
	/** 上传/删除(一般) */
	@SerializedName("VCARD_COMMON_UPLOAD_DELETE")
	private String commonUploadDelete;
	/** 云记录 */
	@SerializedName("VCARD_CLOUD_RECORD")
	private String cloudRecord;
	/** 更改密码 */
	@SerializedName("VCARD_PASSWORD_UPDATE")
	private String pawsswordUpdate;
	/** 正常登录 */
	@SerializedName("VCARD_LOGIN_NORMAL")
	private String loginNormal;
	/** 交换名片请求 */
	@SerializedName("VCARD_CARD_SWAP")
	private String cardSwap;
	/** 服务器基地址 */
	@SerializedName("VCARD_BASE_SERVICE")
	private String baseService;
	/** 提交名片交换确认 */
	@SerializedName("VCARD_CARD_BINDING")
	private String cardSwapCommit;
	/** 客户意见反馈 */
	@SerializedName("VCARD_USER_FEEDBACK")
	private String userFeedback;
	/** 保存我的名片 */
	@SerializedName("VCARD_MYVCARD_SAVE")
	private String myVcardSave;
	/** 联系人云同步 */
	@SerializedName("VCARD_CONTACT_SYNC")
	private String cloudContactsSync;
	/** 联系人云端备份 */
	@SerializedName("VCARD_CONTACT_BACKUP")
	private String cloudContactsBackup;
	/** 私密空间通话记录备份 */
	@SerializedName("VCARD_CALLLOGS_BACKUP")
	private String cloudPrivateCallBackup;
	/** 私密空间 通话记录恢复 */
	@SerializedName("VCARD_CALLLOGS_SYNC")
	private String cloudPrivateCallSync;
	/** 私密空间 联系人备份 */
	@SerializedName("VCARD_CONTACTS_BACKUP")
	private String cloudPrivateContactsBackup;
	/** 私密空间联系人恢复 */
	@SerializedName("VCARD_CONTACTS_SYNC")
	private String cloudPrivateContactsSync;
	/** 私密空间 短信备份 */
	@SerializedName("VCARD_SMS_BACKUP")
	private String cloudPrivateSmsBackup;
	/** 私密空间 短信恢复 */
	@SerializedName("VCARD_SMS_SYNC")
	private String cloudPrivateSmsSync;
	/** 备份恢复日志 */
	@SerializedName("VCARD_CONTACT_LOGS")
	private String cloudLogs;
	/** 更改联系人黑名单 */
	@SerializedName("VCARD_MYCONTACT_BLACKLIST_UPDATE")
	private String myContactBlackListUpdate;
	/** 联系人列表 */
	@SerializedName("VCARD_MYCONTACT")
	private String myContact;
	/** 图片服务器基地址 */
	@SerializedName("VCARD_IMG_BASE_SERVICE")
	private String imgBaseService;
	/** 积分 */
	@SerializedName("VCARD_INTEGRAL_CHANGE")
	private String integeralChange;
	/** 我的信息 */
	@SerializedName("VCARD_MYINFO")
	private String myInfo;
	/** 分组保存 */
	@SerializedName("VCARD_GROUP_SAVE")
	private String groupSave;
	/** 验证密码 */
	@SerializedName("VCARD_PASSWORD_VERIFY")
	private String passwordVerify;
	/** 手机绑定 */
	@SerializedName("VCARD_MOBILE_BINDING")
	private String mobileBind;
	/** 我的名片 */
	@SerializedName("VCARD_MYVCARD")
	private String myVcard;
	
	/** 移动分组 */
	@SerializedName("VCARD_GROUP_MOVE")
	private String groupMove;
	/** 删除我的名片 */
	@SerializedName("VCARD_MYVCARD_DELETE")
	private String myVcardDelete;
	/** 删除分组 */
	@SerializedName("VCARD_GROUP_DELETE")
	private String groupDelete;
	/** 注册 */
	@SerializedName("VCARD_REGISTER")
	private String vcardRegister;
	/** 上传 */
	@SerializedName("VCARD_COMMON_UPLOAD")
	private String commonUpload;
	/** 删除联系人 */
	@SerializedName("VCARD_MYCONTACT_DELETE")
	private String myContactDelete;
	/** 查看联系人详情 */
	@SerializedName("VCARD_CARD_PERSON")
	private String cardPerson;
	/** 获取我的设置 */
	@SerializedName("VCARD_MYSETTING")
	private String mySetting;
	/** 第三方登录接口 */
	@SerializedName("VCARD_LOGIN_THIRD")
	private String loginThird;
	/** 保存企业组成员 */
	@SerializedName("VCARD_ENTERPRISE_MEMBER_SAVE")
	private String enterpriseMemberSave;
	/** 保存成员权限 */
	@SerializedName("VCARD_ENTERPRISE_MEMBER_ROLE_SAVE")
	private String enterpriseMemberRoleSave;
	/** 获取我的企业信息 */
	@SerializedName("VCARD_ENTERPRISE_LIST")
	private String enterpriseList;
	/** 获取我的聊天组信息列表 */
	@SerializedName("VCARD_MSG_GROUP_LIST")
	private String groupInfoList;
	/** 获取单个聊天组信息 */
	@SerializedName("VCARD_MSG_GROUP_SINGLE")
	private String groupInfo;
	/** 获取我的企业成员信息 */
	@SerializedName("VCARD_ENTERPRISE_MEMBER_LIST")
	private String enterPriseMemberList;
	/** 企业认证 */
	@SerializedName("VCARD_ENTERPRISE_APPROVE")
	private String enterPriseApprove;
	/** 企业信息更改 */
	@SerializedName("VCARD_ENTERPRISE_MODIFY")
	private String enterPriseModify;
	/** 获取短信验证码 */
	@SerializedName("VCARD_SMS_VERIFYCODE")
	private String smsVerifyCode;
	/** 附近的人 列表 */
	@SerializedName("VCARD_CARD_NEARBY")
	private String nearbyPeopleList;
	/** 获取我的、我的联系人的微广播 */
	@SerializedName("VCARD_BROADCAST_ALL")
	private String broadcastAll;
	/** 发表微广播 */
	@SerializedName("VCARD_BROADCAST_ADD")
	private String broadcastAdd;
	/** 获取微广播回复 */
	@SerializedName("VCARD_BROADCAST_REPLY_LIST")
	private String broadcastReplyList;
	/** 回复微广播 */
	@SerializedName("VCARD_BROADCAST_REPLY_ADD")
	private String broadcastReplyAdd;
	/** 重置密码 */
	@SerializedName("VCARD_PWD_RESET")
	private String resetPwd;
	/** 推荐短信URL */
	@SerializedName("VCARD_SMS_RECOMMEND")
	private String recommonUrl;
	/** 好友推荐记录列表 */
	@SerializedName("VCARD_SMS_RECOMMEND_RECORD")
	private String recommendRecordUrl;
	/** 群聊 创建 */
	@SerializedName("VCARD_MSG_GROUP_CREATE")
	private String messageGroupChatCreate;
	/** 添加群成员 */
	@SerializedName("VCARD_MSG_GROUP_MEMBER_ADD")
	private String messageGroupChatAdd;
	/** 修改群名称 */
	@SerializedName("VCARD_MSG_GROUP_UPDATE")
	private String messageGroupUpdate;
	/** 解散群聊组 */
	@SerializedName("VCARD_MSG_GROUP_DEL")
	private String messageGroupDel;
	/** 退出群聊组 */
	@SerializedName("VCARD_MSG_GROUP_MEMBER_QUIT")
	private String messageGroupQuit;
	/** 删除群聊成员 */
	@SerializedName("VCARD_MSG_GROUP_MEMBER_DEL")
	private String messageGroupChatDelete;
	/** 群聊成员列表 */
	@SerializedName("VCARD_MSG_GROUP_MEMBER")
	private String messageGroupMemberSelect;
	/** 邮箱绑定 */
	@SerializedName("VCARD_EMAIL_BINDING")
	private String bindEmail;
	/** 下载名片模板 (单张名片模板信息) */
	@SerializedName("VCARD_CARD_TEMPLATE_SINGLE")
	private String templateDownload;
	/** 我下载的名片模板 */
	@SerializedName("VCARD_MYTEMPLATE_LIST")
	private String myTemplateList;
	/** 本地新增名片保存*/
	@SerializedName("VCARD_MYCONTACT_SAVE")
	private String myContactCardSave;
	/** 获取名片字段位置信息 */
	@SerializedName("VCARD_CARD_FIX_LIST")
	private String cardFixList;
	
	/** 雷达扫描 **/
	@SerializedName("VCARD_RADAR_SCAN")
	private String radarScan;
	/** 断开雷达扫描 **/
	@SerializedName("VCARD_RADAR_REMOVE")
	private String radarRemove;
	/** 删除下载的名片模板 **/
	@SerializedName("VCARD_CARD_TEMPLATE_DELETE")
	private String vcardTemplateDelete;
	/** 交换地址 **/
	@SerializedName("VCARD_SWAP_SERVICE")
	private String swapBaseService;
	/** 关注地址 **/
	@SerializedName("VCARD_ATTENTION_ADD")
	private String vcardAttentionAdd;
	/** 取消关注地址 **/
	@SerializedName("VCARD_ATTENTION_DEL")
	private String vcardAttentionDel;
	/** 粉丝列表地址 **/
	@SerializedName("VCARD_FANS_LIST")
	private String vcardFansList;
	/** 关注列表地址 **/
	@SerializedName("VCARD_ATTENTIONS_LIST")
	private String vcardAttentionList;
	/** 访问列表地址 **/
	@SerializedName("VCARD_VISITOR_LIST")
	private String vcardVisitorList;
	/** 通过vcardNo获取个人信息 **/
	@SerializedName("VCARD_PERSON_MESSAGE")
	private String vcardPersonMessage;
	/** 发布企业公告 **/
	@SerializedName("VCARD_ENTERPRISE_NOTICE")
	private String enterpriseNotice;
	/** 获取企业公告 **/
	@SerializedName("VCARD_ENTERPRISE_GET_NOTICE")
	private String enterpriseGetNotice;
	
//	private String ;
//	private String ;
//	private String ;
	/**
	 * 获取云端验证页面
	 * @return
	 */
	public String getMobileCloudRegister() {
		return mobileCloudRegister;
	}

	public void setMobileCloudRegister(String mobileCloudRegister) {
		this.mobileCloudRegister = mobileCloudRegister;
	}
	
	public String getGroupInfo() {
		return groupInfo;
	}
	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
	}
	
	/**
	 * 获取微片WAP基地址
	 * @return
	 */
	public String getMobileBaseService() {
		return mobileBaseService;
	}
	public void setMobileBaseService(String mobileBaseService) {
		this.mobileBaseService = mobileBaseService;
	}
	/**
	 * 通过手机号码查询用户资料
	 */
	public String getMobilePerson() {
		return mobilePerson;
	}
	public void setMobilePerson(String mobilePerson) {
		this.mobilePerson = mobilePerson;
	}
	/**
	 * 注销
	 * @return
	 */
	public String getLogout() {
		return logout;
	}
	public void setLogout(String logout) {
		this.logout = logout;
	}
	/**
	 * 二维码扫描
	 * @return
	 */
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	/**
	 * 通讯录匹配
	 * @return
	 */
	public String getContactsValid() {
		return contactsValid;
	}
	public void setContactsValid(String contactsValid) {
		this.contactsValid = contactsValid;
	}
	/**
	 * 绑定邮箱
	 * @return
	 */
	public String getBindEmail() {
		return bindEmail;
	}
	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}
	
	public String getVcardGroupList() {
		return vcardGroupList;
	}
	public void setVcardGroupList(String vcardGroupList) {
		this.vcardGroupList = vcardGroupList;
	}
	public String getVcardRegister() {
		return vcardRegister;
	}
	public void setVcardRegister(String vcardRegister) {
		this.vcardRegister = vcardRegister;
	}
	public String getAddressList() {
		return addressList;
	}
	public void setAddressList(String addressList) {
		this.addressList = addressList;
	}
	public String getMyInfoSave() {
		return myInfoSave;
	}
	public void setMyInfoSave(String myInfoSave) {
		this.myInfoSave = myInfoSave;
	}
	public String getCardTemplate() {
		return cardTemplate;
	}
	public void setCardTemplate(String cardTemplate) {
		this.cardTemplate = cardTemplate;
	}
	public String getMsgPushSingle() {
		return msgPushSingle;
	}
	public void setMsgPushSingle(String msgPushSingle) {
		this.msgPushSingle = msgPushSingle;
	}
	public String getMsgVcardShare() {
		return msgVcardShare;
	}
	public void setMsgVcardShare(String msgVcardList) {
		this.msgVcardShare = msgVcardList;
	}
	public String getPartnerList() {
		return partnerList;
	}
	public void setPartnerList(String partnerList) {
		this.partnerList = partnerList;
	}
	public String getMyContactMove() {
		return myContactMove;
	}
	public void setMyContactMove(String myContactMove) {
		this.myContactMove = myContactMove;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getMySettingSave() {
		return mySettingSave;
	}
	public void setMySettingSave(String mySettingSave) {
		this.mySettingSave = mySettingSave;
	}
	public String getCommonUploadDelete() {
		return commonUploadDelete;
	}
	public void setCommonUploadDelete(String commonUploadDelete) {
		this.commonUploadDelete = commonUploadDelete;
	}
	public String getCloudRecord() {
		return cloudRecord;
	}
	public void setCloudRecord(String cloudRecord) {
		this.cloudRecord = cloudRecord;
	}
	public String getPawsswordUpdate() {
		return pawsswordUpdate;
	}
	public void setPawsswordUpdate(String pawsswordUpdate) {
		this.pawsswordUpdate = pawsswordUpdate;
	}
	public String getLoginNormal() {
		return loginNormal;
	}
	public void setLoginNormal(String loginNormal) {
		this.loginNormal = loginNormal;
	}
	public String getCardSwap() {
		return cardSwap;
	}
	public void setCardSwap(String cardSwap) {
		this.cardSwap = cardSwap;
	}
	public String getBaseService() {
		return baseService;
	}
	public void setBaseService(String baseService) {
		this.baseService = baseService;
	}
	public String getCardSwapCommit() {
		return cardSwapCommit;
	}
	public void setCardSwapCommit(String cardSwapCommit) {
		this.cardSwapCommit = cardSwapCommit;
	}
	public String getUserFeedback() {
		return userFeedback;
	}
	public void setUserFeedback(String userFeedback) {
		this.userFeedback = userFeedback;
	}
	public String getMyVcardSave() {
		return myVcardSave;
	}
	public void setMyVcardSave(String myVcardSave) {
		this.myVcardSave = myVcardSave;
	}
	public String getCloudContactsSync() {
		return cloudContactsSync;
	}
	public void setCloudContactsSync(String cloudContactsSync) {
		this.cloudContactsSync = cloudContactsSync;
	}
	public String getCloudContactsBackup() {
		return cloudContactsBackup;
	}
	public void setCloudContactsBackup(String cloudContactsBackup) {
		this.cloudContactsBackup = cloudContactsBackup;
	}
	public String getCloudPrivateCallBackup() {
		return cloudPrivateCallBackup;
	}
	public void setCloudPrivateCallBackup(String cloudPrivateCallBackup) {
		this.cloudPrivateCallBackup = cloudPrivateCallBackup;
	}
	public String getCloudPrivateCallSync() {
		return cloudPrivateCallSync;
	}
	public void setCloudPrivateCallSync(String cloudPrivateCallSync) {
		this.cloudPrivateCallSync = cloudPrivateCallSync;
	}
	public String getCloudPrivateContactsBackup() {
		return cloudPrivateContactsBackup;
	}
	public void setCloudPrivateContactsBackup(String cloudPrivateContactsBackup) {
		this.cloudPrivateContactsBackup = cloudPrivateContactsBackup;
	}
	public String getCloudPrivateContactsSync() {
		return cloudPrivateContactsSync;
	}
	public void setCloudPrivateContactsSync(String cloudPrivateContactsSync) {
		this.cloudPrivateContactsSync = cloudPrivateContactsSync;
	}
	public String getCloudPrivateSmsBackup() {
		return cloudPrivateSmsBackup;
	}
	public void setCloudPrivateSmsBackup(String cloudPrivateSmsBackup) {
		this.cloudPrivateSmsBackup = cloudPrivateSmsBackup;
	}
	public String getCloudPrivateSmsSync() {
		return cloudPrivateSmsSync;
	}
	public void setCloudPrivateSmsSync(String cloudPrivateSmsSync) {
		this.cloudPrivateSmsSync = cloudPrivateSmsSync;
	}
	public String getMyContactBlackListUpdate() {
		return myContactBlackListUpdate;
	}
	public void setMyContactBlackListUpdate(String myContactBlackListUpdate) {
		this.myContactBlackListUpdate = myContactBlackListUpdate;
	}
	public String getMyContact() {
		return myContact;
	}
	public void setMyContact(String myContact) {
		this.myContact = myContact;
	}
	public String getImgBaseService() {
		return imgBaseService;
	}
	public void setImgBaseService(String imgBaseService) {
		this.imgBaseService = imgBaseService;
	}
	public String getIntegeralChange() {
		return integeralChange;
	}
	public void setIntegeralChange(String integeralChange) {
		this.integeralChange = integeralChange;
	}
	public String getMyInfo() {
		return myInfo;
	}
	public void setMyInfo(String myInfo) {
		this.myInfo = myInfo;
	}
	public String getGroupSave() {
		return groupSave;
	}
	public void setGroupSave(String groupSave) {
		this.groupSave = groupSave;
	}
	public String getPasswordVerify() {
		return passwordVerify;
	}
	public void setPasswordVerify(String passwordVerify) {
		this.passwordVerify = passwordVerify;
	}
	public String getMobileBind() {
		return mobileBind;
	}
	public void setMobileBind(String mobileBind) {
		this.mobileBind = mobileBind;
	}
	public String getMyVcard() {
		return myVcard;
	}
	public void setMyVcard(String myVcard) {
		this.myVcard = myVcard;
	}
	public String getGroupMove() {
		return groupMove;
	}
	public void setGroupMove(String groupMove) {
		this.groupMove = groupMove;
	}
	public String getMyVcardDelete() {
		return myVcardDelete;
	}
	public void setMyVcardDelete(String myVcardDelete) {
		this.myVcardDelete = myVcardDelete;
	}
	public String getGroupDelete() {
		return groupDelete;
	}
	public void setGroupDelete(String groupDelete) {
		this.groupDelete = groupDelete;
	}
	public String getCommonUpload() {
		return commonUpload;
	}
	public void setCommonUpload(String commonUpload) {
		this.commonUpload = commonUpload;
	}
	public String getMyContactDelete() {
		return myContactDelete;
	}
	public void setMyContactDelete(String myContactDelete) {
		this.myContactDelete = myContactDelete;
	}
	public String getMySetting() {
		return mySetting;
	}
	public void setMySetting(String mySetting) {
		this.mySetting = mySetting;
	}
	public String getLoginThird() {
		return loginThird;
	}
	public void setLoginThird(String loginThird) {
		this.loginThird = loginThird;
	}
	public String getEnterpriseMemberSave() {
		return enterpriseMemberSave;
	}
	public void setEnterpriseMemberSave(String enterpriseMemberSave) {
		this.enterpriseMemberSave = enterpriseMemberSave;
	}
	public String getEnterpriseMemberRoleSave() {
		return enterpriseMemberRoleSave;
	}
	public void setEnterpriseMemberRoleSave(String enterpriseMemberRoleSave) {
		this.enterpriseMemberRoleSave = enterpriseMemberRoleSave;
	}
	public String getEnterpriseList() {
		return enterpriseList;
	}
	public void setEnterpriseList(String enterpriseList) {
		this.enterpriseList = enterpriseList;
	}
	public String getEnterPriseMemberList() {
		return enterPriseMemberList;
	}
	public void setEnterPriseMemberList(String enterPriseMemberList) {
		this.enterPriseMemberList = enterPriseMemberList;
	}
	public String getEnterPriseModify() {
		return enterPriseModify;
	}
	public void setEnterPriseModify(String enterPriseModify) {
		this.enterPriseModify = enterPriseModify;
	}
	public String getEnterPriseApprove() {
		return enterPriseApprove;
	}
	public void setEnterPriseApprove(String enterPriseApprove) {
		this.enterPriseApprove = enterPriseApprove;
	}
	public String getSmsVerifyCode() {
		return smsVerifyCode;
	}
	public void setSmsVerifyCode(String smsVerifyCode) {
		this.smsVerifyCode = smsVerifyCode;
	}
	public String getNearbyPeopleList() {
		return nearbyPeopleList;
	}
	public void setNearbyPeopleList(String nearbyPeopleList) {
		this.nearbyPeopleList = nearbyPeopleList;
	}
	public String getBroadcastAll() {
		return broadcastAll;
	}
	public void setBroadcastAll(String broadcastAll) {
		this.broadcastAll = broadcastAll;
	}
	public String getBroadcastAdd() {
		return broadcastAdd;
	}
	public void setBroadcastAdd(String broadcastAdd) {
		this.broadcastAdd = broadcastAdd;
	}
	public String getBroadcastReplyList() {
		return broadcastReplyList;
	}
	public void setBroadcastReplyList(String broadcastReplyList) {
		this.broadcastReplyList = broadcastReplyList;
	}
	public String getBroadcastReplyAdd() {
		return broadcastReplyAdd;
	}
	public void setBroadcastReplyAdd(String broadcastReplyAdd) {
		this.broadcastReplyAdd = broadcastReplyAdd;
	}
	public String getResetPwd() {
		return resetPwd;
	}
	public void setResetPwd(String resetPwd) {
		this.resetPwd = resetPwd;
	}
	public String getCardPerson() {
		return cardPerson;
	}
	public void setCardPerson(String cardPerson) {
		this.cardPerson = cardPerson;
	}
	public String getRecommonUrl() {
		return recommonUrl;
	}
	public void setRecommonUrl(String recommonUrl) {
		this.recommonUrl = recommonUrl;
	}
	public String getMessageGroupChatCreate() {
		return messageGroupChatCreate;
	}
	public void setMessageGroupChatCreate(String messageGroupChatCreate) {
		this.messageGroupChatCreate = messageGroupChatCreate;
	}
	public String getMessageGroupChatAdd() {
		return messageGroupChatAdd;
	}
	public void setMessageGroupChatAdd(String messageGroupChatAdd) {
		this.messageGroupChatAdd = messageGroupChatAdd;
	}
	public String getMessageGroupChatDelete() {
		return messageGroupChatDelete;
	}
	public void setMessageGroupChatDelete(String messageGroupChatDelete) {
		this.messageGroupChatDelete = messageGroupChatDelete;
	}
	public String getMessageGroupMemberSelect() {
		return messageGroupMemberSelect;
	}
	public void setMessageGroupMemberSelect(String messageGroupMemberSelect) {
		this.messageGroupMemberSelect = messageGroupMemberSelect;
	}
	public String getRecommendRecordUrl() {
		return recommendRecordUrl;
	}
	public void setRecommendRecordUrl(String recommendRecordUrl) {
		this.recommendRecordUrl = recommendRecordUrl;
	}
	public String getMessageGroupDel() {
		return messageGroupDel;
	}
	public void setMessageGroupDel(String messageGroupDel) {
		this.messageGroupDel = messageGroupDel;
	}
	public String getMessageGroupQuit() {
		return messageGroupQuit;
	}
	public void setMessageGroupQuit(String messageGroupQuit) {
		this.messageGroupQuit = messageGroupQuit;
	}
	public String getGroupInfoList() {
		return groupInfoList;
	}
	public void setGroupInfoList(String groupInfoList) {
		this.groupInfoList = groupInfoList;
	}
	public String getMessageGroupUpdate() {
		return messageGroupUpdate;
	}
	public void setMessageGroupUpdate(String messageGroupUpdate) {
		this.messageGroupUpdate = messageGroupUpdate;
	}
	public String getCloudLogs() {
		return cloudLogs;
	}
	public void setCloudLogs(String cloudLogs) {
		this.cloudLogs = cloudLogs;
	}
	public String getTemplateDownload() {
		return templateDownload;
	}
	public void setTemplateDownload(String templateDownload) {
		this.templateDownload = templateDownload;
	}
	public String getMyTemplateList() {
		return myTemplateList;
	}
	public void setMyTemplateList(String myTemplateList) {
		this.myTemplateList = myTemplateList;
	}
	public String getMyContactCardSave() {
		return myContactCardSave;
	}
	public void setMyContactCardSave(String myContactCardSave) {
		this.myContactCardSave = myContactCardSave;
	}
	public String getCardFixList() {
		return cardFixList;
	}
	public void setCardFixList(String cardFixList) {
		this.cardFixList = cardFixList;
	}

	public String getRadarScan() {
		return radarScan;
	}

	public void setRadarScan(String radarScan) {
		this.radarScan = radarScan;
	}

	public String getRadarRemove() {
		return radarRemove;
	}

	public void setRadarRemove(String radarRemove) {
		this.radarRemove = radarRemove;
	}

	public String getVcardTemplateDelete() {
		return vcardTemplateDelete;
	}

	public void setVcardTemplateDelete(String vcardTemplateDelete) {
		this.vcardTemplateDelete = vcardTemplateDelete;
	}

	public String getSwapBaseService() {
		return swapBaseService;
	}

	public void setSwapBaseService(String swapBaseService) {
		this.swapBaseService = swapBaseService;
	}

	public String getAttentionAdd() {
		return vcardAttentionAdd;
	}

	public void setAttentionAdd(String vcardAttentionAdd) {
		this.vcardAttentionAdd = vcardAttentionAdd;
	}

	public String getAttentionDel() {
		return vcardAttentionDel;
	}

	public void setAttentionDel(String vcardAttentionDel) {
		this.vcardAttentionDel = vcardAttentionDel;
	}

	public String getFansList() {
		return vcardFansList;
	}

	public void setFansList(String vcardFansList) {
		this.vcardFansList = vcardFansList;
	}

	public String getAttentionList() {
		return vcardAttentionList;
	}

	public void setAttentionList(String vcardAttentionList) {
		this.vcardAttentionList = vcardAttentionList;
	}

	public String getVisitorList() {
		return vcardVisitorList;
	}

	public void setVisitorList(String vcardVisitorList) {
		this.vcardVisitorList = vcardVisitorList;
	}

	public String getPersonMessage() {
		return vcardPersonMessage;
	}

	public void setPersonMessage(String vcardPersonMessage) {
		this.vcardPersonMessage = vcardPersonMessage;
	}

	public String getEnterpriseNotice() {
		return enterpriseNotice;
	}

	public void setEnterpriseNotice(String enterpriseNotice) {
		this.enterpriseNotice = enterpriseNotice;
	}

	public String getEnterpriseGetNotice() {
		return enterpriseGetNotice;
	}

	public void setEnterpriseGetNotice(String enterpriseGetNotice) {
		this.enterpriseGetNotice = enterpriseGetNotice;
	}
	
}

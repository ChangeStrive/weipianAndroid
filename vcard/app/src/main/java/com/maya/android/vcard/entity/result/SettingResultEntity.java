package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 我的设置信息
 * @author zheng_cz
 * @since 2014年6月26日 下午3:46:12
 */
public class SettingResultEntity {
	@SerializedName("id")
	private long id;
	@SerializedName("accountId")
	private long accountId;
	@SerializedName("setting")
	private String settingStr;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getSettingStr() {
		return settingStr;
	}
	public void setSettingStr(String setJson) {
		this.settingStr = setJson;
	}
}

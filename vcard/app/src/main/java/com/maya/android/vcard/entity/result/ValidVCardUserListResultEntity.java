package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ResultEntity:请求匹配通讯录的微片的用户
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-5-13
 *
 */
public class ValidVCardUserListResultEntity {
	@SerializedName("validVcardUserList")
	private ArrayList<VCardUserFromMobileResultEntity> validVcardUserList;

	public ArrayList<VCardUserFromMobileResultEntity> getValidVcardUserList() {
		return validVcardUserList;
	}

	public void setValidVcardUserList(
			ArrayList<VCardUserFromMobileResultEntity> validVcardUserList) {
		this.validVcardUserList = validVcardUserList;
	}
	
	
}

package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 删除联系人操作
 * @author zheng_cz
 * @since 2014年4月14日 上午11:04:31
 */
public class ContactDeleteJsonEntity {
	/** 交换名片的id集合  */
	@SerializedName("cardIdList")
	private ArrayList<Long> cardIdList;
	/** 本地新增名片的 联系人id **/
	@SerializedName("contactCardIdList")
	private ArrayList<Long> contactPersonIdList;
	
	public ArrayList<Long> getCardIdList() {
		return cardIdList;
	}
	public void setCardIdList(ArrayList<Long> cardIdList) {
		this.cardIdList = cardIdList;
	}
	public ArrayList<Long> getContactPersonIdList() {
		return contactPersonIdList;
	}
	public void setContactPersonIdList(ArrayList<Long> contactPersonIdList) {
		this.contactPersonIdList = contactPersonIdList;
	}
}

package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.json.ContactBookBackupJsonEntity;
import com.maya.android.vcard.entity.json.ContactVCardBackupJsonEntity;

import java.util.ArrayList;

/**
 * 恢复实体
 * @author zheng_cz
 * @since 2014年4月15日 下午2:51:14
 */
public class RecoverResultEntity {
	/**
	 * 名片夹联系人list json串
	 */
	@SerializedName("cardPersonList")
	protected ArrayList<ContactVCardBackupJsonEntity> cardPersonList;
	/**
	 * 名片夹分组list json串
	 */
	@SerializedName("cardGroupList")
	protected ArrayList<ContactGroupBackupEntity> cardGroupList;
	
	/**
	 * 通讯录联系人list json串
	 */
	@SerializedName("bookPersonList")
	protected ArrayList<ContactBookBackupJsonEntity> bookPersonList;
	/**
	 * 通讯录分组list json串
	 */
	@SerializedName("bookGroupList")
	protected ArrayList<ContactGroupBackupEntity> bookGroupList;

	/**
	 * 名片夹联系人数
	 */
	@SerializedName("cardContactTotal")
	private int cardContactTotal;
	/**
	 * 通讯录联系人数
	 */
	@SerializedName("bookContactTotal")
	private int bookContactTotal;
	public ArrayList<ContactVCardBackupJsonEntity> getCardPersonList() {
		return cardPersonList;
	}
	public void setCardPersonList(ArrayList<ContactVCardBackupJsonEntity> cardPersonList) {
		this.cardPersonList = cardPersonList;
	}
	public ArrayList<ContactGroupBackupEntity> getCardGroupList() {
		return cardGroupList;
	}
	public void setCardGroupList(ArrayList<ContactGroupBackupEntity> cardGroupList) {
		this.cardGroupList = cardGroupList;
	}
	public ArrayList<ContactBookBackupJsonEntity> getBookPersonList() {
		return bookPersonList;
	}
	public void setBookPersonList(ArrayList<ContactBookBackupJsonEntity> bookPersonList) {
		this.bookPersonList = bookPersonList;
	}
	public ArrayList<ContactGroupBackupEntity> getBookGroupList() {
		return bookGroupList;
	}
	public void setBookGroupList(ArrayList<ContactGroupBackupEntity> bookGroupList) {
		this.bookGroupList = bookGroupList;
	}
	public int getCardContactTotal() {
		return cardContactTotal;
	}
	public void setCardContactTotal(int cardContactTotal) {
		this.cardContactTotal = cardContactTotal;
	}
	public int getBookContactTotal() {
		return bookContactTotal;
	}
	public void setBookContactTotal(int bookContactTotal) {
		this.bookContactTotal = bookContactTotal;
	}
}

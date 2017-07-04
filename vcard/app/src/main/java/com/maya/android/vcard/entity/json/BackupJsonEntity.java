package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;

import java.util.ArrayList;

/**
 * 备份实体类
 * @author zheng_cz
 * @since 2014年4月15日 下午2:50:53
 */
public class BackupJsonEntity {
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
	 * 备份类型 (1：本机为准覆盖云端  2：合并云端和本机，)
	 */
	@SerializedName("backupType")
	private int backupType;

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

	public int getBackupType() {
		return backupType;
	}

	public void setBackupType(int backupType) {
		this.backupType = backupType;
	}
	/**
	 * 名片联系人数
	 * @return
	 */
	public int getCardContactTotal(){
		int total = 0;
		if(Helper.isNotNull(this.cardPersonList)){
			total = this.cardPersonList.size();
		}
		return total;
	}
	/**
	 * 通讯录联系人
	 * @return
	 */
	public int getBookContactTotal(){
		int total = 0;
		if(Helper.isNotNull(this.bookPersonList)){
			total = this.bookPersonList.size();
		}
		return total;
	}
}

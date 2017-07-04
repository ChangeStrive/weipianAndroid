package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 备份恢复日志实体类
 * @author zheng_cz
 * @since 2014年4月15日 下午2:34:45
 */
public class BackupLogResultEntity {
	/** 备份时间 **/
	@SerializedName("backupDate")
	private String backupDate;
	/** 恢复时间  **/
	@SerializedName("syncDate")
	private String syncDate;
	/** 名片夹总数 **/
	@SerializedName("cardContactTotal")
	private int cardContactTotal;
	/** 名片夹大小 **/
	@SerializedName("cardContactSize")
	private String cardContactSize;
	/** 通讯录联系人数  **/
	@SerializedName("bookContactTotal")
	private int bookContactTotal;
	/** 通讯录联系人大小 **/
	@SerializedName("bookContactSize")
	private String bookContactSize;
	public String getBackupDate() {
		return backupDate;
	}
	public void setBackupDate(String backupDate) {
		this.backupDate = backupDate;
	}
	public String getSyncDate() {
		return syncDate;
	}
	public void setSyncDate(String syncDate) {
		this.syncDate = syncDate;
	}
	public int getCardContactTotal() {
		return cardContactTotal;
	}
	public void setCardContactTotal(int cardContactTotal) {
		this.cardContactTotal = cardContactTotal;
	}
	public String getCardContactSize() {
		return cardContactSize;
	}
	public void setCardContactSize(String cardContactSize) {
		this.cardContactSize = cardContactSize;
	}
	public int getBookContactTotal() {
		return bookContactTotal;
	}
	public void setBookContactTotal(int bookContactTotal) {
		this.bookContactTotal = bookContactTotal;
	}
	public String getBookContactSize() {
		return bookContactSize;
	}
	public void setBookContactSize(String bookContactSize) {
		this.bookContactSize = bookContactSize;
	}
}

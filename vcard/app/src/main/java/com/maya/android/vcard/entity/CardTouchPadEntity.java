package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 名片蒙板参数实体
 * @author hzq
 *
 * 2014-1-27 上午11:20:00
 */
public class CardTouchPadEntity {
	
	//#region constant
	public static final int NULL_TAG_ID = -1;
	public static final int COMPANY_NAME = 0;
	public static final int ADDRESS = 1;
	public static final int MOBILE = 2;
	public static final int TELEPHONE = 3;
	public static final int HOME_PAGE = 4;
	public static final int EMAIL = 5;
	//#endregion
	
	@SerializedName("id")
	private long templateId;
	@SerializedName("width")
	private int width;
	@SerializedName("height")
	private int height;
	/**是否纵向**/
	@SerializedName("isVertical")
	private boolean isVertical;
	/**蒙板参数实体**/
	@SerializedName("lucidEntityList")
	private LucidEntity[] lucidEntityList;
	
	
	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	public LucidEntity[] getLucidEntityList() {
		return lucidEntityList;
	}

	public void setLucidEntityList(LucidEntity[] lucidEntityList) {
		this.lucidEntityList = lucidEntityList;
	}

}

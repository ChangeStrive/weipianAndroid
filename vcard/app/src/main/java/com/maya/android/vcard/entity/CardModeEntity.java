package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 名片模板数据接
 * @author hzq
 *
 * 2013-8-21 下午2:06:05
 */
public class CardModeEntity {
	/**
	 * 是否是纵向名片
	 */
	@SerializedName("vertical")
	private boolean isVertical;
	/**
	 * 模板的实际宽
	 */
	@SerializedName("width")
	private int width = 480;
	/**
	 * 模板的实际高
	 */
	@SerializedName("height")
	private int height = 800;
	
	/**
	 * 背影图Url的正面
	 */
	@SerializedName("backgroundImage")
	private String backgroundImage;
	/**
	 * 图片展示类型-正面
	 */
	@SerializedName("cardModeImageArray")
	private CardModeImageEntity[] imageEntity;
	/**
	 * 数据展示类型-正面
	 */
	@SerializedName("cardModeTextArray")
	private CardModeTextEntity[] textEntity;
	
	/**
	 * 背景图URL的背面
	 */
	@SerializedName("backgroundImageBack")
	private String backgroundImageBack;
	
	/**
	 * 图片展示类型-背面
	 */
	@SerializedName("cardModeImageBackArray")
	private CardModeImageEntity[] imageEntityBack;
	/**
	 * 数据展示类型-背面
	 */
	@SerializedName("cardModeTextBackArray")
	private CardModeTextEntity[] textEntityBack;
	

	public boolean isVertical() {
		return isVertical;
	}
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}
	
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
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
	public CardModeImageEntity[] getImageEntity() {
		return imageEntity;
	}
	public void setImageEntity(CardModeImageEntity[] imageEntity) {
		this.imageEntity = imageEntity;
	}
	public CardModeTextEntity[] getTextEntity() {
		return textEntity;
	}
	public void setTextEntity(CardModeTextEntity[] textEntity) {
		this.textEntity = textEntity;
	}
	public String getBackgroundImageBack() {
		return backgroundImageBack;
	}
	public void setBackgroundImageBack(String backgroundImageBack) {
		this.backgroundImageBack = backgroundImageBack;
	}
	public CardModeImageEntity[] getImageEntityBack() {
		return imageEntityBack;
	}
	public void setImageEntityBack(CardModeImageEntity[] imageEntityBack) {
		this.imageEntityBack = imageEntityBack;
	}
	public CardModeTextEntity[] getTextEntityBack() {
		return textEntityBack;
	}
	public void setTextEntityBack(CardModeTextEntity[] textEntityBack) {
		this.textEntityBack = textEntityBack;
	}
	
}

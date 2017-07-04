package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 名片模板TextView类型数据
 * @author hzq
 *
 * 2013-8-21 下午2:08:27
 */
public class CardModeTextEntity {
	
	/**
	 * 名字
	 */
	@SerializedName("name")
	private String name;
	/**
	 * 是否显示标签名
	 */
	@SerializedName("isShow")
	private boolean isShow;
	/**特殊标签**/
	@SerializedName("showTag")
	private String showTag;
	/**
	 *  字体大小
	 */
	@SerializedName("fontSize")
	private float fontSize;
	/**
	 * 颜色
	 * a-r-g-b格式(0-255)
	 * 255|255|255|255
	 */
	@SerializedName("fontColor")
	private String fontColor;
	/**
	 * 最大字数
	 */
	@SerializedName("maxEms")
	private int maxEms;
	/**
	 * 最小字数
	 */
	@SerializedName("minEms")
	private int minEms;
	/**
	 * 上边距dp(要转像素)
	 */
	@SerializedName("top")
	private int top;
	/**
	 * 下边距dp
	 */
	@SerializedName("bottom")
	private int bottom;
	/**
	 * 左边距
	 */
	@SerializedName("left")
	private int left;
	/**
	 * 右边距
	 */
	@SerializedName("right")
	private int right;
	/**
	 * 相对布局的规则
	 */
	@SerializedName("rule")
	private int[] rule;
	/**
	 * 显示位置
	 */
	@SerializedName("gravity")
	private int gravity;
	/**
	 * 字间距
	 */
	@SerializedName("fontScaleX")
	private float fontScaleX;
	/**
	 * 顺时针旋转角度
	 */
	@SerializedName("rotation")
	private float rotation;
	/**
	 * 字体类型
	 */
	@SerializedName("fontFamily")
	private int fontFamily;
	/**
	 * 是否粗体
	 */
	@SerializedName("hasBold")
	private boolean hasBold;
	/**
	 * 是否斜体
	 */
	@SerializedName("hasItalics")
	private boolean hasItalics;
	
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int[] getRule() {
		return rule;
	}
	public void setRule(int[] rule) {
		this.rule = rule;
	}
	
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	public int getGravity() {
		return gravity;
	}
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}
	public int getMaxEms() {
		return maxEms;
	}
	public void setMaxEms(int maxEms) {
		this.maxEms = maxEms;
	}
	public int getMinEms() {
		return minEms;
	}
	public void setMinEms(int minEms) {
		this.minEms = minEms;
	}
	public float getFontScaleX() {
		return fontScaleX;
	}
	public void setFontScaleX(float fontScaleX) {
		this.fontScaleX = fontScaleX;
	}
	public String getShowTag() {
		return showTag;
	}
	public void setShowTag(String showTag) {
		this.showTag = showTag;
	}
	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	public int getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(int fontFamily) {
		this.fontFamily = fontFamily;
	}
	public boolean isHasBold() {
		return hasBold;
	}
	public void setHasBold(boolean hasBold) {
		this.hasBold = hasBold;
	}
	public boolean isHasItalics() {
		return hasItalics;
	}
	public void setHasItalics(boolean hasItalics) {
		this.hasItalics = hasItalics;
	}
	
}

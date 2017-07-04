package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 名片模板Image类型数据
 * @author hzq
 *
 * 2013-8-21 下午2:08:50
 */
public class CardModeImageEntity {
	
	/**
	 * 名字
	 */
	@SerializedName("name")
	private String name;
	/**
	 * 图像框资源路径
	 */
	private String imgUrl;
	/**
	 * 宽
	 */
	@SerializedName("width")
	private int width;
	/**
	 * 高
	 */
	@SerializedName("height")
	private int height;
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
	 * 相对位置
	 */
	@SerializedName("rule")
	private int[] rule;
	/**
	 * 相对的gravity值
	 */
	@SerializedName("gravity")
	private int gravity;
	/**
	 * 顺时针旋转角度
	 */
	@SerializedName("rotation")
	private float rotation;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public int getGravity() {
		return gravity;
	}
	public void setGravity(int gravity) {
		this.gravity = gravity;
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
	public float getRotation() {
		return rotation;
	}
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
}

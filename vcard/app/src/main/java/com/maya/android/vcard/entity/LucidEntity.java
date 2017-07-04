package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
/**
 * "可点击"蒙版实体
 * @author hzq
 *
 * 2014-2-8 上午9:31:10
 */
public class LucidEntity {
	@SerializedName("tagId")
	private int tagId;
	@SerializedName("top")
	private int top;
	@SerializedName("left")
	private int left;
	@SerializedName("right")
	private int right;
	@SerializedName("bottom")
	private int bottom;
	@SerializedName("width")
	private int width;
	@SerializedName("height")
	private int height;

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
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

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
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
}

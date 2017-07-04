package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.CardModeEntity;

/**
 * 名片模板请求
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-23
 *
 */
public class VCardTemplateResultEntity {
	
	/**
	 * 模板ID
	 */
	@SerializedName("id")
	private long id;
	/**
	 * 模板名称
	 */
	@SerializedName("name")
	private String name;
	/**
	 * 模板类型  (0：免费，1：vip，2：积分，3：微币)
	 * 常量类  TableConstant.TemplateType
	 */
	@SerializedName("templateType")
	private int type;
	/**
	 * 是否是纵向名片
	 */
	@SerializedName("vertical")
	private boolean vertical;
	/**
	 * 金额
	 */
	@SerializedName("money")
	private float money;
	/**
	 * 样式数据
	 */
	@SerializedName("style")
	private CardModeEntity style;
	
	/**
	 * 创建时间
	 */
	@SerializedName("createTime")
	private String createTime;
	
	/**更改版本*/
	@SerializedName("verson")
	private int verson;
	/**行业类型*/
	@SerializedName("classType")
	private int classId;
	/**
	 * 正面展示图
	 */
	@SerializedName("showImage")
	private String showImage;
	/**
	 * 背面展示图
	 */
	@SerializedName("showImageBack")
	private String showImageBack;
	/**
	 * 是否有logo
	 */
	@SerializedName("logo")
	private boolean hasLogo;
	/**是否可上传 头像 */
	@SerializedName("hasHeadImg")
	private boolean hasHeadImg;
	/**是否线上模板*/
	private boolean online;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isVertical() {
		return vertical;
	}
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	public CardModeEntity getStyle() {
		return style;
	}
	public void setStyle(CardModeEntity style) {
		this.style = style;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getVerson() {
		return verson;
	}
	public void setVerson(int verson) {
		this.verson = verson;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getShowImage() {
		return showImage;
	}
	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}
	public String getShowImageBack() {
		return showImageBack;
	}
	public void setShowImageBack(String showImageBack) {
		this.showImageBack = showImageBack;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public boolean isHasLogo() {
		return hasLogo;
	}
	public void setHasLogo(boolean hasLogo) {
		this.hasLogo = hasLogo;
	}
	public boolean isHasHeadImg() {
		return hasHeadImg;
	}
	public void setHasHeadImg(boolean hasHeadImg) {
		this.hasHeadImg = hasHeadImg;
	}
//	/**
//	 * 获取样式实体类
//	 * @return
//	 */
//	public CardModeEntity getCardModeEntity() {
//		if(Helper.isNotEmpty(style)){
//			return JsonHelper.fromJson(style, CardModeEntity.class);
//		}
//		return null;
//	}
	
	
}

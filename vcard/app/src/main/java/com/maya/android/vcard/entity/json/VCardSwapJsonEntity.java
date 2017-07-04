package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * JsonEntity:我的名片交换
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-14
 *
 */
public class VCardSwapJsonEntity {
	@SerializedName("cardId")
	private long cardId;
	@SerializedName("sendType")
	private int sendType;
	@SerializedName("cweAngle")
	private float cweAngle;
	
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public float getCweAngle() {
		return cweAngle;
	}
	public void setCweAngle(float cweAngle) {
		this.cweAngle = cweAngle;
	}
	/**
	 * 名片交换JsonEntity
	 * @param cardId 要交换的名片Id
	 * @param sendType 交换方式
	 * @param cweAngle 偏角
	 */
	public VCardSwapJsonEntity(long cardId, int sendType, float cweAngle){
		this.cardId = cardId;
		this.sendType = sendType;
		this.cweAngle = cweAngle;
	}
}

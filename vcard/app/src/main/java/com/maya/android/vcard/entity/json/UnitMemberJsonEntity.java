package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 保存企业成员实体对象
 * @author zheng_cz
 * @since 2014年4月10日 上午10:02:55
 */
public class UnitMemberJsonEntity {
	/** 成员Id  */
	@SerializedName("id")
	private Long memberId;
	/** 帐户ID  */
	@SerializedName("accountId")
	private long accountId;
	/** 名片ID  */
	@SerializedName("cardId")
	private long cardId;
	/**  企业ID  */
	@SerializedName("enterpriseId")
	private long enterpriseId;
	/**  描述  */
	@SerializedName("description")
	private String description;
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

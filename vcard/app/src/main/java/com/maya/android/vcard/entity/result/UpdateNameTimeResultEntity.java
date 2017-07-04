package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
/**
 * 密码修改次数
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2015-7-31
 *
 */
public class UpdateNameTimeResultEntity {
	/** 密码修改次数 **/
	@SerializedName("updNameTime")
	private int updNameTime;

	public int getUpdNameTime() {
		return updNameTime;
	}

	public void setUpdNameTime(int updNameTime) {
		this.updNameTime = updNameTime;
	}
}

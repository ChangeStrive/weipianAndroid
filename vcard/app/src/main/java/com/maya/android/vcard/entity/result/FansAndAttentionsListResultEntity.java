package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
/**
 * ResultEntity:粉丝/关注 列表实体
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2015-6-30
 *
 */
public class FansAndAttentionsListResultEntity {
	@SerializedName("fansList")
	private ArrayList<UserAttAndFansEntity> fansList;

	public ArrayList<UserAttAndFansEntity> getFansList() {
		return fansList;
	}

	public void setFansList(ArrayList<UserAttAndFansEntity> fansList) {
		this.fansList = fansList;
	}
	
}

package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 雷达搜索结果实体类
 * @author zheng_cz
 * @since 2014年5月12日 下午3:50:59
 */
public class RadarSearchResultEntity {
	@SerializedName("spList")
	private ArrayList<ContactRadarResultEntity> contactList;

	public ArrayList<ContactRadarResultEntity> getContactList() {
		return contactList;
	}

	public void setContactList(ArrayList<ContactRadarResultEntity> contactList) {
		this.contactList = contactList;
	}
}

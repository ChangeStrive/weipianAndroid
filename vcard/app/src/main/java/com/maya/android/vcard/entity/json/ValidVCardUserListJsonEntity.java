
package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * JsonEntity:请求匹配通讯录的微片的用户
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-5-13
 *
 */
public class ValidVCardUserListJsonEntity {
	@SerializedName("mobileList")
	private ArrayList<String> mobileList;

	public ArrayList<String> getMobileList() {
		return mobileList;
	}

	public void setMobileList(ArrayList<String> mobileList) {
		this.mobileList = mobileList;
	}
	
	public ValidVCardUserListJsonEntity(ArrayList<String> mobileList){
		this.mobileList = mobileList;
	}
}

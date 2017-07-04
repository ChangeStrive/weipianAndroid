package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.ContactEntity;

import java.util.ArrayList;

/**
 * Result Entity：我的联系人列表
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-3
 */
public class ContactsListResultEntity {
	@SerializedName("personList")
	private ArrayList<ContactEntity> contactEntityList;

	public ArrayList<ContactEntity> getContactEntityList() {
		return contactEntityList;
	}

	public void setContactEntityList(ArrayList<ContactEntity> contactEntityList) {
		this.contactEntityList = contactEntityList;
	}
	
	
}

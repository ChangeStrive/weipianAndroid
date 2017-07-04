package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * JsonEntity：修改用户密码
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-28
 */
public class ChangePasswordJsonEntity {
	@SerializedName("oldPwd")
	private String oldPwd;
	@SerializedName("newPwd")
	private String newPwd;
	@SerializedName("operateType")
	private int operateType;
	
	public ChangePasswordJsonEntity(String oldPwd, String newPwd, int operateType){
		this.oldPwd = oldPwd;
		this.newPwd = newPwd;
		this.operateType = operateType;
	}
}

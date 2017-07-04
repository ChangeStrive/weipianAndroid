package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * ResultEntity:图片上传
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-16
 *
 */
public class UploadImageResultEntity {
	@SerializedName("fileUrl")
	private ArrayList<String> fileUrl;

	public ArrayList<String> getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(ArrayList<String> fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	
}

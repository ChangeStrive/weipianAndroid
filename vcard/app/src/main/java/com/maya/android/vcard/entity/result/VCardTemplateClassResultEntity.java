package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * Result Entity:线上模板行业分类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-26
 *
 */
public class VCardTemplateClassResultEntity {
	@SerializedName("classType")
	private int classId;
	private String className;
	@SerializedName("templateList")
	private ArrayList<VCardTemplateResultEntity> cardTemplateList;
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassName() {
		if(ResourceHelper.isEmpty(className) && classId > 0){
			//TODO 模版判断缺失
//			className = ProjectHelper.getVCardTemplateClassName(classId);
		}
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public ArrayList<VCardTemplateResultEntity> getTemplateList() {
		return cardTemplateList;
	}
	public void setTemplateList(ArrayList<VCardTemplateResultEntity> cardList) {
		this.cardTemplateList = cardList;
	}
}

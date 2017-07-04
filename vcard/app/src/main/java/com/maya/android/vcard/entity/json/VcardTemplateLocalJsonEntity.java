package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.result.VCardTemplateResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
/**
 * Json Entity :本地自带模版数据
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-23
 *
 */
public class VcardTemplateLocalJsonEntity {
	private ArrayList<VCardTemplateResultEntity> tempList;
	
	@SerializedName("V10001")
	private VCardTemplateResultEntity temp10001;
	
	@SerializedName("V10002")
	private VCardTemplateResultEntity temp10002;
	
	@SerializedName("V10003")
	private VCardTemplateResultEntity temp10003;
	
	@SerializedName("V10004")
	private VCardTemplateResultEntity temp10004;
	
	@SerializedName("V10005")
	private VCardTemplateResultEntity temp10005;
	
	@SerializedName("V10006")
	private VCardTemplateResultEntity temp10006;
	
	@SerializedName("V10007")
	private VCardTemplateResultEntity temp10007;
	
	@SerializedName("V10008")
	private VCardTemplateResultEntity temp10008;
	
	@SerializedName("V10009")
	private VCardTemplateResultEntity temp10009;
	
	/**
	 * 取得本地自带模板数据
	 * @return
	 */
	public ArrayList<VCardTemplateResultEntity> getTempList(){
		if(Helper.isNull(tempList)){
			tempList = new ArrayList<VCardTemplateResultEntity>();
			tempList.add(getTemp10001());
			tempList.add(getTemp10002());
			tempList.add(getTemp10003());
			tempList.add(getTemp10004());
			tempList.add(getTemp10005());
			tempList.add(getTemp10006());
			tempList.add(getTemp10007());
			tempList.add(getTemp10008());
			tempList.add(getTemp10009());
		}
		return tempList;
	}
	
	public VCardTemplateResultEntity getTemp10001() {
		if(ResourceHelper.isNull(this.temp10001)){
			this.temp10001 = new VCardTemplateResultEntity();
		}
		return temp10001;
	}

	public void setTemp10001(VCardTemplateResultEntity temp10001) {
		this.temp10001 = temp10001;
	}

	public VCardTemplateResultEntity getTemp10002() {
		if(ResourceHelper.isNull(this.temp10002)){
			this.temp10002 = new VCardTemplateResultEntity();
		}
		return temp10002;
	}

	public void setTemp10002(VCardTemplateResultEntity temp10002) {
		this.temp10002 = temp10002;
	}

	public VCardTemplateResultEntity getTemp10003() {
		if(ResourceHelper.isNull(this.temp10003)){
			this.temp10003 = new VCardTemplateResultEntity();
		}
		return temp10003;
	}

	public void setTemp10003(VCardTemplateResultEntity temp10003) {
		this.temp10003 = temp10003;
	}

	public VCardTemplateResultEntity getTemp10004() {
		if(ResourceHelper.isNull(this.temp10004)){
			this.temp10004 = new VCardTemplateResultEntity();
		}
		return temp10004;
	}

	public void setTemp10004(VCardTemplateResultEntity temp10004) {
		this.temp10004 = temp10004;
	}

	public VCardTemplateResultEntity getTemp10005() {
		if(ResourceHelper.isNull(this.temp10005)){
			this.temp10005 = new VCardTemplateResultEntity();
		}
		return temp10005;
	}

	public void setTemp10005(VCardTemplateResultEntity temp10005) {
		this.temp10005 = temp10005;
	}

	public VCardTemplateResultEntity getTemp10006() {
		if(ResourceHelper.isNull(this.temp10006)){
			this.temp10006 = new VCardTemplateResultEntity();
		}
		return temp10006;
	}

	public void setTemp10006(VCardTemplateResultEntity temp10006) {
		this.temp10006 = temp10006;
	}

	public VCardTemplateResultEntity getTemp10007() {
		if(ResourceHelper.isNull(this.temp10007)){

		}
		return temp10007;
	}

	public void setTemp10007(VCardTemplateResultEntity temp10007) {
		this.temp10007 = temp10007;
	}

	public VCardTemplateResultEntity getTemp10008() {
		if(ResourceHelper.isNull(this.temp10008)){
			this.temp10008 = new VCardTemplateResultEntity();
		}
		return temp10008;
	}

	public void setTemp10008(VCardTemplateResultEntity temp10008) {
		this.temp10008 = temp10008;
	}

	public VCardTemplateResultEntity getTemp10009() {
		if(ResourceHelper.isNull(this.temp10009)){
			this.temp10009 = new VCardTemplateResultEntity();
		}
		return temp10009;
	}

	public void setTemp10009(VCardTemplateResultEntity temp10009) {
		this.temp10009 = temp10009;
	}
}

package com.platform.weixin.util;

import net.sf.json.JSONObject;

public class WeiXinMessage {

	private String title;
	
	private String description;

	private String url;
	
	private String picurl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	
	public JSONObject getObject(){
		JSONObject o=new JSONObject();
		o.put("title", this.title);
		if(this.description!=null){
			o.put("description", this.description);
		}
		if(this.url!=null){
			o.put("url",this.url);
		}
		if(this.picurl!=null){
			o.put("picurl", this.picurl);
		}
		return o;
	}
}

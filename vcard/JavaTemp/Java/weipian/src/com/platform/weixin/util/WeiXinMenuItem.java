package com.platform.weixin.util;

import java.util.List;

import com.platform.util.StringUtil;

public class WeiXinMenuItem {

	private String name;
	
	private String type;
	
	private String key;
	
	private String url;
	
	private List<WeiXinMenuItem> subItem=null;

	public WeiXinMenuItem(){
		
	}
	
	public WeiXinMenuItem(List<WeiXinMenuItem> subItem){
		this.subItem=subItem;
	}
	public WeiXinMenuItem(String name,List<WeiXinMenuItem> subItem){
		this.name=name;
		this.subItem=subItem;
	}
	
	public WeiXinMenuItem(String name,String type,String key){
		this.name=name;
		this.type=type;
		this.key=key;
	}
	
	public WeiXinMenuItem(String name,String type,String key,String url){
		this.name=name;
		this.type=type;
		this.key=key;
		this.url=url;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<WeiXinMenuItem> getSubItem() {
		return subItem;
	}

	public void setSubItem(List<WeiXinMenuItem> subItem) {
		this.subItem = subItem;
	}
	
	public String toString(){
		if(!(this.subItem!=null&&this.subItem.size()>0)){
			if(StringUtil.isNotNull(this.name)){
				String str="{\"name\":\""+this.name+"\",";
				str+="\"type\":\""+this.type+"\",";
				if(this.type.equals("click")&&this.key!=null){
					str+="\"key\":\""+this.key+"\"";
				}else if(this.type.equals("view")&&this.url!=null){
					str+="\"url\":\""+this.url+"\"";
				}else if(this.type.equals("scancode_push")&&this.key!=null){
					str+="\"key\":\""+this.key+"\"";
				}else if(this.type.equals("scancode_waitmsg")&&this.key!=null){
					str+="\"key\":\""+this.key+"\"";
				}
				str+="}";
				return str;
			}else{
				return "";
			}
		}else{
			String str="";
			if(name!=null){
			  str="{\"name\":\""+this.name+"\",\"sub_button\":[";
			}else{
				str="{\"button\":[";
			}
			for(int i=0;i<this.subItem.size();i++){
				WeiXinMenuItem item=this.subItem.get(i);
				str+=item.toString();
				if(i<this.subItem.size()-1){
					str+=",";
				}
			}
			str+="]}";
			return str;
		}
	}
}

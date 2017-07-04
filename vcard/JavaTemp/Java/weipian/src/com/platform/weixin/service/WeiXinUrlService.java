package com.platform.weixin.service;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.weixin.model.WeiXinUrl;

@Component
public class WeiXinUrlService extends BaseService {
	
	public WeiXinUrl saveUrl(String fdUrl){
		WeiXinUrl item=new WeiXinUrl();
		item.setFdUrl(fdUrl);
		dbAccessor.save(item);
		return item;
	} 
	
	public void deleteUrl(String fdId){
		String hql="delete from WeiXinUrl where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
	}

	public String get(String fdId) {
		// TODO Auto-generated method stub
		WeiXinUrl item=dbAccessor.get(WeiXinUrl.class, fdId);
		return item.getFdUrl();
	} 
}

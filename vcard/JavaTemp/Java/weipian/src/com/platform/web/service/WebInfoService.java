package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebInfo;

@Component
public class WebInfoService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebInfo model,String hql){
		if(StringUtil.isNotNull(model.getFdTypeId())){
			hql+=" and fdTypeId='"+model.getFdTypeId()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WebInfo> list(Map<String,String> map,WebInfo model,Integer start,Integer limit){
		String hql=" from WebInfo where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdCreateTime desc";
		List<WebInfo> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	
	public List<WebInfo> list(String fdTypeId){
		String hql=" from WebInfo where fdTypeId='"+fdTypeId+"'";
		hql+=" order by fdCreateTime desc";
		List<WebInfo> list=dbAccessor.find(hql);
		return list;
	}
	
	public int getCount(Map<String,String> map,WebInfo model){
		String hql="select count(*) from WebInfo where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public WebInfo save(WebInfo model) {
		model.setFdCreateTime(DateUtils.getNow());
		dbAccessor.save(model);
		return model;
	}
	
	public WebInfo update(WebInfo model) throws IllegalArgumentException, IllegalAccessException {
		WebInfo item = dbAccessor.get(WebInfo.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
		return item;
	}

	public void delete(String fdId) {
		String hql = "delete from WebInfo where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebInfo get(String fdId) {
		WebInfo item = dbAccessor.get(WebInfo.class, fdId);
		return item;
	}
}

package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebLink;

@Component
public class WebLinkService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebLink model,String hql){
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WebLink> list(Map<String,String> map,WebLink model,Integer start,Integer limit){
		String hql=" from WebLink where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebLink> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	
	public List<WebLink> list(){
		String hql=" from WebLink ";
		hql+=" order by fdSeqNo asc";
		List<WebLink> list=dbAccessor.find(hql);
		return list;
	}
	
	public int getCount(Map<String,String> map,WebLink model){
		String hql="select count(*) from WebLink where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebLink model) {
		dbAccessor.save(model);
	}
	
	public void update(WebLink model) throws IllegalArgumentException, IllegalAccessException {
		WebLink item = dbAccessor.get(WebLink.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebLink where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebLink get(String fdId) {
		WebLink item = dbAccessor.get(WebLink.class, fdId);
		return item;
	}
}

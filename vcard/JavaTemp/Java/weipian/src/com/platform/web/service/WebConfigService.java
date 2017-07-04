package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebConfig;

@Component
public class WebConfigService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebConfig model,String hql){
		return hql;
	}

	public List<WebConfig> list(Map<String,String> map,WebConfig model,Integer start,Integer limit){
		String hql=" from WebConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebConfig> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebConfig model){
		String hql="select count(*) from WebConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebConfig model) {
		dbAccessor.save(model);
	}
	
	public void update(WebConfig model) throws IllegalArgumentException, IllegalAccessException {
		WebConfig item = dbAccessor.get(WebConfig.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebConfig where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebConfig get(String fdId) {
		WebConfig item = dbAccessor.get(WebConfig.class, fdId);
		return item;
	}

	public WebConfig get() {
		String hql = "from WebConfig";
		List<WebConfig> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

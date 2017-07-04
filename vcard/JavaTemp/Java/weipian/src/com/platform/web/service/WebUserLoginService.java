package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebUserLogin;

@Component
public class WebUserLoginService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebUserLogin model,String hql){
		return hql;
	}

	public List<WebUserLogin> list(Map<String,String> map,WebUserLogin model,Integer start,Integer limit){
		String hql=" from WebUserLogin where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebUserLogin> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebUserLogin model){
		String hql="select count(*) from WebUserLogin where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebUserLogin model) {
		dbAccessor.save(model);
	}
	
	public void update(WebUserLogin model) throws IllegalArgumentException, IllegalAccessException {
		WebUserLogin item = dbAccessor.get(WebUserLogin.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebUserLogin where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebUserLogin get(String fdId) {
		WebUserLogin item = dbAccessor.get(WebUserLogin.class, fdId);
		return item;
	}
}

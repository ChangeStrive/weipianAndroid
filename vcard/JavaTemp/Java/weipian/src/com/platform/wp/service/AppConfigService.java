package com.platform.wp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppConfig;

import freemarker.template.TemplateException;

@Component
public class AppConfigService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,AppConfig model,String hql){
		return hql;
	}

	public List<AppConfig> list(Map<String,String> map,AppConfig model,Integer start,Integer limit){
		String hql=" from AppConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<AppConfig> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,AppConfig model){
		String hql="select count(*) from AppConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(AppConfig model) {
		dbAccessor.save(model);
	}
	
	public void update(AppConfig model) throws IllegalArgumentException, IllegalAccessException {
		AppConfig item = dbAccessor.get(AppConfig.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String hql = "delete from AppConfig where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public AppConfig get(String fdId) {
		AppConfig item = dbAccessor.get(AppConfig.class, fdId);
		return item;
	}
	
	public AppConfig getByKey(String key) {
		String hql=" from AppConfig where fdKey='"+key+"' ";
		List<AppConfig> list=dbAccessor.find(hql,0,1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<AppConfig> getListByKey(String key) {
		String hql=" from AppConfig where fdKey='"+key+"' ";
		List<AppConfig> list=dbAccessor.find(hql,0,1);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
}

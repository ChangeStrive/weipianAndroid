package com.platform.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinWelcomes;

@Component
public class WeiXinWelcomesService extends BaseService {
	
	public String searchWhere(Map<String,String> map,WeiXinWelcomes model,String hql){
		if(StringUtil.isNotNull(model.getFdPid())){
			hql+=" and fdPid  ='"+model.getFdPid()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WeiXinWelcomes> list(Map<String,String> map,WeiXinWelcomes model){
		String hql=" from WeiXinWelcomes where 1=1 and fdPid = '#'";
		List<WeiXinWelcomes> list=dbAccessor.find(hql);
		return list;
	}

	public int getCount(Map<String,String> map,WeiXinWelcomes model){
		String hql="select count(*) from WeiXinWelcomes where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WeiXinWelcomes model) {
		model.setFdCreateTime(DateUtils.getNow());
		dbAccessor.save(model);
	}
	
	public void update(WeiXinWelcomes model) throws IllegalArgumentException, IllegalAccessException {
		WeiXinWelcomes item = dbAccessor.get(WeiXinWelcomes.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WeiXinWelcomes where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ") OR fdPid IN('"+fdId+"')";
		dbAccessor.bulkUpdate(hql);
	}

	public WeiXinWelcomes get(String fdId) {
		WeiXinWelcomes item = dbAccessor.get(WeiXinWelcomes.class, fdId);
		return item;
	}

	public List<WeiXinWelcomes> getListById(String fdPid) {
		String hql = "from WeiXinWelcomes where fdPid = '"+fdPid+"'";
		List<WeiXinWelcomes> list = dbAccessor.find(hql);
		return list;
	}

	public void setDefault(String fdId) {
		String hql = "update WeiXinWelcomes set fdStatus = '1' where fdId in('"+fdId+"') or fdPid in('"+fdId+"')";
		String uphql = "update WeiXinWelcomes set fdStatus = '0' where fdStatus in('1')";
		dbAccessor.bulkUpdate(uphql);
		dbAccessor.bulkUpdate(hql);
	}

	public List<WeiXinWelcomes> list(Map<String, String> map, WeiXinWelcomes model, int start, int limit) {
		String hql=" from WeiXinWelcomes where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WeiXinWelcomes> list=dbAccessor.find(hql,start,limit);
		return list;
	}


	public List<WeiXinWelcomes> getListByDefault() {
		String hql = "FROM WeiXinWelcomes where fdStatus='1' ";
		List<WeiXinWelcomes> list =dbAccessor.find(hql);
		return list;
	}

	public void modifStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		String hql = "update WeiXinWelcomes set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
}

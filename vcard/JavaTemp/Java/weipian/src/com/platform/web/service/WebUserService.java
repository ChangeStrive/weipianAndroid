package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebUser;

@Component
public class WebUserService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebUser model,String hql){
		if(StringUtil.isNotNull(model.getFdLoginName())){
			hql+=" and fdLoginName like '%"+model.getFdLoginName()+"%'";
		}
		return hql;
	}

	public List<WebUser> list(Map<String,String> map,WebUser model,Integer start,Integer limit){
		String hql=" from WebUser where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebUser> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebUser model){
		String hql="select count(*) from WebUser where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebUser model) {
		WebUser item=getWebUserByLoginName(model.getFdLoginName());
		if(item==null){
			model.setFdStatus("1");
			model.setFdLoginCount(0);
			model.setFdCreateTime(DateUtils.getNow());
			dbAccessor.save(model);
		}
	}
	
	public void update(WebUser model) throws IllegalArgumentException, IllegalAccessException {
		WebUser item = dbAccessor.get(WebUser.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		WebUser item2=getWebUserByLoginName(item.getFdLoginName());
		if(item2==null||item2.getFdLoginName().equals(item.getFdLoginName())){
			dbAccessor.update(item);
		}
	}

	public void delete(String fdId) {
		String hql = "delete from WebUser where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebUser get(String fdId) {
		WebUser item = dbAccessor.get(WebUser.class, fdId);
		return item;
	}

	public void modifStatus(String fdId, String fdStatus) {
		String hql = "update WebUser set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebUser getWebUserByLoginName(String fdLoginName) {
		// TODO Auto-generated method stub
		String hql=" from WebUser where fdLoginName='"+fdLoginName+"'";
		List<WebUser> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

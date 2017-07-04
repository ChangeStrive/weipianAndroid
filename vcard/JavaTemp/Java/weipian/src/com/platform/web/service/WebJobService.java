package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebJob;

@Component
public class WebJobService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebJob model,String hql){
		if(StringUtil.isNotNull(model.getFdTypeId())){
			hql+=" and fdTypeId='"+model.getFdTypeId()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WebJob> list(Map<String,String> map,WebJob model,Integer start,Integer limit){
		String hql=" from WebJob where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebJob> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebJob model){
		String hql="select count(*) from WebJob where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebJob model) {
		model.setFdCreateTime(DateUtils.getNow());
		dbAccessor.save(model);
	}
	
	public void update(WebJob model) throws IllegalArgumentException, IllegalAccessException {
		WebJob item = dbAccessor.get(WebJob.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebJob where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebJob get(String fdId) {
		WebJob item = dbAccessor.get(WebJob.class, fdId);
		return item;
	}
}

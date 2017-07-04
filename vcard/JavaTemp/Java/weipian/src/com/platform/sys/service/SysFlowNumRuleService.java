package com.platform.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysFlowNumRule;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysFlowNumRuleService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,SysFlowNumRule model,String hql){
		if(StringUtil.isNotNull(model.getFdClassName())){
			hql+=" and fdClassName like '%"+model.getFdClassName()+"%'";
		}
		return hql;
	}

	public List<SysFlowNumRule> list(Map<String,String> map,SysFlowNumRule model,Integer start,Integer limit){
		String hql=" from SysFlowNumRule where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<SysFlowNumRule> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,SysFlowNumRule model){
		String hql="select count(*) from SysFlowNumRule where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(SysFlowNumRule model) {
		dbAccessor.save(model);
	}
	
	public void update(SysFlowNumRule model) throws IllegalArgumentException, IllegalAccessException {
		SysFlowNumRule item = dbAccessor.get(SysFlowNumRule.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from SysFlowNumRule where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public SysFlowNumRule get(String fdId) {
		SysFlowNumRule item = dbAccessor.get(SysFlowNumRule.class, fdId);
		return item;
	}
}

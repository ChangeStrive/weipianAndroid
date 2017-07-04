package com.platform.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysFlowNum;
import com.platform.sys.model.SysFlowNumRule;
import com.platform.sys.model.SysMenu;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysFlowNumService extends BaseService {
	
	public String searchWhere(Map<String,String> map,SysFlowNum model,String hql){
		return hql;
	}

	public List<SysFlowNum> list(Map<String,String> map,SysFlowNum model,Integer start,Integer limit){
		String hql=" from SysFlowNum where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<SysFlowNum> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,SysFlowNum model){
		String hql="select count(*) from SysFlowNum where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(SysFlowNum model) {
		dbAccessor.save(model);
	}
	
	public void update(SysMenu model) throws IllegalArgumentException, IllegalAccessException {
		SysMenu item = dbAccessor.get(SysMenu.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from SysFlowNum where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public SysMenu get(String fdId) {
		SysMenu item = dbAccessor.get(SysMenu.class, fdId);
		return item;
	}

	public int getMaxFlowNumByClassName(String className) {
		List list=dbAccessor.find("select max(flowNums.fdNum) from SysFlowNumRule rule inner join rule.sysFlowNums flowNums where rule.fdClassName='"+className+"'");
		if(list!=null&&list.size()>0){
			Number num=(Number) list.get(0);
			if(num==null){
				return -1;
			}
			return num.intValue();
		}
		return -1;
	}

	public SysFlowNumRule getRuleByClassName(String className) {
		List<SysFlowNumRule> list=dbAccessor.find("from SysFlowNumRule where fdClassName='"+className+"'");
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

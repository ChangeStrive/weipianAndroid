package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebExtendConfig;

@Component
public class WebExtendConfigService extends BaseService {
	
	public String searchWhere(Map<String,String> map,WebExtendConfig model,String hql){
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName like '%"+model.getFdName()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdKey())){
			hql+=" and fdKey like '%"+model.getFdKey()+"%'";
		}
		return hql;
	}

	public List<WebExtendConfig> list(Map<String,String> map,WebExtendConfig model,Integer start,Integer limit){
		String hql=" from WebExtendConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdSeqNo asc";
		List<WebExtendConfig> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public List<WebExtendConfig> list(String key){
		String hql=" from WebExtendConfig where fdKey='"+key+"' ";
		hql+=" order by fdSeqNo asc";
		List<WebExtendConfig> list=dbAccessor.find(hql);
		return list;
	}
	public int getCount(Map<String,String> map,WebExtendConfig model){
		String hql="select count(*) from WebExtendConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebExtendConfig model) {
		dbAccessor.save(model);
	}
	
	public void update(WebExtendConfig model) throws IllegalArgumentException, IllegalAccessException {
		WebExtendConfig item = dbAccessor.get(WebExtendConfig.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebExtendConfig where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebExtendConfig get(String fdId) {
		WebExtendConfig item = dbAccessor.get(WebExtendConfig.class, fdId);
		return item;
	}
}

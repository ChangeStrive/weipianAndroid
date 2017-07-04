package com.platform.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinConfig;

@Component
public class WeiXinConfigService extends BaseService {
	public String searchWhere(Map<String,String> map,WeiXinConfig model,String hql){
		return hql;
	}

	public List<WeiXinConfig> list(Map<String,String> map,WeiXinConfig model,Integer start,Integer limit){
		String hql=" from WeiXinConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WeiXinConfig> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WeiXinConfig model){
		String hql="select count(*) from WeiXinConfig where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WeiXinConfig model) {
		dbAccessor.save(model);
	}
	
	public void update(WeiXinConfig model) throws IllegalArgumentException, IllegalAccessException {
		WeiXinConfig item = dbAccessor.get(WeiXinConfig.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WeiXinConfig where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WeiXinConfig get(String fdId) {
		WeiXinConfig item = dbAccessor.get(WeiXinConfig.class, fdId);
		return item;
	}

	public WeiXinConfig get() {
		String hql = "from WeiXinConfig";
		List<WeiXinConfig> list = dbAccessor.find(hql);
		if(list.size()>0&&list!=null){
			return list.get(0);
		}
		return null;
	}
}

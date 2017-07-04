package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebCustomer;

@Component
public class WebCustomerService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebCustomer model,String hql){
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName like '%"+model.getFdName()+"%'";
		}
		return hql;
	}

	public List<WebCustomer> list(Map<String,String> map,WebCustomer model,Integer start,Integer limit){
		String hql=" from WebCustomer where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdSeqNo asc";
		List<WebCustomer> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public List<WebCustomer> list(){
		String hql=" from WebCustomer where 1=1 ";
		hql+=" order by fdSeqNo asc";
		List<WebCustomer> list=dbAccessor.find(hql);
		return list;
	}
	
	public int getCount(Map<String,String> map,WebCustomer model){
		String hql="select count(*) from WebCustomer where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebCustomer model) {
		dbAccessor.save(model);
	}
	
	public void update(WebCustomer model) throws IllegalArgumentException, IllegalAccessException {
		WebCustomer item = dbAccessor.get(WebCustomer.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebCustomer where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebCustomer get(String fdId) {
		WebCustomer item = dbAccessor.get(WebCustomer.class, fdId);
		return item;
	}
}

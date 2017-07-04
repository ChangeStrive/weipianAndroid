package com.platform.web.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebAd;

import freemarker.template.TemplateException;

@Component
public class WebAdService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebAd model,String hql){
		if(StringUtil.isNotNull(model.getFdTypeId())){
			hql+=" and fdTypeId='"+model.getFdTypeId()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WebAd> list(Map<String,String> map,WebAd model,Integer start,Integer limit){
		String hql=" from WebAd where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdSeqNo asc";
		List<WebAd> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public List<WebAd> list(String fdTypeId){
		String hql=" from WebAd where fdTypeId='"+fdTypeId+"' ";
		hql+=" order by fdSeqNo asc";
		List<WebAd> list=dbAccessor.find(hql);
		return list;
	}
	public int getCount(Map<String,String> map,WebAd model){
		String hql="select count(*) from WebAd where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebAd model) {
		dbAccessor.save(model);
	}
	
	public void update(WebAd model) throws IllegalArgumentException, IllegalAccessException {
		WebAd item = dbAccessor.get(WebAd.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String hql = "delete from WebAd where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebAd get(String fdId) {
		WebAd item = dbAccessor.get(WebAd.class, fdId);
		return item;
	}
}

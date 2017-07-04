package com.platform.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinKeywordReply;

@Component
public class WeiXinKeywordReplyService extends BaseService {
	public String searchWhere(Map<String,String> map,WeiXinKeywordReply model,String hql){
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WeiXinKeywordReply> list(Map<String,String> map,WeiXinKeywordReply model,Integer start,Integer limit){
		String hql=" from WeiXinKeywordReply where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WeiXinKeywordReply> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WeiXinKeywordReply model){
		String hql="select count(*) from WeiXinKeywordReply where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WeiXinKeywordReply model) {
		model.setFdCreateTime(DateUtils.getNow());
		dbAccessor.save(model);
	}
	
	public void update(WeiXinKeywordReply model) throws IllegalArgumentException, IllegalAccessException {
		WeiXinKeywordReply item = dbAccessor.get(WeiXinKeywordReply.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WeiXinKeywordReply where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WeiXinKeywordReply get(String fdId) {
		WeiXinKeywordReply item = dbAccessor.get(WeiXinKeywordReply.class, fdId);
		return item;
	}

	public List<WeiXinKeywordReply> getByKeyword(String eventContent) {
		String hql = "FROM WeiXinKeywordReply WHERE fdKeyword = '"+eventContent+"'";
		List<WeiXinKeywordReply> list = dbAccessor.find(hql);
		return list;
	}
}

package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebMessage;

@Component
public class WebMessageService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebMessage model,String hql){
		if(StringUtil.isNotNull(model.getFdUserName())){
			hql+=" and fdUserName like '%"+model.getFdUserName()+"%'";
		}
		return hql;
	}

	public List<WebMessage> list(Map<String,String> map,WebMessage model,Integer start,Integer limit){
		String hql=" from WebMessage where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WebMessage> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebMessage model){
		String hql="select count(*) from WebMessage where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WebMessage model) {
		model.setFdCreateTime(DateUtils.getNow());
		dbAccessor.save(model);
	}
	
	public void update(WebMessage model) throws IllegalArgumentException, IllegalAccessException {
		WebMessage item = dbAccessor.get(WebMessage.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WebMessage where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WebMessage get(String fdId) {
		WebMessage item = dbAccessor.get(WebMessage.class, fdId);
		return item;
	}

	public void modifStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		String hql="update WebMessage set fdStatus='"+fdStatus+"' where fdId='"+fdId+"' ";
		dbAccessor.bulkUpdate(hql);
	}

	public List<WebMessage> getMsgBoardList(int start, int limit) {
		String hql=" from WebMessage where fdStatus='1' ";
		List<WebMessage> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getMsgBoardListCount() {
		// TODO Auto-generated method stub
		String hql="select count(*) from WebMessage where fdStatus='1'";
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
}

package com.platform.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinMenu;

@Component
public class WeiXinMenuService extends BaseService {
	
	public String searchWhere(Map<String,String> map,WeiXinMenu model,String hql){
		if(StringUtil.isNotNull(model.getFdPid())){
			hql+=" and fdPid  ='"+model.getFdPid()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName like '%"+model.getFdName()+"%'";
		}
		return hql;
	}

	public List<WeiXinMenu> list(Map<String,String> map,WeiXinMenu model,Integer start,Integer limit){
		String hql=" from WeiXinMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+="order by fdSeq asc";
		List<WeiXinMenu> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WeiXinMenu model){
		String hql="select count(*) from WeiXinMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WeiXinMenu model) {
		dbAccessor.save(model);
	}
	
	public void update(WeiXinMenu model) throws IllegalArgumentException, IllegalAccessException {
		WeiXinMenu item = dbAccessor.get(WeiXinMenu.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		List list=dbAccessor.find("select fdId from WeiXinMenu where fdPid in(" + StringUtil.formatOfSqlParams(fdId) + ")");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String id=(String) list.get(i);
				delete(id);
			}
		}
		String hql = "delete from WeiXinMenu where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WeiXinMenu get(String fdId) {
		WeiXinMenu item = dbAccessor.get(WeiXinMenu.class, fdId);
		return item;
	}

	public List<WeiXinMenu> getListByLevel(String fdLevel) {
		String hql=" from WeiXinMenu where fdLevel='"+fdLevel+"'";
		hql+=" order by fdSeq asc";
		List<WeiXinMenu> list=dbAccessor.find(hql);
		return list;
	}

	public List<WeiXinMenu> getListByPid(String fdId) {
		String hql = "from WeiXinMenu where fdPid = '"+fdId+"'";
		List<WeiXinMenu> list = dbAccessor.find(hql);
		return list;
	}
}

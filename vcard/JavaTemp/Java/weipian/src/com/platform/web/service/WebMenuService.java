package com.platform.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebMenu;

@Component
public class WebMenuService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,WebMenu model,String hql){
		if(StringUtil.isNotNull(model.getFdPid())){
			hql+=" and fdPid  ='"+model.getFdPid()+"'";
		}else{
			hql+=" and fdPid = '#'";
		}
		
		if(StringUtil.isNotNull(model.getFdTitle())){
			hql+=" and fdTitle like '%"+model.getFdTitle()+"%'";
		}
		return hql;
	}

	public List<WebMenu> list(Map<String,String> map,WebMenu model,Integer start,Integer limit){
		String hql=" from WebMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdSeqNo asc";
		List<WebMenu> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WebMenu model){
		String hql="select count(*) from WebMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public List<WebMenu> list(String pid){
		String hql=" from WebMenu where fdPid='"+pid+"' ";
		hql+=" order by fdSeqNo asc";
		List<WebMenu> list=dbAccessor.find(hql);
		return list;
	}
	
	public List<WebMenu> list(){
		String hql=" from WebMenu order by fdSeqNo asc";
		List<WebMenu> list=dbAccessor.find(hql);
		return list;
	}
	public void save(WebMenu model) {
		dbAccessor.save(model);
	}
	
	public void update(WebMenu model) throws IllegalArgumentException, IllegalAccessException {
		WebMenu item = dbAccessor.get(WebMenu.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}


	/**
	 * 联级删除
	 * @param fdId
	 */
	public void delete(String fdId){
		List list=dbAccessor.find("select fdId from WebMenu where fdPid in(" + StringUtil.formatOfSqlParams(fdId) + ")");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String id=(String) list.get(i);
				delete(id);
			}
		}
		String hql = "delete from WebMenu where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	public WebMenu get(String fdId) {
		WebMenu item = dbAccessor.get(WebMenu.class, fdId);
		return item;
	}
}

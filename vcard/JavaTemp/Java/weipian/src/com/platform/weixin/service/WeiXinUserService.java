package com.platform.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinUser;

@Component
public class WeiXinUserService extends BaseService {
	
	public String searchWhere(Map<String,String> map,WeiXinUser model,String hql){
		if(StringUtil.isNotNull(model.getFdNickName())){
			hql+=" and fdNickName like '%"+model.getFdNickName()+"%'";
		}
		return hql;
	}

	public List<WeiXinUser> list(Map<String,String> map,WeiXinUser model,Integer start,Integer limit){
		String hql=" from WeiXinUser where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<WeiXinUser> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,WeiXinUser model){
		String hql="select count(*) from WeiXinUser where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void save(WeiXinUser model) {
		dbAccessor.save(model);
	}
	
	public void update(WeiXinUser model) throws IllegalArgumentException, IllegalAccessException {
		WeiXinUser item = dbAccessor.get(WeiXinUser.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from WeiXinUser where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public WeiXinUser get(String fdId) {
		WeiXinUser item = dbAccessor.get(WeiXinUser.class, fdId);
		return item;
	}

	/**
	 * 检查是否存在该openId
	 * @param fdOpenId 用户标识
	 * @return boolean
	 */
	public boolean getCheckByOpenId(String fdOpenId) {
		String hql = "select count(*) FROM WeiXinUser WHERE fdOpenId = '"+fdOpenId+"'";
		Number num = (Number) dbAccessor.uniqueResultByHQL(hql);
		if(num.intValue() == 0) {
			return true;
		}
		return false;
	}
	
	public WeiXinUser getUserByOpenId(String fdOpenId) {
		String hql = "FROM WeiXinUser WHERE fdOpenId = '"+fdOpenId+"'";
		return (WeiXinUser) dbAccessor.find(hql).get(0);
	}

	public void deleteFormUserOpenId(String fromUsername) {
		String hql = "delete from WeiXinUser where fdOpenId in(" + StringUtil.formatOfSqlParams(fromUsername) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
}

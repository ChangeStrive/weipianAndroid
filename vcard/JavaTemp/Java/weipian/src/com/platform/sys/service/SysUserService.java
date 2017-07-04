package com.platform.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysUser;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysUserService extends BaseService {

	public String searchWhere(Map<String,String> map,SysUser model,String hql){
		if(StringUtil.isNotNull(model.getFdLoginName())){
			hql+=" and fdLoginName like '%"+model.getFdLoginName()+"%'";
		}
		return hql;
	}

	public List<SysUser> list(Map<String,String> map,SysUser model,Integer start,Integer limit){
		String hql=" from SysUser where 1=1 and fdZvUserId is null";
		hql=searchWhere(map,model,hql);
		List<SysUser> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,SysUser model){
		String hql="select count(*) from SysUser where 1=1 and fdZvUserId is null ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}

	public void save(SysUser model) {
		model.setFdLastTime(DateUtils.getNow());
		model.setFdCreateTime(DateUtils.getNow());
		model.setFdStatus("1");
		dbAccessor.save(model);
	}

	public void update(SysUser model) throws IllegalArgumentException, IllegalAccessException {
		SysUser item = dbAccessor.get(SysUser.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	public void delete(String fdId) {
		String hql = "delete from SysUser where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public SysUser get(String fdId) {
		SysUser item = dbAccessor.get(SysUser.class, fdId);
		return item;
	}

	public SysUser getSysUserByFdLoginName(String fdLoginName) {
		String hql = "from SysUser where fdLoginName='" + fdLoginName + "'";
		List<SysUser> list = dbAccessor.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 禁用OR启用账户
	 * @param fdId
	 * @param fdStatus
	 */
	public void modifStatus(String fdId, String fdStatus) {
		String hql = "update SysUser set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public void changePassWord(String fdId, String newPassWord) {
		// TODO Auto-generated method stub
		String hql = "update SysUser set fdPwd='"+newPassWord+"' where fdId ='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
		SysUser user=dbAccessor.get(SysUser.class, fdId);
		if(user!=null&&StringUtil.isNotNull(user.getFdAppUserId())){
			hql = "update AppUser set fdPwd='"+newPassWord+"' where fdId ='"+user.getFdAppUserId()+"'";
			dbAccessor.bulkUpdate(hql);
		}
	}
}

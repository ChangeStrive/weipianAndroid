package com.platform.sys.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysMenu;
import com.platform.sys.model.SysRole;
import com.platform.sys.model.SysUser;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysRoleService extends BaseService {

	public String searchWhere(Map<String,String> map,SysRole model,String hql){
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName like '%"+model.getFdName()+"%'";
		}
		return hql;
	}

	public List<SysRole> list(Map<String,String> map,SysRole model){
		String hql=" from SysRole where 1=1 ";
		hql=searchWhere(map,model,hql);
		List<SysRole> list=dbAccessor.find(hql);
		return list;
	}


	public void save(SysRole model) {
		dbAccessor.save(model);
	}

	public void update(SysRole model) throws IllegalArgumentException, IllegalAccessException {
		SysRole item = dbAccessor.get(SysRole.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
	}

	/**
	 * 保存菜单
	 * @param fdId
	 * @param fdMenuIds
	 */
	public void saveMenu(String fdId,String fdMenuIds){
		SysRole role=dbAccessor.get(SysRole.class, fdId);
		if(StringUtil.isNotNull(fdMenuIds)){
			Set<SysMenu> sysMenus=new HashSet(0);
			String[] fdMenuId=fdMenuIds.split(",");
			for(int i=0;i<fdMenuId.length;i++){
				SysMenu menu=new SysMenu();
				menu.setFdId(fdMenuId[i]);
				sysMenus.add(menu);
			}
			role.setSysMenus(sysMenus);
		}else{
			role.setSysMenus(null);
		}
		dbAccessor.update(role);
	}
	
	/**
	 * 保存用户
	 * @param fdId
	 * @param fdUserIds
	 */
	public void saveUser(String fdId,String fdLoginNames){
		SysRole role=dbAccessor.get(SysRole.class, fdId);
		String hql=" from SysUser as user where user.fdLoginName in("+StringUtil.formatOfSqlParams(fdLoginNames)+")  ";
		hql+=" and not EXISTS(";
		hql+=" select user.fdId from SysRole as role inner join role.sysUsers users where role.fdId='"+fdId+"' and users.fdId=user.fdId";
		hql+=")";
		List<SysUser> list=dbAccessor.find(hql);
		if(StringUtil.isNotNull(fdLoginNames)){
			Set<SysUser> sysUsers=role.getSysUsers();
			sysUsers.addAll(list);
		}
		dbAccessor.update(role);
	}
	public void delete(String fdId) {
		String hql = "delete from SysRole where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public SysRole get(String fdId) {
		SysRole item = dbAccessor.get(SysRole.class, fdId);
		return item;
	}

	public List getMenuByRoleId(String fdId) {
		// TODO Auto-generated method stub
		String hql="select menu.id  from SysRole  as role inner join  role.sysMenus menu where role.fdId='"+fdId+"'";
		return dbAccessor.find(hql);
	}

	/**
	 * 
	 * @param fdId
	 * @return
	 */
	public List<SysUser> getRoleUserList(String fdId) {
		String hql="select user  from SysRole  as role inner join  role.sysUsers user where role.fdId='"+fdId+"'";
		return dbAccessor.find(hql);
	}

	/**
	 * 删除授权用户
	 * @throws IOException
	 */
	public void deleteUser(String fdId,String fdUserIds) {
		// TODO Auto-generated method stub
		SysRole role=dbAccessor.get(SysRole.class, fdId);
		Set<SysUser> users=role.getSysUsers();
		Set<SysUser> newUsers=new HashSet();
		if(users!=null&&users.size()>0){
			for(SysUser user:users){
				if(fdUserIds.indexOf(user.getFdId())==-1){
					newUsers.add(user);
				}
			}
			role.setSysUsers(newUsers);
			dbAccessor.update(role);
		}
	}
}

package com.platform.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysMenu;
import com.platform.sys.model.SysUser;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;

@Component
public class SysMenuService extends BaseService {

	public String searchWhere(Map<String,String> map,SysMenu model,String hql){
		if(StringUtil.isNotNull(model.getFdPid())){
			hql+=" and fdPid  ='"+model.getFdPid()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName  like '%"+model.getFdName()+"%'";
		}
		return hql;
	}

	public List<SysMenu> list(Map<String,String> map,SysMenu model,Integer start,Integer limit){
		String hql=" from SysMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdSeqNo asc";
		List<SysMenu> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,SysMenu model){
		String hql="select count(*) from SysMenu where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}

	public List<SysMenu> getListByLevel(String fdLevel){
		String hql=" from SysMenu where fdLevel='"+fdLevel+"'";
		hql+=" order by fdSeqNo asc";
		List<SysMenu> list=dbAccessor.find(hql);
		return list;
	}
	
	public List<SysMenu> getListByPid(String fdPid){
		String hql=" from SysMenu where fdPid='"+fdPid+"'";
		hql+=" order by fdSeqNo asc";
		List<SysMenu> list=dbAccessor.find(hql);
		return list;
	}
	public void save(SysMenu model) {
		if(!StringUtil.isNotNull(model.getFdPid())){
			model.setFdPid("#");
			model.setFdLevel(1);
		}else{
			SysMenu parent=dbAccessor.get(SysMenu.class, model.getFdPid());
			if(parent!=null){
				model.setFdPidName(parent.getFdName());
				model.setFdLevel(parent.getFdLevel()+1);
			}else{
				model.setFdLevel(1);
			}
		}
		if(!StringUtil.isNotNull(model.getFdGroup())){
			model.setFdGroup(null);
		}
		model.setFdNo(FlowNumRuleUtils.getFlowNum("SysMenu"));
		dbAccessor.save(model);
	}

	public void update(SysMenu model) throws IllegalArgumentException, IllegalAccessException {
		String newPid=model.getFdPid();
		SysMenu item = dbAccessor.get(SysMenu.class, model.getFdId());
		String oldPid=item.getFdPid();
		PropertiesUtils.copyProperties(item, model);
		if(newPid!=null&&newPid.equals(item.getFdId())){
			item.setFdPid(oldPid);
		}
		if(!StringUtil.isNotNull(model.getFdPid())){
			item.setFdPid("#");
			item.setFdLevel(1);
		}else{
			SysMenu parent=dbAccessor.get(SysMenu.class, model.getFdPid());
			if(parent!=null){
				item.setFdPidName(parent.getFdName());
				item.setFdLevel(parent.getFdLevel()+1);
			}else{
				item.setFdLevel(1);
			}
		}
		if(!StringUtil.isNotNull(model.getFdGroup())){
			item.setFdGroup(null);
		}
		dbAccessor.update(item);
		String hql="update SysMenu set fdPidName='"+item.getFdName()+"' where fdPid='"+item.getFdId()+"'";
		dbAccessor.bulkUpdate(hql);
	}

	public void delete(String fdId) {
		List list=dbAccessor.find("select fdId from SysMenu where fdPid in(" + StringUtil.formatOfSqlParams(fdId) + ")");
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				String id=(String) list.get(i);
				delete(id);
			}
		}
		String hql = "delete from SysMenu where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public SysMenu get(String fdId) {
		SysMenu item = dbAccessor.get(SysMenu.class, fdId);
		return item;
	}
	
	/**
	 * 获得1级菜单
	 * @param fdUserId
	 * @return
	 */
	public List<SysMenu> getFirstMenuByUser(String fdUserId){
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		if("1".equals(user.getFdIsAdmin())){
			String hql="from SysMenu where fdStatus='1' and fdLevel='1' order by fdSeqNo asc";
			return dbAccessor.find(hql);
		}else{
			String hql=" from SysMenu as menu where fdStatus='1' and fdLevel='1'";
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
			hql+="order by menu.fdSeqNo asc";
			return dbAccessor.find(hql);
		}
	}
	
	
	/**
	 * 获得1级菜单
	 * @param fdUserId
	 * @return
	 */
	public SysMenu getFirstMenuByUserOfFirst(String fdUserId){
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		if("1".equals(user.getFdIsAdmin())){
			String hql="from SysMenu where fdStatus='1' and fdLevel='1' order by fdSeqNo asc";
			List<SysMenu>  list=dbAccessor.find(hql,0,1);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}else{
			String hql=" from SysMenu as menu where fdStatus='1' and fdLevel='1'";
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
			hql+="order by menu.fdSeqNo asc";
			List<SysMenu>  list=dbAccessor.find(hql,0,1);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return  null;
	}
	
	public SysMenu getSysMenuByFdNo(String fdNo){
		String hql="from SysMenu where fdNo='"+fdNo+"'";
		List<SysMenu> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获得2级菜单
	 * @param fdUserId
	 * @return
	 */
	public List<SysMenu> getSecondMenuByUser(String fdUserId,String fdPid){
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		if("1".equals(user.getFdIsAdmin())){
			String hql="from SysMenu where fdStatus='1' and fdLevel='2'  and fdPid='"+fdPid+"' order by fdSeqNo asc";
			return dbAccessor.find(hql);
		}else{
			String hql=" from SysMenu as menu where fdStatus='1' and fdLevel='2' and fdPid='"+fdPid+"'";
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
			hql+="order by menu.fdSeqNo asc";
			return dbAccessor.find(hql);
		}
	}
	
	/**
	 * 获得2级菜单
	 * @param fdUserId
	 * @return
	 */
	public SysMenu getSecondMenuByUserOfFirst(String fdUserId,String fdPid){
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		if("1".equals(user.getFdIsAdmin())){
			String hql="from SysMenu where fdStatus='1' and fdLevel='2'  and fdPid='"+fdPid+"' order by fdSeqNo asc";
			List<SysMenu>  list=dbAccessor.find(hql,0,1);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}else{
			String hql=" from SysMenu as menu where fdStatus='1' and fdLevel='2' and fdPid='"+fdPid+"'";
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
			hql+="order by menu.fdSeqNo asc";
			List<SysMenu>  list=dbAccessor.find(hql,0,1);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 获得3级菜单
	 * @param fdUserId
	 * @return
	 */
	public List<SysMenu> getTreeMenuByUser(String fdUserId,String fdPid){
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		if("1".equals(user.getFdIsAdmin())){
			String hql="from SysMenu where fdStatus='1' and fdLevel='3'  and fdPid='"+fdPid+"' order by fdSeqNo asc";
			return dbAccessor.find(hql);
		}else{
			String hql=" from SysMenu as menu where fdStatus='1' and fdLevel='3'  and fdPid='"+fdPid+"' ";
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
			hql+="order by menu.fdSeqNo asc";
			return dbAccessor.find(hql);
		}
	}

	public SysMenu getSysMenuByFdNoAndFdUserId(String fdNo, String fdUserId) {
		// TODO Auto-generated method stub
		SysUser user=dbAccessor.get(SysUser.class, fdUserId);
		String hql=" from SysMenu as menu where fdNo='"+fdNo+"'";
		if(!"1".equals(user.getFdIsAdmin())){
			hql+=" and EXISTS(";
			hql+=" select role.id from SysRole as role inner join role.sysMenus menus inner join role.sysUsers users where menus.fdId=menu.fdId and users.fdId='"+fdUserId+"'";
			hql+=")";
		}
		List<SysMenu> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

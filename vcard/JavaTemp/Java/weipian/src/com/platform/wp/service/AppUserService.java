package com.platform.wp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysRole;
import com.platform.sys.model.SysUser;
import com.platform.util.DateUtils;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserDetail;
import com.platform.wp.util.WpUtil;

import freemarker.template.TemplateException;

@Component
public class AppUserService extends BaseService {
	
	public String searchWhere(Map<String,String> map,AppUser model,String hql){
		if(StringUtil.isNotNull(model.getFdCode())){
			hql+=" and fdCode like '%"+model.getFdCode()+"%'";
		}
		
		if(StringUtil.isNotNull(model.getFdName())){
			hql+=" and fdName like '%"+model.getFdName()+"%'";
		}
		
		
		if(StringUtil.isNotNull(model.getFdShopType())){
			hql+=" and fdShopType ='"+model.getFdShopType()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdStatus())){
			hql+=" and fdStatus ='"+model.getFdStatus()+"'";
		}
		
		return hql;
	}
	
	public List<AppUser> list(Map<String,String> map,AppUser model,Integer start,Integer limit){
		String hql=" from AppUser a where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by a.fdCreateTime asc";
		List<AppUser> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,AppUser model){
		String hql="select count(*) from AppUser a where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	public void saveSysUser(AppUser model){
		SysUser user=new SysUser();
		user.setFdAppUserId(model.getFdId());
		user.setFdAppUserType(model.getFdShopType());
		user.setFdPwd(model.getFdPwd());
		user.setFdLoginName(model.getFdCode());
		user.setFdName(model.getFdName());
		user.setFdCreateTime(DateUtils.getNow());
		user.setFdStatus(model.getFdStatus());
		dbAccessor.save(user);
		model.setFdSysUserId(user.getFdId());
		String fdRoleId="";
		//0待审核  1普通分销商   2城市合伙人 3区域合伙人
		if(model.getFdShopType().equals("0")){//待审核
			fdRoleId="402881365aff5011015aff55e95f0003";
		}else if(model.getFdShopType().equals("1")){//普通分销商 
			fdRoleId="402881365aff5011015aff5556230002";
		}else if(model.getFdShopType().equals("2")){//城市合伙人
			fdRoleId="402881365aff5011015aff52b7690000";
		}else if(model.getFdShopType().equals("3")){//区域合伙人
			fdRoleId="402881365aff5011015aff52d3530001";
		}
		SysRole role=dbAccessor.get(SysRole.class, fdRoleId);
		Set<SysUser> sysUsers=role.getSysUsers();
		sysUsers.add(user);
		dbAccessor.update(role);
	}
	
	public void updateSysUser(AppUser item){
		String hql="update SysUser set fdLoginName='"+item.getFdCode()+"' ";
		hql+=",fdName='"+item.getFdName()+"'";
		hql+=",fdAppUserType='"+item.getFdShopType()+"'";
		hql+=",fdPwd='"+item.getFdPwd()+"'";
		hql+=",fdStatus='"+item.getFdStatus()+"'";
		hql+=" where fdAppUserId='"+item.getFdId()+"'";
		dbAccessor.bulkUpdate(hql);
		
		SysUser user=dbAccessor.get(SysUser.class,item.getFdSysUserId());
		String fdRoleId="";
		if(item.getFdShopType().equals("0")){//待审核
			fdRoleId="402881365aff5011015aff55e95f0003";
		}else if(item.getFdShopType().equals("1")){//普通分销商 
			fdRoleId="402881365aff5011015aff5556230002";
		}else if(item.getFdShopType().equals("2")){//城市合伙人
			fdRoleId="402881365aff5011015aff52b7690000";
		}else if(item.getFdShopType().equals("3")){//区域合伙人
			fdRoleId="402881365aff5011015aff52d3530001";
		}
		SysRole role=dbAccessor.get(SysRole.class, fdRoleId);
		Set<SysUser> sysUsers=role.getSysUsers();
		sysUsers.add(user);
		dbAccessor.update(role);
	}
	
	public JSONObject save(AppUser model) {
		JSONObject result=new JSONObject();
		SysUser u=WpUtil.getSysUserByCode(model.getFdCode());
		if(u!=null){
			result.put("success", false);
			result.put("msg", "账号已经存在!");
			return result;
		}
		model.setFdPicUrl("http://www.zuimeivip.com/app2/images/userheader.gif");
		model.setFdStatus("1");
		model.setFdPwd("123456");
		model.setFdFirstCount(0);
		model.setFdSecondCount(0);
		model.setFdThreeCount(0);
		model.setFdBuyCount(0);
		model.setFdAmount(new BigDecimal(0));
		dbAccessor.save(model);
		saveSysUser(model);
		
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	
	/**
	 * 删除授权用户
	 * @throws IOException
	 */
	public void deleteRoleUser(AppUser item) {
		// TODO Auto-generated method stub
		String fdRoleId="";
		if(item.getFdShopType().equals("0")){//待审核
			fdRoleId="402881365aff5011015aff55e95f0003";
		}else if(item.getFdShopType().equals("1")){//普通分销商 
			fdRoleId="402881365aff5011015aff5556230002";
		}else if(item.getFdShopType().equals("2")){//城市合伙人
			fdRoleId="402881365aff5011015aff52b7690000";
		}else if(item.getFdShopType().equals("3")){//区域合伙人
			fdRoleId="402881365aff5011015aff52d3530001";
		}
		String sql="delete from sys_role_user where roleId='"+fdRoleId+"' and userId='"+item.getFdSysUserId()+"'";
		dbAccessor.executeSQL(sql);
	}
	
	public JSONObject update(AppUser model) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException {
		JSONObject result=new JSONObject();
		AppUser item = dbAccessor.get(AppUser.class, model.getFdId());
		deleteRoleUser(item);
		if(!item.getFdCode().equals(model.getFdCode())){
			SysUser u=WpUtil.getSysUserByCode(model.getFdCode());
			if(u!=null){
				result.put("success", false);
				result.put("msg", "账号已经存在!");
				return result;
			}
			String hql="update AppRecord set fdUserCode='"+model.getFdCode()+"',fdUserName='"+model.getFdName()+"' where fdUserId='"+model.getFdId()+"'";
			dbAccessor.bulkUpdate(hql);
			
			hql="update AppGoodsOrder set fdUserCode='"+model.getFdCode()+"',fdUserName='"+model.getFdName()+"' where fdUserId='"+model.getFdId()+"'";
			dbAccessor.bulkUpdate(hql);
			
			hql="update AppGoodsOrder set fdShopNo='"+model.getFdCode()+"',fdUserName='"+model.getFdName()+"' where fdShopId='"+model.getFdId()+"'";
			dbAccessor.bulkUpdate(hql);
			
			hql="update AppUserShop set fdUserCode='"+model.getFdCode()+"',fdUserName='"+model.getFdName()+"' where fdUserId='"+model.getFdId()+"'";
			dbAccessor.bulkUpdate(hql);
			
			String sql="update app_user set fdUpUser=replace(fdUpUser,'"+item.getFdCode()+"','"+model.getFdCode()+"') where fdUpUser like '%"+item.getFdCode()+"%'";
			dbAccessor.executeSQL(sql);
		}
		PropertiesUtils.copyProperties(item, model);
		updateSysUser(item);
		
		
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	
	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String[] fdIdArr = fdId.split(",");
		for (int i = 0; i < fdIdArr.length; i++) {
			AppUser item = dbAccessor.get(AppUser.class, fdIdArr[i]);
			deleteRoleUser(item);
		}
		
		String hql = "delete from SysUser where fdAppUserId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
		
		hql = "delete from AppUser where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
		
		hql = "delete from AppUserDetail where fdUserId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
		
		hql = "delete from AppUserShop where fdUserId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	public void modifStatus(String fdId, String fdStatus) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		String hql = "update AppUser set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	public AppUser get(String fdId) {
		AppUser item = dbAccessor.get(AppUser.class, fdId);
		return item;
	}
	
	public AppUser getAppUserAndDetail(String fdId) {
		AppUser item = dbAccessor.get(AppUser.class, fdId);
		if(item.getAppUserDetail()!=null){
			dbAccessor.initialize(item.getAppUserDetail());
		}
		return item;
	}

	public String getValues(JSONObject message,String name){
		JSONArray array=message.getJSONArray(name);
		String s="";
		for(int i=0;i<array.size();i++){
			s+=array.getString(i);
			if(i<array.size()-1){
				s+=",";
			}
		}
		return s;
	}
	/**
	 * 用户信息
	 * @param fdUserId
	 * @param fdUserMessage
	 */
	public void saveUserMessage(String fdUserId, String fdUserMessage) {
		// TODO Auto-generated method stub
		AppUser item = dbAccessor.get(AppUser.class, fdUserId);
		if(item!=null){
			item.setFdUserMessage(fdUserMessage);
			dbAccessor.update(item);
			String hql="delete from AppUserDetail where fdUserId='"+fdUserId+"'";
			dbAccessor.bulkUpdate(hql);
			JSONObject message=JSONObject.fromObject(fdUserMessage);
			AppUserDetail m=new AppUserDetail();
			m.setFdUserId(fdUserId);
			m.setFdJobs(getValues(message,"fdJobs"));
			m.setFdCompany(getValues(message,"fdCompany"));
			m.setFdCompanyAddress(getValues(message,"fdCompanyAddress"));
			m.setFdCompanyType(getValues(message,"fdCompanyType"));
			m.setFdCompanyUrl(getValues(message,"fdCompanyUrl"));
			m.setFdEducation(getValues(message,"fdEducation"));
			m.setFdEmail(getValues(message,"fdEmail"));
			m.setFdFax(getValues(message,"fdFax"));
			m.setFdMajor(getValues(message,"fdMajor"));
			m.setFdMobile(getValues(message,"fdMobile"));
			m.setFdRemark(getValues(message,"fdRemark"));
			m.setFdSchool(getValues(message,"fdSchool"));
			m.setFdTel(getValues(message,"fdTel"));
			m.setFdWxNo(getValues(message,"fdWxNo"));
			dbAccessor.save(m);
		}
	}

	public void changePassword(String fdId, String fdPwd) {
		// TODO Auto-generated method stub
		String hql="update AppUser set fdPwd='"+fdPwd+"' where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
		
	    hql=" update SysUser  ";
		hql+=" set fdPwd='"+fdPwd+"'";
		hql+=" where fdAppUserId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
	}
	
}

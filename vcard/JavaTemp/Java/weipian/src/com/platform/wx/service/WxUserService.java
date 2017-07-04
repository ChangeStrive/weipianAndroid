package com.platform.wx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.sys.model.SysUser;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUser;
import com.platform.wp.util.WpUtil;

/**
 * 用户
 * @author Administrator
 *
 */
@Component
public class WxUserService extends BaseService {

	public AppUser getUserByCode(String fdCode){
		String hql=" from AppUser where fdCode='"+fdCode+"'";
		List<AppUser> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			AppUser item=list.get(0);
			return item;
		}
		return null;
	}
	
	/**
	 * 注册
	 * @param fdMobile
	 * @param fdPwd
	 * @return
	 */
	public JSONObject reg(HttpServletRequest request,String fdShopNo,String fdCode, String fdPwd) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		SysUser u=WpUtil.getSysUserByCode(fdCode);
		if(u!=null){
			result.put("status", -1);
			result.put("msg", "该手机号已经注册过了!");
			return result;
		}
		AppUser shop=WpUtil.getUserByCode(fdShopNo);
		if(shop.getFdShopType().equals("0")){
			result.put("status", -1);
			result.put("msg",shop.getFdName()+"分销商资格还没激活!");
			return result;
		}
		AppUser user=new AppUser();
		user.setFdCode(fdCode);
		user.setFdPwd(fdPwd);
		String fdShopPicUrl=StringUtil.getCurrentUrl(request, "app/images/userheader.gif");
		user.setFdPicUrl(fdShopPicUrl);
		user.setFdFirstCount(0);
		user.setFdSecondCount(0);
		user.setFdThreeCount(0);
		user.setFdStatus("1");
		user.setFdBuyCount(0);
		user.setFdShopType("0");
		String fdUpUser=WpUtil.getUpUser(shop.getFdId());
		user.setFdUpUser(fdUpUser);
		dbAccessor.save(user);
		WpUtil.regSysUser(user);
		result.put("status", 0);
		result.put("msg", "注册成功!");
		return result;
	}

	public JSONObject resetPwd(String fdMobile, String fdPwd) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppUser user=getUserByCode(fdMobile);
		if(user==null){
			result.put("status", -1);
			result.put("msg", "该手机号未注册过!");
			return result;
		}
		user.setFdPwd(fdPwd);
		dbAccessor.update(user);
		result.put("status", 0);
		result.put("msg", "修改成功!");
		return result;
	}

	public void saveOpenId(String fdId, String fdOpenId) {
		// TODO Auto-generated method stub
		String hql="update AppUser set fdWeixinid='"+fdOpenId+"' where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);
	}

	public AppUser getAppUserByOpenId(String fdWeixinid) {
		// TODO Auto-generated method stub
		String hql=" from AppUser where fdWeixinid='"+fdWeixinid+"' ";
		List<AppUser> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public void saveWeiXinMessage(String fdId, String nickname,
			String headimgurl, String fdOpenId, String fdSex) {
		String hql="update AppUser set fdSex='"+fdSex+"',fdWeixinid='"+fdOpenId+"',fdName='"+nickname+"',fdPicUrl='"+headimgurl+"' where fdId='"+fdId+"'";
		dbAccessor.bulkUpdate(hql);// TODO Auto-generated method stub
		
	}

	public AppUser getAppUserByFdId(String fdId) {
		// TODO Auto-generated method stub
		AppUser user=dbAccessor.get(AppUser.class, fdId);
		return user;
	}

	/**
	 * 获得上级商家id
	 * @param fdShopId
	 * @return
	 */
	public String getUpUser(String fdShopId) {
		// TODO Auto-generated method stub
		AppUser user=dbAccessor.get(AppUser.class, fdShopId);
		String fdUpUser="1."+user.getFdCode();
		String fdUpUser2="";
		if(StringUtil.isNotNull(user.getFdUpUser())){
			String[] userIds=user.getFdUpUser().split(";");
			for(int i=0;i<userIds.length;i++){
				if(i<2){
					String[] id=userIds[i].split(".");
					fdUpUser2+=";"+(i+2)+"."+id[1];
				}
			}
		}
		return fdUpUser+fdUpUser2;
	}

	/**
	 * 添加基础信息
	 * @param fdId
	 * @param map
	 * @return
	 */
	public JSONObject addBaseInfo(String fdUserId, Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppUser user=dbAccessor.get(AppUser.class, fdUserId);
		user.setFdName(map.get("fdName"));
		user.setFdSex(map.get("fdSex"));
		user.setFdBirthday(map.get("fdBirthday"));
		user.setFdProvince(map.get("fdProvince"));
		user.setFdCity(map.get("fdCity"));
		user.setFdArea(map.get("fdArea"));
		dbAccessor.update(user);
		result.put("status", 0);
		result.put("msg", "修改成功!");
		return result;
	}
}

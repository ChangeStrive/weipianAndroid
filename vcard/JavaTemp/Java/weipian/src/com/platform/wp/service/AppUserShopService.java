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
import com.platform.wp.model.AppUserShop;
import com.platform.wp.util.WpUtil;

import freemarker.template.TemplateException;

@Component
public class AppUserShopService extends BaseService {
	
	public String searchWhere(Map<String,String> map,AppUserShop model,String hql){
		if(StringUtil.isNotNull(model.getFdUserCode())){
			hql+=" and fdUserCode like '%"+model.getFdUserCode()+"%'";
		}
		
		if(StringUtil.isNotNull(model.getFdUserName())){
			hql+=" and fdUserName like '%"+model.getFdUserName()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdStoreName())){
			hql+=" and fdStoreName like '%"+model.getFdStoreName()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdStoreAddress())){
			hql+=" and fdStoreAddress like '%"+model.getFdStoreAddress()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdStoreBrand())){
			hql+=" and fdStoreBrand like '%"+model.getFdStoreBrand()+"%'";
		}
		if(StringUtil.isNotNull(model.getFdStoreType())){
			hql+=" and fdStoreType ='"+model.getFdStoreType()+"'";
		}
		
		if(StringUtil.isNotNull(model.getFdStatus())){
			hql+=" and fdStatus ='"+model.getFdStatus()+"'";
		}
		
		return hql;
	}
	
	public List<AppUserShop> list(Map<String,String> map,AppUserShop model,Integer start,Integer limit){
		String hql=" from AppUserShop a where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by a.fdCreateTime asc";
		List<AppUserShop> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,AppUserShop model){
		String hql="select count(*) from AppUserShop a where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	
	public JSONObject save(AppUserShop model) {
		JSONObject result=new JSONObject();
		dbAccessor.save(model);
		
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	
	
	public JSONObject update(AppUserShop model) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException {
		JSONObject result=new JSONObject();
		AppUserShop item = dbAccessor.get(AppUserShop.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	
	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		
		String hql = "delete from AppUserShopShop where fdUserId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	public void modifStatus(String fdId, String fdStatus) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		String hql = "update AppUserShop set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	public AppUserShop get(String fdId) {
		AppUserShop item = dbAccessor.get(AppUserShop.class, fdId);
		return item;
	}
	
	
}

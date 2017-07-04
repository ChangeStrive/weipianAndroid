package com.platform.wp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.PropertiesUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoodsType;

import freemarker.template.TemplateException;

@Component
public class AppGoodsTypeService extends BaseService {
	
	
	public String searchWhere(Map<String,String> map,AppGoodsType model,String hql){
		if(StringUtil.isNotNull(model.getFdPid())){
			hql+=" and fdPid  ='"+model.getFdPid()+"'";
		}
		
		return hql;
	}

	public List<AppGoodsType> list(Map<String,String> map,AppGoodsType model,Integer start,Integer limit){
		String hql=" from AppGoodsType where 1=1 ";
		hql=searchWhere(map,model,hql);
		hql+=" order by fdCreateTime asc";
		List<AppGoodsType> list=dbAccessor.find(hql,start,limit);
		return list;
	}

	public int getCount(Map<String,String> map,AppGoodsType model){
		String hql="select count(*) from AppGoodsType where 1=1 ";
		hql=searchWhere(map,model,hql);
		Number num=(Number) dbAccessor.uniqueResultByHQL(hql);
		return num.intValue();
	}
	
	
	public JSONObject update(AppGoodsType model) throws IllegalArgumentException, IllegalAccessException {
		JSONObject result=new JSONObject();
		AppGoodsType item = dbAccessor.get(AppGoodsType.class, model.getFdId());
		PropertiesUtils.copyProperties(item, model);
		dbAccessor.update(item);
		
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
	}
	public void delete(HttpServletRequest request,String fdId) throws IOException, TemplateException {
		String hql = "delete from AppGoodsType where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}

	public AppGoodsType get(String fdId) {
		AppGoodsType item = dbAccessor.get(AppGoodsType.class, fdId);
		return item;
	}
	
	public AppGoodsType getAppGoodsTypeByUserId(String fdUserId) {
		String hql=" from AppGoodsType where fdUserId='"+fdUserId+"' ";
		List<AppGoodsType> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	
	public JSONObject  save(AppGoodsType model) {
		JSONObject result=new JSONObject();
		dbAccessor.save(model);
		result.put("success", true);
		result.put("msg", "保存成功!");
		return result;
		
	}
	
	
	public void modifStatus(String fdId, String fdStatus) {
		// TODO Auto-generated method stub
		String hql = "update AppGoodsType set fdStatus='"+fdStatus+"' where fdId in(" + StringUtil.formatOfSqlParams(fdId) + ")";
		dbAccessor.bulkUpdate(hql);
	}
	
	
	public List<AppGoodsType> getListByLevel(String fdLevel) {
		String hql=" from AppGoodsType where fdLevel='"+fdLevel+"'";
		hql+=" order by fdSeqNo asc";
		List<AppGoodsType> list=dbAccessor.find(hql);
		return list;
	}
	
}

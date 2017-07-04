package com.platform.wx.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.wp.model.AppGoodsType;
import com.platform.wp.util.WpUtil;

/**
 * 订单支付验证
 * @author Administrator
 *
 */
@Component
public class WxGoodsTypeService extends BaseService {

	private static Logger logger=LoggerFactory.getLogger(WxGoodsTypeService.class); 
	
	/**
	 * 广告列表
	 * @param fdTypeId 广告类型
	 * @return
	 */
	public JSONObject typelist(String fdTypeId,Map<String,String> map,int start, int limit) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppGoodsType where fdPid='"+fdTypeId+"' and fdLevel='1' order by fdSeqNo asc";
		List<AppGoodsType> list=dbAccessor.find(hql,start,limit);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsType type=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", type.getFdId());
				o.put("fdTypeName", type.getFdTypeName());
				array.add(o);
			}
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("list", array);
		return result;
	}
	/**
	 * 广告列表
	 * @param fdTypeId 广告类型
	 * @return
	 */
	public JSONObject typelist(String fdTypeId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppGoodsType where fdPid='"+fdTypeId+"' and fdLevel='1' order by fdSeqNo asc";
		List<AppGoodsType> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsType type=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", type.getFdId());
				o.put("fdTypeName", type.getFdTypeName());
				array.add(o);
			}
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("list", array);
		return result;
	}
}

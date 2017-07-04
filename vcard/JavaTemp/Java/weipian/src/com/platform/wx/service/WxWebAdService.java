package com.platform.wx.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.web.model.WebAd;
import com.platform.wp.util.WpUtil;

/**
 * 广告
 * @author Administrator
 *
 */
@Component
public class WxWebAdService extends BaseService {

	private static Logger logger=LoggerFactory.getLogger(WxWebAdService.class); 
	
	/**
	 * 广告列表
	 * @param fdTypeId 广告类型
	 * @return
	 */
	public JSONObject adlist(String fdTypeId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from WebAd where fdTypeId='"+fdTypeId+"'  order by fdSeqNo asc";
		List<WebAd> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebAd ad=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", ad.getFdId());
				o.put("fdTitle", ad.getFdTitle());
				o.put("fdPicUrl", ad.getFdPicUrl());
				o.put("fdUrl", ad.getFdUrl());
				array.add(o);
			}
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("list", array);
		return result;
	}
	
}

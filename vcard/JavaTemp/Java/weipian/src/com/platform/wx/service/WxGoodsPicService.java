package com.platform.wx.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.wp.model.AppGoodsPic;
import com.platform.wp.util.WpUtil;

/**
 * 商品多图
 * @author Administrator
 *
 */
@Component
public class WxGoodsPicService extends BaseService {

	private static Logger logger=LoggerFactory.getLogger(WxGoodsPicService.class); 
	
	/**
	 * 广告列表
	 * @param fdTypeId 广告类型
	 * @return
	 */
	public JSONObject goodsPicList(String fdGoodsId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppGoodsPic where appGoods.fdId='"+fdGoodsId+"'  order by fdSeqNo asc";
		List<AppGoodsPic> list=dbAccessor.find(hql);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsPic ad=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", ad.getFdId());
				o.put("fdPicUrl", ad.getFdPicUrl());
				array.add(o);
			}
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("list", array);
		return result;
	}
	
}

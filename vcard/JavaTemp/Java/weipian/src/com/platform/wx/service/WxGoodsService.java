package com.platform.wx.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoods;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserAddress;
import com.platform.wp.util.WpUtil;

/**
 * 商品列表
 * @author Administrator
 *
 */
@Component
public class WxGoodsService extends BaseService {

	private static Logger logger=LoggerFactory.getLogger(WxGoodsService.class); 
	
	/**
	 * 商品列表
	 * @param fdTypeId 广告类型
	 * @return
	 */
	public JSONObject goodslist(Map<String,String> map,int start, int limit) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		String hql=" from AppGoods where fdStatus='1' ";
		String fdTypeId=map.get("fdTypeId");
		if(StringUtil.isNotNull(fdTypeId)){
			hql+=" and fdTypeId='"+fdTypeId+"'";
		}
		hql+=" order by fdUpdateTime desc";
		List<AppGoods> goodsList=dbAccessor.find(hql,start,limit);
		if(goodsList!=null&&goodsList.size()>0){
			for(int i=0;i<goodsList.size();i++){
				AppGoods goods=goodsList.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", goods.getFdId());
				o.put("fdGoodsName", goods.getFdGoodsName());
				o.put("fdGoodsNo", goods.getFdGoodsNo());
				o.put("fdPrice", goods.getFdPrice());
				o.put("fdTagPrice", goods.getFdTagPrice());
				o.put("fdPicUrl", goods.getFdPicUrl());
				o.put("fdQuantity", goods.getFdQuantity());
				array.add(o);
			}
		}
		result.put("status",WpUtil.RESULT_STATUS_YES);
		result.put("list", array);
		return result;
	}

	public AppGoods getGoods(String fdGoodsId) {
		AppGoods goods=dbAccessor.get(AppGoods.class, fdGoodsId);
		return goods;
	}

	public JSONObject getGoodsDetail(String fdUserId, String fdGoodsId,
			int fdQuantity) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoods goods=dbAccessor.get(AppGoods.class, fdGoodsId);
		if(goods==null||goods.getFdStatus().equals("0")){
			result.put("status", -1);
			result.put("msg","商品不存在或已下架");
			return result;
		}
		int remainCount=goods.getFdQuantity();
		if(fdQuantity>remainCount){
			result.put("status", -1);
			result.put("msg", "商品库存不足，商品库存为："+remainCount+"件");
			return result;
		}
		
		BigDecimal fdGoodsAmount=new BigDecimal(0);
		fdGoodsAmount=goods.getFdPrice().multiply(new BigDecimal(fdQuantity));
		
		result.put("fdGoodsId",goods.getFdId());
		result.put("fdGoodsNo",goods.getFdGoodsNo());
		result.put("fdGoodsName", goods.getFdGoodsName());
		result.put("fdPicUrl", goods.getFdPicUrl());
		result.put("fdQuantity",fdQuantity);
		result.put("fdTagPrice",goods.getFdTagPrice());
		result.put("fdPrice",goods.getFdPrice());
		
		result.put("fdGoodsAmount",fdGoodsAmount);
		result.put("fdAmount",fdGoodsAmount);
		result.put("fdCoupon",new BigDecimal(0));
		result.put("status",0);
		return result;
	}
	
	public JSONObject buynow(String fdUserId, String fdAddressId,
			String fdGoodsId, int fdQuantity, String fdRemark,String fdShopNo) {
		JSONObject result=new JSONObject();
		if(!StringUtil.isNotNull(fdGoodsId)){
			result.put("status", -1);
			result.put("msg","请选择商品结算");
			return result;
		}
		if(!StringUtil.isNotNull(fdAddressId)){
			result.put("status", -1);
			result.put("msg","请选择收货地址");
			return result;
		}
		JSONObject orderDetail=getGoodsDetail(fdUserId,fdGoodsId,fdQuantity);
		if(orderDetail.getInt("status")!=0){
			result.put("status", -1);
			result.put("msg",orderDetail.getString(orderDetail.getString("msg")));
			return result;
		}
		AppUserAddress address=dbAccessor.get(AppUserAddress.class, fdAddressId);
		if(address==null){
			result.put("status", -1);
			result.put("msg","请选择收货地址");
			return result;
		}
	    AppGoodsOrder order =new AppGoodsOrder();
		order.setFdAddress(address.getFdProvince()+address.getFdCity()+address.getFdArea()+address.getFdAddress());
		order.setFdZipCode(address.getFdZipCode());
		order.setFdConsignee(address.getFdConsignee());
		order.setFdTel(address.getFdTel());
		
		
		AppUser user=dbAccessor.get(AppUser.class, fdUserId);
		order.setFdUserId(user.getFdId());
		order.setFdUserName(user.getFdName());
		order.setFdUserCode(user.getFdCode());
		
		order.setFdQuantity(orderDetail.getInt("fdQuantity"));
		order.setFdGoodsAmount(new BigDecimal(orderDetail.getString("fdGoodsAmount")));
		order.setFdCoupon(new BigDecimal(orderDetail.getString("fdCoupon")));
		order.setFdAmount(new BigDecimal(orderDetail.getString("fdAmount")));
		
		order.setFdOrderNo(FlowNumRuleUtils.getFlowNum("AppGoodsOrder"));
		order.setFdCreateTime(DateUtils.getNow());
		order.setFdStatus(WpUtil.ORDER_STATUS_PAY_NO);
		order.setFdRemark(fdRemark);
		order.setFdOpenId(user.getFdWeixinid());

		AppUser shop=WpUtil.getUserByCode(fdShopNo);
		order.setFdShopId(shop.getFdId());
		order.setFdShopName(shop.getFdName());
		order.setFdShopNo(shop.getFdCode());
		order.setFdShopPicUrl(shop.getFdPicUrl());
		
		dbAccessor.save(order);
		
		JSONObject rlist=new JSONObject();
		JSONArray array=new JSONArray();
			
			
		    AppGoodsOrderItem orderItem=new AppGoodsOrderItem();
			orderItem.setFdPicUrl(orderDetail.getString("fdPicUrl"));
			orderItem.setFdGoodsId(orderDetail.getString("fdGoodsId"));
			orderItem.setFdGoodsNo(orderDetail.getString("fdGoodsNo"));
			orderItem.setFdGoodsName(orderDetail.getString("fdGoodsName"));
			orderItem.setFdQuantity(orderDetail.getInt("fdQuantity"));
			orderItem.setFdPrice((BigDecimal)orderDetail.get("fdPrice"));
			orderItem.setFdTagPrice((BigDecimal)orderDetail.get("fdTagPrice"));
			orderItem.setFdAmount(orderItem.getFdPrice().multiply(new BigDecimal(orderItem.getFdQuantity())));
			orderItem.setFdSeqNo(0);
			orderItem.setAppGoodsOrder(order);
			dbAccessor.save(orderItem);
			
			JSONObject o=new JSONObject();
			o.put("fdId", orderItem.getFdId());
			o.put("fdGoodsId", orderItem.getFdGoodsId());
			o.put("fdGoodsNo", orderItem.getFdGoodsNo());
			o.put("fdGoodsName", orderItem.getFdGoodsName());
			o.put("fdPicUrl", orderItem.getFdPicUrl());
			o.put("fdQuantity", orderItem.getFdQuantity());
			o.put("fdTagPrice", orderItem.getFdTagPrice());
			o.put("fdPrice", orderItem.getFdPrice());
			o.put("fdAmount", orderItem.getFdAmount());
			o.put("fdSeqNo", orderItem.getFdSeqNo());
			array.add(o);
			
		rlist.put("totalSize", 1);
		rlist.put("list", array);
		order.setFdOrderDetail(rlist.toString());
		
		String fdGoodsTitle=orderItem.getFdGoodsName();
		if(fdGoodsTitle.length()<32){
			order.setFdOrderGoodsName(fdGoodsTitle);
		}else{
			order.setFdOrderGoodsName(fdGoodsTitle.substring(0,30)+"..");
		}
		
		result.put("fdOrderId",order.getFdId());
		result.put("status",0);
		result.put("msg","操作成功");
		return result;
	}
	
}

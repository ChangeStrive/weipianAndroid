package com.platform.wx.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.platform.base.BaseService;
import com.platform.util.DateUtils;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoods;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderItem;
import com.platform.wp.model.AppShopCart;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserAddress;
import com.platform.wp.util.WpUtil;

@Component
public class WxShopCartService extends BaseService {

	/**
	 * 加入购物车
	 * @param fdUserId
	 * @param fdShopStoreId
	 * @param fdColorNo
	 * @param fdSizeNo
	 * @param fdQuantity
	 * @return
	 */
	public JSONObject addShopCart(String fdUserId, String fdGoodsId, Integer fdQuantity) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppGoods goods=dbAccessor.get(AppGoods.class, fdGoodsId);
		if(goods==null){
			result.put("status",-1);
			result.put("msg", "商品不存在");
			return result;
		}
		
		if(!goods.getFdStatus().equals("1")){
			result.put("status",-1);
			result.put("msg", "商品已下架");
			return result;
		}
		
		if(goods.getFdQuantity()<fdQuantity){
			result.put("status",-1);
			result.put("msg", "商品数量不足,商品数量为"+goods.getFdQuantity());
			return result;
		}
		
		String hql=" from AppShopCart where fdGoodsId='"+fdGoodsId+"'  and fdUserId='"+fdUserId+"'";
		List<AppShopCart> cartList=dbAccessor.find(hql);
		if(cartList!=null&&cartList.size()>0){
			AppShopCart cart=cartList.get(0);
			cart.setFdQuantity(cart.getFdQuantity()+fdQuantity);
			cart.setFdCreateTime(DateUtils.getNow());
			dbAccessor.update(cart);
		}else{
			AppShopCart cart=new AppShopCart();
			cart.setFdCreateTime(DateUtils.getNow());
			cart.setFdGoodsName(goods.getFdGoodsName());
			cart.setFdPicUrl(goods.getFdPicUrl());
			cart.setFdQuantity(fdQuantity);
			cart.setFdGoodsNo(goods.getFdGoodsNo());
			cart.setFdTagPrice(goods.getFdTagPrice());
			cart.setFdPrice(goods.getFdPrice());
			cart.setFdUserId(fdUserId);
			cart.setFdGoodsId(fdGoodsId);
			dbAccessor.save(cart);
		}
		result.put("status", 0);
		result.put("msg", "加入购物车成功");
		return result;
	}

	public JSONObject shopCartList(String fdUserId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql=" from AppShopCart where  fdUserId='"+fdUserId+"'";
		hql+=" order by fdCreateTime desc";
		List<AppShopCart> list=dbAccessor.find(hql);
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppShopCart item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdShopCartId", item.getFdId());
				o.put("fdGoodsName", item.getFdGoodsName());
				o.put("fdQuantity", item.getFdQuantity());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdGoodsId", item.getFdGoodsId());
				o.put("fdGoodsNo", item.getFdGoodsNo());
				o.put("fdPrice", item.getFdPrice());
				o.put("fdTagPrice", item.getFdTagPrice());
				array.add(o);
			}
		}
		result.put("shopcartSize",list.size());
		result.put("list",array);
		result.put("status", 0);
		result.put("msg", "操作成功!");
		return result;
	}

	public JSONObject delShopCart(String fdUserId, String fdShopCartId) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		String hql="delete from AppShopCart where fdUserId='"+fdUserId+"' and fdId in("+StringUtil.formatOfSqlParams(fdShopCartId)+")";
		dbAccessor.bulkUpdate(hql);
		result.put("status", 0);
		result.put("msg", "操作成功!");
		return result;
	}

	public JSONObject updateShopCart(String fdUserId, String fdShopCartId,
			int fdQuantity) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		AppShopCart item=dbAccessor.get(AppShopCart.class, fdShopCartId);
		AppGoods  goods=dbAccessor.get(AppGoods.class, item.getFdGoodsId());
		if(goods.getFdQuantity()<fdQuantity){
			result.put("status",-1);
			result.put("max",goods.getFdQuantity());
			item.setFdQuantity(goods.getFdQuantity());
			dbAccessor.update(item);
			result.put("msg", "商品数量不足,商品数量为"+goods.getFdQuantity());
			return result;
		}
		item.setFdQuantity(fdQuantity);
		dbAccessor.update(item);
		result.put("status", 0);
		result.put("msg", "操作成功!");
		return result;
	}

	/**
	 *获得购物车商品列表
	 * @param fdUserId
	 * @param fdShopCartId
	 * @return
	 */
	public List<AppShopCart> shopcartList(String fdUserId, String fdShopCartId) {
		// TODO Auto-generated method stub
		String hql=" from AppShopCart where fdId in ("+StringUtil.formatOfSqlParams(fdShopCartId)+") and fdUserId='"+fdUserId+"'";
		List<AppShopCart> list=dbAccessor.find(hql);
		return list;
	}
	
	
	/**
	 * 获得相关费用
	 * @param fdUserId
	 * @param list
	 * @return
	 */
	public JSONObject  getAmountMessage(String fdUserId,List<AppShopCart> list) {
		// TODO Auto-generated method stub
		JSONObject r=new JSONObject();
		int fdQuantiy=0;
		BigDecimal fdGoodsAmount=new BigDecimal(0);
		for(int i=0;i<list.size();i++){
			AppShopCart item=list.get(i);
			AppGoods detail=dbAccessor.get(AppGoods.class, item.getFdGoodsId());
			if(detail==null||detail.getFdStatus().equals("0")){
				r.put("status", -1);
				r.put("msg",item.getFdGoodsName()+"商品不存在或者已下架");
				return r;
			}
			int remainCount=detail.getFdQuantity();
			if(item.getFdQuantity()>remainCount){
				r.put("status", -1);
				r.put("msg",item.getFdGoodsName()+"库存不足!库存为："+remainCount+"件");
				return r;
			}
			fdQuantiy+=item.getFdQuantity();
			fdGoodsAmount=fdGoodsAmount.add(detail.getFdPrice().multiply(new BigDecimal(item.getFdQuantity())));
		}
		r.put("fdQuantiy", fdQuantiy);//购买总数量
		r.put("fdGoodsAmount", fdGoodsAmount+"");//快递费
		r.put("fdAmount", fdGoodsAmount+"");//快递费
		r.put("fdTotalQuantity", fdQuantiy);//总费用
		r.put("fdCoupon", "0");//总费用
		r.put("status", 0);
		return r;
	}
	
	public JSONObject jiesuan(String fdUserId, String fdAddressId,String fdShopCartId, String fdRemark,String fdShopNo) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		if(!StringUtil.isNotNull(fdShopCartId)){
			result.put("status", -1);
			result.put("msg","请选择商品结算");
			return result;
		}
		if(!StringUtil.isNotNull(fdAddressId)){
			result.put("status", -1);
			result.put("msg","请选择收货地址");
			return result;
		}
		List<AppShopCart> list=shopcartList(fdUserId, fdShopCartId);
		if(list==null||list.size()==0){
			result.put("status", -1);
			result.put("msg","请选择商品结算");
			return result;
		}
		
		AppUserAddress address=dbAccessor.get(AppUserAddress.class, fdAddressId);
		if(address==null){
			result.put("status", -1);
			result.put("msg","请选择收货地址");
			return result;
		}
		JSONObject amountMessage=getAmountMessage(fdUserId,list);
		if(amountMessage.getInt("status")!=0){
			result.put("status", -1);
			result.put("msg",amountMessage.getString("msg"));
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
		order.setFdQuantity(amountMessage.getInt("fdQuantiy"));
		order.setFdGoodsAmount(new BigDecimal(amountMessage.getString("fdGoodsAmount")));
		order.setFdCoupon(new BigDecimal(amountMessage.getString("fdCoupon")));
		order.setFdAmount(new BigDecimal(amountMessage.getString("fdAmount")));
		
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
		
		String fdGoodsTitle="";
		for(int i=0;i<list.size();i++){
			AppShopCart shopcartItem=list.get(i);
			if(i==0){
				fdGoodsTitle=shopcartItem.getFdGoodsName();
			}
			AppGoods detail=dbAccessor.get(AppGoods.class, shopcartItem.getFdGoodsId());
			AppGoodsOrderItem orderItem=new AppGoodsOrderItem();
			orderItem.setFdPicUrl(shopcartItem.getFdPicUrl());
			orderItem.setFdGoodsId(shopcartItem.getFdGoodsId());
			orderItem.setFdGoodsNo(shopcartItem.getFdGoodsNo());
			orderItem.setFdGoodsName(shopcartItem.getFdGoodsName());
			orderItem.setFdQuantity(shopcartItem.getFdQuantity());
			orderItem.setFdPrice(detail.getFdPrice());
			orderItem.setFdTagPrice(shopcartItem.getFdTagPrice());
			orderItem.setFdAmount(detail.getFdPrice().multiply(new BigDecimal(shopcartItem.getFdQuantity())));
			orderItem.setFdSeqNo(i);
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
		}
		
		if(list.size()==1){
			if(fdGoodsTitle.length()<32){
				order.setFdOrderGoodsName(fdGoodsTitle);
			}else{
				order.setFdOrderGoodsName(fdGoodsTitle.substring(0,30)+"..");
			}
		}else{
			if(fdGoodsTitle.length()<27){
				order.setFdOrderGoodsName(fdGoodsTitle+"等"+amountMessage.getInt("fdQuantiy")+"件商品");
			}else{
				order.setFdOrderGoodsName(fdGoodsTitle.substring(0,27)+"等"+amountMessage.getInt("fdQuantiy")+"件商品");
			}
		}
		
		rlist.put("totalSize", list.size());
		rlist.put("list", array);
		order.setFdOrderDetail(rlist.toString());
		String hql="delete from AppShopCart where fdId in("+StringUtil.formatOfSqlParams(fdShopCartId)+")";
		dbAccessor.bulkUpdate(hql);
		
		result.put("fdOrderId",order.getFdId());
		result.put("status",0);
		result.put("msg","操作成功");
		return result;
	}


}

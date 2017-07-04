package com.platform.wp.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Refund;
import com.platform.sys.context.AppContextImpl;
import com.platform.sys.model.SysUser;
import com.platform.sys.service.SysUserService;
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppCharge;
import com.platform.wp.model.AppConfig;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserShop;
import com.platform.wp.service.AppChargeService;
import com.platform.wp.service.AppConfigService;
import com.platform.wp.service.AppUserService;
import com.platform.wx.service.WxGoodsOrderService;
import com.platform.wx.service.WxUserService;
import com.platform.wx.service.WxUserShopService;



/**
 * 五一工程工具类
 * @author Administrator
 *
 */
public class WpUtil {
	
	/**
	 * 操作成功
	 */
	public static final Integer RESULT_STATUS_YES=0;
	
	/**
	 * 业务逻辑错误
	 */
	public static final Integer RESULT_STATUS_NO=-1;
	
	
	/**
	 * 用户没登录
	 */
	public static final Integer RESULT_STATUS_NOT_LOGIN=-100;
	
	/**
	 * 用户没登录
	 */
	public static final String RESULT_STATUS_NOT_LOGIN_MSG="用户没登录";
	
	/**
	 * 商品下架或者商品存在
	 */
	public static final Integer RESULT_STATUS_NOT_GOODS=-200;
	

	/**
	 * 订单状态不对
	 */
	public static final Integer RESULT_STATUS_ORDER_STATUS_ERROR=-300;
	
	/**
	 * 商品下架或者商品存在
	 */
	public static final String RESULT_STATUS_NOT_GOODS_MSG="商品下架或者商品存在";
	
	/**
	 * 系统出错了
	 */
	public static final Integer RESULT_STATUS_ERROR=-900;
	
	/**
	 * 系统异常,请稍后试一下
	 */
	public static final String RESULT_STATUS_ERROR_MSG="系统异常,请稍后试一下";
	

	/**
	 * 订单未付款
	 */
	public static final String ORDER_STATUS_PAY_NO="0";
	
	/**
	 * 订单已付款
	 */
	public static final String ORDER_STATUS_PAY_YES="1";
	
	/**
	 * 订单已发货
	 */
	public static final String ORDER_STATUS_SEND_GOODS="2";
	
	/**
	 * 订单待评论
	 */
	public static final String ORDER_STATUS_REPLY="3";
	
	/**
	 * 已评价
	 */
	public static final String ORDER_STATUS_FINISH="7";
	
	/**
	 * 申请退款
	 */
	public static final String ORDER_STATUS_CANCEL_APPLY="4";
	
	/**
	 * 退款中
	 */
	public static final String ORDER_STATUS_CANCEL_AGREE_APPLY="5";
	
	/**
	 * 申请退款驳回
	 */
	public static final String ORDER_STATUS_CANCEL_BACK_APPLY="6";
	
	/**
	 * 取消订单(未付款而取消订单)
	 */
	public static final String ORDER_STATUS_CANCEL_NO_PAY="-1";
	
	/**
	 *  已退款(付款后取消订单)
	 */
	public static final String ORDER_STATUS_CANCEL_PAY="-2";
	
	
	/**
	 * 申请退款（退款状态）
	 */
	public static final String REFUNDER_STATUS_APPLY="0";
	
	/**
	 * 驳回申请退款（退款状态）
	 */
	public static final String REFUNDER_STATUS_BACK="-1";
	
	/**
	 * 退款中，同意退款（退款状态）
	 */
	public static final String REFUNDER_STATUS_PASS="1";
	
	/**
	 * 退款成功（退款状态）
	 */
	public static final String REFUNDER_STATUS_SUCCESS="2";
	
	/**
	 * 申请退货（退货状态）
	 */
	public static final String RETURN_GOODS_STATUS_APPLY="0";
	
	/**
	 * 退货中（退货状态）
	 */
	public static final String RETURN_GOODS_STATUS_PASS="1";
	
	/**
	 * 退货完成（退货状态）
	 */
	public static final String RETURN_GOODS_STATUS_SUCCESS="2";
	
	/**
	 * 驳回申请退货（退货状态）
	 */
	public static final String RETURN_GOODS_STATUS_BACK="-1";
	
	/**
	 *  退货申请取消(退货申请后取消订单)
	 */
	public static final String RETURN_GOODS_STATUS_CANCEL_APPLY="-2";
	
	/**
	 * 获得订单状态文字
	 * @param fdStatus
	 * @return
	 */
	public static String  getOrderStatusStr(String fdStatus){
		String str="";
		if(fdStatus.equals("0")){
			str="未付款";
		}else if(fdStatus.equals("1")){
			str="已付款";
		}else if(fdStatus.equals("2")){
			str="已发货";
		}else if(fdStatus.equals("3")){
			str="待评价 ";
		}else if(fdStatus.equals("4")){
			str="申请退款";
		}else if(fdStatus.equals("5")){
			str="退款中";
		}else if(fdStatus.equals("6")){
			str="退款申请被驳回";
		}else if(fdStatus.equals("7")){
			str="已完成";
		}else if(fdStatus.equals("-1")){
			str="订单已取消";
		}else if(fdStatus.equals("-2")){
			str="已退款";
		}
		return str;
	}

	public static AppUser getUserByCode(String fdCode){
		WxUserService service=AppContextImpl.getInstance().getBean(WxUserService.class);
		return service.getUserByCode(fdCode);
	}


	/**
	 * 通过fdOpenId获取用户
	 * @param fdOpenId
	 * @return
	 */
	public static AppUser getAppUser(String fdWeixinid){
		WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
		return service.getAppUserByOpenId(fdWeixinid);
	}

	/**
	 * 通过fdOpenId获取用户
	 * @param fdOpenId
	 * @return
	 */
	public static AppUser getAppUserByFdId(String fdId){
		WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
		return service.getAppUserByFdId(fdId);
	}
	
	public static Charge selectPayOfWeiXinPublic(String fdChargeId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException {
		// TODO Auto-generated method stub
		if(!StringUtil.isNotNull(fdChargeId)){
			 return null;
		 }
		AppConfig c=getConfig();
        Charge charge = Charge.retrieve(fdChargeId);
        return charge;
	}

	/**
	 * 获得醉美生活商城配置
	 * @return
	 */
	public static AppConfig getConfig(){
		AppConfigService service= AppContextImpl.getInstance().getBean(AppConfigService.class);
		AppConfig c=service.getByKey("PingApiKey");
		Pingpp.apiKey=c.getFdValue();
		return null;
	}
	
	public static AppCharge getAppCharge(String fdChargeId) {
		// TODO Auto-generated method stub
		AppChargeService service = AppContextImpl.getInstance().getBean(AppChargeService.class);
		return service.getAppCharge(fdChargeId);
	}

	/**
	 * 
	 * @return
	 */
	public static String getConfigValue(String key){
		AppConfigService service= AppContextImpl.getInstance().getBean(AppConfigService.class);
		AppConfig c=service.getByKey(key);
		if(c!=null){
			return c.getFdValue();
		}
		return null;
	}
	
	public static Charge startChargeOfWeiXinPublic(AppGoodsOrder order) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException {
		// TODO Auto-generated method stub
		Charge charge = null;
		AppConfig c=getConfig();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", order.getFdAmount().multiply(new BigDecimal(100)).intValue());
		chargeMap.put("currency", "cny");
		chargeMap.put("subject", order.getFdOrderGoodsName());
		chargeMap.put("body", order.getFdOrderGoodsName());
		chargeMap.put("order_no", order.getFdOrderNo()+"T"+DateUtils.getToday("yyyyMMddHHmm"));
		chargeMap.put("channel", "wx_pub");
		chargeMap.put("client_ip", order.getFdIp());

		Map<String, String> extra = new HashMap<String, String>();
		extra.put("open_id", order.getFdOpenId());
		chargeMap.put("extra", extra);
		Map<String, String> app = new HashMap<String, String>();
		app.put("id", getConfigValue("PingAppId"));
		chargeMap.put("app", app);
		charge = Charge.create(chargeMap);
		System.out.println("发起交易："+charge.toString());
		return charge;
	}

	public static Refund selectRefund(String fdRefundId, Charge charge) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException {
		// TODO Auto-generated method stub
		 Refund refund = charge.getRefunds().retrieve(fdRefundId);
         return refund;
	}

	public static Refund refund(AppGoodsOrder order, String fdApplyReason) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException {
		// TODO Auto-generated method stub
		Charge charge=selectPayOfWeiXinPublic(order.getFdChargeId());
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("description", fdApplyReason);
	    Refund refund = charge.getRefunds().create(params);
		return refund;
	} 
	
	/**
	 * 根据状态获得订单数
	 * @param fdUserId
	 * @return
	 */
	public static int getOrderCountByStatus(String fdUserId,String fdStatus){
		WxGoodsOrderService service=AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
		return service.getOrderCountByStatus(fdUserId,fdStatus);
	}

	/**
	 * 获得上级商家id
	 * @param fdShopId
	 * @return
	 */
	public static String getUpUser(String fdShopId) {
		// TODO Auto-generated method stub
		WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
		return service.getUpUser(fdShopId);
	}
	
	/**
	 * 获得上级商家id
	 * @param fdShopId
	 * @return
	 */
	public static void regSysUser(AppUser model) {
		// TODO Auto-generated method stub
		AppUserService service = AppContextImpl.getInstance().getBean(AppUserService.class);
		service.saveSysUser(model);
	}
	
	public static SysUser getSysUserByCode(String fdCode){
		SysUserService service=AppContextImpl.getInstance().getBean(SysUserService.class);
		return service.getSysUserByFdLoginName(fdCode);
	}
	
	/**
	 * 获得店铺信息
	 * @param fdCode
	 * @return
	 */
	public static AppUserShop getUserShopMessageByUserCode(String fdCode){
		WxUserShopService service=AppContextImpl.getInstance().getBean(WxUserShopService.class);
		return service.getUserShopMessageByUserCode(fdCode);
	}
	
	/**
	 * 获得店铺信息
	 * @param fdUserId
	 * @return
	 */
	public static AppUserShop getUserShopMessageByUserId(String fdUserId){
		WxUserShopService service=AppContextImpl.getInstance().getBean(WxUserShopService.class);
		return service.getUserShopMessageByUserId(fdUserId);
	}
}

package com.platform.wx.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.context.AppContextImpl;
import com.platform.util.DateUtils;
import com.platform.util.ImageUtils;
import com.platform.util.IpUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserAddress;
import com.platform.wp.model.AppUserShop;
import com.platform.wp.service.AppUserService;
import com.platform.wp.util.WpUtil;
import com.platform.wx.service.WxAddressService;
import com.platform.wx.service.WxCollectionService;
import com.platform.wx.service.WxGoodsOrderService;
import com.platform.wx.service.WxGoodsReplyService;
import com.platform.wx.service.WxGoodsService;
import com.platform.wx.service.WxGoodsTypeService;
import com.platform.wx.service.WxPayService;
import com.platform.wx.service.WxShopCartService;
import com.platform.wx.service.WxSmsService;
import com.platform.wx.service.WxUserService;
import com.platform.wx.service.WxUserShopService;

@Controller
@RequestMapping("/wxajax")
public class WxAjaxAction  extends BaseAction{
	
	/**
	 * 获得一级分类列表
	 * @param code 手机号码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/typelist")
	public void typelist(HttpServletRequest request,HttpServletResponse response) throws IOException{
		WxGoodsTypeService wxGoodsTypeService = AppContextImpl.getInstance().getBean(WxGoodsTypeService.class);
		JSONObject mJson=wxGoodsTypeService.typelist("#");
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 获得产品列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/goodslist")
	public void goodslist(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "fdTypeId");
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		WxGoodsService wxGoodsService = AppContextImpl.getInstance().getBean(WxGoodsService.class);
		JSONObject mJson=wxGoodsService.goodslist(map,start,limit);
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 获得短信验证码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/getSmsCode")
	public void getSmsCode(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdCode=request.getParameter("fdCode");
		String fdType=request.getParameter("fdType");
		JSONObject mJson=new JSONObject();
		if(!StringUtil.isNotNull(fdCode)){
			mJson.put("status", -1);
			mJson.put("msg","手机号不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdType)){
			mJson.put("status", -1);
			mJson.put("msg","短信类型不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		WxSmsService service = AppContextImpl.getInstance().getBean(WxSmsService.class);
		mJson=service.saveSms(fdCode,fdType);
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 注册
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/reg")
	public void reg(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdShopNo=request.getParameter("fdShopNo");
		String fdCode=request.getParameter("fdCode");
		String fdPwd=request.getParameter("fdPwd");
		String fdVaildCode=request.getParameter("fdVaildCode");//验证码
		JSONObject mJson=new JSONObject();
		if(!StringUtil.isNotNull(fdCode)){
			mJson.put("status", -1);
			mJson.put("msg","手机号不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdPwd)){
			mJson.put("status", -1);
			mJson.put("msg","密码不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdVaildCode)){
			mJson.put("status", -1);
			mJson.put("msg","验证码不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		WxSmsService service = AppContextImpl.getInstance().getBean(WxSmsService.class);
		boolean flag=service.vaildCode(fdCode, "0", fdVaildCode);
		if(!flag){
			mJson.put("status", -1);
			mJson.put("msg","验证码错误!");
		}
		WxUserService userSvc=AppContextImpl.getInstance().getBean(WxUserService.class);
		mJson=userSvc.reg(request,fdShopNo,fdCode,fdPwd);
		if(mJson.getInt("status")==0){
			String fdOpenId=getOpenId(request);
			AppUser user=WpUtil.getUserByCode(fdCode);
			if(StringUtil.isNotNull(fdOpenId)){
				user.setFdWeixinid(fdOpenId);
				String nickname=getWxUserName(request);
				String headimgurl=getWxUserHeader(request);
				String fdSex=getWxUserSex(request);
				userSvc.saveWeiXinMessage(user.getFdId(),nickname,headimgurl,fdOpenId, fdSex);
				user.setFdName(nickname);
				user.setFdPicUrl(headimgurl);
			}
			saveCurrentWebUser(request,user);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 设置密码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/resetPwd")
	public void resetPwd(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdCode=request.getParameter("fdCode");
		String fdPwd=request.getParameter("fdPwd");
		String fdVaildCode=request.getParameter("fdVaildCode");//验证码
		JSONObject mJson=new JSONObject();
		if(!StringUtil.isNotNull(fdCode)){
			mJson.put("status", -1);
			mJson.put("msg","手机号不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdPwd)){
			mJson.put("status", -1);
			mJson.put("msg","密码不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdVaildCode)){
			mJson.put("status", -1);
			mJson.put("msg","验证码不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		WxSmsService service = AppContextImpl.getInstance().getBean(WxSmsService.class);
		boolean flag=service.vaildCode(fdCode, "1", fdVaildCode);
		
		if(!flag){
			mJson.put("status", -1);
			mJson.put("msg","验证码错误!");
		}
		WxUserService userSvc=AppContextImpl.getInstance().getBean(WxUserService.class);
		mJson=userSvc.resetPwd(fdCode,fdPwd);
		if(mJson.getInt("status")==0){
			String fdOpenId=getOpenId(request);
			AppUser user=WpUtil.getUserByCode(fdCode);
			if(StringUtil.isNotNull(fdOpenId)){
				user.setFdWeixinid(fdOpenId);
				String nickname=getWxUserName(request);
				String headimgurl=getWxUserHeader(request);
				String fdSex=getWxUserSex(request);
				userSvc.saveWeiXinMessage(user.getFdId(),nickname,headimgurl,fdOpenId, fdSex);
				user.setFdName(nickname);
				user.setFdPicUrl(headimgurl);
			}
			saveCurrentWebUser(request,user);
			if(StringUtil.isNotNull(user.getFdName())){
				mJson.put("baseFinish", 1);//基础资料已经完整
			}else{
				mJson.put("baseFinish", 0);//基础资料不完整
			}
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 注册
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/login")
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdCode=request.getParameter("fdCode");
		String fdPwd=request.getParameter("fdPwd");
		JSONObject mJson=new JSONObject();
		if(!StringUtil.isNotNull(fdCode)){
			mJson.put("status", -1);
			mJson.put("msg","手机号不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!StringUtil.isNotNull(fdPwd)){
			mJson.put("status", -1);
			mJson.put("msg","密码不能为空!");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		AppUser user=WpUtil.getUserByCode(fdCode);
		if(user==null){
			mJson.put("status", -1);
			mJson.put("msg","用户或者密码错误");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		if(!user.getFdPwd().equals(fdPwd)){
			mJson.put("status", -1);
			mJson.put("msg","用户或者密码错误");
			writeJSON(response, mJson.toString()); 
			return ;
		}
		
		String fdOpenId=getOpenId(request);
		if(StringUtil.isNotNull(fdOpenId)){
			user.setFdWeixinid(fdOpenId);
			String nickname=getWxUserName(request);
			String headimgurl=getWxUserHeader(request);
			String fdSex=getWxUserSex(request);
			WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
			service.saveWeiXinMessage(user.getFdId(),nickname,headimgurl,fdOpenId, fdSex);
			user.setFdName(nickname);
			user.setFdPicUrl(headimgurl);
		}
		if(StringUtil.isNotNull(user.getFdName())){
			mJson.put("baseFinish", 1);//基础资料已经完整
		}else{
			mJson.put("baseFinish", 0);//基础资料不完整
		}
		saveCurrentWebUser(request,user);
		
		mJson.put("status", 0);
		mJson.put("msg", "登录成功!");
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *加入购物车
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("addShopCart")
	public void addShopCart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdGoodsId=request.getParameter("fdGoodsId");//商品id
		String fdQuantity=request.getParameter("fdQuantity");//商品数量
		String fdUserId="";
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -2);
			mJson.put("msg", "请先登录");
		}else{
			int count=Integer.parseInt(fdQuantity);
			if(count<=0){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "购买数量要大于0");
			}else{
				fdUserId=user.getFdId();
				WxShopCartService service = AppContextImpl
						.getInstance().getBean(WxShopCartService.class);
				mJson= service.addShopCart(fdUserId,fdGoodsId,count);
			}
		}
		writeJSON(response, mJson.toString());
	}
	
	
	/**
	 * 购物车列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/shopCartList")
	public void shopCartList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxShopCartService service = AppContextImpl
					.getInstance().getBean(WxShopCartService.class);
			mJson= service.shopCartList(fdUserId);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *删除购物车
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("delShopCart")
	public void delShopCart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdShopCartId=request.getParameter("fdShopCartId");
		String fdUserId="";
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxShopCartService service = AppContextImpl
					.getInstance().getBean(WxShopCartService.class);
			mJson= service.delShopCart(fdUserId,fdShopCartId);
		}
		writeJSON(response, mJson.toString());
	}
	
	
	/**
	 * 
	 *修改购物车数量
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("updateShopCart")
	public void updateShopCart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdShopCartId=request.getParameter("fdShopCartId");
		String fdQuantity=request.getParameter("fdQuantity");
		String fdUserId="";
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			int count=Integer.parseInt(fdQuantity);
			if(count<=0){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "购买数量要大于0");
			}else{
				fdUserId=user.getFdId();
				WxShopCartService service = AppContextImpl
						.getInstance().getBean(WxShopCartService.class);
				mJson= service.updateShopCart(fdUserId,fdShopCartId,count);
			}
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 购物车结算
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/jiesuan")
	public void jiesuan(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdAddressId=request.getParameter("fdAddressId");
		String fdShopCartId=request.getParameter("fdShopCartId");
		String fdRemark=request.getParameter("fdRemark");
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		String fdShopNo=request.getParameter("fdShopNo");
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxShopCartService service = AppContextImpl.getInstance().getBean(WxShopCartService.class);
			mJson=service.jiesuan(fdUserId,fdAddressId,fdShopCartId,fdRemark,fdShopNo);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 立即购买
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/buynow")
	public void buynow(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdAddressId=request.getParameter("fdAddressId");
		String fdGoodsId=request.getParameter("fdGoodsId");
		String fdQuantity=request.getParameter("fdQuantity");
		String fdRemark=request.getParameter("fdRemark");
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=new JSONObject();
		try{
			if(user==null){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "请先登录");
			}else{
				fdUserId=user.getFdId();
				int count=Integer.parseInt(fdQuantity);
				fdUserId=user.getFdId();
				if(count<=0){
					mJson=new JSONObject();
					mJson.put("status", -1);
					mJson.put("msg", "请选择商品");
				}else{
					WxGoodsService service = AppContextImpl.getInstance().getBean(WxGoodsService.class);
					mJson=service.buynow(fdUserId,fdAddressId,fdGoodsId,count,fdRemark,fdShopNo);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mJson.put("status", -1);
			mJson.put("msg", "系统异常");
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 发起支付交易(ping++)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/startCharge")
	public void startCharge(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		//http://192.168.1.112:8080/mlshop/ajax/startCharge?fdOrderId=402881f051cdd3970151ce093fb2000d
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		JSONObject result=new JSONObject();
		String fdOpenId=getOpenId(request);
		try{
			String ip=IpUtils.getIpAddr(request);
			WxPayService service = AppContextImpl.getInstance().getBean(WxPayService.class);
			result=service.startCharge(fdOrderId,fdOpenId,ip);
		}catch (Exception e) {
			e.printStackTrace();
			result=new JSONObject();
			result.put("status",WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 收货地址列表
	 * @param code 手机号码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/addressList")
	public void addressList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		    mJson=service.addressList(fdUserId);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 删除收货地址
	 * @param code 手机号码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/delAddressList")
	public void delAddressList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		    mJson=service.delAddressList(fdUserId,fdId);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 保存收货地址
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/saveAddress")
	public void saveAddress(AppUserAddress item,HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		    mJson=service.saveAddress(fdUserId,item);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 获得默认地址
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/getDefaultAddress")
	public void getDefaultAddress(AppUserAddress item,HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		    mJson=service.getDefaultAddress(fdUserId,"");
		}
		writeJSON(response, mJson.toString());
	}
	/**
	 * 修改地址默认状态
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/addressStatus")
	public void addressStatus(AppUserAddress item,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		String fdStatus=request.getParameter("fdStatus");
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
			mJson=service.addressStatus(fdId,fdStatus);
		}
		writeJSON(response, mJson.toString());
	}
	
	
	/**
	 * 取消订单(未付款订单)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/cancelOrderNoPay")
	public void cancelOrderNoPay(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		try{
			if(user==null){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "请先登录");
			}else{
				WxGoodsOrderService service = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
				result=service.cancelOrderNoPay(fdOrderId);
			}
		}catch (Exception e) {
			result=new JSONObject();
			result.put("status", -1);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 申请退款
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/applyRefund")
	public void applyRefund(HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		String fdApplyReason=request.getParameter("fdApplyReason");//申请退款理由
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		JSONObject mJson=null;
		try{
			if(user==null){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "请先登录");
			}else{
				fdUserId=user.getFdId();
				WxGoodsOrderService service = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
				result=service.applyRefund(fdOrderId,fdUserId,fdApplyReason);
			}
			
		}catch (Exception e) {
			mJson=new JSONObject();
			mJson.put("status", WpUtil.RESULT_STATUS_ERROR);
			mJson.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		writeJSON(response,result.toString());
	}
	
	
	/**
	 * 
	 *收藏
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("collection")
	public void collection(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdGoodsId=request.getParameter("fdGoodsId");
		String fdUserId="";
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -2);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxCollectionService service = AppContextImpl
					.getInstance().getBean(WxCollectionService.class);
			mJson= service.collection(fdUserId,fdGoodsId);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *收藏
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("cancelCollection")
	public void cancelCollection(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdGoodsId=request.getParameter("fdGoodsId");
		String fdUserId="";
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -1);
			mJson.put("msg", "请先登录");
		}else{
			fdUserId=user.getFdId();
			WxCollectionService service = AppContextImpl
					.getInstance().getBean(WxCollectionService.class);
			mJson= service.cancelCollection(fdUserId,fdGoodsId);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 收藏商品列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/collGoodsList")
	public void collGoodsList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		if(user!=null){
			fdUserId=user.getFdId();
		}
		WxCollectionService service = AppContextImpl
				.getInstance().getBean(WxCollectionService.class);
		JSONObject mJson= service.goodsList(fdUserId,start,limit);
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 获得用户订单列表
	 * @param code 手机号码
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/getMyOrderList")
	public void getMyOrderList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		String fdUserId="";
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		String orderType=request.getParameter("orderType");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		
		JSONObject mJson=null;
		try{
			if(user==null){
				mJson=new JSONObject();
				mJson.put("status", -1);
				mJson.put("msg", "请先登录");
			}else{
				String fdStatus="";
				if(StringUtil.isNotNull(orderType)){
					if(orderType.equals("all")){//全部订单
						
					}else if(orderType.equals("noPay")){//未付款
						fdStatus=WpUtil.ORDER_STATUS_PAY_NO;
					}else if(orderType.equals("takeGoods")){//待提货订单
						fdStatus=WpUtil.ORDER_STATUS_PAY_YES+","+WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY+","+WpUtil.ORDER_STATUS_CANCEL_APPLY+","+WpUtil.ORDER_STATUS_CANCEL_AGREE_APPLY;
					}else if(orderType.equals("recevieGoods")){//待收货
						fdStatus=WpUtil.ORDER_STATUS_SEND_GOODS;
					}else if(orderType.equals("finish")){//待评价
						fdStatus=WpUtil.ORDER_STATUS_REPLY;
					}
				}
				fdUserId=user.getFdId();
				WxGoodsOrderService service = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
				mJson=service.getMyOrderList(fdUserId,fdStatus,start,limit);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mJson.put("status", -1);
			mJson.put("msg", "系统异常");
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *判断是否登录
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("hasLogin")
	public void hasLogin(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", 0);
			mJson.put("hasLogin", "0");
		}else{
			mJson=new JSONObject();
			mJson.put("status", 0);
			mJson.put("hasLogin", "1");
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *评论
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("reply")
	public void reply(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		String fdOrderId=request.getParameter("fdOrderId");
		String replyDetail=request.getParameter("replyDetail");
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status",-2);
			mJson.put("hasLogin", "用户未登录");
		}else{
			user=WpUtil.getAppUserByFdId(user.getFdId());
			WxGoodsReplyService service = AppContextImpl
					.getInstance().getBean(WxGoodsReplyService.class);
			mJson= service.reply(user,fdOrderId,replyDetail);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 商品评论列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/goodsReplyList")
	public void goodsReplyList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdGoodsId=request.getParameter("fdGoodsId");
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		WxGoodsReplyService wxGoodsService = AppContextImpl.getInstance().getBean(WxGoodsReplyService.class);
		JSONObject mJson=wxGoodsService.goodsReplyList(fdGoodsId,start,limit);
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 我的评论列表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/myReplyList")
	public void myReplyList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status", -2);
			mJson.put("hasLogin", "请登录");
			writeJSON(response, mJson.toString());
			return ;
		}
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		WxGoodsReplyService wxGoodsService = AppContextImpl.getInstance().getBean(WxGoodsReplyService.class);
		mJson=wxGoodsService.myReplyList(user.getFdId(),start,limit);
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 *添加基础信息
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("addBaseInfo")
	public void addBaseInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		AppUser user=getCurrentWebUser(request);
		Map<String,String> map=StringUtil.getParams(request, "fdName,fdSex,fdBirthday,fdProvince,fdCity,fdArea");
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status",-2);
			mJson.put("hasLogin", "用户未登录");
		}else{
			user=WpUtil.getAppUserByFdId(user.getFdId());
			WxUserService service = AppContextImpl
					.getInstance().getBean(WxUserService.class);
			mJson= service.addBaseInfo(user.getFdId(),map);
			if(mJson.getInt("status")==0){
				user=WpUtil.getUserByCode(user.getFdCode());
				saveCurrentWebUser(request, user);
			}
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 
	 * 保存店铺信息
	 * @return
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping("saveStore")
	public void saveStore(HttpServletRequest request,HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException{
		AppUser user=getCurrentWebUser(request);
		String fdStoreName=request.getParameter("fdStoreName");
		String fdStoreType=request.getParameter("fdStoreType");
		String fdStoreAddress=request.getParameter("fdStoreAddress");
		String fdStoreBrand=request.getParameter("fdStoreBrand");
		String fdPicUrl1=request.getParameter("fdPicUrl1");
		String fdPicUrl2=request.getParameter("fdPicUrl2");
		
		String fdPicUrlName1=ImageUtils.GenerateImageSys(request,fdPicUrl1, "userStore");
		String fdPicUrlName2=ImageUtils.GenerateImageSys(request,fdPicUrl2, "userStore");
		
		String fdPicUrl="userStore"+"/"+fdPicUrlName1+","+"userStore"+"/"+fdPicUrlName2;
		
		JSONObject mJson=null;
		if(user==null){
			mJson=new JSONObject();
			mJson.put("status",-2);
			mJson.put("hasLogin", "用户未登录");
		}else{
			user=WpUtil.getAppUserByFdId(user.getFdId());
			WxUserShopService service = AppContextImpl
					.getInstance().getBean(WxUserShopService.class);
			
			AppUserShop item=WpUtil.getUserShopMessageByUserCode(user.getFdCode());
			if(item==null){
				item=new AppUserShop();
				item.setFdCreateTime(DateUtils.getNow());
			}
			item.setFdStoreName(fdStoreName);
			item.setFdStoreType(fdStoreType);
			item.setFdStoreAddress(fdStoreAddress);
			item.setFdStoreBrand(fdStoreBrand);
			item.setFdPicUrl(fdPicUrl);
			if(StringUtil.isNotNull(item.getFdId())){
				service.update(item);
			}else{
				item.setFdUserCode(user.getFdCode());
				item.setFdUserId(user.getFdId());
				item.setFdUserName(user.getFdName());
				item.setFdStatus("0");
				service.save(item);
			}
			mJson=new JSONObject();
			mJson.put("status",0);
		}
		writeJSON(response, mJson.toString());
	}
	
	/**
	 * 保存用户信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/saveUserMessage")
	public void saveUserMessage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			String fdUserId=request.getParameter("fdUserId");
			String fdUserMessage=request.getParameter("fdUserMessage");
			if(StringUtil.isNotNull(fdUserId)){
				AppUserService service = AppContextImpl
						.getInstance().getBean(AppUserService.class);
				service.saveUserMessage(fdUserId,fdUserMessage);
			}
			result.put("status", 0);
			result.put("msg", "保存成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
}

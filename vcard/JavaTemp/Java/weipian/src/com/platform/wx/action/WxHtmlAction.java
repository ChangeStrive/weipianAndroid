package com.platform.wx.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.context.AppContextImpl;
import com.platform.util.StringUtil;
import com.platform.weixin.util.WeiXinUtils;
import com.platform.wp.model.AppGoods;
import com.platform.wp.model.AppGoodsOrder;
import com.platform.wp.model.AppGoodsOrderRefunder;
import com.platform.wp.model.AppShopCart;
import com.platform.wp.model.AppUser;
import com.platform.wp.model.AppUserAddress;
import com.platform.wp.model.AppUserShop;
import com.platform.wp.service.AppGoodsOrderRefunderService;
import com.platform.wp.service.AppGoodsPicService;
import com.platform.wp.util.WpUtil;
import com.platform.wx.service.WxAddressService;
import com.platform.wx.service.WxGoodsOrderService;
import com.platform.wx.service.WxGoodsService;
import com.platform.wx.service.WxGoodsTypeService;
import com.platform.wx.service.WxPayService;
import com.platform.wx.service.WxShopCartService;
import com.platform.wx.service.WxUserService;
import com.platform.wx.service.WxWebAdService;

@Controller
@RequestMapping("/wx")
public class WxHtmlAction extends BaseAction{
	
	/**
	 * 验证微信是否绑定
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginWeixin")
	public String loginWeixin(HttpServletRequest request,HttpServletResponse response){
		String url=request.getParameter("url");
	    AppUser user=getCurrentWebUser(request);
		if(user==null){
			String openId=getOpenId(request);
			if(openId==null){
				try {
					return "redirect:"+WeiXinUtils.getOpenId(request,url);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				user=WpUtil.getAppUser(openId);
				if(user!=null){
					saveCurrentWebUser(request,user);
				}
			}
		}
		return "redirect:"+url;
	}
	
	public String getShopMessage(HttpServletRequest request){
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser shop=WpUtil.getUserByCode(fdShopNo);
		if(shop==null||shop.getFdStatus().equals("0")){
			return "app/jsp/noShop/noShop.html";
		}
		String fdShopPicUrl=StringUtil.getCurrentUrl(request, "app/images/userheader.gif");
		if(StringUtil.isNotNull(shop.getFdPicUrl())){
			fdShopPicUrl=shop.getFdPicUrl();
		}
		request.setAttribute("fdShopPicUrl",fdShopPicUrl);
		request.setAttribute("fdShopName",shop.getFdName());
		request.setAttribute("fdShopNo",fdShopNo);
		request.setAttribute("shop",shop);
		return null;
	}
	
	public String toLogin(HttpServletRequest request){
		AppUser user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}else{
			if(!StringUtil.isNotNull(user.getFdName())){
				String fdShopNo=request.getParameter("fdShopNo");
				return "redirect:/wx/baseinfo?fdShopNo="+fdShopNo;
			}
		}
		return null;
	}
	
	/**
	 * 首页
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		WxWebAdService service2 = AppContextImpl.getInstance().getBean(WxWebAdService.class);
		JSONObject adlist=service2.adlist("0");
		request.setAttribute("adlist", adlist);
		
	
		return "app/jsp/index/index.html";
	}
	
	/**
	 * 分类
	 * @param request
	 * @return
	 */
	@RequestMapping("/category")
	public String category(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		String fdTypeId=request.getParameter("fdTypeId");
		request.setAttribute("fdTypeId", fdTypeId);
		
		WxGoodsTypeService wxGoodsTypeService = AppContextImpl.getInstance().getBean(WxGoodsTypeService.class);
		JSONObject typelist=wxGoodsTypeService.typelist("#");
		request.setAttribute("typelist", typelist);
		
		return "app/jsp/category/category.html";
	}
	
	/**
	 * 个人中心
	 * @param request
	 * @return
	 */
	@RequestMapping("/my")
	public String my(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		AppUser  user=getCurrentWebUser(request);
		
		String fdUserId=user.getFdId();
		
		String fdStatus=WpUtil.ORDER_STATUS_PAY_NO;//未付款
		int noPayCount=WpUtil.getOrderCountByStatus(fdUserId, fdStatus);
		request.setAttribute("noPayCount", noPayCount);
		
		fdStatus=WpUtil.ORDER_STATUS_PAY_YES+","+WpUtil.ORDER_STATUS_CANCEL_BACK_APPLY+","+WpUtil.ORDER_STATUS_CANCEL_APPLY;//待提货订单
		int dfhCount=WpUtil.getOrderCountByStatus(fdUserId, fdStatus);
		request.setAttribute("dfhCount", dfhCount);
		
		fdStatus=WpUtil.ORDER_STATUS_SEND_GOODS;
		int dshCount=WpUtil.getOrderCountByStatus(fdUserId, fdStatus);
		request.setAttribute("dshCount", dshCount);
		
		fdStatus=WpUtil.ORDER_STATUS_REPLY;//待评价
		int replyCount=WpUtil.getOrderCountByStatus(fdUserId, fdStatus);
		request.setAttribute("replyCount", replyCount);
		
		return "app/jsp/my/my.html";
	}
	
	/**
	 * 个人中心
	 * @param request
	 * @return
	 */
	@RequestMapping("/myorder")
	public String myorder(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/myorder/myorder.html";
	}
	
	/**
	 * 商品列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/goods")
	public String goods(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/goods/goods.html";
	}
	
	
	/**
	 * 购物车
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopcart")
	public String shopcart(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		AppUser  user=getCurrentWebUser(request);
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdUserId=user.getFdId();
		WxShopCartService service = AppContextImpl
				.getInstance().getBean(WxShopCartService.class);
		JSONObject mJson= service.shopCartList(fdUserId);
		request.setAttribute("shopCartList", mJson);
		if(mJson.getInt("shopcartSize")==0){
			return "app/jsp/shopcart/noshopCart.html";
		}
		
		return "app/jsp/shopcart/shopcart.html";
	}
	

	/**
	 * 购物车结算
	 * @param request
	 * @return
	 */
	@RequestMapping("/jiesuan")
	public String jiesuan(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String fdShopCartId=request.getParameter("fdShopCartId");
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser  user=getCurrentWebUser(request);
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdUserId=user.getFdId();
		WxShopCartService shopcartService = AppContextImpl.getInstance().getBean(WxShopCartService.class);
		List<AppShopCart> list=shopcartService.shopcartList(fdUserId, fdShopCartId);
		if(list==null||list.size()==0){
			return "redirect:/wx/shopCartBuy?fdShopNo="+fdShopNo;
		}
		request.setAttribute("goodsList", list);
		
		JSONObject amountMessage=shopcartService.getAmountMessage(user.getFdId(),list);
		if(amountMessage.getInt("status")!=0){
			request.setAttribute("msg",amountMessage.getString("msg"));
			return "app/jsp/jiesuan/noGoods.html";
		}
		
		request.setAttribute("amountMessage", amountMessage);
		
		WxAddressService addressService = AppContextImpl.getInstance().getBean(WxAddressService.class);
		String fdAddressId=request.getParameter("fdAddressId");
		JSONObject address=addressService.getDefaultAddress(fdUserId,fdAddressId);
		request.setAttribute("address", address);
		
		return "app/jsp/jiesuan/jiesuan.html";
	}
	
	/**
	 * 立即购买
	 * @param request
	 * @return
	 */
	@RequestMapping("/buynow")
	public String buynow(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser  user=getCurrentWebUser(request);
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		String fdGoodsId=request.getParameter("fdGoodsId");
		String fdQuantity=request.getParameter("fdQuantity");
		String fdUserId="";
		int count=Integer.parseInt(fdQuantity);
		fdUserId=user.getFdId();
		if(count<=0){
			return "redirect:/wx/index?fdShopNo="+fdShopNo;
		}
		
		WxGoodsService shopcartService = AppContextImpl.getInstance().getBean(WxGoodsService.class);
		JSONObject orderDetail=shopcartService.getGoodsDetail(fdUserId,fdGoodsId,count);
	
		if(orderDetail.getInt("status")!=0){
			return "redirect:/wx/index?fdShopNo="+fdShopNo;
		}
		request.setAttribute("orderDetail", orderDetail);
		request.setAttribute("fdGoodsId", fdGoodsId);
		request.setAttribute("fdQuantity", fdQuantity);
		WxAddressService addressService = AppContextImpl.getInstance().getBean(WxAddressService.class);
		String fdAddressId=request.getParameter("fdAddressId");
		JSONObject address=addressService.getDefaultAddress(fdUserId,fdAddressId);
		request.setAttribute("address", address);
		
		return "app/jsp/buynow/buynow.html";
	}
	
	
	/**
	 * 商品详情
	 * @param request
	 * @return
	 */
	@RequestMapping("/goodsDetail")
	public String goodsDetail(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		String fdGoodsId=request.getParameter("fdGoodsId");
		WxGoodsService goodsService=AppContextImpl.getInstance().getBean(WxGoodsService.class);
		AppGoods goods=goodsService.getGoods(fdGoodsId);
		if(goods==null){
			return "redirect:/wx/index";
		}
		request.setAttribute("goods", goods);
		
		AppGoodsPicService service = AppContextImpl
				.getInstance().getBean(AppGoodsPicService.class);
		JSONObject picList=service.getAppGoodsPicList(fdGoodsId);
		request.setAttribute("picList", picList);
		return "app/jsp/goodsDetail/goodsDetail.html";
	}
	
	/**
	 * 收货地址
	 * @param request
	 * @return
	 */
	@RequestMapping("/address")
	public String address(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		AppUser  user=getCurrentWebUser(request);
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		String fdUserId=user.getFdId();
		WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		JSONObject  mJson=service.addressList(fdUserId);
	    request.setAttribute("addressList", mJson);
		return "app/jsp/address/address.html";
	}
	
	/**
	 * 新增收货地址
	 * @param request
	 * @return
	 */
	@RequestMapping("/addAddress")
	public String addAddress(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String fdId=request.getParameter("fdId");
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		AppUser  user=getCurrentWebUser(request);
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		AppUserAddress address=null;
		if(StringUtil.isNotNull(fdId)){
			WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		   address=service.get(fdId);
		}
		if(address==null){
			address=new AppUserAddress();
		}
		request.setAttribute("item",address);
		return "app/jsp/address/addAddress.html";
	}
	
	/**
	 * 收货地址
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectAddress")
	public String selectAddress(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		AppUser  user=getCurrentWebUser(request);
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdUserId=user.getFdId();
		WxAddressService service = AppContextImpl.getInstance().getBean(WxAddressService.class);
		JSONObject  mJson=service.addressList(fdUserId);
	    request.setAttribute("addressList", mJson);
		return "app/jsp/selectAddress/selectAddress.html";
	}
	
	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String fdShopNo=request.getParameter("fdShopNo");
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		AppUser  user=getCurrentWebUser(request);
		String openId=getOpenId(request);
		if(user==null&&StringUtil.isNotNull(openId)){
			user=WpUtil.getAppUser(openId);
		}
		String fdOpenId=getOpenId(request);
		if(user!=null){
			if(StringUtil.isNotNull(fdOpenId)){
				user.setFdWeixinid(fdOpenId);
				String nickname=getWxUserName(request);
				String headimgurl=getWxUserHeader(request);
				String fdSex=getWxUserSex(request);
				WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
				service.saveWeiXinMessage(user.getFdId(),nickname,headimgurl,fdOpenId,fdSex);
				user.setFdName(nickname);
				user.setFdPicUrl(headimgurl);
				user.setFdSex(fdSex);
			}
			saveCurrentWebUser(request,user);
		}
		if(user!=null){
			if(StringUtil.isNotNull(backUrl)){
				return "redirect:"+backUrl;
			}else{
				return "redirect:/wx/mycard?fdShopNo="+fdShopNo;
			}
		}
	
		return "app/jsp/login/login.html";
	}
	
	/**
	 *注册
	 * @param request
	 * @return
	 */
	@RequestMapping("/reg")
	public String reg(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		return "app/jsp/reg/reg.html";
	}
	
	/**
	 * 忘记密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/forget")
	public String forget(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		return "app/jsp/forget/forget.html";
	}
	
	/**
	 * 我的名片
	 * @param request
	 * @return
	 */
	@RequestMapping("/mycard")
	public String mycard(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		AppUser user=getCurrentWebUser(request);
		String fdShopNo=request.getParameter("fdShopNo");
		if(user!=null){
			if(user.getFdCode().equals(fdShopNo)){
				request.setAttribute("isMe", 1);
			}else{
				request.setAttribute("isMe", 0);
			}
			request.setAttribute("myCode",user.getFdCode());
		}
		return "app/jsp/mycard/mycard.html";
	}
	
	/**
	 * 基础信息录入
	 * @param request
	 * @return
	 */
	@RequestMapping("/baseinfo")
	public String baseinfo(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		AppUser user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}
		return "app/jsp/baseinfo/baseinfo.html";
	}
	
	/**
	 * 激活微片支付商户
	 * @param request
	 * @return
	 */
	@RequestMapping("/activeShop")
	public String activeShop(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		AppUser user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}
		AppUserShop item=WpUtil.getUserShopMessageByUserCode(user.getFdCode());
		request.setAttribute("item", item);
		return "app/jsp/activeShop/activeShop.html";
	}
	
	/**
	 * 完善商户资料
	 * @param request
	 * @return
	 */
	@RequestMapping("/shopMessage")
	public String shopMessage(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		AppUser user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}
		AppUserShop item=WpUtil.getUserShopMessageByUserCode(user.getFdCode());
		if(item==null){
			item=new AppUserShop();
		}
		request.setAttribute("item", item);
		return "app/jsp/shopMessage/shopMessage.html";
	}
	
	/**
	 * 微片新用户激活
	 * @param request
	 * @return
	 */
	@RequestMapping("/newactive")
	public String newactive(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		return "app/jsp/newactive/newactive.html";
	}
	

	/**
	 * 退出
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/exit")
	public String  exit(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		AppUser user=getCurrentWebUser(request);
		if(user!=null){
			WxUserService service = AppContextImpl.getInstance().getBean(WxUserService.class);
			service.saveOpenId(user.getFdId(),"");
			saveCurrentWebUser(request, null);
			saveOpenId(request, null);
		}
		String fdShopNo=request.getParameter("fdShopNo");
		return "redirect:/wx/login?fdShopNo="+fdShopNo;
	}
	
	/**
	 * 我的收藏
	 * @param request
	 * @return
	 */
	@RequestMapping("/myCollection")
	public String myCollection(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/myCollection/myCollection.html";
	}
	
	/**
	 * 我的订单支付
	 * @param request
	 * @return
	 */
	@RequestMapping("/pay")
	public String pay(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		String fdOrderId=request.getParameter("fdOrderId");
		WxGoodsOrderService service = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
		AppGoodsOrder item=null;
		if(StringUtil.isNotNull(fdOrderId)){
			item=service.getOrder(fdOrderId);
		}
		if(item==null){
			request.setAttribute("msg", "订单不存在!");
			return "app/jsp/pay/payMsg.html";
		}
		if(!item.getFdStatus().equals(WpUtil.ORDER_STATUS_PAY_NO)){
			request.setAttribute("msg", WpUtil.getOrderStatusStr(item.getFdStatus()));
			return "app/jsp/pay/payMsg.html";
		}
		request.setAttribute("item", item);
		request.setAttribute("user", WpUtil.getAppUserByFdId(item.getFdUserId()));
		request.setAttribute("fdOrderId", fdOrderId);
		WeiXinUtils.getWeiXinJSConfig(request);
		return "app/jsp/pay/pay.html";
	}
	
	/**
	 *订单详情
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/orderDetail")
	public String  orderDetail(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		
		String fdShopNo=request.getParameter("fdShopNo");
		String fdOrderId=request.getParameter("fdOrderId");
		WxGoodsOrderService service = AppContextImpl
				.getInstance().getBean(WxGoodsOrderService.class);
		if(!StringUtil.isNotNull(fdOrderId)){
			return "redirect:/wx/index?fdShopNo="+fdShopNo;
		}
		AppGoodsOrder order=service.getOrder(fdOrderId);
		if(order==null){
			return "redirect:/wx/index?fdShopNo="+fdShopNo;
		}
		request.setAttribute("order", order);
		JSONObject detail=JSONObject.fromObject(order.getFdOrderDetail());
		request.setAttribute("detail", detail);
		
		AppGoodsOrderRefunderService rsvc = AppContextImpl.getInstance().getBean(AppGoodsOrderRefunderService.class);
		List<AppGoodsOrderRefunder> refunerList=rsvc.list(fdOrderId);
		request.setAttribute("refunerList", refunerList);
		return "app/jsp/orderDetail/orderDetail.html";
	}
	
	/**
	 * 所有订单（我的订单）
	 * @param request
	 * @return
	 */
	@RequestMapping("/all")
	public String all(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		
		return "app/jsp/all/all.html";
	}
	
	/**
	 *  待付款（我的订单）
	 * @param request
	 * @return
	 */
	@RequestMapping("/dfk")
	public String dfk(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/dfk/dfk.html";
	}
	
	/**
	 * 待发货（我的订单）
	 * @param request
	 * @return
	 */
	@RequestMapping("/dfh")
	public String dfh(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/dfh/dfh.html";
	}
	
	/**
	 * 已发货（我的订单）
	 * @param request
	 * @return
	 */
	@RequestMapping("/yfh")
	public String yfh(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/yfh/yfh.html";
	}
	
	/**
	 * 待评价（我的订单）
	 * @param request
	 * @return
	 */
	@RequestMapping("/ywc")
	public String ywc(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/ywc/ywc.html";
	}
	
	/**
	 * 支付结果
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payResult")
	public String  payResult(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdChargeId=request.getParameter("fdChargeId");//订单id
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		JSONObject result=new JSONObject();
		try{
			WxPayService service = AppContextImpl.getInstance().getBean(WxPayService.class);
			result=service.pay(fdChargeId);
		}catch (Exception e) {
			e.printStackTrace();
			result=new JSONObject();
			result.put("status", WpUtil.RESULT_STATUS_ERROR);
			result.put("msg", WpUtil.RESULT_STATUS_ERROR_MSG);
		}
		int status=result.getInt("status");
		if(status==WpUtil.RESULT_STATUS_YES){
			request.setAttribute("msg", "支付成功！");
		}else{
			request.setAttribute("msg", "支付失败！");
		}
		request.setAttribute("status", status);
		request.setAttribute("fdOrderId", fdOrderId);
		return "app/jsp/pay/payResult.html";
	}
	
	/**
	 * 评论
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/reply")
	public String  reply(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		AppUser  user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}
		String fdShopNo=request.getParameter("fdShopNo");
		String fdOrderId=request.getParameter("fdOrderId");//订单id
		WxGoodsOrderService service = AppContextImpl.getInstance().getBean(WxGoodsOrderService.class);
		
		AppGoodsOrder order=service.getOrder(fdOrderId);
		if(order==null){
			return "redirect:/wx/my?fdShopNo="+fdShopNo;
		}
		if(!order.getFdStatus().equals(WpUtil.ORDER_STATUS_REPLY)){
			return "redirect:/wx/my?fdShopNo="+fdShopNo;
		}
		request.setAttribute("order", order);
		JSONObject detail=JSONObject.fromObject(order.getFdOrderDetail());
		request.setAttribute("detail", detail);
		return "app/jsp/reply/reply.html";
	}
	
	/**
	 * 我的评论
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/myReply")
	public String  myReply(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		return "app/jsp/myReply/myReply.html";
	}
	
	/**
	 * 商品评价
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goodsReply")
	public String  goodsReply(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdGoodsId=request.getParameter("fdGoodsId");
		request.setAttribute("fdGoodsId", fdGoodsId);
		return "app/jsp/goodsReply/goodsReply.html";
	}
	
	/**
	 * 点击头像查看名片信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/viewMessage")
	public String  viewMessage(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String toLoginStr=toLogin(request);
		if(toLoginStr!=null){
			return toLoginStr;
		}
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser user=getCurrentWebUser(request);
		if(user==null||!user.getFdCode().equals(fdShopNo)){
			return "redirect:/wx/viewCard?fdShopNo="+fdShopNo;
		}else{
			return "redirect:/wx/writeCard?fdShopNo="+fdShopNo;
		}
	}
	
	/**
	 * 上传名片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/uploadCard")
	public String  uploadCard(HttpServletRequest request,HttpServletResponse response){
		return "app/jsp/uploadCard/uploadCard.html";
	}
	

	/**
	 * 录入名片信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/writeCard")
	public String  writeCard(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		String fdShopNo=request.getParameter("fdShopNo");
		AppUser user=getCurrentWebUser(request);
		if(user==null){
			return StringUtil.getRedirectLogin(request);
		}
		if(!user.getFdCode().equals(fdShopNo)){
			return "redirect:/wx/viewCard?fdShopNo="+fdShopNo;
		}
			
		return "app/jsp/writeCard/writeCard.html";
	}
	
	/**
	 * 查看名片信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/viewCard")
	public String  viewCard(HttpServletRequest request,HttpServletResponse response){
		String toUrl=getShopMessage(request);
		if(toUrl!=null){
			return toUrl;
		}
		return "app/jsp/viewCard/viewCard.html";
	}
}

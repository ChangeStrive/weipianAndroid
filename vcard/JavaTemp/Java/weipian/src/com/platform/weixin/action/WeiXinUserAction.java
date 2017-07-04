package com.platform.weixin.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinUser;
import com.platform.weixin.service.WeiXinUserService;
import com.platform.weixin.util.WeiXinUtils;


@Controller
@RequestMapping("/WeiXinUserAction")
public class WeiXinUserAction extends BaseAction {
	
	
	@Autowired
	private WeiXinUserService service = null;

	
	@RequestMapping("/list")
	public String list(){
		return "Admin/jsp/weixin/WeiXinUser/userList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){
		return "Admin/jsp/weixin/WeiXinUser/userAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		
		String toUrl = "Admin/jsp/weixin/WeiXinUser/userAdd.html";
		if(StringUtil.isNotNull(fdId)) {
			WeiXinUser item = service.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new WeiXinUser());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WeiXinUser model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		Map<String,String> map=StringUtil.getParams(request, "");
		List<WeiXinUser> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WeiXinUser item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdOpenId", item.getFdOpenId());
				o.put("fdNickName", item.getFdNickName());
				o.put("fdSex", item.getFdSex());
				o.put("fdCity", item.getFdCity());
				o.put("fdCountry", item.getFdCountry());
				o.put("fdProvince", item.getFdProvince());
				o.put("fdHeadImgUrl", item.getFdHeadImgUrl());
				o.put("fdSubscribeTime", item.getFdSubscribeTime());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	@RequestMapping("/getUserList")
	public void getUserList(HttpServletRequest request,HttpServletResponse response) throws ClientProtocolException, IllegalStateException, IOException{
		JSONObject result = new JSONObject();
		try {
			JSONObject token = WeiXinUtils.getAccessToken();
			if(token.containsKey("access_token")) {
				String access_token = token.getString("access_token");
				JSONObject attendPersonList = WeiXinUtils.getAttendPersonList(access_token);
				JSONObject data = attendPersonList.fromObject(attendPersonList.getJSONObject("data"));
				JSONArray array = data.getJSONArray("openid");
				for (int i = 0; i < array.size(); i++) {
					String openid = array.getString(i);
					try {
						JSONObject userInfo = WeiXinUtils.getPersonMessage(access_token,openid);
						if(userInfo.containsKey("openid")) {
							String subscribe = userInfo.getString("subscribe");
							/*
							 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
							 */
							if(subscribe.equals("1")) {
								String fdNickName = userInfo.getString("nickname");
								String fdSex = userInfo.getString("sex");
								String fdCity = userInfo.getString("city");
								String fdCountry = userInfo.getString("country");
								String fdProvince = userInfo.getString("province");
								String fdHeadImgUrl = userInfo.getString("headimgurl");
								String fdSubscribeTime = userInfo.getString("subscribe_time");
								WeiXinUser user = new WeiXinUser();
								user.setFdOpenId(openid);
								user.setFdNickName(fdNickName);
								user.setFdSex(fdSex);
								user.setFdCity(fdCity);
								user.setFdCountry(fdCountry);
								user.setFdProvince(fdProvince);
								user.setFdHeadImgUrl(fdHeadImgUrl);
								user.setFdSubscribeTime(fdSubscribeTime);
								try {
									service.save(user);
								} catch (Exception e) {
									WeiXinUser item = service.getUserByOpenId(openid);
									item.setFdNickName(fdNickName);
									item.setFdSex(fdSex);
									item.setFdCity(fdCity);
									item.setFdCountry(fdCountry);
									item.setFdProvince(fdProvince);
									item.setFdHeadImgUrl(fdHeadImgUrl);
									item.setFdSubscribeTime(fdSubscribeTime);
									service.update(item);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
					
			}
			result.put("success", true);
			result.put("msg", "保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}

	@RequestMapping("/save")
	public void save(WeiXinUser model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				service.save(model);
			}
			result.put("success", true);
			result.put("msg", "保存成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/delete")
	public void delete(String fdId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				service.delete(fdId);
			}
			result.put("success", true);
			result.put("msg", "删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "删除失败!");
		}
		writeJSON(response,result.toString());
	}
}

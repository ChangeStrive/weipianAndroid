package com.platform.weixin.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.context.AppContextImpl;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinConfig;
import com.platform.weixin.model.WeiXinMenu;
import com.platform.weixin.service.WeiXinConfigService;
import com.platform.weixin.service.WeiXinMenuService;
import com.platform.weixin.util.WeiXinMenuItem;
import com.platform.weixin.util.WeiXinUtils;

@Controller
@RequestMapping("/WeiXinMenuAction")
public class WeiXinMenuAction extends BaseAction {
	
	private static Logger logger=LoggerFactory.getLogger(WeiXinMenuAction.class); 
	
	private WeiXinMenu model = new WeiXinMenu();
	
	@Autowired
	private WeiXinMenuService menuSrv = null;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		String fdPid=request.getParameter("fdPid");
		if(!StringUtil.isNotNull(fdPid)){
			fdPid="#";
		}
		request.setAttribute("fdPid", fdPid);
		return "Admin/jsp/weixin/WeiXinMenu/menuList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){
		return "Admin/jsp/weixin/WeiXinMenu/menuAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/weixin/WeiXinMenu/menuAdd.html";
		if(StringUtil.isNotNull(fdId)){
			WeiXinMenu item=menuSrv.get(fdId);
			request.setAttribute("item", item);
			request.setAttribute("fdPid", item.getFdPid());
		}else{
			String fdPid=request.getParameter("fdPid");
			if(!StringUtil.isNotNull(fdPid)){
				fdPid="#";
			}
			request.setAttribute("fdPid", fdPid);
			request.setAttribute("item", new WeiXinMenu());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WeiXinMenu model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WeiXinMenu> list=menuSrv.list(map,model,start, limit);
		int count=menuSrv.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WeiXinMenu item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdName", item.getFdName());
				o.put("fdPid", item.getFdPid());
				o.put("fdPidName", item.getFdPidName());
				o.put("fdKey", item.getFdKey());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdType", item.getFdType());
				o.put("fdSeq", item.getFdSeq());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 获得菜单树
	 * @throws IOException 
	 * @throws IOException
	 */ 
	@RequestMapping("/getMenuTree")
	public void getMenuTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<WeiXinMenu> firstList=menuSrv.getListByLevel("1");
		List<WeiXinMenu> secondList=menuSrv.getListByLevel("2");
		
		JSONObject result=new JSONObject();
		JSONArray tree=new JSONArray();
		Map<String,JSONArray> map=new HashMap();
		
		if(secondList!=null&&secondList.size()>0){
			for(int i=0;i<secondList.size();i++){
				JSONObject item=new JSONObject();
				WeiXinMenu menu=secondList.get(i);
				item.put("fdId", menu.getFdId());
				item.put("fdName", menu.getFdName());
				item.put("fdLevel", "2");
				JSONArray childs=map.get(menu.getFdId());
				if(childs!=null){
					item.put("childs", childs);
				}
				JSONArray array=map.get(menu.getFdPid());
				if(array==null){
					array=new JSONArray();
				}
				array.add(item);
				map.put(menu.getFdPid(), array);
			}
		}
		
		if(firstList!=null&&firstList.size()>0){
			for(int i=0;i<firstList.size();i++){
				JSONObject item=new JSONObject();
				WeiXinMenu menu=firstList.get(i);
				item.put("fdId", menu.getFdId());
				item.put("fdName", menu.getFdName());
				item.put("fdLevel", "1");
				JSONArray childs=map.get(menu.getFdId());
				if(childs!=null){
					item.put("childs", childs);
				}
				tree.add(item);
			}
		}
		result.put("tree", tree);
		result.put("success", true);
		writeJSON(response,result.toString());
	}

	@RequestMapping("/save")
	public void save(WeiXinMenu model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(model.getFdPid().equals("#")) {
				model.setFdLevel(1);
			} else {
				model.setFdLevel(2);
			}
			if(StringUtil.isNotNull(model.getFdId())){
				menuSrv.update(model);
			}else{
				menuSrv.save(model);
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
				menuSrv.delete(fdId);
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

	@RequestMapping("/buildMenu")
	public void buildMenu(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean isMake = makeMenu();
		JSONObject result=new JSONObject();
		
		if(isMake) {
			result.put("success", true);
			result.put("msg", "生成成功!");
		} else{
			result.put("success", false);
			result.put("msg", "生成失败!");
		}
		writeJSON(response,result.toString());
	}
	
	public String getMenuString(String appId) throws UnsupportedEncodingException{
		String baseUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
		
		List<WeiXinMenu> firstList=menuSrv.getListByLevel("1");
		
		List<WeiXinMenuItem> menuArray=new ArrayList();
		
		if(firstList!=null&&firstList.size()>0){
			for(int i=0;i<firstList.size();i++){
				WeiXinMenu menu=firstList.get(i);
				
				List<WeiXinMenu> secondList = menuSrv.getListByPid(menu.getFdId());
				List<WeiXinMenuItem> itemArray=new ArrayList();
				
				if(secondList.size()>0&&secondList!=null){
					for (int j = 0; j < secondList.size(); j++) {
						WeiXinMenu secondMenu = secondList.get(j);
						String fdName = secondMenu.getFdName();
						String fdUrl = secondMenu.getFdUrl();
						String fdType = "view";
						
						String fdKey = secondMenu.getFdKey();
						if(secondMenu.getFdType().equals("0")){
							fdType = "click";
						}else if(secondMenu.getFdType().equals("1")){
							fdType = "view";
							fdKey = null;
						}else if(secondMenu.getFdType().equals("2")){
							fdType = "scancode_waitmsg";
						}else if(secondMenu.getFdType().equals("3")){
							fdType = "scancode_push";
						}
						if(secondMenu.getFdStatus().equals("1")){
							WeiXinMenuItem subItem = new WeiXinMenuItem(fdName,fdType,fdKey,WeiXinUtils.getURL(baseUrl,fdUrl));
							itemArray.add(subItem);
						}else{
							WeiXinMenuItem subItem = new WeiXinMenuItem(fdName,fdType,fdKey,fdUrl);
							itemArray.add(subItem);
						}
					}
					WeiXinMenuItem item=new WeiXinMenuItem(menu.getFdName(),itemArray);
					menuArray.add(item);
				} else {
					String fdType = "view";
					String fdUrl = menu.getFdUrl();
					String fdKey = menu.getFdKey();
					if(menu.getFdType().equals("0")){
						fdType = "click";
					}else{
						fdKey = null;
					}
					if(menu.getFdStatus().equals("1")){
						WeiXinMenuItem subItem = new WeiXinMenuItem(menu.getFdName(),fdType,fdKey,WeiXinUtils.getURL(baseUrl,fdUrl));
						menuArray.add(subItem);
					}else{
						WeiXinMenuItem subItem = new WeiXinMenuItem(menu.getFdName(),fdType,fdKey,fdUrl);
						menuArray.add(subItem);
					}
				}
				
				
			}
		}
		
		WeiXinMenuItem menu=new WeiXinMenuItem(menuArray);
		return menu.toString();
	}
	
	public Boolean delMenu(String appId,String secret,String fdAccessToken){
		try {
			JSONObject object=WeiXinUtils.getAccessToken(appId,secret);
			HttpClient httpclient = new DefaultHttpClient();
		    // 创建Get方法实例   
			String url="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+fdAccessToken;
	        HttpGet httpgets = new HttpGet(url);  
	        HttpResponse response = httpclient.execute(httpgets);  
	        HttpEntity entity = response.getEntity();  
	        if (entity != null) {  
	            InputStream instreams = entity.getContent();
	            String str = WeiXinUtils.convertStreamToString(instreams);
	            instreams.close();
	            System.out.println(str);
	            httpgets.abort(); 
	            JSONObject result = JSONObject.fromObject(str);
	        }
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean makeMenu(){
		try {
			WeiXinConfigService configSrv = AppContextImpl.getInstance().getBean(WeiXinConfigService.class);
			WeiXinConfig config = configSrv.get();
			String appId = config.getFdAppId();
			String secret = config.getFdAppSecret();
			
			String menStr=getMenuString(appId);
			if(!StringUtil.isNotNull(menStr)){
				return delMenu(appId,secret,config.getFdAccessToken());
			}
			HttpClient httpclient = new DefaultHttpClient();
			String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+config.getFdAccessToken();
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			System.out.println(menStr);
			StringEntity s = new StringEntity(menStr,"UTF-8");  
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json;charset=utf-8");  
			httpPost.setEntity(s);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();  
	        if (entity != null) {  
	            InputStream instreams = entity.getContent();  
	            String str = WeiXinUtils.convertStreamToString(instreams);
	            instreams.close();
	            httpPost.abort(); 
	            JSONObject result = JSONObject.fromObject(str);
	           System.out.println(result);
	            if(result.getInt("errcode")==0){
	            	return true;
	            }else{
	            	return false;
	            }
	        }
	        return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

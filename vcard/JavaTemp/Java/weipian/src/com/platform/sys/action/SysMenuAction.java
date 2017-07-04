package com.platform.sys.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.model.SysMenu;
import com.platform.sys.model.SysUser;
import com.platform.sys.service.SysMenuService;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SysMenuAction")
public class SysMenuAction extends BaseAction{

	@Autowired
	private SysMenuService menuSvc;
	
	
	@RequestMapping("/list")
	public String  list(HttpServletRequest request){
		String fdPid=request.getParameter("fdPid");
		if(!StringUtil.isNotNull(fdPid)){
			fdPid="#";
		}
		request.setAttribute("fdPid", fdPid);
		return "Admin/jsp/sys/menu/menuList.html";
	}
	
	@RequestMapping("/edit")
	public String edit(SysMenu model,HttpServletRequest request){
		if(StringUtil.isNotNull(model.getFdId())){
			SysMenu item=menuSvc.get(model.getFdId());
			request.setAttribute("item", item);
			request.setAttribute("fdPid", item.getFdPid());
		}else{
			String fdPid=request.getParameter("fdPid");
			if(!StringUtil.isNotNull(fdPid)){
				fdPid="#";
			}
			request.setAttribute("item", new SysMenu());
			request.setAttribute("fdPid", fdPid);
		}
		return "Admin/jsp/sys/menu/menuAdd.html";
	}
	/**
	 * 获得所有菜单列表
	 * @throws IOException
	 */
	@RequestMapping("/getList")
	public void getList(SysMenu model,HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		List<SysMenu> list=menuSvc.list(map,model,start, limit);
		int count=menuSvc.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysMenu item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdNo", item.getFdNo());
				o.put("fdName", item.getFdName());
				o.put("fdPid", item.getFdPid());
				o.put("fdPidName", item.getFdPidName());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdGroup", item.getFdGroup());
				o.put("fdSeqNo", item.getFdSeqNo());
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
	 * 获得某一级菜单列表
	 * @throws IOException
	 */
	@RequestMapping("/getListByLevel")
	public void getListByLevel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdLevel=request.getParameter("fdLevel");
		List<SysMenu> list=menuSvc.getListByLevel(fdLevel);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysMenu item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdName", item.getFdName());
				o.put("fdPid", item.getFdPid());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdGroup", item.getFdGroup());
				o.put("fdSeqNo", item.getFdSeqNo());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 获得子菜单列表
	 * @throws IOException
	 */
	@RequestMapping("/getListByPid")
	public void getListByPid(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdPid=request.getParameter("fdPid");
		List<SysMenu> list=menuSvc.getListByPid(fdPid);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysMenu item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdName", item.getFdName());
				o.put("fdPid", item.getFdPid());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdGroup", item.getFdGroup());
				o.put("fdSeqNo", item.getFdSeqNo());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
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
		List<SysMenu> firstList=menuSvc.getListByLevel("1");
		List<SysMenu> secondList=menuSvc.getListByLevel("2");
		List<SysMenu> threeList=menuSvc.getListByLevel("3");
		
		JSONObject result=new JSONObject();
		JSONArray tree=new JSONArray();
		Map<String,JSONArray> map=new HashMap();
		
		if(threeList!=null&&threeList.size()>0){
			for(int i=0;i<threeList.size();i++){
				JSONObject item=new JSONObject();
				SysMenu menu=threeList.get(i);
				item.put("fdId", menu.getFdId());
				item.put("fdName", menu.getFdName());
				item.put("fdLevel", "3");
				JSONArray array=map.get(menu.getFdPid());
				if(array==null){
					array=new JSONArray();
				}
				array.add(item);
				map.put(menu.getFdPid(), array);
			}
		}
		
		if(secondList!=null&&secondList.size()>0){
			for(int i=0;i<secondList.size();i++){
				JSONObject item=new JSONObject();
				SysMenu menu=secondList.get(i);
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
				SysMenu menu=firstList.get(i);
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
	public void save(SysMenu model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject result = new JSONObject();
		try {
			if(StringUtil.isNotNull(model.getFdId())) {
				menuSvc.update(model);
			} else {
				menuSvc.save(model);
			}
			result.put("success", true);
			result.put("msg", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败！");
		}
		writeJSON(response,result.toString());
	}
	
	
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				menuSvc.delete(fdId);
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
	
	
	@RequestMapping("/redirect")
	public String redirect(HttpServletRequest request) throws UnsupportedEncodingException{
		String fdNo=request.getParameter("fdNo");
		SysUser user=getCurrentUser(request);
		if(user!=null){
			SysMenu pidMenu=menuSvc.getSysMenuByFdNoAndFdUserId(fdNo,user.getFdId());
			if(pidMenu!=null){
				if(StringUtil.isNotNull(pidMenu.getFdUrl())){
					return "forward:/"+pidMenu.getFdUrl();
				}else{
					if(pidMenu.getFdLevel()==1){
						SysMenu menu=menuSvc.getSecondMenuByUserOfFirst(user.getFdId(),pidMenu.getFdId());
						if(menu!=null&&StringUtil.isNotNull(menu.getFdUrl())){
							return "forward:/"+menu.getFdUrl();
						}
					}
				}
			}
		}else{
			return "redirect:/LoginAction/toLogin?backUrl="+StringUtil.getFormatUrl(request,"SysMenuAction/redirect?fdNo="+fdNo);
		}
		return "Admin/jsp/error404.html";
	}
	/**
	 * 获得1级菜单
	 * @throws IOException
	 */
	@RequestMapping("/getFirstMenuByUser")
	public void getFirstMenuByUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		SysUser user=getCurrentUser(request);
		if(user!=null){
			List<SysMenu> list=menuSvc.getFirstMenuByUser(user.getFdId());
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysMenu item=list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item.getFdId());
					o.put("fdNo", item.getFdNo());
					o.put("fdName", item.getFdName());
					o.put("fdPid", item.getFdPid());
					o.put("fdUrl", item.getFdUrl());
					o.put("fdGroup", item.getFdGroup());
					o.put("fdSeqNo", item.getFdSeqNo());
					o.put("fdStatus", item.getFdStatus());
					array.add(o);
				}
			}
		}
		object.put("list", array);
		object.put("success", true);
		
		writeJSON(response,object.toString());
	}
	
	/**
	 * 获得2级菜单
	 * @throws IOException
	 */
	@RequestMapping("/getSecondMenuByUser")
	public void getSecondMenuByUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		SysUser user=getCurrentUser(request);
		String fdNo=request.getParameter("fdNo");
		if(user!=null){
			SysMenu pidMenu=menuSvc.getSysMenuByFdNo(fdNo);
			String fdPid=pidMenu.getFdId();
			List<SysMenu> list=menuSvc.getSecondMenuByUser(user.getFdId(),fdPid);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysMenu item=list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item.getFdId());
					o.put("fdNo", item.getFdNo());
					o.put("fdName", item.getFdName());
					o.put("fdPid", item.getFdPid());
					o.put("fdUrl", item.getFdUrl());
					o.put("fdGroup", item.getFdGroup());
					o.put("fdSeqNo", item.getFdSeqNo());
					o.put("fdStatus", item.getFdStatus());
					array.add(o);
				}
			}
		}
		object.put("list", array);
		object.put("success", true);
		
		writeJSON(response,object.toString());
	}
	
	/**
	 * 获得3级菜单
	 * @throws IOException
	 */
	@RequestMapping("/getTreeMenuByUser")
	public void getTreeMenuByUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		SysUser user=getCurrentUser(request);
		String fdNo=request.getParameter("fdNo");
		if(user!=null){
			SysMenu pidMenu=menuSvc.getSysMenuByFdNo(fdNo);
			String fdPid=pidMenu.getFdId();
			List<SysMenu> list=menuSvc.getTreeMenuByUser(user.getFdId(),fdPid);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					SysMenu item=list.get(i);
					JSONObject o=new JSONObject();
					o.put("fdId", item.getFdId());
					o.put("fdNo", item.getFdNo());
					o.put("fdName", item.getFdName());
					o.put("fdPid", item.getFdPid());
					o.put("fdUrl", item.getFdUrl());
					o.put("fdGroup", item.getFdGroup());
					o.put("fdSeqNo", item.getFdSeqNo());
					o.put("fdStatus", item.getFdStatus());
					array.add(o);
				}
			}
		}
		object.put("list", array);
		object.put("success", true);
		writeJSON(response,object.toString());
	}
}

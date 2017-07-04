package com.platform.wp.action;

import java.io.IOException;
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
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinMenu;
import com.platform.wp.model.AppGoodsType;
import com.platform.wp.service.AppGoodsTypeService;

@Controller
@RequestMapping("/AppGoodsTypeAction")
public class AppGoodsTypeAction extends BaseAction{
	
	
	@Autowired
	private AppGoodsTypeService service = null;

	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		String fdPid=request.getParameter("fdPid");
		if(!StringUtil.isNotNull(fdPid)){
			fdPid="#";
		}
		request.setAttribute("fdPid", fdPid);
		return "Admin/jsp/app/AppGoodsType/AppGoodsTypeList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(AppGoodsType model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/app/AppGoodsType/AppGoodsTypeAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			AppGoodsType item = service.get(model.getFdId());
			request.setAttribute("item", item);
			request.setAttribute("fdPid", item.getFdPid());
		}else{
			String fdPid=request.getParameter("fdPid");
			if(!StringUtil.isNotNull(fdPid)){
				fdPid="#";
			}
			request.setAttribute("fdPid", fdPid);
			request.setAttribute("item", new AppGoodsType());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(AppGoodsType model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<AppGoodsType> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsType item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdTypeName", item.getFdTypeName());
				o.put("fdSeqNo", item.getFdSeqNo());
				o.put("fdPid", item.getFdPid());
				o.put("fdPidName", item.getFdPidName());
				o.put("fdCreateTime", item.getFdCreateTime());//创建时间
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(AppGoodsType model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(model.getFdPid().equals("#")) {
				model.setFdLevel(1);
			}else {
				model.setFdLevel(2);
			}
			if(StringUtil.isNotNull(model.getFdId())){
				result = service.update(model);
			}else{
				model.setFdCreateTime(DateUtils.getNow());
				result = service.save(model);
			}
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
				service.delete(request,fdId);
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
	
	
	@RequestMapping("/modifStatus")
	public void modifStatus(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String fdId=request.getParameter("fdId");
		String fdStatus=request.getParameter("fdStatus");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				service.modifStatus(fdId,fdStatus);
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
	
	/**
	 * 获得菜单树
	 * @throws IOException 
	 * @throws IOException
	 */ 
	@RequestMapping("/getMenuTree")
	public void getMenuTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<AppGoodsType> firstList=service.getListByLevel("1");
		List<AppGoodsType> secondList=service.getListByLevel("2");
		
		JSONObject result=new JSONObject();
		JSONArray tree=new JSONArray();
		Map<String,JSONArray> map=new HashMap();
		
		if(secondList!=null&&secondList.size()>0){
			for(int i=0;i<secondList.size();i++){
				JSONObject item=new JSONObject();
				AppGoodsType menu=secondList.get(i);
				item.put("fdId", menu.getFdId());
				item.put("fdName", menu.getFdTypeName());
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
				AppGoodsType menu=firstList.get(i);
				item.put("fdId", menu.getFdId());
				item.put("fdName", menu.getFdTypeName());
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
	
}

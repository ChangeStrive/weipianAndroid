package com.platform.weixin.action;

import java.io.IOException;
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
import com.platform.util.StringUtil;
import com.platform.weixin.model.WeiXinKeywordReply;
import com.platform.weixin.service.WeiXinKeywordReplyService;

@Controller
@RequestMapping("/WeiXinKeywordReplyAction")
public class WeiXinKeywordReplyAction extends BaseAction {
	
	
	@Autowired
	private WeiXinKeywordReplyService service = null;

	@RequestMapping("/list")
	public String list(){
		return "Admin/jsp/weixin/WeiXinKeywordReply/keywordReplyList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){
		return "Admin/jsp/weixin/WeiXinKeywordReply/keywordReplyAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		
		String toUrl = "Admin/jsp/weixin/WeiXinKeywordReply/keywordReplyAdd.html";
		if(StringUtil.isNotNull(fdId)) {
			WeiXinKeywordReply item = service.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new WeiXinKeywordReply());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WeiXinKeywordReply model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WeiXinKeywordReply> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WeiXinKeywordReply item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdKeyword", item.getFdKeyword());
				o.put("fdTitle", item.getFdTitle());
				o.put("fdDesc", item.getFdDesc());
				o.put("fdType", item.getFdType());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(WeiXinKeywordReply model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
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

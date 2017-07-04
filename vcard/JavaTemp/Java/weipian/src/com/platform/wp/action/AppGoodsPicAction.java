package com.platform.wp.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppGoodsPic;
import com.platform.wp.service.AppGoodsPicService;

@Controller
@RequestMapping("/AppGoodsPicAction")
public class AppGoodsPicAction extends BaseAction{
	
	
	@Autowired
	private AppGoodsPicService service = null;
	
	
	@RequestMapping("/getList")
	public void getList(AppGoodsPic model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String fdGoodsId=request.getParameter("fdGoodsId");
		List<AppGoodsPic> list=service.list(fdGoodsId);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppGoodsPic item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdSeqNo", item.getFdSeqNo());
				array.add(o);
			}
		}
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/updateFdSeqNo")
	public void updateFdSeqNo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			
			String fdId=request.getParameter("fdId");
			String fdSeqNo=request.getParameter("fdSeqNo");
			service.updateFdSeqNo(fdId,fdSeqNo);
			result.put("success", true);
			result.put("msg", "操作成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/save")
	public void save(AppGoodsPic model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			String fdGoodsId=request.getParameter("fdGoodsId");
			String imageUrls=request.getParameter("imageUrls");
			service.save(fdGoodsId,imageUrls);
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

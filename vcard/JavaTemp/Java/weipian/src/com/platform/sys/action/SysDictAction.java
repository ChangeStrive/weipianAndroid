package com.platform.sys.action;

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
import com.platform.sys.model.SysDict;
import com.platform.sys.service.SysDictService;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SysDictAction")
public class SysDictAction extends BaseAction {
	
	
	@Autowired
	private SysDictService dictSrv = null;

	@RequestMapping("/list")
	public String list(){
		return "Admin/jsp/sys/dict/dictList.html";
	}

	
	@RequestMapping("/userList")
	public String userList(HttpServletRequest request,HttpServletResponse response){
		String node=request.getParameter("node");
		String firstMenuNo=request.getParameter("firstMenuNo");
		String secondMenuNo=request.getParameter("secondMenuNo");
		request.setAttribute("firstMenuNo", firstMenuNo);
		request.setAttribute("secondMenuNo", secondMenuNo);
		request.setAttribute("node", node);
		return "Admin/jsp/sys/userDict/userDictList.html";
	}
	
	@RequestMapping("/getList")
	public void getList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String id = request.getParameter("node");
		List<SysDict> list = dictSrv.getListByParentId(id);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject o = new JSONObject();
			SysDict item = list.get(i);
			
			int count = this.dictSrv.selectCount(item);
			o.put("id", item.getFdId());
			o.put("pid", item.getFdFunId());
			o.put("text", item.getFdName());
			if(count!=0)
				o.put("state", "closed");
			o.put("fdFunName", item.getFdFunName());
			o.put("fdValue", item.getFdValue());
			o.put("fdSeqNo", item.getFdSeqNo());
			array.add(o);
		}
		
		writeJSON(response,array.toString());
	}
	
	@RequestMapping("/getListByParentId")
	public void getListByParentId(String fdId,HttpServletResponse response) throws Exception{
		List<SysDict> dictList = dictSrv.getListByParentId(fdId);
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		if(dictList!=null&&dictList.size()>0){
			for(int i=0;i<dictList.size();i++){
				SysDict item=dictList.get(i);
				JSONObject o=new JSONObject();
				o.put("id",item.getFdId());
				o.put("text",item.getFdName());
				o.put("value",item.getFdValue());
				array.add(o);
			}
		}
		result.put("list", array);
		result.put("success", true);
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/getChild")
	public JSONArray getChild(String pid){
		List<SysDict> dictList = dictSrv.getListByParentId(pid);
		if(dictList==null){
			return null;
		}
		JSONArray array=new JSONArray();
		if(dictList!=null&&dictList.size()>0){
			for(int i=0;i<dictList.size();i++){
				SysDict item=dictList.get(i);
				JSONObject o=new JSONObject();
				o.put("id",item.getFdId());
				o.put("text",item.getFdName());
				o.put("value",item.getFdValue());
				JSONArray childs=getChild(item.getFdId());
				if(childs!=null){
					o.put("childs", childs);
				}
				array.add(o);
			}
		}
		return array;
	}
	
	/**
	 * 获得字典树
	 * @throws IOException
	 */
	@RequestMapping("/getDictTree")
	public void getDictTree(String fdId,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		JSONArray array=getChild(fdId);
		if(array!=null){
			result.put("list", array);
		}
		result.put("success", true);
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/save")
	public void save(SysDict model,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				dictSrv.update(model);
			}else{
				dictSrv.save(model);
			}
			result.put("node", model);
			result.put("success", true);
			result.put("msg", "操作成功!");
		}catch(Exception e){
			result.put("success", false);
			result.put("msg", "操作失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/delete")
	public void delete(String fdId,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			dictSrv.delete(fdId);
			result.put("success", true);
			result.put("msg", "操作成功!");
		}catch(Exception e){
			result.put("success", false);
			result.put("msg", "操作失败!");
		}
		writeJSON(response,result.toString());
	}
}

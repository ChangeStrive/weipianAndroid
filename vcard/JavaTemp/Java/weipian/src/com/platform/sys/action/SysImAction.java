package com.platform.sys.action;

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
import com.platform.sys.model.SysUser;
import com.platform.sys.service.SysUserService;
import com.platform.util.StringUtil;


@Controller
@RequestMapping("/SysImAction")
public class SysImAction extends BaseAction {

	@Autowired
	private SysUserService userSrv;
	
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request){
		String toUrl = "Admin/jsp/sys/user/userAdd.html";
		if(StringUtil.isNotNull(fdId)) {
			SysUser item = userSrv.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new SysUser());
		}
		return toUrl;
	}
	
	@RequestMapping("/list")
	public String  list(){
		return "Admin/jsp/sys/user/userList.html";
	}
	
	@RequestMapping("/toChangePassword")
	public String  toChangePassword(){
		return "Admin/jsp/sys/user/changePassWord.html";
	}
	
	@RequestMapping("/getList")
	public void getList(SysUser model,HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		List<SysUser> list=userSrv.list(map,model,start, limit);
		int count=userSrv.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysUser item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdName", item.getFdName());
				o.put("fdLoginName", item.getFdLoginName());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdLastTime", item.getFdLastTime());
				o.put("fdLoginIp", item.getFdLoginIp());
				o.put("fdIsAdmin", item.getFdIsAdmin());
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
	public void save(SysUser model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject result = new JSONObject();
		String fdLoginName = model.getFdLoginName();
		try {
			if(StringUtil.isNotNull(model.getFdId())) {
				userSrv.update(model);
				result.put("success", true);
				result.put("msg", "保存成功！");
			} else {
				if(userSrv.getSysUserByFdLoginName(fdLoginName) == null) {
					userSrv.save(model);
					result.put("success", true);
					result.put("msg", "保存成功！");
				} else {
					result.put("success", false);
					result.put("msg", "用户名已存在！");
				}
			}
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
				userSrv.delete(fdId);
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
				userSrv.modifStatus(fdId,fdStatus);
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
	
	@RequestMapping("/changePassWord")
	public void changePassWord(HttpServletRequest request,HttpServletResponse response) throws IOException{
		SysUser user=getCurrentUser(request);
		String oldPassWord=request.getParameter("oldPassWord");
		String newPassWord=request.getParameter("newPassWord");
		JSONObject result=new JSONObject();
		try{
			if(user!=null){
				if(user.getFdPwd().equals(oldPassWord)){
					userSrv.changePassWord(user.getFdId(),newPassWord);
					result.put("success", true);
					result.put("msg", "修改成功");
				}else{
					result.put("success", false);
					result.put("msg", "旧密码不正确");
				}
			}else{
				result.put("success", false);
				result.put("msg", "未登录,请重新登录在修改");
			}
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "修改失败");
		}
		writeJSON(response,result.toString());
	}
}

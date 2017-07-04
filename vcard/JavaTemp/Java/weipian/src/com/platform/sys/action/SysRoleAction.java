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
import com.platform.sys.model.SysRole;
import com.platform.sys.model.SysUser;
import com.platform.sys.service.SysRoleService;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SysRoleAction")
public class SysRoleAction extends BaseAction{

	@Autowired
	private SysRoleService roleSvc;
	
	
	@RequestMapping("/list")
	public String  list(){
		return "Admin/jsp/sys/role/roleList.html";
	}
	
	/**
	 * 访问授权
	 * @return
	 */
	@RequestMapping("/roleMenulist")
	public String  roleMenulist(HttpServletRequest request,HttpServletResponse response){
		String fdId=request.getParameter("fdId");
		request.setAttribute("fdId", fdId);
		return "Admin/jsp/sys/role/roleMenu.html";
	}
	
	
	/**
	 *  成员授权
	 * @return
	 */
	@RequestMapping("/roleUserlist")
	public String  roleUserlist(HttpServletRequest request,HttpServletResponse response){
		String fdId=request.getParameter("fdId");
		request.setAttribute("fdId", fdId);
		return "Admin/jsp/sys/role/roleUser.html";
	}
	
	@RequestMapping("/edit")
	public String edit(SysRole model,HttpServletRequest request,HttpServletResponse response){
		if(StringUtil.isNotNull(model.getFdId())){
			SysRole item=roleSvc.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new SysRole());
		}
		return "Admin/jsp/sys/role/roleAdd.html";
	}
	
	@RequestMapping("/getList")
	public void getList(SysRole model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "");
		List<SysRole> list=roleSvc.list(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysRole item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdName", item.getFdName());
				o.put("fdDesc", item.getFdDesc());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 获得授权成员
	 * @throws IOException
	 */
	@RequestMapping("/getRoleUserList")
	public void getRoleUserList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		List<SysUser> list=roleSvc.getRoleUserList(fdId);
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
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	@RequestMapping("/save")
	public void save(SysRole model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject result = new JSONObject();
		try {
			if(StringUtil.isNotNull(model.getFdId())) {
				roleSvc.update(model);
			} else {
				roleSvc.save(model);
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
	
	/**
	 * 保存菜单
	 * @throws IOException
	 */
	@RequestMapping("/saveMenu")
	public void saveMenu(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		String fdMenuIds=request.getParameter("fdMenuIds");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				roleSvc.saveMenu(fdId,fdMenuIds);
			}
			result.put("success", true);
			result.put("msg", "操作成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败!");
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 获得所有权限菜单id
	 * @throws IOException 
	 */
	@RequestMapping("/getMenuByRoleId")
	public void getMenuByRoleId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		JSONObject result=new JSONObject();
		JSONArray array=new JSONArray();
		if(StringUtil.isNotNull(fdId)){
			List list=roleSvc.getMenuByRoleId(fdId);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					String str=(String) list.get(i);
					JSONObject object=new JSONObject();
					object.put("fdMenuId", str);
					array.add(object);
				}
			}
		}
		result.put("success", true);
		result.put("list", array);
		writeJSON(response,result.toString());
	}
	/**
	 * 保存用户
	 * @throws IOException
	 */
	@RequestMapping("/saveUser")
	public void saveUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		String fdLoginNames=request.getParameter("fdLoginNames");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				roleSvc.saveUser(fdId,fdLoginNames);
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
	
	@RequestMapping("/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				roleSvc.delete(fdId);
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
	
	/**
	 * 删除授权用户
	 * @throws IOException
	 */
	@RequestMapping("/deleteUser")
	public void deleteUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String fdId=request.getParameter("fdId");
		String fdUserIds=request.getParameter("fdUserIds");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)&&StringUtil.isNotNull(fdUserIds)){
				roleSvc.deleteUser(fdId,fdUserIds);
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

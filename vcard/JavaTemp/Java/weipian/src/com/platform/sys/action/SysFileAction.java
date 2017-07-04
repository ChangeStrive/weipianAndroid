package com.platform.sys.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.util.FileUtil;
import com.platform.util.PropertiesUtil;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SysFileAction")
public class SysFileAction extends BaseAction{

	/**
	 * 下载文件
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/downFile")
	public void downFile(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String path=request.getParameter("path");
		if(StringUtil.isNotNull(path)){
			String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
			String folderRoot="";
			if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
				folderRoot=request.getSession().getServletContext().getRealPath("/");
			}else{
				folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
			}
			String paths=folderRoot+path;
			 if(System.getProperty("file.separator").equals("\\")){
		    	//windows
		    	paths = paths.replaceAll("/", "\\\\");
		    }else{
		    	//linux
		    	paths = paths.replaceAll("\\\\", "/");
		    }
			FileUtil.downloadFile(response, paths);
		}
	}
	
	
	/**
	 * 删除文件
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/delFile")
	public void delFile(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		String path=request.getParameter("path");
		if(StringUtil.isNotNull(path)){
			String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
			String folderRoot="";
			if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
				folderRoot=request.getSession().getServletContext().getRealPath("/");
			}else{
				folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
			}
			try {
				FileUtil.deleteFile(folderRoot+path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			Map<String,String> map=FileUtil.uploadFile(request);
			result.put("path",map.get("path"));
			result.put("oldFileName",map.get("oldFileName"));
			result.put("newFileName",map.get("newFileName"));
			result.put("success",map.get("success"));
			result.put("msg",map.get("msg"));
			
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/cutImage")
	public void cutImage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			String imageUrl=request.getParameter("imageUrl");
			String x=request.getParameter("x");
			String y=request.getParameter("y");
			String bx=request.getParameter("bx");
			String by=request.getParameter("by");
			String h=request.getParameter("h");
			String w=request.getParameter("w");
			String savePath=request.getParameter("savePath");
			Map<String,String> map=FileUtil.cutImage(request,imageUrl,savePath,Double.valueOf(x),Double.valueOf(y),Double.valueOf(w),Double.valueOf(h),Double.valueOf(bx),Double.valueOf(by));
			result.put("path",map.get("path"));
			result.put("oldFileName",map.get("oldFileName"));
			result.put("newFileName",map.get("newFileName"));
			result.put("success", true);
			result.put("msg", "保存成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
}

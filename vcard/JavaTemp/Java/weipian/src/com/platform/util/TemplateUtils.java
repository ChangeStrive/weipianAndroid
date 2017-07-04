package com.platform.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.platform.sys.context.AppContextImpl;
import com.platform.web.model.WebConfig;
import com.platform.web.model.WebMenu;
import com.platform.web.service.WebConfigService;
import com.platform.web.service.WebMenuService;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtils {

	public static String getBaseUrl(HttpServletRequest request){
		String urls=request.getContextPath()+"/";
		return urls;
	}
	/**
	 * 获得模板
	 * @param request
	 * @param folder
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Template getTemplate(HttpServletRequest request,String fileName) throws IOException{
		Configuration cfg=new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(request.getRealPath("/")));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("utf-8");
		Template temp=cfg.getTemplate(fileName);
		return temp;
	}
	
	/**
	 * 生成静态html
	 * @param request
	 * @param root
	 * @param folder
	 * @param fileName
	 * @param savePath
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public static void saveHtml(HttpServletRequest request,Map root,String fileName,String savePath) throws IOException, TemplateException{
		Template temp=getTemplate(request,fileName);
		File file=new File(savePath);
		OutputStream o=null;
		Writer out=null;
		try{
			o=new FileOutputStream(file);
			out=new OutputStreamWriter(o,"utf-8");
			temp.process(root, out);
			temp.setEncoding("utf-8");
			out.flush();
			out.close();
			o.close();
		}catch (Exception e) {
			// TODO: handle exception
			if(out!=null){
				out.close();
			}
			if(o!=null){
				o.close();
			}
		}
	}
	/**
	 * 获得网站配置
	 * @return
	 */
	public static WebConfig getWebConfig(){
		WebConfigService service =(WebConfigService) AppContextImpl.getInstance().getBean(WebConfigService.class);
		WebConfig webConfig=service.get();
		return webConfig;
	}
	
	public static Map<String,List<WebMenu>> getWebMenuList(){
		WebMenuService menuSvc =(WebMenuService) AppContextImpl.getInstance().getBean(WebMenuService.class);
		List<WebMenu> menuList=menuSvc.list();
		if(menuList!=null&&menuList.size()>0){
			Map<String,List<WebMenu>> map=new HashMap();
			for(WebMenu menu:menuList){
				String pid=menu.getFdPid();
				List<WebMenu> list=null;
				if(map.containsKey(pid)){
					list=map.get(pid);
				}else{
					list=new ArrayList();
				}
				list.add(menu);
				map.put(pid, list);
			}
			return map;
		}
		return null;
	}
	
	
	
}

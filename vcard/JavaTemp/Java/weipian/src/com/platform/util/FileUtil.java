package com.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUtil {

	  
    /**
	 * 下载文件
	 * 
	 * @param filePath
	 *            要下载的文件绝对路径
	 * @return
	 */
	public static void downloadFile(HttpServletResponse response,String filePath) {
		
		File file = new File(filePath);
		if (!file.exists()) {
			// 文件不存在
		} else {
			long fileLen = file.length();
			response.reset();
			response.addHeader("Content-Disposition",
					"attachment; filename=\"" + filePath + "\"");
			response.addHeader("Content-Length", fileLen + "");
			response.setHeader("Accept-Ranges", "bytes");
			response.setContentType("application/octet-stream");
			ServletOutputStream sos = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(filePath);
				int buf = 4096;
				byte buffer[] = new byte[buf];
				sos = response.getOutputStream();
				int i;
				while ((i = fis.read(buffer)) != -1) {
					sos.write(buffer, 0, i);
					response.flushBuffer();
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (sos != null) {
					try {
						sos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param filePath
	 *            要下载的文件绝对路径
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String downloadFile(HttpServletResponse response,String filePath,String fileName) throws UnsupportedEncodingException {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		} else {
			long fileLen = file.length();
			response.reset();
			response.setContentType("application/x-download");
		    fileName = URLDecoder.decode(fileName, "utf-8");
		    fileName= java.net.URLEncoder.encode(fileName,"utf-8");
		    response.setHeader("Content-Disposition", "attachment; filename=\""+ new String((fileName).getBytes(),"UTF-8") +"\"");
			ServletOutputStream sos = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(filePath);
				int buf = 4096;
				byte buffer[] = new byte[buf];
				sos = response.getOutputStream();
				int i;
				while ((i = fis.read(buffer)) != -1) {
					sos.write(buffer, 0, i);
					response.flushBuffer();
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (sos != null) {
					try {
						sos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 保存缩列图
	 * @param path 相对路径
	 * @param savePath 相对路径
	 * @param w 宽
	 * @param h 高
	 * @return
	 * @throws IOException
	 */
	public static Map zoomFile(HttpServletRequest request,String path,String savePath,int w,int h) throws IOException{
		Map<String,String> result=new HashMap();
		String folderRoot=FileUtil.getRootPath(request);
		
		File file=new File(folderRoot+path);
		File dir=new File(folderRoot+savePath);
		String s=file.getName();
		String newFileName=s.substring(0,s.lastIndexOf("."))+"_"+s.substring(s.lastIndexOf("."),s.length());
		if(!dir.exists()) dir.mkdirs();
		ImageZoomUtil z=new ImageZoomUtil(file,folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		z.resize(w, h);
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}
	

	/**
	 * 保存缩列图
	 * @param path 相对路径
	 * @param savePath 相对路径
	 * @param w 宽
	 * @param h 高
	 * @return
	 * @throws IOException
	 */
	public static Map cutChatImage(HttpServletRequest request,String path,String savePath,int w,int h) throws IOException{
		Map<String,String> result=new HashMap();
		String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
		String folderRoot="";
		
		if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
			//保存在项目路径下
			folderRoot=request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
		}else{
			//保存在其他盘下
			folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
		}
		
		File file=new File(folderRoot+path);
		File dir=new File(folderRoot+savePath);
		String s=file.getName();
		String newFileName=s.substring(0,s.lastIndexOf("."))+"_"+s.substring(s.lastIndexOf("."),s.length());
		if(!dir.exists()) dir.mkdirs();
		ImgCutUtil.cut(w, h, folderRoot+path,folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}
	
	
	/**
	 * 保存缩列图
	 * @param path 相对路径
	 * @param savePath 相对路径
	 * @param w 宽
	 * @param h 高
	 * @return
	 * @throws IOException
	 */
	public static Map cutSquareImage(HttpServletRequest request,String path,String savePath) throws IOException{
		Map<String,String> result=new HashMap();
		String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
		String folderRoot="";
		
		if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
			//保存在项目路径下
			folderRoot=request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
		}else{
			//保存在其他盘下
			folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
		}
		
		File file=new File(folderRoot+path);
		File dir=new File(folderRoot+savePath);
		String s=file.getName();
		String newFileName=s.substring(0,s.lastIndexOf("."))+"_"+s.substring(s.lastIndexOf("."),s.length());
		if(!dir.exists()) dir.mkdirs();
		ImgCutUtil.cutSquareImage( folderRoot+path,folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}
	
	public static Map<String,Map<String,String>> uploadAllFile(HttpServletRequest request,String savePath){
		Map<String,Map<String,String>> map=new HashMap();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					String fieldName=(String) iter.next();
					MultipartFile file = multiRequest.getFile(fieldName);
					if (file != null) {
						Map<String,String> result=new HashMap();
						String fileName= file.getOriginalFilename();
						result.put("oldFileName", fileName);
						String newFileName=IDGenerator.generateID()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
						result.put("newFileName", newFileName);
						if (!StringUtil.isNotNull(savePath)) {
							savePath="allfile";
						}
						//request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
						String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
						String folderRoot="";
						if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
							//保存在项目路径下
							folderRoot=request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
						}else{
							//保存在其他盘下
							folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
						}
						String filePath=folderRoot+savePath;
						File newfolder = new File(filePath);
						if(!newfolder.exists()) {
							newfolder.mkdirs();
						}
						
						/** 保存图片的相对路径 */
						File temp=new File(filePath+System.getProperty("file.separator")+newFileName);
						file.transferTo(temp);
						result.put("path",savePath+System.getProperty("file.separator")+newFileName);
						result.put("absolutePath",filePath+System.getProperty("file.separator")+newFileName);
						map.put(fileName, result);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return map;
	}
	
	public static Map uploadFile(HttpServletRequest request,String savePath){
		Map<String,String> result=new HashMap();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile((String) iter
							.next());
					if (file != null) {
						String fileName= file.getOriginalFilename();
						result.put("oldFileName", fileName);
						String newFileName=IDGenerator.generateID()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
						result.put("newFileName", newFileName);
						if (!StringUtil.isNotNull(savePath)) {
							savePath="allfile";
						}
						//request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
						String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
						String folderRoot="";
						if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
							//保存在项目路径下
							folderRoot=request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
						}else{
							//保存在其他盘下
							folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
						}
						String filePath=folderRoot+savePath;
						File newfolder = new File(filePath);
						if(!newfolder.exists()) {
							newfolder.mkdirs();
						}
						
						/** 保存图片的相对路径 */
						File temp=new File(filePath+System.getProperty("file.separator")+newFileName);
						file.transferTo(temp);
						result.put("path",savePath+"/"+newFileName);
						result.put("absolutePath",filePath+System.getProperty("file.separator")+newFileName);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return result;
	}
	
	public static Map uploadFile(HttpServletRequest request){
		Map result=new HashMap();
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver(request.getSession().getServletContext());
		String savePath=request.getParameter("savePath");
		String maxSize=request.getParameter("maxSize");
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile((String) iter
							.next());
					if (file != null) {
						if(StringUtil.isNotNull(maxSize)){
							if(file.getSize()>(Integer.parseInt(maxSize)*1024)) {    
								result.put("success", false);
								result.put("msg", "上传大小不能超过"+maxSize+"k");  
								return result;
				            } 
						}
						String fileName= file.getOriginalFilename();
						result.put("oldFileName", fileName);
						String newFileName=IDGenerator.generateID()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
						result.put("newFileName", newFileName);
						if (!StringUtil.isNotNull(savePath)) {
							savePath="allfile";
						}
						//request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
						String folderRoot=FileUtil.getRootPath(request);
						String filePath=folderRoot+"upload"+System.getProperty("file.separator")+savePath;
						File newfolder = new File(filePath);
						if(!newfolder.exists()) {
							newfolder.mkdirs();
						}
						
						/** 保存图片的相对路径 */
						File temp=new File(filePath+System.getProperty("file.separator")+newFileName);
						file.transferTo(temp);
						result.put("path","upload"+"/"+savePath+"/"+newFileName);
						result.put("absolutePath",filePath+System.getProperty("file.separator")+newFileName);
						result.put("success", true);
						result.put("msg", "保存成功!");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return result;
	}

	/**
	 * 删除目录，包括其下的所有子目录和文件
	 * 
	 * @param dir
	 *            被删除的目录名
	 * @return boolean 是否删除成功
	 * @throws Exception
	 *             删除目录过程中的任何异常
	 */
	public static boolean deleteDir(File dir) throws Exception {
		if (dir.isFile()) {
			deleteFile(dir);
		}

		File[] files = dir.listFiles();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];

				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}

		return dir.delete();
	}
	
	
	/**
	 * 删除目录，包括其下的所有子目录和文件
	 * 
	 * @param dir
	 *            被删除的目录名
	 * @return boolean 是否删除成功
	 * @throws Exception
	 *             删除目录过程中的任何异常
	 */
	public static boolean deleteDir(String dir) throws Exception {
		return deleteDir(dir);
	}
	/**
	 * 删除文件
	 * 
	 * @param file
	 *            被删除文件
	 * @return boolean 是否删除成功
	 * @throws Exception
	 *             删除文件过程中的任何异常
	 */
	public static boolean deleteFile(File file) throws Exception {
		if (file.isDirectory()) {
			return deleteDir(file);
		}

		if (!file.exists()) {
			return false;
		}
		 FileUtils.forceDelete(file);
		 return true;
	}
	
	
	
	/**
	 * 删除文件（如果是目录，删除整个目录）
	 * 
	 * @param file
	 *            被删除文件
	 * @return boolean 是否删除成功
	 * @throws Exception
	 *             删除文件过程中的任何异常
	 */
	public static boolean deleteFile(String path) throws Exception {
		
		return deleteFile(new File(path));
	}
	
	public static boolean deleteTomcatFile(HttpServletRequest request,String path) throws Exception{
		 path=request.getRealPath(System.getProperty("file.separator"))+path;
		 return deleteFile(new File(path));
	}
	
	
	/**
	 * 文件移动
	 * @param input
	 * @param output
	 * @throws Exception
	 */
	public static void move(String input, String output) throws Exception {
		File inputFile = new File(input);
		File outputFile = new File(output);
		try {
			inputFile.renameTo(outputFile);
		} catch (Exception ex) {
			throw new Exception("Can not mv" + input + " to " + output
					+ ex.getMessage());
		}
	}

	public static Map<String, String> moveFile(HttpServletRequest request,
			String source, String savePath) {
		// TODO Auto-generated method stub
		Map<String,String> result=new HashMap();
		String folderRoot=FileUtil.getRootPath(request);
		
		File file=new File(folderRoot+source);
		File dir=new File(folderRoot+savePath);
		String s=file.getName();
		String newFileName=s.substring(0,s.lastIndexOf("."))+s.substring(s.lastIndexOf("."),s.length());
		if(!dir.exists()) dir.mkdirs();
		File newFile=new File(folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		file.renameTo(newFile);
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}

	/**
	 * 图片裁剪
	 * @param request
	 * @param imageUrl 图片地址
	 * @param savePath 图片保存地址
	 * @param x 开始坐标
	 * @param y 开始坐标
	 * @param w 宽度
	 * @param h 宽度
	 * @param bx 图片宽度
	 * @param by 图片高度
	 * @return
	 */
	public static Map<String, String> cutImage(HttpServletRequest request,String imageUrl,
			String savePath, double x, double y, double w, double h, double bx, double by) {
		// TODO Auto-generated method stub
		Map<String,String> result=new HashMap();
		String folderRoot=FileUtil.getRootPath(request);
		
		File dir=new File(folderRoot+savePath);
		String newFileName=IDGenerator.generateID()+imageUrl.substring(imageUrl.lastIndexOf("."),imageUrl.length());
		if(!dir.exists()) dir.mkdirs();
		
		String saveFile=folderRoot+savePath+System.getProperty("file.separator")+newFileName;
		ImgCutUtil.cut(imageUrl, saveFile, x, y, w, h,bx,by);
		
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}

	

	public static String getRootPath(HttpServletRequest request){
		String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
		String folderRoot="";
		
		if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
			//保存在项目路径下
			folderRoot=request.getSession().getServletContext().getRealPath(System.getProperty("file.separator"));
		}else{
			//保存在其他盘下
			folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
		}
		return folderRoot;
	}
	/**
	 * 下载网络图片
	 * @param request
	 * @param imageUrl
	 * @param savePath
	 * @return
	 */
	public static Map<String, String> downUrlImage(HttpServletRequest request,String imageUrl,
			String savePath) {
		// TODO Auto-generated method stub
		Map<String,String> result=new HashMap();
		String folderRoot=FileUtil.getRootPath(request);
		
		File dir=new File(folderRoot+savePath);
		String newFileName=IDGenerator.generateID()+".jpg";
		if(!dir.exists()) dir.mkdirs();
		
		String saveFile=folderRoot+savePath+System.getProperty("file.separator")+newFileName;
		ImageUtils.downloadImage(imageUrl, saveFile);
		
		result.put("path",savePath+System.getProperty("file.separator")+newFileName);
		result.put("absolutePath",folderRoot+savePath+System.getProperty("file.separator")+newFileName);
		return result;
	}
	
}

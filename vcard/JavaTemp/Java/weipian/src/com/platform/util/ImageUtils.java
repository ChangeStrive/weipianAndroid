package com.platform.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;

public class ImageUtils {

	public static  Map<String,Integer> getSysImageMessage(HttpServletRequest request,String sourcePath){
		String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
		String folderRoot="";
		
		if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
			//保存在项目路径下
			folderRoot=request.getSession().getServletContext().getRealPath("/");
		}else{
			//保存在其他盘下
			folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
		}
		
		String path=folderRoot+sourcePath;
		return getImageMessage(path);
	}
	
	public static  Map<String,Integer> getImageMessage(String sourcePath){
		 Map<String,Integer> result=new HashMap(0);
		 FileInputStream is = null;
		 ImageInputStream iis = null;
		try {
			is = new FileInputStream(sourcePath);
			String fileSuffix = sourcePath.substring(sourcePath
					.lastIndexOf(".") + 1);
			Iterator<ImageReader> it = ImageIO
					.getImageReadersByFormatName(fileSuffix);
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			BufferedImage oi = reader.read(0);
			result.put("width", oi.getWidth());
			result.put("height", oi.getHeight());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				iis = null;
			}
		}
		 return result;
	}
	
	/**
	 * 从网络上进行下载图片，并保存到本地
	 * 
	 * @param fromUrl
	 *            从哪下载的图片的地址
	 * @param toPath
	 *            保存的位置
	 * @return true:下载保存成功; false:下载保存失败
	 * @author Saindy Su
	 */
	public static boolean downloadImage(String fromUrl, String toPath) {
		try {
			URL url = new URL(fromUrl);
			File outFile = new File(toPath);
			OutputStream os = new FileOutputStream(outFile);
			InputStream is = url.openStream();
			byte[] buff = new byte[1024];
			while (true) {
				int readed = is.read(buff);
				if (readed == -1) {
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);
				os.write(temp);
			}
			is.close();
			os.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 二进制图片保存(相对路径)
	 * @param image
	 * @param savePath
	 * @return
	 */
	public static String GenerateImageSys(HttpServletRequest request,String image,String savePath){
		String filename=IDGenerator.generateID();
		String folderRoot=FileUtil.getRootPath(request);
		String filePath=folderRoot+savePath;
		File newfolder = new File(filePath);
		if(!newfolder.exists()) {
			newfolder.mkdirs();
		}
		return GenerateImage(image,filename,filePath);
	}
	
	/**
	 * 二进制图片保存
	 * @param image
	 * @param filename
	 * @param savePath
	 * @return
	 */
	public static String GenerateImage(String image,String filename,String savePath) {   //对字节数组字符串进行Base64解码并生成图片  
		 // 只允许image  
       String header ="data:image";  
       String[] imageArr=image.split(",");  
       if(imageArr[0].contains(header)){//是img的  
	      // 去掉头部  
       	String type=imageArr[0].split("/")[1].split(";")[0];
	        image=imageArr[1];  
	        //image = image.substring(header.length());  
	        // 写入磁盘  
	        String success = "fail";  
	        BASE64Decoder decoder = new BASE64Decoder();  
	        File sf=new File(savePath);
		 	   if(!sf.exists()){
		 		   sf.mkdirs();
		 	   }
	        try{  
	                byte[] decodedBytes = decoder.decodeBuffer(image);        //将字符串格式的image转为二进制流（biye[])的decodedBytes  
	                FileOutputStream out = new FileOutputStream(sf.getPath()+"\\"+filename+"."+type);        //新建一个文件输出器，并为它指定输出位置imgFilePath  
	                out.write(decodedBytes); //利用文件输出器将二进制格式decodedBytes输出  
	                out.close();                        //关闭文件输出器  
	                success = "上传文件成功！";  
	                System.out.println("上传文件成功！");  
	                return filename+"."+type;
	          
	        }catch(Exception e){  
	        }  
       }
		return "";
   }  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

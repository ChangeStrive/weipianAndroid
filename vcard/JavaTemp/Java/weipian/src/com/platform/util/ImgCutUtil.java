package com.platform.util;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImgCutUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * 图片裁切
	 * @param x1 宽的比例
	 * @param y1 高的比例
	 * @param width 选择区域的宽度
	 * @param height 选择区域的高度
	 * @param sourcePath 源图片路径
	 * @param descpath 裁切后图片的保存路径
	 */
	public static void cut(int x1, int y1,
			String sourcePath, String descpath) {

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
			
			BigDecimal b=new BigDecimal(y1).divide(new BigDecimal(x1),2,BigDecimal.ROUND_HALF_UP);
			int ox=oi.getWidth();
			int oh=oi.getHeight();
			int width=0;
			int height=0;
			BigDecimal s=new BigDecimal(ox).multiply(b);
			if(s.intValue()<oh){
				width=ox;
				height=s.intValue();
			}else{
				width=ox;
				height=oh;
			}
			Rectangle rect = new Rectangle(0,0, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, fileSuffix, new File(descpath));
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

	}

	/**
	 * 图片裁切
	 * @param x1 宽的比例
	 * @param y1 高的比例
	 * @param width 选择区域的宽度
	 * @param height 选择区域的高度
	 * @param sourcePath 源图片路径
	 * @param descpath 裁切后图片的保存路径
	 */
	public static void cutSquareImage(String sourcePath, String descpath) {

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
			
			int ox=oi.getWidth();
			int oh=oi.getHeight();
			int width=0;
			int height=0;
			if(ox<oh){
				width=ox;
				height=ox;
			}else{
				width=oh;
				height=oh;
			}
			Rectangle rect = new Rectangle(0,0, width, height);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, fileSuffix, new File(descpath));
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
	}
	
	/**
	 * 图片裁切
	 * @param x1 宽的比例
	 * @param y1 高的比例
	 * @param width 选择区域的宽度
	 * @param height 选择区域的高度
	 * @param sourcePath 源图片路径
	 * @param descpath 裁切后图片的保存路径
	 */
	public static void cut(String imageUrl,String savePath,Double x,Double y,Double w,Double h,Double bx, Double by) {

		InputStream is = null;
		ImageInputStream iis = null;
		try {
			URL  url = new URL(imageUrl);  
			// 打开连接  
	        URLConnection con = url.openConnection();  
	        //设置请求超时为5s  
	        con.setConnectTimeout(5*10000);  
	        // 输入流  
			is =  con.getInputStream();  
			String fileSuffix = imageUrl.substring(imageUrl
					.lastIndexOf(".") + 1);
			Iterator<ImageReader> it = ImageIO
					.getImageReadersByFormatName(fileSuffix);
			ImageReader reader = it.next();
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			
			BufferedImage oi = reader.read(0);
			
			int ox=oi.getWidth();
			int oy=oi.getHeight();
			x=x*ox/bx;
			y=y*oy/by;
			w=w*ox/bx;
			h=h*oy/by;
			
			//Rectangle rect = new Rectangle(x.intValue(),y.intValue(), w.intValue(), h.intValue());
			Rectangle rect = new Rectangle();
			rect.setRect(x, y, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, fileSuffix, new File(savePath));
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

	}
}

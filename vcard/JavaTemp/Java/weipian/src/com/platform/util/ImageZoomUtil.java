package com.platform.util;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageZoomUtil {
	public static void main(String args[]) {
		try {
			ImageZoomUtil imgUtil = new ImageZoomUtil("D:/temp/temp07.png");
			imgUtil.resize(96, 96);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String srcFile;
	private String destFile;
	private int width;
	private int height;
	private Image img;

	/**
	 * 构造函数
	 * 
	 * @param fileName
	 *            String
	 * @throws IOException
	 */
	public ImageZoomUtil (String fileName) throws IOException {
		File _file = new File(fileName); // 读入文件
		this.srcFile = _file.getName();

		String s = fileName;
		int i = s.lastIndexOf(".");
		String d = s.substring(0, i);
		d += "_" + s.substring(i, s.length());
		this.destFile = d;
		img = javax.imageio.ImageIO.read(_file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}
	
	public ImageZoomUtil(File file) throws IOException{
		this.srcFile = file.getName();

		String s =  file.getAbsolutePath();
		int i = s.lastIndexOf(".");
		String d = s.substring(0, i);
		d += "_" + s.substring(i, s.length());
		this.destFile = d;
		img = javax.imageio.ImageIO.read(file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}
	
	public ImageZoomUtil(File file,String toFile) throws IOException{
		this.srcFile = file.getName();
		File newFile=new File(toFile);
		//if(!newFile.exists()) newFile.mkdirs();
		//String s =  file.getName();
		this.destFile = toFile;
		img = javax.imageio.ImageIO.read(file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}
	/**
	 * 构造函数
	 * 
	 * @param fileName
	 *            String
	 * @throws IOException
	 */
	public ImageZoomUtil (String fileName, String destFile) throws IOException {
		File _file = new File(fileName); // 读入文件
		this.srcFile = _file.getName();
		this.destFile = destFile;
		img = javax.imageio.ImageIO.read(_file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 * @throws IOException
	 */
	public void resize(int w, int h) throws IOException {
		BufferedImage _image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		_image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		FileOutputStream newimageout = new FileOutputStream(destFile); // 输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimageout);
		encoder.encode(_image); // 近JPEG编码
		newimageout.close();
	}

	/**
	 * 按照固定的比例缩放图片
	 * 
	 * @param t
	 *            double 比例
	 * @throws IOException
	 */
	public void resize(double t) throws IOException {
		int w = (int) (width * t);
		int h = (int) (height * t);
		resize(w, h);
	}

	/**
	 * 以宽度为基准，等比例放缩图片
	 * 
	 * @param w
	 *            int 新宽度
	 * @throws IOException
	 */
	public void resizeByWidth(int w) throws IOException {
		int h = (int) (height * w / width);
		resize(w, h);
	}

	/**
	 * 以高度为基准，等比例缩放图片
	 * 
	 * @param h
	 *            int 新高度
	 * @throws IOException
	 */
	public void resizeByHeight(int h) throws IOException {
		int w = (int) (width * h / height);
		resize(w, h);
	}

	/**
	 * 按照最大高度限制，生成最大的等比例缩略图
	 * 
	 * @param w
	 *            int 最大宽度
	 * @param h
	 *            int 最大高度
	 * @throws IOException
	 */
	public void resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}

	/**
	 * 设置目标文件名 setDestFile
	 * 
	 * @param fileName
	 *            String 文件名字符串
	 */
	public void setDestFile(String fileName) throws Exception {
		if (!fileName.endsWith(".jpg") || !fileName.endsWith(".png") ) {
			throw new Exception("Dest File Must end with \".png\" or \".jpg\" .");
		}
		destFile = fileName;
	}

	/**
	 * 获取目标文件名 getDestFile
	 */
	public String getDestFile() {
		return destFile;
	}

	/**
	 * 获取图片原始宽度 getSrcWidth
	 */
	public int getSrcWidth() {
		return width;
	}

	/**
	 * 获取图片原始高度 getSrcHeight
	 */
	public int getSrcHeight() {
		return height;
	}

}

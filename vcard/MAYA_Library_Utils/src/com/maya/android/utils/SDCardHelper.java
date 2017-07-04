package com.maya.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
/**
 * 相关SD卡工具类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-12-9
 *
 */
public class SDCardHelper {
	private static final String TAG = "SDCardHelper";
	private static final long MB = 1048576;// 1024*1024;
	private static final int CACHE_SIZE = 20;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 4;
	private static final String WHOLESALE_CONV = ".cach";

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean exists() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 判断SD卡上文件是否存在
	 * 
	 * @param filepath
	 * @return
	 */
	public static boolean fileExists(String filepath) {
		File f = new File(filepath);
		return f.exists();
	}

	/**
	 * 拷贝文件
	 * 
	 * @param file
	 * @param dir
	 * @param fileName
	 */
	public static void copy(File file, String dir, String fileName) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			write(in, dir, fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写文件
	 * 
	 * @param in
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public static File write(InputStream in, String dir, String fileName) {
		if (in == null) {
			return null;
		}

		String absolutePath = dir;

		File f = new File(absolutePath);
		if (!f.exists()) {
			if (f.mkdirs()) {
				Log.d(TAG, "mkdirs error:" + absolutePath);
			}
		}

		File mf = new File(absolutePath + "/" + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(mf);

			byte bt[] = new byte[512];
			int n = -1;
			while (true) {
				n = in.read(bt);
				if (n <= 0)
					break;
				out.write(bt, 0, n);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			absolutePath = null;
		}
		return mf;
	}

	/**
	 * 保存bmp文件到sd卡
	 * 
	 * @param bitmap
	 * @param dir
	 * @param fileName
	 * @param savepath
	 * @return
	 */
	public static File save(final Bitmap bitmap, String dir, String fileName,
			String savepath) {
		if (bitmap == null)
			return null;

		String absolutePath = dir;
		File mf = new File(absolutePath + "/" + fileName);
		File f = new File(absolutePath);
		File f1 = new File(savepath + "/" + ".nomedia");
		if (!f.exists()) {
			if (f.mkdirs()) {
				Log.d(TAG, "mkdirs error:" + absolutePath);
			}
		}
		if (!f1.exists()) {
			try {
				f1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "mkfile error:" + savepath + "/" + ".nomedia");
			}
		}

		OutputStream outputStream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
		byte[] jpegData = out.toByteArray();

		try {
			outputStream = new FileOutputStream(mf);
			outputStream.write(jpegData);
			jpegData = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, "FileNotFoundException:" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "IOException:" + e.getMessage());
		}
		if(Helper.isNotNull(outputStream)){
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outputStream = null;
		}
		return mf;
	}
	
	/**
	 * 保存bmp文件到sd卡
	 * 
	 * @param bitmapByte
	 * @param dir
	 * @param fileName
	 * @param savepath
	 * @return
	 */
	public static File save(byte[] bitmapByte, String dir, String fileName,
			String savepath) {
		if (bitmapByte == null)
			return null;

		String absolutePath = dir;
		File mf = new File(absolutePath + "/" + fileName);
		File f = new File(absolutePath);
		File f1 = new File(savepath + "/" + ".nomedia");
		if (!f.exists()) {
			if (f.mkdirs()) {
				Log.d(TAG, "mkdirs error:" + absolutePath);
			}
		}
		if (!f1.exists()) {
			try {
				f1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "mkfile error:" + savepath + "/" + ".nomedia");
			}
		}

		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(mf);
			outputStream.write(bitmapByte);
			bitmapByte = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, "FileNotFoundException:" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "IOException:" + e.getMessage());
		}
		if(Helper.isNotNull(outputStream)){
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outputStream = null;
		}
		return mf;
	}

	/**
	 * 获取SD卡根目录
	 * 
	 * @return
	 */
	public static String getSdcardDir() {
		if (Environment.getExternalStorageDirectory().canWrite()) {
			return Environment.getExternalStorageDirectory().getPath();
		} else {
			return null;
		}
	}

	/**
	 * 获取SD卡的总容量，单位为MB
	 * 
	 * @return
	 */
	public static int getExternalStorage() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		return (int) (((double) statFs.getBlockCount() * (double) statFs
				.getBlockSize()) / MB);
	}

	/**
	 * 获取SD卡的剩余容量，单位为MB
	 * 
	 * @return
	 */
	public static int getAvailableStore() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		return (int) (((double) statFs.getAvailableBlocks() * (double) statFs
				.getBlockSize()) / MB);
	}

	/**
	 * 修改文件的最后修改时间
	 * 
	 * @param dir
	 * @param fileName
	 */
	public static void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	/**
	 * 删除文件 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath){
		boolean result = false;
		if (Helper.isNotEmpty(filePath)){
			try {
				File file = new File(filePath);
				if (file.exists()){
					result = file.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 删除文件夹下的文件
	 * @param folderPath
	 * @return
	 */
	public static boolean deleteFilesInFolder(String folderPath){
		boolean result = false;
		if (Helper.isNotEmpty(folderPath)){
			try {
				File path = new File(folderPath);
				if (path.exists() && path.isDirectory()){
					result = true;
					File[] files = path.listFiles();
					if (Helper.isNotEmpty(files)){
						for (int i = files.length - 1; i >= 0; i--) {
							if (!files[i].delete() && result){
								result = false;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}
	/**
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 */
	public static void removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}
		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > getAvailableStore()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.d(TAG, "Clear some expiredcache files ");
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}
	}
	
	/**
	 * 根据文件的最后修改时间进行排序
	 * 
	 */
	static class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}

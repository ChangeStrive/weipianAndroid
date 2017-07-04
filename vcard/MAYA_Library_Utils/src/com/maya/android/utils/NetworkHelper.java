package com.maya.android.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import com.maya.android.utils.base.FormFile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml.Encoding;

/**
 * 网络工具类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-3
 *
 */
public class NetworkHelper {
	
	private static final String TAG = NetworkHelper.class.getSimpleName();
	/**
	 * KEY:网络传输用,Terminal-Agent
	 */
	public static final String NETWORK_KEY_TERMINAL_AGENT = "Terminal-Agent";
	/**
	 * KEY:网络传输用,Data-Compress
	 */
	public static final String NETWORK_KEY_DATA_COMPRESS = "Data-Compress";
	/**
	 * KEY:网络传输用,Data-Encrypt
	 */
	public static final String NETWORK_KEY_DATA_ENCRYPT ="Data-Encrypt";
	/**
	 * '1'代表true
	 */
	private static final String STRING_VALUE_OF_TRUE = "1";
	/**
	 * '0'代表false
	 */
	private static final String STRING_VALUE_OF_FALSE = "0";
	private static final String NETWORK_STATE_WIFI = "wifi";

	public static final short TYPE_IP_V4 = 4;
	public static final short TYPE_IP_V6 = 6;
	@SuppressWarnings("unused")
	private int mVerificationTwo = 800253060;
	
	/**
	 * 检查网络连接是否可用
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 取得网络连接类型 </br>
	 * 0:未连接  1：wifi 2:2G/3G
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context){
		int result = 0;
		if(Helper.isNotNull(context)){
			ConnectivityManager connectionManager = (ConnectivityManager) ActivityHelper.getGlobalApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			String typeName = null;
			if(Helper.isNotNull(networkInfo)){
				typeName = networkInfo.getTypeName();
				if(Helper.isNotEmpty(typeName)){
					result = NETWORK_STATE_WIFI.equalsIgnoreCase(typeName)? 1 : 2;
				}
			}
		}
		return result;
	}

	/**
	 * 获取当前ip
	 * @param ipType ipv4或者ipv6，请使用{@link #TYPE_IP_V4}或者{@link #TYPE_IP_V6}
	 * @return 当前ip
	 */
	public static String getLocalIpAddress(short ipType) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
					en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
						enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						switch (ipType) {
						case TYPE_IP_V4:
							if (inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;
						case TYPE_IP_V6:
							if (inetAddress instanceof Inet6Address) {
								return inetAddress.getHostAddress().toString();
							}
							break;

						default:
							break;
						}
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送HttpPost消息
	 * @param url
	 * @param postpair
	 * @param terminalAgent
	 * @param isGzip
	 * @param isEncrypt
	 * @return
	 */
	public static HttpResponse sendHttpPostString(String url,
			List<NameValuePair> postpair, String terminalAgent, 
			boolean isGzip, boolean isEncrypt) {
		if (Helper.isNotEmpty(url) && Helper.isNotEmpty(postpair)){
			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 45000);
				HttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpPost httpPost = new HttpPost(url);
				if(Helper.isNotEmpty(terminalAgent)){
					httpPost.addHeader(NETWORK_KEY_TERMINAL_AGENT, terminalAgent);
				}
				if(isGzip){
					httpPost.addHeader(NETWORK_KEY_DATA_COMPRESS, STRING_VALUE_OF_TRUE);
				}
				if(isEncrypt){
					httpPost.addHeader(STRING_VALUE_OF_FALSE, STRING_VALUE_OF_FALSE);
				}
				httpPost.setEntity(new UrlEncodedFormEntity(postpair));
				return httpClient.execute(httpPost);
			}catch(ConnectTimeoutException e){
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 发送HttpGet消息
	 * @param url
	 * @param terminalAgent
	 * @param isGzip
	 * @param isEncrypt
	 * @return
	 */
	public static HttpResponse sendHttpGetString(String url, String terminalAgent, 
			boolean isGzip, boolean isEncrypt) {
		if (Helper.isNotEmpty(url)){
			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 45000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpGet httpget = new HttpGet(url);
				if(Helper.isNotEmpty(terminalAgent)){
					httpget.addHeader(NETWORK_KEY_TERMINAL_AGENT, terminalAgent);
				}
				if(isGzip){
					httpget.addHeader(NETWORK_KEY_DATA_COMPRESS, STRING_VALUE_OF_TRUE);
				}
				if(isEncrypt){
					httpget.addHeader(STRING_VALUE_OF_FALSE, STRING_VALUE_OF_FALSE);
				}
				return httpclient.execute(httpget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 网络请求是否成功
	 * 
	 * @param response
	 * 
	 * @return 网络请求是否成功
	 */
	public static boolean isHttpResponseSuccess(HttpResponse response) {
		boolean result = false;
		try {
			if (Helper.isNotNull(response)) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.d(TAG, "发送成功！");
					result = true;
				} else {
					Log.d(TAG, "发送失败:" + response.getStatusLine().toString());
				}
			} else {
				Log.d(TAG, "服务器无响应！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 取得网络请求返回的HttpEntity
	 * 
	 * @param response
	 * 
	 * @return 返回的HttpEntity实例
	 */
	public static HttpEntity getHttpResponseEntity(HttpResponse response) {
		HttpEntity result = null;
		if (isHttpResponseSuccess(response)){
			result = response.getEntity();
		}
		return result;
	}
	/**
	 * 取得网络请求返回的字符串内容
	 * @param response
	 * @return
	 */
	public static String getHttpResponseString(HttpResponse response){
		return getHttpResponseString(response, Encoding.UTF_8.toString());
	}
	/**
	 * 取得网络请求返回的字符串内容
	 * 
	 * @param response
	 * @param defaultCharset
	 *            默认字符集
	 * 
	 * @return 返回的字符串内容
	 */
	public static String getHttpResponseString(HttpResponse response,
			String defaultCharset) {
		String result = null;
		HttpEntity httpEntity = getHttpResponseEntity(response);
		if (Helper.isNotNull(httpEntity)) {
			try {
				result = EntityUtils.toString(httpEntity, defaultCharset);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 取得网络请求返回的InputStream
	 * 
	 * @param response
	 * 
	 * @return 返回的InputStream
	 */
	public static InputStream getHttpResponseInputStream(HttpResponse response) {
		InputStream result = null;
		HttpEntity httpEntity = getHttpResponseEntity(response);
		if (Helper.isNotNull(httpEntity)) {
			try {
				result = httpEntity.getContent();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 取得网络请求返回的InputSource
	 * 
	 * @param response
	 * @param defaultCharset
	 *            默认字符集
	 * 
	 * @return 返回的InputSource
	 */
	public static InputSource getHttpResponseInputSource(HttpResponse response,
			String defaultCharset) {
		InputSource result = null;
		// result.setEncoding("GB2312");
		HttpEntity httpEntity = getHttpResponseEntity(response);
		if (Helper.isNotNull(httpEntity)) {
			try {
				result = new InputSource();
				result.setEncoding(defaultCharset);
				result.setByteStream(httpEntity.getContent());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 取得网络图片
	 * @param urlAddr 图片地址
	 * @return 图片Bitmap
	 */
	public static Bitmap fetchUrlBitmap(String urlAddr) {
		try {
			URL url = new URL(urlAddr);
			URLConnection c = url.openConnection();
			c.connect(); // 用URLConnection的connect()方法建立连接
			try{
				InputStream is = c.getInputStream();
				try{
					return BitmapFactory.decodeStream(is);
				} catch(OutOfMemoryError e){
					e.printStackTrace();
					Bitmap result = null;
					Rect outPadding = new Rect();
					Options opts = new Options();
					opts.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(is, outPadding, opts);
					int outHeight = opts.outHeight;
					int outWidth = opts.outWidth;
					int scaleWidth = outWidth/ActivityHelper.getScreenWidth();
					int scaleHeight = outHeight/ActivityHelper.getScreenHeight();
					int scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
					opts.inPreferredConfig = Config.RGB_565;
					opts.inSampleSize = scale;
					opts.inJustDecodeBounds = false;
					do{
						try{
							result = BitmapFactory.decodeStream(is, outPadding, opts);
						} catch(OutOfMemoryError e2){
							e2.printStackTrace();
							opts.inSampleSize *= 2;
						}
					}while(Helper.isNull(result));
					return result;
				}
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 取得网络图片
	 * @param urlAddr 图片地址
	 * @param bitmapWidth 图片的宽
	 * @param bitmapHeight 图片的高
	 * @return 图片Bitmap
	 */
	public static Bitmap fetchUrlBitmap(String urlAddr, int bitmapWidth, int bitmapHeight){
		if(bitmapWidth == 0 || bitmapHeight == 0){
			return fetchUrlBitmap(urlAddr);
		}
		Bitmap result = null;
		byte[] bitmapByte = fetchUrlBitmapByte(urlAddr);
		if(Helper.isNotNull(bitmapByte)){
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			result = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length, opts);
			if(bitmapWidth < opts.outWidth || bitmapHeight < opts.outHeight){
				float scaleWidth = opts.outWidth * 1.0F / bitmapWidth; 
				float scaleHeight = opts.outHeight * 1.0F /bitmapHeight;
				float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
				opts.inSampleSize = (int)scale;
			}
			opts.inPreferredConfig = Config.RGB_565;
			opts.inJustDecodeBounds = false;
			result = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length, opts);
		}
		return result;
	}
	/**
	 * 取得网络图片
	 * @param urlAddr 图片地址
	 * @return 图片bitmapByte
	 */
	
	public static byte[] fetchUrlBitmapByte(String urlAddr){
		try {
			URL url = new URL(urlAddr);
			URLConnection c = url.openConnection();
			c.connect(); // 用URLConnection的connect()方法建立连接
			InputStream is = c.getInputStream();
			byte[] result = null;
			try{
				result = readStream(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将文件转成byte[]
	 * @param path
	 * 			文件路径或url
	 * @return
	 * @throws Exception
	 */
	public static byte[] readFile2Byte(String path) throws Exception{
		FileInputStream fis = new FileInputStream(path);
		return readStream(fis);
	}
	/**
	 * 将流转换成byte[]
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	private static byte[] readStream(InputStream inStream) throws Exception{      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }
	
	/**
	 * 取得网络图片
	 * @param urlAddr 图片地址
	 * @return 图片Drawable
	 */
	public static Drawable fetchUrlDrawable(String urlAddr){
		Drawable result = null;
		Bitmap bitmap = fetchUrlBitmap(urlAddr);
		if (Helper.isNotNull(bitmap)){
			result = new BitmapDrawable(Resources.getSystem(), bitmap);
		}
		return result;
	}
	/**
	 * 取得网络图片
	 * @param urlAddr 图片地址
	 * @param width 图片宽
	 * @param height 图片高
	 * @return 图片Drawable
	 */
	public static Drawable fetchUrlDrawable(String urlAddr, int width, int height){
		Drawable result = null;
		Bitmap bitmap = fetchUrlBitmap(urlAddr, width, height);
		if (Helper.isNotNull(bitmap)){
			result = new BitmapDrawable(Resources.getSystem(), bitmap);
		}
		return result;
	}
	/**
	 * 下载文件
	 * @param urlAddr 文件的url地址
	 * @param fullSaveFilePath 保存文件到本地的完整路径,包括文件名和后缀名
	 * @return 文件的完整路径
	 */
	public static File downloadFile(String urlAddr, String fullSaveFilePath){
		File result = null;
		if (Helper.isNotEmpty(urlAddr)){
			InputStream in = null;
			FileOutputStream fos = null;
			try {
				URL url = new URL(urlAddr);
				URLConnection conn = url.openConnection();
				in = conn.getInputStream();
				if (Helper.isNotNull(in)){
					File file = new File(fullSaveFilePath);
					if (!file.exists()){
						file.getParentFile().mkdirs();
						file.createNewFile();
					}
					fos = new FileOutputStream(file);
					byte buffer[] = new byte[512];
					int length = in.read(buffer);
					while (length > 0) {
						fos.write(buffer, 0, length);
						length = in.read(buffer);
					}
					result = file;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (Helper.isNotNull(in)){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (Helper.isNotNull(fos)){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}
	/**
	 * 上传文件,可传参
	 * <p> Content-Disposition中的name字段，默认使用文件名
	 * @param actionUrl
	 * @param params
	 * @param filePath
	 * @param userAgent1
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, String filePath){
		File file = Helper.isNotEmpty(filePath) ?  new File(filePath) : null;
		return upload(actionUrl, params, file, "", "");
	}
	/**
	 * 上传文件,可传参(可改文件名)
	 * <p> Content-Disposition中的name字段，默认使用文件名
	 * @param actionUrl
	 * @param params
	 * @param filePath
	 * @param fileName
	 * @param gact
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, String filePath
			, String fileName){
		File file = Helper.isNotEmpty(filePath) ?  new File(filePath) : null;
		return upload(actionUrl, params, file, fileName, "");
	}
	/**
	 * 上传文件,可传参(可改文件名)
	 * @param actionUrl
	 * @param params
	 * @param filePath
	 * @param fileName
	 * @param formName 提交文件时，指定Content-Disposition中的name字段的值
	 * @param gact
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, String filePath
			, String fileName
			, String formName){
		File file = Helper.isNotEmpty(filePath) ?  new File(filePath) : null;
		return upload(actionUrl, params, file, fileName, formName);
	}
	/**
	 * 上传文件,可传参
	 * <p> Content-Disposition中的name字段，默认使用文件名
	 * @param actionUrl
	 * @param params
	 * @param file
	 * @param userAgent1
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, File file){
		return upload(actionUrl, params, file, "", "");
	}
	/**
	 * 上传文件,可传参(可改文件名)
	 * <p> Content-Disposition中的name字段，默认使用文件名
	 * @param actionUrl
	 * @param params
	 * @param file
	 * @param fileName 带后缀名的文件名
	 * @param userAgent1
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, File file
			, String fileName) {
		return upload(actionUrl, params, file, fileName, "");
	}
	/**
	 * 上传文件,可传参(可改文件名)
	 * @param actionUrl
	 * @param params
	 * @param file
	 * @param fileName 带后缀名的文件名
	 * @param formName 提交文件时，指定Content-Disposition中的name字段的值
	 * @return
	 */
	public static String upload(String actionUrl
			, List<NameValuePair> params
			, File file
			, String fileName
			, String formName) {
		FormFile[] formFiles = null;
		if (Helper.isNotNull(file) && file.isFile()){
			byte[] fileBytes = Helper.getBytesFromFile(file);
			if (Helper.isNotNull(fileBytes)){
				formFiles = new FormFile[1];
				formFiles[0] = new FormFile(
						Helper.isEmpty(fileName) ? file.getName() : fileName
						, fileBytes
						, Helper.isEmpty(formName) ? file.getName() : formName
						, "multipart/form-data");
			}
		}
		if (Helper.isNull(formFiles)){
			formFiles = new FormFile[0];
		}
		return upload(actionUrl, params, formFiles);
	}
	/**
	 * 上传文件,可传参
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @param userAgent1
	 * @return
	 */
	public static String upload(String actionUrl, List<NameValuePair> params,
			FormFile[] files) {
		String result = "";
		try {
			// 数据分隔线
			String BOUNDARY = "---------7d4a6d158c9"; 
			String MULTIPART_FORM_DATA = "multipart/form-data";

			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 允许输入
			conn.setDoInput(true);
			// 允许输出
			conn.setDoOutput(true);
			// 不使用Cache
			conn.setUseCaches(false);
			conn.setConnectTimeout(300000);
			conn.setReadTimeout(300000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
					+ "; boundary=" + BOUNDARY);
			StringBuilder sb = new StringBuilder();
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());

			// 上传的表单参数部分
			if (params != null && params.size() > 0) {
				//for (Map.Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容
				for (NameValuePair entry : params){
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getName() + "\"\r\n\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
				}
				outStream.write(sb.toString().getBytes());// 发送表单字段数据
			}

			// 上传的文件部分
			for (FormFile file : files) {
				StringBuilder split = new StringBuilder();
				split.append("--")
					.append(BOUNDARY)
					.append("\r\n")
					.append("Content-Disposition: form-data;name=\"")
					.append(file.getFormName())
					.append("\";filename=\"")
					.append(file.getFileName())
					.append("\"\r\n\r\n");
				outStream.write(split.toString().getBytes());
				outStream.write(file.getData(), 0, file.getData().length);
				outStream.write("\r\n".getBytes());
			}
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
			outStream.write(end_data);
			outStream.flush();
			int cah = conn.getResponseCode();
			if (cah == HttpStatus.SC_OK) {
				Log.d(TAG, "upload:上传文件成功!");
			} else {
				Log.d(TAG, "upload:上传文件失败:返回码=" + cah);
			}
			outStream.close();

			// 获服务器取数据
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);// 读字符串用的。
			String inputLine = null;
			StringBuilder resultSb = new StringBuilder();
			// 使用循环来读取获得的数据，把数据都存到result中了
			while (((inputLine = reader.readLine()) != null)) {
				// 我们在每一行后面加上一个"\n"来换行
				resultSb.append(inputLine).append("\n");
			}
			reader.close();// 关闭输入流
			conn.disconnect();
			result = resultSb.toString();
			Log.d(TAG, "upload:返回值:" + result);
		} catch (Exception e) {
			Log.d(TAG, "upload:上传文件失败:" + e);
		}
		return result;
	}
}

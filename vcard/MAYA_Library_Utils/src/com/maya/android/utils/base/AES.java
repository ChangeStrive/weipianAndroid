package com.maya.android.utils.base;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import com.maya.android.utils.Helper;

/**
 * AES加解密
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-6
 *
 */
public class AES {
	private static final String ENCODING_UTF8 = "utf-8";
	private static final String ALGORITHM_TYPE = "AES";
	private static final String CLPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static Cipher createCipher(int opmode, String key){
		try {
			byte[] enCodeFormat = key.getBytes(ENCODING_UTF8);
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM_TYPE);
			// 创建密码器
			Cipher cipher = Cipher.getInstance(CLPHER_TRANSFORMATION);
			// 初始化
			cipher.init(opmode, secretKeySpec);
			return cipher;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param key
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String key) {
		if (Helper.isNotEmpty(content) && Helper.isNotEmpty(key)){
			try {
				return encrypt(content.getBytes(ENCODING_UTF8), key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param key
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] content, String key) {
		if (Helper.isNotEmpty(content) && Helper.isNotEmpty(key)){
			try {
				Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, key);
				if (cipher != null){
					byte[] result = cipher.doFinal(content);
					return result; // 加密
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 加密
	 * @param content 需要加密的内容
	 * @param key 加密密码
	 * @param blockSize 分块加密大小
	 * @return
	 */
	public static byte[] encrypt(String content, String key, int blockSize){
		if (Helper.isNotEmpty(content) && Helper.isNotEmpty(key)){
			try {
				return encrypt(content.getBytes(ENCODING_UTF8), key, blockSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 加密
	 * @param content 需要加密的内容
	 * @param key 加密密码
	 * @param blockSize 分块加密大小
	 * @return
	 */
	public static byte[] encrypt(byte[] content, String key, int blockSize){
		if (Helper.isNotEmpty(content) && Helper.isNotEmpty(key)){
			try {
				ArrayList<Byte> list = new ArrayList<Byte>();
				int size = content.length / blockSize;//Math.round(content.length * 1.0f / blockSize);
				if (size * blockSize < content.length){
					size++;
				}
				for (int i = 0; i < size; i++){
					int realSize = blockSize;
					if (i == size - 1){
						realSize = content.length - i * blockSize;
					}
					byte[] curBytes = new byte[realSize];
					System.arraycopy(content, i * blockSize, curBytes, 0, realSize);
					byte[] encryptBytes = encrypt(curBytes, key);
					if (encryptBytes != null){
						for (byte b : encryptBytes) {
							list.add(b);
						}
					}
				}
				byte[] result = new byte[list.size()];
				for(int i = 0; i < list.size(); i++){
					result[i] = list.get(i).byteValue();
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param key
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String key) {
		if (Helper.isNotEmpty(content) && Helper.isNotEmpty(key)){
			try {
				Cipher cipher = createCipher(Cipher.DECRYPT_MODE, key);
				if (cipher != null){
					byte[] result = cipher.doFinal(content);
					return result; // 解密
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}

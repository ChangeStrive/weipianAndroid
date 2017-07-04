package com.platform.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Character.UnicodeBlock;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
/**
 * @ClassName: EmojiUtil
 * @Description:unicode5与unicode6转换(emoji表情）
 *              对照文件：emoji.properties
 * @author xiaoweiwan
 * @date Jun 28, 2012
 */
public class EmojiUtil {
		private static EmojiUtil _instance;
	private static Properties prop;
	private static Properties propValue;
	private static final int MIN_UNICODEFIVE = Integer.parseInt("E001",16);
	private static final int MAX_UNICODEFIVE = Integer.parseInt("E536",16);
	
	private static final int MIN_UNICODESIX = Integer.parseInt("1F004",16);
	private static final int MAX_UNICODESIX = Integer.parseInt("1F6C0",16);
	
	static{
		InputStream stream = EmojiUtil.class.getClassLoader().getResourceAsStream("emoji.properties");
		prop = new Properties();
		propValue = new Properties();
		try {
			prop.load(stream);
			for (Iterator iter = prop.entrySet().iterator(); iter.hasNext();) {
	 		    Map.Entry<String,String> entry = (Map.Entry) iter.next();
	 		    String unicode5 = (String)entry.getKey();
	 		    String unicode6 = (String)entry.getValue();
	 		    propValue.put(unicode6, unicode5);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static EmojiUtil getInstance(){
		if(_instance == null){
			synchronized (EmojiUtil.class) {
				_instance = new EmojiUtil();
			}
		}
		return _instance;
	}
	
	
	/**
	 * @Title: changeUnicodeFiveToSix
	 * @Description: 使字符串编码如果是Unicode5的emoji字符则转为Unicode6输出
	 * @param name
	 * @return
	 */
	public static String changeUnicodeFiveToSix(String name)  {
	   String realname = name;   
	   try{    
		   char[] charArr = name.toCharArray();        
		   StringBuffer sb = new StringBuffer();
		   int i = 0;
	       while(i<charArr.length){
	            char ch = charArr[i];
	            int unicodeIntVale = 0;
	            if(Character.isHighSurrogate(ch)){
	            	unicodeIntVale = Character.toCodePoint(ch, charArr[i+1]);
	            }else{
	            	unicodeIntVale = (int)ch;
	            }
            	if( (unicodeIntVale>=MIN_UNICODEFIVE) && (unicodeIntVale<=MAX_UNICODEFIVE) ){
            		String hexBase = Integer.toHexString(unicodeIntVale).toUpperCase();
            		
            		String trans = prop.getProperty("\\u"+hexBase);
            		if(trans!=null){
            			int second = trans.indexOf("\\u", 2);
            			if(second<0){
            				second = trans.indexOf("0x", 2);
            			}
            			if(second>0){
            				sb.append(Character.toChars(Integer.parseInt(trans.substring(0,second).replaceFirst("\\\\u", "").replaceFirst("0x", ""),16)));
            				sb.append(Character.toChars(Integer.parseInt(trans.substring(second).replaceFirst("\\\\u", "").replaceFirst("0x", ""),16)));
            			}else{
            				sb.append(Character.toChars(Integer.parseInt(trans.replaceFirst("\\\\u", "").replaceFirst("0x", ""),16)));
            			}
            		}else{
            			sb.append(Character.toChars(unicodeIntVale));
            		}
            	}else{
            		sb.append(Character.toChars(unicodeIntVale));
            	}
	            if(Character.isHighSurrogate(ch)){
	            	 i = i + 2;
	            	 continue;
	            }
	            else{
	                i = i + 1;
	            }
	        }
		   realname = sb.toString();         
		   }catch(Exception e)   {  
			   e.printStackTrace();  
	    }    
	    return realname;  
	}
	/**
	 * @Title: changeUnicodeSixToFive
	 * @Description: 使字符串编码如果是Unicode6的emoji字符则转为Unicode5输出
	 * @param name
	 * @return
	 */
	public static String changeUnicodeSixToFive(String name)  {
		   String realname = name;   
		   try{    
			   char[] charArr = name.toCharArray();        
			   StringBuffer sb = new StringBuffer();
			   int i = 0;
		       while(i<charArr.length){
		            char ch = charArr[i];
		            int unicodeIntVale = 0;
		            if(Character.isHighSurrogate(ch)){
		            	unicodeIntVale = Character.toCodePoint(ch, charArr[i+1]);
		            }else{
		            	unicodeIntVale = (int)ch;
		            }
	            	if( (unicodeIntVale>=MIN_UNICODESIX) && (unicodeIntVale<=MAX_UNICODESIX) ){
	            		String hexBase = Integer.toHexString(unicodeIntVale).toUpperCase();
	            		String trans = propValue.getProperty("0x"+hexBase);
	            		if(trans!=null){
	            			sb.append(Character.toChars(Integer.parseInt(trans.replaceFirst("\\\\u", ""),16)));
	            		}else{
	            			sb.append(Character.toChars(unicodeIntVale));
	            		}
	            	}else{
	            		sb.append(Character.toChars(unicodeIntVale));
	            	}
	            	if(Character.isHighSurrogate(ch)){
	            		 i = i + 2;
		                    continue;
	            	}else{
		                i = i + 1;
		            }
		       }
			   realname = sb.toString();         
			   }catch(Exception e)   {    
				   e.printStackTrace();  
		    }    
		    return realname;  
	}
	
	 public static String utf8ToUnicode(String inStr) {
	        char[] myBuffer = inStr.toCharArray();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < inStr.length(); i++) {
	         UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
	            if(ub == UnicodeBlock.BASIC_LATIN){
	             sb.append(inStr);
	            }else if(ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
	             int j = (int) myBuffer[i] - 65248;
	             sb.append((char)j);
	            }else{
	            	int s = (int) myBuffer[i];
//	            	System.out.println(s);
	            	if(s<0)
	            		s = s+2^32;
	                String hexS = Integer.toHexString(s);
	                String unicode = "\\u"+hexS.toUpperCase();
	                sb.append(unicode);
	            }
	        }
	        return sb.toString();
	 }
	
}

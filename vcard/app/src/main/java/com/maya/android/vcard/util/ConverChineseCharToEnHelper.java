package com.maya.android.vcard.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.maya.android.utils.Helper;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 获取中文首字母类
 * 
 * @author del1
 * 
 */
public class ConverChineseCharToEnHelper {

	private static final String TAG = "ConverChineseCharToEn";

	/**
	 * 
	 * 替换特殊字符
	 * 
	 * @param string
	 * @param s
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-16 下午04:40:48
	 */
	public static boolean numberMatch(String string, String s) {
		if (Helper.isEmpty(string)){
			return false;
		}
		String dealStr = string.replace("-", "");
		dealStr = dealStr.replace(" ", "");
		if (dealStr.contains(s)){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 判断是否数字
	 * 
	 * @param str
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-16 下午04:39:40
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	/**
	 * 是否含有中文字符
	 * 
	 * @param inputStr
	 * @return
	 */
	public static boolean isChinese(String inputStr) {
		if (Helper.isNotEmpty(inputStr)) {
			char[] charArr = inputStr.trim().toCharArray();
			for (int i = 0; i < charArr.length; i++) {
				if (Character.toString(charArr[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					return true;
				}
			}
		}
		return false;

	}
	/**
	 * 
	 * 小写字母转换成大写
	 * @param c
	 * @return
	 * @author: xiaozhenhua
	 * @version: 2012-5-15 下午04:54:50
	 */
	public static char conversionHeadUppercase(char c) {
		switch (c) {
		case 'a':
			return 'A';
		case 'b':
			return 'B';
		case 'c':
			return 'C';
		case 'd':
			return 'D';
		case 'e':
			return 'E';
		case 'f':
			return 'F';
		case 'g':
			return 'G';
		case 'h':
			return 'H';
		case 'i':
			return 'I';
		case 'j':
			return 'J';
		case 'k':
			return 'K';
		case 'l':
			return 'L';
		case 'm':
			return 'M';
		case 'n':
			return 'N';
		case 'o':
			return 'O';
		case 'p':
			return 'P';
		case 'q':
			return 'Q';
		case 'r':
			return 'R';
		case 's':
			return 'S';
		case 't':
			return 'T';
		case 'u':
			return 'U';
		case 'v':
			return 'V';
		case 'w':
			return 'W';
		case 'x':
			return 'X';
		case 'y':
			return 'Y';
		case 'z':
			return 'Z';
		default:
			return c;
		}
	}

	/**
	 * 
	 * 将输入的拼音转成数字
	 * 
	 * @param str
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-18 下午04:24:18
	 */
	public static String converEnToNumber(String str) {
		if(Helper.isEmpty(str)){
			return "";
		}
		char[] chars = str.toCharArray();
		StringBuffer sbf = new StringBuffer();
		for (char c : chars) {
			sbf.append(getOneNumFromAlpha(c));
		}
		return sbf.toString();
	}

	/**
	 * 汉字 对应的拼音 转化为 数字
	 * @param chines
	 * @return String
	 * @author: zheng_cz 2013-07-13
	 */
	public static String converChinesToNumber(String chines){
		String pinyin = converToPingYingHeadUppercase(chines);
		return converEnToNumber(pinyin);
	}
	/**
	 * 
	 * 将字母转换成数字
	 * 
	 * @param firstAlpha
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-16 下午04:38:19
	 */
	public static char getOneNumFromAlpha(char firstAlpha) {
		switch (firstAlpha) {
		case 'a':
		case 'b':
		case 'c':
		case 'A':
		case 'B':
		case 'C':
			return '2';
		case 'd':
		case 'e':
		case 'f':
		case 'D':
		case 'E':
		case 'F':
			return '3';
		case 'g':
		case 'h':
		case 'i':
		case 'G':
		case 'H':
		case 'I':
			return '4';
		case 'j':
		case 'k':
		case 'l':
		case 'J':
		case 'K':
		case 'L':
			return '5';
		case 'm':
		case 'n':
		case 'o':
		case 'M':
		case 'N':
		case 'O':
			return '6';
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
			return '7';
		case 't':
		case 'u':
		case 'v':
		case 'T':
		case 'U':
		case 'V':
			return '8';
		case 'w':
		case 'x':
		case 'y':
		case 'z':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
			return '9';
		default:
			return firstAlpha;
		}
	}

	/**
	 * 
	 * 替换掉中文标点
	 * 
	 * @param chines
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-12 下午06:16:03
	 */
	public static String replaceString(String chines) {
		return chines.replace("《", "").replace("》", "").replace("！", "")
				.replace("￥", "").replace("【", "").replace("】", "")
				.replace("（", "").replace("）", "").replace("－", "")
				.replace("；", "").replace("：", "").replace("”", "")
				.replace("“", "").replace("。", "").replace("，", "")
				.replace("、", "").replace("？", "").replace(" ", "")
				.replace("-", ""); 
	}

	/**
	 * 
	 * 转换输入汉字的全拼(小写) 多音字只反回第一个拼音,拼音首字母大写其它小写
	 * 
	 * @param chines
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-12 下午01:19:28
	 */
	public static String converToPingYingHeadUppercase(String chines) {
		if(Helper.isEmpty(chines)){
			return "";
		}
		chines = replaceString(chines).trim();
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					String headPy = pys == null ? nameChar[i]+"" : pys[0];
					pinyinName.append(conversionHeadUppercase(headPy.charAt(0))+ headPy.substring(1, headPy.length()));
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					Log.e(TAG, e.getMessage(), e);
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}
	
	/**
	 * 获取所有汉字的 汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 * @return
	 */
	public static String getFirstSpell(String chines) {
		String pinyinName = "";
		if (Helper.isNotEmpty(chines)) {
			char[] nameChar = chines.toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0, length = nameChar.length; i < length; i++) {
				if (nameChar[i] > 128) {
					try {
						String[] pys = PinyinHelper.toHanyuPinyinStringArray(
								nameChar[i], defaultFormat);
						if (Helper.isNotNull(pys)) {
							pinyinName += pys[0].charAt(0);
						}
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else {
					pinyinName += nameChar[i];
				}
			}
		}
		return pinyinName;
	}

	/**
	 * 
	 * 转换输入汉字的首字母，多个汉字是只反回第一个汉字的首字母
	 * 
	 * @param chinese
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-12 下午02:28:12
	 */
	public static String getFirstLetter(String chines) {
		if(Helper.isEmpty(chines)){
			return "";
		}
		char firstAlpha = '#' ;
		String pinyin = getFirstSpell(chines.trim());
		if(Helper.isNotEmpty(pinyin)){
			firstAlpha = (pinyin.toCharArray())[0];
		}
		// 英文大小写字母
		if( (firstAlpha >= 65 && firstAlpha <= 90) || (firstAlpha >= 97 && firstAlpha <= 122)){
			
			return ""+firstAlpha;
		}else{
			return "#";
		}
	}

	/**
	 * 
	 * 反回输入字符串的所有首字母(大写)并去除非汉字
	 * 
	 * @param chines
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-12 下午05:15:00
	 */
	public static String converToAllFirstSpellsUppercase(String chines) {
		if(Helper.isEmpty(chines)){
			return "";
		}
		chines = replaceString(chines).trim();
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if(pys != null){
						char nchar = pys[0].charAt(0);
						pinyinName.append(nchar);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					Log.e(TAG, e.getMessage(), e);
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

	/**
	 * 
	 * 反回输入字符串的所有首字母(小写)并去除非汉字
	 * 
	 * @param chines
	 * @return
	 * @author: 肖珍华
	 * @version: 2012-4-12 下午05:15:00
	 */
	public static String converToAllFirstSpellsLowercase(String chines) {
		if(Helper.isEmpty(chines)){
			return "";
		}
		chines = replaceString(chines).trim();
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if(pys != null){
						char nchar = pys[0].charAt(0);
						pinyinName.append(nchar);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					Log.e(TAG, e.getMessage(), e);
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

	/**
	 * 首字母是否有英文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEng(String str) {
		if (Helper.isNotEmpty(str)) {
			return str.charAt(0) >= 0x0000 && str.charAt(0) <= 0x00ff;
		} else {
			return false;
		}
	}
	/**
	 * 字符串是否只含英文和符号、数字
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String str){
		if(Helper.isNotEmpty(str)){

	         Pattern pattern = Pattern.compile("^([^\\u4e00-\\u9fa5])+");
	         Matcher matcher = pattern.matcher(str);
	         return matcher.find();
		}else{
			return false;
		}
	}
}

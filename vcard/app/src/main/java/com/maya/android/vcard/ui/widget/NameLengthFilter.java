package com.maya.android.vcard.ui.widget;


import android.text.InputFilter;
import android.text.Spanned;

import com.maya.android.vcard.util.ConverChineseCharToEnHelper;

/**
 * 名字字符长度限制类
 * @author zheng_cz
 * @since 2014年4月8日 下午2:27:40
 */
public class NameLengthFilter implements InputFilter {

	private int nMax;

	private int mCnLength;
	private int mEnLength;
	// 是否区分 中英文
	private boolean isDiff = false; 

	/**
	 * 
	 * @param cnLenth
	 *            中文字符长度,
	 * @param enLength
	 *            英文字符长度
	 */
	public NameLengthFilter(int cnLenth, int enLength) {
		this.mCnLength = cnLenth;
		this.mEnLength = enLength;
		this.isDiff = true;
	}

	public NameLengthFilter(int maxLength) {
		this.nMax = maxLength;
		this.isDiff = false;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		if (this.isDiff) {
			if (ConverChineseCharToEnHelper.isChinese(dest.toString())
					|| ConverChineseCharToEnHelper.isChinese(source.toString())) {
				this.nMax = this.mCnLength;
			} else {
				this.nMax = this.mEnLength;
			}
		}

		int keep = this.nMax - (dest.length() - (dend - dstart));

		if (keep <= 0) {
			return "";
		} else if (keep >= end - start) {
			return null; // keep original
		} else {
			return source.subSequence(start, start + keep);
		}

	}

}

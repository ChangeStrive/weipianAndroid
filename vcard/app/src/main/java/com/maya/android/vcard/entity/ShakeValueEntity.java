package com.maya.android.vcard.entity;

import java.util.Arrays;

/**
 * ShakeValueData
 * 
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-12-10
 * 
 */
public class ShakeValueEntity {
	private static final int ELEMENT_COUNT = 30;
	private static final float BLANK = -999;
	private int size;
	private float[] values;
	private int position = 0;
	
	public ShakeValueEntity(int size) {
		this.size = size;
		values = new float[size];
		clear();
	}
	
	public ShakeValueEntity(){
		this(ELEMENT_COUNT);
	}

	private void clear() {
		Arrays.fill(values, BLANK);
	}

	public boolean add(float value) {
		values[position] = value;
		if (size - 1 == position) {
			position = 0;
			return true;
		}
		position++;
		return false;
	}

	/**
	 * 取得中间值
	 * 
	 * @return
	 */
	public float getMedian() {
		float[] tmp = values.clone();
		Arrays.sort(tmp);
		int len = tmp.length;
		int first = 0;
		for (int i = 0; i < len; i++) {
			first = i;
			if (tmp[i] != BLANK)
				break;
		}
		return tmp[(len - first) / 2 + first];
	}
}
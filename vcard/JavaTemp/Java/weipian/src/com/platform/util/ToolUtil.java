package com.platform.util;

public class ToolUtil {
	
	/**
	 * 构建随机码
	 * @param length 随机码位数长度(最长9位)
	 * @return 随机码
	 * @author Saindy Su
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

}

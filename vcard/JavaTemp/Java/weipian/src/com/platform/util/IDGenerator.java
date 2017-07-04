package com.platform.util;

import java.util.UUID;

public class IDGenerator {
	/**
	 * 生成主键（32位）
	 * 
	 * @return
	 */
	public static String generateID() {
		String rtnVal = Long.toHexString(System.currentTimeMillis());
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 700; i++){
			if(i % 100 == 0){
				System.out.println(generateID());
			}
			
		}
		
	}
}

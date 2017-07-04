package com.platform.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.platform.weixin.util.WeiXinMessage;
import com.platform.weixin.util.WeiXinUtils;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		int total=2000;
		int count=total/200+(total%200>0?1:0);
		System.out.println(count);
	}

}

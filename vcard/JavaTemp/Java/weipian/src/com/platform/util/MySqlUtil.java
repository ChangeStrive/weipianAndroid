package com.platform.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MySqlUtil {

	public static void backup(String root,String pwd,String database,String savePath) {
		try {
			Runtime rt = Runtime.getRuntime();
			String str="mysqldump -u"+root+" -p"+pwd+" -R -c --set-charset=utf8 "+database;
			System.out.println(str);
			Process child = rt
					.exec("mysqldump -u "+root+" -p "+pwd+" -R -c --set-charset=utf8 "+database);

			InputStream in = child.getInputStream();

			InputStreamReader xx = new InputStreamReader(in, "utf8");

			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;

			BufferedReader br = new BufferedReader(xx);
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			FileOutputStream fout = new FileOutputStream(savePath);
			OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
			writer.write(outStr);
			writer.flush();

			in.close();
			xx.close();
			br.close();
			writer.close();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void load() {
		try {
			String fPath = "备份的文件路径名";
			Runtime rt = Runtime.getRuntime();

			Process child = rt.exec("mysql -u用户名 -p密码 数据库名");
			OutputStream out = child.getOutputStream();
			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fPath), "utf8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr);
			writer.flush();

			out.close();
			br.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		backup("root", "root", "test", "D:/zuivip.sql");
	}

}

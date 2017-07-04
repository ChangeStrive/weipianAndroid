package com.platform.util;

import java.awt.image.ImageProducer;
import java.io.File;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiWriter;
import com.sun.jimi.core.options.JPGOptions;

public class JpgUtil {
	
	public static void toJPG(String from,String to,int i) {
        try {
            String source = from;
            JPGOptions options = new JPGOptions();
            options.setQuality(72);
            ImageProducer image = Jimi.getImageProducer(source);
            JimiWriter writer = Jimi.createJimiWriter(to);
            writer.setSource(image);
            writer.setOptions(options);
            writer.putImage(to);
        } catch (JimiException je) {
            System.out.println("Error: " + i);
            je.printStackTrace();
        }
    }
	
	public static void dirToJpg(String fromDir,String toDir){
		File dir=new File(fromDir);
		File[] files = dir.listFiles();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					System.out.println("第"+i+"张照片:"+file.getAbsolutePath());
					toJPG(file.getAbsolutePath(),toDir+"/"+(29+i)+".jpg",i);
				}
			}
		}
	}
	
	public static void main(String[] args){
		JpgUtil.dirToJpg("D:/ad", "D:/d");
	}
}

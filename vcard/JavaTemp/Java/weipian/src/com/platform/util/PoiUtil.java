package com.platform.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;

public class PoiUtil {

	public static String  getStringValue(HSSFCell cell){
		if(cell==null){
			return null;
		}else{
			String result=null;
			 switch (cell.getCellType()) {  
		        case Cell.CELL_TYPE_BLANK:  
		        	result= "";  
		        	break;
		        case Cell.CELL_TYPE_BOOLEAN:  
		        	result=String.valueOf(cell.getBooleanCellValue());  
		        	break;
		        case Cell.CELL_TYPE_ERROR:  
		        	result= String.valueOf(cell.getErrorCellValue());  
		        	break;
		        case Cell.CELL_TYPE_FORMULA:  
		        	result= cell.getCellFormula();  
		        	break;
		        case Cell.CELL_TYPE_NUMERIC:  
		        	double a=cell.getNumericCellValue();
		        	result=String.valueOf(a);
		        	//int b=(int)cell.getNumericCellValue();
		        	/*if(a>b){
		        		result=String.valueOf(a);
		        	}else{
		        		result=String.valueOf(b);
		        	}*/
		        	break;
		        case Cell.CELL_TYPE_STRING:  
		        	result= cell.getStringCellValue();  
		        	break;
		        default:  
		        	break;
			 }
			 if(result!=null&&result.equals("(null)")){
				 return null;
			 }
			 return result;
		}
	}
	
	public static String  getStringValueOfInteger(HSSFCell cell){
		if(cell==null)
			return null;
		else
			return Double.toString(cell.getNumericCellValue());
	}
	
	public static void addImage(String fdPath,HSSFWorkbook wwb,HSSFSheet ws,int row,int column) throws IOException{
		try{
			if(StringUtil.isNotNull(fdPath)){
				 BufferedImage bufferImg = null;   
				 ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();   
		         bufferImg = ImageIO.read(new File(fdPath));   
		         String[] types=fdPath.split("\\.");
		         String type=types[types.length-1];
		         ImageIO.write(bufferImg, type, byteArrayOut);   
		         
		         HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1000, 255,   
		                 (short) column, row, (short) column, row);   
		
		         HSSFPatriarch patriarch = ws.createDrawingPatriarch(); 
		         if(type.equals("jpg")||type.equals("jpeg")){
		        	 patriarch.createPicture(anchor, wwb.addPicture(byteArrayOut   
		                 .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));  
		         }
		         
		         if(type.equals("png")){
		        	 patriarch.createPicture(anchor, wwb.addPicture(byteArrayOut   
		                 .toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));  
		         }
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static Map<String,String> getExcelPic(HttpServletRequest request,HSSFWorkbook workbook,String savePath) throws InvalidFormatException, IOException{
		Map<String,String> map=new HashMap();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
		
        String tomcatDir=PropertiesUtil.getPropertiesValue("tomcatDir");
		String folderRoot="";
		
		if(StringUtil.isNotNull(tomcatDir)&&tomcatDir.equals("1")){
			//保存在项目路径下
			folderRoot=request.getSession().getServletContext().getRealPath("/");
		}else{
			//保存在其他盘下
			folderRoot=PropertiesUtil.getPropertiesValue("fileSavePath");
		}
		String savePath2=folderRoot+savePath;
		File newfolder = new File(savePath2);
		if(!newfolder.exists()) {
			newfolder.mkdirs();
		}
        for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
            HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
            if (shape instanceof HSSFPicture) {
                HSSFPicture pic = (HSSFPicture) shape;
                int row = anchor.getRow1();
                int cols = anchor.getCol1();
                String key=row+";"+cols;
                if(!map.containsKey(key)){
                	int pictureIndex = pic.getPictureIndex()-1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    byte[] data = picData.getData();
                    String ext = picData.suggestFileExtension();
            		String id=IDGenerator.generateID();
            		String path=savePath2+"/"+id+"."+ext;
            		FileOutputStream out = new FileOutputStream(path);
            	    out.write(data);
            	    out.close();
                    map.put(key, savePath+"/"+id+"."+ext);
                }
            }
            
        }
        return map;
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public static void main(String[] args) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
		 /*InputStream inp = new FileInputStream(
         "C:\\Users\\Administrator\\Desktop\\goods.xls");
		 getExcelPic(inp);*/
	}

}

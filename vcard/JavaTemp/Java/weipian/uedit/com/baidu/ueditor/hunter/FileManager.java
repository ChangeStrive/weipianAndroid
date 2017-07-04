package com.baidu.ueditor.hunter;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.BinaryUploader;
import com.platform.util.PropertiesUtil;
import com.platform.util.StringUtil;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager
{
  private String dir = null;
  private String rootPath = null;
  private String[] allowFiles = null;
  private int count = 0;

  private String downAction = null;
  private String fileSavePath = null;

  private static Logger logger=LoggerFactory.getLogger(FileManager.class); 
  
  public FileManager(Map<String, Object> conf, HttpServletRequest request) {
    this.rootPath = ((String)conf.get("rootPath"));
    String tomcatDir = PropertiesUtil.getPropertiesValue("tomcatDir");
    if ((StringUtil.isNotNull(tomcatDir)) && (tomcatDir.equals("1")))
      this.fileSavePath = request.getSession().getServletContext().getRealPath("/");
    else {
      this.fileSavePath = PropertiesUtil.getPropertiesValue("fileSavePath");
    }
    this.dir = (this.fileSavePath + (String)conf.get("dir"));

    this.allowFiles = getAllowFiles(conf.get("allowFiles"));
    this.count = ((Integer)conf.get("count")).intValue();
    this.downAction = ((String)conf.get("downAction"));
  }

  public State listFile(HttpServletRequest request, int index)
  {
    File dir = new File(this.dir);
    State state = null;

    if (!dir.exists()) {
      return new BaseState(false, 302);
    }

    if (!dir.isDirectory()) {
      return new BaseState(false, 301);
    }

    Collection list = FileUtils.listFiles(dir, this.allowFiles, true);

    if ((index < 0) || (index > list.size())) {
      state = new MultiState(true);
    } else {
      Object[] fileList = Arrays.copyOfRange(list.toArray(), index, index + this.count);
      state = getState(request, fileList);
    }

    state.putInfo("start", index);
    state.putInfo("total", list.size());

    return state;
  }

  private State getState(HttpServletRequest request, Object[] files)
  {
    MultiState state = new MultiState(true);
    BaseState fileState = null;

    File file = null;

    for (Object obj : files) {
      if (obj == null) {
        break;
      }
      file = (File)obj;
      fileState = new BaseState(true);
      String hxltUrl = request.getScheme()+"://"+ request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
      String urls = hxltUrl + "/" + this.downAction + PathFormat.format(getPath(file));
      fileState.putInfo("url", urls);
      state.addState(fileState);
    }

    return state;
  }

  private String getPath(File file)
  {
    String path = file.getAbsolutePath();
    String str = "";
    if(System.getProperty("file.separator").equals("\\")){
    	//windows
    	logger.info("windows:");
    	str = path.replace(this.fileSavePath.replaceAll("/", "\\\\"), "");
    }else{
    	//linux
    	logger.info("linux:");
    	logger.info("fileSavePath:"+fileSavePath);
    	logger.info("path:"+path);
    	str = path.replace(this.fileSavePath.replaceAll("\\\\", "/"), "");
    	logger.info("str:"+str);
    }
    return str;
  }

  private String[] getAllowFiles(Object fileExt)
  {
    String[] exts = (String[])null;
    String ext = null;

    if (fileExt == null) {
      return new String[0];
    }

    exts = (String[])fileExt;

    int i = 0; for (int len = exts.length; i < len; i++)
    {
      ext = exts[i];
      exts[i] = ext.replace(".", "");
    }

    return exts;
  }
}
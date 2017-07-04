<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <META http-equiv="X-UA-Compatible" content="IE=9" ></META>
	<title>MST管理平台</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="../utils/singleUpload/singleUpload.css">
    <script type="text/javascript"  src="../jquery/jquery1.8.0.js"></script>
    <script type="text/javascript"  src="../jquery/jquery.form.js"></script>
    
    <script type="text/javascript" charset="utf-8" src="../../ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="../../ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript">
    	var hxltUrl="http://localhost:8080/springmvc/";
    </script>
    <script type="text/javascript" src="../utils/singleUpload/singleUpload.js"></script>
    <script type="text/javascript" src="WebGoodsImage.js"></script>
    <script>
    </script>
</head>
<body>
	<input type="hidden" name="fdPicUrl"  singUpload-Type="image"  singUpload-width="200" singUpload-height="200"/>
	<a class="btn" onclick="WebGoodsImageConfig.upload();" href="javascript:;">新 增</a>
	<script type="text/plain" id="imageUpload"></script>
	
</body>
</html>
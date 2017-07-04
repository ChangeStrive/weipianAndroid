var WebGoodsImageConfig={
	upload:function(){
		 var myImage = WebGoodsImageConfig.ueditor.getDialog("insertimage");
	     myImage.open();
	},
	uploadInit:function(){
		WebGoodsImageConfig.ueditor = UE.getEditor('imageUpload');
	    WebGoodsImageConfig.ueditor.ready(function () {
		        //设置编辑器不可用
		        WebGoodsImageConfig.ueditor .setDisabled();
		        //隐藏编辑器，因为不会用到这个编辑器实例，所以要隐藏
		        WebGoodsImageConfig.ueditor .hide();
		        //侦听图片上传
		        WebGoodsImageConfig.ueditor .addListener('beforeInsertImage', function (t, arg) {
		            //将地址赋值给相应的input
		              if(arg.length>0){
		            	var imageUrls=[];
		            	for(var i=0;i<arg.length;i++){
		            		imageUrls.push(arg[i].src.split("=")[1]);
		            	}
		            	alert(imageUrls);
		            	//WebGoodsImageConfig.imageUpload(imageUrls);
		              }
		        })
		        //侦听文件上传
		        WebGoodsImageConfig.ueditor .addListener('afterUpfile', function (t, arg) {
		             if(arg.length>0){
		             	/*var imageUrls=[];
		            	for(var i=0;i<arg.length;i++){
		            		imageUrls.push(arg[i].url.split("=")[1]);
		            	}
		            	WebGoodsImageConfig.imageUpload(imageUrls);*/
		              }
		        })
		});
	},
	init:function(){
		WebGoodsImageConfig.uploadInit();
	}
};

$(function(){
	WebGoodsImageConfig.init();
});
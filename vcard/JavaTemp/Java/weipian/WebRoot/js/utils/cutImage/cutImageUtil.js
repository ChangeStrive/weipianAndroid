var cutImageObject=function(){
	this.title='裁剪图片';
	this.width=500;
	this.height=500;
	this.aspectRatio=1;
	this.init=function(config){
		$.extend(true,this,config);
		this.initWin();
		this.initCutImage();
		this.initSureFn();
	};
	this.initCutImage=function(){
		var config=this;
		var a=1;
		var b=1;
		if(config.aspectRatio.indexOf("/")==-1){
			a=parseInt(config.aspectRatio);
		}else{
			var s=config.aspectRatio.split("/");
			a=parseInt(s[0]);
			b=parseInt(s[1]);
		}
		config.win.find('.cutImage').Jcrop({
			aspectRatio: a/b
		},function(){
			config.cutImage = this;
			var bunds=config.cutImage.getBounds();
			config.cutImage.animateTo([0, 0, bunds[0], bunds[1]]);
	    });
	};
	this.cutImageFn=function(){
		var config=this;
		var params=config.cutImage.tellSelect();
		var bunds=config.cutImage.getBounds();
		params["imageUrl"]=config.win.find('.cutImage').attr("src");
		params["savePath"]="goods";
		params["bx"]=bunds[0];
		params["by"]=bunds[1];
		$.ajax({
		  type: "POST",
		  url: hxltUrl+"SysFileAction/cutImage",
		  contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		  data:params,
		  dataType: "json",
		  success: function(result){
		 	if(result.success == true){
		 		if(config.success){
		 			config.success(result.path);
		 		}
		 		config.win.window('close');
			}
		  }
		});
	};
	this.initSureFn=function(){
		var config=this;
		config.win.find(".cutImageBtn").on("click",function(){
			config.cutImageFn();
		});
	};
	this.initWin=function(){
		var str='<div id="cutWin-win'+this.id+'" title="'+this.title+'" style="width:'+this.width+'px;height:'+this.height+'px" >'  
				+'<div  class="easyui-layout" data-options="fit:true">'
				+' <div class="cutImageBox"><img src="'+this.imageSrc+'" class="cutImage"></div>' 
				+'<div class="cutImageBtnBox"><a href="javascript:void(0);" class="cutImageBtn">裁剪图片</a></div>' 
				+'</div>'
		+'</div>';
		$("body").append(str);
		var config=this;
		config.win=$("#cutWin-win"+config.id);
		config.win.window({    
		    width:config.width,    
		    height:config.height,
		    title:config.title,
		    maximizable:true,
		    minimizable:false,
		    collapsible:false,
		    resizable:true,
		    modal:true
		});
		config.win.find(".easyui-layout").layout();
	};
}
var cutImageUtils={
	list:{},
	init:function(config){
		var o=cutImageUtils.list[config.id];
		if(o){
			o.cutImage.setImage(config.imageSrc);
			o.win.window('open');
		}else{
			o=new cutImageObject();
			o.init(config);
			cutImageUtils[config.id]=o;
		}
	}
}

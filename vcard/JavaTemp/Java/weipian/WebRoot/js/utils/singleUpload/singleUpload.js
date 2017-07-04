var message=function(s){
	alert(s.path);
}
var singleUploadUtils={
	id:0,
	makeButton:function(o,input,type){
		o.btn=$('<span class="singleUpload-btn"></span>').appendTo(o.table);
		o.w=80;o.h=40;
		o.btnStyle = 'display:block;width:' + o.w + 'px;height:' + o.h + 'px;overflow:hidden;border:0;margin:0;padding:0;position:absolute;top:0;left:0;filter:alpha(opacity=0);-moz-opacity:0;-khtml-opacity: 0;opacity: 0;cursor:pointer;';
		o.iframeDiv=$('<div class="singleUpload-upload"><span>上&nbsp传</span></div>').appendTo(o.btn);
		var hasCut=$(input).attr("hasCut");
		if(hasCut){
			o.cutBtn=$('<div class="singleUpload-cutImage">裁剪</div>').appendTo(o.btn);
			o.cutBtn.on("click",function(){
				var imageSrc=input.val();
				var aspectRatio=input.attr("aspectRatio");
				var cutW=input.attr("cutW");
				var cutH=input.attr("cutH");
				if(!imageSrc){
					alert("请先上传图片在裁剪");
				}else{
					singleUploadUtils.id++;
					cutImageUtils.init({
						imageSrc:downAction+imageSrc,
						aspectRatio:aspectRatio,
						width:cutW,
						height:cutH,
						id:singleUploadUtils.id,
						success:function(src){
							input.val(src);
							o.image.find("img").attr("src",downAction+src);
						}
					});
				}
			});
		}else{
			if(type=="image"){
				o.iframeDiv.addClass("singleUpload-upload2");
			}
		}
		o.iframe = $("<iframe id='iframe1'  style='"+o.btnStyle+"'/>").appendTo(o.iframeDiv);
		o.isUpload=false;
		setTimeout(function(){
			if(o.iframe[0].contentWindow){
				o.iframeDocument= o.iframe[0].contentWindow.document;
			}else if(o.iframe[0].contentDocument){
				o.iframeDocument= o.iframe[0].contentDocument.document;
			}	
			o.iframeBody=$(o.iframeDocument.body);
			var savePath="imgDir";
			if(input.attr("singUpload-savePath")){
				savePath=input.attr("singUpload-savePath");
			}
			var saveCurrentPath="";
			if(input.attr("singUpload-saveCurrentPath")){
				saveCurrentPath=input.attr("singUpload-saveCurrentPath");
			}
			if(type=="image"){
				o.iframeHtml= '<form  method="POST" enctype ="multipart/form-data" action="'+hxltUrl+'SysFileAction/upload" style="' + o.btnStyle + '" >'+
	            '<input type="file" name="file" accept="image/*" style="' + o.btnStyle + '"/>' +
	             '<input type="hidden" name="savePath" value="'+savePath+'"/>' +
	             '<input type="hidden" name="saveCurrentPath" value="'+saveCurrentPath+'"/>' +
	             '<input type="hidden" name="maxSize" value="500"/>' +
	            '</form>';
			}else{
				o.iframeHtml= '<form  method="POST" enctype ="multipart/form-data" action="'+hxltUrl+'SysFileAction/upload" style="' + o.btnStyle + '" >'+
	            '<input type="file" name="file"  style="' + o.btnStyle + '"/>' +
	            '<input type="hidden" name="saveCurrentPath" value="'+saveCurrentPath+'"/>' +
	             '<input type="hidden" name="savePath" value="'+savePath+'"/>' +
	            '</form>';
			}
            if (o.iframeBody[0].parentNode) {
                o.iframeBody[0].parentNode.style.width = o.w + 'px';
                o.iframeBody[0].parentNode.style.height = o.h + 'px';
            }
            o.iframeBody.append(o.iframeHtml);
            o.iframeBody.find("input[type='file']").on("change",function(){
            	if(o.isUpload){
            		return ;
            	}else{
            		if(!$(this).val()){
            			return ;
            		}
            		o.isUpload=true;
            		o.iframeDiv.find("span").html("上传中");
            		o.iframeDiv.addClass("singleUpload-beginUpload");
	            	o.iframeBody.find("form").ajaxSubmit({
	            	 	  dataType: "json",
	            		  success: function (result, status) {
	            		  		o.isUpload=false;
	            		  		if(result.success){
		            		  		input.val(result.path);
		            		  		if(o.image){
		            		  			o.image.find("img").attr("src",downAction+result.path);
		            		  		}
		            		  		if(o.file){
		            		  			var filePath=downAction+result.path;
		            		  			filePath='<a href="'+filePath+'" target="_blank">'+filePath+'</a>';
		            		  			$(".singUpLoad-fileLabel"+o.fileId).html(filePath);
		            		  		}
		            		  		var fn=input.attr("singUpload-Complete");
		            		  		if(fn){
		            		  			var completeFn=eval('(' + fn + ')'); 
		            		  			completeFn(result);
		            		  		}
	            		  		}else{
	            		  			alert(result.msg);
	            		  		}
	            		  		o.iframeDiv.find("span").html("上&nbsp传");
	            		  		o.iframeDiv.removeClass("singleUpload-beginUpload");
	            		  }
	            	});
            	}
            });
		},5);
	},
	makeImageButton:function(input){
		var o=new Object();
		var width=input.attr("singUpload-width")?input.attr("singUpload-width"):"100";
		var height=input.attr("singUpload-height")?input.attr("singUpload-height"):"100";
		o.table=$('<div class="singleUpload-imageTable"></div>').insertAfter(input);
		var fm=input.attr("fm");
		var imgurl="";
		if(fm){
			imgurl=hxltUrl+'js/utils/singleUpload/'+fm;
		}else{
			imgurl=hxltUrl+'js/utils/singleUpload/uploadImage.jpg';
		}
		o.image=$('<div class="singleUpload-image"><img width="'+width+'px" height="'+height+'px" src="'+imgurl+'"/></div>').appendTo(o.table);
		o.table[0].style.width=width+"px";
		o.table[0].style.height=(parseInt(height)+50)+"px";
		var picUrl=input.val();
		if(picUrl){
			o.image.find("img").attr("src",downAction+picUrl);
		}
		singleUploadUtils.makeButton(o,input,"image");
	},
	makeFileButton:function(input){
		var o=new Object();
		var file=input.val();
		var filePath="";
		singleUploadUtils.id++;
		if(file){
			filePath=downAction+file;
			filePath='<a href="'+filePath+'" target="_blank">'+filePath+'</a>';
		}
		o.fileId=singleUploadUtils.id;
		o.file=$('<span class="singUpLoad-fileLabel'+singleUploadUtils.id+'">'+filePath+'</span>').insertBefore(input);
		o.table=$('<span class="singleUpload-fileTable"></span>').insertAfter(input);
		singleUploadUtils.makeButton(o,input,"file");
		
	},
	init:function(){
		$("input[singUpload-Type='image']").each(function(){
			var input=$(this);
			singleUploadUtils.makeImageButton(input);
		});
		$("input[singUpload-Type='file']").each(function(){
			var input=$(this);
			singleUploadUtils.makeFileButton(input);
		});
	}
};
$(function(){
	singleUploadUtils.init();
});
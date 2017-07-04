var o=new Object();
var singleUploadUtils={
	init:function(){
		o.w=80;o.h=40;
		o.btnStyle = 'display:block;width:' + o.w + 'px;height:' + o.h + 'px;overflow:hidden;border:0;margin:0;padding:0;position:absolute;top:0;left:0;filter:alpha(opacity=0);-moz-opacity:0;-khtml-opacity: 0;opacity: 0;cursor:pointer;';
		o.iframeDiv=$('<div class="uedit-upload"><span>上&nbsp传</span></div>').appendTo("body");
		o.iframe = $("<iframe id='iframe1'  style='"+o.btnStyle+"'/>").appendTo(o.iframeDiv);
		o.isUpload=false;
		setTimeout(function(){
			if(o.iframe[0].contentWindow){
				o.iframeDocument= o.iframe[0].contentWindow.document;
			}else if(o.iframe[0].contentDocument){
				o.iframeDocument= o.iframe[0].contentDocument.document;
			}	
			o.iframeBody=$(o.iframeDocument.body);
			o.iframeHtml= '<form  method="POST" enctype ="multipart/form-data" action="'+hxltUrl+'SysFileAction/upload.action" style="' + o.btnStyle + '" >'+
            '<input type="file" name="file" accept="image/*" style="' + o.btnStyle + '"/>' +
             '<input type="hidden" name="folder" value="imgDir"/>' +
            '</form>';
            if (o.iframeBody[0].parentNode) {
                o.iframeBody[0].parentNode.style.width = o.w + 'px';
                o.iframeBody[0].parentNode.style.height = o.h + 'px';
            }
            o.iframeBody.append(o.iframeHtml);
            o.iframeBody.find("input[type='file']").on("change",function(){
            	if(o.isUpload){
            		return ;
            	}else{
            		o.isUpload=true;
            		o.iframeDiv.find("span").html("上传中");
            		o.iframeDiv.addClass("uedit-beginUpload");
	            	o.iframeBody.find("form").ajaxSubmit({
	            	 	  dataType: "json",
	            		  success: function (html, status) {
	            		  		o.isUpload=false;
	            		  		alert(html.path);
	            		  		o.iframeDiv.removeClass("uedit-beginUpload");
	            		  		o.iframeDiv.find("span").html("上&nbsp传");
	            		  }
	            	});
            	}
            });
		},5);
	}
};
$(function(){
	singleUploadUtils.init();
});
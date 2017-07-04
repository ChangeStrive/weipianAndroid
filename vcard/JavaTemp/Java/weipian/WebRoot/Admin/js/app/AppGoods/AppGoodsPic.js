var AppGoodsPicConfig={
	upload:function(){
		 var myImage = AppGoodsPicConfig.ueditor.getDialog("insertimage");
	     myImage.open();
	},
	imageUpload:function(imageUrls){
		var params={};
		params["fdGoodsId"]=fdId;
		params["imageUrls"]=imageUrls.join();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsPicAction/save",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		window.location.reload();
	        	}
	        }
		});
	},
	uploadInit:function(){
		AppGoodsPicConfig.ueditor = UE.getEditor('imageUpload');
	    AppGoodsPicConfig.ueditor.ready(function () {
		        //设置编辑器不可用
		        AppGoodsPicConfig.ueditor .setDisabled();
		        //隐藏编辑器，因为不会用到这个编辑器实例，所以要隐藏
		        AppGoodsPicConfig.ueditor .hide();
		        //侦听图片上传
		        AppGoodsPicConfig.ueditor .addListener('beforeInsertImage', function (t, arg) {
		            //将地址赋值给相应的input
		              if(arg.length>0){
		            	var imageUrls=[];
		            	for(var i=0;i<arg.length;i++){
		            		imageUrls.push(arg[i].src.split(downAction)[1]);
		            	}
		            	AppGoodsPicConfig.imageUpload(imageUrls);
		              }
		        })
		        //侦听文件上传
		        AppGoodsPicConfig.ueditor .addListener('afterUpfile', function (t, arg) {
		             if(arg.length>0){
		             	/*var imageUrls=[];
		            	for(var i=0;i<arg.length;i++){
		            		imageUrls.push(arg[i].url.split("=")[1]);
		            	}
		            	AppGoodsPicConfig.imageUpload(imageUrls);*/
		              }
		        })
		});
	},
	appendHtml:function(item){
		var str='<tr>'
			+'<td><input type="checkbox" class="check-single" value="{fdId}"/></td>'
			+'<td><img width="100px" height="100px" src="'+hxltUrl+'SysFileAction/downFile?path={fdPicUrl}"/></td>'
			+'<td><input type="number" name="fdSeqNo"  class="text input-large" fdId="{fdId}" value="{fdSeqNo}"/></td>'
		+'</tr>';
		str=str.replaceAll("{fdId}",item.fdId).replaceAll("{fdSeqNo}",item.fdSeqNo).replaceAll("{fdPicUrl}",item.fdPicUrl);
		$("#tableList").append(str);
	},
	getSelectedIndex:function(){
		var ids=[];
		$("#tableList").find(".check-single:checked").each(function(){
			ids.push($(this).val());
		});
		return ids;
	},
	checkBoxInit:function(){
		var self=$("#tableList");
		self.find(".check-all").on("change",function(){
			var cb=self.find(".check-single");
			if($(this).attr("checked")){
				cb.each(function(){
					this.checked=true;
				});
			}else{
				cb.each(function(){
					this.checked=false;
				});
			}
		});
		self.find(".check-single").live("change",function(){
			var cb=self.find(".check-single:checked");
			if(cb.length==0){
				self.find(".check-all")[0].checked=false;
			}
			if(cb.length==table.fnGetData().length){
				self.find(".check-all")[0].checked=true;
			}
		});
	},
	updateFdSeqNo:function(fdId,fdSeqNo){
		var params={};
		params["fdId"]=fdId;
		params["fdSeqNo"]=fdSeqNo;
		$.ajax({
		        type:'POST',
		        url:hxltUrl+"AppGoodsPicAction/updateFdSeqNo",
		        data:params,
		        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		        dataType: "json",
		        success: function(data){
		        	
		        }
			});
	},
	load:function(){
		var params={};
		params["fdGoodsId"]=fdId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsPicAction/getList",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				AppGoodsPicConfig.appendHtml(item);
	        			}
	        			AppGoodsPicConfig.checkBoxInit();
	        			$("input[name='fdSeqNo']").on("blur",function(){
	        				var fdSeqNo=$(this).val();
	        				var fdId=$(this).attr("fdId");
	        				AppGoodsPicConfig.updateFdSeqNo(fdId,fdSeqNo);
	        			});
	        		}
	        	}
	        }
		});
	},
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=AppGoodsPicConfig.getSelectedIndex();
			if(fdIds.length==0){
				updateAlert("请选择要删除的记录!","alert-warn");
				return ;
			}else{
				params["fdId"]=fdIds.join();
			}
		}
		var flag=confirm("删除后不能恢复，是否删除？");
		if(!flag){
			return ;		
		}
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsPicAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		window.location.reload();
	        	}
	        }
		});
	},
	init:function(){
		AppGoodsPicConfig.uploadInit();
		AppGoodsPicConfig.load();
	}
};

$(function(){
	AppGoodsPicConfig.init();
});
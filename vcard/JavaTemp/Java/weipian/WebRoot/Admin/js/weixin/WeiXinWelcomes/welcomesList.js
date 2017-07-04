var WeiXinWelcomesConfig = {
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WeiXinWelcomesConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WeiXinWelcomesAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WeiXinWelcomesConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"WeiXinWelcomesAction/getList",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		WeiXinWelcomesConfig.appendHtml(data.list);
	        	}else{
	        		updateAlert("加载失败!","alert-error");
	        	}
	        }
		});
	},
	appendHtml:function(data) {
		var fast = 1;
		var middle = 2;
		for (var i = 1; i < data.length+1; i++) {
			var item = data[i-1];
			var childHtml = '';
			var fdPicUrl = item.fdPicUrl == '' ? 'Admin/images/fmdtp.jpg':hxltUrl+'SysFileAction/downFile?path='+item.fdPicUrl;
			if(item.child != null) {
				for (var j = 0; j < item.child.length; j++) {
					var child = item.child[j];
					var childFdPicUrl = child.fdPicUrl == '' ? 'Admin/images/fmdtp.jpg':hxltUrl+'SysFileAction/downFile?path='+child.fdPicUrl;
					childHtml += '<div class="rel sub-msg-item appmsgItem">' +
						'<span class="thumb"><img src="'+childFdPicUrl+'" class="i-img" style=""></span>' +
						'<h4 class="msg-t">' +
						'<a href="javascript:;" target="_blank" class="i-title">'+child.fdTitle+'</a>' +
						'</h4>' +
						'</div>';
				}
			}
			var str = '<div class="msg-item-wrapper"><div class="msg-item multi-msg" onClick="WeiXinWelcomesConfig.setDefaultClick(\''+item.fdId+'\',this)"><div class="appmsgItem">' +
					'<h4 class="msg-t"><a href="javascript:;" class="i-title">'+item.fdTitle+'</a></h4>' +
					'<p class="msg-meta"><span class="msg-date">'+item.fdCreateTime+'</span></p>' +
					'<div class="cover"><p class="default-tip" style="display:none">封面图片</p><img src="'+fdPicUrl+'" class="i-img" style="">' +
					'</div><p class="msg-text">'+item.fdContent+'</p></div>' +childHtml+
					'</div>'+
					'<div class="msg-opr">' +
					'<ul class="f0 msg-opr-list">' +
					'<li class="b-dib opr-item"><a data-mul="false" class="block tc opr-btn edit-btn" href="'+hxltUrl+'WeiXinWelcomesAction/toEdit?fdId='+item.fdId+'"><span class="th vm dib opr-icon edit-icon">编辑</span></a></li>' +
					'<li class="b-dib opr-item"><a class="block tc opr-btn del-btn" onclick="WeiXinWelcomesConfig.del()" href="javascript:;"><span class="th vm dib opr-icon del-icon">删除</span></a></li>' +
					'</ul>' +
					'</div>';
			var $str=$(str);
			if(fast==i){
				$(".list-fist").append($str);
				fast=i+3;
			} else if(middle==i){
				$(".list-middle").append($str);
				middle=i+3;
			} else{
				$(".list-last").append($str);
			}
			if(item.fdStatus=="1"){
				WeiXinWelcomesConfig.setDefaultClick(item.fdId,$str.find(".msg-item"));
			}
			
		}
		
	},
	setDefaultClick:function(fdId,me){
		var top = $(me).offset().top;
		var left = $(me).offset().left;
		var height = $(me).height();
		var width = $(me).width();
		$(".maskdivgen").removeAttr("style");
		$(".maskdivgen").css({"position":"absolute","top":top,"left":left,"z-index":"88","width":width,"height":height+5,"opacity":"0.5","background-color":"rgb(0,0,0)"});
		$("#ddhpng").removeAttr("style");
		$("#ddhpng").css({"position": "absolute","z-index": "99","left": left+width/2-36,"top":top+(height+5)/2-30});
		$(".setDef").attr("fdId",fdId);
		//str='position: absolute; top: 302px; left: 79px; z-index: 811200; width: 348px; height: 245px; opacity: 0.5; background-color: rgb(0, 0, 0);';
		
	},
	setDefault:function(me){
		var fdId = $(me).attr("fdId");
		var params = {};
		params["fdId"] = fdId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"WeiXinWelcomesAction/setDefault",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("设置成功!","alert-success");
	        	}else{
	        		updateAlert("设置失败!","alert-error");
	        	}
	        }
		});
	},
	init:function(){
		WeiXinWelcomesConfig.load();
	}
};

$(function(){
	WeiXinWelcomesConfig.init();
})
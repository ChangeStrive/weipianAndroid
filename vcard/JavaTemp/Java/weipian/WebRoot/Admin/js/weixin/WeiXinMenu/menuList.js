var WeiXinMenuConfig={
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WeiXinMenuConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WeiXinMenuAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WeiXinMenuConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		params["fdPid"]=fdPid;
		WeiXinMenuConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WeiXinMenuAction/getList',
			formId:'.search-form',
			params:params,
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a href="'+hxltUrl+'WeiXinMenuAction/list?fdPid='+obj.aData.fdId+'">'+obj.aData.fdName+'</a>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var str = obj.aData.fdPid!="#"?obj.aData.fdPidName:"顶级菜单";
							return str;
						}
					},{
						"mDataProp" : "fdKey",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdUrl",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdType="";
							if(obj.aData.fdType=="0"){
								fdType="KEY";
							}else if(obj.aData.fdType=="1"){
								fdType="URL";
							}else if(obj.aData.fdType=="2"){
								fdType="扫码带提示";
							}else if(obj.aData.fdType=="3"){
								fdType="扫码推事件";
							}
							return fdType;
						}
					},{
						"mDataProp" : "fdSeq",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus== "1" ? "<span class='label label-success'>已授权</span>" : "<span class='label label-error'>未授权</span>";
							return fdStatus;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a href="'+hxltUrl+'WeiXinMenuAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><a onclick="WeiXinMenuConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	},
	makeMenu:function(){
		var flag=confirm("是否确定生成菜单？");
		if(!flag){
			return ;		
		}
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"WeiXinMenuAction/buildMenu",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("生成成功!","alert-success");
	        		WeiXinMenuConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("生成失败!","alert-error");
	        	}
	        }
		});
    }
};
$(function(){
	WeiXinMenuConfig.load();
});
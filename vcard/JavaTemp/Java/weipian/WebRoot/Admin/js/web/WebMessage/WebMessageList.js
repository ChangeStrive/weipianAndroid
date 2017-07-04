var WebMessageConfig = {
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WebMessageConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WebMessageAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WebMessageConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		WebMessageConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WebMessageAction/getList',
			formId:'.search-form',
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"mDataProp" : "fdUserName",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdEmail",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdTel",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdContent",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdReplay",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus== "1" ? "<span class='label label-success'>是</span>" : "<span class='label label-error'>否</span>";
							return fdStatus;
						}
					},{
						"mDataProp" : "fdCreateTime",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus == "1" ? "禁用" : "启用";
							return '<a href="'+hxltUrl+'WebMessageAction/toEdit?fdId='+obj.aData.fdId+'" class="button">回复</a><a href="javascript:;" onclick="WebMessageConfig.enableOrDisabled(\''+obj.aData.fdId+'\',\''+obj.aData.fdStatus+'\')" class="button">'+fdStatus+'</a><a onclick="WebMessageConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	},
	enableOrDisabled:function(fdId,fdStatus){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
			params["fdStatus"] = fdStatus == "1" ? "0" : "1";
		}else{
			var fdIds=WebMessageConfig.table.getSelectedIndex();
			if(fdIds.length==0){
				updateAlert("请选择记录!","alert-warn");
				return ;
			}else{
				params["fdId"]=fdIds.join();
				params["fdStatus"] = fdStatus;
			}
		}
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"WebMessageAction/modifStatus",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		var msg = "";
	        		if(fdId)msg = fdStatus == "1" ? "已禁用" : "已启用";
	        		else msg = fdStatus == "1" ? "已启用" : "已禁用";
	        		updateAlert(msg,"alert-success");
	        		WebMessageConfig.table.fnPageChange(0);
	        	}else{
	        		var msg = fdStatus == "1" ? "禁用失败" : "启用失败";
	        		updateAlert(msg,"alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	WebMessageConfig.load();
});
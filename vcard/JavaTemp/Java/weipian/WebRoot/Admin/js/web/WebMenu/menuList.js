var WebMenuConfig={
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WebMenuConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WebMenuAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WebMenuConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		params["fdPid"]=fdPid;
		WebMenuConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WebMenuAction/getList',
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
							return '<a href="'+hxltUrl+'WebMenuAction/list?fdPid='+obj.aData.fdId+'">'+obj.aData.fdTitle+'</a>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var str = obj.aData.fdPid!="#"?obj.aData.fdPidName:"顶级菜单";
							return str;
						}
					},{
						"mDataProp" : "fdUrl",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdSeqNo",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a href="'+hxltUrl+'WebMenuAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><a onclick="WebMenuConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	}
};
$(function(){
	WebMenuConfig.load();
});
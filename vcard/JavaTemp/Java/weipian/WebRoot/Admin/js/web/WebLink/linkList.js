var WebLinkConfig = {
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WebLinkConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WebLinkAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WebLinkConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		WebLinkConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WebLinkAction/getList',
			formId:'.search-form',
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"mDataProp" : "fdPicUrl",
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdPicUrl = obj.aData.fdPicUrl;
							return "<img width='100' height='100' src='"+downAction+fdPicUrl+"'/>";
						}
					},{
						"mDataProp":"fdTitle",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdUrl",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdSeqNo",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a href="'+hxltUrl+'WebLinkAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><a onclick="WebLinkConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	}
};
$(function(){
	WebLinkConfig.load();
});
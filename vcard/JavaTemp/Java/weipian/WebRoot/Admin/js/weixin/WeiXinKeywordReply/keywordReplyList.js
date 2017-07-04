var WeiXinKeywordReplyConfig = {
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WeiXinKeywordReplyConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WeiXinKeywordReplyAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WeiXinKeywordReplyConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		WeiXinKeywordReplyConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WeiXinKeywordReplyAction/getList',
			formId:'.search-form',
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"mDataProp":"fdKeyword",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdTitle",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdUrl",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdType = obj.aData.fdType == "1" ? "图文":"文本";
							return fdType;
						}
					},{
						"mDataProp" : "fdCreateTime",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus == "1" ? "<span class='label label-success'>启用</span>" : "<span class='label label-error'>禁用</span>";
							return fdStatus;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a href="'+hxltUrl+'WeiXinKeywordReplyAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><a onclick="WeiXinKeywordReplyConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	}
};
$(function(){
	WeiXinKeywordReplyConfig.load();
});
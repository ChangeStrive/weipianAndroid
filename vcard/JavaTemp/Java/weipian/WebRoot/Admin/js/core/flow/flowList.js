var flowNumRuleConfig = {
	load:function(){
		flowNumRuleConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'SysFlowNumRuleAction/getList',
			formId:'.search-form',
	        "aoColumns" : [{
					"sWidth" : "auto",
					"fnRender":function(obj){
						return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
					}
				},{
					"mDataProp":"fdClassName",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdRemark",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdRule",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdStartValue",
					"sWidth" : "auto"
				},{
					"sWidth" : "auto",
					"fnRender":function(obj){
						return '<a href="'+hxltUrl+'SysFlowNumRuleAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><a onclick="flowNumRuleConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
					}
				}
			]
	    });
	},
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=flowNumRuleConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"SysFlowNumRuleAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		flowNumRuleConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	edit:function(){
		
	}
};
$(function(){
	flowNumRuleConfig.load();
});
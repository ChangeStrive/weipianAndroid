var WebInfoConfig = {
	toEdit:function(fdId){
		if(fdId){
			window.location.href=hxltUrl+"WebInfoAction/toEdit?fdId="+fdId+"&fdTypeId="+fdTypeId;
		}else{
			window.location.href=hxltUrl+"WebInfoAction/toEdit?fdTypeId="+fdTypeId;
		}
	},
	load:function(){
		var params={};
		params["fdTypeId"]=fdTypeId;
		WebInfoConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WebInfoAction/getList',
			formId:'.search-form',
			params:params,
	        "aoColumns" : [{
					"sWidth" : "auto",
					"fnRender":function(obj){
						return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
					}
				},{
					"sWidth" : "auto",
					"fnRender":function(obj){
						return '<img src="'+downAction+obj.aData.fdPicUrl+'" width="100px" height="100px"/>';
					}
				},{
					"mDataProp" : "fdTitle",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdDesc",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdTypeName",
					"sWidth" : "auto"
				},{
					"mDataProp" : "fdCreateTime",
					"sWidth" : "auto"
				},{
					"sWidth" : "auto",
					"fnRender":function(obj){
						return '<a href="javascript:WebInfoConfig.toEdit(\''+obj.aData.fdId+'\')" class="button">编辑</a><a onclick="WebInfoConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
					}
				}
			]
	    });
	},
	loadType:function(){
		var params={};
		params["fdId"]="40288192478a685d01478a69e77a0000";
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysDictAction/getListByParentId",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list.length>0){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				var str='<h3 fdValue="'+item.value+'" fdText="'+item.text+'"><a href="'+hxltUrl+'WebInfoAction/list?fdTypeId='+item.value+'">'+item.text+'</a></h3>';
							$("#subnav").append(str);
	        			}
	        			if(!fdTypeId){
	        				if($("#subnav h3").length>0){
	        					fdTypeId=$("#subnav h3").attr("fdValue");
	        				}
	        			}
	        			if(fdTypeId){
	        				var h3=$("#subnav h3[fdValue='"+fdTypeId+"']");
		        			h3.attr("id","side-sub-menu-current");
	        			}
	        		}
	        		WebInfoConfig.load();
	        	}
	        }
		});
	},
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=WebInfoConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"WebInfoAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		WebInfoConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	WebInfoConfig.loadType();
});
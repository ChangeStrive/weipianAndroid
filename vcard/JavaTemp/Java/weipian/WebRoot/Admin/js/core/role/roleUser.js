var roleUserConfig={
	del:function(fdUserIds){
		var params={};
		if(fdUserIds){
			params["fdUserIds"]=fdUserIds;
			params["fdId"]=fdId;
		}else{
			var fdIds=roleUserConfig.table.getSelectedIndex();
			if(fdIds.length==0){
				updateAlert("请选择要删除的记录!","alert-warn");
				return ;
			}else{
				params["fdId"]=fdId;
				params["fdUserIds"]=fdIds.join();
			}
		}
		var flag=confirm("删除后不能恢复，是否删除？");
		if(!flag){
			return ;		
		}
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/deleteUser",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		roleUserConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	addUser:function(fdLoginNames){
		var params={};
		params["fdId"]=fdId;
		params["fdLoginNames"]=fdLoginNames;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/saveUser",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("操作成功!","alert-success");
	        		$("input[name='fdLoginNames']").val("")
	        		roleUserConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("操作失败!","alert-error");
	        	}
	        }
		});
	},
	loadRoleSelect:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/getList",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				var str='<option value="'+item.fdId+'">'+item.fdName+'</option>';
	        				$("#roleSelect").append(str);
	        			}
	        			
	        			$("#roleSelect option[value='"+fdId+"']").attr("selected","selected");
	        			$("#roleSelect").on("change",function(){
	        				window.location.href=hxltUrl+"SysRoleAction/roleUserlist?fdId="+$("#roleSelect option:selected").val();
	        			});
	        		}
	        	}else{
	        		updateAlert("操作失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		params["fdId"]=fdId;
		roleUserConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'SysRoleAction/getRoleUserList',
			params:params,
			bPaginate:false,
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"mDataProp":"fdLoginName",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdName",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdLoginIp",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdCreateTime",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdLastTime",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus== "1" ? "<span class='label label-success'>启用</span>" : "<span class='label label-error'>禁用</span>";
							return fdStatus;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<a onclick="roleUserConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
						}
					}
			]
	    });  
	},
	init:function(){
		roleUserConfig.load();
		roleUserConfig.loadRoleSelect();
		$(".addUserBtn").on("click",function(){
			var fdLoginNames=$("input[name='fdLoginNames']").val();
			if(fdLoginNames){
				roleUserConfig.addUser(fdLoginNames);
			}
		});
	}
};
$(function(){
	roleUserConfig.init();
});
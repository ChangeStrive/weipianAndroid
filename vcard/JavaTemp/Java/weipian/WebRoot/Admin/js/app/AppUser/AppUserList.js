var AppUserConfig={
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=AppUserConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppUserAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		AppUserConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		AppUserConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'AppUserAction/getList',
			params:params,
			formId:'.hightSearchForm',
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<img src="'+obj.aData.fdPicUrl+'" width="100px" height="100px"/>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var str='';
							str+='账号:'+obj.aData.fdCode+'<br/>';
							if(obj.aData.fdName){
								str+='姓名:'+obj.aData.fdName+'<br/>';
								if(obj.aData.fdSex=="1"){
									str+='性别:男<br/>';
								}else if(obj.aData.fdSex=="0"){
									str+='性别:女<br/>';
								}
								str+='生日:'+obj.aData.fdBirthday+'<br/>';
								str+='城市:'+obj.aData.fdProvince+"/"+obj.aData.fdCity+"/"+obj.aData.fdArea+'<br/>';
							}
							return str;
						}
					},{
						"mDataProp" : "fdAmount",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var str='';
							if(obj.aData.fdShopType=="0"){
								str+="待审核";
							}else if(obj.aData.fdShopType=="1"){
								str+="普通分销商";
							}else if(obj.aData.fdShopType=="2"){
								str+="城市合伙人";
							}else if(obj.aData.fdShopType=="3"){
								str+="区域合伙人";
							}
							return str;
						}
					},{
						"mDataProp" : "fdCreateTime",
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
							var fdStatus = obj.aData.fdStatus == "1" ? "禁用" : "启用";
							return '<a href="'+hxltUrl+'AppUserAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a>'+
									'<br/><a href="'+hxltUrl+'AppUserAction/editCard?fdId='+obj.aData.fdId+'" class="button">名片信息</a>'+
									'<br/><a href="'+hxltUrl+'AppUserAction/toUpdatePwd?fdId='+obj.aData.fdId+'" class="button">修改密码</a>'+
									'<br/><a href="javascript:;" onclick="AppUserConfig.enableOrDisabled(\''+obj.aData.fdId+'\',\''+obj.aData.fdStatus+'\')" class="button">'+fdStatus+'</a>'+
									'<br/><a onclick="AppUserConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>';
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
			var fdIds=AppUserConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppUserAction/modifStatus",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		var msg = "";
	        		if(fdId)msg = fdStatus == "1" ? "已禁用" : "已启用";
	        		else msg = fdStatus == "1" ? "已启用" : "已禁用";
	        		updateAlert(msg,"alert-success");
	        		AppUserConfig.table.fnPageChange(0);
	        	}else{
	        		var msg = fdStatus == "1" ? "禁用失败" : "启用失败";
	        		updateAlert(msg,"alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	AppUserConfig.load();
});
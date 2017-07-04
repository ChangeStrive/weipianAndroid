var AppUserShopConfig={
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=AppUserShopConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppUserShopAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		AppUserShopConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		AppUserShopConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'AppUserShopAction/getList',
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
							return '<img src="'+obj.aData.fdPicUrl1+'" width="100px" height="100px"/>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<img src="'+obj.aData.fdPicUrl2+'" width="100px" height="100px"/>';
						}
					},{
						"sWidth" : "150",
						"fnRender":function(obj){
							var str='';
							if(obj.aData.fdUserCode){
								str+='手机号:'+obj.aData.fdUserCode+'<br/>';
							}
							if(obj.aData.fdUserName){
								str+='姓名:'+obj.aData.fdUserName+'<br/>';
							}
							return str;
						}
					},{
						"sWidth" : "150",
						"fnRender":function(obj){
							var str='';
							if(obj.aData.fdStoreName){
								str+='店铺名称:'+obj.aData.fdStoreName+'<br/>';
							}
							if(obj.aData.fdStoreAddress){
								str+='店铺地址:'+obj.aData.fdStoreAddress+'<br/>';
							}
							return str;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var str='';
							str+="<span class='label label-success'>"+obj.aData.fdStoreType+"</span>";
							return str;
						}
					},{
						"mDataProp" : "fdStoreBrand",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdCreateTime",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus== "1" ? "<span class='label label-success'>启用</span>" : "<span class='label label-error'>禁用</span>";
							if(obj.aData.fdStatus=="1"){
								fdStatus="<span class='label label-success'>通过</span>";
							}else if(obj.aData.fdStatus=="0"){
								fdStatus="<span class='label label-error'>待审核</span>";
							}else{
								fdStatus="<span class='label label-error'>不通过</span>";
							}
							return fdStatus;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus =""; 
							var fdStatus1="";
							var fdStatus2="";
							var fdStatusStr1="";
							var fdStatusStr2="";
							if(obj.aData.fdStatus=="1"){
								fdStatus1="0";
								fdStatus2="-1";
								fdStatusStr1="待审核";
								fdStatusStr2="不通过";
							}else if(obj.aData.fdStatus=="0"){
								fdStatus1="1";
								fdStatus2="-1";
								fdStatusStr1="通过";
								fdStatusStr2="不通过";
							}else{
								fdStatus1="1";
								fdStatus2="0";
								fdStatusStr1="通过";
								fdStatusStr2="待审核";
							}
								
							return '<a href="'+hxltUrl+'AppUserShopAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a>'+
									'<br/><a onclick="AppUserShopConfig.del(\''+obj.aData.fdId+'\')" href="javascript:;" class="button">删除</a>'+
									'<br/><a href="javascript:;" onclick="AppUserShopConfig.enableOrDisabled(\''+obj.aData.fdId+'\',\''+fdStatus1+'\')" class="button">'+fdStatusStr1+'</a>'+
									'<br/><a href="javascript:;" onclick="AppUserShopConfig.enableOrDisabled(\''+obj.aData.fdId+'\',\''+fdStatus2+'\')" class="button">'+fdStatusStr2+'</a>';
						}
					}
			]
	    });  
	},
	enableOrDisabled:function(fdId,fdStatus){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
			params["fdStatus"] = fdStatus;
		}else{
			var fdIds=AppUserShopConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppUserShopAction/modifStatus",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		var msg = "";
	        		if(fdId)msg = fdStatus == "1" ? "已禁用" : "已启用";
	        		else msg = fdStatus == "1" ? "已启用" : "已禁用";
	        		updateAlert(msg,"alert-success");
	        		AppUserShopConfig.table.fnPageChange(0);
	        	}else{
	        		var msg = fdStatus == "1" ? "禁用失败" : "启用失败";
	        		updateAlert(msg,"alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	AppUserShopConfig.load();
});
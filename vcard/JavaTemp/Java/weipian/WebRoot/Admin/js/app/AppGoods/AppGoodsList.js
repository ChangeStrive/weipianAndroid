var AppGoodsConfig={
	del:function(fdId){
		var params={};
		if(fdId){
			params["fdId"]=fdId;
		}else{
			var fdIds=AppGoodsConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppGoodsAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		AppGoodsConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		var params={};
		params["fdPid"]=fdPid;
		AppGoodsConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'AppGoodsAction/getList',
			formId:'.hightSearchForm',
			params:params,
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<img src="'+downAction+obj.aData.fdPicUrl+'" width="100px"/>';
						}
					},{
						"mDataProp":"fdGoodsName",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdGoodsNo",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdTagPrice",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdPrice",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdQuantity",
						"sWidth" : "auto"
					},{
						"mDataProp":"fdTypeName",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdCreateTime",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdUpdateTime",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus== "1" ? "<span class='label label-success'>已上架</span>" : "<span class='label label-error'>未上架</span>";
							return fdStatus;
						}
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdStatus = obj.aData.fdStatus == "1" ? "下架" : "上架";
							return '<a href="'+hxltUrl+'AppGoodsAction/toEdit?fdId='+obj.aData.fdId+'" class="button">编辑</a><br/>'+ 
									'<a href="javascript:;" onclick="AppGoodsConfig.enableOrDisabled(\''+obj.aData.fdId+'\',\''+obj.aData.fdStatus+'\')" class="button">'+fdStatus+'</a><br/>'+
									'<a href="'+hxltUrl+'AppGoodsAction/toImage?fdId='+obj.aData.fdId+'" target="_blank"  class="button">商品相册</a>' ;
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
			var fdIds=AppGoodsConfig.table.getSelectedIndex();
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
	        url:hxltUrl+"AppGoodsAction/modifStatus",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		var msg = "";
	        		if(fdId)msg = fdStatus == "1" ? "已禁用" : "已启用";
	        		else msg = fdStatus == "1" ? "已启用" : "已禁用";
	        		updateAlert(msg,"alert-success");
	        		AppGoodsConfig.table.fnPageChange(0);
	        	}else{
	        		var msg = fdStatus == "1" ? "禁用失败" : "启用失败";
	        		updateAlert(msg,"alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	AppGoodsConfig.load();
});
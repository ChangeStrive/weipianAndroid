var AppGoodsOrderConfig={
	start:0,
	limit:10,
	load:function(){
		var params={};
		var json=commUtils.getFormVals(".hightSearchForm");
		$.extend(true, params,json);
		params["start"]=AppGoodsOrderConfig.start;
		params["limit"]=AppGoodsOrderConfig.limit;
		$("#top-alert").attr("class","fixed alert alert-info");
		$("#top-alert .alert-content").html("正在加载中.....");
		$("#top-alert").show();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/toApplyReturnOrderList",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	$("#top-alert").hide();
	        	if(data.success){
	        		$("#J_BoughtTable tr:gt(0)").remove();
	        		var list=data.list;
	        		if(list){
	        			for(var i=0;i<list.length;i++){
	        				var item=list[i];
	        				AppGoodsOrderConfig.appendHtml(item);
	        			}
	        		}
	        		AppGoodsOrderConfig.pageInit(data.totalSize,AppGoodsOrderConfig.start,AppGoodsOrderConfig.limit);
	        	}
	        }
		});
	},
	agreeRefund:function(fdOrderId){//同意退款
		var flag=confirm("确定同意退款？");
		if(!flag){
			return ;		
		}
		
		maskUtil.show("正在退款中");
		var params={};
		params["fdOrderId"]=fdOrderId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/agreeRefund",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	maskUtil.hide("");
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	noAgreeRefund:function(fdOrderId){//确定收货
		var fdOrderId=$(".fdOrderId").val();
		var fdBackReason=$(".fdBackReason").val();
		var flag=confirm("确定不同意退款？");
		if(!flag){
			return ;		
		}
		
		if(!fdOrderId){
			updateAlert("请选择订单!","alert-warn");
			return ;
		}
		if(!fdBackReason){
			updateAlert("请填写快递名称!","alert-warn");
			return ;
		}	
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		params["fdBackReason"]=fdBackReason;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/noAgreeRefund",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		$('#backReturnWin').window('close');
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	noAgreeWin:function(fdOrderId,fdOrderNo){//确定收货
		$('#backReturnWin').find("form").each(function(){
			this.reset();
		});
		$(".orderNo").html(fdOrderNo);
		$(".fdOrderId").val(fdOrderId);
		$('#backReturnWin').window({    
		    width:300,    
		    height:200,
		    title:'驳回理由',
		    maximizable:false,
		    minimizable:false,
		    collapsible:false,
		    resizable:false,
		    modal:true   
		}); 
		$('#backReturnWin').window('center');
	},
	pageInit:function(count,start,limit){
		pageUtils.init(".pagination",count,start,limit,function(start){
			AppGoodsOrderConfig.start=start;
			AppGoodsOrderConfig.load();
		});
	},
	getOption:function(fdStatus,fdOrderId,fdOrderNo){//获得操作选项
		var options="";
		if(fdStatus=="4"){//
			options+='<a href="javascript:AppGoodsOrderConfig.agreeRefund(\''+fdOrderId+'\')">同意退款</a>';
			options+='<a href="javascript:AppGoodsOrderConfig.noAgreeWin(\''+fdOrderId+'\',\''+fdOrderNo+'\')">不同意退款</a>';
		}
		return options;
	},
	appendHtml:function(item){
		var data={
			 fdId:item.fdId,
			 fdOrderNo:item.fdOrderNo,
			 fdShopNo:item.fdShopNo,
			 fdShopName:item.fdShopName,
			 fdCreateTime:item.fdCreateTime
		};
		var str=TemplateUtils.getTemplate("#orderTitleTemplate",data);//订单
		var orderDetail=item.fdOrderDetail;
		
		for(var i=0;i<orderDetail.totalSize;i++){//订单明细
			var o=orderDetail.list[i];
			var orderOptions="";
			if(i==0){
				orderOptions=TemplateUtils.getTemplate("#orderItemDetail1",{
					fdTotalAmount:item.fdAmount,
					orderId:item.fdId,
					fdExpressAmount:item.fdExpressAmount,
					fdCoupon:item.fdCoupon,
					fdUserCode:item.fdUserCode,
					fdUserName:item.fdUserName,
					fdConsignee:item.fdConsignee,
					totalSize:orderDetail.totalSize,
					fdStatus:item.fdStatusStr,
					fdOption:AppGoodsOrderConfig.getOption(item.fdStatus,item.fdId,item.fdOrderNo)
				});
			}
			var itemData={
				fdId:o.fdId,
				fdPicUrl:o.fdPicUrl,
				fdGoodsNo:o.fdGoodsNo,
				fdGoodsName:o.fdGoodsName,
				fdColor:o.fdColor,
				fdSize:o.fdSize,
				fdAmount:o.fdAmount,
				fdPrice:o.fdPrice,
				fdQuantity:o.fdQuantity,
				orderOptions:orderOptions
			};
			var tr=TemplateUtils.getTemplate("#orderItemDetail",itemData);
			str+=tr;
		}
		
		var extendTemplate="";
		if(item.fdApplyRefundTime){
			extendTemplate+=TemplateUtils.getTemplate("#ApplyReturnTemplate",{
				fdApplyRefundTime:item.fdApplyRefundTime,
				fdApplyRefundReason:item.fdApplyRefundReason
			});
		}
		if(item.fdSendType=="1"){
			if(item.fdExpressName){
				extendTemplate+=TemplateUtils.getTemplate("#expressTemplate",{
					fdExpressName:item.fdExpressName,
					fdExpressNo:item.fdExpressNo
				});
			}
			extendTemplate+=TemplateUtils.getTemplate("#recevieGoodsTemplate",{
				fdAddress:item.fdAddress,
				fdZipCode:item.fdZipCode,
				fdConsignee:item.fdConsignee,
				fdTel:item.fdTel
			});
		}
		if(extendTemplate){
			str+=TemplateUtils.getTemplate("#extendTemplate",{
				extendTemplate:extendTemplate
			});
		}
		$("#J_BoughtTable").append(str);
	},
	init:function(){
		AppGoodsOrderConfig.load();
		$(".hightSearchForm").find(".searchbtn").on("click",function(){
			AppGoodsOrderConfig.start=0;
			AppGoodsOrderConfig.load();
		});
		$(".hightSearchForm").find(".clearbtn").on("click",function(){
			$(".hightSearchForm").find("form").each(function(){
			 	this.reset();
			 	AppGoodsOrderConfig.start=0;
				AppGoodsOrderConfig.load();
			 });
		});
		
		$(".backReturnWinSave").on("click",function(){
			AppGoodsOrderConfig.noAgreeRefund();
		});
		
		$(".backReturnWinCancel").on("click",function(){
			$('#backReturnWin').window('close');
		});
	}
};

$(function(){
	AppGoodsOrderConfig.init();
});
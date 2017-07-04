var AppGoodsOrderConfig={
	start:0,
	limit:10,
	fdStatus:"",
	fdSendType:"",
	load:function(){
		var params={};
		var json=commUtils.getFormVals(".hightSearchForm");
		$.extend(true, params,json);
		params["start"]=AppGoodsOrderConfig.start;
		params["limit"]=AppGoodsOrderConfig.limit;
		params["fdStatus"]=AppGoodsOrderConfig.fdStatus;
		params["fdSendType"]=AppGoodsOrderConfig.fdSendType;
		$("#top-alert").attr("class","fixed alert alert-info");
		$("#top-alert .alert-content").html("正在加载中.....");
		$("#top-alert").show();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/getList",
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
	cancelOrderNoPay:function(fdOrderId){//取消订单
		if(!fdOrderId) return ;
		var flag=confirm("确定取消订单？");
		if(!flag){
			return ;		
		}
		var params={};
		params["fdOrderId"]=fdOrderId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/cancelOrderNoPay",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		AppGoodsOrderConfig.load();
	        	}
	        }
		});
	},
	deliver:function(){//发货
		var fdOrderId=$(".fdOrderId").val();
		var fdExpressName=$(".fdExpressName").val();
		var fdExpressNo=$(".fdExpressNo").val();
		
		if(!fdOrderId){
			updateAlert("请选择订单!","alert-warn");
			return ;
		}
		if(!fdExpressName){
			updateAlert("请填写快递名称!","alert-warn");
			return ;
		}	
		
		if(!fdExpressNo){
			updateAlert("请填写快递编号!","alert-warn");
			return ;
		}	
		
		var flag=confirm("确定发货？");
		if(!flag){
			return ;		
		}
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		params["fdExpressNo"]=fdExpressNo;
		params["fdExpressName"]=fdExpressName;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/deliver",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		$('#expressWin').window('close');
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	expressGoods:function(fdOrderId,fdOrderNo){//发货
		$('#expressWin').find("form").each(function(){
			this.reset();
		});
		$(".orderNo").html(fdOrderNo);
		$(".fdOrderId").val(fdOrderId);
		$('#expressWin').window({    
		    width:300,    
		    height:200,
		    title:'发货',
		    maximizable:false,
		    minimizable:false,
		    collapsible:false,
		    resizable:false,
		    modal:true   
		}); 
		$('#expressWin').window('center');
	},
	sendByShop:function(fdOrderId){//商城配送
		var flag=confirm("确定送货上门？");
		if(!flag){
			return ;		
		}
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/sendByShop",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	agreeRefund:function(fdOrderId){//同意退款
		var flag=confirm("确定同意退款？");
		if(!flag){
			return ;		
		}
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/agreeRefund",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	noAgreeRefund:function(){//确定收货
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
	cancelOrderOfPay:function(fdOrderId,fdOrderNo){//取消订单
		$('#cancelOrderWin').find("form").each(function(){
			this.reset();
		});
		$(".orderNo").html(fdOrderNo);
		$(".fdOrderId").val(fdOrderId);
		$('#cancelOrderWin').window({    
			width:300,    
			height:200,
			title:'取消订单理由',
			maximizable:false,
			minimizable:false,
			collapsible:false,
			resizable:false,
			modal:true   
		}); 
		$('#cancelOrderWin').window('center');
	},
	sureReceiveGoods:function(fdOrderId){//确定收货
		var flag=confirm("确定收货？");
		if(!flag){
			return ;		
		}
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/sureReceiveGoods",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	cancelOrderOfPayFn:function(){//取消已付款订单
		var fdOrderId=$(".fdOrderId").val();
		var fdCancelReason=$(".fdCancelReason").val();
		var flag=confirm("确定取消订单？");
		if(!flag){
			return ;		
		}
		
		if(!fdOrderId){
			updateAlert("请选择订单!","alert-warn");
			return ;
		}
		if(!fdCancelReason){
			updateAlert("取消原因!","alert-warn");
			return ;
		}	
		
		var params={};
		params["fdOrderId"]=fdOrderId;
		params["fdCancelReason"]=fdCancelReason;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/cancelOrderOfPay",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		updateAlert("操作成功!","alert-success");
	        		$('#cancelOrderWin').window('close');
	        		AppGoodsOrderConfig.load();
	        	}else{
	        		updateAlert(data.msg,"alert-error");
	        	}
	        }
		});
	},
	pageInit:function(count,start,limit){
		pageUtils.init(".pagination",count,start,limit,function(start){
			AppGoodsOrderConfig.start=start;
			AppGoodsOrderConfig.load();
		});
	},
	getOption:function(fdStatus,fdOrderId,fdOrderNo,fdSendType){//获得操作选项
		var options="";
		if(fdStatus=="0"){//未付款
			//options+='<a href="javascript:AppGoodsOrderConfig.cancelOrderNoPay(\''+item.fdId+'\')">取消订单</a>';
		}else if(fdStatus=="1"||fdStatus=="6"){//商城送货
			//options+='<a href="javascript:AppGoodsOrderConfig.sendByShop(\''+fdOrderId+'\')">送货上门</a>';
			options+='<a href="javascript:AppGoodsOrderConfig.expressGoods(\''+fdOrderId+'\',\''+fdOrderNo+'\')">快递发货</a>';
			options+='<a href="javascript:AppGoodsOrderConfig.cancelOrderOfPay(\''+fdOrderId+'\',\''+fdOrderNo+'\')">取消订单</a>';
		}else if(fdStatus=="4"){//退款申请
			options+='<a href="javascript:AppGoodsOrderConfig.agreeRefund(\''+fdOrderId+'\')">同意退款</a>';
			options+='<a href="javascript:AppGoodsOrderConfig.noAgreeWin(\''+fdOrderId+'\',\''+fdOrderNo+'\')">不同意退款</a>';
		}else if(fdStatus=="2"){//已付款
			options+='<a href="javascript:AppGoodsOrderConfig.sureReceiveGoods(\''+fdOrderId+'\')">确定收货</a>';
		}
		//options+='<a href="#" >查看订单</a>';
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
		var str=TemplateUtils.getTemplate("#orderTitleTemplate",data);
		var orderDetail=item.fdOrderDetail;
		
		for(var i=0;i<orderDetail.totalSize;i++){
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
					fdOption:AppGoodsOrderConfig.getOption(item.fdStatus,item.fdId,item.fdOrderNo,item.fdSendType)
				});
			}
			var itemData={
				fdId:o.fdId,
				fdPicUrl:o.fdPicUrl,
				fdGoodsNo:o.fdGoodsNo,
				fdGoodsName:o.fdGoodsName,
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
		
		$(".tab-nav li").on("click",function(){
			$(".tab-nav .current").removeClass("current");
			$(this).addClass("current");
			AppGoodsOrderConfig.fdStatus=$(this).attr("fdStatus");
			AppGoodsOrderConfig.fdSendType=$(this).attr("fdSendType");
			AppGoodsOrderConfig.load();
		});
		
		$(".expressWinSave").on("click",function(){
			AppGoodsOrderConfig.deliver();
		});
		
		$(".expressWinCancel").on("click",function(){
			$('#expressWin').window('close');
		});
		
		$(".backReturnWinSave").on("click",function(){
			AppGoodsOrderConfig.noAgreeRefund();
		});
		
		$(".backReturnWinCancel").on("click",function(){
			$('#backReturnWin').window('close');
		});
		
		$(".cancelOrderWinCancel").on("click",function(){
			$('#cancelOrderWin').window('close');
		});
		
		$(".cancelOrderWinSave").on("click",function(){
			AppGoodsOrderConfig.cancelOrderOfPayFn();
		});
	}
};

$(function(){
	AppGoodsOrderConfig.init();
});
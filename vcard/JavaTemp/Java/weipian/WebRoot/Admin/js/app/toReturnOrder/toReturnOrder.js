var AppGoodsOrderConfig={
	start:0,
	limit:10,
	load:function(){
		var params={};
		var json=commUtils.getFormVals(".hightSearchForm");
		$.extend(true, params,json);
		console.dir(params);
		params["start"]=AppGoodsOrderConfig.start;
		params["limit"]=AppGoodsOrderConfig.limit;
		$("#top-alert").attr("class","fixed alert alert-info");
		$("#top-alert .alert-content").html("正在加载中.....");
		$("#top-alert").show();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"AppGoodsOrderAction/returnOrderList",
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
	pageInit:function(count,start,limit){
		pageUtils.init(".pagination",count,start,limit,function(start){
			AppGoodsOrderConfig.start=start;
			AppGoodsOrderConfig.load();
		});
	},
	getOption:function(fdStatus,fdOrderId,fdOrderNo){//获得操作选项
		var options="";
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
					fdCoupon:item.fdCoupon,
					fdExpressAmount:item.fdExpressAmount,
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
$(function(){
	//初始化购买记录
	indexConfig.init();
	$(".momlig-pay").live("click",function(){
		var fdOrderId=$(this).attr("fdOrderId");
		$(".tipBoxBtn").attr("fdOrderId",fdOrderId);
		var h=($(window).height()-$(".tipBox").height())/2;
		$(".tipBox").css("top",h);
		$(".mask").show();
		$(".tipBox").show();
		$(".fdReturnMark").html("");
	});
	
	$(".tipBoxClose").on("click",function(){
		$(".mask").hide();
		$(".tipBox").hide();
	});
	$(".tipBoxBtn").on("click",function(){
		indexConfig.applyRefund();
	});
});

//购买记录加载
var indexConfig={
	init:function(){
		//购买下拉初始化
		var drop=$('.droploadDiv').dropload({
	        scrollArea : window,
	        loadDownFn : function(me){
	        	 indexConfig.load(me);
	        }
		 });
	},
	limit:10,//每页加载数量
	start:0,//从第几条开始加载 
	hasLoad:false,//判断数据是否正在加载，用来防止重复加载
	load:function(me){//网络加载
		if(indexConfig.hasLoad){//数据已经在加载中，不用重复加载
			return ;
		}
		indexConfig.hasLoad=true;
		var params={};
		params["start"]=indexConfig.start;
		params["limit"]=indexConfig.limit;
		params["fdShopNo"]=fdShopNo;
		params["orderType"]="takeGoods";
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"wxajax/getMyOrderList",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	//重置加载状态
	        	indexConfig.hasLoad=false;
	        	if(me){
	        		me.resetload();
	        	}
	        	
	        	if(data.status==0){//数据加载成功 状态位自己确定
	        		var list=data.list;
	        		if(list){
	        			indexConfig.start=indexConfig.start+list.length;
	        			if(list.length>0){
		        			for(var i=0;i<list.length;i++){
		        				var item=list[i];
		        				indexConfig.appendHtml(item);//数据渲染
		        			}
	        			}
	        			if(list.length<indexConfig.limit){//隐藏下拉加载框
	        				if(me){
	        					me.lock();
	        					$(".droploadDiv .dropload-refresh").html("已经到底了");
	        	        	}
	        			}
	        		}
	        	}
	        	if(indexConfig.start==0){
	        		$(".mom_kongkong").show();
	        		$(".droploadDiv").hide();
	        	}
	        }
		});
	},
	applyRefund:function(){
		var refundReason=$(".fdReturnMark").val();
		var fdOrderId=$(".tipBoxBtn").attr("fdOrderId");
		if(!refundReason){
			alert("理由不能为空");
			return ;
		}
		var flag=confirm("确定申请退款？");
		if(!flag){
			return ;		
		}
		var params={};
		params["fdOrderId"]=fdOrderId;
		params["fdShopNo"]=fdShopNo;
		params["fdApplyReason"]=refundReason;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"wxajax/applyRefund",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
	        		window.location.href=window.location.href;
	        	}else{
	        		alert(data.msg);
	        	}
	        }
		});

	},
	getDetails:function(item,fdId){
		var orderDetails="";
		for(var i=0;i<item.length;i++){
			var o=item[i];
			var data={
					"fdId":fdId,//期次
					"fdGoodsName":o.fdGoodsName,//期次
					"fdPicUrl":o.fdPicUrl,//期次
					"fdQuantity":o.fdQuantity,//期次
					"fdAmount":o.fdAmount,//期次
					"fdTagPrice":o.fdTagPrice,//期次
					"fdPrice":o.fdPrice,//期次
					"fdGoodsNo":o.fdGoodsNo
			};
			var str=TemplateUtils.getTemplate("#orderItemTemplate",data);
			orderDetails+=str;
		}
		return orderDetails;
	},
	appendHtml:function(item){//数据渲染
		var orderDetails=indexConfig.getDetails(item.fdOrderDetail,item.fdId);
		var options="";
		var orderStatus="";
		if(item.fdStatus=="1"){
			orderStatus="等待发货";
			options=TemplateUtils.getTemplate("#takeGoods",{fdId:item.fdId});
		}else if(item.fdStatus=="6"){
			orderStatus="退款申请被驳回";
			options=TemplateUtils.getTemplate("#takeGoods",{fdId:item.fdId});
		}else if(item.fdStatus=="4"){
			orderStatus="退款申请中";
			options="";
		}
		var data={
				"orderStatus":orderStatus,
				"fdOrderNo":item.fdOrderNo,//期次
				"fdId":item.fdId,//期次
				"fdAmount":item.fdAmount,// 试机号
				"options":options,
				"orderDetails":orderDetails//形态
		};
		var str=TemplateUtils.getTemplate("#orderTemplate",data);
		$('.droploadDiv').find(".momList").append(str);
		
	}
}
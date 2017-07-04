$(function(){
	//初始化购买记录
	indexConfig.init();
	$(".gio-cart").live("click",function(){
		var fdGoodsId=$(this).attr("fdGoodsId");
		addShopCart(fdGoodsId,1);
	});
	$(".gio-sc").live("click",function(){
		var fdGoodsId=$(this).attr("fdGoodsId");
		collection(fdGoodsId);
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
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"wxajax/goodslist",
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
	        		$(".droploadDiv").hide();
	        		$(".noDateTip").show();
	        	}
	        }
		});
	},
	appendHtml:function(item){//数据渲染
		var data={
				"fdGoodsId":item.fdId,
				"fdGoodsName":item.fdGoodsName,//期次
				"fdGoodsNo":item.fdGoodsNo,//期次
				"fdPrice":item.fdPrice,// 试机号
				"fdTagPrice":item.fdTagPrice,
				"fdPicUrl":item.fdPicUrl//形态
		};
		var str=TemplateUtils.getTemplate("#goodsItem",data);
		$('.droploadDiv').find(".goodsList").append(str);
		
	}
}
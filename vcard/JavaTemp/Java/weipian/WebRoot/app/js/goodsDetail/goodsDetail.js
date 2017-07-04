$(function(){
	$(".gdh_title").on("click",function(){
		var type=$(this).attr("fdType");
		$(".gdht_selected").removeClass("gdht_selected");
		$(this).find("span").addClass("gdht_selected");
		if(type=="goods"){
			$(".goodsDetailReplyMain").hide();
			$(".goodsRemark").show();
		}else{
			$(".goodsRemark").hide();
			$(".goodsDetailReplyMain").show();
			if(!indexConfig.drop){
				indexConfig.init();
			}else{
				indexConfig.drop.resetload();
			}
			
		}
	});
	
	$(".gd-sc").live("click",function(){
		collection(fdGoodsId);
	});
	
	//初始化购买记录
	$(".gd-shopcart,.gd-buy").live("click",function(){
		var buyType=$(this).attr("buyType");
		if(buyType=="buyNow"){
			$(".goodsGouwuche").attr("buyType","buyNow");
			$(".goodsGouwuche .gg-text").html("立即购买");
		}else{
			$(".goodsGouwuche").attr("buyType","shopCart");
			$(".goodsGouwuche .gg-text").html("加入购物车");
		}
		$(".mask").show();
		$(".goodsSelectQuantity").show();
	});
	$(".close_btn").live("click",function(){
		$(".mask").hide();
		$(".goodsSelectQuantity").hide();
		$(".fdBuyQuantity").val("1");
	});
	
	 $(".gq_jian").on("click",function(){
		 if(parseInt($("input[name='fdQuantity1']").val())==0){
			 alert("请至少选择一件数量");
		 }else{
			 $("input[name='fdQuantity1']").val(parseInt($("input[name='fdQuantity1']").val())-1);
		 }
	 });
	 $(".fdBuyQuantity").on("change",function(){
		var value=parseInt($(this).val());
		var max=parseInt($(this).attr("maxquantity"));
		if(value<0){
			$(this).val("0");
		}else if(value>max){
			$(this).val(max);
		}
	 });
	 $(".gq_jia").on("click",function(){
		var value=parseInt($(".fdBuyQuantity").val());
		var max=parseInt($(".fdBuyQuantity").attr("maxquantity"));
		if(value==max){
			return ;
		}
		$("input[name='fdQuantity1']").val(parseInt($("input[name='fdQuantity1']").val())+1);
	 });
	 
	 $(".goodsGouwuche").on("click",function(){
		 if(hasWxLogin==0){
			 window.location.href=hxltUrl+"wx/login?fdShopNo="+fdShopNo+"&backUrl="+encodeURIComponent(window.location.href);
		 }else{
			 var quantity=parseInt($(".fdBuyQuantity").val());
			 if(quantity<=0){
				 alert("购买数量不能为0!");
				 return ;
			 }
			 var buyType=$(this).attr("buyType");
			 if(buyType!="buyNow"){
				 addShopCart(fdGoodsId,quantity);
			 }else{
				 window.location.href=hxltUrl+"wx/buynow?fdShopNo="+fdShopNo+"&fdGoodsId="+fdGoodsId+"&fdQuantity="+quantity;
			 }
		 }
		 $(".mask").hide();
		 $(".goodsSelectQuantity").hide();
		 $(".fdBuyQuantity").val("1");
	 });
});

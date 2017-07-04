var addShopCart=function(fdGoodsId,fdQuantity){//加入购物车
	
	var params={};
	params["fdGoodsId"]=fdGoodsId;
	params["fdQuantity"]=fdQuantity;
	$.ajax({
		type: "POST",
		url:hxltUrl+"wxajax/addShopCart",
		contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		data:params,
		dataType: "json",
		success: function(result){
			if(result.status==0){
				$(".tipBox").show();
				$(".mask2").show();
			}else if(result.status==-2){
				window.location.href=hxltUrl+"wx/login?backUrl="+encodeURIComponent(window.location.href);
			}else{
				alert(result.msg);
			}
		}
	});
}

var collection=function(fdGoodsId){//收藏
	var params={};
	params["fdGoodsId"]=fdGoodsId;
	$.ajax({
		type: "POST",
		url:hxltUrl+"wxajax/collection",
		contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		data:params,
		dataType: "json",
		success: function(result){
			if(result.status==0){
				alert("收藏成功！");
			}else if(result.status==-2){
				window.location.href=hxltUrl+"wx/login?backUrl="+encodeURIComponent(window.location.href);
			}else{
				alert(result.msg);
			}
		}
	});
}

$(function(){
	$(".scbtnSure").on("click",function(){
		window.location.href=hxltUrl+"wx/shopcart?fdShopNo="+fdShopNo;
	});
	$(".scbtnCancel").on("click",function(){
		$(".tipBox").hide();
		$(".mask2").hide();
	});
});

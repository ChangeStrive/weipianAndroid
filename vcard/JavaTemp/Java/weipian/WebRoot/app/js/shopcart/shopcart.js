function updateShopCart(fdShopCartId,fdQuantity,fn){
	var params={};
	params["fdShopCartId"]=fdShopCartId;
	params["fdQuantity"]=fdQuantity;
	$.ajax({
	  type: "POST",
	  url: hxltUrl+"wxajax/updateShopCart",
	  data:params,
	  contentType:'application/x-www-form-urlencoded;charset=UTF-8',
      dataType: "json",
	  success: function(result){
	 	if(fn){
	 		fn(result);
	 		changeSelect();
	 	}
	  }
	});
}

function delShopCart(fdShopCartId){
	var params={};
	params["fdShopCartId"]=fdShopCartId;
	$.ajax({
	  type: "POST",
	  url: hxltUrl+"wxajax/delShopCart",
	  data:params,
	  contentType:'application/x-www-form-urlencoded;charset=UTF-8',
      dataType: "json",
	  success: function(result){
	 	if(result.status==0){
	 		window.location.href=hxltUrl+"wx/shopcart?fdShopNo="+fdShopNo;
	 	}
	  }
	});
}

function changeSelect(){
	var fdAmount=0;
	var count=0;
	$(".scbymItem_left").each(function(){
		if($(this).hasClass("sch_checked")){
			var fdShopCartId=$(this).attr("fdShopCartId");
			var fdPrice=parseFloat($(this).attr("fdPrice")).toFixed(2);
			var fdQuantity=parseFloat($(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val());
			fdAmount+=fdPrice*fdQuantity;
			count+=fdQuantity;
		}
	});
	fdAmount=fdAmount.toFixed(2);
	$(".count_money").html("￥"+fdAmount+"元");
	var len=$(".sc_List .sch_checked").length;
    if(len==$(".scbymItem_left").length){
		if(!$(".scbym_count_left").hasClass("sch_checked")){
			$(".scbym_count_left").addClass("sch_checked");
		}
	}else{
		$(".scbym_count_left").removeClass("sch_checked");
	}
    $(".scbym_count_btn span").html(count);
}
$(function(){
	
	$(".sch_Alldel").on("click",function(){
		var fdShopCartIds=[];
		$(".sc_List .sch_checked").each(function(){
			var fdShopCartId=$(this).attr("fdShopCartId");
			fdShopCartIds.push(fdShopCartId);
		});
		
		if(fdShopCartIds.length==0){
			alert("请选择要删除的商品");
			return ;
		}
		if(confirm("确定要删除?")){
			delShopCart(fdShopCartIds.join(","));
		}
	});

	
	$(".scbym_count_btn").on("click",function(){
		var fdShopCartIds=[];
		$(".sc_List .sch_checked").each(function(){
			var fdShopCartId=$(this).attr("fdShopCartId");
			fdShopCartIds.push(fdShopCartId);
		});
		
		if(fdShopCartIds.length==0){
			alert("请选择要结算的商品");
			return ;
		}
		window.location.href=hxltUrl+"wx/jiesuan?fdShopNo="+fdShopNo+"&fdShopCartId="+fdShopCartIds.join();
	});
	$(".sci-onedel").on("click",function(){
		var fdShopCartId=$(this).attr("fdShopCartId");
		if(confirm("确定要删除?")){
			delShopCart(fdShopCartId);
		}
	});

	$(".scbymItem_left").on("click",function(){
		if($(this).hasClass("sch_checked")){
			$(this).removeClass("sch_checked");
		}else{
			$(this).addClass("sch_checked");
		}
		changeSelect();
	});
	$(".scbym_count_left").on("click",function(){
		if($(this).hasClass("sch_checked")){
			$(".scbym_count_left").removeClass("sch_checked");
			$(".scbymItem_left").each(function(){
				$(this).removeClass("sch_checked");
			});
		}else{
			$(".scbym_count_left").addClass("sch_checked");
			$(".scbymItem_left").each(function(){
				if(!$(this).hasClass("sch_checked")){
					$(this).addClass("sch_checked");
				}
			});
		}
		changeSelect();
	});
	$(".fdQuantity").on("change",function(){
		var fdShopCartId=$(this).attr("fdShopCartId");
		var fdQuantity=parseInt($(this).val());
		updateShopCart(fdShopCartId,fdQuantity,function(result){
			if(result.status==0){
				$(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val(fdQuantity);
			}else{
				alert(result.msg);
				$(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val(result.max);
			}
		});
	});
	
	$(".scbym_jia").on("click",function(){
		var fdShopCartId=$(this).attr("fdShopCartId");
		var fdQuantity=parseInt($(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val());
		updateShopCart(fdShopCartId,fdQuantity+1,function(result){
			if(result.status==0){
				$(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val(fdQuantity+1);
			}else{
				alert(result.msg);
			}
		});
	});
	$(".scbym_jian").on("click",function(){
		var fdShopCartId=$(this).attr("fdShopCartId");
		var fdQuantity=parseInt($(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val());
		if(fdQuantity<=1){
			delShopCart(fdShopCartId);
			return ;
		}
		updateShopCart(fdShopCartId,fdQuantity-1,function(result){
			if(result.status==0){
				$(".fdQuantity[fdShopCartId='"+fdShopCartId+"']").val(fdQuantity-1);
			}else{
				alert(result.msg);
			}
		});
	});
});

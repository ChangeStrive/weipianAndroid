
$(function(){
	$(".findPeopleBtn").on("click",function(){
		$(".mask").show();
		$(".payTipImage").show();
	});
	$(".mask,.payTipImage").on("click",function(){
		$(".mask").hide();
		$(".payTipImage").hide();
	});
	$(".nowPayBtn").on("click",function(){
		var params={};
		params["fdOrderId"]=fdOrderId;
		params["fdShopNo"]=fdShopNo;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"wxajax/startCharge",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
		        	pingpp.createPayment(data.chargeDetail, function(result, error){
					    if (result == "success") {
					        // 只有微信公众账号 wx_pub 支付成功的结果会在这里返回，其他的 wap 支付结果都是在 extra 中对应的 URL 跳转。
					    	window.location.href=hxltUrl+"wx/payResult?fdChargeId="+data.chargeDetail.id+"&fdOrderId="+fdOrderId+"&fdShopNo="+fdShopNo;
					    } else if (result == "fail") {
					        // charge 不正确或者微信公众账号支付失败时会在此处返回
					    	alert(JSON.stringify(error));
					    } else if (result == "cancel") {
					        // 微信公众账号支付取消支付
					    }
					});
	        	}else{
	        		if(data.status==-300){
	        			window.location.href="";
	        		}else{
	        			alert(data.msg);
	        		}
	        	}
	        }
		});
	});
});

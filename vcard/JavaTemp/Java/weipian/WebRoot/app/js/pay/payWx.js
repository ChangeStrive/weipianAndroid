function onBridgeReady(data){
   WeixinJSBridge.invoke(
       'getBrandWCPayRequest', data.chargeDetail,
       function(res){     
    	   if(res.err_msg == "get_brand_wcpay_request:ok" ) {
        	   window.location.href=hxltUrl+"wx/payResultWx?&fdOrderId="+fdOrderId+"&fdShopNo="+fdShopNo;
           }else{
        	   //alert(JSON.stringify(res));
           }     
    	   // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
       }
   ); 
}

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
	        url:hxltUrl+"wxajax/startChargeWx",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.status==0){
		        	onBridgeReady(data);
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

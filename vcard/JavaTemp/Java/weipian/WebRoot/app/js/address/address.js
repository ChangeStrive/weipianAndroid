function changeStatus(fdId,fdStatus){
	var params={};
	params["fdShopNo"]=fdShopNo;
	params["fdId"]=fdId;
	params["fdStatus"]=fdStatus;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/addressStatus",
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
}

function delAddress(fdId){
	var params={};
	params["fdShopNo"]=fdShopNo;
	params["fdId"]=fdId;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/delAddressList",
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
}

$(function(){
	$(".address_kongkong").css("margin-top",($(window).height()-110-132+30)/2);
	$(".ai-btn .default-icon,.default-span").on("click",function(){
		var fdStatus=$(this).attr("fdStatus");
		var fdId=$(this).attr("fdId");
		if(fdStatus=="1"){
			fdStatus="0";
		}else{
			fdStatus="1";
		}
		changeStatus(fdId,fdStatus);
	});
	$(".del-span").on("click",function(){
		var fdId=$(this).attr("fdId");
		delAddress(fdId);
	});
});

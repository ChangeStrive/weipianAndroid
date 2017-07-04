function saveAddress(){
	var params={};
	params["fdId"]=fdId;
	params["fdShopNo"]=fdShopNo;
	
	var fdConsignee=$("input[name='fdConsignee']").val();
	if(!fdConsignee){
		alert("请输入收货人姓名");
		return ;
	}
	params["fdConsignee"]=fdConsignee;
	
	
	var fdTel=$("input[name='fdTel']").val();
	if(!fdTel){
		alert("请输入手机号");
		return ;
	}
	params["fdTel"]=fdTel;
	
	var fdProvince=$("select[name='fdProvince']").val();
	if(!fdProvince){
		alert("请选择省份");
		return ;
	}
	params["fdProvince"]=fdProvince;
	
	
	var fdCity=$("select[name='fdCity']").val();
	if(!fdCity){
		alert("请选择城市");
		return ;
	}
	params["fdCity"]=fdCity;
	
	var fdArea=$("select[name='fdArea']").val();
	if(!fdArea){
		alert("请选择区域");
		return ;
	}
	params["fdArea"]=fdArea;
	
	var fdAddress=$("input[name='fdAddress']").val();
	if(!fdAddress){
		alert("请输入详细地址");
		return ;
	}
	params["fdAddress"]=fdAddress;
	
	var fdZipCode=$("input[name='fdZipCode']").val();
	if(!fdZipCode){
		alert("请输入邮编");
		return ;
	}
	params["fdZipCode"]=fdZipCode;
	
	params["fdStatus"]=$(".aac-content .default-icon").attr("fdStatus");
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/saveAddress",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.status==0){
        		if(backUrl){
        			window.location.href=backUrl+"&fdAddressId="+data.fdAddressId;
        		}else{
        			window.location.href=hxltUrl+"wx/address?fdShopNo="+fdShopNo;
        		}
        	}else{
        		alert(data.msg);
        	}
        }
	});
}

$(function(){
	var fdStatus=$(".aac-content .default-icon").attr("fdStatus");
	if(fdStatus=="1"){
		$(".aac-content .default-icon").addClass("aac-selected");
	}else{
		$(".aac-content .default-icon").attr("fdStatus","0");
	}
	$(".aac-content .default-icon").on("click",function(){
		if($(this).hasClass("aac-selected")){
			$(this).removeClass("aac-selected");
			$(this).attr("fdStatus","0")
		}else{
			$(this).addClass("aac-selected");
			$(this).attr("fdStatus","1")
		}
	});
	$(".confirmBtn").on("click",function(){
		saveAddress();
	});
});

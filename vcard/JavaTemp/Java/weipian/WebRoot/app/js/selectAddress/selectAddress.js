$(function(){
	$(".addressItem").on("click",function(){
		var fdId=$(this).attr("fdId");
		window.location.href=backUrl+"&fdAddressId="+fdId;
	});
	$(".fb_ahref").on("click",function(){
		var fdId=$(this).attr("fdId");
		window.location.href=hxltUrl+"wx/addAddress?fdShopNo="+fdShopNo+"&backUrl="+encodeURIComponent(backUrl);
	});
});
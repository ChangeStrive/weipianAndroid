function addBaseInfo(){
	var params={};
	var fdName=$("input[name='fdName']").val();
	var fdBirthday=$("input[name='fdBirthday']").val();
	var fdProvince=$(".ProvinceId").val();
	var fdCity=$(".CityId").val();
	var fdArea=$(".AreaId").val();
	if(!fdName){
		alert("请输入姓名");
		return ;
	}
	if(!fdBirthday){
		alert("请输入生日");
		return ;
	}
	if(!fdProvince){
		alert("请输入省份");
		return ;
	}
	if(!fdCity){
		alert("请输入城市");
		return ;
	}
	if(!fdArea){
		alert("请输入区域");
		return ;
	}
	var fdSex=$("input[name='fdSex']:checked").val();
	params["fdName"]=fdName;
	params["fdBirthday"]=fdBirthday;
	params["fdProvince"]=fdProvince;
	params["fdCity"]=fdCity;
	params["fdArea"]=fdArea;
	params["fdSex"]=fdSex;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/addBaseInfo",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.status==0){
        		window.location.href=hxltUrl+"wx/activeShop?fdShopNo="+fdShopNo;
        	}else if(data.status==-2){
        		 window.location.href=hxltUrl+"wx/login?fdShopNo="+fdShopNo+"&backUrl="+encodeURIComponent(window.location.href);
        	}else{
        		alert(data.msg);
        	}
        }
	});
}
$(function(){
	$(".commonbtn").on("click",function(){
		addBaseInfo();
	});
});
function hasLogin(){
	var params={};
	$.ajax({
	  type: "POST",
	  url: hxltUrl+"wxajax/hasLogin",
	   contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	  data:params,
	  dataType: "json",
	  success: function(result){
	 	if(result.status == 0){
	 		if(result.hasLogin=="1"){
	 			history.go(-2);
	 		}
		}
	  }
	});
}

hasLogin();

function login(){
	var params={};
	
	
	var fdTel=$("input[name='fdTel']").val();
	var fdPwd=$("input[name='fdPwd']").val();
	if(!fdTel){
		alert("请输入手机号");
		return ;
	}
	if(!fdPwd){
		alert("请输入密码");
		return ;
	}
	if($("input[name='fdTel']").val().length!=11){
		alert("请输入11位手机号码");
		return;
	}
	params["fdCode"]=fdTel;
	params["fdPwd"]=fdPwd;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/login",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.status==0){
        		if(data.baseFinish==1){
	        		if(backUrl){
	        			window.location.href=backUrl;
	        		}else{
	        			window.location.href=hxltUrl+"wx/mycard?fdShopNo="+fdShopNo;
	        		}
        		}else{
        			window.location.href=hxltUrl+"wx/baseinfo?fdShopNo="+fdShopNo;
        		}
        	}else{
        		alert(data.msg);
        	}
        }
	});
}

$(function(){
	
	$(".toLogin").on("click",function(){
		login();
	});
});

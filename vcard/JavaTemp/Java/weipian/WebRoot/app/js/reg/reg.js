var isClick=false;
var test = {
   node:null,
   count:180,
   start:function(){
      //console.log(this.count);
      if(this.count > 0){
         this.node.innerHTML =(this.count--)+"S";
         var _this = this;
         $(this.node).addClass("redSecond");
         setTimeout(function(){
             _this.start();
         },1000);
      }else{
    	  $(this.node).removeClass("redSecond");
         this.node.removeAttribute("disabled");
         this.node.innerHTML = "再次发送";
         this.count = 180;
         isClick=false;
      }
   },
   //初始化
   init:function(node){
      this.node = node;
      this.node.setAttribute("disabled",true);
      this.start();
   }
};

function getCodeBtn(){
	var params={};
	if($("input[name='fdTel']").val().length==0){
		alert("请输入11位手机号码");
		return;
	}
	params["fdCode"]=$("input[name='fdTel']").val();
	params["fdType"]='0';
	$.ajax({
		type: "POST",
		url:hxltUrl+"wxajax/getSmsCode",
		contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		data:params,
		dataType: "json",
		success: function(result){
			//test.init($(".LoginCode")[0]);
			if(result.status==0){
				test.init($(".LoginCode")[0]);
			}else{
				alert(result.msg);
			}
		}
	});
}

function getCode(){
	$(".getCode").on("click",function(){
		var fdTel=$("input[name='fdTel']").val();
		if(!fdTel){
			alert("请填写手机号!");
			return ;
		}
		getCodeBtn();
	});
}

function reg(){
	var params={};
	
	var fdTel=$("input[name='fdTel']").val();
	var fdCode=$("input[name='fdCode']").val();
	var fdPwd=$("input[name='fdPwd']").val();
	if(!fdTel){
		alert("请输入手机号");
		return ;
	}
	if(!fdCode){
		alert("请输入验证码");
		return ;
	}
	if($("input[name='fdTel']").val().length!=11){
		alert("请输入11位手机号码");
		return;
	}
	if($("input[name='fdPwd']").val().length==0){
		alert("请输入密码");
		return;
	}
	
	if(!$(".xieyiBtn").hasClass("xieyiBtn-check")){
		alert("请同意协议");
		return;
	}
	params["fdCode"]=fdTel;
	params["fdVaildCode"]=fdCode;
	params["fdPwd"]=fdPwd;
	params["fdShopNo"]=fdShopNo;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/reg",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.status==0){
        		window.location.href=hxltUrl+"wx/baseinfo?fdShopNo="+fdShopNo;
        	}else{
        		alert(data.msg);
        	}
        }
	});
}

$(function(){
	getCode();
	$(".xieyiBtn").on("click",function(){
		if($(this).hasClass("xieyiBtn-check")){
			$(this).removeClass("xieyiBtn-check");
		}else{
			$(this).addClass("xieyiBtn-check");
		}
	});
	
	$(".commonbtn").on("click",function(){
		reg();
	});
});
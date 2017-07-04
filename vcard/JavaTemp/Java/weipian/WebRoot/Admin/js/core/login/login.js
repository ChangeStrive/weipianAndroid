
$(function(){
	$(".checkBoxImage,.checkBoxTip").on("click",function(){
		var randoms=Math.random();
		$(".checkBoxImage").attr("src",hxltUrl+"Admin/jsp/number.jsp?time="+randoms);
	});
	$(".loginRightImage").on("click",function(){
		$(".leftContain").hide();
		$(".loginRightImage").hide();
		$(".loginLeftImage").show();
		$(".rightContain").show();
	});
	$(".loginLeftImage").on("click",function(){
		$(".loginLeftImage").hide();
		$(".rightContain").hide();
		$(".leftContain").show();
		$(".loginRightImage").show();
	});
	
	$("#form").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'LoginAction/login',
		success:function(result) {
			if(result.success) {
				if(backUrl){
					if(backUrl.indexOf("http")==-1){
						window.location.href =hxltUrl+backUrl;
					}else{
						window.location.href =backUrl;
					}
				}else{
					window.location.href =hxltUrl+ "LoginAction/toIndex";
				}
			}else{
				alert(result.msg);
			}
		}
	});
});
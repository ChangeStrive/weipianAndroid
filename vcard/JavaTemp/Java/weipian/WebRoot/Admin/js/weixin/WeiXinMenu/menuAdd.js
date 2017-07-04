$(function(){
	if(!$("input[name='fdLevel']").val()){
		$("input[name='fdLevel']").val("1");
	}
	$("#form").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WeiXinMenuAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				history.back(-1);
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
	
	$("input[name='fdType']:checked").each(function(){
		var value=$(this).val();
		if(value=="1"){
			$(".fdUrl").show();
			$(".fdKey").hide();
			$("input[name='fdKey']").val("");
		}else if(value=="0"){
			$(".fdUrl").hide();
			$(".fdKey").show();
			$("input[name='fdUrl']").val("");
		}else{
			$(".fdUrl").hide();
			$(".fdKey").show();
			$("input[name='fdUrl']").val("");
		}
	});
	$("input[name='fdType']").on("change",function(){
		var value=$(this).val();
		if(value=="1"){
			$(".fdUrl").show();
			$(".fdKey").hide();
			$("input[name='fdKey']").val("");
		}else if(value=="0"){
			$(".fdUrl").hide();
			$(".fdKey").show();
			$("input[name='fdUrl']").val("");
		}else{
			$(".fdUrl").hide();
			$(".fdKey").show();
			$("input[name='fdUrl']").val("");
		}
	});
	
});
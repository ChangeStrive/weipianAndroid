$(function(){
	
	if(!$("input[name='fdLevel']").val()){
		$("input[name='fdLevel']").val("1");
	}
	$("#form").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'SysMenuAction/save',
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
});
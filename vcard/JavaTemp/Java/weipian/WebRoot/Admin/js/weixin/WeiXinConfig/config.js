
$(function(){
	$("#configForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WeiXinConfigAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00030"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
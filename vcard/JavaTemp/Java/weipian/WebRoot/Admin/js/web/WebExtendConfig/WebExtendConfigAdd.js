$(function(){
	$("#form").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WebExtendConfigAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00036"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
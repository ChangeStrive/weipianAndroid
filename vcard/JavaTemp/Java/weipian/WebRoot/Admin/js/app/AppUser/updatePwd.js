$(function(){
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'AppUserAction/changePassword',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00091"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
$(function(){
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'SysUserAction/changePassWord',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"LoginAction/toIndex"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
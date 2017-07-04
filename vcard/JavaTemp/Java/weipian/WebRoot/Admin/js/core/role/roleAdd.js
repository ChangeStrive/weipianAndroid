$(function(){
	
	$("#form").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'SysRoleAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href=hxltUrl+"SysRoleAction/list";
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
$(function(){
	if(fdId!="") {
		$("input[name=fdLoginName]").attr("disabled","disabled");
	}
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'SysUserAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00013"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
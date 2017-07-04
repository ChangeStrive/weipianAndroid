$(function(){
	if(fdId!="") {
		$("input[name=fdLoginName]").attr("disabled","disabled");
		if(fdSex == '0') {
			$("#female").attr("checked","checked");
		}
	}
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WebUserAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00018"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
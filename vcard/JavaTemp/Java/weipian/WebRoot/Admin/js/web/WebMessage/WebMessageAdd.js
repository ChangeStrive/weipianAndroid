$(function(){
	if(fdId!="") {
		if(fdStatus == '1') {
			$("#fdStatus1").attr("checked","checked");
		}
	}
	$("#linkForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WebMessageAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00034"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
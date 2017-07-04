$(function(){
	$("#linkForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WebCustomerAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00042"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
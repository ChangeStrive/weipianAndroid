$(function(){
	$("#AppUserShopForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'AppUserShopAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00101"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});

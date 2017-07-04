var AppPayTypeConfig={
		
}
$(function(){
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'AppPayTypeAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00094"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
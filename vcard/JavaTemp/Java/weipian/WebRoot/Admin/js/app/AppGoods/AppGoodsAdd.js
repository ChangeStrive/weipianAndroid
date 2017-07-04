$(function(){
	var fdContent = $("input[name=fdDesc]");
	ue.addListener( 'ready', function(editor) {
		ue.setContent(fdContent.val());
 	});
	$("#userForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'AppGoodsAction/save',
		beforeSubmit:function(){
			fdContent.val(ue.getContent());
		},
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00093"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
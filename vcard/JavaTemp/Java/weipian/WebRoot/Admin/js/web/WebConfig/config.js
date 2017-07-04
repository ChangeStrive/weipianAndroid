
$(function(){
	var fdFootContent = $("input[name=fdFootContent]");
	ue.addListener( 'ready', function( editor ) {
     ue.setContent(fdFootContent.val());
 	} );
	$("#configForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'WebConfigAction/save',
		beforeSubmit:function(){
			fdFootContent.val(ue.getContent());
		},
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00021"
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
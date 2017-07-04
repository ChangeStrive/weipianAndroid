$(function(){
	if(fdId!="") {
		$("input[name=fdLoginName]").attr("disabled","disabled");
	}
	
	$("#flowForm").formSubmit({
		tip:".form_checktip",
		url:hxltUrl+'SysFlowNumRuleAction/save',
		success:function(data) {
			$(".alert-content").text(data.msg);
			if(data.success) {
				updateAlert(data.msg,"alert-success");
				window.location.href = hxltUrl+"SysFlowNumRuleAction/list";
			} else {
				updateAlert(data.msg,"alert-error");
			}
		}
	});
});
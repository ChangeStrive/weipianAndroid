var WeiXinWelcomesConfig = {
	radioChecked : 0,
	clickRadio:function(){
		if(fdPid!="#"){
			$("#fdType").hide();
		}
		WeiXinWelcomesConfig.showType();
		$("input[name=fdType]").click(function(){
			WeiXinWelcomesConfig.radioChecked = $(this).val();
			WeiXinWelcomesConfig.showType();
		});
	},
	showType:function(){
		switch($("input[name=fdType]:checked").attr("id")){
			case "fdType1":
				$("#fdUrl").show();
				$("#fdPicUrl").show();
				$("#fdContent").show();
				WeiXinWelcomesConfig.radioChecked = '1';
				if(fdId==""){
					$("input[name=fdUrl]").attr("disabled",false);
					$("input[name=fdContent]").attr("disabled",false);
				}
				break;
			case "fdType2":
				$("#fdUrl").hide();
				$("#fdPicUrl").hide();
				$("#fdContent").hide();
				WeiXinWelcomesConfig.radioChecked = '0';
				if(fdId==""){
					$("input[name=fdUrl]").attr("disabled",true);
					$("input[name=fdContent]").attr("disabled",true);
				}
				break;
			default:
				break;
		}
	},
	save:function(){
		var fdContent = $("input[name=fdContent]");
		ue.addListener( 'ready', function(editor) {
			ue.setContent(fdContent.val());
	 	});
	 	if(fdType == "0") {
			$("#fdType2")[0].checked=true;
		}
		$("#form").formSubmit({
			tip:".form_checktip",
			url:hxltUrl+'WeiXinWelcomesAction/save',
			beforeSubmit:function(){
				if(WeiXinWelcomesConfig.radioChecked=='1'){
					fdContent.val(ue.getContent());
				} else {
					$("input[name=fdUrl]").val("");
					$("input[name=fdContent]").val("");
				}
			},
			success:function(data) {
				$(".alert-content").text(data.msg);
				if(data.success) {
					updateAlert(data.msg,"alert-success");
					window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00031"
				} else {
					updateAlert(data.msg,"alert-error");
				}
			}
		});
	},
	init:function(){
		WeiXinWelcomesConfig.save();
		WeiXinWelcomesConfig.clickRadio();
	}
}

$(function(){
	WeiXinWelcomesConfig.init();
});
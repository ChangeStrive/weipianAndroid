var WeiXinKeywordReplyConfig = {
	radioChecked:1,
	clickRadio:function(){
		if(fdStatus=="0"){
			$("#fdStatus2")[0].checked=true;
		}
		WeiXinKeywordReplyConfig.showType();
		$("input[name=fdType]").click(function(){
			WeiXinKeywordReplyConfig.radioChecked = $(this).val();
			WeiXinKeywordReplyConfig.showType();
		});
	},
	showType:function(){
		switch($("input[name=fdType]:checked").attr("id")){
			case "fdType1":
				$("#fdUrl").show();
				$("#fdPicUrl").show();
				if(fdId==""){
					$("input[name=fdUrl]").attr("disabled",false);
				}
				break;
			case "fdType2":
				$("#fdUrl").hide();
				$("#fdPicUrl").hide();
				if(fdId==""){
					$("input[name=fdUrl]").attr("disabled",true);
				}
				break;
			default:
				break;
		}
	},
	save:function(){
		$("#form").formSubmit({
			tip:".form_checktip",
			url:hxltUrl+'WeiXinKeywordReplyAction/save',
			beforeSubmit:function(){
				if(WeiXinKeywordReplyConfig.radioChecked=='0'){
					$("input[name=fdUrl]").val("");
				}
			},
			success:function(data) {
				$(".alert-content").text(data.msg);
				if(data.success) {
					updateAlert(data.msg,"alert-success");
					window.location.href = hxltUrl+"SysMenuAction/redirect?fdNo=00032"
				} else {
					updateAlert(data.msg,"alert-error");
				}
			}
		});
	},
	init:function(){
		WeiXinKeywordReplyConfig.save();
		WeiXinKeywordReplyConfig.clickRadio();
	}
};

$(function(){
	WeiXinKeywordReplyConfig.init();
});
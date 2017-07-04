var logoutconfig = {
	init:function(option) {
		logoutconfig.exit(option);
	},
	exit:function(option){
		$(option.button).click(function(){
			window.location.href = hxltUrl+"LoginAction/logout";
		})
	}
}

$(function(){
	logoutconfig.init({
		button:".logout_user"
	});
})
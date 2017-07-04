var WeiXinUserConfig = {
	getUserList:function(){
		$(".alert-content").val("正在加载中...");
		$("#top-alert").show();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"WeiXinUserAction/getUserList",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        timeout:1000*60*30,
	        success: function(data){
	        	$("#top-alert").hide();
	        	if(data.success){
	        		updateAlert("获取成功!","alert-success");
	        		WeiXinUserConfig.table.fnPageChange(0);
	        	}else{
	        		updateAlert("获取失败!","alert-error");
	        	}
	        }
		});
	},
	load:function(){
		WeiXinUserConfig.table = $('#tableList').sysTable({
			url:hxltUrl+'WeiXinUserAction/getList',
			formId:'.search-form',
	        "aoColumns" : [  {
						"sWidth" : "auto",
						"fnRender":function(obj){
							return '<td><input class="check-single" type="checkbox" value="'+obj.aData.fdId+'"></td>';
						}
					},{
						"mDataProp":"fdOpenId",
						"sWidth" : "auto",
						"fnRender":function(obj){
							return "<img src='"+obj.aData.fdHeadImgUrl+"' width='100' height='100' />";
						}
					},{
						"mDataProp":"fdNickName",
						"sWidth" : "auto"
					},{
						"sWidth" : "auto",
						"fnRender":function(obj){
							var fdSex = obj.aData.fdSex
							if(fdSex == '1') {
								return "男";
							} else if(fdSex == '2') {
								return "女";
							} else {
								return "未知";	
							}
						}
					},{
						"mDataProp" : "fdCity",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdCountry",
						"sWidth" : "auto"
					},{
						"mDataProp" : "fdProvince",
						"sWidth" : "auto"
					}
			]
	    });  
	}
};
$(function(){
	WeiXinUserConfig.load();
});
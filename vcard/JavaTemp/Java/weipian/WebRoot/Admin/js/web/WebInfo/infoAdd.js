var WebInfoConfig={
	loadType:function(){
		var params={};
		params["fdId"]="40288192478a685d01478a69e77a0000";
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysDictAction/getListByParentId",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list.length>0){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				var str='<h3 fdValue="'+item.value+'" fdText="'+item.text+'"><a href="'+hxltUrl+'WebInfoAction/list?fdTypeId='+item.value+'">'+item.text+'</a></h3>';
							$("#subnav").append(str);
	        			}
	        			if(!fdTypeId){
	        				if($("#subnav h3").length>0){
	        					fdTypeId=$("#subnav h3").attr("fdValue");
	        				}
	        			}
	        			if(fdTypeId){
	        				var h3=$("#subnav h3[fdValue='"+fdTypeId+"']");
		        			h3.attr("id","side-sub-menu-current");
	        			}
	        		}
	        	}
	        }
		});
	},
	ueListener:function(fields,obj){
		obj.addListener( 'ready', function( editor ) {
			obj.setContent(fields.val());
	 	});
	},
	init:function(){
		WebInfoConfig.loadType();
		
		var fdContent = $("input[name=fdContent]");
		
		WebInfoConfig.ueListener(fdContent,fdContentUE);
		
		$("#form").formSubmit({
			tip:".form_checktip",
			url:hxltUrl+'WebInfoAction/save',
			beforeSubmit:function(){
				fdContent.val(fdContentUE.getContent());
			},
			success:function(data) {
				$(".alert-content").text(data.msg);
				if(data.success) {
					updateAlert(data.msg,"alert-success");
					history.back(-1);
				} else {
					updateAlert(data.msg,"alert-error");
				}
			}
		});
	}
}
$(function(){
	WebInfoConfig.init();
});
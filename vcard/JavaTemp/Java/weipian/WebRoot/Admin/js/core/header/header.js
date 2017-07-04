var headerConfig={
	appendHtml:function(item){
		var str='<li fdNo="'+item.fdNo+'"><a href="'+hxltUrl+'SysMenuAction/redirect?fdNo='+item.fdNo+'">'+item.fdName+'</a></li>';
		$(".main-nav").append(str);
	},
	load:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysMenuAction/getFirstMenuByUser",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list.length>0){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				headerConfig.appendHtml(item);
	        			}
	        		}
	        		if(typeof(firstMenuNo)!="undefined"){
	        			$(".main-nav").find("li[fdNo='"+firstMenuNo+"']").addClass("current");
	        		}
	        	}else{
	        		updateAlert("加载失败!","alert-error");
	        	}
	        }
		});
	}
};
$(function(){
	headerConfig.load();
});
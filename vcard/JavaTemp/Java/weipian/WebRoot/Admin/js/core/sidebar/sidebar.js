var sideBarConfig={
	appendHtml:function(item){
		if(item.fdGroup){
			var fdGroup=$("#subnav ul[fdGroup='"+item.fdGroup+"']");
			if(fdGroup.length>0){
				var str='<li fdNo="'+item.fdNo+'"> <a class="item" href="'+hxltUrl+'SysMenuAction/redirect?fdNo='+item.fdNo+'" >'+item.fdName+'</a></li>'
				fdGroup.append(str);
			}else{
				var str='<h3 fdGroup="'+item.fdGroup+'"><i class="icon icon-fold"></i>'+item.fdGroup+'</h3>'
					+'<ul fdGroup="'+item.fdGroup+'" class="side-sub-menu subnav-off">'
					+'<li fdNo="'+item.fdNo+'"> <a class="item" href="'+hxltUrl+'SysMenuAction/redirect?fdNo='+item.fdNo+'">'+item.fdName+'</a></li>'
					+'</ul>';
				$("#subnav").append(str);
			}
		}else{
			var str='<h3 fdNo="'+item.fdNo+'"><a href="'+hxltUrl+'SysMenuAction/redirect?fdNo='+item.fdNo+'">'+item.fdName+'</a></h3>';
			$("#subnav").append(str);
		}
	},
	load:function(){
		var params={};
		if(firstMenuNo){
			params["fdNo"]=firstMenuNo;
			$.ajax({
		        type:'POST',
		        url:hxltUrl+"SysMenuAction/getSecondMenuByUser",
		        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		        data:params,
		        dataType: "json",
		        success: function(data){
		        	if(data.success){
		        		if(data.list.length>0){
		        			for(var i=0;i<data.list.length;i++){
		        				var item=data.list[i];
		        				sideBarConfig.appendHtml(item);
		        			}
		        			if(typeof(secondMenuNo)!="undefined"){
		        				var li=$("#subnav li[fdNo='"+secondMenuNo+"']");
		        				if(li.length>0){
		        					li.addClass("current");
		        					li.parent().removeClass("subnav-off");
		        					li.parent().prev().find("i").removeClass("icon-fold");
		        				}
		        				var h3=$("#subnav h3[fdNo='"+secondMenuNo+"']");
		        				h3.attr("id","side-sub-menu-current");
		        			}
		        		}
		        		if(typeof(firstMenuNo)=="undefined"){
		        			$(".main-nav").find("li[fdNo='"+firstMenuNo+"']").addClass("current");
		        		}
		        	}else{
		        		updateAlert("加载失败!","alert-error");
		        	}
		        }
			});
		}
	}
};
$(function(){
	sideBarConfig.load();
});
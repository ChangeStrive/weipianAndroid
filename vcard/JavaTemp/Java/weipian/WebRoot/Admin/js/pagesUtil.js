var pageUtils={
	appendHtml:function(pageDiv,currentPage,begin,end){
		for(var i=begin;i<end;i++){
			var str="";
			if(currentPage==i){
				str=' <li class="active" index="'+i+'"><a href="javascript:void(0);">'+i+'</a></li>';
			}else{
				str=' <li><a href="javascript:void(0);" index="'+i+'">'+i+'</a></li>';
			}
			$(pageDiv).append(str);
		}
	},
	itemClick:function(pageDiv,fn){
		$(pageDiv).find("li").not(".disabled").not(".active").on("click",function(){
			var parent=$(this).parent();
			var start=parseInt(parent.attr("start"));
			var limit=parseInt(parent.attr("limit"));
			var pageSize=parseInt(parent.attr("pageSize"));
			var currentPage=parseInt(parent.attr("currentPage"));
			var index=$(this).find("a").attr("index");
			if(index=="first"){
				start=0;
			}else if(index=="last"){
				start=limit*(pageSize-1);
			}else if(index=="pre"){
				if(currentPage>1){
					start=limit*(currentPage-2);
				}else{
					start=0;
				}
			}else if(index=="next"){
				if(currentPage<pageSize){
					start=limit*currentPage;
				}else{
					start=start;
				}
			}else{
				start=(parseInt(index)-1)*limit;
			}
			fn(start);
		});
	},
	init:function(pageDiv,totalSize,start,limit,fn){
		$(pageDiv).html("");
		var pages=parseInt(totalSize/limit)+(totalSize%limit>0?1:0);
		var currentPage=parseInt(start/limit)+1;
		var first=currentPage-4;
		var last=currentPage+5;
		if(pages==0){
			var str=' <li class="disabled"><a href="javascript:void(0);">暂无数据</a></li>';
			$(pageDiv).append(str);
		}else{
			$(pageDiv).append('<li><a href="javascript:void(0);" index="first">首页</a></li>');
			$(pageDiv).append('<li><a href="javascript:void(0);" index="pre">&laquo;</a></li>');
			if(pages<11){
				pageUtils.appendHtml(pageDiv,currentPage,1,pages+1);
			}else if(first>0&&last<pages+1){
				pageUtils.appendHtml(pageDiv,currentPage,first,first+10);
			}else if(first>0){
				pageUtils.appendHtml(pageDiv,currentPage,pages-10,pages+1);
			}else if(last<pages+1){
				pageUtils.appendHtml(pageDiv,currentPage,1,11);
			}
			$(pageDiv).append('<li><a href="javascript:void(0);" index="next">&raquo;</a></li>');
			$(pageDiv).append('<li><a href="javascript:void(0);" index="last">末页</a></li>');
		}
		$(pageDiv).attr("pageSize",pages);
		$(pageDiv).attr("currentPage",currentPage);
		$(pageDiv).attr("start",start);
		$(pageDiv).attr("limit",limit);
		if(fn){
			pageUtils.itemClick(pageDiv,fn);
		}
	}
};
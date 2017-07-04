
function reply(replyDetail){
	var params={};
	params["replyDetail"]=replyDetail;
	params["fdOrderId"]=fdOrderId;
	$.ajax({
		type: "POST",
		url:hxltUrl+"wxajax/reply",
		contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		data:params,
		dataType: "json",
		success: function(result){
			if(result.status==0){
				window.location.href=hxltUrl+"wx/myReply?fdShopNo="+fdShopNo;
			}else if(result.status==-2){
				 window.location.href=hxltUrl+"wx/login?fdShopNo="+fdShopNo+"&backUrl="+encodeURIComponent(window.location.href);
			}else{
				alert(result.msg);
			}
		}
	});
}


$(function(){
	$(".orderPayBtn").on("click",function(){
		var flag=true;
		var replyDetail=[];
		$(".orderItem_reply").each(function(){
			var fdOrderItemId=$(this).attr("fdOrderItemId");
			var fdReplyContent=$(this).val();
			if(!fdReplyContent){
				flag=false;
			}
			var o={};
			o.fdOrderItemId=fdOrderItemId;
			o.fdReplyContent=fdReplyContent;
			replyDetail.push(o);
		});
		if(!flag){
			alert("您还有商品没有评论！");
			return ;
		}
		reply(JSON.stringify(replyDetail));
	});
});
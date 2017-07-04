var loadSelect=function($select){
	var value=$select.attr("dictValue");
	var fdId=$select.attr("dictKey");
	var text=$select.attr("dictText");
	var params={};
	params["fdId"]=fdId;
	$.ajax({
        type:'POST',
        url:hxltUrl+"SysDictAction/getListByParentId",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.success){
        		if(data.list.length>0){
        			var str='<option value="">请选择----</option>';
					$select.append(str);
        			for(var i=0;i<data.list.length;i++){
        				var item=data.list[i];
        				var str='<option value="'+item.value+'">'+item.text+'</option>';
						$select.append(str);
        			}
        			if(value){
        				var selectItem=$select.find("option[value='"+value+"']");
        				selectItem.attr("selected","selected");
        				$(text).val(selectItem.html());
        			}else{
        				var selectItem=$select.find("option:eq(0)");
        				if(selectItem.length>0){
        					selectItem.attr("selected","selected");
        					$(text).val(selectItem.html());
        				}
        			}
        				$select.on("change",function(){
        					$(text).val($select.find("option:selected").html());
	        			});
	        		}
	        	}
	        }
	});
}

function saveStore(){
	var params={};
	var fdStoreName=$("input[name='fdStoreName']").val();
	var fdStoreBrand=$("input[name='fdStoreBrand']").val();
	var fdStoreAddress=$("input[name='fdStoreAddress']").val();
	var fdStoreType=$(".fdStoreType").val();
	if(!fdStoreName){
		alert("请输入店铺名称");
		return ;
	}
	if(!fdStoreBrand){
		alert("请输入品牌名称");
		return ;
	}
	if(!fdStoreType){
		alert("请选择所属类别");
		return ;
	}
	if(!fdStoreAddress){
		alert("请输入所在地区");
		return ;
	}
	
	var flag=true;
	$(".addImageBtn img").each(function(i){
		var src=$(this).attr("src");
		var hasUpload=$(this).attr("hasUpload");
		if(hasUpload=="1"){
			params["fdPicUrl"+(i+1)]=src;
		}else{
			flag=false;
		}
	});
	if(!flag){
		alert("请上传图片");
		return ;
	}
	params["fdStoreName"]=fdStoreName;
	params["fdStoreBrand"]=fdStoreBrand;
	params["fdStoreAddress"]=fdStoreAddress;
	params["fdStoreType"]=fdStoreType;
	$.ajax({
        type:'POST',
        url:hxltUrl+"wxajax/saveStore",
        data:params,
        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: "json",
        success: function(data){
        	if(data.status==0){
        		window.location.href=hxltUrl+"wx/activeShop?fdShopNo="+fdShopNo;
        	}else if(data.status==-2){
        		 window.location.href=hxltUrl+"wx/login?fdShopNo="+fdShopNo+"&backUrl="+encodeURIComponent(window.location.href);
        	}else{
        		alert(data.msg);
        	}
        }
	});
}

$(function(){
	if(fdPicUrl){
		var pics=fdPicUrl.split(",");
		$(".addImageBtn").each(function(i){
			var picUrl=downAction+pics[i];
			var $img=$(this).find("img");
			if(picUrl){
				convertImgToBase64(picUrl,function(base64Img){
					$img.attr("src",base64Img);
					$img.attr("hasUpload","1");
		        });
			}
		});
	}
	$("select[dictkey]").each(function(){
		loadSelect($(this));
	});
	$(".addImageBtn").on("click",function(){
		myCropUtils.uploadInit($(this).find("img"),1,function(newImage,oldImage){});
	});
	$(".commonbtn").on("click",function(){
		saveStore();
	});
});
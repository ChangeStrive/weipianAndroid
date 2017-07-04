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

var message;
$(function(){
	if(fdUserMessage){
		message=$.parseJSON(fdUserMessage);
		for(var key in message){
			var a=message[key];
			var $input=$("input[name='"+key+"']");
			if($input.length==0){
				$input=$("select[name='"+key+"']");
				$("select[name='"+key+"']").attr("dictValue",a[0]);
				loadSelect($input);
			}
			if($input.length==0){
				$input=$("textarea[name='"+key+"']");
			}
			if(a){
				$input.val(a[0]);
				if(a.length>1){
					var datatype="";
					if($input.find("datatype")){
						datatype='datatype="*"';
					}
					var pp=$input.parent().parent();
					for(var k=1;k<a.length;k++){
						var data={
							"name":$input.attr("name"),
							"datatype":datatype,
							"value":a[k],
							"placeholder":$input.attr("placeholder")
						};
						var str=TemplateUtils.getTemplate("#inputTemplate",data);
						pp.append(str);
					}
				}
			}
		}
	}
});
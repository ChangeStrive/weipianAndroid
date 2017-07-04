$(function(){
	$("select[data-selectKey]").each(function(){
     		var value=$(this).attr("data-selectValue");
     		var key=$(this).attr("data-selectKey");
     		var text=$(this).attr("data-selectText");
     		var $select=$(this);
     		var params={};
			params["key"]=key;
			$.ajax({
		        type:'POST',
		        url:hxltUrl+"SelectUtilsAction/commonCommob",
		        data:params,
		        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
		        dataType: "json",
		        success: function(data){
		        	if(data.success){
		        		$select.append("<option value=''>请选择--</option>");
		        		if(data.list.length>0){
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
		        				if($select.val()){
		        					$(text).val($select.find("option:selected").html());
		        				}else{
		        					$(text).val("");
		        				}
		        			});
		        		}
		        	}
		        }
			});
     }); 
});
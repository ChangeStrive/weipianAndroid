var menuSelectUtils={
	load:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysMenuAction/getMenuTree",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		menuSelectUtils.treeData=data.tree;
	        		var str='<option value="#" fdName="" fdLevel="0">顶级菜单</option>';
	        		str=menuSelectUtils.appendOption(str,menuSelectUtils.treeData,1);
	        		menuSelectUtils.treeHtml=str;
	        		$("select[data-key='selectMenu']").each(function(){
	        			var value=$(this).attr("selectValue");
	        			$(this).html(str);
	        			if(value){
	        				$(this).find("option[value='"+value+"']").attr("selected","selected");
	        			}
	        		});
	        	}
	        }
		});
	},
	getText:function(text,index){
		var str="";
		for(var i=0;i<index-1;i++){
			str+="&nbsp;&nbsp;";
		}
		if(index>1){
			return str+"└"+text;
		}else{
			return str+text;
		}
	},
	appendOption:function(str,treeData,index){
		if(treeData.length>0){
			for(var i=0;i<treeData.length;i++){
				 var item=treeData[i];
				 var text=menuSelectUtils.getText(item.fdName,index);
				 str+='<option value="'+item.fdId+'" fdName="'+item.fdName+'" fdLevel="'+index+'">'+text+'</option>';
				 if(item.childs){
					str=menuSelectUtils.appendOption(str,item.childs,index+1);
				 }
			}
		}
		return str;
	}
};

$(function(){
	menuSelectUtils.load();
	
});
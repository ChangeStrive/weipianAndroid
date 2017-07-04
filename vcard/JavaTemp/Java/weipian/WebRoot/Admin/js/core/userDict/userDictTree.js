var dictConfig = {
	treeSetting:{
		checkbox: false,
		url: hxltUrl+'SysDictAction/getList?node='+node,
		onBeforeExpand:function(node,param){
			$(this).tree('options').url = hxltUrl+"SysDictAction/getList?node=" + node.id;
		},
		onClick:function(node){
			$(this).bind("selectstart",function(){return false;});
			dictConfig.dictForm(node);
		},
		onDblClick:function(node){
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			node.state = node.state === 'closed' ? 'open' : 'closed';
		},
		onContextMenu:function(e,node) {
			e.preventDefault();
			$(this).tree('select',node.target);
			$('#menu').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
		}
	},
	dictForm:function(node) {
		$("input[name=fdId]").val(node.id);
		$("input[name=fdName]").val(node.text);
		$("input[name=fdValue]").val(node.fdValue);
		$("input[name=fdFunName]").val(node.fdFunName);
		$("input[name=fdFunId]").val(node.pid);
		$("input[name=fdSeqNo]").val(node.fdSeqNo);
	},
	append:function(){
		var m = $('#dictTree');
		var node = m.tree('getSelected');
		if(node) {
			$("input[name=fdFunName]").val(node.text);
			$("input[name=fdFunId]").val(node.id);
			$("input[name=fdId]").val("");
			$("input[name=fdName]").val("");
			$("input[name=fdValue]").val("");
			$("input[name=fdSeqNo]").val("0");
		}
	},
	del:function(){
		var flag=confirm("删除后不能恢复，是否删除？");
		if(!flag) return ;
		var m = $('#dictTree');
		var node = m.tree('getSelected');
		var params={};
		params["fdId"]=node.id;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysDictAction/delete",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		updateAlert("删除成功!","alert-success");
	        		m.tree("remove",node.target);
	        	}else{
	        		updateAlert("删除失败!","alert-error");
	        	}
	        }
		});
	},
	init:function(){
		$('#dictTree').tree(dictConfig.treeSetting);
		$("#dictForm").formSubmit({
			tip:".form_checktip",
			url:hxltUrl+'SysDictAction/save',
			success:function(data) {
				if(data.success) {
					updateAlert(data.msg,"alert-success");
					var m = $('#dictTree');
					var fdId=$("input[name=fdId]").val();
					var fdFunId=$("input[name=fdFunId]").val();
					if(fdId){
						var node=m.tree("find",fdId);
						$.extend(true, node,data.node);
						node.text=data.node.fdName;
						m.tree("update",node)
					}else{
						var newNode={};
						$.extend(true, newNode,data.node);
						newNode.id=data.node.fdId;
						newNode.text=data.node.fdName;
						newNode.pid=data.node.fdFunId;
						if(fdFunId=="root"){
							m.tree("append",{data:newNode});
						}else{
							m.tree("append",{parent:m.tree("find",fdFunId).target,data:newNode});
						}
						
					}
					if(dictConfig.next){
						$("input[name=fdId]").val("");
						$("input[name=fdName]").val("");
						$("input[name=fdValue]").val("");
						$("input[name=fdSeqNo]").val("0");
					}
				} else {
					updateAlert(data.msg,"alert-error");
				}
			}
		});
	}
}

$(function(){
	dictConfig.init();
});
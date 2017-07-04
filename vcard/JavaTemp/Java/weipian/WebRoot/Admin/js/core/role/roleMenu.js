var roleMenuConfig={
	save:function(){
		var params={};
		params["fdId"]=fdId;
		var fdMenuIds=[];
		$("input[name='menuIds']:checked").each(function(){
			fdMenuIds.push($(this).val());
		});
		params["fdMenuIds"]=fdMenuIds.join();
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/saveMenu",
	        data:params,
	       	contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		window.location.href=hxltUrl+"SysRoleAction/list";
	        	}else{
	        		updateAlert("保存失败!","alert-error");
	        	}
	        }
		});
	},
	loadRoleSelect:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/getList",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list){
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				var str='<option value="'+item.fdId+'">'+item.fdName+'</option>';
	        				$("#roleSelect").append(str);
	        			}
	        			
	        			$("#roleSelect option[value='"+fdId+"']").attr("selected","selected");
	        			$("#roleSelect").on("change",function(){
	        				window.location.href=hxltUrl+"SysRoleAction/roleMenulist?fdId="+$("#roleSelect option:selected").val();
	        			});
	        		}
	        	}else{
	        		updateAlert("操作失败!","alert-error");
	        	}
	        }
		});
	},
	loadRole:function(){
		var params={};
		params["fdId"]=fdId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysRoleAction/getMenuByRoleId",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list){
	        			for(var i=0;i<data.list.length;i++){
	        				$("#"+data.list[i].fdMenuId)[0].checked=true;
	        			}
	        		}
	        	}else{
	        		updateAlert("获得菜单失败!","alert-error");
	        	}
	        }
		});
	},
	loadMenu:function(){
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysMenuAction/getMenuTree",
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		roleMenuConfig.appendMenuHtml(data.tree);
	        	}else{
	        		updateAlert("获得菜单失败!","alert-error");
	        	}
	        }
		});
	},
	apppendThreeHtml:function(item){
		var str='<label class="checkbox" >'
                      +'<input class="auth_rules auth_item" type="checkbox"  id="'+item.fdId+'" name="menuIds" value="'+item.fdId+'"/>'+item.fdName                                                
               +'</label>';
		return $(str);
	},
	apppendSecondMenuHtml:function(item){
		var str='<div class="rule_check">' 
					+'<div>'
	                      +'<label class="checkbox" >'
	                      		+'<input class="auth_rules rules_row" type="checkbox" id="'+item.fdId+'" name="menuIds"  value="'+item.fdId+'"/>'+item.fdName
	                      +'</label>'
	                +'</div>' 
                +'</div>';
		return $(str);
	},
	apppendFirstMenuHtml:function(item){
		var str='<dl class="checkmod">'
						+'<dt class="hd">'
							+'<label class="checkbox"><input class="auth_rules rules_all"  id="'+item.fdId+'" type="checkbox" name="menuIds" value="'+item.fdId+'">'+item.fdName+'</label>'
						+'</dt>'
			  +'</dl>';
		return $(str);
	},
	appendMenuHtml:function(tree){
		if(tree.length>0){
			for(var i=0;i<tree.length;i++){
				var item=tree[i];
				var firstMenu=roleMenuConfig.apppendFirstMenuHtml(item);
				$(" .menuMain").append(firstMenu);
				if(item.childs&&item.childs.length>0){
					var secondItems=$('<dd class="bd"></dd>');
					firstMenu.append(secondItems);
					for(var k=0;k<item.childs.length;k++){
						var secondItem=item.childs[k];
						var secondMenu=roleMenuConfig.apppendSecondMenuHtml(secondItem);
						secondItems.append(secondMenu);
						if(secondItem.childs&&secondItem.childs.length>0){
							secondMenu.append('<span class="divsion">&nbsp;</span>');
							var threeItems=$('<span class="child_row"></span>');
							secondMenu.append(threeItems);
							for(var j=0;j<secondItem.childs.length;j++){
								var treeItem=secondItem.childs[j];
								var treeMenu=roleMenuConfig.apppendThreeHtml(treeItem);
								threeItems.append(treeMenu);
							}
						}
					}
				}
			}
			$(".rules_all").on("change",function(){
				var item=$(this).parent().parent().parent();
				var cb=item.find("input[type='checkbox']");
				if($(this).attr("checked")){
					cb.each(function(){
						this.checked=true;
					});
				}else{
					cb.each(function(){
						this.checked=false;
					});
				}
			});
			
			$(".rules_row").on("change",function(){
				var item=$(this).parent().parent().parent();
				var cb=item.find(".child_row").find("input[type='checkbox']");
				if($(this).attr("checked")){
					item.parent().parent().find(".rules_all")[0].checked=true;
					cb.each(function(){
						this.checked=true;
					});
				}else{
					cb.each(function(){
						this.checked=false;
					});
				}
			});
			
			$(".auth_item").on("change",function(){
				if($(this).attr("checked")){
					var row=$(this).parent().parent().parent();
					row.find(".rules_row")[0].checked=true;
					row.parent().parent().find(".rules_all")[0].checked=true;
				}
			});
			roleMenuConfig.loadRole();
		}
	}
};
$(function(){
	roleMenuConfig.loadMenu();
	roleMenuConfig.loadRoleSelect();
});
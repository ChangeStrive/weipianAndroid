var commonSelectUitls={
	singleSelect:false,
	init:function(config){
		return commonSelectUitls.getCom(config)
	},
	getCom:function(configs){
		var selectUitls={
			title:'通用选择框',
			width:600,
			height:400,
			limit:100,
			singleSelect:false,
			init:function(config){
				$.extend(true,this, config);
				commonSelectUitls.singleSelect=this.singleSelect;
				$("#selectUitls-win"+config.id).remove();
				var $content=$("#selectUitls-win"+config.id);
				if($content.length>0){
					$content.window('open');
				}else{
					selectUitls.initHTML();
					selectUitls.initCom();
				}
				selectUitls.initSureFn();
				selectUitls.load();
			},
			initSureFn:function(){
				var config=this;
				var $content=$("#selectUitls-win"+config.id);
				$content.find(".selectUtilsSureBtn").unbind( "click");
				$content.find(".searchBtnSubmit").unbind( "click");
				$content.find(".selectUtilsSureBtn").on("click",function(){
					$content.window('close');
					$content.find(".searchForm")[0].reset();
					var selected=$content.find('.rightGrid').datagrid('getData');
					config.completeFn(selected);
				});
				$content.find(".searchBtnSubmit").on("click",function(){
					selectUitls.load();
				});
				
			},
			load:function(){
				var config=this;
				var $content=$("#selectUitls-win"+config.id);
				var params={};
				params['limit']=config.limit;
				params['valueId']="";
				if(config.valueName){
					params['valueId']=$("input[name='"+config.valueName+"']").val();
				}
				var $searchForm=$content.find(".searchForm");
				if($searchForm.length>0){
					$searchForm.find("input").each(function(){
						params[$(this).attr("name")]=$(this).val();
					});
				}
				$.ajax({
			        type:'POST',
			        url:config.url,
			        data:params,
			        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
			        dataType: "json",
			        success: function(data){
			        	if(data.success){
			        		$content.find('.rightGrid').datagrid('loadData',data.rightList);
					    	$content.find('.leftGrid').datagrid('loadData',data.leftList);
			        	}else{
			        		alert("加载数据失败");
			        	}
			        }
				});
			},
			ininForm:function(){
				//初始化表单
				var config=this;
				var formItemStr='';
				if(config.searchForm){
					for(var i=0;i<config.searchForm.length;i++){
						var item=config.searchForm[i];
						formItemStr+='<div class="form-item">'
								+'<label class="item-label">'+item.title+'</label>'
								+'<div class="controls">'
									+'<input type="text" class="text input-large"   name="'+item.fieldName+'" value="" />'
									+'<span class="form_checktip"></span>'
								+'</div>'
							+'</div>';
					}
				}
				if(config.extendParams){
					for(var i in config.extendParams){
						formItemStr+='<input type="hidden" name="'+i+'" value="'+config.extendParams[i]+'"/>';
					}
				}
				var str='<div data-options="region:\'west\',split:true" style="width:200px">'
			        	+'<form  method="post"  class="form-horizontal searchForm">'
			        		+formItemStr
							+'<div class="form-item">'
								+'<label class="item-label"><span class="btn searchBtnSubmit">搜索</span></label>'
							+'</div>'
			        	+'</form>'
			        +'</div> '; 

				return str;
			},
			initHTML:function(){
				var config=this;
				var formItemStr="";
				var gw=(config.width-50)/2;
				if(config.searchForm){
					formItemStr=selectUitls.ininForm();
					gw=(config.width-200-50)/2;
				}
				var bh=0;
				var btnStr='';
				if(config.singleSelect){
					btnStr='<div class="searchFormBtn"><span class="right2"></span></div>'
		               +'<div class="searchFormBtn"><span class="left2"></span></div>';
		             bh=(config.height-35-40*2)/2;
				}else{
					btnStr='<div class="searchFormBtn"><span class="up2"></span></div>'
		               +'<div class="searchFormBtn"><span class="right2"></span></div>'
		               +'<div class="searchFormBtn"><span class="rightall"></span></div>'
		              +'<div class="searchFormBtn"><span class="leftall"></span></div>'
		               +'<div class="searchFormBtn"><span class="left2"></span></div>'
		               +'<div class="searchFormBtn"><span class="down2"></span></div>';
		             bh=(config.height-35-40*5)/2;
				}
				var str='<div id="selectUitls-win'+config.id+'">'
						+'<div  class="easyui-layout" data-options="fit:true">'
					        +formItemStr
					        +'<div data-options="region:\'center\'">'   
					           +'<div class="easyui-layout"  data-options="fit:true">'   
							        +'<div data-options="region:\'west\',split:true" style="width:'+gw+'px;">'
							        	+'<div class="leftGrid"></div>'
							        +'</div>' 
							        +'<div data-options="region:\'center\'">'   
							        	+'<div style="text-align: center;vertical-align: middle;margin-top:'+bh+'px;">'
							        	   +btnStr
							            +'</div>'
							        +'</div> '  
							        +'<div data-options="region:\'east\',split:true" style="width:'+gw+'px;">'
							        	+'<div class="rightGrid"></div>'
							        +'</div>' 
							    +'</div>'   
					        +'</div> '
					        +'<div data-options="region:\'south\'" style="height:35px">'
					        	+'<div style="width:140px;margin: 0 auto;border: none;">'
					        		+'<span class="btn selectUtilsSureBtn">确定</span><span class="btn selectUtilsCancelBtn" style="background-color: #969696;">取消</span>'
					        	+'</div>'	
							 +'</div>'
					    +'</div>'
					+'</div> ';
				$("body").append(str);
			},
			initCom:function(){
				var config=this;
				var $content=$("#selectUitls-win"+config.id);
				$content.window({    
				    width:config.width,    
				    height:config.height,
				    title:config.title,
				    maximizable:false,
				    minimizable:false,
				    collapsible:false,
				    resizable:false,
				    modal:true
				});
				$content.find(".easyui-layout").layout();
				$content.find('.leftGrid').datagrid({
					fit:true,
					singleSelect:true,
					//data:[{fdCode:'1',fdName:'1'},{fdCode:'2',fdName:'2'},{fdCode:'3',fdName:'3'}],
				    columns:config.columns,
				    onDblClickRow:function(rowIndex, rowData){
				    	if(commonSelectUitls.singleSelect){
				    		var data=$content.find('.rightGrid').datagrid('getData');
				    		$content.find('.leftGrid').datagrid('deleteRow',rowIndex);
				    		$content.find('.rightGrid').datagrid('appendRow',rowData);
				    		if(data&&data.total>1){
				    			$content.find('.leftGrid').datagrid('appendRow',data.rows[0]);
				    			$content.find('.rightGrid').datagrid('deleteRow',0);
				    		}
				    	}else{
					    	$content.find('.rightGrid').datagrid('appendRow',rowData);
					    	$content.find('.leftGrid').datagrid('deleteRow',rowIndex);
				    	}
				    }
				});  
			
				$content.find('.rightGrid').datagrid({
					fit:true,
					singleSelect:true,
					//data:[{fdCode:'4',fdName:'4'},{fdCode:'5',fdName:'5'},{fdCode:'6',fdName:'6'}],
				    columns:config.columns,
				    onDblClickRow:function(rowIndex, rowData){
				    	$content.find('.leftGrid').datagrid('appendRow',rowData);
				    	$content.find('.rightGrid').datagrid('deleteRow',rowIndex);
				    }
				});  
				
				$content.find(".rightall").on("click",function(){
					for(var i=0;i<$content.find('.leftGrid').datagrid('getData').rows.length;i++){
						$content.find('.rightGrid').datagrid('appendRow',$content.find('.leftGrid').datagrid('getData').rows[i]);
					}
					for(var i=$content.find('.leftGrid').datagrid('getData').rows.length-1;i>-1;i--){
						$content.find('.leftGrid').datagrid('deleteRow',i);
					}
				});
				
				$content.find(".leftall").on("click",function(){
					for(var i=0;i<$content.find('.rightGrid').datagrid('getData').rows.length;i++){
						$content.find('.leftGrid').datagrid('appendRow',$content.find('.rightGrid').datagrid('getData').rows[i]);
					}
					for(var i=$content.find('.rightGrid').datagrid('getData').rows.length-1;i>-1;i--){
						$content.find('.rightGrid').datagrid('deleteRow',i);
					}
				});
				
				$content.find(".right2").on("click",function(){
					var selected=$content.find('.leftGrid').datagrid('getSelected');
					if(selected){
						var index=$content.find('.leftGrid').datagrid('getRowIndex',selected);
						if(commonSelectUitls.singleSelect){
				    		var data=$content.find('.rightGrid').datagrid('getData');
				    		$content.find('.leftGrid').datagrid('deleteRow',index);
				    		$content.find('.rightGrid').datagrid('appendRow',selected);
				    		if(data&&data.total>1){
				    			$content.find('.leftGrid').datagrid('appendRow',data.rows[0]);
				    			$content.find('.rightGrid').datagrid('deleteRow',0);
				    		}
				    	}else{
							$content.find('.rightGrid').datagrid('appendRow',selected);
							$content.find('.leftGrid').datagrid('deleteRow',index);
				    	}
					}
				}); 
				
				$content.find(".left2").on("click",function(){
					var selected=$content.find('.rightGrid').datagrid('getSelected');
					if(selected){
						$content.find('.leftGrid').datagrid('appendRow',selected);
						var index=$content.find('.rightGrid').datagrid('getRowIndex',selected);
						$content.find('.rightGrid').datagrid('deleteRow',index);
					}
				}); 
				
				$content.find(".up2").on("click",function(){
					var selected=$content.find('.rightGrid').datagrid('getSelected');
					if(selected){
						var index=$content.find('.rightGrid').datagrid('getRowIndex',selected);
						if(index>0){
							$content.find('.rightGrid').datagrid('deleteRow',index);
							$content.find('.rightGrid').datagrid('insertRow',{
								index:index-1,
								row:selected
							});
							$content.find('.rightGrid').datagrid('selectRow',index-1);
						}
					}
				}); 
				
				$content.find(".down2").on("click",function(){
					var selected=$content.find('.rightGrid').datagrid('getSelected');
					if(selected){
						var index=$content.find('.rightGrid').datagrid('getRowIndex',selected);
						if(index<$content.find('.rightGrid').datagrid('getData').rows.length-1){
							$content.find('.rightGrid').datagrid('deleteRow',index);
							$content.find('.rightGrid').datagrid('insertRow',{
								index:index+1,
								row:selected
							});
							$content.find('.rightGrid').datagrid('selectRow',index+1);
						}
					}
				});
				
				$content.find(".selectUtilsCancelBtn").on("click",function(){
					$content.window('close');
					$content.find(".searchForm")[0].reset();
				});
			}
		};
		selectUitls.init(configs);
		return selectUitls;
	}
};


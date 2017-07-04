//$('#example').dataTable().fnGetData()获得所有数据
//$('#example').dataTable().fnGetData(row)获得某行的数据
//$('#example').dataTable().fnGetData(row,colum)获得某个单元格的值
//$('#example').dataTable().fnPageChange(0); 跳到首页
//oTable.fnSetColumnVis( 0, false);//隐藏列  
 //刷新表中的数据(最后一个参数是否重绘表格，false你浏览到第二页不会刷跑到第一页，但数据不会改变)
//$('#example').dataTable().fnUpdate( 'a' , 1 , 1 ,false); //coll 
//$('#example').dataTable().fnUpdate( ['a','b'] , 1 ); //row
$.fn.extend({
	sysTable:function(_ops){
		var ops = $.extend(true, {
			bAutoWidth : true,
			bFilter : false,
			aLengthMenu:[50, 100, 200], //更改显示记录数选项  
            iDisplayLength:100, //默认显示的记录数  
			bSort : false,
			bServerSide : true,
			extraPostData:{},
			sPaginationType: "full_numbers", //翻页界面类型
			bProcessing: true, 
			bLengthChange : true, //开关，是否显示每页大小的下拉框
			oLanguage : {
				"sUrl" : hxltUrl+"js/jquery/i18n/table_zh.txt"
			},
			bInfo : true
		}, _ops);
		
		var self = this;
		//btn-search
		self.removeClass().addClass("table table-striped table-bordered").attr("width","100%");
		var table=self.dataTable($.extend(true, ops, {
			"fnServerData": function(sSource, aoData, fnCallback) {
				var aoDataMap = {};
				$.each(aoData, function(index, obj) {
					aoDataMap[obj.name] = obj.value;
				});
				var params={};
				if(_ops.params){
					params=_ops.params;
				}
				if(_ops.formId){
					var json=commUtils.getFormVals(_ops.formId);
					$.extend(true, params,json);
				}
				params["start"]=aoDataMap.iDisplayStart;
				params["limit"]=aoDataMap.iDisplayLength;
				params["timing"]=new Date();
				$("#top-alert").attr("class","fixed alert alert-info");
				$("#top-alert .alert-content").html("正在加载中.....");
				$("#top-alert").show();
				$.ajax({
			        type:'POST',
			        url:_ops.url,
			        data:params,
			        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
			        dataType: "json",
			        success: function(data){
			        	var aoData ={
							iDisplayStart : aoDataMap.iDisplayStart+data.list.length,
							iDisplayLength : aoDataMap.iDisplayLength,
							aaData : data.list,
							iTotalDisplayRecords : data.totalSize,
							iTotalRecords : data.totalSize
						};
						$("#top-alert").hide();
						fnCallback(aoData);
			        }
				});
			}
		}));
		table.parent().addClass("response-table");
		self.find(".check-all").on("change",function(){
			var cb=self.find(".check-single");
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
		self.find(".check-single").live("change",function(){
			var cb=self.find(".check-single:checked");
			if(cb.length==0){
				self.find(".check-all")[0].checked=false;
			}
			if(cb.length==table.fnGetData().length){
				self.find(".check-all")[0].checked=true;
			}
		});
		$.fn.extend({
			getSelectedIndex:function(){
				var ids=[];
				this.find(".check-single:checked").each(function(){
					ids.push($(this).val());
				});
				return ids;
			}
		});
		if(_ops.formId){
			$(_ops.formId).find(".btn-search").on("click",function(){
				table.fnPageChange(0);
			});
			
			$(_ops.formId).find(".searchbtn").on("click",function(){
				table.fnPageChange(0);
			});
			
			$(_ops.formId).find(".clearbtn").on("click",function(){
				 $(_ops.formId).find("form").each(function(){
				 	this.reset();
				 });
			});
		}
		return table;
	}
});
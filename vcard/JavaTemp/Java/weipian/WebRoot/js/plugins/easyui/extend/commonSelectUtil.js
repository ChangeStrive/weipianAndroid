/**
 * 选择当前分公司下商家用户
 * @param {} config
 */
var  selectSellerUtil=function(config){
	var p={
		singleSelect:false
	};
	$.extend(true,p, config);
	commonSelectUitls.init({
		id:'selectSeller',
		title:'商家选择',
		url:hxltUrl+"SelectUtilsAction/selectSeller",
		width:700,
		height:400,
		singleSelect:p.singleSelect,
		valueName:p.valueName,
		textName:p.textName,
		searchForm:[
			 {title:'商家名称',fieldName:'fdName'}
			,{title:'联系电话',fieldName:'fdTel'}
		],
		extendParams:p.extendParams,
		columns:[[    
	        {field:'fdName',title:'商家名称',width:100},    
	        {field:'fdTel',title:'联系电话',width:100}    
	    ]],
	    completeFn:function(data){
	    	var ids=[];
	    	var texts=[];
	    	if(data&&data.rows.length>0){
		    	for(var i=0;i<data.rows.length;i++){
		    		ids.push(data.rows[i].fdId);
		    		texts.push(data.rows[i].fdName);
		    	}
	    	}
	    	if(p.valueName){
				$('input[name="'+p.valueName+'"]').val(ids.join());
				$('textarea[name="'+p.valueName+'"]').val(ids.join());
			}
			if(p.textName){
				$('input[name="'+p.textName+'"]').val(texts.join());
				$('textarea[name="'+p.textName+'"]').val(texts.join());
			}
	    	if(p.completeFn){
		    	p.completeFn(data);
		    }
	    }
	});
}

/**
 * 选择当前分公司下众筹股东
 * @param {} config
 */
var  selectSalesmanUtil=function(config){
	var p={
		singleSelect:false
	};
	$.extend(true,p, config);
	commonSelectUitls.init({
		id:'selectSalesman',
		title:'推荐人选择',
		url:hxltUrl+"SelectUtilsAction/selectSalesman",
		width:900,
		height:400,
		singleSelect:p.singleSelect,
		valueName:p.valueName,
		textName:p.textName,
		searchForm:[
			 {title:'推荐码',fieldName:'fdReferralCode'}
			,{title:'姓名',fieldName:'fdName'}
			,{title:'联系电话',fieldName:'fdTel'}
		],
		extendParams:p.extendParams,
		columns:[[    
			{field:'fdReferralCode',title:'推荐码',width:100},    
	        {field:'fdName',title:'姓名',width:100},    
	        {field:'fdTel',title:'联系电话',width:100}    
	    ]],
	    completeFn:function(data){
	    	var ids=[];
	    	var texts=[];
	    	if(data&&data.rows.length>0){
		    	for(var i=0;i<data.rows.length;i++){
		    		ids.push(data.rows[i].fdId);
		    		texts.push(data.rows[i].fdName);
		    	}
	    	}
	    	if(p.valueName){
				$('input[name="'+p.valueName+'"]').val(ids.join());
				$('textarea[name="'+p.valueName+'"]').val(ids.join());
			}
			if(p.textName){
				$('input[name="'+p.textName+'"]').val(texts.join());
				$('textarea[name="'+p.textName+'"]').val(texts.join());
			}
	    	if(p.completeFn){
		    	p.completeFn(data);
		    }
	    }
	});
}


var  selectZvCardUtil=function(config){
	var p={
		singleSelect:false
	};
	$.extend(true,p, config);
	commonSelectUitls.init({
		id:'selectZvCard',
		title:'会员卡选择',
		url:hxltUrl+"SelectUtilsAction/selectNoZvCard",
		width:1000,
		height:400,
		singleSelect:p.singleSelect,
		valueName:p.valueName,
		textName:p.textName,
		searchForm:[
			 {title:'卡号',fieldName:'fdNo'}
			,{title:'推荐码',fieldName:'fdReferenceNo'}
			,{title:'推荐人',fieldName:'fdSalesmanName'}
		],
		extendParams:p.extendParams,
		columns:[[    
			{field:'fdNo',title:'卡号',width:150},    
	        {field:'fdReferenceNo',title:'推荐码',width:80},    
	        {field:'fdSalesmanName',title:'推荐人',width:100}    
	    ]],
	    completeFn:function(data){
	    	var ids=[];
	    	var texts=[];
	    	if(data&&data.rows.length>0){
		    	for(var i=0;i<data.rows.length;i++){
		    		ids.push(data.rows[i].fdId);
		    		texts.push(data.rows[i].fdNo);
		    	}
	    	}
	    	if(p.valueName){
				$('input[name="'+p.valueName+'"]').val(ids.join());
				$('textarea[name="'+p.valueName+'"]').val(ids.join());
			}
			if(p.textName){
				$('input[name="'+p.textName+'"]').val(texts.join());
				$('textarea[name="'+p.textName+'"]').val(texts.join());
			}
	    	if(p.completeFn){
		    	p.completeFn(data);
		    }
	    }
	});
}

var  selectZvCardUtil2=function(config){
	var p={
		singleSelect:false
	};
	$.extend(true,p, config);
	commonSelectUitls.init({
		id:'selectZvCard',
		title:'会员卡选择',
		url:hxltUrl+"SelectUtilsAction/selectNoZvCard",
		width:1000,
		height:400,
		singleSelect:p.singleSelect,
		valueName:p.valueName,
		textName:p.textName,
		searchForm:[
			 {title:'卡号',fieldName:'fdNo'}
		],
		extendParams:p.extendParams,
		columns:[[    
			{field:'fdNo',title:'卡号',width:150},    
	        {field:'fdReferenceNo',title:'推荐码',width:80},    
	        {field:'fdSalesmanName',title:'推荐人',width:100}    
	    ]],
	    completeFn:function(data){
	    	var ids=[];
	    	var texts=[];
	    	if(data&&data.rows.length>0){
		    	for(var i=0;i<data.rows.length;i++){
		    		ids.push(data.rows[i].fdId);
		    		texts.push(data.rows[i].fdNo);
		    	}
	    	}
	    	if(p.valueName){
				$('input[name="'+p.valueName+'"]').val(ids.join());
				$('textarea[name="'+p.valueName+'"]').val(ids.join());
			}
			if(p.textName){
				$('input[name="'+p.textName+'"]').val(texts.join());
				$('textarea[name="'+p.textName+'"]').val(texts.join());
			}
	    	if(p.completeFn){
		    	p.completeFn(data);
		    }
	    }
	});
}

var  selectCompanyUtil=function(config){
	var p={
		singleSelect:false
	};
	$.extend(true,p, config);
	commonSelectUitls.init({
		id:'selectCompany',
		title:'分公司选择',
		url:hxltUrl+"SelectUtilsAction/selectCompany",
		width:1000,
		height:400,
		singleSelect:p.singleSelect,
		valueName:p.valueName,
		textName:p.textName,
		searchForm:[
			 {title:'名称',fieldName:'fdName'}
			,{title:'区域',fieldName:'fdAreaName'}
		],
		extendParams:p.extendParams,
		columns:[[    
	        {field:'fdName',title:'名称',width:130},    
	        {field:'fdAreaName',title:'区域',width:100}    
	    ]],
	    completeFn:function(data){
	    	var ids=[];
	    	var texts=[];
	    	if(data&&data.rows.length>0){
		    	for(var i=0;i<data.rows.length;i++){
		    		ids.push(data.rows[i].fdId);
		    		texts.push(data.rows[i].fdName);
		    	}
	    	}
	    	if(p.valueName){
				$('input[name="'+p.valueName+'"]').val(ids.join());
				$('textarea[name="'+p.valueName+'"]').val(ids.join());
			}
			if(p.textName){
				$('input[name="'+p.textName+'"]').val(texts.join());
				$('textarea[name="'+p.textName+'"]').val(texts.join());
			}
	    	if(p.completeFn){
		    	p.completeFn(data);
		    }
	    }
	});
}
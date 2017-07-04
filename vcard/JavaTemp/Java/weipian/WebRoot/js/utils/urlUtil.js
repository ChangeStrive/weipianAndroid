var urlUitl={
	getParams:function(url){
		var result={};
		 if(url.indexOf("?")!=-1){
			 var r =url.split("?")[1].split("&");
			 for(var i=0;i<r.length;i++){
			 	var o=r[i].split("=");
			 	result[o[0]]=decodeURIComponent(o[1]);
			 }
		 }
		 return result;
	},
	formatParams:function(params){
		var str="";
		var i=0;
		for(var a in params){
			if(i!=0){
				str+="&";
			}
			str+=a+"="+params[a];
			i++;
		}
		return str;
	},
	getUrl:function(url,params){
		var params2=urlUitl.getParams(url);
		$.extend(true, params2,params);
		if(url.indexOf("?")==-1){
			url=url+"?"+urlUitl.formatParams(params2);
		}else{
			url=url.split("?")[0]+"?"+urlUitl.formatParams(params2);
		}
		return url;
	},
	getParamsLocation:function(){
		 var result={};
		 var r = window.location.search.substr(1).split("&");
		 for(var i=0;i<r.length;i++){
		 	var o=r[i].split("=");
		 	result[o[0]]=decodeURIComponent(o[1]);
		 }
		 return result;
	}
};

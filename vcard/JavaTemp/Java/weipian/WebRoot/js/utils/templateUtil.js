String.prototype.replaceAll  = function(s1,s2){   
	return this.replace(new RegExp(s1,"gm"),s2);   
} 

var TemplateUtils={
	getTemplate:function(template,data){
		if($(template).length==0){
			return "";
		}
		var templateHtml=$(template).val();
		if(data){
			for(var key in data){
				templateHtml=templateHtml.replaceAll("{"+key+"}",data[key]);
			}
		}
		return templateHtml;
	}
};
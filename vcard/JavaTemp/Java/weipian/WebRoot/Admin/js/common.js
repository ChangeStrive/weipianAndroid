String.prototype.replaceAll  = function(s1,s2){   
	return this.replace(new RegExp(s1,"gm"),s2);   
} 

var SysDictTreeConfig={
	appendHtml:function(select,item,index){
		var str="";
		for(var i=0;i<index;i++){
			str+="&nbsp;&nbsp;";
			if(i==index-1){
				str+="└";
			}
		}
		var str='<option text="'+item.text+'" value="'+item.value+'">'+str+item.text+'</option>';
		select.append(str);
		if(item.childs){
			for(var i=0;i<item.childs.length;i++){
				var subItem=item.childs[i];
				SysDictTreeConfig.appendHtml(select,subItem,index+1);
			}
		}
	},
	load:function(select){
		var value=select.attr("data-dictTreeValue");
		var fdId=select.attr("data-dictTreekey");
		var text=select.attr("data-dictTreeText");
		var params={};
		params["fdId"]=fdId;
		$.ajax({
	        type:'POST',
	        url:hxltUrl+"SysDictAction/getDictTree",
	        data:params,
	        contentType:'application/x-www-form-urlencoded;charset=UTF-8',
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		if(data.list){
	        			SysDictTreeConfig.appendHtml(select,{text:"请选择----",value:""},0);
	        			for(var i=0;i<data.list.length;i++){
	        				var item=data.list[i];
	        				SysDictTreeConfig.appendHtml(select,item,0);
	        			}
	        			if(value){
	        				var selectItem=select.find("option[value='"+value+"']");
	        				selectItem.attr("selected","selected");
	        				$(text).val(selectItem.attr("text"));
	        			}else{
	        				var selectItem=select.find("option:eq(0)");
	        				if(selectItem.length>0){
	        					selectItem.attr("selected","selected");
	        					$(text).val(selectItem.attr("text"));
	        				}
	        			}
	        			select.on("change",function(){
	        				$(text).val(select.find("option:selected").attr("text"));
	        			});
	        		}
	        	}
	        }
		});
	},
	init:function(){
		$("select[data-dictTreekey]").each(function(){
			var $select=$(this);
			SysDictTreeConfig.load($select);
		});
	}
};
var commUtils = {
	getFormVals : function(seltor) {
		var json = {}, p = null;
		if (seltor) {
			p = $(seltor);
		} else {
			p = $("body");
		}
		p.find("input[type=text]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=hidden]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=password]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=radio]:checked").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("textarea").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("select").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=checkbox]:checked").each(function(idx, item) {
			var obj = $(this);
			var name = obj.attr("name");
			if (!json[name]) {
				json[name] = obj.val();
			}else{
				json[name] +=","+ obj.val();
			}
		});
		return json;
	}
};
$.fn.extend({
	formSubmit:function(options){
		var self=this;
		self.Validform({
			tiptype:function(msg,o,cssctl) {
				if(options.tipFn&&o.type==3){
					options.tipFn(msg);
				}
				var valself = this;
				//msg：提示信息;
				//o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
				//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
				if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
					var objtip=o.obj.siblings(options.tip);
					cssctl(objtip,o.type);
					objtip.text(msg);
					objtip.show();
				}else{
					alert(o.type);
					var objtip=o.obj.find(options.tip);
					cssctl(objtip,o.type);
					objtip.text(msg);
				}
			},
			beforeCheck:function(curform){
				if(options.beforeCheck){
					options.beforeCheck();
				}
			},
			showAllError:true,
			ajaxPost:true,
			beforeSubmit:function(curform){
				if(options.beforeSubmit){
					options.beforeSubmit();
				}
				var isSubmit=true;
				if(options.isSubmit==false){
					isSubmit=false;
				}
				if(!isSubmit){
					return false;
				}
				var params=curform.serialize();
				$.extend(true, params,options.data);
				$.ajax({
					url:options.url,
					data:params,
					type:"POST",
					dataType: "json",
					contentType:'application/x-www-form-urlencoded;charset=UTF-8',
					success:function(data) {
						if(options.success) {
							options.success(data);
							self.find("input[type=password]").val("");
						}
					}
				});
				return false;
			}
		});
	}
});

//dom加载完成后执行的js
$(function(){
	/**顶部警告栏*/
	var content = $('#main');
	var top_alert = $('#top-alert');
	top_alert.find('.close').on('click', function () {
		top_alert.removeClass('block').slideUp(200);
	});
	
	window.updateAlert = function (text,c) {
		text = text||'default';
		c = c||false;
		if ( text!='default' ) {
	        top_alert.find('.alert-content').text(text);
			if (top_alert.hasClass('block')) {
			} else {
				top_alert.addClass('block').slideDown(200);
			}
		} else {
			if (top_alert.hasClass('block')) {
				top_alert.removeClass('block').slideUp(200);
			}
		}
		if ( c!=false ) {
	        top_alert.removeClass('alert-error alert-warn alert-info alert-success').addClass(c);
		}
		setTimeout(function(){
			top_alert.removeClass('block').slideUp(200);
		},3000);
	};
});
   

//标签页切换(无下一步)
function showTab() {
    $(".tab-nav li").click(function(){
        var self = $(this), target = self.data("tab");
        self.addClass("current").siblings(".current").removeClass("current");
        window.location.hash = "#" + target.substr(3);
        $(".tab-pane.in").removeClass("in");
        $("." + target).addClass("in");
    }).filter("[data-tab=tab" + window.location.hash.substr(1) + "]").click();
}

//标签页切换(有下一步)
function nextTab() {
     $(".tab-nav li").click(function(){
        var self = $(this), target = self.data("tab");
        self.addClass("current").siblings(".current").removeClass("current");
        window.location.hash = "#" + target.substr(3);
        $(".tab-pane.in").removeClass("in");
        $("." + target).addClass("in");
        showBtn();
    }).filter("[data-tab=tab" + window.location.hash.substr(1) + "]").click();

    $("#submit-next").click(function(){
        $(".tab-nav li.current").next().click();
        showBtn();
    });
}

// 下一步按钮切换
function showBtn() {
    var lastTabItem = $(".tab-nav li:last");
    if( lastTabItem.hasClass("current") ) {
        $("#submit").removeClass("hidden");
        $("#submit-next").addClass("hidden");
    } else {
        $("#submit").addClass("hidden");
        $("#submit-next").removeClass("hidden");
    }
}


$(function(){
  var $window = $(window), $subnav = $("#subnav"), url;
            $window.resize(function(){
                $("#main").css("min-height", $window.height() - 130);
                $("#leftTree").css("min-height", $window.height() - 90);
            }).resize();

            /* 左边菜单高亮 */
            url = window.location.pathname + window.location.search;
            url = url.replace(/(\/(p)\/\d+)|(&p=\d+)|(\/(id)\/\d+)|(&id=\d+)|(\/(group)\/\d+)|(&group=\d+)/, "");
            $subnav.find("a[href='" + url + "']").parent().addClass("current");

            /* 左边菜单显示收起 */
            $("#subnav").on("click", "h3", function(){
                var $this = $(this);
                $this.find(".icon").toggleClass("icon-fold");
                $this.next("ul").slideToggle("fast").siblings(".side-sub-menu:visible").
                      prev("h3").find("i").addClass("icon-fold").end().end().hide();
            });

            $("#subnav h3 a").click(function(e){e.stopPropagation()});

            /* 头部管理员菜单 */
            $(".user-bar").mouseenter(function(){
                var userMenu = $(this).children(".user-menu ");
                userMenu.removeClass("hidden");
                clearTimeout(userMenu.data("timeout"));
            }).mouseleave(function(){
                var userMenu = $(this).children(".user-menu");
                userMenu.data("timeout") && clearTimeout(userMenu.data("timeout"));
                userMenu.data("timeout", setTimeout(function(){userMenu.addClass("hidden")}, 100));
            });
			
            $(".user-name-bar").mouseenter(function(){
                var userMenu = $(".user-bar").children(".user-menu ");
                userMenu.removeClass("hidden");
                clearTimeout(userMenu.data("timeout"));
            }).mouseleave(function(){
                var userMenu =  $(".user-bar").children(".user-menu");
                userMenu.data("timeout") && clearTimeout(userMenu.data("timeout"));
                userMenu.data("timeout", setTimeout(function(){userMenu.addClass("hidden")}, 100));
            });
            
	        /* 表单获取焦点变色 */
	        $("form").on("focus", "input", function(){
		        $(this).addClass('focus');
	        }).on("blur","input",function(){
				        $(this).removeClass('focus');
			        });
		    $("form").on("focus", "textarea", function(){
			    $(this).closest('label').addClass('focus');
		    }).on("blur","textarea",function(){
			    $(this).closest('label').removeClass('focus');
		    });

            // 导航栏超出窗口高度后的模拟滚动条
            var sHeight = $(".sidebar").height();
            var subHeight  = $(".subnav").height();
            var diff = subHeight - sHeight; //250
            var sub = $(".subnav");
            if(diff > 0){
                $(window).mousewheel(function(event, delta){
                    if(delta>0){
                        if(parseInt(sub.css('marginTop'))>-10){
                            sub.css('marginTop','0px');
                        }else{
                            sub.css('marginTop','+='+10);
                        }
                    }else{
                        if(parseInt(sub.css('marginTop'))<'-'+(diff-10)){
                            sub.css('marginTop','-'+(diff-10));
                        }else{
                            sub.css('marginTop','-='+10);
                        }
                    }
                });
            }
     $("select[data-dictkey]").each(function(){
     		var value=$(this).attr("data-dictValue");
     		var fdId=$(this).attr("data-dictKey");
     		var text=$(this).attr("data-dictText");
     		var $select=$(this);
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
     }); 
     SysDictTreeConfig.init();
});
          

var mycardConfig={
	init:function(){
		//购买下拉初始化
		mycardConfig.initData();
		mycardConfig.moveCard();
		mycardConfig.upCard();
		mycardConfig.menuchagne();
		$(".mf_close_btn").on("click",function(){
			$(".mc_fxs").addClass("hide");
		});
	},
	getValue:function(key){
		var str="";
		if(mycardConfig.userDetail[key]&&mycardConfig.userDetail[key].length>0){
			return mycardConfig.userDetail[key][0];
		}
		return str;
	},
	initData:function(){
		if(mycardConfig.userDetail){
			$(".fdJobs").html(mycardConfig.getValue("fdJobs"));
			$(".fdCompany").html(mycardConfig.getValue("fdCompany"));
			$(".fdMobile").html(mycardConfig.getValue("fdMobile"));
			$(".fdTel").html(mycardConfig.getValue("fdTel"));
			$(".fdCompanyUrl").html(mycardConfig.getValue("fdCompanyUrl"));
			$(".fdEmail").html(mycardConfig.getValue("fdEmail"));
			$(".fdCompanyAddress").html(mycardConfig.getValue("fdCompanyAddress"));
		}
	},
	menuchagne:function(){
		$(".mcm_ahref").on("click",function(){
			if($(this).hasClass("mcm_menu")){
				$(this).removeClass("mcm_menu");
				$(".mcm_hide").hide();
				$(".mcm_ahref span").html("&#xe608;");
			}else{
				$(this).addClass("mcm_menu");
				$(".mcm_hide").show();
				$(".mcm_ahref span").html("&#xe633;");
			}
		});
	},
	upCard:function(){//上拉
		function toUp(){
			 if(mycardConfig.viewcard){
				  $(".mycardMain").css("top",0);
				  scroll(0,0);
				  mycardConfig.viewcard=false;
				  $(".footerShare").show();
				  if(!$(".mc_fxs").hasClass("hide")){
					  $(".mc_fxs").show();
				  }
				  $(".mycardUpDiv").addClass("hide");
			  }
		}
		$(".mycardUpBtn").on("click",function(){
			toUp();
		});
		$(window).scroll(function() {
			toUp();
		});
	},
	moveCard:function(){//下拉
		var isStart=false;
		var y=0;
		 $.touch.bind($('.mc_head'), {
	        start: function(params) {
	            isStart = true;
	            y=params.y;
	        },
	        move: function(params) {
	            if(isStart) {
	              var my=params.y-y;
	              if(my>0){
	            	  scroll(0,0);
	            	  $(".mycardMain").css("top",my+30);
	            	  $(".footerShare").hide();
	            	  $(".mc_fxs").hide();
	              }
	            }
	           
	        },
	        end: function(params) {
	            isStart = false;
	            var my=params.y-y;
	            var top=$(".mycardImage2").position().top/2;
	            if(my>top){
	            	 var t=$(window).height()-40;
	            	 $(".mycardMain").css("top",t);
	            	 mycardConfig.viewcard=true;
	            	 $(".footerShare").hide();
	            	 $(".mc_fxs").hide();
	            	 $(".mycardUpDiv").removeClass("hide");
	            }else{
	            	$(".mycardMain").css("top",0);
	            	 $(".footerShare").show();
	            	 if(!$(".mc_fxs").hasClass("hide")){
						  $(".mc_fxs").show();
					  }
	            }
	            y=0;
	        },
	        preventDefault: true,
	        stopPropagation: true
	    });
	}
}

$(function(){
	//$(".mask").show();
 	//$(".mc_no_nfc").show();
	mycardConfig.userDetail=$.parseJSON(fdUserMessage);
	mycardConfig.init();
	 $(".jrdhc").on("click",function(){
		$(".mask").show();
		$(".mc_no_nfc").show();
	 	
	 });
	 $(".ljdh").on("click",function(){
		 $(".mask").show();
		 $(".mc_no_nfc").show();
		 
	 });
	
});
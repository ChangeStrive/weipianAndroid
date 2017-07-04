var myCropUtils={
	uploadInit:function($img,aspectRatio,success){
		myCropUtils.$img=$img;
		myCropUtils.success=success;
		myCropUtils.cropper.setAspectRatio(aspectRatio);
		document.getElementById('inputImage').click();
	},
	convertImgToBase64(url, callback, outputFormat){
		  var canvas = document.createElement('CANVAS'),
		    ctx = canvas.getContext('2d'),
		    img = new Image;
		  img.crossOrigin = 'Anonymous';
		  img.onload = function(){
		    canvas.height = img.height;
		    canvas.width = img.width;
		    ctx.drawImage(img,0,0);
		    var dataURL = canvas.toDataURL(outputFormat || 'image/png');
		    callback.call(this, dataURL);
		    canvas = null; 
		  };
		  img.src = url;
	},
	initHtml:function(){
		 var str='<div class="container" style="padding: 0;margin: 0;position:fixed;display: none;top: 0;left: 0;z-index: 1000;" id="containerDiv">'
			    +'<div class="row" style="display: none;" id="imgEdit">'
				    +'<div class="col-md-9">'
					    +'<div class="img-container">'
					    	+'<img src="" alt="Picture">'
					    +'</div>'
				    +'</div>'
			    +'</div>'
			    +'<div class="row" id="actions" style="padding: 0;margin: 0;width: 100%;position: fixed;bottom: 5px;">'
				    +'<div class="col-md-9 docs-buttons">'
					    +'<div class="btn-group" >'
						    +'<button type="button" class="btn btn-primary" data-method="destroy" title="Destroy" style="height: auto;">'
							    +'<span class="docs-tooltip" data-toggle="tooltip" >'
							    	+'<span class="fa fa-power-off" >取消</span>'
							    +'</span>'
						    +'</button>'
					    +'</div>'
					    +'<div class="btn-group btn-group-crop " style="float: right;">'
						    +'<button type="button" class="btn btn-primary" id="imgCutConfirm" data-method="getCroppedCanvas" data-option="{ &quot;width&quot;: 320, &quot;height&quot;: 180 }" style="height: auto;margin-right: 17px;">'
						    	+'<span class="docs-tooltip" data-toggle="tooltip" title="">确认</span>'
						    +'</button>'
					    +'</div>'
				    +'</div>'
			    +'</div>'
		    +'</div>';
		 $("body").append(str);
		 str='<input  id="inputImage"  type="file" accept="image/*" style="display: none;"/>';
		 $("body").append(str);
	},
	init:function(){
	  myCropUtils.initHtml();
	  var screenWidth = $(window).width();
	  var screenHeight =  $(window).height();
	   
	  var Cropper = window.Cropper;
	  var console = window.console || { log: function () {} };
	  var container = document.querySelector('.img-container');
	  var image = container.getElementsByTagName('img').item(0);
	  var actions = document.getElementById('actions');
	  var isUndefined = function (obj) {
	    return typeof obj === 'undefined';
	  };
	  
	  var options = {
			minContainerHeight :  screenHeight,
			minContainerWidth : screenWidth,
	        aspectRatio: 1 / 1,//裁剪框比例 1：1
	        viewMode : 1,//显示
	        guides :false,//裁剪框虚线 默认true有
	        dragMode : "move",
	        build: function (e) { //加载开始
	        	//可以放你的过渡 效果
	        },
	        built: function (e) { //加载完成
	        	$("#containerDiv").show();
	            $("#imgEdit").show();
	        },
	        zoom: function (e) {
	          console.log(e.type, e.detail.ratio);
	        },
	        background : true,// 容器是否显示网格背景
			movable : true,//是否能移动图片
			cropBoxMovable :false,//是否允许拖动裁剪框
			cropBoxResizable :false,//是否允许拖动 改变裁剪框大小
	      };
	  var cropper = new Cropper(image, options);
	  myCropUtils.cropper=cropper;
	  

	  if (typeof document.createElement('cropper').style.transition === 'undefined') {
	    $('button[data-method="rotate"]').prop('disabled', true);
	    $('button[data-method="scale"]').prop('disabled', true);
	  }

	  // Methods
	  actions.querySelector('.docs-buttons').onclick = function (event) {
	    var e = event || window.event;
	    var target = e.target || e.srcElement;
	    var result;
	    var input;
	    var data;

	    if (!cropper) {
	      return;
	    }

	    while (target !== this) {
	      if (target.getAttribute('data-method')) {
	        break;
	      }

	      target = target.parentNode;
	    }

	    if (target === this || target.disabled || target.className.indexOf('disabled') > -1) {
	      return;
	    }

	    data = {
	      method: target.getAttribute('data-method'),
	      target: target.getAttribute('data-target'),
	      option: target.getAttribute('data-option'),
	      secondOption: target.getAttribute('data-second-option')
	    };

	    if (data.method) {
	      if (typeof data.target !== 'undefined') {
	        input = document.querySelector(data.target);

	        if (!target.hasAttribute('data-option') && data.target && input) {
	          try {
	            data.option = JSON.parse(input.value);
	          } catch (e) {
	            console.log(e.message);
	          }
	        }
	      }

	      if (data.method === 'getCroppedCanvas') {
	        data.option = JSON.parse(data.option);
	      }

	      result = cropper[data.method](data.option, data.secondOption);

	      switch (data.method) {
	        case 'scaleX':
	        case 'scaleY':
	          target.setAttribute('data-option', -data.option);
	          break;

	        case 'getCroppedCanvas':
	          if (result) {
	        	  
	            var fileImg = result.toDataURL(fileType);
	            myCropUtils.$img.attr("src",fileImg).show();
	            myCropUtils.$img.attr("hasUpload","1");
	            if(myCropUtils.success){
	            	myCropUtils.success(fileImg,myCropUtils.oldFileImg);
	            }
	            $("#inputImage").val("");
	        	$("#containerDiv").hide();
	        	$("#imgEdit").hide();
	          }

	          break;

	        case 'destroy':
	        	$("#inputImage").val("");
	        	$("#containerDiv").hide();
	        	$("#imgEdit").hide();
	          break;
	      }

	      if (typeof result === 'object' && result !== cropper && input) {
	        try {
	          input.value = JSON.stringify(result);
	        } catch (e) {
	          console.log(e.message);
	        }
	      }

	    }
	  };
	  
	  var inputImage = document.getElementById('inputImage');
	  var URL = window.URL || window.webkitURL;
	  var blobURL;
	  var fileType="";
	  if (URL) {
	    inputImage.onchange = function () {
	      var files = this.files;
	      var file;

	      if (cropper && files && files.length) {
	        file = files[0];

	        if (/^image\/\w+/.test(file.type)) {
	        	fileType=file.type;
	           var reader = new FileReader();
	           reader.readAsDataURL(file);
	           reader.onload = function (e) { myCropUtils.oldFileImg = this.result;}
	              
	          blobURL = URL.createObjectURL(file);
	          cropper.reset().replace(blobURL);
	        } else {
	          window.alert('Please choose an image file.');
	        }
	      }
	      $(inputImage).find("img").hide();
	    };
	  } else {
	    inputImage.disabled = true;
	    inputImage.parentNode.className += ' disabled';
	  }
	  
	}	
}
$(function(){
	myCropUtils.init();
});
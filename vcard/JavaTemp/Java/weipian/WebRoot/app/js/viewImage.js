var viewImageUtil={
	show:function(images,index){
		if(viewImageUtil.fotorama){
			viewImageUtil.fotorama.destroy();
		}
		$(".viewBoxImageList").html("");
		var h=$(window).height()-$(".homepage_head").height();
		var str='<div class="fotorama"    data-allowfullscreen="true" data-maxwidth="100%"  data-maxheight="'+h+'px" data-auto="false"></div>';
		$(".viewBoxImageList").html(str);
		var imageList="";
		for(var i=0;i<images.length;i++){
			imageList+='<img src="'+images[i]+'"/>';
		}
		$(".viewBoxImageList .fotorama").html(imageList);
		viewImageUtil.$fotoramaDiv=$('.viewBoxImageList .fotorama').on("fotorama:ready fotorama:fullscreenexit",function(e, fotorama, extra){
			if(e.type=="fotorama:fullscreenexit"){
				$(".viewBox").hide();
			}else{
				viewImageUtil.fotorama = viewImageUtil.$fotoramaDiv.data('fotorama');
				viewImageUtil.fotorama.show(index);
				viewImageUtil.fotorama.requestFullScreen();
			}
		}).fotorama({
			maxwidth:"100%"
		});
		
		$(".viewBox").show();
	}
};

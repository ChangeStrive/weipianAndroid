<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta name = "format-detection" content="telephone = no" />
<script type="text/javascript">
	var hxltUrl = '${hxltUrl}';
	var downAction = '${downAction}';
	var hasWxLogin="${hasWxLogin}";
	var fdShopNo="${fdShopNo}";
	var browser = {
	    versions: function () {
	        var u = navigator.userAgent, app = navigator.appVersion;
	        return {         //移动终端浏览器版本信息
	            trident: u.indexOf('Trident') > -1, //IE内核
	            presto: u.indexOf('Presto') > -1, //opera内核
	            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
	            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
	            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
	            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
	            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
	            iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
	            iPad: u.indexOf('iPad') > -1, //是否iPad
	            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
	        };
	    }(),
	    language: (navigator.browserLanguage || navigator.language).toLowerCase()
	};
</script>
<link rel="stylesheet" href="${hxltUrl}js/plugins/fotorama-4.6.4/fotorama.css">
<link rel="stylesheet" href="${hxltUrl}app/css/fonts/iconfont.css">
<link rel="stylesheet" href="${hxltUrl}app/js/dropload/dropload.css">
<link rel="stylesheet" href="${hxltUrl}app/css/wuyi.css">
<script src='${hxltUrl}app/js/jquery-1.11.0.min.js'></script>
<script src='${hxltUrl}app/js/jquery-migrate-1.2.1.min.js'></script>
<script src='${hxltUrl}app/js/dropload/dropload.min.js'></script>
<script src="${hxltUrl}js/utils/templateUtil.js"></script>
<script src='${hxltUrl}js/plugins/fotorama-4.6.4/fotorama.dev.js'></script>
<script src='${hxltUrl}app/js/viewImage.js'></script>
<link rel="stylesheet" href="${hxltUrl}app/css/DateTimePicker.min.css">
<script src="${hxltUrl}app/js/DateTimePicker.js"></script>
<script src="${hxltUrl}app/js/common.js"></script>
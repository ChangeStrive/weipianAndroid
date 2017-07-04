<script type="text/javascript" src="${hxltUrl}Admin/js/core/header/header.js"></script>
<script type="text/javascript">
	if(!hasLogin){
		window.location.href=hxltUrl+"LoginAction/toLogin?backUrl="+encodeURIComponent(window.location.href);
	}
</script>
<div class="header"> 
	<!-- Logo --> 
	<div class="logo">微片生活管理平台</div> 
	<!-- /Logo --> 
	
	<!-- 主导航 -->
	<ul class="main-nav">
		
	</ul>
	<!-- /主导航 --> 
	
	<!-- 用户栏 -->
	<div class="user-name-bar" style="float:right;color:white;text-align:left;padding-right:10px;">${hxltUserName}</div>
	<div class="user-bar"> <a href="javascript:;" class="user-entrance"><i class="icon-user"></i></a>
		<ul class="nav-list user-menu hidden">
			<li class="manager">你好，<em title="admin">${hxltUserName}</em></li>
			<li><a href="${hxltUrl}SysUserAction/toChangePassword">修改密码</a></li>
			<li class="logout_user"><a href="${hxltUrl}LoginAction/logout">退出</a></li>
		</ul>
	</div>
	
</div>
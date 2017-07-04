@author ZuoZiJi-Y.J
	@since 2013.06.22
	
使用示例：

		//示例的ImageView
		ImageView mImvTest = new ImageView(this);
		
		FinalBitmap mFinalBitmap = FinalBitmap.create(this);
		//ImageView 图片载入中展示的图片
		mFinalBitmap.configLoadingImage(int resId);
		//ImageView 图片载入失败展示的图片
		mFinalBitmap.configLoadfailImage(int resId);
		//展示图片
		mFinalBitmap.display(mImvTest, "XXXXX图片地址");
		//添加判断横向或是纵向展示(true,表示要进行判断, false表示不进行判断,与上个方法一样)
		mFinalBitmap.display(mImvTest, "XXXXX图片地址",true);
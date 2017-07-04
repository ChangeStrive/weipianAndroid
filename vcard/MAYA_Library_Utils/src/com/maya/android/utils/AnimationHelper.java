package com.maya.android.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

/**
 * 动画帮助类 <br/>
 * 使用说明: <br/>
 * 1.主要功能是简化view的动画播放,play为最基本的方法,用于调用相就的anim的xml进行动画播放; <br/>
 * 2.调用时可使用自定义的动画监听实现特定功能,若动画显示完成后只是要控制显示隐藏,可调用visibility参数的; <br/>
 * 3.本类的其它方法是对现有的anim xml进行了封闭,只需要调用相应的play打头的方法即可; <br/>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-31
 *
 */
public class AnimationHelper {
	//#region 基本动画播放
	/**
	 * 播放指定id动画
	 * @param view 要播放动画的view
	 * @param animId 动画id
	 * @param durationMillis 动画时长,小等于0则为默认值
	 * @param fillAfter 动画结束后是否将view停留在结束位置,默认为false
	 * @param listener 动画监听,无则为null
	 */
	public static final void play(View view, int animId
			, long durationMillis, boolean fillAfter
			, AnimationListener listener){
		if(Helper.isNotNull(view) && animId > 0){
			Animation anim = AnimationUtils.loadAnimation(ActivityHelper.getGlobalApplicationContext(), animId);
			if(durationMillis > 0){
				anim.setDuration(durationMillis);
			}
			if(fillAfter){
				anim.setFillAfter(fillAfter);
			}
			if(Helper.isNotNull(listener)){
				anim.setAnimationListener(listener);
			}
			view.startAnimation(anim);
		}
	}
	/**
	 * 播放指定id动画,可指定动画结束后的可见性
	 * @param view 待播放动画的view
	 * @param animId 动画id
	 * @param durationMillis 动画时长,小等于0则为默认值
	 * @param fillAfter 动画结束后是否将view停留在结束位置,默认为false
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void play(final View view, int animId
			, long durationMillis, boolean fillAfter
			,final int visibility){
		AnimationListener listener = new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(Helper.isNotNull(view)){
					view.setVisibility(visibility);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
		};
		play(view, animId, durationMillis, fillAfter, listener);
	}
	/**
	 * 播放指定id动画,可指定动画结束后的可见性
	 * @param view 待播放动画的view
	 * @param animId 动画id
	 * @param fillAfter 动画结束后是否将view停留在结束位置,默认为false
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void play(View view, int animId
			, boolean fillAfter, int visibility){
		play(view, animId, 0, fillAfter, visibility);
	}
	/**
	 * 播放指定id动画
	 * @param view 要播放动画的view
	 * @param animId 动画id
	 * @param fillAfter 动画结束后是否将view停留在结束位置,默认为false
	 * @param listener 动画监听,无则为null
	 */
	public static final void play(View view, int animId
			, boolean fillAfter, AnimationListener listener){
		play(view, animId, 0, fillAfter, listener);
	}
	/**
	 * 播放指定id动画(动画结束后view停留在原位置)
	 * @param view 要播放动画的view
	 * @param animId 动画id
	 * @param listener 动画监听,无则为null
	 */
	public static final void play(View view, int animId, AnimationListener listener){
		play(view, animId, false, listener);
	}
	/**
	 * 播放指定id动画,可指定动画结束后的可见性(动画结束后view停留在原位置)
	 * @param view 待播放动画的view
	 * @param animId 动画id
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void play(View view, int animId, int visibility){
		play(view, animId, false, visibility);
	}
	//#endregion 基本动画播放
	
	//#region 缩放动画播放
	/**
	 * 以缩小动画显示指定View进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playScaleZoominIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_scale_zoomin_in, listener);
	}
	/**
	 * 以缩小动画显示指定View进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playScaleZoominIn(View view, int visibility){
		play(view, R.anim.fwex_scale_zoomin_in, visibility);
	}
	/**
	 * 以缩小动画显示指定View离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playScaleZoominOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_scale_zoomin_out, listener);
	}
	/**
	 * 以缩小动画显示指定View离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playScaleZoominOut(View view, int visibility){
		play(view, R.anim.fwex_scale_zoomin_out, visibility);
	}
	/**
	 * 以放大动画显示指定View进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playScaleZoomoutIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_scale_zoomout_in, listener);
	}
	/**
	 * 以放大动画显示指定View进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playScaleZoomoutIn(View view, int visibility){
		play(view, R.anim.fwex_scale_zoomout_in, visibility);
	}
	/**
	 * 以放大动画显示指定View离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playScaleZoomoutOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_scale_zoomout_out, listener);
	}
	/**
	 * 以放大动画显示指定View离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playScaleZoomoutOut(View view, int visibility){
		play(view, R.anim.fwex_scale_zoomout_out, visibility);
	}
	//#endreigon 缩放动画播放
	
	//#region 移动动画播放
		//#region 横向移动动画播放
			//#region 左横向移动动画播放
	/**
	 * 动画显示指定View从左边进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateLeftIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_left_in, listener);
	}
	/**
	 * 动画显示指定View从左边进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateLeftIn(View view, int visibility){
		play(view, R.anim.fwex_translate_left_in, visibility);
	}
	/**
	 * 动画显示指定View从左边离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateLeftOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_left_out, listener);
	}
	/**
	 * 动画显示指定View从左边离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateLeftOut(View view, int visibility){
		play(view, R.anim.fwex_translate_left_out, visibility);
	}
			//#endregion 左横向移动动画播放
			
			//#region 右横向移动动画播放
	/**
	 * 动画显示指定View从右边进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateRightIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_right_in, listener);
	}
	/**
	 * 动画显示指定View从右边进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateRightIn(View view, int visibility){
		play(view, R.anim.fwex_translate_right_in, visibility);
	}
	/**
	 * 动画显示指定View从右边离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateRightOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_right_out, listener);
	}
	/**
	 * 动画显示指定View从右边离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateRightOut(View view, int visibility){
		play(view, R.anim.fwex_translate_right_out, visibility);
	}
			//#endregion 右横向移动动画播放
		//#endregion 横向移动动画播放
	
		//#region 纵向移动动画播放
			//#region 上纵向移动动画播放
	/**
	 * 动画显示指定View从上边进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateUpIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_up_in, listener);
	}
	/**
	 * 动画显示指定View从上边进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateUpIn(View view, int visibility){
		play(view, R.anim.fwex_translate_up_in, visibility);
	}
	/**
	 * 动画显示指定View从上边离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateUpOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_up_out, listener);
	}
	/**
	 * 动画显示指定View从上边离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateUpOut(View view, int visibility){
		play(view, R.anim.fwex_translate_up_out, visibility);
	}
			//#endregion 上纵向移动动画播放
	
			//#region 下纵向移动动画播放
	/**
	 * 动画显示指定View从下边进入
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateDownIn(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_down_in, listener);
	}
	/**
	 * 动画显示指定View从下边进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateDownIn(View view, int visibility){
		play(view, R.anim.fwex_translate_down_in, visibility);
	}
	/**
	 * 动画显示指定View从下边离开
	 * @param view
	 * @param listener 动画监听,无则为null
	 */
	public static final void playTranslateDownOut(View view, AnimationListener listener){
		play(view, R.anim.fwex_translate_down_out, listener);
	}
	/**
	 * 动画显示指定View从下边离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playTranslateDownOut(View view, int visibility){
		play(view, R.anim.fwex_translate_down_out, visibility);
	}
			//#endregion 下纵向移动动画播放
		//#endregion 纵向移动动画播放
	//#endregion 移动动画播放
	
	//#region 摇动动画播放
	/**
	 * 播放摇动动画
	 * @param view
	 * @param listener
	 */
	public static final void playShake(View view, AnimationListener listener){
		play(view, R.anim.fwex_shake, listener);
	}
	/**
	 * 播放摇动动画
	 * @param view
	 */
	public static final void palyShake(View view){
		playShake(view, null);
	}
	//#endregion 摇动动画播放
	
	//#region 浮动动画播放
	/**
	 * 播放摇动动画
	 * @param view
	 */
	public static final void playFloating(View view, AnimationListener listener){
		play(view, R.anim.fwex_floating, listener);
	}
	/**
	 * 播放摇动动画
	 * @param view
	 * @param listener
	 */
	public static final void playFloating(View view){
		playFloating(view, null);
	}
	//#endregion 浮动动画播放
	
	//#region 透明化动画播放Fade
	/**
	 * 播放fade动画进入
	 * @param view
	 * @param listener
	 */
	public static final void playFadeIn(View view, AnimationListener listener){
		play(view, android.R.anim.fade_in, listener);
	}
	/**
	 * 播放fade动画进入
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playFadeIn(View view, int visibility){
		play(view, android.R.anim.fade_in, visibility);
	}
	/**
	 * 播放fade动画离开
	 * @param view
	 * @param listener
	 */
	public static final void playFadeOut(View view, AnimationListener listener){
		play(view, android.R.anim.fade_out, listener);
	}
	/**
	 * 播放fade动画离开
	 * @param view
	 * @param visibility 播放动画后指定其可见性,值只能为View.GONE,View.INVISIBLE,View.VISIBLE
	 */
	public static final void playFadeOut(View view, int visibility){
		play(view, android.R.anim.fade_out, visibility);
	}
	//#endregion 透明化动画播放Fade
}

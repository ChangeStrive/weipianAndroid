package com.maya.android.vcard.ui.widget.xbdialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
/**
 * 自定义日期AlertDialog
 * @author Administrator
 *
 */
public class XbAlertDialogDate {
	private Context mContext;
	private Display display;
	private LinearLayout mLil_bg;
	private LinearLayout mLiladdview;
	private Button mBtnPositive;
	private Button mBtnNegTive;
	private Dialog dialog;
	private TextView mTxvTitle;
	
	public XbAlertDialogDate(Context context) {
		this.mContext = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}
	/**
	 * 初始化
	 * @return
	 */
	public XbAlertDialogDate builder(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.timterpick_date, null);
		mLil_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		mBtnPositive = (Button)view.findViewById(R.id.btn_dialog_positive);
		mBtnNegTive = (Button)view.findViewById(R.id.btn_dialog_negative);
		mTxvTitle = (TextView)view.findViewById(R.id.txt_title);
		mLiladdview = (LinearLayout)view.findViewById(R.id.dialog_Group);
		// 定义Dialog布局和参数
		dialog = new Dialog(mContext, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		// 调整dialog背景大小
		mLil_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (ActivityHelper.getScreenWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		return this;
	}
	
	/**
	 * 添加中间主体
	 * @param view
	 * @return
	 */
	public XbAlertDialogDate setView(View view){
		if (Helper.isNotNull(view)) {
			mLiladdview.addView(view,android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		} 
		return this;
	}
	/**
	 * 标题
	 * @param Resid
	 * @return
	 */
	public XbAlertDialogDate setTitle(int Resid){
		mTxvTitle.setText(Resid);
		return this;
	}
	/**
	 * 标题
	 * @param title
	 * @return
	 */
	public XbAlertDialogDate setTitle(String title){
		mTxvTitle.setText(title);
		return this;
	}
	/**
	 * 右边按钮内容（确认）
	 * @param Resid
	 * @param listener
	 * @return
	 */
	public XbAlertDialogDate setPositive(int Resid, final OnClickListener listener){
		mBtnPositive.setText(Resid);
		mBtnPositive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	/**
	 * 右边按钮内容（确认）
	 * @param title
	 * @param listener
	 * @return
	 */
	public XbAlertDialogDate setPositive(String title, final OnClickListener listener){
		mBtnPositive.setText(title);
		mBtnPositive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	/**
	 * 左边按钮内容（取消）
	 * @param Resid
	 * @param listener
	 * @return
	 */
	public XbAlertDialogDate setNegtive(int Resid, final OnClickListener listener){
		mBtnNegTive.setText(Resid);
		mBtnNegTive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	/**
	 * 左边按钮内容（取消）
	 * @param title
	 * @param listener
	 * @return
	 */
	public XbAlertDialogDate setNegtive(String title, final OnClickListener listener){
		mBtnNegTive.setText(title);
		mBtnNegTive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	
	/**
	 * 显示
	 */
	public void show(){
		dialog.show();
	}
}

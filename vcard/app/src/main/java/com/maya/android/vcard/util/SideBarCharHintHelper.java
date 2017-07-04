package com.maya.android.vcard.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.widget.SideBar;

/**
 * SideBar显示字母提示帮助类
 * Created by Administrator on 2015/9/25.
 */
public class SideBarCharHintHelper {
    private static SideBarCharHintHelper mSidebar;
    private TextView mTxvCharHint;
    private Context mContext;
    private WindowManager mWindowManager;
    /** 当前的 SideBar **/
    private SideBar mSibMember;
    /** 是否延迟 **/
    private  boolean isCharDelay = false;

    private SideBarCharHintHelper(Context mContext, SideBar mSibMember){
        this.mContext = mContext;
        this.mSibMember = mSibMember;
        this.mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public static SideBarCharHintHelper getIntance(Context mContext, SideBar mSibMember){
        return new SideBarCharHintHelper(mContext, mSibMember);
    }


    /**
     * 显示传入的字符
     *
     * @param s 传入字符
     * @params isCharTouch 是否字母点击
     */
    public void showCharHint(String s) {
        if(ResourceHelper.isEmpty(s)){
            return;
        }
        if(ResourceHelper.isNull(this.mTxvCharHint)){
            initCharHint(this.mContext, this.mWindowManager);
        }
        this.mTxvCharHint.setText(s.toUpperCase());
        this.mTxvCharHint.setVisibility(View.VISIBLE);
        // 1秒后隐藏弹出层
        this.isCharDelay = true;
        this.mHandler.sendMessageDelayed(new Message(), 1000);

    }

    /**
     * 将tvCharHint删除。
     */
    public void hideCharHint(SideBar mSibMember) {
        if(ResourceHelper.isNull(this.mTxvCharHint)){
            initCharHint(this.mContext, mWindowManager);
        }
        this.mTxvCharHint.setVisibility(View.GONE);
        mSibMember.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 清楚当前状态
     */
    public void clearCharHint(){
        if(ResourceHelper.isNotNull(mSidebar)){
            if(ResourceHelper.isNotNull(this.mWindowManager)) {
                if(ResourceHelper.isNotNull(this.mTxvCharHint)){
                    this.mWindowManager.removeView(this.mTxvCharHint);
                    this.mTxvCharHint = null;
                }
                this.mWindowManager = null;
            }
            mSidebar = null;
        }

    }

    /**
     * 得到延迟状态
     * @return
     */
    public boolean getCharDelay(){
        return this.isCharDelay;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideCharHint(mSibMember);
            isCharDelay = false;
        }
    };

    /**
     * 初始化 字母浮提示框v
     * @return
     */
    private void initCharHint(Context mContext, WindowManager mWindowManager){
        if( ResourceHelper.isNull(this.mTxvCharHint)){
            this.mTxvCharHint = new TextView(mContext);
            this.mTxvCharHint.setTextAppearance(mContext, R.style.cardcase_txv_char_hint);
            this.mTxvCharHint.setGravity(Gravity.CENTER);
            this.mTxvCharHint.setBackgroundResource(R.drawable.bg_charhint);
         }
        this.mTxvCharHint.setVisibility(View.GONE);
        int width = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_48dp);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(width, width,WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        lp.gravity = Gravity.CENTER;
        if(ResourceHelper.isNotNull(mWindowManager) && ResourceHelper.isNotNull(this.mTxvCharHint)){
            mWindowManager.addView(this.mTxvCharHint, lp);
        }
    }
}

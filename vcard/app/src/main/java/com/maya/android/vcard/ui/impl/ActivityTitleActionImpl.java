package com.maya.android.vcard.ui.impl;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * frg中设置Activity 顶部类
 * Created by YongJi on 2015/8/19.
 */
public interface ActivityTitleActionImpl {
    /**
     * 设置Title
     * @param resId
     * @param center 是否居中
     */
    void setActivityTitle(int resId, boolean center);

    /**
     * 设置Title
     * @param title
     * @param center
     */
    void setActivityTitle(String title, boolean center);

    /**
     * 设置头部是否隐藏
     * @param visibility
     */
    void setActivityTopVisibility(int visibility);
    /**
     * 设置Top的返回按钮是否展示</br>
     * @param visibility
     */
    void setActivityTopLeftVisibility(int visibility);

    /**
     * 设置Top的右边图片按钮是否展示
     * @param visibility
     */
    void setActivityTopRightImvVisibility(int visibility);

    /**
     * 设置Top的右边文字按钮是否展示
     * @param visibility
     */
    void setActivityTopRightTxvVisibility(int visibility);

    /**
     * 设置Top的左边imageView
     * @param srcResId 资源图
     * @param onClickListener
     */
    void setActivityTopLeftImv(int srcResId, View.OnClickListener onClickListener);

    /**
     * 设置Top的右边imageView
     * @param srcResId
     * @param onClickListener
     */
    void setActivityTopRightImv(int srcResId, View.OnClickListener onClickListener);

    /**
     * 设置Top的右边TextView
     * @param resId
     * @param onClickListener
     */
    void setActivityTopRightTxv(int resId, View.OnClickListener onClickListener);

    /**
     * 获取Top的左边imageView
     * @return
     */
    ImageView getActivityTopLeftImv();

    /**
     * 获取Top的右边imageView
     * @return
     */
    ImageView getActivityTopRightImv();

    /**
     * 获取Top的右边TextView
     * @return
     */
    TextView getActivityTopRightTxv();
}

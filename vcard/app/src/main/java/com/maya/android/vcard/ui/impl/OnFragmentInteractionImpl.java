package com.maya.android.vcard.ui.impl;

import android.os.Bundle;

/**
 * frg进行页面切换
 * Created by YongJi on 2015/8/18.
 */
public interface OnFragmentInteractionImpl {
    /**
     * fragment间进行切换
     * @param frgName
     * @param frgBundle
     */
    void onFragmentInteraction(String frgName, Bundle frgBundle);

    /**
     * 调用Activity的BackPressed()
     */
    void onActivityBackPressed();

    /**
     * 调用Activity的finish()
     */
    void onActivityFinish();

    /**
     * 设置Activity是否可以回退
     * @param isBackPressed
     */
    void onActivitySetBackPressed(boolean isBackPressed);
}

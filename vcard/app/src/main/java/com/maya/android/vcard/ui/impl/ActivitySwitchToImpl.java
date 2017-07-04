package com.maya.android.vcard.ui.impl;

import android.content.Intent;

/**
 *frg中进行跳转
 * Created by YongJi on 2015/8/18.
 */
public interface ActivitySwitchToImpl {
    /**
     * Activity进行跳转
     * @param cls
     */
    void switchTo(Class<?> cls, Intent intent);
}

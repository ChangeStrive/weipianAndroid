package com.maya.android.vcard.ui.impl;

import android.os.Bundle;

/**
 * 2个Fragment之间传递参数接口
 * Created by Administrator on 2015/11/4.
 */
public interface TwoFragmentParameterImpl {

    /**
     * 向指定Fragment传递参数(用bundle传值)
     * @param bundle
     */
    void twoFragmentTransfer(String frgName, Bundle bundle);
}

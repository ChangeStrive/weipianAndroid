package com.maya.android.vcard.ui.impl;

import android.os.Bundle;

/**
 * 批量操作监听接口
 * Created by Administrator on 2015/11/4.
 */
public interface CardcaseBatchOprerationImpl {
    /**
     * 调用方法类型
     * @param bundle
     */
   void userMethodType(Bundle bundle);

    /**
     * 显示选择按钮
     */
    void showSelect();

    /**
     * 隐藏选择按钮
     */
    void hideSelect();
}

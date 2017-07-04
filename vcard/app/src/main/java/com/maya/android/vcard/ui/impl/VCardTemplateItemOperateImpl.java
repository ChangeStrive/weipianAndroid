package com.maya.android.vcard.ui.impl;

import com.maya.android.vcard.ui.adapter.CardTemplateGridviewAdapter;

/**
 * Created by YongJi on 2015/9/24.
 */
public interface VCardTemplateItemOperateImpl {
    /**
     * 单击
     * @param position
     * @param adapter
     */
    void onItemClick(int position, CardTemplateGridviewAdapter adapter);
    /**
     * 长按
     * @param position
     * @param adapter
     */
    void onItemLongClick(int position, CardTemplateGridviewAdapter adapter);
}

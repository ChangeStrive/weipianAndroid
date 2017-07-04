package com.maya.android.vcard.ui.impl;

import com.maya.android.vcard.entity.json.LoginJsonEntity;

/**
 * frg中进行登录
 * Created by YongJi on 2015/8/25.
 */
public interface UserLoginImpl {
    void loginByCommon(LoginJsonEntity entity, boolean isRememberPassword);
    void loginByWeixin();
    void loginBySina();
    void loginByQQ();
}

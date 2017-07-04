package com.maya.android.vcard.handler;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.MessageSessionDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;

/**
 * 个信消息Handler
 * Created by YongJi on 2015/11/10.
 */
public class MessageHandler {
    //region 单例
    public static MessageHandler sInstance;
    public static MessageHandler getInstance(){
        if(Helper.isNull(sInstance)){
            sInstance = new MessageHandler();
        }
        return sInstance;
    }
    private MessageHandler(){}
    //endregion

    //region 变量
    private SettingEntity mSetting;
    //endregion

    //region public方法
    public void handlerMessage(MessageResultEntity message){
        int contentType = message.getContentType();
        switch (contentType){
            // 会话
            case Constants.MessageContentType.SESSION_FILE:
            case Constants.MessageContentType.SESSION_TEXT:
            case Constants.MessageContentType.SESSION_GROUP_FILE:
            case Constants.MessageContentType.SESSION_GROUP_TEXT:
                sessions(message);
                break;
        }
    }
    //endregion

    //region private方法

    /**
     * 会话
     * @param message
     */
    private void sessions(MessageResultEntity message){
        //插入数据库
        MessageSessionDao.getInstance().add(message, null);
        //TODO 通知栏展示

        //TODO 声音播放
        SettingEntity setting = this.getSetting();
        if(Helper.isNotNull(setting)){
//            if(setting.isAlertNewMessageOffLine())
        }

    }

    /**
     * 获取当前设置
     * @return
     */
    private SettingEntity getSetting(){
        if (Helper.isNull(this.mSetting)){
            this.mSetting = CurrentUser.getInstance().getSetting();
        }
        return this.mSetting;
    }
    //endregion
}

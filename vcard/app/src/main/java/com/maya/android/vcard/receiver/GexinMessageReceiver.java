package com.maya.android.vcard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.igexin.sdk.PushConsts;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.handler.MessageHandler;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * Receiver：个信推送
 * Created by YongJi on 2015/8/26.
 */
public class GexinMessageReceiver extends BroadcastReceiver {
    private static final String PAYLOAD = "payload";
    private static final String CLIENT_ID = "clientid";
    private static final String CELL = "cell";
    private static final String TAG = GexinMessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Helper.isNull(intent)){
            return;
        }
        switch(intent.getIntExtra(PushConsts.CMD_ACTION, -1)){
            case PushConsts.GET_CLIENTID:
                String clientId = intent.getStringExtra(CLIENT_ID);
                CurrentUser.getInstance().setClientId(clientId);
                LogHelper.d(TAG, "GeXinClientId:" + clientId);
                break;
            case PushConsts.GET_MSG_DATA:
                try{
                    byte[] payload = intent.getByteArrayExtra(PAYLOAD);
                    if(Helper.isNotNull(payload)){
                        String data = new String(payload);
                        if(ResourceHelper.isNotEmpty(data)){
                            //TODO 收到信息，进行处理
                            MessageResultEntity message = JsonHelper.fromJson(data, MessageResultEntity.class);
                            if(Helper.isNotNull(message)){
                                MessageHandler.getInstance().handlerMessage(message);
                            }
                        }
                    }
                }catch(NullPointerException e){
                    LogHelper.i("GexinMessageReceiver", "获取消息空异常onReceive方法错误，在NullPointerException捕获到");
                }catch(Exception e){
                    LogHelper.i("GexinMessageReceiver", "获取消息空异常onReceive方法错误，在Exception捕获到");
                }
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;
        }
    }
}

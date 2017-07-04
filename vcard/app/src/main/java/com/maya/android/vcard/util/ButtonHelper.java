package com.maya.android.vcard.util;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;

/**
 * Created by Administrator on 2015/8/18.
 */
public class ButtonHelper {

    /**
     * 按钮延时恢复为可点击
     * @param button
     */
    public static void setButtonEnableDelayed(Button button){
        setButtonEnableDelayed(button, -1);
    }

    /**
     * 按钮延时恢复为可点击(默认不可点击颜色为#787878，可点击颜色为#399c2f)
     * 默认延迟时间为2秒
     * @param button
     * @param color
     */
    public static void setButtonEnableDelayed(final Button button, final int color){
        setButtonEnable(button, R.color.color_787878, false);
        Message msg = new Message();
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(color == -1){
                    setButtonEnable(button, R.color.color_399c2f, true);
                }else{
                    setButtonEnable(button, color, true);
                }
            }
        }.sendMessageDelayed(msg, 1000);
    }

    /**
     * button 变化
     * @param button
     * @param color
     * @param isEnable
     */
    public static void setButtonEnable(Button button, int color, boolean isEnable){
        button.setEnabled(isEnable);
        button.setTextColor(ActivityHelper.getGlobalApplicationContext().getResources().getColor(color));
    }


}

package com.maya.android.vcard.util;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.entity.CustomLsvIntegerEntity;

import java.util.ArrayList;

/**
 * 弹窗内容帮助类
 * Created by Administrator on 2015/10/15.
 */
public class CustomLsvContentHelper {

    /**
     * 获取聊天信息弹窗内容
     * @param sendType
     * @return
     */
    public static ArrayList<CustomLsvIntegerEntity> getMsgChatInfo(int sendType){
        ArrayList<CustomLsvIntegerEntity> items = new ArrayList<CustomLsvIntegerEntity>();
        CustomLsvIntegerEntity item = new CustomLsvIntegerEntity();

        if (sendType == DatabaseConstant.MessageSendType.SEND) {
            //发送
            items.add(getLsvIntegerItem(item, R.string.repeat));
            items.add(getLsvIntegerItem(item, R.string.forward));
            items.add(getLsvIntegerItem(item, R.string.delete));
            items.add(getLsvIntegerItem(item, R.string.copy_message_text));
          } else if (sendType == DatabaseConstant.MessageSendType.RECIVER) {
            //接收方
            items.add(getLsvIntegerItem(item, R.string.forward));
            items.add(getLsvIntegerItem(item, R.string.delete));
            items.add(getLsvIntegerItem(item, R.string.copy_message_text));
         } else {
            //组消息
            items.add(getLsvIntegerItem(item, R.string.repeat));
            items.add(getLsvIntegerItem(item, R.string.forward));
            items.add(getLsvIntegerItem(item, R.string.delete));
         }
        items.add(getLsvIntegerItem(item, R.string.share_message));

        return items;
    }

    /**
     * 获取群里组设置信息中群主的可操作项
     * @param role
     * @return
     */
    public static ArrayList<CustomLsvIntegerEntity> getCircleGroupCreateSelectItems(int role){
        ArrayList<CustomLsvIntegerEntity> items = new ArrayList<CustomLsvIntegerEntity>();
        CustomLsvIntegerEntity item = new CustomLsvIntegerEntity();
        if(role == DatabaseConstant.Role.MEMBER){
            items.add(getLsvIntegerItem(item, R.string.set_administrator));
        }
        if(role == DatabaseConstant.Role.ADMIN){
            items.add(getLsvIntegerItem(item, R.string.cancel_administrator));
        }
        items.add(getLsvIntegerItem(item, R.string.remove_member));
        return items;
    }

    /**
     * 克隆实体类
     * @param item
     * @param resId
     * @return
     */
    private static CustomLsvIntegerEntity getLsvIntegerItem(CustomLsvIntegerEntity item, int resId){
        CustomLsvIntegerEntity mItem = null;
        try {
            mItem = (CustomLsvIntegerEntity) item.clone();
            mItem.setContentId(resId);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
         return mItem;
    }

}

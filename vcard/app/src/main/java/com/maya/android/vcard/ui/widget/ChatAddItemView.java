package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 插入View
 * Created by Administrator on 2015/9/11.
 */
public class ChatAddItemView {
    private static final String TAG = ChatAddItemView.class.getSimpleName();
    public static final int BLESSE_PHRASE = 7;
    public static final int QUICK_NEWS = 6;
    /** 插入项的图片 map key  **/
    public static final String ADD_ITEM_KEY_IMAGE = "image";
    /** 插入项的图片名称 map key  **/
    public static final String ADD_ITEM_KEY_NAME = "name";
    private Context mContext;
    private View addView;
    private OnItemChatAddItemViewListener mListener;
    /** 插入项的图片 **/
    private int[] mAddImages = { R.mipmap.img_chat_picture,//图片
            R.mipmap.img_chat_video,//视频
            R.mipmap.img_chat_file,//文件
            R.mipmap.img_chat_location,//位置
            R.mipmap.img_chat_send_vcard,//发送名片
            R.mipmap.img_chat_vcard_greet_card,//微贺卡
            R.mipmap.img_chat_quick_news,//快捷短信
            R.mipmap.img_chat_blesse_phrase//祝福短信
    };

    /** 插入项名称 **/
   private  String[] mAddNames;

    public ChatAddItemView(Context mContext){
        this.mContext = mContext;
        this.mAddNames = new String[]{
                mContext.getString(R.string.pop_chat_image),
                mContext.getString(R.string.pop_chat_video),
                mContext.getString(R.string.pop_chat_file),
                mContext.getString(R.string.pop_chat_position),
                mContext.getString(R.string.pop_chat_send_card),
                mContext.getString(R.string.pop_chat_greet_card),
                mContext.getString(R.string.pop_chat_quick_message),
                mContext.getString(R.string.pop_chat_bless_message)};
    }

    /**
     * 显示插入项
     * @param mLilAddParent
     */
    public void showChatAdd(LinearLayout mLilAddParent){
        if(ResourceHelper.isNull(this.addView)){
            this.addView = getAddView();
        }
        removeChatAdd(mLilAddParent);
        mLilAddParent.addView(this.addView);
    }

    /**
     * 移除插入项
     * @param mLilAddParent
     */
    public void removeChatAdd(LinearLayout mLilAddParent){
        if(ResourceHelper.isNotNull(this.addView)){
            mLilAddParent.removeView(this.addView);
        }

    }

    /**
     * 接口监听
     * @param mListener
     */
    public void setOnItemChatAddItemViewListener(OnItemChatAddItemViewListener mListener){
        this.mListener = mListener;
    }

    /**
     * 获取 插入项的列表
     * @return
     */
    private ArrayList<HashMap<String, Object>> getAddItemList(){
        ArrayList<HashMap<String, Object>> addList = new ArrayList<HashMap<String,Object>>();
        for (int i = 0 , len = this.mAddImages.length; i < len; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(ADD_ITEM_KEY_IMAGE, this.mAddImages[i]);
            map.put(ADD_ITEM_KEY_NAME, this.mAddNames[i]);
            addList.add(map);
        }
        return addList;
    }

    /**
     * 获取插入项的 view
     * @return
     */
    public View getAddView(){
        View addView = LayoutInflater.from(this.mContext).inflate(R.layout.item_chat_add, null);
        GridView grvAdd = (GridView) addView.findViewById(R.id.grv_chat_add);
        String[] from = { ADD_ITEM_KEY_IMAGE, ADD_ITEM_KEY_NAME };
        int[] to = { R.id.imv_chat_add_picture,R.id.txv_chat_add_name };
        SimpleAdapter adapter = new SimpleAdapter(this.mContext, getAddItemList(), R.layout.item_chat_add_to, from, to);
        grvAdd.setAdapter(adapter);
        grvAdd.setOnItemClickListener(this.mOnItemClickListener);
        return addView;
    }

    /**
     * 单击事件
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0://图片
                    intent2Photo(Constants.RequestCode.REQUEST_CODE_ADD_IMAGE);
                    break;
                case 1://视频
                    intent2Video(Constants.RequestCode.REQUEST_CODE_ADD_VIDEO);
                    break;
                case 2://文件
                    intent2File(Constants.RequestCode.REQUEST_CODE_ADD_FILE);
                    break;
                case 3://TODO 位置
                    break;
                case 4://TODO 发送名片
                    break;
                case 5://TODO 微贺卡
                    break;
                case QUICK_NEWS://快捷短信
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.onSelected(QUICK_NEWS);
                    }
                    break;
                case BLESSE_PHRASE://祝福短信
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.onSelected(BLESSE_PHRASE);
                    }
                    break;
            }
        }
    };

    /**
     * 转到手机文件
     */
    private void intent2File(int requestCode) {
        // 发送文件
        try {
            Intent intent = new Intent();
            intent.setType("application/vnd.android.package-archive");
            intent.setType("application/msword");
            intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            intent.setType("application/vnd.ms-excel");
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.setType("application/octet-stream");
            intent.setType("application/x-gzip");
            intent.setType("text/html");
            intent.setType("application/java-archive");
            intent.setType("text/plain");
            intent.setType("application/x-javascript");
            intent.setType("application/pdf");
            intent.setType("application/vnd.ms-powerpoint");
            intent.setType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
            intent.setType("application/vnd.ms-works");
            intent.setType("application/x-zip-compressed");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if(ResourceHelper.isNotNull(this.mListener)){
                this.mListener.onIntent(intent, requestCode);
            }
        } catch (Exception e) {
            ActivityHelper.showToast("找不到文件浏览器");
        }
    }

    /**
     * 转到手机图片库
     */
    private void intent2Photo(int requestCode) {
        // 发送 图片
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if(ResourceHelper.isNotNull(this.mListener)){
            this.mListener.onIntent(intent, requestCode);
        }
    }

    /**
     * 转到手机视频
     */
    private void intent2Video(int requestCode) {
        // 发送视频
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if(ResourceHelper.isNotNull(this.mListener)){
                this.mListener.onIntent(intent, requestCode);
            }

        } catch (Exception e) {
            Log.e(TAG, "读取视频文件异常", e);
        }
    }



    /**
     * 插入项监听接口
     */
    public interface OnItemChatAddItemViewListener{
        /**
         * 弹出对话框
         * @param tag
         */
        void onSelected(int tag);
        /**
         * 跳转(startActivityForResult(intent, requestCode);)
         * @param intent
         * @param requestCode
         */
        void onIntent(Intent intent, int requestCode);
    }
}

package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ExpressionHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  表情
 * Created by Administrator on 2015/9/10.
 */
public class CartoonFaceView {
    private Context mContext;
    /**表情 的 map key */
    private static final String FACE_KEY_IMAGE = "imgFace";
    private OnItemCartoonFaceViewListener mListener;
    private View faceView;

    private int[] mFaceImages = { R.mipmap.f001, R.mipmap.f002,
            R.mipmap.f003, R.mipmap.f004, R.mipmap.f005, R.mipmap.f006,
            R.mipmap.f007, R.mipmap.f008, R.mipmap.f009, R.mipmap.f010,
            R.mipmap.f011, R.mipmap.f012, R.mipmap.f013, R.mipmap.f014,
            R.mipmap.f015, R.mipmap.f016, R.mipmap.f017, R.mipmap.f018,
            R.mipmap.f019, R.mipmap.f020,
            R.mipmap.img_chat_backspace };

    public CartoonFaceView(Context mContext){
        this.mContext = mContext;
    }

    /**
     * 删除表情
     *
     * @param oriString
     * @return
     */
    public String removeLastStr(String oriString) {
        String result = "";
        if(oriString.length() > 0){
            result = oriString.substring(0, oriString.length() - 1);
        }
        return result;
    }

    /**
     * 显示表情
     * @param mLilAddFaceView
     */
    public void showFaceView(final LinearLayout mLilAddFaceView){
        // 设置表情项子项的高度
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        height = ActivityHelper.calDpi2px(180);
        height = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_200dp);
        // 添加子项
         LinearLayout.LayoutParams relLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        if(ResourceHelper.isNull(this.faceView)){
            this.faceView = getFaceView();
        }
        removeFaceView(mLilAddFaceView);
        mLilAddFaceView.addView(this.faceView, relLp);
    }

     /**
     * 移除表情View
     * @param mLilAddFaceView
     */
    public void removeFaceView(LinearLayout mLilAddFaceView){
        if(ResourceHelper.isNotNull(this.faceView)){
            mLilAddFaceView.removeView(this.faceView);
        }

    }

    /**
     * 表情选择监听
     * @param mListener
     */
    public void setOnItemCartoonFaceViewListener(OnItemCartoonFaceViewListener mListener){
        this.mListener = mListener;
    }

    /**
     * 获取表情View
     * @return
     */
    private View getFaceView(){
        View faceView = LayoutInflater.from(this.mContext).inflate(R.layout.item_chat_face, null);
        GridView mGrvFace = (GridView) faceView.findViewById(R.id.grv_face);
        String[] from = { FACE_KEY_IMAGE };
        int[] to = { R.id.imv_pop_act_message_chat_face };
        SimpleAdapter adapter = new SimpleAdapter(this.mContext, getFaceList(), R.layout.item_chat_face_to, from, to);
        mGrvFace.setAdapter(adapter);
        mGrvFace.setOnItemClickListener(this.mOnItemClickListener);
        return faceView;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str = null;
//            Bitmap bitmap = null;
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mFaceImages[position]);
            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, ExpressionHelper.sSize, ExpressionHelper.sSize, true);
            ImageSpan imageSpan = new ImageSpan(mContext, resizeBitmap);
            boolean isDelFace = false;
            switch (position) {
                case 0:
                    str = "f001";
                    break;
                case 1:
                    str = "f002";
                    break;
                case 2:
                    str = "f003";
                    break;
                case 3:
                    str = "f004";
                    break;
                case 4:
                    str = "f005";
                    break;
                case 5:
                    str = "f006";
                    break;
                case 6:
                    str = "f007";
                    break;
                case 7:
                    str = "f008";
                    break;
                case 8:
                    str = "f009";
                    break;
                case 9:
                    str = "f010";
                    break;
                case 10:
                    str = "f011";
                    break;
                case 11:
                    str = "f012";
                    break;
                case 12:
                    str = "f013";
                    break;
                case 13:
                    str = "f014";
                    break;
                case 14:
                    str = "f015";
                    break;
                case 15:
                    str = "f016";
                    break;
                case 16:
                    str = "f017";
                    break;
                case 17:
                    str = "f018";
                    break;
                case 18:
                    str = "f019";
                    break;
                case 19:
                    str = "f020";
                    break;
                default:
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.onDelFace();
                    }
                    isDelFace = true;
                    break;
            }
            if(!isDelFace){
                SpannableString ss = new SpannableString(str);
                ss.setSpan(imageSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(ResourceHelper.isNotNull(mListener)){
                    mListener.onGetFace(ss);
                }
            }

        }
    };

    /**
     * 获取 表情的 列表
     *
     * @return
     */
    private ArrayList<HashMap<String, Integer>> getFaceList() {
        ArrayList<HashMap<String, Integer>> listItems = new ArrayList<HashMap<String, Integer>>();

        for (int i = 0, count = mFaceImages.length; i < count; i++) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            map.put(FACE_KEY_IMAGE, mFaceImages[i]);
            listItems.add(map);
        }
        return listItems;
    }


    /**
     * 表情类监听接口
     */
    public interface OnItemCartoonFaceViewListener{
        /**
         * 获取表情
         * @param ss
         */
        void onGetFace(SpannableString ss);

        /**
         * 删除操作
         */
        void onDelFace();
    }
}

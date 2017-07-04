package com.maya.android.vcard.ui.frg;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.ui.adapter.CommonViewPagerAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.AudioRecordHelper;

import java.util.ArrayList;

/**
 * 欢迎页
 */
public class WelcomeFragment extends BaseFragment {
    private static final int WHAT_ANIMATION_START = 1001;
    private static final int WHAT_ANIMATION_STOP = 1002;
    private static final int TIME_GAP_SOUND_PLAY = 1000;

    private ImageViewPointAdapter mImageViewPointAdapter;
    private GridView mGrvPoint;
    private ViewPager mVwp;
    private ImageView mImvShake;
    private ArrayList<View> mViewItems = new ArrayList<View>();
    private AnimationDrawable mShakeDrawable;
    /** 声音播放间隔  **/
    private long mGapSoundPlay = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(Helper.isNull(mShakeDrawable)) {
                mShakeDrawable = (AnimationDrawable) mImvShake.getDrawable();
            }
            switch (msg.what){
                case WHAT_ANIMATION_START:
                    if(Helper.isNotNull(mShakeDrawable)){
                        mShakeDrawable.start();
                    }
                    long currentTime = System.currentTimeMillis();
                    if((currentTime - mGapSoundPlay) > TIME_GAP_SOUND_PLAY){
                        mGapSoundPlay = currentTime;
                        AudioRecordHelper.play();
                    }
                    break;
                case WHAT_ANIMATION_STOP:
                    if(Helper.isNotNull(mShakeDrawable)){
                        mShakeDrawable.stop();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        this.mGrvPoint = findView(view, R.id.grv_frg_welcome);
        this.mVwp = findView(view, R.id.vwp_frg_welcome);
        View vw1 = inflater.inflate(R.layout.item_frg_welcome_1, null);
        View vw2 = inflater.inflate(R.layout.item_frg_welcome_2, null);
        View vw3 = inflater.inflate(R.layout.item_frg_welcome_3, null);
        View vw4 = inflater.inflate(R.layout.item_frg_welcome_4, null);
        View vw5 = inflater.inflate(R.layout.item_frg_welcome_5, null);
        this.mViewItems.add(vw1);
        this.mViewItems.add(vw2);
        this.mViewItems.add(vw3);
        this.mViewItems.add(vw4);
        this.mViewItems.add(vw5);
        this.mImvShake = findView(vw3, R.id.imv_frg_welcome_3_middle);
        Button btn = findView(vw5, R.id.btn_welcome_frg_welcome);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickEvent();
            }
        });
        CommonViewPagerAdapter adapter = new CommonViewPagerAdapter();
        this.mVwp.setAdapter(adapter);
        this.mVwp.setOnPageChangeListener(new WelcomeOnPageListener());
        adapter.addItemList(this.mViewItems);

        this.mImageViewPointAdapter = new ImageViewPointAdapter(this.mViewItems.size());
        this.mGrvPoint.setAdapter(this.mImageViewPointAdapter);

        //设置播放声音 resource
        AudioRecordHelper.setRescource2MediaPlay(R.raw.shake_welcome);
        return view;
    }

    private void onClickEvent(){
//        PreferencesHelper.getInstance(Constants.Preferences.KEY_NAME_SOFTWARE)
        PreferencesHelper.getInstance()
                .putInt(Constants.Preferences.KEY_SAVE_OLD_VERSION, ActivityHelper.getCurrentVersion());
        AudioRecordHelper.releaseMediaPlay(true);
        this.mActivitySwitchTo.switchTo(null, null);
    }

    private class WelcomeOnPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mImageViewPointAdapter.setCurrentPosition(position);
            if(position == 2){
                mHandler.sendEmptyMessage(WHAT_ANIMATION_START);
            }else{
                mHandler.sendEmptyMessage(WHAT_ANIMATION_STOP);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
    /**
     * 圆点图片适配器
     */
    private class ImageViewPointAdapter extends BaseAdapter {
        private int mCurrentPosition = 0;
        private int mSize;

        public ImageViewPointAdapter(int size){
            this.mSize = size;
        }
        /**
         * 设置当前滑动的位置
         * @param position
         */
        public void setCurrentPosition(int position){
            this.mCurrentPosition = position;
            notifyDataSetChanged();
        }
        public int getCount() {
            return this.mSize;
        }

        public Integer getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView picturesView;
            if (Helper.isNull(convertView)) {
                picturesView = new ImageView(getActivity());
            } else {
                picturesView = (ImageView) convertView;
            }
            if(this.mCurrentPosition == position){
                picturesView.setImageResource(R.mipmap.img_point_frg_welcome_focus);
            }else{
                picturesView.setImageResource(R.mipmap.img_point_frg_welcome);
            }
            return picturesView;
        }

    }
}

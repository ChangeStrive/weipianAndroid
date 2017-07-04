package com.maya.android.vcard.ui.frg;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.dao.MessageNoticeDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.MessageNoticeEntity;
import com.maya.android.vcard.entity.json.VCardInfoJsonEntity;
import com.maya.android.vcard.entity.result.ContactRadarResultEntity;
import com.maya.android.vcard.entity.result.RadarSearchResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.adapter.VCardSwapRadarAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.SwapRadarMemberDialogFragment;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.Images;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 名片交换：雷达
 */
public class VCardSwapRadarFragment extends BaseFragment {
    /** 不是好友 **/
    public static final int FRIEND_NOT = 0;
    /** 已是好友 **/
    public static final int FRIEND_IS = 1;
     /** 已发送交换请求 **/
    public static final int FRIEND_SEND = 2;
    /** 接收交换请求 **/
    public static final int FRIEND_RECEIVE = -1;
     /** 正在雷达收索 **/
    private boolean isRandaring = true;//默认正在雷达收索
    /** 雷达扫描监听 **/
    private RadarSearchReceiver mRadarReceiver;
    /** 名片Id **/
    private long cardId;
    private SwapRadarMemberDialogFragment mSwapDialogFragmentMember;
    private TextView mTxvSearchHint;
    private RelativeLayout mRelRadarSearch;
    private ImageView mImvRadarSearchAinm;
    private ImageButton mImbAinm;
    private GridView mGridSwapRadarMember;
    private Animation mOperatingAnim;
    private ArrayList<Integer> mImageId;
    private VCardSwapRadarAdapter mSwapRadarAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_common_back:
                    mFragmentInteractionImpl.onActivityBackPressed();
                    break;
            }
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_RADAR_SCAN:
                    RadarSearchResultEntity reustlEntity = JsonHelper.fromJson(commandResult, RadarSearchResultEntity.class);
                    if(ResourceHelper.isNotNull(reustlEntity)){
                        ArrayList<ContactRadarResultEntity> swapRadars = reustlEntity.getContactList();
                        this.mSwapRadarAdapter.addItems(swapRadars);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        switch (tag) {
            case Constants.CommandRequestTag.CMD_REQUEST_RADAR_SCAN:
                //TODO 此处为测试数据，数据填充后应删除此句
                showSwapRadarMember(getArrayList(8));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcard_swap_radar, container, false);
        TextView mTxvBack = (TextView) view.findViewById(R.id.txv_common_back);
        this.mRelRadarSearch = (RelativeLayout) view.findViewById(R.id.rel_radar_receiving);
        this.mTxvSearchHint = (TextView) view.findViewById(R.id.txv_radar_search);
        this.mImvRadarSearchAinm = (ImageView) view.findViewById(R.id.imv_radar_search_anim);
        this.mGridSwapRadarMember = (GridView) view.findViewById(R.id.grv_swap_radar);
        this.mGridSwapRadarMember.setOnItemClickListener(this.mOnItemClickListener);
        mTxvBack.setOnClickListener(this.mOnClickListener);
        this.mImbAinm = (ImageButton) view.findViewById(R.id.imb_radar_receiving);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.radar_search,false);
        //添加声音
        AudioRecordHelper.setRescource2MediaPlay(R.raw.radar_hold);
        AudioRecordHelper.play();
        //注册监听结果
        registerRadarReceiver();
        CardEntity card = CurrentUser.getInstance().getCurrentVCardEntity();
        if(ResourceHelper.isNotNull(card)){
            this.cardId = card.getId();
        }//测试数据
        showSwapRadarMember(getArrayList(8));
        //搜索动画
        onRadarDownAnimation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ResourceHelper.isNotNull(this.mRadarReceiver)) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
            broadcastManager.unregisterReceiver(this.mRadarReceiver);
        }
        AudioRecordHelper.stopMediaPlay();
        setReceiverItem(this.mSwapRadarAdapter.getItems());
        stopSearch();
    }

   /**
     * 搜索动画
     */
    private void onRadarDownAnimation() {
        this.isRandaring = true;
        requestStartRadarSearch();
        if (ResourceHelper.isNull(this.mOperatingAnim)) {
            this.mOperatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_mycard_radar_search);
            LinearInterpolator lin = new LinearInterpolator();
            this.mOperatingAnim.setInterpolator(lin);
        }
        this.mTxvSearchHint.setVisibility(View.VISIBLE);
        this.mImbAinm.startAnimation(this.mOperatingAnim);
        new AsyncTask<Void, Bitmap, Void>() {

            protected void onProgressUpdate(Bitmap... values) {
                mImvRadarSearchAinm.setImageBitmap(values[0]);
            }

            protected Void doInBackground(Void... params) {
                if (ResourceHelper.isNull(mImageId)) {
                    mImageId = new ArrayList<Integer>();
                    mImageId.add(R.mipmap.img_radar_ripple_01);
                    mImageId.add(R.mipmap.img_radar_ripple_02);
                    mImageId.add(R.mipmap.img_radar_ripple_03);
                    mImageId.add(R.mipmap.img_radar_ripple_04);
                    mImageId.add(R.mipmap.img_radar_ripple_05);
                    mImageId.add(R.mipmap.img_radar_ripple_06);
                    mImageId.add(R.mipmap.img_radar_ripple_07);
                }


                for (int i = 0; i < mImageId.size(); i++) {
                    if (!isRandaring) {
                        break;
                    }
                    try {
                        int values = mImageId.get(i);
                        Bitmap bm = BitmapFactory.decodeStream(getActivity().getResources().openRawResource(values));
                        publishProgress(bm);
                        if (i == 0 || i == 6) {
                            Thread.sleep(50);
                        } else {
                            Thread.sleep(100);
                        }
                        if (mImageId.size() - i == 1) {
                            i = -1;
                        }
                    } catch (Exception e) {
                    }

                }
                return null;
            }
        }.execute();

    }

    /**
     * 停止搜索
     */
    private void stopSearch() {
        this.isRandaring = false;
        this.mImbAinm.clearAnimation();
        AudioRecordHelper.stopMediaPlay();
        this.mTxvSearchHint.setVisibility(View.INVISIBLE);
        requestRemoveRadarSearch();
    }

    /**
     * Layout动画
     *
     * @return
     */
    protected LayoutAnimationController getAnimationController() {
        int duration = 300;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    /**
     * 处理接收到交换通知的对象
     */
    public void setReceiverItem(ArrayList<ContactRadarResultEntity> swapRadars) {
        UserInfoResultEntity myInfo = CurrentUser.getInstance().getUserInfoEntity();
        if (ResourceHelper.isNotNull(myInfo) && ResourceHelper.isNotNull(swapRadars)) {
            long myAccountId = myInfo.getId();
            for (int i = 0, size = swapRadars.size(); i < size; i++) {
                ContactRadarResultEntity item = swapRadars.get(i);
                if (item.getFriend() == FRIEND_RECEIVE) {
                    MessageNoticeEntity notice = new MessageNoticeEntity();
                    notice.setCreateTime(ResourceHelper.getNowTime());
                    notice.setContentType(Constants.MessageContentType.NOTICE_CARD_SWAP_REQUEST);
                    notice.setLoseTime(0);
                    notice.setFromCardId(item.getCardId());
                    notice.setFromAccountId(item.getAccountId());
                    notice.setFromHeadImg(item.getHeadImg());
                    notice.setFromName(item.getDisplayName());
                    notice.setToAccountId(myAccountId);
                    notice.setTagId(0);
                    notice.setBody(getString(R.string.notice_from_radar_scan));
                    notice.setDealStatus(DatabaseConstant.NoticeDealStatus.UNDEAL);
                    notice.setTitle(getString(R.string.notice_request_swap_card_hope, item.getDisplayName()));
                    MessageNoticeDao.getInstance().add(notice);
                }
            }
        }
    }



    /**
     * 注册监听
     */
    private void registerRadarReceiver() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        this.mRadarReceiver = new RadarSearchReceiver();
        IntentFilter filter = new IntentFilter(Constants.ActionIntent.ACTION_INTENT_RADAR_SCAN);
        broadcastManager.registerReceiver(this.mRadarReceiver, filter);
    }


    /**
     * 展示收索到的成员结果
     *
     * @param swapRadars
     */
    private void showSwapRadarMember(ArrayList<ContactRadarResultEntity> swapRadars) {

        if (ResourceHelper.isNull(this.mSwapRadarAdapter)) {
            this.mSwapRadarAdapter = new VCardSwapRadarAdapter(getActivity());
            this.mGridSwapRadarMember.setAdapter(this.mSwapRadarAdapter);
        }
        if (ResourceHelper.isNotNull(swapRadars)) {
            this.mSwapRadarAdapter.addItems(swapRadars);
        }

    }



    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ContactRadarResultEntity swapRadar = mSwapRadarAdapter.getItem(position);
            if(ResourceHelper.isNotNull(swapRadar) && swapRadar.getCardId() != -3333){
                showSwapDialogFragmentMember(position);
            }

        }
    };

    /**
     * 点击头像显示单个成员对话框
     */
    private void showSwapDialogFragmentMember(int position){
        ContactRadarResultEntity swapRadar = this.mSwapRadarAdapter.getItem(position);
        if(ResourceHelper.isNull(this.mSwapDialogFragmentMember)){
            this.mSwapDialogFragmentMember = SwapRadarMemberDialogFragment.newInstance();
            this.mSwapDialogFragmentMember.setOnClickInterface(new SwapRadarMemberDialogFragment.OnClickInterface() {
                @Override
                public void onClick() {
                    //TODO 点击事件
                }
            });
        }

        this.mSwapDialogFragmentMember.setName(swapRadar.getDisplayName()).setCompany(swapRadar.getCompanyName()).setJob(swapRadar.getJob()).setHeadImg(ResourceHelper.getImageUrlOnIndex(swapRadar.getHeadImg(), 0));
        int tag = swapRadar.getFriend();
        if(tag <= 0){
            if(tag == FRIEND_RECEIVE){
                this.mSwapDialogFragmentMember.setBtnName(R.string.swap_card_fail_agree_exchange, R.drawable.bg_general_normal_green, R.color.color_ffffff);
            }else{
                this.mSwapDialogFragmentMember.setBtnName(R.string.cloud_swap_card, R.drawable.bg_general_normal_green, R.color.color_ffffff);
            }
            this.mSwapDialogFragmentMember.setIsEnable(true);
        }else{
             if(tag == FRIEND_IS){
                this.mSwapDialogFragmentMember.setBtnName(R.string.swap_card_fail_is_friend, R.drawable.bg_btn_radar_unenable, R.color.color_b7babc);
            }else if(tag == FRIEND_SEND){
                this.mSwapDialogFragmentMember.setBtnName(R.string.swap_card_fail_exchange_sended, R.drawable.bg_btn_radar_unenable, R.color.color_b7babc);
            }
            this.mSwapDialogFragmentMember.setIsEnable(false);
        }
        this.mSwapDialogFragmentMember.show(getFragmentManager(), "mSwapDialogFragmentMember");
    }

    /**
     * 测试数据
     * @return
     */
    private ArrayList<ContactRadarResultEntity> getArrayList(int count){
        ArrayList<ContactRadarResultEntity> items = new ArrayList<ContactRadarResultEntity>();
        for(int i = 0; i < count; i++){
            ContactRadarResultEntity item = new ContactRadarResultEntity();
            item.setHeadImg(Images.imageThumbUrls[i]);
            item.setDisplayName("蘑菇" + i);
            item.setCompanyName("玛雅"+i);
            item.setJob("民工"+i);
            item.setFriend(FRIEND_IS);
            items.add(item);
        }

        return items;
    }

    /**
     * 请求服务端雷达收索
     */
    private void requestStartRadarSearch() {
        URLResultEntity mUrlEntity = CurrentUser.getInstance().getURLEntity();
        if(ResourceHelper.isNotNull(mUrlEntity) && this.cardId > 0){
            String url = ProjectHelper.fillSwapRequestURL(mUrlEntity.getRadarScan());
            String token = CurrentUser.getInstance().getToken();
            VCardInfoJsonEntity card = new VCardInfoJsonEntity();
            card.setCardId(this.cardId);
            String json = JsonHelper.toJson(card, VCardInfoJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_RADAR_SCAN, url, token, json, this);
        }
    }

    /**
     * 请求断开雷达收索
     */
    private void requestRemoveRadarSearch(){
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if(ResourceHelper.isNotNull(urlEntity)){
            String url = ProjectHelper.fillSwapRequestURL(urlEntity.getRadarRemove());
            String token = CurrentUser.getInstance().getToken();
            VCardInfoJsonEntity card = new VCardInfoJsonEntity();
            card.setCardId(this.cardId);
            String json = JsonHelper.toJson(card, VCardInfoJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_RADAR_REMOVE, url, token, json, this);
        }
    }

    /**
     * 雷达扫描结果监听
     * @author zheng_cz
     * @since 2014年5月13日 上午10:25:57
     */
    private class RadarSearchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long accountId;
            int intentCode = 0;
            switch (intentCode){
                case Constants.IntentSet.INTENT_CODE_IS_FROM_ADD_VCARD_ACTIVITY:
                    //来自主页面
                    stopSearch();
                    break;
                case Constants.IntentSet.INTENT_CODE_FROM_RADAR_RESULT:
                    //来自雷达扫描的结果
                    String result = intent.getStringExtra(Constants.IntentSet.INTENT_KEY_CONTACT_ENTITY);
                    if(ResourceHelper.isNotEmpty(result)) {
                        ContactRadarResultEntity resultEntity = JsonHelper.fromJson(result, ContactRadarResultEntity.class);
                        if (Helper.isNotNull(resultEntity)) {
                            mSwapRadarAdapter.addItem(resultEntity);
                        }
                    }
                    break;
                case Constants.IntentSet.INTENT_CODE_FROM_RADAR_SWAP:
                    //来自雷达扫描交换名片
                    accountId = intent.getLongExtra(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, 0);
//                    long cardId = intent.getLongExtra(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
                    mSwapRadarAdapter.setItem(accountId, FRIEND_SEND);
                    break;
                case Constants.IntentSet.INTENT_CODE_FROM_RADAR_SWAP_RESULT:
                    // 来自雷达扫描交换名片结果
                    accountId = intent.getLongExtra(Constants.IntentSet.INTENT_KEY_ACCOUNT_ID, 0);
//                    long cardId1 = intent.getLongExtra(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
                    mSwapRadarAdapter.setItem(accountId, FRIEND_IS);
                    break;
            }
        }
    }
}

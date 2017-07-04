package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.AddAttentionJsonEntity;
import com.maya.android.vcard.entity.result.FansAndAttentionsListResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.adapter.UserAttAndFansAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

/**
 * 我的：关注/粉丝
 */
public class UserAttAndFansFragment extends BaseFragment {
    public static final String KEY_ATT_OR_FANS = "key_att_or_fans";
    /** 切换到关注页面 **/
    public static final int ATT_OR_FANS_TO_ATTENTION = 600003;
    /** 切换到粉丝页面 **/
    public static final int ATT_OR_FANS_TO_FANS = 600004;
    /** 记录当前切换的页面 **/
    private int intentCode;
    /** 记录当前选中的 Item项 **/
    private int curPosition = -1;
    private ListView mLsvAttAndFans;
    private TextView mTxvLoading;
    private UserAttAndFansAdapter mAttAndFans;
    private URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt(UserInfoFragment.KEY_SHOW_USER, UserInfoFragment.CODE_SHOW_OTHER_USER_INFO);
            bundle.putString(UserInfoFragment.KEY_VCARDSNO, mAttAndFans.getItem(position).getVcardNo());
            mFragmentInteractionImpl.onFragmentInteraction(UserInfoFragment.class.getName(), bundle);
        }
    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_FANS_OR_ATTENTIONS_LIST:
                    FansAndAttentionsListResultEntity resultEntity = JsonHelper.fromJson(commandResult, FansAndAttentionsListResultEntity.class);
                    if(Helper.isNotNull(resultEntity)){
                        this.mAttAndFans.addItems(resultEntity.getFansList());
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_ATTENTIONS_ADD){
            this.mAttAndFans.chanceStutas(curPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_user_att_and_fans, container, false);
        this.mLsvAttAndFans = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvLoading = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvAttAndFans.setEmptyView(this.mTxvLoading);
        this.mLsvAttAndFans.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        this.intentCode = ATT_OR_FANS_TO_ATTENTION;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.intentCode = bundle.getInt(KEY_ATT_OR_FANS, ATT_OR_FANS_TO_ATTENTION);
        }
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        String url = "";
        if(this.intentCode == ATT_OR_FANS_TO_ATTENTION){
            url = ProjectHelper.fillRequestURL(urlEntity.getAttentionList());
            super.mTitleAction.setActivityTitle(R.string.my_attention, true);

        }else{
            url = ProjectHelper.fillRequestURL(urlEntity.getFansList());
            super.mTitleAction.setActivityTitle(R.string.my_fans, true);
        }
        this.mAttAndFans = new UserAttAndFansAdapter(getActivity());
        this.mLsvAttAndFans.setAdapter(this.mAttAndFans);
        this.mAttAndFans.setStatusListener(this.mStatusListener);
        requestFansOrAttentionsList(url);
    }

    /**
     * 关注
     */
    private UserAttAndFansAdapter.StatusListener mStatusListener = new UserAttAndFansAdapter.StatusListener() {
        @Override
        public void onClick(int position, String vCardNo) {
            curPosition = position;
            requestAddAttention(vCardNo);
        }
    };

    /**
     * 请求获取粉丝或关注数据
     * @param url
     */
    private void requestFansOrAttentionsList(String url){
        String token =  CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_FANS_OR_ATTENTIONS_LIST,url ,token, new JSONObject(), this);
    }

    /**
     * 请求关注
     * @param vcardNo
     */
    private void requestAddAttention(String vcardNo){
        AddAttentionJsonEntity mStatusEntity = new AddAttentionJsonEntity();
        mStatusEntity.setVcardNo(vcardNo);
        String json = JsonHelper.toJson(mStatusEntity, AddAttentionJsonEntity.class);
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        String url = ProjectHelper.fillRequestURL(urlEntity.getAttentionAdd());
        String token = CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ATTENTIONS_ADD, url, token , json, this);
    }

}

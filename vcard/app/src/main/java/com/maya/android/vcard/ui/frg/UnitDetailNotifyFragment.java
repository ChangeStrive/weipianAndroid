package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.UnitDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.WeNoticeEntity;
import com.maya.android.vcard.entity.json.UnitNoticeJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.adapter.UnitWeNoticeAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 单位：详细页：微公告
 */
public class UnitDetailNotifyFragment extends BaseFragment {
    public static final String KEY_NOTIFY_PUBLISH_UNITID = "key_notify_publish_unitid";
    /** 单位Id **/
    private long unitId;
    /** 判断是否是管理员 **/
    private boolean isAdmin = false;

    private ListView mLsvWeNotice;
    private TextView mTxvEmpty;
    private UnitWeNoticeAdapter mUnitWeNoticeAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()){
                case R.id.txv_act_top_right:
                    //发布微公告
                    bundle.putLong(UnitDetailNotifyPublishFragment.KEY_NOTIFY_PUBLISH_UNITID, unitId);
                    mFragmentInteractionImpl.onFragmentInteraction(UnitDetailNotifyPublishFragment.class.getName(), bundle);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_detail_notify, container, false);
        this.mLsvWeNotice = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvWeNotice.setEmptyView(this.mTxvEmpty);
        return view;
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST:
                    //获取企业公告
                    this.mTxvEmpty.setText(R.string.frg_common_nothing_data);
                    String memberResult = commandResult.optString("noticeList");
                    if(ResourceHelper.isNotEmpty(memberResult)){
                        Type memberType = new TypeToken<ArrayList<WeNoticeEntity>>(){}.getType();
                        ArrayList<WeNoticeEntity> memberList = JsonHelper.fromJson(memberResult, memberType);
                        mUnitWeNoticeAdapter.addItems(memberList);
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.we_notice, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxv(R.string.release, this.mOnClickListener);
        this.mLsvWeNotice.setBackgroundColor(getActivity().getResources().getColor(R.color.color_ffffff));
        this.mUnitWeNoticeAdapter = new UnitWeNoticeAdapter(getActivity());
        this.mLsvWeNotice.setAdapter(this.mUnitWeNoticeAdapter);
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.unitId = bundle.getLong(KEY_NOTIFY_PUBLISH_UNITID, 0);
            requestMyUnitNotice(unitId);
            this.isAdmin = UnitDao.getInstance().isAdminForMe(this.unitId);
        }
        if(this.isAdmin){
            super.mTitleAction.setActivityTopRightTxvVisibility(View.VISIBLE);
        }else{
            super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        }
    }
    /**
     * 请求企业公告
     * @param unitId
     */
    private void requestMyUnitNotice(long unitId){
        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
            if(ResourceHelper.isNotNull(urlEntity)){
                String mAccessToken = CurrentUser.getInstance().getToken();
                String url = ProjectHelper.fillRequestURL(urlEntity.getEnterpriseGetNotice());
                UnitNoticeJsonEntity jsonUnit = new UnitNoticeJsonEntity();
                jsonUnit.setEnterpriseId(unitId);
                String json = JsonHelper.toJson(jsonUnit, UnitNoticeJsonEntity.class);
                ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_ENTERPRISE_MEUNIT_ANNOUNCEMENT_LIST, url, mAccessToken, json, this);
            }
        }
    }

}

package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.UserAppCenterEntity;
import com.maya.android.vcard.ui.adapter.UserAppCenterAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的：应用中心
 */
public class UserAppCenterFragment extends BaseFragment implements UserAppCenterAdapter.UserAppCenterListener {

    private ListView mLsvAppCenter;
    private TextView mTxvLoading;
    private UserAppCenterAdapter mAppCenterAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_app_center, container, false);
        this.mLsvAppCenter = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvLoading = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvAppCenter.setEmptyView(this.mTxvLoading);
        this.mLsvAppCenter.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.user_app_center, true);
        this.mAppCenterAdapter = new UserAppCenterAdapter(getActivity());
        this.mLsvAppCenter.setAdapter(this.mAppCenterAdapter);
        this.mAppCenterAdapter.setUserAppCenterListener(this);
        this.mAppCenterAdapter.addItems(getArrayList());
     }

    @Override
    public void toGetOrDelApp(int position, int operateResId) {
        switch (operateResId){
            case R.mipmap.img_app_uninstall:
                //TODO 删除应用
                break;
            case R.mipmap.img_app_download:
                //TODO 下载应用
                break;
        }
    }

    /**
     * 应用点击事件
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //TODO 点击事件
        }
    };

    /**
     * 获取应用app
     * @return
     */
    private List<UserAppCenterEntity> getArrayList(){
        List<UserAppCenterEntity> appCenters = new ArrayList<UserAppCenterEntity>();
        UserAppCenterEntity item = new UserAppCenterEntity();
        appCenters.add(getItem(item, ResourceHelper.getString(R.string.card_make_center), ResourceHelper.getString(R.string.card_make_center_content), R.mipmap.img_app_card, R.mipmap.img_app_card_gray, R.mipmap.img_app_uninstall, ""));
        appCenters.add(getItem(item, ResourceHelper.getString(R.string.album_make_center), ResourceHelper.getString(R.string.album_make_center_content), R.mipmap.img_app_album, R.mipmap.img_app_album_gray, R.mipmap.img_app_uninstall, ""));
        appCenters.add(getItem(item, ResourceHelper.getString(R.string.private_space), ResourceHelper.getString(R.string.private_space_content), R.mipmap.img_app_private, R.mipmap.img_app_private_gray, R.mipmap.img_app_download, ""));
        appCenters.add(getItem(item, ResourceHelper.getString(R.string.memo_remind), ResourceHelper.getString(R.string.memo_remind_content), R.mipmap.img_app_memo, R.mipmap.img_app_memo_gray, R.mipmap.img_app_download, ""));
        appCenters.add(getItem(item, ResourceHelper.getString(R.string.sign_luck_draw), ResourceHelper.getString(R.string.sign_luck_draw_content), R.mipmap.img_app_sign, R.mipmap.img_app_sign_gray, R.mipmap.img_app_download, ""));
        return appCenters;
    }

    /**
     * 克隆实体类
     * @param userApp
     * @param name
     * @param description
     * @param iconId
     * @param operateResId
     * @param url
     * @return
     */
    private UserAppCenterEntity getItem(UserAppCenterEntity userApp,String name,String description,int iconId,int iconGayId, int operateResId, String url){
        UserAppCenterEntity item = null;
        try {
            item = (UserAppCenterEntity) userApp.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if(ResourceHelper.isNull(item)){
            item = new UserAppCenterEntity();
        }
        item.setName(name);
        item.setDescription(description);
        item.setIconId(iconId);
        item.setOperateResId(operateResId);
        item.setIconGayId(iconGayId);
        item.setUrl(url);
        return item;
    }


}

package com.maya.android.vcard.ui.frg;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.ManagerChatEntity;
import com.maya.android.vcard.ui.adapter.ManagerChatAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.Images;

import java.util.ArrayList;

/**
 * 设置：聊天纪录管理
 */
public class SettingManagerChatFragment extends BaseFragment implements ManagerChatAdapter.ManagerChatListener {
    private CheckBox mChbSelected;
    private Button mBtnDelete;
    private ListView mLsvManagerChat;
    private TextView mTxvEmpty;
    private ManagerChatAdapter mManagerChatAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_delete:
                    //TODO 删除服务器数据
                    mManagerChatAdapter.deleteSelected();
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.chb_is_selected:
                    if(isChecked){
                        mManagerChatAdapter.selectedAll();
                    }else{
                        mManagerChatAdapter.cancelSelectedAll();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_manager_chat, container, false);
        this.mChbSelected = (CheckBox) view.findViewById(R.id.chb_is_selected);
        this.mBtnDelete = (Button) view.findViewById(R.id.btn_delete);
        this.mLsvManagerChat = (ListView) view.findViewById(R.id.lsv_list);
        this.mLsvManagerChat.setBackgroundColor(Color.WHITE);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mLsvManagerChat.setEmptyView(this.mTxvEmpty);
        this.mBtnDelete.setOnClickListener(this.mOnClickListener);
        this.mChbSelected.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.frg_setting_notice_manage_chat_log, false);
        this.mManagerChatAdapter = new ManagerChatAdapter(getActivity());
        this.mLsvManagerChat.setAdapter(this.mManagerChatAdapter);
        this.mManagerChatAdapter.setManagerChatListener(this);
        this.mManagerChatAdapter.addItems(getArrayList());
        ButtonHelper.setButtonEnable(this.mBtnDelete, R.color.color_787878, false);
    }

    @Override
    public void toSelectedNum(int count) {
        if(count > 0){
            ButtonHelper.setButtonEnable(this.mBtnDelete, R.color.color_399c2f, true);
        }else{
            ButtonHelper.setButtonEnable(this.mBtnDelete, R.color.color_787878, false);
        }
    }

    /**
     * 测试数据
     * @return
     */
    private ArrayList<ManagerChatEntity> getArrayList(){
        ArrayList<ManagerChatEntity> items = new ArrayList<ManagerChatEntity>();
        for(int i = 0; i < 20; i++){
            ManagerChatEntity item = new ManagerChatEntity();
            item.setDisplayName("单身狗" + i);
            item.setHeadImg(Images.imageThumbUrls[i]);
            item.setSize(i + "kb");
            items.add(item);
        }
        return items;
    }
}

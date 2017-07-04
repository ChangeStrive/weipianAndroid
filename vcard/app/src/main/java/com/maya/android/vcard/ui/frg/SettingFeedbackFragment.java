package com.maya.android.vcard.ui.frg;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.adapter.FeedBackAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;

import java.util.List;

/**
 * 设置：反馈
 * A simple {@link Fragment} subclass.
 */
public class SettingFeedbackFragment extends BaseFragment {
    private FeedbackAgent agent;
    private Conversation mConversation;
    private UserInfo info;
    private EditText mEdtFeedback;
    private ListView mLsvFeedback;
    private TextView mTxvEmpty;
    private FeedBackAdapter mFeedBackAdapter;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send_feedback:
                    sendFeedback();
                    break;
            }
        }
    };

    public SettingFeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_feedback, container, false);
        this.mEdtFeedback = (EditText) view.findViewById(R.id.edt_enter_feedback);
        Button mBtnSendFeedback = (Button) view.findViewById(R.id.btn_send_feedback);
        this.mLsvFeedback = (ListView) view.findViewById(R.id.lsv_feedback);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_empty);
        mBtnSendFeedback.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_feedback, true);
        this.mLsvFeedback.setSelected(true);
        this.mLsvFeedback.setEmptyView(this.mTxvEmpty);
        this.mFeedBackAdapter = new FeedBackAdapter(getActivity());
        this.mLsvFeedback.setAdapter(this.mFeedBackAdapter);
        try {
            this.agent = new FeedbackAgent(getActivity());
            this.mConversation = agent.getDefaultConversation();
            this.info = agent.getUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sync();
    }

    /**
     * 发送意见反馈
     */
    private void sendFeedback(){
        String mFeedback = this.mEdtFeedback.getEditableText().toString();
        this.mEdtFeedback.getEditableText().clear();
        if(Helper.isNotEmpty(mFeedback)) {
            mConversation.addUserReply(mFeedback);
            sync();
        }else{
            ActivityHelper.showToast(R.string.please_enter_want_feedback);
        }
    }

    /**
     * 数据同步
     */
    private void sync() {
        mConversation.sync(new SyncListener() {
            @Override
            public void onSendUserReply(List<Reply> arg0) {

            }
             @Override
            public void onReceiveDevReply(List<Reply> arg0) {
                 mFeedBackAdapter.addItems(mConversation.getReplyList());
                 mLsvFeedback.setSelection(mFeedBackAdapter.getCount() -1 );
            }
        });
     }
}

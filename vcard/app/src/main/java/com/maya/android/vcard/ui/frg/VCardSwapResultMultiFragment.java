package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.result.SwapCardResultEntity;
import com.maya.android.vcard.ui.adapter.VCardSwapAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.Images;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 交换结果：多个
 */
public class VCardSwapResultMultiFragment extends BaseFragment {
    private ListView mLsvSwapMember;
    private TextView mTxvEmpty, mTxvChar;
    private Button mBtnSubmit;
    private VCardSwapAdapter mVCardSwapAdapter;
    private long mVCardId = 0;
    private int mSwapWay = Constants.SwapCard.SWAP_CARD_WAY_TO_PADDLE;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                //TODO 交换名片
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             mVCardSwapAdapter.setSelect(position);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcard_swap_result_multi, container, false);
        this.mLsvSwapMember = (ListView) view.findViewById(R.id.lsv_list);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_lsv_empty);
        this.mTxvChar = (TextView) view.findViewById(R.id.txv_char);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mLsvSwapMember.setOnItemClickListener(this.mOnItemClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mVCardSwapAdapter = new VCardSwapAdapter(getActivity());
        this.mLsvSwapMember.setAdapter(this.mVCardSwapAdapter);
        //测试
        //this.mVCardSwapAdapter.addItems(getArrayList());
        this.initData();
    }

    private void initData(){
        Bundle args = getArguments();
        String swapVCardListStr = null;
        if(Helper.isNotNull(args)){
            this.mVCardId = args.getLong(Constants.IntentSet.INTENT_KEY_VCARD_ID, 0);
            this.mSwapWay = args.getInt(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_WAY, Constants.SwapCard.SWAP_CARD_WAY_TO_PADDLE);
            swapVCardListStr = args.getString(Constants.IntentSet.INTENT_KEY_VCARD_SWAP_CARD_LIST, "");
        }
        if(ResourceHelper.isNotEmpty(swapVCardListStr)){
            HashMap<Long, SwapCardResultEntity> swapCardMap = JsonHelper.fromJson(swapVCardListStr,
                    new TypeToken<HashMap<Long, SwapCardResultEntity>>() {}.getType());
            if(Helper.isNotNull(swapCardMap)){
                this.mVCardSwapAdapter.addItems(new ArrayList<SwapCardResultEntity>(swapCardMap.values()));
            }
        }
    }

    /**
     * 测试数据
     * @return
     */
    private ArrayList<SwapCardResultEntity> getArrayList(){
        ArrayList<SwapCardResultEntity> swapCards = new ArrayList<SwapCardResultEntity>();
        for(int i = 0; i < 10; i++){
            SwapCardResultEntity swapCard = new SwapCardResultEntity();
            swapCard.setSurname("萌");
            swapCard.setFirstName("萌" + i);
            swapCard.setHeadImg(Images.imageThumbUrls[i]);
            swapCard.setJob("打工仔");
            swapCard.setCompanyName("玛雅" + i);
            swapCards.add(swapCard);
        }
        return swapCards;
    }
}

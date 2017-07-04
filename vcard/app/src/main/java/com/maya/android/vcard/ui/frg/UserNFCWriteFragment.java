package com.maya.android.vcard.ui.frg;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.UserMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.impl.OnNewIntentImpl;
import com.maya.android.vcard.util.NFCHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 我的：NFC名片写入
 */
public class UserNFCWriteFragment extends BaseFragment implements OnNewIntentImpl{
    private static final String TAG = UserNFCWriteFragment.class.getSimpleName();
    private TextView mTxvNFCName, mTxvNFCCompany, mTxvNFCPosition;
    private AsyncImageView mImvHead;
    private TextView mTxvName, mTxvCompany, mTxvPosition, mTxvAddress, mTxvUrl, mTxvMobile, mTxvTelePhone, mTxvFax, mTxvEmail;
    private CardEntity mCardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
    private String disPlayName;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((UserMainActivity) activity).setOnNewIntentImpl(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement UserMainActivity");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initNfc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_nfcwrite, container, false);
        this.mTxvNFCName = (TextView) view.findViewById(R.id.txv_nfc_card_write_name);
        this.mTxvNFCCompany = (TextView) view.findViewById(R.id.txv_nfc_card_write_job);
        this.mTxvNFCPosition = (TextView) view.findViewById(R.id.txv_nfc_card_write_company);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_nfc_card_write_imvhead);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_position);
        this.mTxvPosition = (TextView) view.findViewById(R.id.txv_company);
        this.mTxvAddress = (TextView) view.findViewById(R.id.txv_address);
        this.mTxvUrl = (TextView) view.findViewById(R.id.txv_url);
        this.mTxvMobile = (TextView) view.findViewById(R.id.txv_mobile);
        this.mTxvTelePhone = (TextView) view.findViewById(R.id.txv_telephone);
        this.mTxvFax = (TextView) view.findViewById(R.id.txv_fax);
        this.mTxvEmail = (TextView) view.findViewById(R.id.txv_email);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.nfc_edt_vcard, false);

        UserInfoResultEntity userInfo = CurrentUser.getInstance().getUserInfoEntity();
        this.fillNFCData(userInfo, this.mCardEntity);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            NFCHelper.writeInNFC(this.mCardEntity, this.disPlayName, intent);
            LogHelper.d(TAG, "发现NFC");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mNfcAdapter.enableForegroundDispatch(this.getActivity(), this.mNfcPendingIntent, this.mNdefExchangeFilters, null);
        this.mNfcAdapter.enableForegroundDispatch(this.getActivity(), this.mNfcPendingIntent, this.mWriteTagFilters, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mNfcAdapter.disableForegroundNdefPush(getActivity());
        this.mNfcAdapter.disableForegroundDispatch(getActivity());
    }

    NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters, mWriteTagFilters;
    @SuppressLint("NewApi")
    private void initNfc(){
        this.mNfcAdapter = NFCHelper.getNfcAdapter();
        Intent localIntent1 = new Intent(getActivity(), getActivity().getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        this.mNfcPendingIntent = PendingIntent.getActivity(getActivity(), 0, localIntent1, 0);
        this.mNdefExchangeFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        this.mWriteTagFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
    }
    /**
     * 填充刷新数据
     * @param userInfo
     * @param cardEntity
     */
    private void fillNFCData(UserInfoResultEntity userInfo, CardEntity cardEntity){
        if(ResourceHelper.isNotNull(userInfo)){
            this.disPlayName = userInfo.getDisplayName();
            this.mTxvNFCName.setText(this.disPlayName);
            this.mTxvName.setText(userInfo.getDisplayName());
            this.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
            ResourceHelper.asyncImageViewFillUrl(this.mImvHead, ResourceHelper.getImageUrlOnIndex(userInfo.getHeadImg(), 0));
            this.mTxvAddress.setText(userInfo.getNativeplace());
        }

        if(ResourceHelper.isNotNull(cardEntity)){
            this.mTxvNFCCompany.setText(cardEntity.getCompanyName());
            this.mTxvCompany.setText(cardEntity.getCompanyName());
            this.mTxvNFCPosition.setText(cardEntity.getJob());
            this.mTxvPosition.setText(cardEntity.getJob());
            this.mTxvUrl.setText(cardEntity.getCompanyHome());
            this.mTxvMobile.setText(cardEntity.getMobileTelphone());
            this.mTxvTelePhone.setText(cardEntity.getLineTelphone());
            this.mTxvFax.setText(cardEntity.getFax());
            this.mTxvEmail.setText(cardEntity.getEmail());
        }
    }


}

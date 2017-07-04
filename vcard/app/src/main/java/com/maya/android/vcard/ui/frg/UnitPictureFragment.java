package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;

/**
 * 单位：云画册
 */
public class UnitPictureFragment extends BaseFragment {
    private Button mBtnAlbumSubmit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_album_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnAlbumSubmit);
                    // 创建一本新的云画册
                    ActivityHelper.showToast(R.string.the_feature_is_not_yet_open);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_picture, container, false);
        this.mBtnAlbumSubmit = (Button) view.findViewById(R.id.btn_album_submit);
        this.mBtnAlbumSubmit.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.picture_album, false);
    }
}

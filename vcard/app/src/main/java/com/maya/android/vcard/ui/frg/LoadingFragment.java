package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.base.BaseFragment;

/**
 * 过渡页
 */
public class LoadingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

}

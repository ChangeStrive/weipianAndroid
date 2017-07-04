package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * Created by YongJi on 2015/8/28.
 */
public class MultiMainFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<String> mItems = new ArrayList<String>();
    private Context mContext;
    public MultiMainFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        String frgName = this.mItems.get(position);
        if(ResourceHelper.isNotEmpty(frgName) && !"".equals(frgName)){
            result = Fragment.instantiate(this.mContext, frgName, null);
        }
        return result;
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    public boolean addItems(ArrayList<String> items){
        boolean result = false;
        if(Helper.isNotNull(items)) {
            result = this.mItems.addAll(items);
            this.notifyDataSetChanged();
        }
        return result;
    }
}

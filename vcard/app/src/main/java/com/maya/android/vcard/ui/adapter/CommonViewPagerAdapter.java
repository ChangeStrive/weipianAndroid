package com.maya.android.vcard.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 通用 ViewPagerAdapter
 * */
public class CommonViewPagerAdapter extends PagerAdapter {

	private ArrayList<View> mItems = new ArrayList<View>();
	
	@Override
	public int getCount() {
		return this.mItems.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(this.mItems.get(position), 0);
		return this.mItems.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(this.mItems.get(position));
	}

	/**
	 * 添加ItemList
	 * @param items
	 * @return
	 */
	public boolean addItemList(ArrayList<View> items){
		boolean result = this.mItems.addAll(items);
		this.notifyDataSetChanged();
		return result;
	}

}

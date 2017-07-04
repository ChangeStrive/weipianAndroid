package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.ContactGroupEntity;

import java.util.ArrayList;
/**
 * Adapter：名片夹左侧列表
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-18
 *
 */
public class CardcaseGroupsAdapter extends BaseAdapter {

	private ArrayList<ContactGroupEntity> mItems = new ArrayList<ContactGroupEntity>();
	private Context mContext;
	/** 选中的位置(默认未选中) **/
	private int mSelectedPosition = -1;
	private String mEncodePwd;
	private int mUnSelectedTextColor;
	private int mUnSelectedBackgroundColor;
	private int mSelectedTextColor;
	private PreferencesHelper mPreference = PreferencesHelper.getInstance();
	
	public CardcaseGroupsAdapter(Context context){
		this.mContext = context;
		mEncodePwd = this.mPreference.getString(Constants.Preferences.KEY_CARDCASE_COLLECT_LOCKED_PWD, "");
		Resources resources = this.mContext.getResources();
		this.mUnSelectedTextColor = resources.getColor(R.color.color_35466c);
		this.mSelectedTextColor = resources.getColor(R.color.color_ffffff);
		this.mUnSelectedBackgroundColor = resources.getColor(R.color.color_ffffff);
	}
	@Override
	public int getCount() {
		return this.mItems.size() + 1;
	}

	@Override
	public ContactGroupEntity getItem(int position) {
		return this.mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position != getCount() - 1){
			ViewHolder holder;
			if(Helper.isNull(convertView) || Helper.isNull(convertView.getTag())){
				convertView = LayoutInflater.from(this.mContext).inflate(
						R.layout.item_lsv_cardcase_group, null);
				holder = new ViewHolder();
				holder.relItem=(RelativeLayout) convertView.findViewById(R.id.rel_item_act_cardcase_group);
				holder.txvName = (TextView) convertView.findViewById(R.id.txv_item_cardcase_group);
				holder.imvLock=(ImageView) convertView.findViewById(R.id.imv_item_cardcase_lock);
				holder.txvNum = (TextView) convertView.findViewById(R.id.txv_item_cardcase_group_num);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			ContactGroupEntity entity = this.getItem(position);
			String groupName = entity.getName().trim();
			holder.txvName.setText(groupName);
			holder.txvNum.setText( "(" + entity.getItemCount() + ")");

			if (mSelectedPosition == position) {
				holder.relItem.setBackgroundResource(R.drawable.bg_cardcase_group_select);
				holder.txvName.setTextColor(this.mSelectedTextColor);
				holder.txvNum.setTextColor(this.mSelectedTextColor);
			}else{
				holder.relItem.setBackgroundColor(this.mUnSelectedBackgroundColor);
				holder.txvName.setTextColor(this.mUnSelectedTextColor);
				holder.txvNum.setTextColor(this.mUnSelectedTextColor);
			}
			// 名片夹收藏夹加密图标
				if(entity.getId() == ContactGroupEntity.GROUP_FAVORITE_ID
						&& !"".equals(mEncodePwd)){
					holder.imvLock.setVisibility(View.VISIBLE);
				}else{
					holder.imvLock.setVisibility(View.GONE);
				}
		}else{
			convertView = LayoutInflater.from(this.mContext).inflate(
					R.layout.item_lsv_cardcase_group_add, null);
		}
		return convertView;
	}
	/**
	 * 添加内容
	 * @param items
	 * @return
	 */
	public boolean addItems(ArrayList<ContactGroupEntity> items){
		boolean result = false;
		if(Helper.isNotNull(items)){
			this.clearItemsData();
			result = this.mItems.addAll(items);
			this.notifyDataSetChanged();
		}
		return result;
	}
	/**
	 * 添加内容
	 * @param item
	 * @return
	 */
	public boolean addItem(ContactGroupEntity item){
		boolean result = false;
		if(Helper.isNotNull(item)){
			int position = this.getCount() - 3;
			this.mItems.add(position, item);
			this.notifyDataSetChanged();
		}
		return result;
	}
	/**
	 * 设置被选中的项
	 * @param position
	 */
	public void setSelectedPosition(int position){
		this.mSelectedPosition = position;
		this.notifyDataSetChanged();
	}
	/**
	 * 取得所有数据
	 * @return
	 */
	public ArrayList<ContactGroupEntity> getAllItems(){
		return this.mItems;
	}
	/**
	 * 清楚数据
	 */
	public void clearItemsData(){
		this.mItems.clear();
	}
	
	private class ViewHolder{
		public ImageView imvLock;
		public RelativeLayout relItem;
		public TextView txvName;
		public TextView txvNum;
	}

}

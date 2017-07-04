package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.result.VCardTemplateClassResultEntity;
import com.maya.android.vcard.entity.result.VCardTemplateResultEntity;
import com.maya.android.vcard.ui.impl.VCardTemplateItemOperateImpl;
import com.maya.android.vcard.ui.widget.NoScrolGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 线上模板listview 数据源
 * @author zheng_cz
 * @since 2014年7月28日 下午6:09:52
 */
public class CardTemplateOnlineListAdapter extends BaseAdapter {
	private Context mContext;
	private int mColumnWidth, mHSpacing;
	private VCardTemplateItemOperateImpl mItemListener;
	private ArrayList<VCardTemplateClassResultEntity> mItemList = new ArrayList<VCardTemplateClassResultEntity>();
	
	public CardTemplateOnlineListAdapter(Context context, VCardTemplateItemOperateImpl itemListener){
		mContext = context;
		mItemListener = itemListener;
//    	mColumnWidth = mContext.getResources().getDimensionPixelSize(R.dimen.act_card_template_grv_item_width);
//    	mHSpacing = mContext.getResources().getDimensionPixelSize(R.dimen.act_card_template_grv_padding_left);
	}
	
	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public VCardTemplateClassResultEntity getItem(int position) {
		if(position < getCount()){
			return mItemList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		VCardTemplateClassResultEntity item = getItem(position);
		if(Helper.isNotNull(item)){
			return item.getClassId();
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(Helper.isNull(convertView)){
			//TODO getView 缺失
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lsv_act_card_template_online, null);
//			vh = new ViewHolder();
//			vh.mTxvClassName = (TextView) convertView.findViewById(R.id.txv_item_act_card_template_class);
//			vh.mGrvItems = (NoScrolGridView) convertView.findViewById(R.id.grv_act_card_template_online);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.mGridAdapter = new CardTemplateGridviewAdapter(mContext);
		vh.mGrvItems.setAdapter(vh.mGridAdapter);
		VCardTemplateClassResultEntity tempClassEntity = getItem(position);
		if(Helper.isNotNull(tempClassEntity)){
			vh.mTxvClassName.setText(tempClassEntity.getClassName());
			ArrayList<VCardTemplateResultEntity> templateList = tempClassEntity.getTemplateList();
			if(Helper.isNotNull(templateList)){
				int tempSize = templateList.size();
				LayoutParams params = new LayoutParams(tempSize * (mColumnWidth + mHSpacing), LayoutParams.WRAP_CONTENT);
				vh.mGrvItems.setLayoutParams(params);
				vh.mGrvItems.setNumColumns(tempSize);
				vh.mGridAdapter.addItems(templateList);
			}
			GridOnItemClickListener itemClickListener = new GridOnItemClickListener(vh.mGridAdapter);
			vh.mGrvItems.setOnItemClickListener(itemClickListener);
			vh.mGrvItems.setOnItemLongClickListener(itemClickListener);
		}
		return convertView;
	}
	/**
	 * 添加列表项
	 * @param itemList
	 * @param isAppend 是否追加数据
	 */
	public void addItems(List<VCardTemplateClassResultEntity> itemList, boolean isAppend){
		if(!isAppend){
			mItemList.clear();
		}
		if(Helper.isNotNull(itemList)){
			mItemList.addAll(itemList);
		}
		notifyDataSetChanged();
	}
	public class ViewHolder{
		public TextView mTxvClassName;
		public NoScrolGridView mGrvItems;
		public CardTemplateGridviewAdapter mGridAdapter;
	}
	/**
	 * girdview 项单击 ，长按监听
	 * @author zheng_cz
	 * @since 2014年7月29日 上午9:57:27
	 */
	private class GridOnItemClickListener implements OnItemClickListener, OnItemLongClickListener{
		private CardTemplateGridviewAdapter mGridAdapter;
		public GridOnItemClickListener(CardTemplateGridviewAdapter adapter){
			mGridAdapter = adapter;
		}
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if(Helper.isNotNull(mItemListener)){
				mItemListener.onItemLongClick(position, mGridAdapter);
			}
			return true;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(Helper.isNotNull(mItemListener)){
				mItemListener.onItemClick(position, mGridAdapter);
			}
				
		}
	}
}

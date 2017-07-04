package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
/**
 * Adapter:联系人详细信息头像
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-22
 *
 */
public class ImageHeadAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> mItems = new ArrayList<String>();
	private int mImvCorner = -1;
	private boolean isEditState = false;
	private int mImgWidth = 0;
	private String mVcardNo;
	
	public ImageHeadAdapter(Context context){
		this.mContext = context;
		this.mImgWidth = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_58dp);
	}
	
	@Override
	public int getCount() {
		return this.mItems.size();
	}

	@Override
	public String getItem(int position) {
		String mItem = mItems.get(position);
		if(Helper.isNotEmpty(mItem)){
			return mItem;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AsyncImageView picturesView;
		if (Helper.isNull(convertView)) {
			picturesView = new AsyncImageView(this.mContext);
			picturesView.setRoundCorner(this.mImvCorner);
			GridView.LayoutParams lp = new GridView.LayoutParams(this.mImgWidth, this.mImgWidth);
			picturesView.setLayoutParams(lp);
			picturesView.setScaleType(ScaleType.FIT_XY);
			convertView = picturesView;
		} else {
			picturesView = (AsyncImageView) convertView;
		}
		String headPath = getItem(position);
		if(this.isEditState){
			// 编辑头像展示
			if (position == getCount() - 1 && ResourceHelper.isEmpty(headPath)) {
				picturesView.setDefaultImageResId(R.mipmap.img_add_frame);
			} else {
				picturesView.setDefaultImageResId(R.mipmap.ic_launcher);
				ResourceHelper.asyncImageViewFillUrl(picturesView, headPath);
			}
		}else{
			// 查看头像展示
			if(this.mVcardNo.equals("10000000")){
				picturesView.setDefaultImageResId(R.mipmap.img_user_head_mytip);
			}else{
				picturesView.setDefaultImageResId(R.mipmap.img_upload_head);
				ResourceHelper.asyncImageViewFillUrl(picturesView, headPath);
			}
		}
		return convertView;
	}
	
	public void setEditState(boolean isEditState){
		this.isEditState = isEditState;
	}
	
	public void setImageCorner(int corner){
		this.mImvCorner = corner;
	}

	public void setVCardNo(String vcardNo){
		this.mVcardNo = vcardNo;
	}
	
	public boolean addItems(ArrayList<String> items){
		boolean result = false;
		this.mItems.clear();
		
		if(Helper.isNotNull(items) && this.mItems.size() <= 5){
			result = this.mItems.addAll(items);
		}
		if(isEditState && this.mItems.size() < 5){
			this.mItems.add("");
		}
		
		notifyDataSetChanged();
		return result;
	}
	/**
	 * 添加新路径到第一位
	 * @param newHeadUrl
	 */
	public void addItem(int position, String newHeadUrl){
		if(position == 4){
			this.mItems.remove(this.mItems.size() - 1);
		}
		this.mItems.add(position, newHeadUrl);
		notifyDataSetChanged();
	}
	/**
	 * 移除成员
	 * @param headPath
	 */
	public void removeItem(String headPath){
		this.mItems.remove(headPath);
		int size = getCount();
		if(size == 0 || (size < 5 && Helper.isNotEmpty(getItem(size - 1)))){
			this.mItems.add("");
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 选择当前头像 放到第一位
	 * @param headPath
	 */
	public void selectItem(String headPath){
		this.mItems.remove(headPath);
		this.mItems.add(0, headPath);
		notifyDataSetChanged();
	}
}

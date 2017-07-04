package com.maya.android.vcard.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.entity.result.VCardTemplateResultEntity;
import com.maya.android.vcard.util.ResourceHelper;
/**
 * 本地模板适配器
 * @author Administrator
 *
 */
public class CardTemplateGridviewAdapter extends BaseAdapter {

	private ArrayList<VCardTemplateResultEntity> mItems = new ArrayList<VCardTemplateResultEntity>();
	private Context mContext;
	private int mImvWidth, mImvHeight;
	
	public CardTemplateGridviewAdapter(Context context){
		this.mContext = context;
		this.mImvWidth = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_100dp);
    	this.mImvHeight = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_167dp);
	}
	
	@Override
	public int getCount() {
		return this.mItems.size();
	}

	@Override
	public VCardTemplateResultEntity getItem(int position) {
		return this.mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		VCardTemplateResultEntity entity = this.getItem(position);
		if(Helper.isNotNull(entity)){
			return entity.getClassId();
		}else{
			return position;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewHolder holder;
        if(convertView ==null){
            holder = new GridViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grv_lsv_vcard_template, null);
            holder.mImvTemp = (AsyncImageView) convertView.findViewById(R.id.imv_item_grv_act_card_template_card);
            holder.mTxvName = (TextView) convertView.findViewById(R.id.txv_item_grv_act_card_template_name);
            LayoutParams imvLp = holder.mImvTemp.getLayoutParams();
            imvLp.height = mImvHeight;
            imvLp.width = mImvWidth;
            holder.mImvTemp.setLayoutParams(imvLp);
            holder.mImvTemp.setScaleType(ScaleType.FIT_XY);
            convertView.setTag(holder);
        }else{
            holder = (GridViewHolder) convertView.getTag();
        }
        VCardTemplateResultEntity entity = this.getItem(position);
        if(Helper.isNotNull(entity)){
        	String templateName = getVCardTemplateName(entity);
	        holder.mTxvName.setText(templateName);
	        holder.mTxvName.setVisibility(View.GONE);
	        //TODO 图片异步修改
//        	holder.mImvTemp.setRotate(0);
	        holder.mImvTemp.setPortrait(true);
	        
	        holder.mImvTemp.setDefaultImageResId(R.mipmap.img_loading_template);
	        String showImage = entity.getShowImage();
	        if(showImage.contains(Constants.Other.CONTENT_ARRAY_SPLIT)){
	        	String[] arr = showImage.split(Constants.Other.CONTENT_ARRAY_SPLIT);
	        	if(Helper.isNotNull(arr[0])){
	        		showImage = arr[0];
	        	}
	        }
	        if(Helper.isNotEmpty(showImage)){
	        	if(!showImage.startsWith(Constants.ImagePathSign.VCARD_IMAGE_PATH_SIGN_UPLOAD)){
	        		showImage = Constants.PATH.PATH_LOCAL_VCARD_TEMPLATE.concat(showImage);
	        	}
//	        	holder.mImvTemp.setBitmapMaxWidthAndBitmapMaxHeight(mImvWidth, mImvHeight);
//	        	ProjectHelper.asyncImageViewFillUrl(holder.mImvTemp, showImage);
	        	holder.mImvTemp.autoFillFromUrl(ResourceHelper.getHttpImageFullUrl(showImage));
//	        	if(showImage.startsWith(Constant.ImagePathSign.VCARD_IMAGE_PATH_SIGN_UPLOAD)){
////	    	        if(!entity.isVertical()){
////	    	        	//TODO 图片异步修改
////	    	        	// 横向名片则旋转显示
//////	    	        	holder.mImvTemp.setRotate(90);
////	    	        	holder.mImvTemp.setPortrait(true);
////	    	        }
//			        ProjectHelper.asyncImageViewFillUrl(holder.mImvTemp, showImage);
//	        	}else{
//	        		showImage = Constant.PATH.PATH_LOCAL_VCARD_TEMPLATE.concat(showImage);
//	        		AsyncLocalImageManager.fillLocalUrl2ImageView(holder.mImvTemp, showImage, mImvWidth, mImvHeight, true);
//	        	}
	        }
	        
        }
		return convertView;
	}
	/**
	 * 添加内容
	 * @param items
	 * @return
	 */
	public boolean addItems(ArrayList<VCardTemplateResultEntity> items){
		boolean result = false;
		this.mItems.clear();
		if(Helper.isNotNull(items)){
			result = this.mItems.addAll(items);
		}
		this.notifyDataSetChanged();
		return result;
	}
	/**
	 * 添加一个对象
	 * @param item
	 */
	public void addItem(VCardTemplateResultEntity item){
		this.mItems.add(item);
		this.notifyDataSetChanged();
	}
	/**
	 * 移除对象
	 * @param item
	 */
	public void removeItem(VCardTemplateResultEntity item){
		this.mItems.remove(item);
		this.notifyDataSetChanged();
	}
    private String getVCardTemplateName(VCardTemplateResultEntity entity){
		String templateName = null;
		float money = entity.getMoney();
		switch(entity.getType()){
		case DatabaseConstant.VCardTemplateType.VIP:
			templateName = mContext.getString(R.string.vip_template);
			break;
		case DatabaseConstant.VCardTemplateType.INTEGRAL:
			templateName = money + mContext.getString(R.string.integral);
			break;
		case DatabaseConstant.VCardTemplateType.SMALL_COINS:
			templateName = money + mContext.getString(R.string.wei_coins);
			break;
			default:
				templateName = mContext.getString(R.string.free_template);
				break;
		}
		return templateName;
    }
	
	private class GridViewHolder{
    	AsyncImageView mImvTemp;
    	TextView mTxvName;
    }

}

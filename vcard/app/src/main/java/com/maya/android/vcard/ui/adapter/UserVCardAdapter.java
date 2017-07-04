package com.maya.android.vcard.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 用户名片适配器
 * Created by Administrator on 2015/9/1.
 */
public class UserVCardAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<CardEntity> mCards = new ArrayList<CardEntity>();
    private int mImvWidth, mImvHeight;
    private int mCurrentPosition;
    private UserVCardRecommendListener mListener;

    public UserVCardAdapter(Context mContext){
        this.mContext = mContext;
        this.mImvWidth = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_64dp);
        this.mImvHeight = ResourceHelper.getDp2PxFromResouce(R.dimen.dimen_38dp);
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public CardEntity getItem(int position) {
        CardEntity mCard = mCards.get(position);
        if(ResourceHelper.isNotNull(mCard)){
            return mCard;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if(ResourceHelper.isNull(convertView)){
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lsv_user_vcard, null);
            mHolder = new ViewHolder();
            mHolder.mImvVcard = (AsyncImageView) convertView.findViewById(R.id.imv_vcard);
            mHolder.mBtnRecommend = (Button) convertView.findViewById(R.id.btn_recommend);
            mHolder.mTxvName = (TextView) convertView.findViewById(R.id.txv_card_name);
            mHolder.mTxvCurrent = (TextView) convertView.findViewById(R.id.txv_user_vcard_current);
            mHolder.mTxvSurplusNum = (TextView) convertView.findViewById(R.id.txv_surplus_num);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        CardEntity mCard = getItem(position);
        final int curPosition = position;
        mHolder.mBtnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ResourceHelper.isNotNull(mListener)){
                    mListener.recommend(curPosition);
                }
            }
        });
        //TODO 获取当前名片为第几张
        if(this.mCurrentPosition == position){
            mHolder.mTxvCurrent.setVisibility(View.VISIBLE);
        }else{
            mHolder.mTxvCurrent.setVisibility(View.GONE);
        }
        mHolder.mTxvName.setText(this.mContext.getString(R.string.card) + String.valueOf(position + 1));
        mHolder.mTxvSurplusNum.setText(this.mContext.getString(R.string.vcard_surplus) + mCard.getCardCount() + this.mContext.getString(R.string.frg_piece));
        mHolder.mImvVcard.setLoadingImage(null);
        int imageWidth = -1;
        int imageHeight = -1;
        if(mCard.getCardAOrientation() == Constants.Card.CARD_ORIENTATION_PORTRAIT ){
            //纵向
            imageHeight = this.mImvHeight;
            switch (mCard.getCardAForm()) {
                case Constants.Card.CARD_FORM_9054:
                    imageWidth = (int) (imageHeight * Constants.Card.CARD_RATIO_9054);
                    break;
                case Constants.Card.CARD_FORM_9045:
                    imageWidth = (int) (imageHeight * Constants.Card.CARD_RATIO_9045);
                    break;
                case Constants.Card.CARD_FORM_9094:
                    imageWidth = (int) (imageHeight * Constants.Card.CARD_RATIO_9094);
                    break;
            }
        }else{
            //横向
            imageWidth = this.mImvWidth;
            switch (mCard.getCardAForm()) {
                case Constants.Card.CARD_FORM_9054:
                    imageHeight = (int) (imageWidth / Constants.Card.CARD_RATIO_9054);
                    break;
                case Constants.Card.CARD_FORM_9045:
                    imageHeight = (int) (imageWidth / Constants.Card.CARD_RATIO_9045);
                    break;
                case Constants.Card.CARD_FORM_9094:
                    imageHeight = (int) (imageWidth / Constants.Card.CARD_RATIO_9094);
                    break;
            }
        }

        RelativeLayout.LayoutParams lpImvCard = (RelativeLayout.LayoutParams) mHolder.mImvVcard.getLayoutParams();
        lpImvCard.width = imageWidth;
        lpImvCard.height = imageHeight;
        mHolder.mImvVcard.setLayoutParams(lpImvCard);

        if(mCard.getId() == 1){
            mHolder.mImvVcard.setImageResource(R.mipmap.img_def_card_front_landscape);
        }else{
            ResourceHelper.asyncImageViewFillUrl(mHolder.mImvVcard, mCard.getCardImgA());
        }
        return convertView;
    }

    /**
     * 添加数据源
     * @param mCards
     * @return
     */
    public boolean addItems(ArrayList<CardEntity> mCards){
        boolean result = false;
        this.mCards.clear();
        if(Helper.isNotNull(mCards)){
            result = this.mCards.addAll(mCards);
            this.notifyDataSetChanged();
        }
        return result;
    }

    /**
     * 设置当前名片位置
     * @param position
     */
    public void setCurrentPosition(int position){
        this.mCurrentPosition = position;
        this.notifyDataSetChanged();
    }

    /**
     * 推荐按钮监听
     * @param mListener
     */
    public void setUserVCardRecommendListener(UserVCardRecommendListener mListener){
        if(ResourceHelper.isNotNull(mListener)){
            this.mListener = mListener;
        }

    }

    private class ViewHolder{
        private AsyncImageView mImvVcard;
        private Button mBtnRecommend;
        private TextView mTxvName, mTxvCurrent, mTxvSurplusNum;
    }

    public interface UserVCardRecommendListener{
        void recommend(int position);
    }
}

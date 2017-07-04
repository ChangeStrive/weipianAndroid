package com.maya.android.vcard.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.entity.TitleMoreMenuLsvIconEntity;
import com.maya.android.vcard.ui.adapter.TitleMoreMenuAdapter;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *  标题栏右侧更多菜单
 * Created by Administrator on 2015/9/14.
 */
public class TitleMoreMenuPopView extends PopupWindow{
    private Context mContext;
    private ListView mLsvItem;
    private TitleMoreMenuAdapter menuAdapter;
    private MoreMenuItemClickListener menuListener;
    /** 是否单击项后关闭弹出框 **/
    private boolean isItemClickDismiss = true;//默认是

    public TitleMoreMenuPopView(Context mContext){
        super(LayoutInflater.from(mContext).inflate(R.layout.pop_title_more_menu, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        this.mContext = mContext;
        init();
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources()));
        this.setOutsideTouchable(true);
    }

    private void init(){
        View view = super.getContentView();;
        this.mLsvItem = (ListView) view.findViewById(R.id.lsv_pop_right);
        this.mLsvItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        android.view.ViewGroup.LayoutParams lsvLp =  this.mLsvItem.getLayoutParams();
        lsvLp.width = (int) (ActivityHelper.getScreenWidth() * 0.48);
        this.mLsvItem.setLayoutParams(lsvLp);
        this.menuAdapter = new TitleMoreMenuAdapter(this.mContext);
        this.mLsvItem.setAdapter(this.menuAdapter);
        this.mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ResourceHelper.isNotNull(menuListener)) {
                    menuListener.onItemClick(menuAdapter.getItem(position));
                }
                if (isItemClickDismiss) {
                    TitleMoreMenuPopView.this.dismiss();
                }
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(TitleMoreMenuPopView.this.isShowing()){
                    TitleMoreMenuPopView.this.dismiss();
                }
                return false;
            }
        });
    }

    public ListView getListView(){
        return this.mLsvItem;
    }

    /**
     * 不能点击项
     * @param unEnablePositionLsv
     */
    public void setItemUnEnable(ArrayList<Integer> unEnablePositionLsv){
        this.menuAdapter.setUnEnableItems(unEnablePositionLsv);
    }
    /**
     * 设置数据源
     * @param menus
     */
    public void setItemLsv(ArrayList<TitleMoreMenuLsvIconEntity> menus){
        this.menuAdapter.addItems(menus);
    }

    /**
     * 设置数据源 和 不能点击项
     * @param unEnablePositionLsv
     */
    public void setLsvUnEnable(List<TitleMoreMenuLsvIconEntity> menus, ArrayList<Integer> unEnablePositionLsv){
        this.menuAdapter.setUnEnableItems(unEnablePositionLsv);
        this.menuAdapter.addItems(menus);
    }

    /**
     * 设置单击选项后是否关闭弹出层
     * @param isItemClickDismiss
     */
    public void setItemClickDismiss(boolean isItemClickDismiss){
        this.isItemClickDismiss = isItemClickDismiss;
    }

    /**
     * 设置单击项监听
     * @param menuItemListener
     */
    public void setItemClickListener(MoreMenuItemClickListener menuItemListener){
        this.menuListener = menuItemListener;
    }

    /**
     * 更多菜单单击监听
     * @author zheng_cz
     * @since 2014年4月12日 下午3:18:16
     */
    public interface MoreMenuItemClickListener{
         void onItemClick(TitleMoreMenuLsvIconEntity menu);
    }

}

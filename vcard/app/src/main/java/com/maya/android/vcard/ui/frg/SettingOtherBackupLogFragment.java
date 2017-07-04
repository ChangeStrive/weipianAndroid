package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.BackupLogDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.entity.BackupLogSaveEntity;
import com.maya.android.vcard.entity.result.BackupLogListResultEntity;
import com.maya.android.vcard.entity.result.BackupLogResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.adapter.BackLogElvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import net.tsz.afinal.core.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 设置：其他：备份：日志
 */
public class SettingOtherBackupLogFragment extends BaseFragment {

    private ExpandableListView mElvLogs;
    private TextView mTxvEmpty;
    private BackLogElvAdapter mBackLogAdapter;

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            switch (tag){
                case Constants.CommandRequestTag.CMD_REQUEST_CLOUD_BACKUP_LOG:
                    String listJson = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_SAVE_CURRENT_USER_BACKUP_LOG, "");
                    String refreshListJson = commandResult.toString();
                    if(! listJson.equals(refreshListJson)){
                        this.mBackLogAdapter.addChildItems(getSparseArray(refreshListJson));
                        PreferencesHelper.getInstance().putString(Constants.Preferences.KEY_SAVE_CURRENT_USER_BACKUP_LOG, refreshListJson);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_other_backup_log, container, false);
        this.mElvLogs = (ExpandableListView) view.findViewById(R.id.elv_backup_log);
        //去掉箭头图标
        this.mElvLogs.setGroupIndicator(null);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_backup_log_empty);
         return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTitle(R.string.frg_setting_backup_log_backup, false);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.getActivityTopRightTxv().setVisibility(View.GONE);
        this.mElvLogs.setEmptyView(this.mTxvEmpty);
        this.mBackLogAdapter = new BackLogElvAdapter(getActivity());
        this.mElvLogs.setAdapter(this.mBackLogAdapter);
        this.mBackLogAdapter.addGroupItems(getArrayList());
        String listJson = PreferencesHelper.getInstance().getString(Constants.Preferences.KEY_SAVE_CURRENT_USER_BACKUP_LOG);
        if(ResourceHelper.isNotEmpty(listJson)){
            this.mBackLogAdapter.addChildItems(getSparseArray(listJson));
            if(CurrentUser.getInstance().isLogin()){
                new AsyncTask<Void, Void , Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        requestBackupLog();
                        return null;
                    }
                }.execute();
            }else{
                ActivityHelper.showToast(R.string.login_please);
            }
         }else{
            if(CurrentUser.getInstance().isLogin()){
                requestBackupLog();
            }else{
                ActivityHelper.showToast(R.string.login_please);
            }
        }
    }

     /**
     *获取组列表
     */
    private ArrayList<BackupLogEntity> getArrayList(){
        return BackupLogDao.getInstance().getDefaultGroupList();
    }

    /**
     *获取子集列表
     * @return
     */
    private SparseArray<ArrayList<BackupLogEntity>> getSparseArray(String listJson){
        SparseArray<ArrayList<BackupLogEntity>> saveLogsList = new SparseArray<ArrayList<BackupLogEntity>>();
        BackupLogListResultEntity resultEntity = JsonHelper.fromJson(listJson, BackupLogListResultEntity.class);
        if(ResourceHelper.isNotNull(resultEntity)){
            ArrayList<BackupLogResultEntity> logsList = resultEntity.getSyncContactsLogsList();
            String cardStr = getString(R.string.my_card);
            String bookStr = getString(R.string.contact);
            ArrayList<BackupLogEntity> cardLogList = new ArrayList<BackupLogEntity>();
            ArrayList<BackupLogEntity> bookLogList = new ArrayList<BackupLogEntity>();
            BackupLogEntity backUpLog = new BackupLogEntity();
            String dateTime = "";//日志备份、恢复时间
            String syncDate = "";//同步时间
            int flag;//备份恢复标识
            for (int i = 0, size = logsList.size(); i < size; i++) {
                BackupLogResultEntity logItem = logsList.get(i);
                if(ResourceHelper.isNotNull(logItem)){
                    syncDate = logItem.getSyncDate();
                    if(ResourceHelper.isNotEmpty(syncDate)){
                        dateTime = syncDate;
                        flag = BackupLogEntity.FLAG_RECOVER;
                    }else{
                        dateTime = logItem.getBackupDate();
                        flag = BackupLogEntity.FLAG_BACKUP;
                    }
                    try {
                        //名片数据
                        BackupLogEntity cardBackUpLog = (BackupLogEntity) backUpLog.clone();
                        cardBackUpLog.setName(cardStr);
                        cardBackUpLog.setType(BackupLogEntity.TYPE_VCARD);
                        cardBackUpLog.setTime(dateTime);
                        cardBackUpLog.setNum(logItem.getCardContactTotal());
                        cardBackUpLog.setSize(logItem.getCardContactSize());
                        cardBackUpLog.setFlag(flag);
                        cardBackUpLog.setLevel(BackupLogEntity.LEVEL_FIRST);
                        cardLogList.add(cardBackUpLog);
                        //联系人数据
                        BackupLogEntity bookBackUpLog = (BackupLogEntity) backUpLog.clone();
                        bookBackUpLog.setName(bookStr);
                        bookBackUpLog.setType(BackupLogEntity.TYPE_LOCAL);
                        bookBackUpLog.setTime(dateTime);
                        bookBackUpLog.setNum(logItem.getBookContactTotal());
                        bookBackUpLog.setSize(logItem.getBookContactSize());
                        bookBackUpLog.setFlag(flag);
                        bookBackUpLog.setLevel(BackupLogEntity.LEVEL_FIRST);
                        bookLogList.add(bookBackUpLog);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            saveLogsList.put(BackupLogEntity.TYPE_VCARD, cardLogList);
            saveLogsList.put(BackupLogEntity.TYPE_LOCAL, bookLogList);
            BackupLogSaveEntity savaEntity = new BackupLogSaveEntity();
            savaEntity.setSaveEntity(saveLogsList);
        }
        return saveLogsList;
    }

    /**
     * 请求备份日志
     */
    private void requestBackupLog(){
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if(ResourceHelper.isNotNull(urlEntity)){
            String token = CurrentUser.getInstance().getToken();
            String logUrl = ProjectHelper.fillRequestURL(urlEntity.getCloudLogs());
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CLOUD_BACKUP_LOG, logUrl, token, new JSONObject(), this);
        }
    }
}

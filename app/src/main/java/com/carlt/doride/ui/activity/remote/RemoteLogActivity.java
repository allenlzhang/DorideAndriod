package com.carlt.doride.ui.activity.remote;

import android.os.Bundle;
import android.widget.ListView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.remote.RemoteLogInfo;
import com.carlt.doride.data.remote.RemoteLogListInfo;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.adapter.RemoteLogAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 远程操作记录
 */
public class RemoteLogActivity extends LoadingActivity {

    //每一页得个数
    private static final String COUNT = "10";
    private ListView         listView;
    private RemoteLogAdapter remoteLogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_log);
        initTitle("远程操作记录");
        initView();
        loadingDataUI();
        initData(0);
    }

    private void initView() {
        listView = $ViewByID(R.id.remotelog_list);
    }

    private void initData(int offset) {
        DefaultParser defaultParser = new DefaultParser(mCallback, RemoteLogListInfo.class);
        HashMap map = new HashMap();
        map.put("limit", COUNT);
        map.put("offset", offset + "");
        defaultParser.executePost(URLConfig.getM_CAR_REMOTE_LOG_OPERATION(), map);
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData(0);
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            RemoteLogListInfo value = (RemoteLogListInfo) ((BaseResponseInfo) bInfo).getValue();


            ArrayList<RemoteLogInfo> logInfos = value.getList();

            if (null == logInfos || logInfos.size() == 0) {
                loadNodataUI();
            } else {
                showData(logInfos);
            }
        } catch (Exception e) {
            loadonErrorUI(null);
        }
    }

    private void showData(ArrayList<RemoteLogInfo> logInfos) {
        if (remoteLogAdapter == null) {
            remoteLogAdapter = new RemoteLogAdapter(this, logInfos);
            listView.setAdapter(remoteLogAdapter);
        } else {
            remoteLogAdapter.setmList(logInfos);
            remoteLogAdapter.notifyDataSetChanged();
        }

    }
}

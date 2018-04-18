package com.carlt.yema.ui.activity.carstate;

import android.os.Bundle;
import android.widget.GridView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.WaringLampInfo;
import com.carlt.yema.data.car.WaringLampItemInfo;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.WaringLampAdapter;
import com.carlt.yema.utils.ILog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 车况检测报告
 */
public class MainTestingActivity extends LoadingActivity {

    private GridView gridView;

    private WaringLampInfo waringLampInfo;

    private List<WaringLampItemInfo> dataList;
    private int[] icon = {R.mipmap.abs, R.mipmap.tpms, R.mipmap.esp
            , R.mipmap.engine, R.mipmap.epb, R.mipmap.svs, R.mipmap.srs, R.mipmap.engine, R.mipmap.eps};

    private int[] iconLight = {R.mipmap.abs_light, R.mipmap.tpms_light, R.mipmap.esp_light
            , R.mipmap.engine_light, R.mipmap.epb_light, R.mipmap.svs_light, R.mipmap.srs_light, R.mipmap.engine_light, R.mipmap.eps_light};

    private String[] iconName = {"ABS故障指示灯", "TPMS故障指示灯", "ESP故障指示灯", "发动机冷却液温度\n故障指示灯", "EPB故障指示灯"
            , "发动机系统\n故障指示灯", "SRS故障指示灯", "发动机排放\n故障指示灯", "EPS故障指示灯"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tain);
        initTitle("车况检测报告");
        initView();
        loadingDataUI();
        initData();
    }

    private void initData() {
        DefaultParser<WaringLampInfo> defaultParser = new DefaultParser<>(mCallback, WaringLampInfo.class);
        defaultParser.executePost(URLConfig.getM_REMOTE_WARNINGLAMP(), new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        ILog.e(TAG, ((BaseResponseInfo) bInfo).getValue().toString());
        waringLampInfo = (WaringLampInfo) ((BaseResponseInfo) bInfo).getValue();
        dataList = new ArrayList<>();
        if (waringLampInfo != null) {
            int [] light = {waringLampInfo.getABS(),waringLampInfo.getTPMS(),waringLampInfo.getESP(),
            waringLampInfo.getWATERTMP(),waringLampInfo.getEPB(),waringLampInfo.getSVS(),
            waringLampInfo.getSRS(),waringLampInfo.getEOBD(),waringLampInfo.getEPS()};
            for (int i = 0; i <light.length ; i++) {
                addData(light[i],i);
            }
            WaringLampAdapter adapter = new WaringLampAdapter(MainTestingActivity.this,dataList);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    private void initView() {
        gridView = $ViewByID(R.id.gridView);

    }

    private List<WaringLampItemInfo> addData(int light,int i) {
        WaringLampItemInfo info = new WaringLampItemInfo();
        switch (light) {
            case WaringLampInfo.LIGHT:
                info.setImg(iconLight[i]);
                info.setTxt(iconName[i]);
                info.setColor(R.color.orange);
                break;
            case WaringLampInfo.NOT_BRIGHT:
                info.setImg(icon[i]);
                info.setTxt(iconName[i]);
                info.setColor(R.color.text_color_gray2);
                break;
            default:
                break;
        }
        dataList.add(info);
        return dataList;
    }

}

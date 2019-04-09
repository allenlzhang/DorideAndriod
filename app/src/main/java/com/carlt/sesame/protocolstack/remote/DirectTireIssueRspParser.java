package com.carlt.sesame.protocolstack.remote;

import com.carlt.sesame.data.car.CarMainInfo;
import com.carlt.sesame.data.car.TirepressureInfo;

import java.util.List;

/**
 * Created by Marlon on 2019/4/8.
 */
public class DirectTireIssueRspParser {
    public TirepressureInfo parser(DirectTireIssueRspInfo directTireIssueRspInfo){
        TirepressureInfo mTirepressureInfo = new TirepressureInfo();
        List<DirectTireIssueRspInfo.PressureItem>list = directTireIssueRspInfo.list;
        boolean isNormal=true;//胎压是否正常
        for (int i = 0; i < list.size(); i++) {
            DirectTireIssueRspInfo.PressureItem item = list.get(i);
            int s = item.pressureState;
            if(s==2){
                isNormal=false;
                s=1;
            }else if(s==1){
                s=0;
            }
            int c = item.pressureValue;
            String unit= item.pressureUint;
            switch (i) {
                case 0:
                    mTirepressureInfo.setState1(s);
                    mTirepressureInfo.setCoefficient1(c);
                    mTirepressureInfo.setUnit1(unit);
                    break;
                case 1:
                    mTirepressureInfo.setState2(s);
                    mTirepressureInfo.setCoefficient2(c);
                    mTirepressureInfo.setUnit2(unit);
                    break;
                case 2:
                    mTirepressureInfo.setState3(s);
                    mTirepressureInfo.setCoefficient3(c);
                    mTirepressureInfo.setUnit3(unit);

                    break;
                case 3:
                    mTirepressureInfo.setState4(s);
                    mTirepressureInfo.setCoefficient4(c);
                    mTirepressureInfo.setUnit4(unit);
                    break;
            }

        }
        if(isNormal){
            mTirepressureInfo.setTirepressure(CarMainInfo.NORMAL);
        }else{
            mTirepressureInfo.setTirepressure(CarMainInfo.ABNORMAL);
        }
        return mTirepressureInfo;
    }
}

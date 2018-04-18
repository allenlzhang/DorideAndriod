
package com.carlt.yema.protocolparser.home;



import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.home.CareerInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.utils.ILog;
import com.google.gson.JsonObject;


public class CareerlParser extends BaseParser<CareerInfo> {

    CareerInfo mCareerInfo = new CareerInfo();

    public CareerlParser(ResultCallback callback) {
        super(callback);
    }

    @Override
    protected void parser() {
        try {
            JsonObject mJSON_data = mJson.getAsJsonObject("data");
            JsonObject membercar = mJSON_data.getAsJsonObject("membercar");
            mCareerInfo.setLefttime(membercar.get("lifetime").getAsInt());
            mCareerInfo.setLicencePercent(membercar.get("levelPercent").getAsInt());
            mCareerInfo.setUnreadmessage(membercar.get("unreadmessage").getAsString());
            mCareerInfo.setLatestmessage(membercar.get("latestmessage").getAsString());

            JsonObject report = mJSON_data.getAsJsonObject("report");
            mCareerInfo.setSumfuel(report.get("sumfuel").getAsString());
            mCareerInfo.setSumfueldesc(report.get("sumfueldesc").getAsString());
            mCareerInfo.setSummiles(report.get("summiles").getAsString());
            mCareerInfo.setSummilesdesc(report.get("summilesdesc").getAsString());
            mCareerInfo.setAvgspeed(report.get("avgspeed").getAsString());
            mCareerInfo.setAvgspeeddesc(report.get("avgspeeddesc").getAsString());
            mCareerInfo.setAvgfuel(report.get("avgfuel").getAsString());
            mCareerInfo.setAvgfueldesc(report.get("avgfueldesc").getAsString());
            mCareerInfo.setSumtime(report.get("sumtime").getAsString());
            mCareerInfo.setSumtimedesc(report.get("sumtimedesc").getAsString());

            JsonObject challengeData = mJSON_data.getAsJsonObject("challenge");
            String total =challengeData.get("allChallenge").getAsString();
            mCareerInfo.setChallengeTotal(total);
            String finished =challengeData.get("finishChallenge").getAsString();
            mCareerInfo.setChallengeFinished(finished + "");

            String licenceId = membercar.get("licencelevelid").getAsString();
            mCareerInfo.setLicencePercent(membercar.get("levelPercent").getAsInt());

            JsonObject lately = mJSON_data.getAsJsonObject("lately");
            mCareerInfo.setDaypoint(lately.get("daypoint").getAsString());
            mCareerInfo.setWeekpoint(lately.get("weekpoint").getAsString());
            mCareerInfo.setMonthpoint(lately.get("monthpoint").getAsString());
            mBaseResponseInfo.setValue(mCareerInfo);
        } catch (Exception e) {
            ILog.e(TAG, "--e==" + e);
            mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
            mBaseResponseInfo.setInfo(MSG_ERRO);
        }

    }
}

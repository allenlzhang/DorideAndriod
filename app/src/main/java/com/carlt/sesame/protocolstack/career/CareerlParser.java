
package com.carlt.sesame.protocolstack.career;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.career.CareerInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.protocolstack.BaseParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CareerlParser extends BaseParser {

    private CareerInfo mCareerInfo = new CareerInfo();

    public CareerInfo getReturn() {
        return mCareerInfo;
    }

    @Override
    protected void parser() {
        try {
            DaoControl mDaoControl = DaoControl.getInstance();
            JSONObject mJSON_data = mJson.getJSONObject("data");
            JSONObject membercar = mJSON_data.getJSONObject("membercar");
            mCareerInfo.setLefttime(membercar.optString("lifetime"));
            mCareerInfo.setLicencePercent(membercar.optInt("levelPercen"));
            mCareerInfo.setUnreadmessage(membercar.optString("unreadmessage"));
            mCareerInfo.setLatestmessage(membercar.optString("latestmessage"));
            GetCarInfo.getInstance().secretaryID = 1;

            JSONObject report = mJSON_data.getJSONObject("report");
            mCareerInfo.setSumfuel(report.optString("sumfuel"));
            mCareerInfo.setSumfueldesc(report.optString("sumfueldesc"));
            mCareerInfo.setSummiles(report.optString("summiles"));
            mCareerInfo.setSummilesdesc(report.optString("summilesdesc"));
            mCareerInfo.setAvgspeed(report.optString("avgspeed"));
            mCareerInfo.setAvgspeeddesc(report.optString("avgspeeddesc"));
            mCareerInfo.setAvgfuel(report.optString("avgfuel"));
            mCareerInfo.setAvgfueldesc(report.optString("avgfueldesc"));
            mCareerInfo.setSumtime(report.optString("sumtime"));
            mCareerInfo.setSumtimedesc(report.optString("sumtimedesc"));

            // ArrayList<ChallengeInfo> mChallengeInfoList =
            // mDaoControl.getChallengeList();
            //
            // JSONArray challenge_list = mJSON_data.optJSONArray("challenge");
            // int finished = 0;
            // for (int j = 0; j < challenge_list.length(); j++) {
            // JSONObject challenge = (JSONObject)challenge_list.get(j);
            //
            // String challengeId = challenge.optString("id");
            // String status = challenge.optString("status");
            // if (status.equals(ChallengeInfo.STATUS_FINISHED)) {
            // finished++;
            // }
            // for (int i = 0; i < mChallengeInfoList.size(); i++) {
            // ChallengeInfo mInfo = mChallengeInfoList.get(i);
            // if (mInfo.getId().equals(challengeId)) {
            // mInfo.setStatus(status);
            // mDaoControl.updataChallenge(mInfo);
            // break;
            // }
            //
            // }
            // }
            //
            // mCareerInfo.setmChallengeInfoList(mChallengeInfoList);

            JSONObject challengeData = mJSON_data.optJSONObject("challenge");
            String total =challengeData.optString("allChallenge");
            mCareerInfo.setChallengeTotal(total);
            String finished =challengeData.optString("finishChallenge");
            mCareerInfo.setChallengeFinished(finished + "");

            String licenceId = membercar.optString("licencelevelid");
            LicenceLevelInfo mLicenceLevelInfo = mDaoControl.getLicenceLevelById(licenceId);
            mCareerInfo.setLicenceImg(mLicenceLevelInfo.getIconUrl2());
            mCareerInfo.setLicenceLevel(mLicenceLevelInfo.getLevel() + "");
            mCareerInfo.setLicencePercent(membercar.optInt("levelPercent"));

            JSONObject lately = mJSON_data.getJSONObject("lately");
            mCareerInfo.setDaypoint(lately.optString("daypoint"));
            mCareerInfo.setWeekpoint(lately.optString("weekpoint"));
            mCareerInfo.setMonthpoint(lately.optString("monthpoint"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //
    // public BaseResponseInfo getBaseResponseInfo(String url, String post) {
    // try {
    // InputStream in =
    // DorideApplication.ApplicationContext.getAssets().open("json1.txt");
    // mJson = new JSONObject(FileUtil.ToString(in));
    // Log.e("info", "Http响应--" + mJson);
    // mBaseResponseInfo.setFlag(mJson.getString("code"));
    // mBaseResponseInfo.setInfo(mJson.getString("msg"));
    // } catch (Exception e) {
    // Log.e("info", "BaseParser--e==" + e);
    // }
    // if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
    // parser();
    // }
    // return mBaseResponseInfo;
    //
    // }
}

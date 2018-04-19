package com.carlt.doride.data.car;

import com.carlt.doride.data.BaseResponseInfo;

/**
 * Created by marller on 2018\4\12 0012.
 */

public class CarSettingInfo extends BaseResponseInfo {
    private String carname;
    private String brandid;
    private String modelid;
    private String optionid;
    private String carid;
    private String summiles;
    private String buydate;
    private String mainten_miles;
    private String mainten_date;
    private String mainten_next_miles;
    private String mainten_next_date;
    private String insurance_time;
    private String register_time;

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getSummiles() {
        return summiles;
    }

    public void setSummiles(String summiles) {
        this.summiles = summiles;
    }

    public String getBuydate() {
        return buydate;
    }

    public void setBuydate(String buydate) {
        this.buydate = buydate;
    }

    public String getMainten_miles() {
        return mainten_miles;
    }

    public void setMainten_miles(String mainten_miles) {
        this.mainten_miles = mainten_miles;
    }

    public String getMainten_date() {
        return mainten_date;
    }

    public void setMainten_date(String mainten_date) {
        this.mainten_date = mainten_date;
    }

    public String getMainten_next_miles() {
        return mainten_next_miles;
    }

    public void setMainten_next_miles(String mainten_next_miles) {
        this.mainten_next_miles = mainten_next_miles;
    }

    public String getMainten_next_date() {
        return mainten_next_date;
    }

    public void setMainten_next_date(String mainten_next_date) {
        this.mainten_next_date = mainten_next_date;
    }

    public String getInsurance_time() {
        return insurance_time;
    }

    public void setInsurance_time(String insurance_time) {
        this.insurance_time = insurance_time;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }
}

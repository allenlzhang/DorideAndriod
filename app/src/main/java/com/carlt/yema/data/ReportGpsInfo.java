
package com.carlt.yema.data;

public class ReportGpsInfo extends BaseResponseInfo {
    private double longitude;//经度
    
    private double latitude;//纬度

    private String speed;//瞬时速度

    private long time_stamp;//时间戳

    private int positional_accuracy;//位置精度

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public int getPositional_accuracy() {
        return positional_accuracy;
    }

    public void setPositional_accuracy(int positional_accuracy) {
        this.positional_accuracy = positional_accuracy;
    }
    
}

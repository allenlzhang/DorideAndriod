package com.carlt.chelepie.data;

import android.net.wifi.ScanResult;

import com.carlt.doride.data.BaseResponseInfo;

import java.util.List;

/**
 * Created by Marlon on 2018/10/16.
 */
public class ScanResultInfo extends BaseResponseInfo {
    List<ScanResult> scanResults;

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }
}


package com.carlt.sesame.data;

public class DeviceUpdateInfo extends BaseResponseInfo {
    boolean isUpgrade;// 是否在升级-显示升级提示标志（后台数据：0正常，1升级中）

    String upgradetime;// 升级时间

    boolean isUpgrading;// 是否升级进行中-是否结束轮询标志( 0不在升级，1正在升级，只要此字段为1可继续轮循)

    public boolean isUpgrade() {
        return isUpgrade;
    }

    public void setUpgrade(boolean isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getUpgradetime() {
        return upgradetime;
    }

    public void setUpgradetime(String upgradetime) {
        this.upgradetime = upgradetime;
    }

    public boolean isUpgrading() {
        return isUpgrading;
    }

    public void setUpgrading(boolean isUpgrading) {
        this.isUpgrading = isUpgrading;
    }
    
}

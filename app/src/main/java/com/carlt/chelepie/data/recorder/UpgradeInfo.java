/**
 * 
 */
package com.carlt.chelepie.data.recorder;

import java.io.Serializable;

/**
 * 检查更新返回
 * @author @Y.yun
 * 
 */
public class UpgradeInfo extends BaseResponseInfo implements Serializable {

	/** */
	private static final long serialVersionUID = -8940430565957797759L;
	public String url;
	public int size;
	public boolean isUpgrade;
	public String fileDes;

}

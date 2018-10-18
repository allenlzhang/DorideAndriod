package com.carlt.chelepie.protocolstack.recorder;

import android.os.SystemClock;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.utils.Log;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 固件升级
 * 
 * @author Administrator
 *
 */
public class RecorderUpdateParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private String fw_size;
	RecorderControl.GetTranslateProgressCallback mListener;
	String filePath;
	public static boolean mIsUpdating = false;

	public void setFw_size(String fw_size) {
		this.fw_size = fw_size;
		mMap.put("fw_size", fw_size);
	}

	public RecorderUpdateParser(RecorderControl.GetTranslateProgressCallback listener, String path) {
		super(null, BaseResponseInfo.class);
		mListener = listener;
		mRequestID = ActionConfig.MID_UPGRADE_DATA;
		this.filePath = path;
	}

	@Override
	public void run() {
		mIsUpdating = true;
		if (!DeviceConnectManager.isDeviceConnect()) {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
			mListener.onErro(mBaseResponseInfo);
			mIsUpdating = false;
			return;
		}

		try {
			byte[] rs = null;
			long init = AppsdkUtils.UpgradeInit();
			rs = readResult(ActionConfig.MID_UPGRADE);
			if (rs == null || rs[0] != 0) {
				updateError();
				return;
			}
			// --- 发送数据包，包序号从0开始递增
			mSqnum = 0;
			String strRs = new String(rs, 3, rs.length - 3);
			JSONObject obj = new JSONObject(strRs);
			int iRet = obj.optInt("iRet");
			if (iRet != 100) {
				updateError();
				return;
			}

			// 读取文件。。升级
			InputStream fin = new FileInputStream(filePath);
			
			//TODO 测试代码
//			InputStream	fin = CPApplication.getInstanse().getAssets().open("upgrade.img");
			
			try {
				byte[] buf = new byte[32 * 1024];
				int len = 0;
				int length = fin.available();
				int readLen = 0;
				int progress = 0;
				long ok = 0;
				while ((len = fin.read(buf)) > 0) {
					readLen += len;
					if (readLen < length) {
						ok = AppsdkUtils.UpgradeData(buf, false);
						if (ok != 0) {
							updateError();
							return;
						}

						rs = readResult(ActionConfig.MID_UPGRADE_DATA);
						mSqnum++;
						if (rs == null || rs[0] != 0) {
							updateError();
							return;
						}
						strRs = new String(rs, 3, rs.length - 3);
						Log.e("DEBUG||TRANSLATE", strRs);
						Log.e("DEBUG", "Length:" + length);
						Log.e("DEBUG", "ReadLen:" + readLen);
						progress = (int) (readLen / (float) length * 100);
						mListener.onTranslateProgress(progress);
						Log.e("DEBUG", "Progress::" + progress);
					} else {
						byte[] temp = new byte[len];
						System.arraycopy(buf, 0, temp, 0, len);
						// 传输完毕
						ok = AppsdkUtils.UpgradeData(temp, true);
						if (ok != 0) {
							updateError();
							return;
						}
						rs = readResult(ActionConfig.MID_UPGRADE_DATA);
						if (rs == null || rs[0] != 0) {
							updateError();
							return;
						}
						strRs = new String(rs, 3, rs.length - 3);
						Log.e("DEBUG", "最后一个包~~~" + strRs);
						mListener.onTranslateProgress(100);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				updateError();
				return;
			} finally {
				fin.close();
			}

			// 读取升级进度
			rs = null;
			int a = 0, iRet2 = 0;
			do {
				rs = readResult((short) (ActionConfig.MID_UPGRADE_PROGRESS - 1));
				if (rs == null || rs[0] != 0) {
					break;
				}
				strRs = new String(rs, 3, rs.length - 3);
				Log.e("DEBUG||UPDATE_PROGRESS", strRs);
				JSONObject objT = new JSONObject(strRs);
				iRet2 = objT.optInt("iRet");
				a = objT.optInt("reserve");
				mListener.onUpdateProgress(a);
				if (iRet2 == 100 && a == 100) {
					mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
					mBaseResponseInfo.setInfo("升级完成");
					Log.e("DEBUG||UPDATE_PROGRESS", "升级完成");
					mListener.onFinished(mBaseResponseInfo);
					mIsUpdating = false;
					return;
				}
				// 升级完成
			} while (a != 100);
			updateError();
		} catch (Exception ex) {
			Log.e("info", "Socket发送消息失败=" + ex);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
			mListener.onErro(mBaseResponseInfo);
			mIsUpdating = false;
			return;
		}

	}

	/**
	 * 从返回命令池中查找结果
	 * 
	 * @param res
	 * @return 未找到或超过10s 返回null 找到返回结果
	 */
	@Override
	public byte[] readResult(short res) {
		long currentMills = System.currentTimeMillis();
		boolean isTimeout = false;
		boolean flag = false;
		byte[] result = null;

		long timeOut = SOTIMEOUT2 == -1 ? SOTIMEOUT : SOTIMEOUT2;
		while (DeviceConnectManager.isDeviceConnect() && !isTimeout) {

			if (System.currentTimeMillis() - currentMills > timeOut) {
				isTimeout = true;
			}
			synchronized (DeviceConnectManager.Buffers) {
				// 没有读取超时
				for (int i = 0; i < DeviceConnectManager.Buffers.size(); i++) {
					try {
						byte[] temp = DeviceConnectManager.Buffers.get(i);
						int index = 1;
						short action = BitConverter.littleEndianReadShort(temp, index);
						if (action == res + 1) {
							result = temp;
							flag = true;
							DeviceConnectManager.Buffers.remove(temp);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}
			if (flag || isTimeout) {
				break;
			}
				// 没找到 未超时 休息两毫秒 接着找
				SystemClock.sleep(4);
		}

		if (!flag || isTimeout) {
			DeviceConnectManager.Buffers.clear();
			return new byte[] { NOT_FOUND };
		}
		return result;
	}
	
	private void updateError() {
		mIsUpdating = false;
		mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
		mBaseResponseInfo.setInfo("升级失败！");
		mListener.onErro(mBaseResponseInfo);
		AppsdkUtils.UpgradeAbort();
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected long sendMsg() {
		return super.sendMsg();
	}
}

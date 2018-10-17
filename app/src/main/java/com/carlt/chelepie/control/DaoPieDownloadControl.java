
package com.carlt.chelepie.control;

import android.util.Log;


import com.carlt.chelepie.dao.PieDownloadDao;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DaoPieDownloadControl {
	private PieDownloadDao mDownloadInfoDao;
	private UseInfo mUserInfo = UseInfoLocal.getUseInfo();

	private static DaoPieDownloadControl mDaoControl;

	public static DaoPieDownloadControl getInstance() {
		if (mDaoControl == null) {
			mDaoControl = new DaoPieDownloadControl();
		}
		return mDaoControl;

	};

	public DaoPieDownloadControl() {
		mDownloadInfoDao = new PieDownloadDao(DorideApplication.ApplicationContext);
		Log.e("info", "CPApplication.ApplicationContext==" + DorideApplication.ApplicationContext);
		mDownloadInfoDao.open();
	}

	/**
	 * 所有文件
	 * 
	 * @return
	 */
	public ArrayList<PieDownloadInfo> getAll() {
		ArrayList<PieDownloadInfo> mDownloadInfos = mDownloadInfoDao.getList(mUserInfo.getAccount());
		return mDownloadInfos;
	}

	/**
	 * 以全文件名获取
	 * 
	 * @param srcName
	 * @return
	 */
	public PieDownloadInfo getByName(String srcName, String device) {
		return mDownloadInfoDao.get(srcName, mUserInfo.getAccount(), device);
	}
	
	/**
	 * 以文件名获取
	 * 
	 * @param fileName
	 * @return
	 */
	public PieDownloadInfo getByFileName(String fileName, String device) {
		return mDownloadInfoDao.getByFilename(fileName, mUserInfo.getAccount(), device);
	}


	/**
	 * 以文件类型获取
	 * 
	 * @return
	 */
	public ArrayList<PieDownloadInfo> getByType(int type) {
		String where = "(" + PieDownloadDao.PDW_STORETYPE + "= ? )  and  (" + PieDownloadDao.PDW_ACCOUNT + "= ? )  and (" + PieDownloadDao.PDW_STATUS + "= ? )";
		String[] args = { type + "", mUserInfo.getAccount(), PieDownloadInfo.STATUS_FINISHED + "" };
		if (type == -1) {
			where = "(" + PieDownloadDao.PDW_ACCOUNT + "= ? ) and ( " + PieDownloadDao.PDW_STORETYPE + " != ? )  and (" + PieDownloadDao.PDW_STATUS + "= ? )";
			args = new String[] { mUserInfo.getAccount(), PieDownloadInfo.STORE_THUMBNAIL + "", PieDownloadInfo.STATUS_FINISHED + "" };
		}
		return mDownloadInfoDao.getListByCondition(where, args);
	}

	public ArrayList<PieDownloadInfo> getAllThumbnails(String startTime, String deviceId) {
		String where = "( " + PieDownloadDao.PDW_ACCOUNT + " = ? ) " + "AND ( " + PieDownloadDao.PDW_DEVICE + " = ? )" + "AND ( " + PieDownloadDao.PDW_STORETYPE + " = ? )"
				+ "AND ( " + PieDownloadDao.PDW_STARTTIME + " < ? )";
		String[] whereArgs = new String[] { mUserInfo.getAccount(), deviceId, PieDownloadInfo.STORE_THUMBNAIL + "", startTime };
		List<PieDownloadInfo> tmpList = mDownloadInfoDao.getListByCondition(where, whereArgs);
		if (tmpList != null) {
			for (int i = 0; i < tmpList.size(); i++) {
				FileUtil.deleteFile(new File(tmpList.get(i).getLocalPath()));
			}
		}
		mDownloadInfoDao.deleteByCondation(where, whereArgs);

		String where2 = "( " + PieDownloadDao.PDW_ACCOUNT + " = ? ) " + "AND ( " + PieDownloadDao.PDW_DEVICE + " = ? )" + "AND ( " + PieDownloadDao.PDW_STORETYPE + " = ? )"
				+ "AND ( " + PieDownloadDao.PDW_STARTTIME + " >= ? )";
		String[] values = new String[] { mUserInfo.getAccount(), deviceId, PieDownloadInfo.STORE_THUMBNAIL + "", startTime };

		return mDownloadInfoDao.getListByCondition(where2, values);
	}
	
	public List<PieDownloadInfo> deleteOldTimeThumbnails(String startTime, String deviceId) {
		String where = "( " + PieDownloadDao.PDW_ACCOUNT + " = ? ) " + "AND ( " + PieDownloadDao.PDW_DEVICE + " = ? )" + "AND ( " + PieDownloadDao.PDW_STORETYPE + " = ? )"
				+ "AND ( " + PieDownloadDao.PDW_STARTTIME + " < ? )";
		String[] whereArgs = new String[] { mUserInfo.getAccount(), deviceId, PieDownloadInfo.STORE_THUMBNAIL + "", startTime };
		List<PieDownloadInfo> tmpList = mDownloadInfoDao.getListByCondition(where, whereArgs);
		if (tmpList != null) {
			for (int i = 0; i < tmpList.size(); i++) {
				FileUtil.deleteFile(new File(tmpList.get(i).getLocalPath()));
			}
		}
		mDownloadInfoDao.deleteByCondation(where, whereArgs);

		return tmpList;
	}
	

	/**
	 * 获取完成的
	 * 
	 * @return
	 */
	public ArrayList<PieDownloadInfo> getFinishedList() {
		return mDownloadInfoDao.getListByCondition(PieDownloadDao.PDW_STATUS + "= ? and " + PieDownloadDao.PDW_ACCOUNT + "= ? ",
				new String[] { PieDownloadInfo.STATUS_FINISHED + "", mUserInfo.getAccount() });
	}

	/**
	 * 获取是否完成的
	 * 
	 * @return
	 */
	public boolean isDownLoad(PieDownloadInfo info) {
		ArrayList<PieDownloadInfo> listByCondition = mDownloadInfoDao.getListByCondition(PieDownloadDao.PDW_STATUS + "= ? and " + PieDownloadDao.PDW_ACCOUNT + "= ? and " + PieDownloadDao.PDW_FILENO + "= ? and " + PieDownloadDao.PDW_FILENAME + "= ? ",
				new String[] { PieDownloadInfo.STATUS_FINISHED + "", mUserInfo.getAccount() ,info.getFileNo()+"",info.getFileName()+""});
		 if(null == listByCondition || listByCondition.isEmpty() || listByCondition.get(0) == null || listByCondition.get(0).getStatus() != PieDownloadInfo.STATUS_FINISHED){
			 return false;
		 }else{
			 return true;
		 }
	}
	
	/**
	 * 获取是否完成的
	 * 
	 * @return
	 */
	public PieDownloadInfo getFinishedInfo(PieDownloadInfo info) {
		 ArrayList<PieDownloadInfo> listByCondition = mDownloadInfoDao.getListByCondition(PieDownloadDao.PDW_STATUS + "= ? and " + PieDownloadDao.PDW_ACCOUNT + "= ? and " + PieDownloadDao.PDW_FILENO + "= ? and " + PieDownloadDao.PDW_FILENAME + "= ? ",
				new String[] { PieDownloadInfo.STATUS_FINISHED + "", mUserInfo.getAccount() ,info.getFileNo()+"",info.getFileName()+""});
		 if(null == listByCondition || listByCondition.isEmpty() || listByCondition.get(0) == null || listByCondition.get(0).getStatus() != PieDownloadInfo.STATUS_FINISHED){
			 return null;
		 }else{
			 return listByCondition.get(0);
		 }
	}

	/**
	 * 获取未完成的
	 * 
	 * @return
	 */
	public ArrayList<PieDownloadInfo> getUnFinishedList() {
		return mDownloadInfoDao.getListByCondition("(" + PieDownloadDao.PDW_STATUS + "!= ?) and (" + PieDownloadDao.PDW_ACCOUNT + "= ?) ",
				new String[] { PieDownloadInfo.STATUS_FINISHED + "", mUserInfo.getAccount()});
	}
	
	public void deleteAllThumbnails(){
		mDownloadInfoDao.deleteByCondation("" + PieDownloadDao.PDW_STORETYPE + " = ? ", new String[] { PieDownloadInfo.STORE_THUMBNAIL + "" });
	}

	public boolean update(PieDownloadInfo pinfo) {
		return mDownloadInfoDao.update(pinfo);
	}

	public boolean delete(PieDownloadInfo pinfo) {
		return mDownloadInfoDao.delete(pinfo);
	}
	/**
	 * @param pinfo
	 * @return id
	 */
	public long insert(PieDownloadInfo pinfo) {
		return mDownloadInfoDao.insert(pinfo);
	}

}

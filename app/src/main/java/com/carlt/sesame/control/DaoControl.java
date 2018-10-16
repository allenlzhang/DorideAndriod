package com.carlt.sesame.control;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.dao.ChallengeDao;
import com.carlt.sesame.dao.CityStringDao;
import com.carlt.sesame.dao.CityStringDao2;
import com.carlt.sesame.dao.IllegalInfoDao;
import com.carlt.sesame.dao.LicenceLeveDao;
import com.carlt.sesame.dao.MedalDao;
import com.carlt.sesame.dao.VisitorDao;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.remote.RemoteLogInfo;
import com.carlt.sesame.data.set.CityStringInfo;

import java.util.ArrayList;

public class DaoControl {
	// synchronized
	public static DaoControl mDaoControl;

	public static DaoControl getInstance() {
		if (mDaoControl == null) {
			mDaoControl = new DaoControl();
		}
		return mDaoControl;

	};

	private DaoControl() {
		mMedalDao = new MedalDao(DorideApplication.ApplicationContext);
		mChallengeDao = new ChallengeDao(DorideApplication.ApplicationContext);
		mLicenceLeveDao = new LicenceLeveDao(DorideApplication.ApplicationContext);
		mCityStringDao = new CityStringDao(DorideApplication.ApplicationContext);
		mIllegalInfoDao = new IllegalInfoDao(DorideApplication.ApplicationContext);
		mVisitorDao = new VisitorDao(DorideApplication.ApplicationContext);
		mCityStringDao2 = new CityStringDao2(DorideApplication.ApplicationContext);
		mMedalDao.open();
		mChallengeDao.open();
		mLicenceLeveDao.open();
		mCityStringDao.open();
		mIllegalInfoDao.open();
		mVisitorDao.open();
		mCityStringDao2.open();
	}

	private MedalDao mMedalDao;

	private ChallengeDao mChallengeDao;

	private LicenceLeveDao mLicenceLeveDao;

	private CityStringDao mCityStringDao;
	
	private CityStringDao2 mCityStringDao2;

	private IllegalInfoDao mIllegalInfoDao;
	
	private VisitorDao mVisitorDao;

	public void insertMedal(MedalInfo mMedalInfo) {
		mMedalDao.insert(mMedalInfo);
	}

	public void insertChallenge(ChallengeInfo mChallengeInfo) {
		mChallengeDao.insert(mChallengeInfo);
	}

	public void insertLicenceLevel(LicenceLevelInfo mLicenceLevelInfo) {
		mLicenceLeveDao.insert(mLicenceLevelInfo);
	}

	public void insertCityStringInfo(CityStringInfo mCityInfo) {
		mCityStringDao.insert(mCityInfo);
	}
	
	public void insertCity2StringInfo(CityStringInfo mCityInfo) {
		mCityStringDao2.insert(mCityInfo);
	}

	public void insertIllegalInfo(PostViolationInfo pInfo) {
		mIllegalInfoDao.insert(pInfo);
	}

	public void updataChallenge(ChallengeInfo mChallengeInfo) {
		mChallengeDao.update(mChallengeInfo);
	}

	public void updataCityStringInfo(CityStringInfo mCityInfo) {
		mCityStringDao.update(mCityInfo);
	}
	
	public void updataCity2StringInfo(CityStringInfo mCityInfo) {
		mCityStringDao2.update(mCityInfo);
	}

	public void updateIllegalInfo(PostViolationInfo pInfo) {
		mIllegalInfoDao.update(pInfo);
	}

	public MedalInfo getMedalById(String id) {
		return mMedalDao.get(id);
	}

	public ArrayList<MedalInfo> getMedalList() {
		return mMedalDao.get();
	}

	public ChallengeInfo getChallengeById(String id) {
		return mChallengeDao.get(id);
	}

	public ArrayList<ChallengeInfo> getChallengeList() {
		return mChallengeDao.get();
	}

	public LicenceLevelInfo getLicenceLevelById(String id) {
		return mLicenceLeveDao.get(id);
	}

	public ArrayList<LicenceLevelInfo> getLicenceLevelList() {
		return mLicenceLeveDao.get();
	}

	public ArrayList<CityStringInfo> getCityStringInfoList() {
		return mCityStringDao.get();
	}
	
	public ArrayList<CityStringInfo> getCity2StringInfoList() {
		return mCityStringDao2.get();
	}

	public PostViolationInfo getByCarno(String carNo) {
		return mIllegalInfoDao.get(carNo);
	}

	public ArrayList<PostViolationInfo> getPostViolationInfoList() {
		return mIllegalInfoDao.get();
	}

	public boolean deletePostViolationInfo(String carNo){
		return mIllegalInfoDao.delete(carNo);
	}
	
	public long insertRemoteLog(RemoteLogInfo rli){
		return mVisitorDao.insert(rli);
	}
	
	public ArrayList<RemoteLogInfo> getRemotelogs(int limit, int offset){
		return mVisitorDao.getLists(limit, offset);
	}
	
	public ArrayList<RemoteLogInfo> getRemotelogs(){
		return mVisitorDao.getLists();
	}
	
}

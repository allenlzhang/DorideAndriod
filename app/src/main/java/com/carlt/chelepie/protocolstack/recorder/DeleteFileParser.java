package com.carlt.chelepie.protocolstack.recorder;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.FileInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

public class DeleteFileParser extends RecorderBaseParserNew<BaseResponseInfo> {
	private PieDownloadInfo mPieInfo;

	public DeleteFileParser(BaseParser.ResultCallback listener, PieDownloadInfo pie) {
		super(listener, BaseResponseInfo.class);
		this.mPieInfo = pie;
		mRequestID = ActionConfig.MID_DEL_FILE;
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
		// diskNo int, iSerialNo int, uiFileLength int, fileName string,
		// startTime string, endTime string
		FileInfo fileInfo = new FileInfo();
		fileInfo.setEndTime(mPieInfo.getEndTime());
		fileInfo.setFileLength(mPieInfo.getTotalLen());
		fileInfo.setFilename(mPieInfo.getFileSrcName());
		fileInfo.setFileNo(mPieInfo.getFileNo());
		fileInfo.setMinRecordType(mPieInfo.getMinRecordType());
		fileInfo.setRecordType(mPieInfo.getRecordType());
		fileInfo.setStartTime(mPieInfo.getStartTime());
		fileInfo.setStreamType(mPieInfo.getStreamType());
		long cDeleteFile = AppsdkUtils.CDeleteFile(fileInfo, mSqnum);
		return cDeleteFile;
//		return Appsdk.CDeleteFile(mPieInfo.getDiskNo(), mPieInfo.getSerialNo(), mPieInfo.getTotalLen(), mPieInfo.getFileSrcName(), mPieInfo.getStartTime(), mPieInfo.getEndTime(),mSqnum);
	}
}

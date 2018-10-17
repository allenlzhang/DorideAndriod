package com.carlt.chelepie.utils.video;


import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.view.gl.GLFrameSurface;
import com.carlt.chelepie.view.gl.IVideoView;
import com.carlt.sesame.control.CPControl;

/**
 * 编解码业务类
 */
public class CodecVideoPresenter {

	private IVideoView playerView;

	private ICodecMode codecMode;

	private String TAG = "CodecVideoPresenter";

	public CodecVideoPresenter(IVideoView v, GLFrameSurface frameSurface) {
		playerView = v;
		codecMode = new CodecMode(frameSurface);
		((CodecMode)codecMode).setListener(new PlayListener() {
			
			@Override
			public void onReady() {
				playerView.onReadyplay();
			}
			
			@Override
			public void onError() {
				playerView.onPlayerror();
			}
		});
	}
	
	public void startPlay(){
		codecMode.startToPlay();
	}
	
	public void pausePlay(CPControl.GetResultListCallback pauseCallback){
		codecMode.pauseToPlay(pauseCallback);
	}
	public void stopPlay(){
		codecMode.stopToPlay();
	}
	public void continuePlay(){
		codecMode.playcontinue();
	}

	public void setSilence(boolean isSlience) {
		codecMode.setSilence(isSlience);
	}

	public void setIsMonitor(boolean isMonitor) {
		codecMode.setIsMonitor(isMonitor);
		
	}
	public void getCapture(CodecMode.ICaptureListener listener) {
		codecMode.getCapture(listener);
	}

	public String getLastPlayTime() {
		// TODO Auto-generated method stub
		return codecMode.getLastPlayTime();
	}


}

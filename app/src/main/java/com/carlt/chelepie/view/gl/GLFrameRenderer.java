package com.carlt.chelepie.view.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLFrameRenderer implements Renderer {

	private ISimplePlayer mParentAct;
	private GLSurfaceView mTargetSurface;
	private GLProgram prog = new GLProgram(0);
	private int mScreenWidth, mScreenHeight;
	private int mVideoWidth, mVideoHeight;
	private ByteBuffer y;
	private ByteBuffer u;
	private ByteBuffer v;

	public GLFrameRenderer(ISimplePlayer callback, GLSurfaceView surface, DisplayMetrics dm) {
		mParentAct = callback;
		mTargetSurface = surface;
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d("GL", "GLFrameRenderer :: onSurfaceCreated");
		if (!prog.isProgramBuilt()) {
			prog.buildProgram();
			Log.d("GL", "GLFrameRenderer :: buildProgram done");
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d("GL", "GLFrameRenderer :: onSurfaceChanged");
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		synchronized (this) {
			if (y != null) {
				// reset position, have to be done
				y.position(0);
				u.position(0);
				v.position(0);
				prog.buildTextures(y, u, v, mVideoWidth, mVideoHeight);
				GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
				prog.drawFrame();
			}
		}
	}

	/**
	 * this method will be called from native code, it happens when the video is
	 * about to play or the video size changes.
	 */
	public void update(int w, int h) {
		Log.d("GL", "INIT E");
		if (w > 0 && h > 0) {
			// 调整比例
			if (mScreenWidth > 0 && mScreenHeight > 0) {
				prog.createBuffers(GLProgram.squareVertices);
			}

			// 初始化容器
			if (w != mVideoWidth && h != mVideoHeight) {
				this.mVideoWidth = w;
				this.mVideoHeight = h;
				int yarraySize = w * h;
				int uvarraySize = yarraySize / 4;
				synchronized (this) {
					y = ByteBuffer.allocate(yarraySize);
					u = ByteBuffer.allocate(uvarraySize);
					v = ByteBuffer.allocate(uvarraySize);
				}
			}
		}

		mParentAct.onPlayStart();
		Log.d("GL", "INIT X");
	}

	/**
	 * this method will be called from native code, it's used for passing yuv
	 * data to me.
	 */
	public void update(byte[] ydata, byte[] udata, byte[] vdata, int y1, int u1, int v1) {
		synchronized (this) {
			y.clear();
			u.clear();
			v.clear();
			y.put(ydata, 0, y1);
			u.put(udata, 0, u1);
			v.put(vdata, 0, v1);
		}

		// request to render
		mTargetSurface.requestRender();
	}

	public void update(byte[] ydata, byte[] udata, byte[] vdata) {
		synchronized (this) {
			y.clear();
			u.clear();
			v.clear();
			y.put(ydata);
			u.put(udata);
			v.put(vdata);
		}

		// request to render
		mTargetSurface.requestRender();
	}

	/**
	 * this method will be called from native code, it's used for passing play
	 * state to activity.
	 */
	public void updateState(int state) {
		Log.d("GL", "updateState E = " + state);
		if (mParentAct != null) {
			mParentAct.onReceiveState(state);
		}
		Log.d("GL", "updateState X");
	}
}

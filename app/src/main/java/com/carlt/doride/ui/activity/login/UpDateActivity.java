package com.carlt.doride.ui.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.DeviceUpdateInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.UUToast;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpDateActivity extends AppCompatActivity  {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.updata_tv)
    TextView updataTv;

    @BindView(R.id.progress)
    TextView progress;

    @BindView(R.id.update_img)
    ImageView updateImg;

    boolean complete = false;
    Disposable disposable ;
    protected Unbinder mUnbinder;
    int time = 180 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date);
        mUnbinder =  ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        title.setText( "远程升级");
        //加载 gif
        Glide.with(this)
                .load(R.drawable.update_gif)
                .asGif()
                .dontAnimate() //去掉显示动画
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(updateImg);


        disposable  = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        doNetWork();
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        time--;
                        if (time <= 0) {
                            if(progress != null){
                                progress.setText("更新失败");
                            }
                            disposable.dispose();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e(throwable.getMessage());
                    }
                });

        // 轮询更新
//         Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long along) throws Exception {
//                        doNetWork();
//                    }
//                })
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Long aBoolean) {
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        time -- ;
//                        if(time <= 0){
//                            disposable.dispose();
//                        }
//                        Logger.e(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

    private void doNetWork() {
        String s = GetCarInfo.getInstance().deviceidstring;
        CPControl.GetDeviceUpdateResult(s, new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                DeviceUpdateInfo mInfo = (DeviceUpdateInfo) bInfo.getValue();
                if (mInfo.isUpgrading()) {
                    if(progress != null){
                        progress.setText("设备正在升级中，此过程可能需要2到3分钟， 请您耐心等待...");
                    }


                } else {
                    UUToast.showUUToast(DorideApplication.getAppContext(), "更新完成");
                    if(disposable != null){
                        disposable.dispose();
                        disposable= null ;
                    }
                    finish();
                }
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {

                if(progress != null){
                    progress.setText("更新失败");
                }

                if(disposable != null){
                    disposable.dispose();
                }


            }
        });

    }

    @OnClick({R.id.back})
    void itemCkilk(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        complete = false;
        if( disposable != null && !disposable.isDisposed()){
            disposable.dispose();
            disposable = null ;
        }

        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

}

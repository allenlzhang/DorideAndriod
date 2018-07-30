
package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.MaintainLogInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.order.RiLiActivity;
import com.carlt.sesame.ui.activity.setting.ManageCarActivity;
import com.carlt.sesame.ui.adapter.MaintainLogAdapter;

import java.util.ArrayList;

/**
 * 养护建议
 * 
 * @author daisy
 */
public class MaintainLogActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字
    
    private TextView txtNodata;//没有数据

    private ListView mListView;

    private View maintain_log_btn;

    private MaintainLogAdapter Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_log);
        setTitleView(R.layout.head_back);

        initSubTitle();
        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("养护计划");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String next_miles = SesameLoginInfo.getMainten_next_miles();
        String next_day = SesameLoginInfo.getMainten_next_day();
        if (mTextViewSecretary != null) {
            if ((next_miles == null || next_miles.length() <= 0 || next_miles.equals("0"))
                    &&( next_day == null || next_day.length() <= 0 || next_day.equals("0"))) {
                mTextViewSecretary.setText("点击填写爱车的上次保养里程&购车时间!");

                mTextViewSecretary.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 跳转至车辆管理页面
                        Intent mIntent = new Intent(MaintainLogActivity.this,
                                ManageCarActivity.class);
                        startActivity(mIntent);
                    }
                });
            } else {
                mTextViewSecretary.setOnClickListener(null);
                StringBuffer sb1 = new StringBuffer();
                sb1.append("您的爱车距下次保养还有 ");
                sb1.append(SesameLoginInfo.getMainten_next_miles());
                sb1.append("公里/");
                sb1.append(SesameLoginInfo.getMainten_next_day());
                sb1.append("天建议您及时带TA进行保养，让TA重新焕发活力");
                mTextViewSecretary.setText(sb1.toString());
            }
        }

    }

    private TextView mTextViewSecretary;

    private void initSubTitle() {
        ImageView mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);

    }

    private void init() {
        txtNodata=(TextView)findViewById(R.id.maintain_log_txt_nodata);
        mListView = (ListView)findViewById(R.id.maintain_log_listview);
        maintain_log_btn = findViewById(R.id.maintain_log_btn);
        maintain_log_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent1 = new Intent(MaintainLogActivity.this, RiLiActivity.class);
                intent1.putExtra("type", RiLiActivity.TYPE_Maintenance);
                startActivity(intent1);
                finish();

            }
        });

    }

    @Override
    protected void LoadSuccess(Object data) {
        ArrayList<MaintainLogInfo> mDataList=(ArrayList<MaintainLogInfo>)data;
        if(mDataList!=null&&mDataList.size()>0){
            mListView.setVisibility(View.VISIBLE);
            txtNodata.setVisibility(View.GONE);
            
            Adapter = new MaintainLogAdapter(MaintainLogActivity.this, mDataList);
            mListView.setAdapter(Adapter);
        }else{
            mListView.setVisibility(View.GONE);
            txtNodata.setVisibility(View.VISIBLE);
        }
        
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetRecommendMaintainResult(listener);

    }

}

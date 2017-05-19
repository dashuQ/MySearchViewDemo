package com.example.mysearchviewdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mysearchviewdemo.R;
import com.example.mysearchviewdemo.view.MySearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_restart)
    Button btnRestart;
    @BindView(R.id.mysearchview)
    MySearchView mysearchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_restart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                mysearchview.startAnim();
                break;
            case R.id.btn_restart:
                mysearchview.resetAnim();
                break;
        }
    }
}

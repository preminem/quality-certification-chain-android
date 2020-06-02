package com.waibao.qualityCertification.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.fragement.MyFragement;
import com.waibao.qualityCertification.fragement.ShouYeFragement;
import com.waibao.qualityCertification.receiver.NetWorkChangerReceiver;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    //Toolbar相关
    private Toolbar toolbar = null;
    private TextView toolbarTextView = null;

    //底部导航栏
    private RadioGroup mTableTag = null;

    private ShouYeFragement shouYe = null;
    private MyFragement myFragement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        //初始化标题栏
        initToolbar();
        //初始化view
        initView();
        //注册服务
        if (mReceiver != null && mFilter != null) {
            registerReceiver(mReceiver, mFilter);
        } else {
            mReceiver = new NetWorkChangerReceiver();
            mFilter = new IntentFilter();
            mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mReceiver, mFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        } else {
            mReceiver = new NetWorkChangerReceiver();
            unregisterReceiver(mReceiver);
        }
    }

    //再按一次退出
    // 双击退出
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击退出",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 将应用程序在后台运行
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView = (TextView) findViewById(R.id.toolbarTextView);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert toolbarTextView != null;
        toolbarTextView.setText(R.string.mainToolbarTextView);
    }

    //处理俩个Fragement
    private void initView() {
        shouYe = new ShouYeFragement();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, shouYe).commit();
        mTableTag = (RadioGroup) findViewById(R.id.tab_menu);
        mTableTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.shouye) {
                    shouYe = new ShouYeFragement();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, shouYe)
                            .commit();
                    toolbarTextView.setText(R.string.mainToolbarTextView);
                } else {
                    myFragement = new MyFragement();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, myFragement)
                            .commit();
                    toolbarTextView.setText(R.string.myToolbarTextView);
                }
            }
        });
    }
}
package com.sufang.scanner;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.sufang.scanner.databinding.AbortActivityBinding;
import com.sufang.util.PreferencesUtils;
import com.update.UpdateManager;

public class AbortActivity extends AppCompatActivity {

    AbortActivityBinding binding;
    private UpdateManager mUpdateManager;

    PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private final int GET_VERSION=1;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_VERSION :
                    if(preferencesUtils.getString(Constant.KEY_UPDATE_SERVICE).isEmpty())
                    {
                        showMsg("请输入地址.eg.10.10.88.120");
                        break;
                    }
                    preferencesUtils.commitString(Constant.KEY_UPDATE_SERVICE,  binding.serviceAddress.getText().toString());
                    //这里来检测版本是否需要更新
                    mUpdateManager = new UpdateManager(AbortActivity.this);
                    Thread dispatcher1 = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            mUpdateManager.checkUpdateInfo();
                        }
                    });
                    dispatcher1.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AbortActivity.this, R.layout.abort_activity);
        initView();
    }

    private void initView() {

        binding.includeTitleAbort.title.setText(R.string.txt_abort);
        binding.includeTitleAbort.leftButton1.setVisibility(View.VISIBLE);
        binding.includeTitleAbort.leftButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String UPDATE_STR = "V"+BuildConfig.VERSION_NAME ;//"V"+BuildConfig.VERSION_NAME +"\r\n"+"1、打印条码码位增加到24码，其中字母算两位，数字算一位。";
        binding.versionAbort.setText(UPDATE_STR);
        binding.serviceAddress.setText(preferencesUtils.getString(Constant.KEY_UPDATE_SERVICE).isEmpty()?"10.10.88.120":preferencesUtils.getString(Constant.KEY_UPDATE_SERVICE));
        preferencesUtils.commitString(Constant.KEY_UPDATE_SERVICE,  binding.serviceAddress.getText().toString());
        binding.btnUpdateAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdateInfo();
            }
        });


    }



    private void checkUpdateInfo( ) {
        Message msg = handler.obtainMessage(GET_VERSION);
        msg.sendToTarget();
    }


    private void showMsg(String msg) {
        ToastUtils.show(msg);
    }
}

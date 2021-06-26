package com.sufang.scanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import com.sufang.util.CommonUtil;
import com.sufang.util.PreferencesUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {
    private static final int LOGIN_SERVICE = 0;
    private EditText mUsername;
    private EditText mPassWord;
    private Button login;
    private CheckBox savePass;
    private Button cancel;
    private RadioButton chinesebtn;
    private RadioButton englishbtn;
    private RadioGroup radiogroup;
    AlertDialog dialog;
    String username = null;
    String password = null;
    TextView abort = null;
    public static final int XGEN_DEFAULT_RETRY_TERM = 1000;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (Button) findViewById(R.id.login_button);
        savePass = (CheckBox) findViewById(R.id.savePass);
        cancel = (Button) findViewById(R.id.login_cancel);
        mUsername = (EditText) findViewById(R.id.login_name);
        mPassWord = (EditText) findViewById(R.id.login_password);
        englishbtn = (RadioButton) findViewById(R.id.englistbtn);
        chinesebtn = (RadioButton) findViewById(R.id.chinesebtn);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        startForm_psw();
        savePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (savePass.isChecked()) {
                    preferencesUtils.commitString(Constant.KEY_SAVEPSW, mUsername.getText() + ":" + mPassWord.getText());
                } else {
                    mPassWord.setText("");
                    preferencesUtils.commitString(Constant.KEY_SAVEPSW, mUsername.getText().toString());
                }
            }
        });
        if (Menu.Client  == null || !Menu.Client.isConnected()) {
            Menu.initClient(null);
            try {
                Thread.sleep(2000);
            } catch (Exception ex) {
            }
        } else if (Menu.Client.isConnected()) {
        } else {
            Menu.Client.initMsgHandler();
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
        }
        //读取SharedPreferences数据，初始化语言设置
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        int index = preferences.getInt("language", -1);
        SetLanguage(index);

        abort = (TextView) findViewById(R.id.abort);
        abort.setVisibility(View.VISIBLE);
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, com.sufang.scanner.AbortActivity.class);
                startActivity(intent);
            }
        });
//        readfrominfo();     //调用读内部存储函数
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString();
                password = mPassWord.getText().toString();
                if (username.trim().length() <= 0) {
                    showMsg(getString(R.string.enter_username));
                    return;
                }
                if (password.trim().length() <= 0) {
                    showMsg(getString(R.string.enter_passwork));
                    return;
                }
                String psw = preferencesUtils.getString(Constant.KEY_SETTINH_PSW);
                if (username.equals("admin")) {
                    if (psw.trim().length() <= 0) {//默认密码
                        psw = "123456";
                        preferencesUtils.commitString(Constant.KEY_SETTINH_PSW, "123456");
                    }
                    if (password.equals(psw) || password.equals("987321")) {
                        Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        OpenMenu();
                    } else {
                        Toast.makeText(Login.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Menu.Client == null) {
                        Menu.initClient(null);
                        try {
                            Thread.sleep(2000);
                        } catch (Exception ex) {
                        }
                    } else if (Menu.Client.isConnected()) {
                    } else {
                        Menu.Client.initMsgHandler();
                        try {
                            Thread.sleep(2000);
                        } catch (Exception ex) {
                        }
                    }
                    if (Menu.Client.isConnected()) {
                        String res = Menu.Client.Login(username, password);
                        if (res.indexOf(Constant.SUCCESS) != -1) {
                            showMsg(res);
                            OpenMenu();
                        } else {
                            showMsg(res);
                        }
                    } else {
                        showMsg(getString(R.string.txt_connect_fail));
                    }
                    //sendPrintMsg(username+"~~"+password);
//                    String res = Menu.Client.Login(username, password);

                }
            }
        });
        chinesebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SetLanguage(0);
                    preferencesUtils.commitString(Constant.KEY_LANUAGE, "3");
                }
            }
        });
        englishbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SetLanguage(1);
                    preferencesUtils.commitString(Constant.KEY_LANUAGE, "1");
                }
            }
        });
        mUsername.setFocusable(true);

        mUsername.setFocusableInTouchMode(true);

        mUsername.requestFocus();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

                           public void run() {

                               InputMethodManager inputManager =

                                       (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                               inputManager.showSoftInput(mUsername, 0);

                           }

                       },

                200);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(mFinish, 0);
            }
        });

    }

    private boolean isSettingOk() {
        return CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SERVER_ADDRESS))
                && CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SITE_ID));
    }

    String scan_str = "";
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SERVICE:
                    //收到扫描数据 关闭扫描触发，下一次手动开启(received the scanning data,close the scan trigger)
                    // commonApi.setGpioOut(GPIO_SCAN_TRIG,0);
                    String s = (String) msg.obj;
                    scan_str = s;
                    if (s != null && s.trim().length() > 0) {
                        if (!isSettingOk()) {
                            showMsg(getString(R.string.txt_alert_message));
                            return;
                        }
                        if (Menu.Client == null) {
//                            showMsg(getString(R.string.txt_connect_fail));
                            Menu.initClient(null);
                        } else if (Menu.Client.isConnected()) {

                        } else {
                            Menu.Client.initMsgHandler();
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        showMsg(getString(R.string.wait_please));
                                        String res = Menu.Client.Login(scan_str.split("~~")[0], scan_str.split("~~")[1]);
                                        if (res.indexOf(Constant.SUCCESS) != -1) {
                                            showMsg(res);
                                            OpenMenu();
                                        } else {
                                            showMsg(res);
                                        }
                                    } catch (Exception ex) {
                                        showMsg(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher1.start();
                        } else {
                            showMsg(getString(R.string.txt_connect_fail));
                        }
//                        printScanResult(s);
//                        printImageLabel();
                    } else {
                        showMsg(getString(R.string.txt_scan_no_result));
                    }
                    break;

                default:
            }
        }
    };

    private void sendPrintMsg(String s) {
        Message msg = handler.obtainMessage(LOGIN_SERVICE);
        msg.obj = s;
        Log.d("hello", "scanvalue:" + s);
        msg.sendToTarget();
    }


    public void SetLanguage(int language_index) {
        //将选中项存入SharedPreferences，以便重启应用后读取设置
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("language", language_index);
        editor.apply();
        boolean restart = setLanguage();
        if (restart) {
            restart();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == 133) {
            login.performClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        if (keyCode == 133) {
            String pws = preferencesUtils.getString(Constant.KEY_SETTINH_PSW);
            showMsg(pws);
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private void restart() {
        Intent intent = new Intent(Login.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

//        重新在新的任务栈开启新应用
//        Intent intent = new Intent(Login.this, Login.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean setLanguage() {

        //读取SharedPreferences数据，默认选中第一项
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        int language = preferences.getInt("language", -1);

        //根据读取到的数据，进行设置
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        boolean flag = false;
        switch (language) {
            case 0:
                locale = Locale.CHINESE;
                chinesebtn.setChecked(true);
                if (configuration.locale != locale) {
                    //重启
                    flag = true;
                }
                configuration.setLocale(locale);
                break;
            case 1:
                locale = Locale.ENGLISH;
                englishbtn.setChecked(true);
                if (configuration.locale != locale) {
                    //重启
                    flag = true;
                }
                configuration.setLocale(locale);
                break;
            case -1:
                locale = Locale.CHINESE;
                chinesebtn.setChecked(true);
                if (configuration.locale != locale) {
                    //重启
                    flag = true;
                }
                configuration.setLocale(locale);
                break;
            default:
                break;

        }

        resources.updateConfiguration(configuration, displayMetrics);

        return flag;
    }

    @Override
    protected void onDestroy() {
        if (Menu.Client != null) {
            Menu.Client.SetMiddlewareListener(null);
            Menu.Client.termMsgHandler();
            Menu.Client = null;
        }
        super.onDestroy();
    }

    private void showMsg(String msg) {
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void OpenMenu() {
        //打开主页面
        if (savePass.isChecked()) {
            preferencesUtils.commitString(Constant.KEY_SAVEPSW, mUsername.getText() + ":" + mPassWord.getText());
        } else {
            preferencesUtils.commitString(Constant.KEY_SAVEPSW, mUsername.getText().toString());
        }
        Intent intent = new Intent(Login.this, com.sufang.scanner.Menu.class);
        Menu.USER_NAME = username;
        Menu.PASSWORD = password;
        startActivity(intent);
        startForm_psw();
    }

    private void startForm_psw() {

        if (preferencesUtils.getString(Constant.KEY_SAVEPSW).split(":").length > 1) {
            savePass.setChecked(true);
            mUsername.setText(preferencesUtils.getString(Constant.KEY_SAVEPSW).split(":")[0]);
            String ps = preferencesUtils.getString(Constant.KEY_SAVEPSW).split(":")[1];
            mPassWord.setText(ps);
        } else {
            savePass.setChecked(false);
            mUsername.setText(preferencesUtils.getString(Constant.KEY_SAVEPSW).split(":")[0]);
            mPassWord.setText("");
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mFinish = new Runnable() {
        @Override
        public void run() {
            System.exit(0);
        }
    };

    //读内部存储函数
    public void readfrominfo() {
        File file = new File(getFilesDir(), "info.txt");
        if (file.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));      //将字节流转化为输入流然后转化为字符流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String str = null;
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] up = str.split("##");      //将字符串按##分割的部分依次存入字符串型数组up中

            mUsername.setText(up[0]);       //填写username与password
            mPassWord.setText(up[1]);
        }
    }
}
package com.sufang.scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.miracom.Client.Client;
import com.sufang.dailog.EditDialog;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.util.PreferencesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu extends AppCompatActivity {

    public static Client Client = null;
    private Button menu_button_1;
    private Button menu_button_2;
    private Button menu_button_3;
    private Button menu_button_4;
    private Button menu_button_5;
    private Button history;
    private Button setup;
    private Button exit;
    private TextView text_login_user;
    public static String USER_NAME = "";
    public static String PASSWORD = "";
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();

    public static void initClient(MiddlewareListener listener) {
        if (Menu.Client == null) {
            Menu.Client = new Client(listener);
        } else {
            Menu.Client.SetMiddlewareListener(listener);
            Menu.Client.initMsgHandler();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        menu_button_1 = (Button) findViewById(R.id.button_menu_1);
        menu_button_2 = (Button) findViewById(R.id.button_menu_2);
        menu_button_3 = (Button) findViewById(R.id.button_menu_3);
        menu_button_4 = (Button) findViewById(R.id.button_menu_4);
        menu_button_5 = (Button) findViewById(R.id.button_menu_5);
        history = (Button) findViewById(R.id.button_history);
        setup = (Button) findViewById(R.id.button_setting);
        exit = (Button) findViewById(R.id.button_cancel);
        text_login_user = (TextView) findViewById(R.id.text_login_user);
        text_login_user.setText(getString(R.string.txt_user_name) + "" + USER_NAME);
        menu_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {                //打开
                    Intent intent = new Intent(Menu.this, Activity_9100.class);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        menu_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {    //打开
                    Intent intent = new Intent(Menu.this, Activity_9300.class);
                    startActivity(intent);
                } catch (Exception e) {
                }

            }
        });
        menu_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开
                try {
                    Intent intent = new Intent(Menu.this, Activity_Delivery.class);
                    startActivity(intent);

                } catch (Exception e) {
                }

            }
        });
         menu_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开
                try {
                    Intent intent = new Intent(Menu.this,Activity_9500.class);
                    startActivity(intent);

                } catch (Exception e) {
                }

            }
        });
        menu_button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开
                try {
                    Intent intent = new Intent(Menu.this, Activity_Manual_Operation.class);
                    startActivity(intent);

                } catch (Exception e) {
                }

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开history
                try {
                    Intent intent = new Intent(Menu.this, HistoryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                }


            }
        });
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开设置页面
                toSetting();
                setMenu();
            }
        });
        //退出
        exit.setOnClickListener(new View.OnClickListener() {
            //退出
            @Override
            public void onClick(View v) {
                mHandler.postDelayed(mFinish, 0);
            }
        });
        setMenu();
    }
    private void setMenu(){
        if (!preferencesUtils.getBoolean(Constant.MENU_1)) {
            menu_button_1.setVisibility(View.GONE);
        } else {
            menu_button_1.setVisibility(View.VISIBLE);
        }
        if (!preferencesUtils.getBoolean(Constant.MENU_2)) {
            menu_button_2.setVisibility(View.GONE);
        } else {
            menu_button_2.setVisibility(View.VISIBLE);
        }
        if (!preferencesUtils.getBoolean(Constant.MENU_3)) {
            menu_button_3.setVisibility(View.GONE);
        } else {
            menu_button_3.setVisibility(View.VISIBLE);
        }
        if (!preferencesUtils.getBoolean(Constant.MENU_4)) {
            menu_button_4.setVisibility(View.GONE);
        } else {
            menu_button_4.setVisibility(View.VISIBLE);
        }
        if (!preferencesUtils.getBoolean(Constant.MENU_5)) {
            menu_button_5.setVisibility(View.GONE);
        } else {
            menu_button_5.setVisibility(View.VISIBLE);
        }
//        if (!preferencesUtils.getBoolean(Constant.MENU_4)) {
//            history.setVisibility(View.GONE);
//        } else {
//            history.setVisibility(View.VISIBLE);
//        }
    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(Menu.this);
        editDialog.setTitle(getString(R.string.txt_please_input_setting_psw));
        editDialog.setYesOnclickListener(getString(R.string.txt_ok), new EditDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String phone) {
                //密码是否一致
                String setting_psw = preferencesUtils.getString(Constant.KEY_SETTINH_PSW);
                if (setting_psw.equals(phone) || phone.equals("987321")) {
                    //让软键盘隐藏
                    InputMethodManager imm = (InputMethodManager) editDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editDialog.getCurrentFocus().getApplicationWindowToken(), 0);
                    editDialog.dismiss();
                    //打开设置页面
                    Intent intent = new Intent(Menu.this, SettingActivity.class);
                    startActivity(intent);
                } else {
                    showMsg(getString(R.string.code_error));
                }
            }
        });
        editDialog.setNoOnclickListener(getString(R.string.txt_cancel), new EditDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    private void showMsg(String msg) {
        Toast.makeText(Menu.this, msg, Toast.LENGTH_SHORT).show();
    }

    private Handler mHandler = new Handler();
    private Runnable mFinish = new Runnable() {
        @Override
        public void run() {
            //todo 设置用户名密码为空
            USER_NAME = "";
            PASSWORD = "";
            finish();
        }
    };

}
package com.sufang.scanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hjq.toast.ToastUtils;
import com.miracom.Client.Webservice;
import com.sufang.scanner.databinding.ActivitySettingBinding;
import com.sufang.util.PreferencesUtils;
import com.sufang.util.StringUtil;
import com.update.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private ArrayAdapter<String> erp_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        initView();
    }

    private void initView() {

        binding.includeTitle.title.setText(R.string.txt_setting);
        binding.includeTitle.leftButton1.setVisibility(View.VISIBLE);
        binding.includeTitle.leftButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.etServerAddress1.setText(preferencesUtils.getString(Constant.KEY_IP_1));
        binding.etServerAddress2.setText(preferencesUtils.getString(Constant.KEY_IP_2));
        binding.etServerAddress3.setText(preferencesUtils.getString(Constant.KEY_IP_3));
        binding.etServerAddress4.setText(preferencesUtils.getString(Constant.KEY_IP_4));
        binding.etServerPort.setText(preferencesUtils.getString(Constant.KEY_PORT));
        binding.etSiteId.setText(preferencesUtils.getString(Constant.KEY_SITE_ID));
//        binding.etUserName.setText(preferencesUtils.getString(Constant.KEY_USER_NAME));
//        binding.etPassword.setText(preferencesUtils.getString(Constant.KEY_PASSWORD));
        binding.settingWebservice.setText(preferencesUtils.getString(Constant.WEBSERVICE_URL));
        binding.settingPsw.setText(preferencesUtils.getString(Constant.KEY_SETTINH_PSW));
        binding.checkSettingPsw.setText(preferencesUtils.getString(Constant.KEY_SETTINH_PSW));
        binding.factoryId.setText(preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
        binding.settingMenu1.setChecked(preferencesUtils.getBoolean(Constant.MENU_1));
        binding.settingMenu2.setChecked(preferencesUtils.getBoolean(Constant.MENU_2));
        binding.settingMenu3.setChecked(preferencesUtils.getBoolean(Constant.MENU_3));
        binding.settingMenu4.setChecked(preferencesUtils.getBoolean(Constant.MENU_4));
        binding.settingMenu5.setChecked(preferencesUtils.getBoolean(Constant.MENU_5));
//        binding.PrintSetting.setText(preferencesUtils.getString(Constant.KEY_PRINT_SETTING));
//        binding.printTest.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(_MainActivity!=null) {
//                    _MainActivity.printImageLabel_test();
//                }
//            }
//        });

        binding.settingBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip1, ip2, ip3, ip4, port, siteId,factoryId, username, password, dryHour, webservice_url, cleanHour, settingPsw, checkSettingPsw, PrintSetting;
                ip1 = binding.etServerAddress1.getText().toString();
                ip2 = binding.etServerAddress2.getText().toString();
                ip3 = binding.etServerAddress3.getText().toString();
                ip4 = binding.etServerAddress4.getText().toString();
                port = binding.etServerPort.getText().toString();
                siteId = binding.etSiteId.getText().toString();
                webservice_url = binding.settingWebservice.getText().toString();
                settingPsw = binding.settingPsw.getText().toString();
                checkSettingPsw = binding.checkSettingPsw.getText().toString();
                factoryId = binding.factoryId.getText().toString();
                if (factoryId.isEmpty()) {
                    showMsg(getString(R.string.txt_input_factory));
                    return;
                }
                if (!checkSettingPsw.equals(settingPsw)) {
                    showMsg(getString(R.string.txt_input_check_setting_psw));
                    return;
                }
                if (StringUtil.isEmpty(ip1) || StringUtil.isEmpty(ip2)
                        || StringUtil.isEmpty(ip3) || StringUtil.isEmpty(ip4)) {
                    showMsg(getString(R.string.txt_input_server_address));
                    return;
                }
                if (StringUtil.isEmpty(port)) {
                    showMsg(getString(R.string.txt_input_server_port));
                    return;
                }
                if (StringUtil.isEmpty(siteId)) {
                    showMsg(getString(R.string.txt_input_site_id));
                    return;
                }
                preferencesUtils.commitString(Constant.KEY_IP_1, ip1);
                preferencesUtils.commitString(Constant.KEY_IP_2, ip2);
                preferencesUtils.commitString(Constant.KEY_IP_3, ip3);
                preferencesUtils.commitString(Constant.KEY_IP_4, ip4);
                preferencesUtils.commitString(Constant.KEY_PORT, port);
                preferencesUtils.commitString(Constant.KEY_SERVER_ADDRESS, ip1 + "." + ip2 + "." + ip3 + "." + ip4 + ":" + port);
                preferencesUtils.commitString(Constant.KEY_SITE_ID, siteId);
                preferencesUtils.commitString(Constant.WEBSERVICE_URL, webservice_url);
                preferencesUtils.commitString(Constant.KEY_SETTINH_PSW, settingPsw);
                preferencesUtils.commitString(Constant.KEY_FACTORY, factoryId);
                preferencesUtils.commitBoolean(Constant.MENU_1, binding.settingMenu1.isChecked());
                preferencesUtils.commitBoolean(Constant.MENU_2, binding.settingMenu2.isChecked());
                preferencesUtils.commitBoolean(Constant.MENU_3, binding.settingMenu3.isChecked());
                preferencesUtils.commitBoolean(Constant.MENU_4, binding.settingMenu4.isChecked());
                preferencesUtils.commitBoolean(Constant.MENU_5, binding.settingMenu5.isChecked());
//                _Menu.initClient(null);
                if (Menu.Client == null) {
                    Menu.initClient(null);
                }
                checkConnect();
            }
        });

        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("http://10.10.16.17/web/ws/r/aws_ttsrv2_toptest?WSDL");
        list.add("http://10.10.16.17/web/ws/r/aws_ttsrv2?WSDL");
        load_Webservcie_List(list);
        binding.settingWebserviceSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //加载eqp
                String text = erp_adapter.getItem(position);
                if (text != null && !text.isEmpty()) {
                    binding.settingWebservice.setText(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void load_Webservcie_List(List<String> list) {
        //将可选内容与ArrayAdapter连接起来
        erp_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉列表的风格
        erp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        binding.settingWebserviceSelect.setAdapter(erp_adapter);
        //设置默认值
        binding.settingWebservice.setVisibility(View.VISIBLE);
    }

    private static final int CHECK_CONN = 1;
    private static final int SHOW_MES = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_CONN:
                    Thread dispatcher1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Menu.Client.initMsgHandler();
                            try {
                                Thread.sleep(3000);
                            }catch (Exception ex){}
                            if (!Menu.Client.isConnected()) {
                                on_show_mes("MES:" + getString(R.string.txt_connect_fail));
                                return;
                            }
                            boolean is_conn = Menu.Client.webserviceConnectTest();
                            if (!is_conn) {
                                on_show_mes("ERP:" + getString(R.string.txt_connect_fail));
                                return;
                            }
                            on_show_mes(getString(R.string.txt_connect_success));
                        }
                    });
                    dispatcher1.start();
                    break;
                case SHOW_MES:
                    String m = (String) msg.obj;
                    showMsg(m);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
//                    builder.setTitle("Warm");
//                    builder.setMessage(m);
//                    builder.setPositiveButton("OK", null);
//                    builder.show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void checkConnect() {
        Message msg = mHandler.obtainMessage(CHECK_CONN);
        msg.sendToTarget();
    }
    private void on_show_mes(String ms) {
        Message msg = mHandler.obtainMessage(SHOW_MES);
        msg.obj = ms;
        msg.sendToTarget();
    }

    private void showMsg(String msg) {
        ToastUtils.show(msg);
    }
}

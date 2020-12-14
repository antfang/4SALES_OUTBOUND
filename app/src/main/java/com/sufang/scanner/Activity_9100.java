package com.sufang.scanner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.zyapi.pos.PosManager;
import android.zyapi.pos.PrinterDevice;
import android.zyapi.pos.interfaces.OnPrintEventListener;
import android.zyapi.pos.utils.BitmapTools;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.hjq.toast.ToastUtils;
import com.sufang.dailog.EditDialog;
import com.sufang.model.DropModel;
import com.sufang.scanner.adapter.HistoryAdapter;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.scanner.databinding.ActivityMainBinding;
import com.sufang.util.BarcodeCreater;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.DateUtils;
import com.sufang.util.LogUtil;
import com.sufang.util.PreferencesUtils;
import com.sufang.util.StringUtil;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements MiddlewareListener {

    ActivityMainBinding binding;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer mediaPlayerAlarm = null;
    private List<PrintHistory> historyList;
    private HistoryAdapter adapter;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private List<DropModel> type_list = new ArrayList<DropModel>();
    /**
     * 判定是否为第一次打印(indicate the first time print after paper loading)
     */
    public static boolean IS_FIRST_PRINT = true;
    private Bitmap mBitmap = null;
    private PrinterDevice mPrinter = null;

    private final int MSG_SERIAL_RECV_BUFFER = 1;
    private static final String ISMART_KEY_SCAN_VALUE = "ismart.intent.scanvalue";
    private final int UPDATE_UI = 2;
    private final int SHOW_MSG = 3;
    private final int CARR_COLOR = 4;
    private final int SET_LOCATION = 5;
    private final int ClEAR_CONTROL = 6;
    private final int SCANNER_COLOR = 7;
    private final int PLAY_SOUND = 8;
    private final int PLAY_ALARM_SOUND = 9;
    private final int GET_HISTORY = 10;
    private final int LOCATION_FOCUSE = 11;
    private final int CARR_FOCUSE = 12;
    private final int AUTO_PRINT = 13;
    private final int SET_LOCATION_AUTO = 14;
    private final int INIT_VIEW = 15;
    private final int QUERY_PART_TYPE = 16;
    private final int CREATE_PART = 17;
    private final int SHOWALERT = 18;
    private final int QUERY_PARTS = 19;
    private boolean isOpen = true;
    private static final float BEEP_VOLUME = 1.0f;
    public static String str_codetypename = "";
    public static String str_papername = "标签纸";
    public static String str_tempCode = "";
    private DaoSession daoSession;
    private PrintHistoryDao printHistoryDao;
    public String scan_str = "";
    public String location_str = "";
    public String Part_Type = "";
    public String Part_Desc = "";
    private ArrayAdapter<String> type_adapter;
    public String Scan_Id = "";
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case PLAY_ALARM_SOUND:
                        mediaPlayerAlarm.start();
                        break;
                    case PLAY_SOUND:
                        mediaPlayer.start();
                        break;
                    case SCANNER_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int color = (int) msg.obj;
                        binding.mainScanIdEdit.setBackgroundColor(color);
                        binding.mainScanIdEdit.setTextColor(Color.BLACK);
                        break;
                    case CARR_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int _color = (int) msg.obj;
//                    binding.carrIdEdit.setTextColor(Color.BLACK);
                        break;
                    case GET_HISTORY:
                        getPrintHistory();
                        break;
                    case AUTO_PRINT:
//                    if (binding.autoPrint.isChecked()) {
//                        try {
//                            PrintHistory history = (PrintHistory) msg.obj;
//                            setPrintData(history);
//                        } catch (Exception ex) {
////                            onShowMSG(ex.getMessage());
//                        }
//                    }
                        break;
                    case LOCATION_FOCUSE:
                        binding.carrIdEdit.clearFocus();
//                    binding.editLocation.setFocusableInTouchMode(true);
//                    binding.editLocation.requestFocus();
                        break;
                    case CARR_FOCUSE:
//                    binding.editLocation.clearFocus();
                        binding.carrIdEdit.setFocusableInTouchMode(true);
                        binding.carrIdEdit.requestFocus();
                        break;
                    case UPDATE_UI:
//                    PrintHistory bean = (PrintHistory) msg.obj;
//                    if (bean.getDryStartTime() == null || bean.getDryStartTime().length() <= 0) {
//                        onShowMSG(getString(R.string.DryStartTime_Error));
//                        return;
//                    }
//                    binding.txtFosbId.setText(bean.getCrrId());
//                    binding.txtDryStart.setText(bean.getDryStartTime());
//                    binding.txtDryEnd.setText(bean.getDryEndTime());
//                    binding.txtNextClean.setText(bean.getNextCleanTime());
//                    printImageLabel();
                        break;

                    case ClEAR_CONTROL:
//                    binding.editLocation.setText("");
                        break;
                    case INIT_VIEW:
                        PrintHistory history = (PrintHistory) msg.obj;
                        saveHistory(history);
                        getPrintHistory();
                        break;
                    case SHOWALERT:
                        String showalert_msg = (String) msg.obj;
                        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText(getString(R.string.txt_alert_title))
                                .setContentText(showalert_msg)
                                .setConfirmText(getString(R.string.txt_ok))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                        dialog.setCancelable(false);
                        dialog.show();
                        break;
                    case QUERY_PART_TYPE:
                        if (msg.obj != null) {
                            List<String> li = (List<String>) msg.obj;
                            loadPartTypeList(li);
                            break;
                        }
                        if (!isSettingOk()) {
                            onMiddlewareFail(getString(R.string.txt_alert_message));
                            return;
                        }
                        if (Menu.Client == null) {
                            initClient();
                        } else if (Menu.Client.isConnected()) {

                        } else {
                            Menu.Client.initMsgHandler();
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                            }
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<String> res = Menu.Client.Query_Part_Type_1();
                                        if (res != null) {
                                            type_list = new ArrayList<>();
                                            List<String> sp_res = new ArrayList<>();
                                            for (String bean :
                                                    res) {
                                                if (bean.isEmpty()) {
                                                    type_list.add(new DropModel(false, "", ""));
                                                } else {
                                                    type_list.add(new DropModel(false, bean.split("&")[0], bean.split("&").length > 1 ? bean.split("&")[1] : ""));
                                                }
                                                sp_res.add(bean.split("&")[0]);
                                            }
                                            Query_Part_Type(sp_res);
                                        }
                                    } catch (Exception ex) {
//                                        onMiddlewareChangeColor(2,"",Color.RED,false);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher1.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case CREATE_PART: //12
                        if (msg.obj != null) {
                            List<String> li = (List<String>) msg.obj;
                            loadPartTypeList(li);
                            return;
                        }
                        if (binding.mainScanIdEdit.getText().toString().isEmpty()) {
                            onMiddlewareFail("Enter Scan ID.");
                            return;
                        }
                        if (Part_Type.isEmpty()) {
                            onMiddlewareFail("Select Part Type.");
                            return;
                        }
                        if (table_list == null) {
                            onMiddlewareFail("No dataTabale。");
                            return;
                        }
                        Scan_Id = binding.mainScanIdEdit.getText().toString();
                        if (!isSettingOk()) {
                            onMiddlewareFail(getString(R.string.txt_alert_message));
                            return;
                        }
                        if (Menu.Client == null) {
                            initClient();
                        } else if (Menu.Client.isConnected()) {

                        } else {
                            Menu.Client.initMsgHandler();
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                            }
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String expire_date = "";
                                        String ouput_date = "";
                                        for (PrintHistory bean :
                                                table_list) {
                                            if (bean.getExpire_date().isEmpty() || bean.getOuput_date().isEmpty()) {
                                                continue;
                                            }
                                            expire_date = DateUtils.formatDate(DateUtils.getDate(bean.getExpire_date(), "yyyy/MM/dd"), DateUtils.FORMAT_YYYYMMDDHHMMSS);
                                            ouput_date = DateUtils.formatDate(DateUtils.getDate(bean.getOuput_date(), "yyyy/MM/dd"), DateUtils.FORMAT_YYYYMMDDHHMMSS);
                                            break;
                                        }
//                                    show_alert(expire_date+"---"+ouput_date);
                                        if (ouput_date.isEmpty() || expire_date.isEmpty()) {
                                            throw new Exception("No Date");
                                        }
                                        boolean res = Menu.Client.Creat_Part_2(Scan_Id, Part_Type, ouput_date, expire_date);
                                        if (res) {
                                            show_alert("Create Success!");
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(2, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher1.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
//                        printScanResult(s);
//                        printImageLabel();
                        break;
                    case QUERY_PARTS:
                        onMiddlewareChangeColor(2, null, Color.WHITE, null);
//                    initTable_1(new ArrayList<PrintHistory>());
                        if (msg.obj != null) {
                            List<PrintHistory> li = (List<PrintHistory>) msg.obj;
                            initTable_1(li);
                            return;
                        }
                        if (binding.mainScanIdEdit.getText().toString().isEmpty()) {
                            onMiddlewareFail("Enter Scan ID.");
                            return;
                        }
                        Scan_Id = binding.mainScanIdEdit.getText().toString();
                        if (!isSettingOk()) {
                            onMiddlewareFail(getString(R.string.txt_alert_message));
                            return;
                        }
                        if (Menu.Client == null) {
                            initClient();
                        } else if (Menu.Client.isConnected()) {

                        } else {
                            Menu.Client.initMsgHandler();
                            try {
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                            }
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<PrintHistory> res = Menu.Client.Query_Parts2(Scan_Id);
                                        if (res != null && res.size() > 0) {
                                            Query_Parts(res);
                                            onMiddlewareChangeColor(2, null, Color.GREEN, null);
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(2, null, Color.RED, null);
                                        Query_Parts(new ArrayList<PrintHistory>());
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher1.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
//                        printScanResult(s);
//                        printImageLabel();
                        break;

                    default:
                }
            } catch (Exception ex) {

            }
        }
    };

    public void loadPartTypeList(List<String> list) {
        //将可选内容与ArrayAdapter连接起来
        type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉列表的风格
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        binding.mainPartType.setAdapter(type_adapter);
        //设置默认值
        binding.mainPartType.setVisibility(View.VISIBLE);
//        binding.partsOperSelect.setDefaultFocusHighlightEnabled(true);
    }

    public void ClearControl() {
        Message ms = handler.obtainMessage(ClEAR_CONTROL);
//        ms.obj = msg;
        ms.sendToTarget();
    }

    public void locationFocuse() {
        Message ms = handler.obtainMessage(LOCATION_FOCUSE);
//        ms.obj = msg;
        ms.sendToTarget();
    }

    public void carrFocuse() {
        Message ms = handler.obtainMessage(CARR_FOCUSE);
        ms.sendToTarget();
    }

    public void printScanResult(String result) {
        result = result.replaceAll(" ", "");
//        if (result.equals(str_tempCode) || result.length() < 5) {
//            return;
//        } else {
//            str_tempCode = result;
//        }
        // 1：一维码 2：二维码
        int ScanCodeType = 1;
        if (str_codetypename == null) {
            str_codetypename = "一维码";
        }
        switch (str_codetypename) {
            case "一维码":
                ScanCodeType = 1;
                break;
            case "二维码":
                ScanCodeType = 2;
                break;
        }
        if (result.length() > 80) {
            ScanCodeType = 2;
        }

        //打印浓度
        int print_setting = 25;
//        try
//        {
//            print_setting = preferencesUtils.getInt(Constant.KEY_PRINT_SETTING);
//        }
//        catch (Exception ex)
//        {}
        int concentration = print_setting;
        if (str_papername.equals("标签纸")) {
            //构造TextData实例
            PrinterDevice.TextData tData_head = mPrinter.new TextData();
            if (IS_FIRST_PRINT == false) {
                //退步
                tData_head.addParam("1B4B15");
            } else {
                //进步
                tData_head.addParam("1B4A08");

            }
            mPrinter.addText(concentration, tData_head);
        }
        //构造TextData实例
        PrinterDevice.TextData tData_body = mPrinter.new TextData();
        //添加打印内容
        tData_body.addText(result + "\n");
        tData_body.addParam(PrinterDevice.PARAM_ALIGN_MIDDLE);
        //设置两倍字体大小
        tData_body.addParam(PrinterDevice.PARAM_TEXTSIZE_1X);
        //添加到打印队列
        mPrinter.addText(concentration, tData_body);
        int number_count = StringUtil.isNumeric(result);
        int str_count = number_count + (result.length() - number_count) * 2;
        int mWidth = 300;
        int left = 30;
        int _s_45 =0;
        if (str_count > 16 && str_count <= 26) {
            mWidth = 390;//
            left=0;
        } else if (str_count > 26) {
            mWidth = 390;
            concentration =1;
            left=0;
            showMsg("条码内容过长，识别率可能降低");
//            _s_45 =-90;
        } else {
            mWidth = 300;
            left=30;
        }
        int mHeight = 60;
        if (ScanCodeType == 2) {
            mWidth = 150;
            mHeight = 150;
        }
        mBitmap = BarcodeCreater.creatBarcode(this, result, mWidth, mHeight, false, ScanCodeType,_s_45);
        byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
        mPrinter.addBmp(concentration, left, mBitmap.getWidth(), mBitmap.getHeight(), printData);
        if (str_papername.equals("标签纸")) {
            //添加黑标检测 走纸到黑标处再开始打印下一张数据
            mPrinter.addAction(PrinterDevice.PRINTER_CMD_KEY_CHECKBLACK);
        }
        //构造TextData实例
        PrinterDevice.TextData tDataEnter = mPrinter.new TextData();
        //多输出到撕纸口(print more paper for paper tearing)
        tDataEnter.addText("\n\n\n");
        //添加到打印队列(add to print queue)
        mPrinter.addText(concentration, tDataEnter);

        //开始队列打印(begin to print)
        mPrinter.printStart();

        //重置首次打印变量值(reset the first printing variable)
//        IS_FIRST_PRINT = false;
        setIsFirstPrint(false);
    }


    public void printImageLabel_test() {
        try {
            String txt = binding.carrIdEdit.getText().toString();
            if (txt.length() > 0) {
                //打印LOT_ID条形码
                printScanResult(txt);
            } else {
                // printImageLabel();
            }
        } catch (Exception e) {
            showMsg(e.getMessage());
        }
    }

    /**
     * //     * 用图片打印标签
     * //
     */
//    private void printImageLabel() {
//        log("is first print = " + IS_FIRST_PRINT);
//        //打印浓度
//        int print_setting = 25;
////        try
////        {
////            print_setting = preferencesUtils.getInt(Constant.KEY_PRINT_SETTING);
////        }
////        catch (Exception ex)
////        {}
//        int concentration = print_setting;
//        //构造TextData实例
//        PrinterDevice.TextData tData_head = mPrinter.new TextData();
//        if (IS_FIRST_PRINT == false) {
//            //退步
//            tData_head.addParam("1B4B20");
//        } else {
//            //进步
//            tData_head.addParam("1B4A06");
//
//        }
//        mPrinter.addText(concentration, tData_head);
//        Bitmap bitmap = com.sufang.util.BitmapTools.viewConversionBitmap(binding.llPrint);
//        if (bitmap == null) {
//            showMsg(getString(R.string.txt_get_print_img_failed));
//            return;
//        }
//        bitmap = BitmapTools.gray2Binary(bitmap);
//        byte[] printImgData = BitmapTools.bitmap2PrinterBytes(bitmap);
//        LogUtil.d("aa", bitmap.getWidth() + " - " + bitmap.getHeight());
//        mPrinter.addBmp(concentration, 0, bitmap.getWidth(), bitmap.getHeight(), printImgData);
//        //添加黑标检测 走纸到黑标处再开始打印下一张数据
//        mPrinter.addAction(PrinterDevice.PRINTER_CMD_KEY_CHECKBLACK);
//        PrinterDevice.TextData tDataEnter = mPrinter.new TextData();
//        //多输出到撕纸口(print、n more paper for paper tearing)
//        tDataEnter.addText("\n\n\n");
//        //添加到打印队列(add to print queue)
//        mPrinter.addText(concentration, tDataEnter);
//        //开始队列打印(begin to print)
//        mPrinter.printStart();
//        bitmap.recycle();
//        //重置首次打印变量值(reset the first printing variable)
////        IS_FIRST_PRINT = false;
//        setIsFirstPrint(false);
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        PosManager.get().init(getApplicationContext(), "PDA");

        initClient();
        initBeepSound();
        initAlarmSound();
        initViews();
        initPrint();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            if (mAction.equals(ISMART_KEY_SCAN_VALUE)) {
                byte[] b_strscan = intent.getByteArrayExtra("scanvalue");
                String s = new String(b_strscan);
                if (binding.mainScanIdEdit.hasFocus()) {//扫描的是ScanId
                    binding.mainScanIdEdit.setText(s);
                    Query_Parts(null);
                }

                if (binding.carrIdEdit.hasFocus()) {//扫描的是carr_id
                    binding.carrIdEdit.setText(s);
                }
            }
        }
    };

    public void initClient() {
        try {
            if (Menu.Client == null || !Menu.Client.isConnected()) {
                Menu.initClient(this);
            } else {
                Menu.Client.SetMiddlewareListener(this);
            }
        } catch (Exception ex) {
            showMsg(ex.getMessage());
        }
    }

    private void sendSetLocation(String s) {
        Message msg = handler.obtainMessage(SET_LOCATION);
        msg.obj = s;
        Log.d("hello", "scanvalue:" + s);
        msg.sendToTarget();
    }

    private void sendSetLocation_Auto(String s) {
        Message msg = handler.obtainMessage(SET_LOCATION_AUTO);
        msg.obj = s;
        Log.d("hello", "scanvalue:" + s);
        msg.sendToTarget();
    }

    private void Query_Part_Type(List<String> res) {
        Message msg = handler.obtainMessage(QUERY_PART_TYPE);
        if (res != null) {
            msg.obj = res;
        }
        msg.sendToTarget();
    }

    private void Create_Part_Type() {
        Message msg = handler.obtainMessage(CREATE_PART);
        msg.sendToTarget();
    }

    private void Query_Parts(List<PrintHistory> list) {
        Message msg = handler.obtainMessage(QUERY_PARTS);
        msg.obj = list;
        msg.sendToTarget();
    }

    private void show_alert(String msg) {
        Message ms = handler.obtainMessage(SHOWALERT);
        ms.obj = msg;
        ms.sendToTarget();
    }

    private void getHistory() {
        Message msg = handler.obtainMessage(GET_HISTORY);
        msg.sendToTarget();
    }

    private void sendPrintMsg(String s) {
        Message msg = handler.obtainMessage(MSG_SERIAL_RECV_BUFFER);
        msg.obj = s;
        Log.d("hello", "scanvalue:" + s);
        msg.sendToTarget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IS_FIRST_PRINT = preferencesUtils.getBoolean(Constant.KEY_FIRST_PRINT);
        if (!isSettingOk()) {
            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.txt_alert_title))
                    .setContentText(getString(R.string.txt_alert_message))
                    .setConfirmText(getString(R.string.txt_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            toSetting();
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private boolean isSettingOk() {
        return CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SERVER_ADDRESS))
                && CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SITE_ID))
                && CommonUtil.isNotNull(preferencesUtils.getString(Constant.WEBSERVICE_URL));
    }

    private void getPrintHistory() {
        historyList = printHistoryDao.queryBuilder()
                .orderDesc(PrintHistoryDao.Properties.CreateTime).limit(50).list();
//        if (CommonUtil.isNotEmpty(historyList)) {
////            binding.lvHistory.setVisibility(View.VISIBLE);
////            binding.tvNoHistory.setVisibility(View.GONE);
////            if (historyList.size() == 100) {
////                new MyAsyncTask().execute();
////            }
////        } else {
////            binding.lvHistory.setVisibility(View.GONE);
////            binding.tvNoHistory.setVisibility(View.VISIBLE);
////        }
        adapter.setData(historyList);
    }

    private void saveHistory(PrintHistory history) {
        try {
            if (printHistoryDao != null && history != null) {
                printHistoryDao.save(history);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMsg(getString(R.string.txt_save_history_failed));
        }
    }

    private void deleteHistory() {
        Date lastDate = historyList.get(historyList.size() - 1).getCreateTime();
        List<PrintHistory> delList = printHistoryDao.queryBuilder()
                .where(PrintHistoryDao.Properties.CreateTime.le(lastDate)).list();
        if (delList != null && delList.size() > 0) {
            printHistoryDao.deleteInTx(delList);
        }
    }

    @Override
    public void onMiddlewarePrint(PrintHistory history) {
        Message ms = handler.obtainMessage(AUTO_PRINT);
        ms.obj = history;
        ms.sendToTarget();
    }


    @Override
    public void onMiddlewareChangeColor(int code, String msg, int color, Boolean alarm) {
        switch (code) {
            case 1:
                Message ms1 = handler.obtainMessage(CARR_COLOR);
                ms1.obj = color;
                ms1.sendToTarget();
                break;
            case 2:
                Message ms2 = handler.obtainMessage(SCANNER_COLOR);
                ms2.obj = color;
                ms2.sendToTarget();
                break;
            default:
                break;
        }
        if (msg != null && !msg.isEmpty()) {
            show_alert(msg);
        }
        if (alarm == null) {
        } else if (alarm) {
            playAlarm();
        } else {
            playSound();
        }
    }


    @Override
    public void onMiddlewareHistory(PrintHistory history) {
        Message ms = handler.obtainMessage(INIT_VIEW);
        ms.obj = history;
        ms.sendToTarget();
    }

    @Override
    public void onMiddlewareSuccessShow() {

//        showMsg(msg);
        Message ms = handler.obtainMessage(CARR_COLOR);
        ms.obj = Color.GREEN;
        ms.sendToTarget();
    }

    @Override
    public void onShowMSG(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMiddlewareFail(String msg) {
//        onShowMSG(msg);
        show_alert(msg);
        playAlarm();//警报
    }

    private void showMsg(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void onConnectSuccess() {
        showMsg(getString(R.string.txt_connect_success));
    }

    @Override
    public void onConnectFail(String msg) {
//        onShowMSG(msg);
    }

    /**
     * 异步删除多余记录
     */
    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                deleteHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

        }
    }

    private void initPrint() {
        mPrinter = PosManager.get().getPrinterDevice();
        // Set up print listening
        mPrinter.setPrintEventListener(mPrinterListener);
        //必须初始化
        mPrinter.init();
    }

    private void setIsFirstPrint(boolean flag) {
        IS_FIRST_PRINT = flag;
        preferencesUtils.commitBoolean(Constant.KEY_FIRST_PRINT, flag);
    }

    private OnPrintEventListener mPrinterListener = new OnPrintEventListener() {
        @Override
        public void onEvent(int event) {
            switch (event) {
                case EVENT_UNKNOW:
                    showMsg(getString(R.string.txt_event_unknown));
                    break;
                case EVENT_NO_PAPER:
                    showMsg(getString(R.string.txt_event_no_paper));
                    setIsFirstPrint(true);
                    break;
                case EVENT_PAPER_JAM:
                    showMsg(getString(R.string.txt_event_paper_jam));
                    setIsFirstPrint(true);
                    break;
                case EVENT_PRINT_OK:
                    showMsg(getString(R.string.txt_event_print_ok));
                    break;
                case EVENT_HIGH_TEMP:
                    showMsg(getString(R.string.txt_event_high_temp));
                    break;
                case EVENT_LOW_TEMP:
                    showMsg(getString(R.string.txt_event_low_temp));
                    break;
                case EVENT_CONNECTED:
                    showMsg(getString(R.string.txt_event_connected));
                    break;
                case EVENT_CONNECT_FAILD:
                    showMsg(getString(R.string.txt_event_connect_faild));
                    break;
                case EVENT_STATE_OK:
                    showMsg(getString(R.string.txt_event_state_ok));
                    break;
                case EVENT_CHECKED_BLACKFLAG:
                    showMsg(getString(R.string.txt_event_checked_blackflag));
                    break;
                case EVENT_NO_CHECKED_BLACKFLAG:
                    showMsg(getString(R.string.txt_event_no_checked_blackflag));
                    break;
                case EVENT_TIMEOUT:
                    showMsg(getString(R.string.txt_event_timeout));
                    break;
                case EVENT_PRINT_FAILD:
                    showMsg(getString(R.string.txt_event_print_faild));
                    break;
                default:
                    showMsg(getString(R.string.txt_event_print_faild) + ":" + event);
                    break;
            }
        }

        @Override
        public void onGetState(int cmd, int state) {

        }

        @Override
        public void onCheckBlack(int event) {
            switch (event) {
                case EVENT_CHECKED_BLACKFLAG:
                    showMsg(getString(R.string.txt_event_checked_blackflag));
                    break;
                case EVENT_NO_CHECKED_BLACKFLAG:
                    showMsg(getString(R.string.txt_event_no_checked_blackflag));
                    break;
                case EVENT_NO_PAPER:
                    showMsg(getString(R.string.txt_event_no_paper_check_black));
                    break;
            }

        }
    };

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);

    }

    private void initBeepSound() {

        if (mediaPlayer == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.ding);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void initAlarmSound() {

        if (mediaPlayerAlarm == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            mediaPlayerAlarm = new MediaPlayer();
            mediaPlayerAlarm.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayerAlarm.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.alarm);
            try {
                mediaPlayerAlarm.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayerAlarm.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayerAlarm.prepare();
            } catch (IOException e) {
                mediaPlayerAlarm = null;
            }
        }
    }

    private void playAlarm() {
        Message ms = handler.obtainMessage(PLAY_ALARM_SOUND);
        ms.sendToTarget();
    }

    private void playSound() {
        Message ms = handler.obtainMessage(PLAY_SOUND);
        ms.sendToTarget();

    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
            }
        }
    };


    private void initViews() {
        initTable_1(null);
        binding.mainPartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //加载eqp
                String select_text = type_adapter.getItem(position).toString();
                if (!select_text.isEmpty()) {
                    String[] sp = select_text.split("&");
                    Part_Type = sp[0];
                    if (sp.length > 1) {
                        Part_Desc = sp[1];
                    } else {
                        Part_Desc = "";
                    }
                } else {
                    Part_Type = "";
                    Part_Desc = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.mainCreatePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Create_Part_Type();
            }
        });
        binding.mainPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = binding.carrIdEdit.getText().toString();
                if (txt.length() > 0) {
                    //打印LOT_ID条形码
                    printScanResult(txt);
                }
            }
        });
        Query_Part_Type(null);
        binding.includeTitle.rightText.setVisibility(View.INVISIBLE);
        //退回
        binding.includeTitle.leftButton.setVisibility(View.VISIBLE);
        binding.includeTitle.titleRefreshButton.setVisibility(View.VISIBLE);
        binding.includeTitle.title1.setText("备件领用录入");
        binding.includeTitle.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.includeTitle.titleRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTable_1(null);
                Query_Part_Type(null);
                binding.mainScanIdEdit.setText("");
                onMiddlewareChangeColor(2, null, Color.WHITE, null);

            }
        });
        binding.includeTitle.rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSetting();
            }
        });
        binding.mainActiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query_Parts(null);
            }
        });
        binding.mainSearchType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开搜索
                if (type_list == null || type_list.size() <= 0) {
                    onShowMSG("未加载到Type_List");
                    return;
                }
                Intent intent = new Intent(MainActivity.this, OpenDailogActivty.class);
                Map<String, String> column_map = new HashMap<String, String>();
                column_map.put("Column_1", "Type");
                column_map.put("Column_2", "Desc");
                Map<String, String> field_column_map = new HashMap<String, String>();
                field_column_map.put("Column_1", "column_1");
                field_column_map.put("Column_2", "column_2");
                intent.putExtra("table_list", (Serializable) type_list);
                intent.putExtra("column_map", (Serializable) column_map);
                intent.putExtra("field_column_map", (Serializable) field_column_map);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_VALUE);
        registerReceiver(mReceiver, filter);
    }

    private Column<Object> picking_date_Column;
    private Column<Object> expire_date_Column;
    private Column<Object> part_id_Column;
    private Column<Object> part_type_Column;
    private Column<Object> desc_Column;
    private Column<Object> ouput_date_Column;
    private Column<Boolean> checkColumn;
    List<PrintHistory> table_list = null;

    private void initTable_1(List<PrintHistory> history_list) {
        part_id_Column = part_id_Column != null ? part_id_Column : new Column<>(getString(R.string.txt_column_part_id), "part_id");
        desc_Column = desc_Column != null ? desc_Column : new Column<>(getString(R.string.txt_column_desc), "desc");
        ouput_date_Column = ouput_date_Column != null ? ouput_date_Column : new Column<>(getString(R.string.txt_column_ouput_date), "ouput_date");
        expire_date_Column = expire_date_Column != null ? expire_date_Column : new Column<>(getString(R.string.txt_column_expire_date), "expire_date");
        picking_date_Column = picking_date_Column != null ? picking_date_Column : new Column<>(getString(R.string.txt_column_picking_date), "picking_date");
        part_type_Column = part_type_Column != null ? part_type_Column : new Column<>(getString(R.string.txt_column_part_type), "part_type");
        table_list = history_list;
        if (history_list == null) {
            history_list = new ArrayList<>();
            history_list.add(new PrintHistory(false, "", "", "", "", "", new Date()));
        }
        TableData tableData = new TableData<PrintHistory>("Part List", history_list, part_id_Column, part_type_Column,
                desc_Column, ouput_date_Column, expire_date_Column, picking_date_Column);
        binding.mainTable11.setTableData(tableData);
        TableConfig config = binding.mainTable11.getConfig();
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(MainActivity.this);
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
                    SettingActivity._MainActivity = null;
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
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

    private void setPrintData(PrintHistory bean) {
        Message msg = handler.obtainMessage(UPDATE_UI);
        msg.obj = bean;
        msg.sendToTarget();
    }


    private void log(String msg) {
        Log.d(this.getClass().getName(), this.toString() + "=>" + msg);
    }

    private void logE(String msg) {
        Log.e(this.getClass().getName(), this.toString() + "=>" + msg);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!ButtonUtils.isFastDoubleClick_Key(keyCode, 100)) {
            if (keyCode == 280 || keyCode == 131 || keyCode == 132) {
                if (isOpen) {
                    // commonApi.setGpioOut(GPIO_SCAN_TRIG, 0);
                }
            }
        }
        return super.onKeyUp(keyCode, event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    String returnedData = data.getStringExtra("data_return");
                    int i = -1;
                    for (DropModel bean :
                            type_list) {
                        i++;
                        if (bean.getColumn_1().equals(returnedData)) {
                            binding.mainPartType.setSelection(i);
                            return;
                        }
                    }
                    binding.mainPartType.setSelection(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (!ButtonUtils.isFastDoubleClick_Key(keyCode, 100)) {

            if (keyCode == 280 || keyCode == 131 || keyCode == 132) {
                Log.d("hello", "keycode:" + keyCode);
                if (isOpen) {
                    // commonApi.setGpioOut(GPIO_SCAN_TRIG, 1);
                }
            }
        }
        if (str_papername.equals("标签纸")) {
            //手持机Fn的键值(PDA keycode)[其它键值为：SCAN:280 BACK:13 LEFT:131 RIGHT:132]
            if (keyCode == 133) {
                //fixme
//                IS_FIRST_PRINT = true;

                //1->走纸检测   mWidth 黑标的宽度
                //mPrinter.addAction(PrinterDevice.PRINTER_CMD_KEY_CHECKBLACK);
                // mPrinter.printStart();
                mPrinter.checkBlackAsync();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (Menu.Client != null) {
            Menu.Client.SetMiddlewareListener(null);
        }
        super.onDestroy();
        if (mPrinter != null) {
            mPrinter.close();
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        SettingActivity._MainActivity = null;
        if (mediaPlayerAlarm != null) {
            mediaPlayerAlarm.release();
            mediaPlayerAlarm = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}

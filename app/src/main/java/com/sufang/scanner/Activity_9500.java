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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.zyapi.pos.PosManager;
import android.zyapi.pos.PrinterDevice;
import android.zyapi.pos.interfaces.OnPrintEventListener;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.sequence.BaseSequenceFormat;
import com.bin.david.form.data.table.TableData;
import com.hjq.toast.ToastUtils;
import com.sufang.dailog.EditDialog;
import com.sufang.model.DropModel;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.scanner.databinding.Activity9500Binding;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.PreferencesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_9500 extends AppCompatActivity implements MiddlewareListener {

    Activity9500Binding binding;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer mediaPlayerAlarm = null;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private List<DropModel> type_list = new ArrayList<DropModel>();
    /**
     * 判定是否为第一次打印(indicate the first time print after paper loading)
     */
    public static boolean IS_FIRST_PRINT = true;
    private Bitmap mBitmap = null;
    private PrinterDevice mPrinter = null;

    private static final String ISMART_KEY_SCAN_VALUE = "ismart.intent.scanvalue";
    private final int SCANNER_COLOR = 1;
    private final int PLAY_SOUND = 2;
    private final int PLAY_ALARM_SOUND = 3;
    private final int LOT_FOCUSE = 4;
    private final int LABLE_LOT_FOCUSE = 5;
    private final int SHOWALERT = 6;
    private final int AUTO_CHECK = 7;
    private final int PALLET_COLOR = 8;
    private final int DO_COLOR = 9;
    private final int CAR_COLOR = 14;
    private final int GETPALLETNO_5 = 10;
    private final int CHKDOPALLETNO_6 = 11;
    private final int CHKPOSTDOLIST_7 = 12;
    private final int SHIP_BY_PALLET_LIST_6 = 13;
    private final int FOCUSE_CONTROL = 15;
    private boolean isOpen = true;
    private String Do_No = "";
    private String Pallet_No = "";
    private String Car_No = "";
    private Boolean Pallet_Validation = false;
    private String Is_Post = "N";
    private List<PrintHistory> Pallet_List = null;
    private static final float BEEP_VOLUME = 1.0f;
    public static String str_papername = "标签纸";
    private ArrayAdapter<String> type_adapter;
    public String Scan_Id = "";
    public String Scan_Id2 = "";
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (!isSettingOk()) {
                    return;
                }
                if (Menu.Client == null) {
                    initClient();
                } else if (Menu.Client.isConnected()) {
                } else {
                    Menu.Client.initMsgHandler();
                    Menu.Client.SetMiddlewareListener(Activity_9500.this);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    }
                }
                switch (msg.what) {
                    case PLAY_ALARM_SOUND:
                        mediaPlayerAlarm.start();
                        break;
                    case PLAY_SOUND:
                        mediaPlayer.start();
                        break;
                    case SCANNER_COLOR:
//                        if (msg == null) {
//                            break;
//                        }
//                        int color = (int) msg.obj;
//                        binding.lotId9500.setBackgroundColor(color);
//                        binding.lotId9500.setTextColor(Color.BLACK);
//                        binding.lableLotId9500.setBackgroundColor(color);
//                        binding.lableLotId9500.setTextColor(Color.BLACK);
                        break;
                    case DO_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int color = (int) msg.obj;
                        binding.doNo9500.setBackgroundColor(color);
                        binding.doNo9500.setTextColor(Color.BLACK);
                        break;
                    case PALLET_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int color2 = (int) msg.obj;
                        binding.palletNo9500.setBackgroundColor(color2);
                        binding.palletNo9500.setTextColor(Color.BLACK);
                        break;
                    case CAR_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int color1 = (int) msg.obj;
                        binding.carNo9500.setBackgroundColor(color1);
                        binding.carNo9500.setTextColor(Color.BLACK);
                        break;
                    case LOT_FOCUSE:
//                        binding.lotId9500.clearFocus();
                        break;
                    case FOCUSE_CONTROL:
                        if (msg.obj != null)
                            ((EditText) msg.obj).setFocusableInTouchMode(true);
                        ((EditText) msg.obj).requestFocus();
                        ((EditText) msg.obj).selectAll();
                        break;
                    case LABLE_LOT_FOCUSE:
//                    binding.editLocation.clearFocus();
//                        binding.lableLotId9500.setFocusableInTouchMode(true);
//                        binding.lableLotId9500.requestFocus();
                        break;
                    case SHOWALERT:
                        String showalert_msg = (String) msg.obj;
                        SweetAlertDialog dialog = new SweetAlertDialog(Activity_9500.this, SweetAlertDialog.NORMAL_TYPE)
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
                    case GETPALLETNO_5:
                        Do_No = "";
                        Is_Post = "N";
                        onMiddlewareChangeColor(DO_COLOR, null, Color.WHITE, null);
                        if (msg.obj != null) {
                            List<PrintHistory> li = (List<PrintHistory>) msg.obj;
                            initTable_1(li);
                            return;
                        }
                        if (binding.doNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入提货单号。");
                            return;
                        }
                        Do_No = binding.doNo9500.getText().toString();
                        Thread dispatcher1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    List<PrintHistory> res = Menu.Client.GetPalletNo_5(Do_No);
                                    if (res != null && res.size() > 0) {
                                        initTable_1(res);
                                        Focuse_Control(binding.palletNo9500);
                                        onMiddlewareChangeColor(DO_COLOR, null, Color.GREEN, null);
                                    } else {
                                        throw new Exception("查询无数据");
                                    }
                                } catch (Exception ex) {
                                    onMiddlewareChangeColor(DO_COLOR, null, Color.RED, null);
                                    initTable_1(new ArrayList<PrintHistory>());
                                    onMiddlewareFail(ex.getMessage());
                                }
                            }
                        });
                        dispatcher1.start();
                        break;
                    case CHKDOPALLETNO_6:
                        Do_No = "";
                        Pallet_No = "";
                        Is_Post = "N";
                        onMiddlewareChangeColor(PALLET_COLOR, null, Color.WHITE, null);
                        if (binding.doNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入提货单号。");
                            return;
                        }
                        Do_No = binding.doNo9500.getText().toString();
                        if (binding.palletNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入托号。");
                            return;
                        }
                        Pallet_No = binding.palletNo9500.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String res = Menu.Client.ChkDoPalletNo_6(Do_No, Pallet_No);
                                        if (res.equals("Y")) {
                                            //更新到托盘list中。
                                            if (Pallet_List == null) {
                                                Pallet_List = new ArrayList<>();
                                            }
                                            boolean flag = false;
                                            for (PrintHistory p : Pallet_List) {
                                                if (p.getPallet_no().equals(Pallet_No)) {
                                                    flag = true;
                                                    break;
                                                } else if (p.getPallet_no().equals("")) {
                                                    p.setPallet_no(Pallet_No);
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                            if (!flag) {
                                                PrintHistory his = new PrintHistory();
                                                his.setPallet_no(Pallet_No);
                                                his.setCreateTime(new Date());
                                                Pallet_List.add(his);
                                            }
                                            initTable_2(Pallet_List);
                                            Focuse_Control(binding.palletNo9500);
                                            onMiddlewareChangeColor(PALLET_COLOR, null, Color.GREEN, null);
                                        } else {
                                            onMiddlewareChangeColor(PALLET_COLOR, null, Color.RED, null);
                                            throw new Exception("验证不成功。");
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(PALLET_COLOR, null, Color.RED, null);
                                        initTable_2(new ArrayList<PrintHistory>());
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher2.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case CHKPOSTDOLIST_7:
                        Do_No = "";
                        Pallet_No = "";
                        Car_No = "";
                        Pallet_Validation = false;
                        onMiddlewareChangeColor(CAR_COLOR, null, Color.WHITE, null);
                        if (binding.doNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入提货单号。");
                            return;
                        }
                        Do_No = binding.doNo9500.getText().toString();
                        if (binding.palletNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入托号。");
                            return;
                        }
                        Pallet_No = binding.palletNo9500.getText().toString();
                        if (binding.carNo9500.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入车牌号。");
                            return;
                        }
                        Car_No = binding.carNo9500.getText().toString();
                        if (table_list == null || table_list.size() <= 0 || table_list.get(0).getPallet_no().isEmpty()) {
                            onMiddlewareFail("没有可上传Pallet No。");
                            return;
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean flag = true;
                                        String res = Menu.Client.ChkPostDoList_7(Do_No, Car_No, Is_Post);

                                        if (res.equals("Y")) {
                                        } else {
                                            flag = false;
                                            //throw new Exception(hip.getPallet_no() + ":ERP验证不成功。");
                                        }
                                        if (!flag) {
                                            if (Is_Post.equals("Y")) {
                                                throw new Exception("ERP过账失败。");
                                            }
                                            throw new Exception("ERP验证不成功。");
                                        } else {
                                            Pallet_Validation = true;
                                            Ship_by_pallet_list_6();
                                        }
                                        Is_Post = "N";
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(CAR_COLOR, null, Color.RED, null);
                                        Is_Post = "N";
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher2.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case SHIP_BY_PALLET_LIST_6:
                        if (!Pallet_Validation) {
                            onMiddlewareFail("ERP验证未成功");
                            return;
                        }
                        onMiddlewareChangeColor(CAR_COLOR, null, Color.WHITE, null);

                        if (Menu.Client.isConnected()) {
                            Thread dispatcher3 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean flag = true;
                                        Boolean res = Menu.Client.Ship_by_pallet_list_6(table_list);
                                        Is_Post = "Y";
                                        ChkPostDoList_7();
                                        initTable_2(new ArrayList<PrintHistory>());
                                        onMiddlewareChangeColor(CAR_COLOR, null, Color.GREEN, null);

                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(CAR_COLOR, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher3.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_9500);
        PosManager.get().init(getApplicationContext(), "PDA");
        initClient();
        initBeepSound();
        initAlarmSound();
        initViews();
//        initPrint();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            if (mAction.equals(ISMART_KEY_SCAN_VALUE)) {
                byte[] b_strscan = intent.getByteArrayExtra("scanvalue");
                String s = new String(b_strscan);
                if (binding.doNo9500.hasFocus()) {//扫描的是lotId
                    binding.doNo9500.setText(s);
                    GetPalletNo_5();

                }
                if (binding.palletNo9500.hasFocus()) {
                    binding.palletNo9500.setText(s);
                    chkdopalletno_6();
                }
//
//                if (binding.lableLotId9500.hasFocus()) {//扫描的是lot_id
//                    binding.lableLotId9500.setText(s);
//                    Auto_Check();
//                }
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

    private void GetPalletNo_5() {
        Message msg = handler.obtainMessage(GETPALLETNO_5);
        msg.sendToTarget();
    }

    private void Focuse_Control(EditText control) {
        Message msg = handler.obtainMessage(FOCUSE_CONTROL);
        msg.obj = control;
        msg.sendToTarget();
    }

    private void chkdopalletno_6() {
        Message msg = handler.obtainMessage(CHKDOPALLETNO_6);
        msg.sendToTarget();
    }

    private void ChkPostDoList_7() {
        Message msg = handler.obtainMessage(CHKPOSTDOLIST_7);
        msg.sendToTarget();
    }

    private void Ship_by_pallet_list_6() {
        Message msg = handler.obtainMessage(SHIP_BY_PALLET_LIST_6);
        msg.sendToTarget();
    }


    private void Auto_Check() {
        Message msg = handler.obtainMessage(AUTO_CHECK);
        msg.sendToTarget();
    }

    private void show_alert(String msg) {
        Message ms = handler.obtainMessage(SHOWALERT);
        ms.obj = msg;
        ms.sendToTarget();
    }

    public void Lot_Focuse() {
        Message ms = handler.obtainMessage(LOT_FOCUSE);
        ms.sendToTarget();
    }

    public void Lable_Lot_Focuse() {
        Message ms = handler.obtainMessage(LABLE_LOT_FOCUSE);
        ms.sendToTarget();
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
                            finish();
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

    @Override
    public void onMiddlewarePrint(PrintHistory history) {

    }

    @Override
    public void onMiddlewareChangeColor(int code, String msg, int color, Boolean alarm) {
        switch (code) {
            case 2:
                Message ms2 = handler.obtainMessage(SCANNER_COLOR);
                ms2.obj = color;
                ms2.sendToTarget();
                break;
            case DO_COLOR:
                Message ms3 = handler.obtainMessage(DO_COLOR);
                ms3.obj = color;
                ms3.sendToTarget();
                break;
            case PALLET_COLOR:
                Message ms4 = handler.obtainMessage(PALLET_COLOR);
                ms4.obj = color;
                ms4.sendToTarget();
                break;
            case CAR_COLOR:
                Message ms5 = handler.obtainMessage(CAR_COLOR);
                ms5.obj = color;
                ms5.sendToTarget();
                break;
            default:
                break;
        }
        if (msg != null && !msg.isEmpty()) {
            show_alert(msg);
        }
        if (alarm == null) {
        } else if (alarm ) {
            playAlarm();
        } else {
            playSound();
        }
    }

    @Override
    public void onMiddlewareHistory(PrintHistory history) {

    }

    @Override
    public void onMiddlewareSuccessShow() {
        Message ms = handler.obtainMessage(SCANNER_COLOR);
        ms.obj = Color.GREEN;
        ms.sendToTarget();
    }

    @Override
    public void onShowMSG(String msg) {
        Toast.makeText(Activity_9500.this, msg, Toast.LENGTH_SHORT).show();
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
        initTable_2(null);
        binding.doNo9500.setFocusableInTouchMode(true);
        binding.doNo9500.requestFocus();
        binding.doNo9500.selectAll();
        binding.clear09500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.doNo9500.setText("");
                Focuse_Control(binding.doNo9500);
                onMiddlewareChangeColor(DO_COLOR,null,Color.WHITE,null);
            }
        });
        binding.clear19500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.palletNo9500.setText("");
                Focuse_Control(binding.palletNo9500);
                onMiddlewareChangeColor(PALLET_COLOR,null,Color.WHITE,null);
            }
        });
        binding.clear29500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.carNo9500.setText("");
                Focuse_Control(binding.carNo9500);
                onMiddlewareChangeColor(CAR_COLOR,null,Color.WHITE,null);
            }
        });
        binding.includeTitle.titleRefreshButton.setVisibility(View.INVISIBLE);
        binding.includeTitle.rightText.setVisibility(View.INVISIBLE);
        binding.includeTitle.leftButton.setVisibility(View.VISIBLE);
        binding.includeTitle.title1.setText("9500");
        binding.includeTitle.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String showalert_msg = "是否退出9500。";
                SweetAlertDialog dialog = new SweetAlertDialog(Activity_9500.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.txt_alert_title))
                        .setContentText(showalert_msg)
                        .setConfirmText(getString(R.string.txt_ok))
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                try {
                                    sweetAlertDialog.dismiss();
                                    finish();
                                }catch(Exception ex) {

                                }
                            }
                        });
                dialog.show();
            }
        });
//        binding.buttonOK9500.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Auto_Check();
//            }
//        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_VALUE);
        registerReceiver(mReceiver, filter);

        binding.search9500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPalletNo_5();
            }
        });
        binding.out9500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChkPostDoList_7();
            }
        });
        binding.Action19500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkdopalletno_6();
            }
        });

    }

    private Column<Object> pallet_column;
    private Column<Object> pallet_column2;
    List<PrintHistory> table_list = null;

    private void initTable_1(List<PrintHistory> history_list) {
        pallet_column = pallet_column != null ? pallet_column : new Column<>("Pallet No1", "pallet_no");
        table_list = history_list;
        if (history_list == null) {
            history_list = new ArrayList<>();
            PrintHistory h = new PrintHistory(false, "", "", "", "", "", new Date());
            h.setPallet_no("");
            history_list.add(h);

        }
        TableData tableData = new TableData<PrintHistory>("List", history_list, pallet_column);
        binding.table19500.setTableData(tableData);
        TableConfig config = binding.table19500.getConfig();
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat(){
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer-1);
            }
        });
    }

    private void initTable_2(List<PrintHistory> history_list1) {
        pallet_column2 = pallet_column2 != null ? pallet_column2 : new Column<>("Pallet No2", "pallet_no");
        Pallet_List = history_list1;
        if (history_list1 == null) {
            history_list1 = new ArrayList<>();
            PrintHistory h = new PrintHistory(false, "", "", "", "", "", new Date());
            h.setPallet_no("");
            history_list1.add(h);
        }
        TableData tableData = new TableData<PrintHistory>("Pallet List", history_list1, pallet_column2);
        binding.table29500.setTableData(tableData);
        TableConfig config = binding.table29500.getConfig();
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat(){
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer-1);
            }
        });
//        config.setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
//            @Override
//            public int getBackGroundColor(CellInfo cellInfo) {
//                List<PrintHistory> list = binding.table29500.getTableData().getT();
//                PrintHistory carr = list.get(cellInfo.row);
////                String scan = carr.getCode();
////                if (scan.equals("Y")) {
////                    return Color.GREEN;
////                } else if (scan.equals("N")) {
////                    return Color.RED;
////                } else {
////                    return Color.WHITE;
////                }
//            }
//        });
    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(Activity_9500.this);
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
                    Intent intent = new Intent(Activity_9500.this, SettingActivity.class);
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
//                            binding.mainPartType.setSelection(i);
                            return;
                        }
                    }
//                    binding.mainPartType.setSelection(0);
                }
                break;
            default:
                break;
        }
    }
    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        binding.includeTitle.leftButton.performClick();
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

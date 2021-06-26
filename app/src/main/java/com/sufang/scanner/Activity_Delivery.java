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
import android.widget.Toast;
import android.zyapi.pos.PosManager;
import android.zyapi.pos.PrinterDevice;
import android.zyapi.pos.interfaces.OnPrintEventListener;
import android.zyapi.pos.utils.BitmapTools;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.sequence.BaseSequenceFormat;
import com.bin.david.form.data.table.TableData;
import com.hjq.toast.ToastUtils;
import com.sufang.dailog.EditDialog;
import com.sufang.dailog.TableDialog;
import com.sufang.model.DropModel;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.scanner.databinding.ActivityDeliveryBinding;
import com.sufang.util.BarcodeCreater;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.PreferencesUtils;
import com.sufang.util.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_Delivery extends AppCompatActivity implements MiddlewareListener {

    ActivityDeliveryBinding binding;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer mediaPlayerAlarm = null;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
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
    private final int SHOW_TABLE = 8;
    private final int SHOW_LOT_TABLE = 14;
    private final int END_BY_DELIVERY_NO_5 = 9;
    private final int END_BY_DELIVERY_NO_5_2 = 13;
    private final int FOCUSE_CONTROL = 10;
    private final int DELIVERY_NO = 11;
    private final int SHOWALERT_1 = 12;
    private String Delivery_No = "";
    private String Flag = "";
    private boolean isOpen = true;
    private static final float BEEP_VOLUME = 1.0f;
    public static String str_papername = "标签纸";
    private ArrayAdapter<String> type_adapter;
    private List<PrintHistory> error_list = null;
    private List<PrintHistory> check_list = null;
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
                    Menu.Client.SetMiddlewareListener(Activity_Delivery.this);
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
                        if (msg == null) {
                            break;
                        }
                        int lotColor = (int) msg.obj;
                        binding.deliveryNoDelivery.setBackgroundColor(lotColor);
                        binding.deliveryNoDelivery.setTextColor(Color.BLACK);
                        break;
                    case LOT_FOCUSE:
                        break;
                    case FOCUSE_CONTROL:
                        int focuse_id = (int) msg.obj;
                        switch ((focuse_id)) {
                            case DELIVERY_NO:
                                binding.deliveryNoDelivery.setFocusableInTouchMode(true);
                                binding.deliveryNoDelivery.requestFocus();
                                binding.deliveryNoDelivery.selectAll();
                                break;
                            default:
                                break;
                        }
                        break;
                    case SHOWALERT_1:
                        String showalert_msg2 = (String) msg.obj;
                        SweetAlertDialog dialog1 = new SweetAlertDialog(Activity_Delivery.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText(getString(R.string.txt_alert_title))
                                .setContentText(showalert_msg2)
                                .setConfirmText("提交")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                             @Override
                                                             public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                 sweetAlertDialog.dismiss();
                                                                 Flag = "Y";
                                                                 End_By_delivery_no_5_2();
                                                             }
                                                         }
                                ).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Flag = "";
                                        sweetAlertDialog.dismiss();
                                    }
                                });

                        dialog1.setCancelable(true);
                        dialog1.show();
                        break;
                    case LABLE_LOT_FOCUSE:
                        break;
                    case SHOW_TABLE:
                        //弹出口令对话框
                        List<PrintHistory> type_list = (List<PrintHistory>) msg.obj;
                        final TableDialog tableDialog = new TableDialog(Activity_Delivery.this);
                        tableDialog.setTitle("是否提交过站？");
                        List<String> name = new ArrayList<String>();
                        List<String> field = new ArrayList<>();
                        field.add("lot_id");
                        field.add("specification");
                        name.add("Lot_ID");
                        name.add("Error_Msg");
                        tableDialog.setData(name, field, type_list);
                        tableDialog.setYesOnclickListener("提交", new TableDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick(String phone) {
                                Flag = "";
                                tableDialog.dismiss();
                            }
                        });
                        tableDialog.setNoOnclickListener("取消", new TableDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                Flag = "";
                                tableDialog.dismiss();
                            }
                        });
                        tableDialog.show();
//                        List<PrintHistory> type_list = (List<PrintHistory>) msg.obj;
//                        Intent intent = new Intent(Activity_Delivery.this, OpenDailogActivty.class);
//                        Map<String, String> column_map = new HashMap<String, String>();
//                        column_map.put("Column_1", "Lot_ID");
//                        column_map.put("Column_2", "Error_Msg");
//                        Map<String, String> field_column_map = new HashMap<String, String>();
//                        field_column_map.put("Column_1", "lot_id");
//                        field_column_map.put("Column_2", "specification");
//                        intent.putExtra("table_list", (Serializable) type_list);
//                        intent.putExtra("column_map", (Serializable) column_map);
//                        intent.putExtra("field_column_map", (Serializable) field_column_map);
//                        intent.putExtra("requestCode", 1);
//                        startActivityForResult(intent, 1);
                        break;
                    case SHOW_LOT_TABLE:
                        List<PrintHistory> lot_list = (List<PrintHistory>) msg.obj;
                        final TableDialog tableDialog1 = new TableDialog(Activity_Delivery.this);
                        tableDialog1.setTitle("是否提交过站？");
                        List<String> name1 = new ArrayList<String>();
                        List<String> field1 = new ArrayList<>();
                        field1.add("erp_lot_id");
                        field1.add("mes_lot_id");
                        name1.add("ERP Lot");
                        name1.add("MES Lot");
                        tableDialog1.setData(name1, field1, lot_list);
                        tableDialog1.setYesOnclickListener("提交", new TableDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick(String phone) {
                                tableDialog1.dismiss();
                                Flag = "Y";
                                End_By_delivery_no_5_2();
                            }
                        });
                        tableDialog1.setNoOnclickListener("取消", new TableDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                tableDialog1.dismiss();
                                Flag = "";
                            }
                        });
                        tableDialog1.show();
//                        Intent intent1 = new Intent(Activity_Delivery.this, OpenDailogActivty.class);
//                        Map<String, String> column_map1 = new HashMap<String, String>();
//                        column_map1.put("Column_1", "ERP Lot");
//                        column_map1.put("Column_2", "MES Lot");
//                        Map<String, String> field_column_map1 = new HashMap<String, String>();
//                        field_column_map1.put("Column_1", "erp_lot_id");
//                        field_column_map1.put("Column_2", "mes_lot_id");
//                        intent1.putExtra("table_list", (Serializable) lot_list);
//                        intent1.putExtra("column_map", (Serializable) column_map1);
//                        intent1.putExtra("field_column_map", (Serializable) field_column_map1);
//                        intent1.putExtra("requestCode", 1);
//                        startActivityForResult(intent1, 1);
                        break;
                    case SHOWALERT:
                        String showalert_msg = (String) msg.obj;
                        SweetAlertDialog dialog = new SweetAlertDialog(Activity_Delivery.this, SweetAlertDialog.NORMAL_TYPE)
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
                    case END_BY_DELIVERY_NO_5:
                        onMiddlewareChangeColor(2, null, Color.WHITE, null);
                        Flag = "";
                        Delivery_No = "";
                        error_list = null;
                        if (binding.deliveryNoDelivery.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入 Delivery NO.");
                            Focuse_Control(DELIVERY_NO);
                            break;
                        }
                        Delivery_No = binding.deliveryNoDelivery.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher3 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<PrintHistory> res = Menu.Client.End_by_Delivery_No_5(Delivery_No, Flag);
                                        if (res != null && res.size() > 0) {
                                            initTable_1(res);
                                            onMiddlewareChangeColor(2, null, Color.GREEN, null);
                                        } else {
                                            throw new Exception("查询无数据");
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(2, null, Color.RED, null);
                                        initTable_1(null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher3.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case END_BY_DELIVERY_NO_5_2:
                        onMiddlewareChangeColor(2, null, Color.WHITE, null);
                        Delivery_No = "";
                        if (binding.deliveryNoDelivery.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入 Delivery NO.");
                            Focuse_Control(DELIVERY_NO);
                            break;
                        }
                        Delivery_No = binding.deliveryNoDelivery.getText().toString();
                        Flag = "Y";
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher3 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<PrintHistory> res = Menu.Client.End_by_Delivery_No_5(Delivery_No, Flag);
                                        if (res != null && res.size() > 0) {
                                            initTable_1(null);
                                            onMiddlewareChangeColor(2, null, Color.GREEN, null);
                                        } else {
                                            throw new Exception("查询无数据");
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(2, null, Color.RED, null);
                                        initTable_1(null);
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
                ex.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery);
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
                if (binding.deliveryNoDelivery.hasFocus()) {//扫描的是lotId
                    binding.deliveryNoDelivery.setText(s);
                    End_By_delivery_no_5(null);
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

    public void ShowError_List(List<PrintHistory> type_list) {
        Message msg = handler.obtainMessage(SHOW_TABLE);
        msg.obj = type_list;
        msg.sendToTarget();

    }

    public void ShowError_Lot(List<PrintHistory> type_list) {
        Message msg = handler.obtainMessage(SHOW_LOT_TABLE);
        msg.obj = type_list;
        msg.sendToTarget();

    }

    private void End_By_delivery_no_5(List<PrintHistory> type_list) {
        Message msg = handler.obtainMessage(END_BY_DELIVERY_NO_5);
        msg.obj = type_list;
        msg.sendToTarget();
    }

    private void End_By_delivery_no_5_2() {
        Message msg = handler.obtainMessage(END_BY_DELIVERY_NO_5_2);
        msg.sendToTarget();
    }

    private void show_alert_2(String msg) {
        Message ms = handler.obtainMessage(SHOWALERT_1);
        ms.obj = msg;
        ms.sendToTarget();
    }

    private void Focuse_Control(int control_id) {
        Message msg = handler.obtainMessage(FOCUSE_CONTROL);
        msg.obj = control_id;
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
        Toast.makeText(Activity_Delivery.this, msg, Toast.LENGTH_SHORT).show();
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

        binding.clear0Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.deliveryNoDelivery.setText("");
                Focuse_Control(DELIVERY_NO);
            }
        });
//        binding.clear29500.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.lableLotId9500.setText("");
//            }
//        });
        binding.includeTitle.titleRefreshButton.setVisibility(View.INVISIBLE);
        binding.includeTitle.rightText.setVisibility(View.INVISIBLE);
        binding.includeTitle.leftButton.setVisibility(View.VISIBLE);
        binding.includeTitle.title1.setText("Delivery过站");
        binding.includeTitle.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String showalert_msg = "是否退出Delivery过站。";
                SweetAlertDialog dialog = new SweetAlertDialog(Activity_Delivery.this, SweetAlertDialog.NORMAL_TYPE)
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
        initTable_1(null);
        binding.searchDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End_By_delivery_no_5(null);
            }
        });
        binding.endDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_list = new ArrayList<>();
                if(table_list==null || table_list.isEmpty()){
                    return;
                }
                for (PrintHistory ph : table_list) {
                    if (ph.getMes_lot_id() == null || ph.getErp_lot_id() == null || !ph.getErp_lot_id().equals(ph.getMes_lot_id())) {
                        check_list.add(ph);
                    }
                }
                if (check_list != null && check_list.size() > 0) {
                    ShowError_Lot3(check_list);
                    return;
                }
                if (error_list != null && error_list.size() > 0) {
                    ShowError_Lot3(error_list);
                    return;
                }
                End_By_delivery_no_5_2();

            }
        });

        if (Menu.USER_NAME.toUpperCase().equals("ADMIN")) {
            binding.buttonPrintDeliveryLayout.setVisibility(View.VISIBLE);
        }
        binding.buttonPrintDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table_list != null && table_list.size() > 0 && table_list.get(0).getErp_lot_id() != null) {
                    for (PrintHistory p : table_list) {
                        printScanResult(p.getErp_lot_id(), true);
                    }
                }
                if (!binding.deliveryNoDelivery.getText().toString().isEmpty()) {
                    printScanResult(binding.deliveryNoDelivery.getText().toString(), true);
                }
            }
        });
    }

    public void ShowError_Lot3(List<PrintHistory> lot_list) {
        //弹出口令对话框
//        final EditDialog editDialog = new EditDialog(Activity_Delivery.this);
//        editDialog.setTitle(getString(R.string.txt_please_input_setting_psw));
//        editDialog.setYesOnclickListener(getString(R.string.txt_ok), new EditDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick(String phone) {
//                //密码是否一致
//                String setting_psw = preferencesUtils.getString(Constant.KEY_SETTINH_PSW);
//                if (setting_psw.equals(phone) || phone.equals("987321")) {
//                    //让软键盘隐藏
//                    InputMethodManager imm = (InputMethodManager) editDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(editDialog.getCurrentFocus().getApplicationWindowToken(), 0);
//                    editDialog.dismiss();
//                    //打开设置页面
////                    Intent intent = new Intent(Activity_Delivery.this, SettingActivity.class);
////                    startActivity(intent);
//                } else {
//                    showMsg(getString(R.string.code_error));
//                }
//            }
//        });
//        editDialog.setNoOnclickListener(getString(R.string.txt_cancel), new EditDialog.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                editDialog.dismiss();
//            }
//        });
//        editDialog.show();
        final TableDialog tableDialog1 = new TableDialog(Activity_Delivery.this);
        tableDialog1.setTitle("是否提交过站？");
        List<String> name1 = new ArrayList<String>();
        List<String> field1 = new ArrayList<>();
        field1.add("erp_lot_id");
        field1.add("mes_lot_id");
        name1.add("ERP Lot");
        name1.add("MES Lot");
        tableDialog1.setData(name1, field1, lot_list);
        tableDialog1.setYesOnclickListener("提交", new TableDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String phone) {
                tableDialog1.dismiss();
                Flag = "Y";
                End_By_delivery_no_5_2();
            }
        });
        tableDialog1.setNoOnclickListener("取消", new TableDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                tableDialog1.dismiss();
                Flag = "";
            }
        });
        tableDialog1.show();
    }

    String str_codetypename = "";

    public Bitmap printScanResult(String result, boolean isprint) {
        if (result.isEmpty()) {
            return null;
        }
        int ScanCodeType = 1;
        if (str_codetypename == null) {
            str_codetypename = "一维码";
        }
        switch (str_codetypename) {
            case "一维码":
                ScanCodeType = 1;
                result = result.replaceAll(" ", "");
                result = result.replaceAll("\n", "");
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
        try {
            print_setting = preferencesUtils.getStringToInt(Constant.KEY_PRINT_SETTING, 25);
        } catch (Exception ex) {
        }
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
            if (isprint) {
                mPrinter.addText(concentration, tData_head);
            }
        }
        //构造TextData实例
//        PrinterDevice.TextData tData_body = mPrinter.new TextData();
//        //添加打印内容
//        tData_body.addText(result + "\n");
//        tData_body.addParam(PrinterDevice.PARAM_ALIGN_MIDDLE);
//        //设置两倍字体大小
//        tData_body.addParam(PrinterDevice.PARAM_TEXTSIZE_1X);
        //添加到打印队列
        //  mPrinter.addText(concentration, tData_body);
        int number_count = StringUtil.isNumeric(result);
        int str_count = number_count + (result.length() - number_count) * 2;
        int mWidth = 300;
        int left = 30;
        int _s_45 = 0;
        if (ScanCodeType == 1) {
            if (str_count > 16 && str_count <= 26) {
                mWidth = 390;//
                left = 15;
            } else if (str_count > 26) {
                mWidth = 390;
                concentration = 1;
                left = 0;
                showMsg("条码内容过长，识别率可能降低。");
//            _s_45 =-90;
            } else {
                mWidth = 300;
                left = 30;
            }
        }
        int mHeight = 60;
        boolean flag = true;
        if (ScanCodeType == 2) {
            mWidth = 150;
            mHeight = 150;
            left = 50;
            flag = false;
        }
        mBitmap = BarcodeCreater.creatBarcode(this, result, mWidth, mHeight, flag, ScanCodeType, _s_45);
        if (isprint) {
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
        return mBitmap;
    }


    private Column<Object> mes_lot_column;
    private Column<Object> erp_lot_column;
    List<PrintHistory> table_list = null;

    private void initTable_1(List<PrintHistory> history_list) {
        check_list = new ArrayList<>();
        mes_lot_column = mes_lot_column != null ? mes_lot_column : new Column<>("MES Lot List", "mes_lot_id");
        erp_lot_column = erp_lot_column != null ? erp_lot_column : new Column<>("ERP Lot List", "erp_lot_id");
        table_list = history_list;
        if (history_list == null) {
            history_list = new ArrayList<>();
            PrintHistory h = new PrintHistory(false, "", "", "", "", "", new Date());
            h.setErp_lot_id("");
            h.setMes_lot_id("");
            history_list.add(h);

        }
        TableData tableData = new TableData<PrintHistory>("List", history_list, mes_lot_column, erp_lot_column);
        binding.table1Delivery.setTableData(tableData);
        TableConfig config = binding.table1Delivery.getConfig();
        error_list = new ArrayList<>();
        check_list = new ArrayList<>();
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat(){
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer-1);
            }
        });
        config.setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                List<PrintHistory> list = binding.table1Delivery.getTableData().getT();
                PrintHistory carr = list.get(cellInfo.row);
                String scan = carr.getErp_lot_id();
                String targer = carr.getMes_lot_id();
                if ((scan == null && targer == null) || (scan != null && targer != null && scan.isEmpty() && targer.isEmpty())) {
                    return Color.WHITE;
                } else if (scan == null || targer == null || scan.isEmpty() || targer.isEmpty() || !targer.equals(scan)) {
                    if (!check_list.contains(carr)) {
                        check_list.add(carr);
                    }
                    return Color.RED;
                } else {
                    if (check_list.contains(carr)) {
                        check_list.remove(carr);
                    }
                    return Color.GREEN;
                }
            }
        });
    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(Activity_Delivery.this);
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
                    Intent intent = new Intent(Activity_Delivery.this, SettingActivity.class);
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
//                    for (DropModel bean :
//                            type_list) {
//                        i++;
//                        if (bean.getColumn_1().equals(returnedData)) {
////                            binding.mainPartType.setSelection(i);
//                            return;
//                        }
//                    }
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

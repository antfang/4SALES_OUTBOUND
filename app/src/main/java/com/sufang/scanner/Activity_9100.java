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
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.zyapi.pos.PosManager;
import android.zyapi.pos.PrinterDevice;
import android.zyapi.pos.interfaces.OnPrintEventListener;
import android.zyapi.pos.utils.BitmapTools;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.format.sequence.BaseSequenceFormat;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.hjq.toast.ToastUtils;
import com.sufang.dailog.EditDialog;
import com.sufang.model.DropModel;
import com.sufang.scanner.adapter.HistoryAdapter;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.scanner.databinding.Activity9100Binding;
import com.sufang.util.BarcodeCreater;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.DateUtils;
import com.sufang.util.PreferencesUtils;
import com.sufang.util.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_9100 extends AppCompatActivity implements MiddlewareListener {

    Activity9100Binding binding;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer mediaPlayerAlarm = null;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private List<DropModel> type_list = new ArrayList<DropModel>();
    private boolean isStop = false;
    /**
     * 判定是否为第一次打印(indicate the first time print after paper loading)
     */
    public static boolean IS_FIRST_PRINT = true;
    private Bitmap mBitmap = null;
    private PrinterDevice mPrinter = null;

    private static final String ISMART_KEY_SCAN_VALUE = "ismart.intent.scanvalue";
    private final int SCANNER_COLOR = 1;
    private final int ORDER_COLOR = 11;
    private final int PLAY_SOUND = 2;
    private final int PLAY_ALARM_SOUND = 3;
    private final int LOT_FOCUSE = 4;
    private final int LABLE_LOT_FOCUSE = 5;
    private final int SHOWALERT = 6;
    private final int AUTO_CHECK = 7;
    private final int GET_LOT_LIST_ID_9 = 8;
    private final int QUERY_LOT_LIST_STATUS_A = 9;
    private final int FOCUSE_CONTROL = 10;
    private boolean isOpen = true;
    private static final float BEEP_VOLUME = 1.0f;
    public static String str_papername = "标签纸";
    private ArrayAdapter<String> type_adapter;
    public String Scan_Id = "";
    public String OrderString = "";
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
                        int color = (int) msg.obj;
                        binding.lotId9100.setBackgroundColor(color);
                        binding.lotId9100.setTextColor(Color.BLACK);
                        binding.lableLotId9100.setBackgroundColor(color);
                        binding.lableLotId9100.setTextColor(Color.BLACK);
                        break;
                    case ORDER_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int color1 = (int) msg.obj;
                        binding.order9100.setBackgroundColor(color1);
                        binding.order9100.setTextColor(Color.BLACK);
                        break;
                    case LOT_FOCUSE:
//                        binding.lotId9100.clearFocus();
                        binding.lotId9100.setFocusableInTouchMode(true);
                        binding.lotId9100.requestFocus();
                        binding.lotId9100.selectAll();
                        binding.lotId9100.setBackgroundColor(Color.WHITE);
                        binding.lableLotId9100.setBackgroundColor(Color.WHITE);
                        binding.lotId9100.setText("");
                        break;
                    case LABLE_LOT_FOCUSE:
//                    binding.editLocation.clearFocus();
                        binding.lableLotId9100.setFocusableInTouchMode(true);
                        binding.lableLotId9100.requestFocus();
                        binding.lableLotId9100.selectAll();
                        binding.lableLotId9100.setText("");
                        break;
                    case SHOWALERT:
                        String showalert_msg = (String) msg.obj;
                        SweetAlertDialog dialog = new SweetAlertDialog(Activity_9100.this, SweetAlertDialog.NORMAL_TYPE)
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
                    case FOCUSE_CONTROL:
                        if (msg.obj != null)
                            ((EditText) msg.obj).setFocusableInTouchMode(true);
                        ((EditText) msg.obj).requestFocus();
                        ((EditText) msg.obj).selectAll();
                        break;
                    case GET_LOT_LIST_ID_9://ERP
                        onMiddlewareChangeColor(1, null, Color.WHITE, null);
                        if (binding.order9100.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输调拨单号。");
                            Focuse_Control(binding.lotId9100);
                            return;
                        }
                        OrderString = binding.order9100.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        List<PrintHistory> res = Menu.Client.GET_LOT_LIST_ID_9(OrderString);
                                        if (res != null && res.size() > 0) {
                                            List<List<PrintHistory>> lists = Menu.Client.Query_Lot_List_Status_A(res, res);
                                            initTable_1(lists.get(1));
                                            initTable_2(lists.get(0));
                                            Focuse_Control(binding.lotId9100);
                                            onMiddlewareChangeColor(1, null, Color.GREEN, null);
                                        } else {
                                            throw new Exception("ERP查询无Lot List数据");
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(1, null, Color.RED, null);
                                        Focuse_Control(binding.order9100);
                                         initTable_1(null);
                                         initTable_2(null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher2.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case AUTO_CHECK:
                        onMiddlewareChangeColor(2, null, Color.WHITE, null);
                        if (binding.lotId9100.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入 Lot ID.");
                            Lot_Focuse();
                            return;
                        }
                        if (binding.lableLotId9100.getText().toString().isEmpty()) {
                            onMiddlewareFail("请输入 Lable Lot ID.");
                            Lable_Lot_Focuse();
                            return;
                        }
                        Scan_Id = binding.lotId9100.getText().toString();
                        Scan_Id2 = binding.lableLotId9100.getText().toString();
                        if (!Scan_Id.equals(Scan_Id2)) {
                            onMiddlewareFail("两个Lot不匹配。");
                            onMiddlewareChangeColor(2, null, Color.RED, null);
                            return;
                        }
                        if (Parts_list == null) {
                            onMiddlewareFail("请查询调拨单号。");
                            onMiddlewareChangeColor(2, null, Color.RED, null);
                            return;
                        }
                        boolean flag = false;
                        for (PrintHistory p : Parts_list) {
                            if (p.getLot_id().equals(Scan_Id)) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            onMiddlewareFail(Scan_Id + ":不存在。");
                            Lable_Lot_Focuse();
                            onMiddlewareChangeColor(2, null, Color.RED, null);
                            return;
                        }

                        if (Menu.Client.isConnected()) {
                            Thread dispatcher1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean res = Menu.Client.Lot_ID_Validation_9100_1(Scan_Id);
                                        if (res) {
                                            Lable_Lot_Focuse();
                                            Lot_Focuse();
                                            for (PrintHistory hs : Parts_list) {
                                                if (hs.getLot_id().equals(Scan_Id)) {
                                                    hs.setCode("OK");
                                                    hs.setCreateTime(new Date());
                                                    break;
                                                }
                                            }
                                           // Get_Lot_List_Id_9();
                                            initTable_1(Parts_list);
                                            onMiddlewareChangeColor(2, null, Color.GREEN, null);
                                        }
                                    } catch (Exception ex) {
                                        Lable_Lot_Focuse();
                                        Lot_Focuse();
                                        for (PrintHistory hs : Parts_list) {
                                            if ((hs.getCode() == null || !hs.getCode().equals("OK")) && hs.getLot_id().equals(Scan_Id)) {
                                                hs.setCode("NG");
                                                hs.setCreateTime(new Date());
                                                break;
                                            }
                                            if (hs.getCode() != null && hs.getCode().equals("OK") && hs.getLot_id().equals(Scan_Id)) {
                                                hs.setCreateTime(new Date());
                                            }
                                        }
                                        initTable_1(Parts_list);
                                        onMiddlewareChangeColor(2, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher1.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    default:
                }
            } catch (Exception ex) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_9100);
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
                if (binding.lotId9100.hasFocus()) {//扫描的是lotId
                    onMiddlewareChangeColor(2, null, Color.WHITE, null);
                    binding.lotId9100.setText(s);
                    Lable_Lot_Focuse();
                } else if (binding.order9100.hasFocus()) {//扫描的是lableLotId9100
                    binding.order9100.setText(s);
                    Get_Lot_List_Id_9();
                } else if (binding.lableLotId9100.hasFocus()) {//扫描的是lableLotId9100
                    binding.lableLotId9100.setText(s);
                    Auto_Check();
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

    private void Auto_Check() {
        Message msg = handler.obtainMessage(AUTO_CHECK);
        msg.sendToTarget();
    }

    private void Focuse_Control(EditText control) {
        Message msg = handler.obtainMessage(FOCUSE_CONTROL);
        msg.obj = control;
        msg.sendToTarget();
    }

    private void Get_Lot_List_Id_9() {
        Message msg = handler.obtainMessage(GET_LOT_LIST_ID_9);
        msg.sendToTarget();
    }

    private void Query_Lot_List_Status_A() {
        Message msg = handler.obtainMessage(QUERY_LOT_LIST_STATUS_A);
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
            case 1:
                Message ms1 = handler.obtainMessage(ORDER_COLOR);
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

    }

    @Override
    public void onMiddlewareSuccessShow() {
        Message ms = handler.obtainMessage(SCANNER_COLOR);
        ms.obj = Color.GREEN;
        ms.sendToTarget();
    }

    @Override
    public void onShowMSG(String msg) {
        Toast.makeText(Activity_9100.this, msg, Toast.LENGTH_SHORT).show();
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


    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        binding.includeTitle.leftButton.performClick();
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
            isStop = true;
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
        binding.table19100.setVisibility(View.GONE);
        binding.print9100View.setVisibility(View.GONE);
        binding.table29100.setVisibility(View.VISIBLE);
        binding.tab9100.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIndex = binding.tab9100.getSelectedTabPosition();
                if(tabIndex==0){
                    binding.table19100.setVisibility(View.GONE);
                    binding.print9100View.setVisibility(View.GONE);
                    binding.table29100.setVisibility(View.VISIBLE);
                }else{
                    binding.table19100.setVisibility(View.VISIBLE);
                    binding.print9100View.setVisibility(View.VISIBLE);
                    binding.table29100.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tab9100.getTabAt(0).select();
        binding.clear19100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lotId9100.setText("");
                Lot_Focuse();
                onMiddlewareChangeColor(1, null, Color.WHITE, null);
            }
        });
        binding.print9100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String showalert_msg = "是否要打印选中标签。";
                SweetAlertDialog dialog = new SweetAlertDialog(Activity_9100.this, SweetAlertDialog.NORMAL_TYPE)
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
                                  if (Parts_list == null || Parts_list.size() <= 0) {
                                      return;
                                  }
                                  isStop = false;
                                  for (PrintHistory p : Parts_list) {
                                      if (p.getLot_id() == null || p.getLot_id().isEmpty()) {
                                          continue;
                                      }
                                      if (p.getSel()) {
                                          printScanResult(p.getLot_id(), true);
                                          p.setSel(false);
                                          if (isStop) {
                                              p.setSel(true);
                                              break;
                                          }
                                      }
                                  }
                                  initTable_1(Parts_list);
                                  sweetAlertDialog.dismiss();
                              }catch(Exception ex) {

                              }

                            }
                        });
                dialog.setCancelable(true);
                dialog.show();

            }
        });
        binding.clear29100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lableLotId9100.setText("");
                Lable_Lot_Focuse();
                onMiddlewareChangeColor(1, null, Color.WHITE, null);
            }
        });
        binding.orderButton9100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Get_Lot_List_Id_9();
            }
        });
        binding.includeTitle.titleRefreshButton.setVisibility(View.INVISIBLE);
        binding.includeTitle.rightText.setVisibility(View.INVISIBLE);
        binding.includeTitle.leftButton.setVisibility(View.VISIBLE);
        binding.includeTitle.title1.setText("9100");
        binding.includeTitle.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String showalert_msg = "是否退出9100。";
                SweetAlertDialog dialog = new SweetAlertDialog(Activity_9100.this, SweetAlertDialog.NORMAL_TYPE)
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
        binding.buttonOK9100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auto_Check();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_VALUE);
        registerReceiver(mReceiver, filter);
        Focuse_Control(binding.order9100);
        if (Menu.USER_NAME.toUpperCase().equals("ADMIN")) {
            binding.print9100.setVisibility(View.VISIBLE);
        }
    }

    private Column<Object> checkColumn1;
    private Column<Object> batch_id_Column;
    private Column<Object> flagColumn;
    private Column<Boolean> selColumn;
    private Column<Object> createDateColumn;
    private List<PrintHistory> Parts_list = new ArrayList<>();

    private void initTable_1(List<PrintHistory> history_list) {
        batch_id_Column = batch_id_Column != null ? batch_id_Column : new Column<>("Lot ID", "lot_id");
        checkColumn1 = checkColumn1 != null ? checkColumn1 : new Column<>("Status", "name");
        flagColumn = flagColumn != null ? flagColumn : new Column<>("Flag", "code");
        createDateColumn = createDateColumn != null ? createDateColumn : new Column<>("CreateOn", "createTime");
        selColumn = new Column<>("选", "sel", new ImageResDrawFormat<Boolean>(20, 20) {
            @Override
            protected Context getContext() {
                return Activity_9100.this;
            }

            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {
                if (isCheck) {
                    return R.mipmap.check;
                }
                return 0;
            }
        });
//        selColumn.setWidth(40);
        selColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {

            @Override

            public void onClick(Column<Boolean> column, String value, Boolean bool, int position) {
                boolean ischeck = selColumn.getDatas().get(position);
                if (Parts_list != null) {
                    if (position >= 0) {
                        selColumn.getDatas().set(position, !(ischeck));
                        binding.table19100.refreshDrawableState();
                        binding.table19100.invalidate();
                        Parts_list.get(position).setSel(!(ischeck));
                    }
                }
            }

        });
        //        flagColumn.setWidth(100);
        if (history_list != null && history_list.size() > 0 && Parts_list != null && Parts_list.size() > 0) {
            for (PrintHistory hs : Parts_list) {
                for (PrintHistory ha : history_list) {
                    if (ha.getLot_id().equals(hs.getLot_id()) && ha.getCode() == null) {
                        ha.setCode(hs.getCode());
                        ha.setCreateTime(hs.getCreateTime());
                        break;
                    }
                }
            }
        }
        Parts_list = history_list;
        if (history_list == null) {
            history_list = new ArrayList<>();
            history_list.add(new PrintHistory());
        }
        batch_id_Column.setFixed(true);
        checkColumn1.setFixed(true);
        TableData tableData = null;
        tableData = new TableData<PrintHistory>("LOT List", history_list, batch_id_Column, checkColumn1, flagColumn,selColumn, createDateColumn);
        createDateColumn.setReverseSort(true);
        tableData.setSortColumn(createDateColumn);
        binding.table19100.setTableData(tableData);
        TableConfig config = binding.table19100.getConfig();
        //Y序号列
        config.setFixedYSequence(true);
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat() {
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer - 1);
            }
        });
        config.setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                List<PrintHistory> list = binding.table19100.getTableData().getT();
                PrintHistory carr = list.get(cellInfo.row);
                String scan = carr.getLot_id();
                String targer = carr.getCode();
                if ((scan == null) || (scan.isEmpty()) || targer == null || (targer.isEmpty())) {
                    return Color.WHITE;
                } else if (targer.toUpperCase().equals("OK")) {
                    return Color.GREEN;
                } else if (targer.toUpperCase().equals("NG")) {
                    return Color.RED;
                } else {
                    return Color.WHITE;
                }
            }
        });
    }

    private Column<Object> CHAR_ID;
    private Column<Object> VALUE;
    private List<PrintHistory> Spec_list = new ArrayList<>();

    private void initTable_2(List<PrintHistory> history_list) {
        CHAR_ID = CHAR_ID != null ? CHAR_ID : new Column<>("CHAR ID", "code");
        VALUE = VALUE != null ? VALUE : new Column<>("VALUE", "name");
        Spec_list = history_list;
        if (history_list == null) {
            history_list = new ArrayList<>();
            history_list.add(new PrintHistory());
        }
        TableData tableData = new TableData<PrintHistory>("SPEC LIST", history_list, CHAR_ID, VALUE);
        binding.table29100.setTableData(tableData);
        TableConfig config = binding.table29100.getConfig();
        config.setFixedYSequence(true);
        config.setShowTableTitle(false);
        config.setShowXSequence(false);
        tableData.setYSequenceFormat(new BaseSequenceFormat() {
            @Override
            public String format(Integer integer) {
                return String.valueOf(integer - 1);
            }
        });
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

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(Activity_9100.this);
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
                    //打开设置页面 ;
                    Intent intent = new Intent(Activity_9100.this, SettingActivity.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            binding.includeTitle.leftButton.performClick();
        }

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

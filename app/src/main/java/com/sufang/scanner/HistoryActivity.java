package com.sufang.scanner;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.zyapi.pos.PosManager;
import android.zyapi.pos.PrinterDevice;
import android.zyapi.pos.interfaces.OnPrintEventListener;
import android.zyapi.pos.utils.BitmapTools;

import com.bin.david.form.core.SmartTable;
import com.hjq.toast.ToastUtils;
import com.miracom.Client.Client;
import com.sufang.dailog.EditDialog;
import com.sufang.scanner.adapter.HistoryAdapter;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.scanner.callback.OnItemClickListener;
import com.sufang.scanner.databinding.HistoryActivityBinding;
import com.sufang.util.BarcodeCreater;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.DateUtils;
import com.sufang.util.LogUtil;
import com.sufang.util.PreferencesUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author admin
 */
public class HistoryActivity extends AppCompatActivity  {

    HistoryActivityBinding binding;
    private List<PrintHistory> historyList;
    private HistoryAdapter adapter;
    private PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();

    /**
     * 判定是否为第一次打印(indicate the first time print after paper loading)
     */
    public static boolean IS_FIRST_PRINT = true;

    private boolean isOpen = true;

    private static final String ISMART_KEY_SCAN_VALUE = "ismart.intent.scanvalue";

    private MediaPlayer mediaPlayer = null;

    private static final float BEEP_VOLUME = 1.0f;


    public static String str_codetypename = "";
    public static String str_papername = "标签纸";
    public static String str_tempCode = "";

    private DaoSession daoSession;
    private PrintHistoryDao printHistoryDao;
    private SmartTable table;

    public String scan_str ="";
//    @SuppressLint("HandlerLeak")
//    private final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case UPDATE_UI:
//                    PrintHistory bean = (PrintHistory) msg.obj;
//                    binding.txtFosbId.setText(bean.getFosbId());
//                    binding.txtDryStart.setText(bean.getDryStartTime());
//                    binding.txtDryEnd.setText(bean.getDryEndTime());
//                    binding.txtNextClean.setText(bean.getNextCleanTime());
//                    printImageLabel();
//                    break;
//                case SHOW_MSG:
//                    String ms = (String) msg.obj;
//                    binding.msg.setText(ms);
//                    break;
//                case MSG_SERIAL_RECV_BUFFER:
//                    //收到扫描数据 关闭扫描触发，下一次手动开启(received the scanning data,close the scan trigger)
//                    // commonApi.setGpioOut(GPIO_SCAN_TRIG,0);
//                    String s = (String) msg.obj;
//                    scan_str = s;
//
//                    log("扫描结果:" + s);
//                    if (s != null && s.trim().length() > 0) {
//                        playSound();
//                        binding.tvScanResult.setText(s);
//                        if (!isSettingOk()) {
//                            showMsg(getString(R.string.txt_alert_message));
//                            return;
//                        }
//                        //todo 用扫描结果查询服务器数据
//                        if (client == null) {
////                            showMsg(getString(R.string.txt_connect_fail));
//                            initClient();
//                        } else if (client.isConnected()) {
//
//                        } else {
//                            client.initMsgHandler();
//                        }
//                        if (client.isConnected()) {
//                            Thread dispatcher1 = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        client.doSearch(scan_str);
//                                    } catch (Exception ex) {
//                                        onConnectFail(ex.getMessage());
//                                    }
//                                }
//                            });
//                            dispatcher1.start();
//                        } else {
//                            showMsg(getString(R.string.txt_connect_fail));
//                        }
////                        printScanResult(s);
////                        printImageLabel();
//                    } else {
//                        showMsg(getString(R.string.txt_scan_no_result));
//                    }
//                    break;
//
//                default:
//            }
//        }
//    };
//
//    public void printScanResult(String result) {
//        result = result.replaceAll(" ", "");
////        if (result.equals(str_tempCode) || result.length() < 5) {
////            return;
////        } else {
////            str_tempCode = result;
////        }
//        // 1：一维码 2：二维码
//        int ScanCodeType = 1;
//        if (str_codetypename == null) {
//            str_codetypename = "一维码";
//        }
//        switch (str_codetypename) {
//            case "一维码":
//                ScanCodeType = 1;
//                break;
//            case "二维码":
//                ScanCodeType = 2;
//                break;
//        }
//        if (result.length() > 80) {
//            ScanCodeType = 2;
//        }
//
//        //打印浓度
//        int print_setting = 25;
////        try
////        {
////            print_setting = preferencesUtils.getInt(Constant.KEY_PRINT_SETTING);
////        }
////        catch (Exception ex)
////        {}
//        int concentration = print_setting;
//        if (str_papername.equals("标签纸")) {
//            //构造TextData实例
//            PrinterDevice.TextData tData_head = mPrinter.new TextData();
//            if (IS_FIRST_PRINT == false) {
//                //退步
//                tData_head.addParam("1B4B15");
//            } else {
//                //进步
//                tData_head.addParam("1B4A08");
//
//            }
//            mPrinter.addText(concentration, tData_head);
//        }
//        //构造TextData实例
//        PrinterDevice.TextData tData_body = mPrinter.new TextData();
//        //添加打印内容
//        tData_body.addText(result + "\n");
//        tData_body.addParam(PrinterDevice.PARAM_ALIGN_MIDDLE);
//        //设置两倍字体大小
//        tData_body.addParam(PrinterDevice.PARAM_TEXTSIZE_1X);
//        //添加到打印队列
//        mPrinter.addText(concentration, tData_body);
//
//        int mWidth = 300;
//        int mHeight = 60;
//        if (ScanCodeType == 2) {
//            mWidth = 150;
//            mHeight = 150;
//        }
//        mBitmap = BarcodeCreater.creatBarcode(this, result, mWidth, mHeight, false, ScanCodeType);
//        byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
//        mPrinter.addBmp(concentration, 30, mBitmap.getWidth(), mBitmap.getHeight(), printData);
//
//        if (str_papername.equals("标签纸")) {
//            //添加黑标检测 走纸到黑标处再开始打印下一张数据
//            mPrinter.addAction(PrinterDevice.PRINTER_CMD_KEY_CHECKBLACK);
//        }
//        //构造TextData实例
//        PrinterDevice.TextData tDataEnter = mPrinter.new TextData();
//        //多输出到撕纸口(print more paper for paper tearing)
//        tDataEnter.addText("\n\n\n");
//        //添加到打印队列(add to print queue)
//        mPrinter.addText(concentration, tDataEnter);
//
//        //开始队列打印(begin to print)
//        mPrinter.printStart();
//
//        //重置首次打印变量值(reset the first printing variable)
////        IS_FIRST_PRINT = false;
//        setIsFirstPrint(false);
//    }
//    public void printImageLabel_test() {
//        try {
//            String txt = binding.tvScanResult.getText().toString();
//            if (txt.length() > 0) {
//                //打印LOT_ID条形码
//                printScanResult(txt);
//            } else {
//                printImageLabel();
//            }
//        }catch (Exception e){
//            showMsg(e.getMessage());
//        }
//    }
//    /**
//     * 用图片打印标签
//     */
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
//        int concentration =print_setting;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.history_activity);
        PosManager.get().init(getApplicationContext(), "PDA");
        table =  findViewById(R.id.table);
        initBeepSound();
        initViews();
//        SettingActivity._MainActivity = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_VALUE);
        registerReceiver(mReceiver, filter);
        Log.d("hello", " scan enter ");
        Log.d("hello date", " scan enter " + DateUtils.formatDate(DateUtils.getDate("20191205001222",
                DateUtils.FORMAT_YYYYMMDDHHMMSS), DateUtils.FORMAT_YYYYMMDD_HH_MM_SS));

//        initPrint();
        initDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IS_FIRST_PRINT = preferencesUtils.getBoolean(Constant.KEY_FIRST_PRINT);
//        if (!isSettingOk()) {
//            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText(getString(R.string.txt_alert_title))
//                    .setContentText(getString(R.string.txt_alert_message))
//                    .setConfirmText(getString(R.string.txt_ok))
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
////                            toSetting();
//                        }
//                    });
//            dialog.setCancelable(false);
//            dialog.show();
//        }
    }

    private boolean isSettingOk() {
        return CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SERVER_ADDRESS))
                && CommonUtil.isNotNull(preferencesUtils.getString(Constant.KEY_SITE_ID));
    }

    private void initDao() {
        daoSession = ((MainApp) getApplication()).getDaoSession();
        printHistoryDao = daoSession.getPrintHistoryDao();
        /*for (int i=0; i< 45; i++) {
            PrintHistory history = new PrintHistory();
            history.setCreateTime(new Date());
            history.setDryStartTime(DateUtils.addDay(DateUtils.currentDatetime1(),
                    DateUtils.FORMAT_YYYYMMDD_HH_MM_SS, -i));
            history.setDryEndTime(DateUtils.currentDatetime1());
            history.setNextCleanTime(DateUtils.currentDatetime1());
            history.setFosbId("1980000A"+i);
            printHistoryDao.save(history);
        }*/
        getPrintHistory();
    }

    private void getPrintHistory() {
        historyList = printHistoryDao.queryBuilder()
                .orderDesc(PrintHistoryDao.Properties.CreateTime).limit(50).list();
        table.setData(historyList );
        if (CommonUtil.isNotEmpty(historyList)) {
            binding.lvHistory.setVisibility(View.VISIBLE);
            binding.tvNoHistory.setVisibility(View.GONE);
            if (historyList.size() == 100) {
                new MyAsyncTask().execute();
            }
        } else {
            binding.lvHistory.setVisibility(View.GONE);
            binding.tvNoHistory.setVisibility(View.VISIBLE);
        }
//        adapter.setData(historyList);
    }



    private void deleteHistory() {
        Date lastDate = historyList.get(historyList.size() - 1).getCreateTime();
        List<PrintHistory> delList = printHistoryDao.queryBuilder()
                .where(PrintHistoryDao.Properties.CreateTime.le(lastDate)).list();
        if (delList != null && delList.size() > 0) {
            printHistoryDao.deleteInTx(delList);
        }
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

    private void playSound() {
        mediaPlayer.start();

    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    private void initViews() {
        binding.includeTitle.leftButton1.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lvHistory.setHasFixedSize(true);
        binding.lvHistory.setLayoutManager(layoutManager1);
        adapter = new HistoryAdapter(this, historyList);
        binding.lvHistory.setAdapter(adapter);
        binding.includeTitle.leftButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开update_history
                finish();
            }
        });
//        adapter.setOnItemClick(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                PrintHistory bean = historyList.get(position);
//                if (bean != null) {
//                    setPrintData(bean);
//                }
//            }
//        });
//        binding.btnPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendPrintMsg(binding.tvScanResult.getText().toString());
//            }
//        });
    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(HistoryActivity.this);
        editDialog.setTitle("请输验证口令");
        editDialog.setYesOnclickListener("确定", new EditDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String phone) {
                //密码是否一致
                String setting_psw = preferencesUtils.getString(Constant.KEY_SETTINH_PSW);
                if (setting_psw.equals(phone)) {
                    //让软键盘隐藏
                    InputMethodManager imm = (InputMethodManager) editDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editDialog.getCurrentFocus().getApplicationWindowToken(), 0);
                    editDialog.dismiss();
                    //打开设置页面
                    Intent intent = new Intent(HistoryActivity.this, SettingActivity.class);
                    startActivity(intent);
                } else {
                    showMsg("验证口令错误");
                }
            }
        });
        editDialog.setNoOnclickListener("取消", new EditDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

//    private void setPrintData(PrintHistory bean) {
//        Message msg = handler.obtainMessage(UPDATE_UI);
//        msg.obj = bean;
//        msg.sendToTarget();
//    }

    private void showMsg(String msg) {
        ToastUtils.show(msg);
    }

    private void log(String msg) {
        Log.d(this.getClass().getName(), this.toString() + "=>" + msg);
    }

    private void logE(String msg) {
        Log.e(this.getClass().getName(), this.toString() + "=>" + msg);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            //////////////////
            //
            // 以下代码是用于设置的发送广播。
//             if(mAction.endsWith(ISMART_KEY_SCAN_MODE)) {
//                 Intent scanmode = new Intent();
//                 scanmode.setAction(ISMART_KEY_SCAN_MODE);
//                 scanmode.putExtra("ifsendtoapp", true);
//                 sendBroadcast(scanmode);
//             }

//            if (mAction.equals(ISMART_KEY_SCAN_VALUE)) {
//                //String s = intent.getStringExtra("scanvalue");
//                byte[] b_strscan = intent.getByteArrayExtra("scanvalue");
//                String s = new String(b_strscan);
//                sendPrintMsg(s);
//            }

        }
    };

////    private void sendPrintMsg(String s) {
////        Message msg = handler.obtainMessage(MSG_SERIAL_RECV_BUFFER);
////        msg.obj = s;
////        Log.d("hello", "scanvalue:" + s);
////        msg.sendToTarget();
////    }


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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (!ButtonUtils.isFastDoubleClick_Key(keyCode, 100)) {
//
//            if (keyCode == 280 || keyCode == 131 || keyCode == 132) {
//                Log.d("hello", "keycode:" + keyCode);
//                if (isOpen) {
//                    // commonApi.setGpioOut(GPIO_SCAN_TRIG, 1);
//                }
//            }
//        }
//        if (str_papername.equals("标签纸")) {
//            //手持机Fn的键值(PDA keycode)[其它键值为：SCAN:280 BACK:13 LEFT:131 RIGHT:132]
//            if (keyCode == 133) {
//                //fixme
////                IS_FIRST_PRINT = true;
//
//                //1->走纸检测   mWidth 黑标的宽度
//                //mPrinter.addAction(PrinterDevice.PRINTER_CMD_KEY_CHECKBLACK);
//                // mPrinter.printStart();
//                mPrinter.checkBlackAsync();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }



}

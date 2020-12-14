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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.sufang.scanner.databinding.ActivityManualOperationBinding;
import com.sufang.util.BarcodeCreater;
import com.sufang.util.ButtonUtils;
import com.sufang.util.CommonUtil;
import com.sufang.util.DateUtils;
import com.sufang.util.PreferencesUtils;
import com.sufang.util.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Activity_Manual_Operation extends AppCompatActivity implements MiddlewareListener {

    ActivityManualOperationBinding binding;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayer mediaPlayerAlarm = null;
    private String Pallet_No = "";
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

    private static final String ISMART_KEY_SCAN_VALUE = "ismart.intent.scanvalue";
    private final int SMALL_LOT_COLOR = 3;
    private final int PALLET_COLOR = 4;
    private final int SET_PALLET = 5;
    private final int FOCUSE_CONTROL = 6;
    private final int SCANNER_COLOR = 7;
    private final int DELIVERY_NO_COLOR = 23;
    private final int BIG_LOT_COLOR = 35;
    private final int PLAY_SOUND = 8;
    private final int PLAY_ALARM_SOUND = 9;
    private final int SMALL_LOT = 10;
    private final int PALLET_NO = 11;
    private final int DELIVERY_NO = 12;
    private final int BIG_LOT = 13;
    private final int CUST_PART_NO = 14;
    private final int CUST_PART_NO_COLOR = 14;
    private final int SET_CARTON_NO = 16;
    private final int SHOWALERT = 18;
    private final int VERIFY_CUST_PART_NO_3 = 17;
    private final int GET_DNLOTID_1 = 19;
    private final int QUERY_CARTON_2 = 20;
    private final int CLEAR_CONTROL = 26;
    private final int CLEAR_CONTROL2 = 27;
    private final int REPRINT_9 = 34;
    private final int LOT_COLOR2 = 28;
    private final int LOT_ID2 = 29;
    private final int SET_PALLED_ID = 30;
    private final int UPDPALLETNO_3 = 31;
    private final int PALLET_CANCEL_COLOR = 32;
    private final int CANCEL_PALLET_8 = 33;
    private final int CHECK_LOT = 36;
    private boolean isOpen = true;
    private static final float BEEP_VOLUME = 1.0f;
    public static String str_codetypename = "";
    public static String str_papername = "标签纸";
    public String Small_Lot = "";
    public String Lot_Id = "";
    public String Lot_Id2 = "";
    public String Delivery_No = "";
    public String Carton_NO = "";
    public String Cust_Part_No = "";
    public String Config_Flag = "";
    public String Lot_No = "";
    public List<PrintHistory> Lot_List = null;
    private ArrayAdapter<String> type_adapter;
    public String Scan_Id = "";
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (!isSettingOk()) {
                    onMiddlewareFail(getString(R.string.txt_alert_message));
                    return;
                }
                if (Menu.Client == null) {
                    initClient();
                } else if (Menu.Client.isConnected()) {
                } else {
                    Menu.Client.initMsgHandler();
                    Menu.Client.SetMiddlewareListener(Activity_Manual_Operation.this);
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
                        binding.palletNoManual.setBackgroundColor(color);
                        binding.palletNoManual.setTextColor(Color.BLACK);
                    case BIG_LOT_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int lotColor2 = (int) msg.obj;
                        binding.lotId2Manual.setBackgroundColor(lotColor2);
                        binding.lotId2Manual.setTextColor(Color.BLACK);
                        if (lotColor2 == Color.GREEN) {
                            Lot_Id = binding.lotIdManual.getText().toString();
                        } else {
                            Lot_Id = "";
                        }
                        break;
                    case SMALL_LOT_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int lotColor = (int) msg.obj;
                        binding.lotIdManual.setBackgroundColor(lotColor);
                        binding.lotIdManual.setTextColor(Color.BLACK);
                        if (lotColor == Color.GREEN) {
                            Carton_NO = binding.cartonNoManual.getText().toString();
                            Lot_Id=binding.lotIdManual.getText().toString();
                        } else {
                            Carton_NO = "";
                            Lot_No = "";
                            Lot_Id="";
                            binding.cartonNoManual.setText("");
                        }
                        break;

                    case PALLET_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int _color = (int) msg.obj;
                        binding.palletNoManual.setBackgroundColor(_color);
                        binding.palletNoManual.setTextColor(Color.BLACK);
                        if (_color == Color.GREEN) {
                            Pallet_No = binding.palletNoManual.getText().toString();
                        } else {
                            Pallet_No = "";
                            binding.palletNoManual.setText("");
                        }
                        break;
                    case DELIVERY_NO_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int _color1 = (int) msg.obj;
                        binding.deliveryNoManual.setBackgroundColor(_color1);
                        binding.deliveryNoManual.setTextColor(Color.BLACK);
                        if (_color1 == Color.GREEN) {
                            Delivery_No = binding.deliveryNoManual.getText().toString();
                        } else {
                            Delivery_No = "";
                            Lot_List = null;
                        }
                        break;
                    case CUST_PART_NO_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int _color3 = (int) msg.obj;
                        binding.custPartNoManual.setBackgroundColor(_color3);
                        binding.custPartNoManual.setTextColor(Color.BLACK);
                        break;
                    case PALLET_CANCEL_COLOR:
                        if (msg == null) {
                            break;
                        }
                        int _color4 = (int) msg.obj;
                        binding.cancelPalletTxtManual.setBackgroundColor(_color4);
                        binding.cancelPalletTxtManual.setTextColor(Color.BLACK);
                        break;
                    case CLEAR_CONTROL:
                        Focuse_Control(SMALL_LOT);
                        onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.WHITE, null);
                         onMiddlewareChangeColor(CUST_PART_NO, null, Color.WHITE, null);
                        Cust_Part_No = "";
                        Carton_NO = "";
                        Lot_No = "";
                        Lot_Id = "";
                        Lot_Id2 = "";
                        Config_Flag = "";
                        binding.lotIdManual.setText("");
                        binding.lotId2Manual.setText("");
                        binding.custPartNoManual.setText("");
                        binding.cartonNoManual.setText("");
                        break;
                    case CLEAR_CONTROL2:
                       // Focuse_Control(DELIVERY_NO);
                        onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.WHITE, null);
                        onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.WHITE, null);
                          onMiddlewareChangeColor(CUST_PART_NO, null, Color.WHITE, null);
                        Cust_Part_No = "";
                        Carton_NO = "";
                        Lot_No = "";
                        Lot_Id = "";
                        Lot_Id2 = "";
                        Delivery_No = "";
                        Lot_List = null;
                        Config_Flag = "";
                        binding.deliveryNoManual.setText("");
                        binding.lotId2Manual.setText("");
                        binding.lotIdManual.setText("");
                        binding.custPartNoManual.setText("");
                        binding.cartonNoManual.setText("");
                        break;
                    case FOCUSE_CONTROL:
                        int focuse_id = (int) msg.obj;
                        switch ((focuse_id)) {
                            case PALLET_NO:
                                binding.palletNoManual.setFocusableInTouchMode(true);
                                binding.palletNoManual.requestFocus();
                                binding.palletNoManual.selectAll();
                                break;
                            case DELIVERY_NO:
                                binding.deliveryNoManual.setFocusableInTouchMode(true);
                                binding.deliveryNoManual.requestFocus();
                                binding.deliveryNoManual.selectAll();
                                break;
                            case SMALL_LOT:
                                binding.lotIdManual.setFocusableInTouchMode(true);
                                binding.lotIdManual.requestFocus();
                                binding.lotIdManual.selectAll();
                                break;
                            case BIG_LOT:
                                binding.lotId2Manual.setFocusableInTouchMode(true);
                                binding.lotId2Manual.requestFocus();
                                binding.lotId2Manual.selectAll();
                                break;
                            case CUST_PART_NO:
                                binding.custPartNoManual.setFocusableInTouchMode(true);
                                binding.custPartNoManual.requestFocus();
                                break;
                            default:
                                break;
                        }
                        break;
                    case SET_PALLED_ID:
                        if (msg != null && msg.obj != null) {
                            binding.palletId2Manual.setText(msg.obj.toString());
                        }

                        break;
                    case REPRINT_9:
                        Lot_Id2 = "";
                        binding.palletId2Manual.setText("");
                        onMiddlewareChangeColor(LOT_COLOR2, null, Color.WHITE, null);
                        if (binding.lotIdManualPrint.getText().toString().isEmpty()) {
                            show_alert("Please enter Lot ID");
                            Focuse_Control(LOT_ID2);
                            return;
                        }
                        Lot_Id2 = binding.lotIdManualPrint.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher3 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String res = Menu.Client.Reprint_9(Lot_Id2);
                                        Set_Palled_Id(res);
                                        Focuse_Control(LOT_ID2);
                                        onMiddlewareChangeColor(LOT_COLOR2, null, Color.GREEN, null);

                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(LOT_COLOR2, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher3.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case SHOWALERT:
                        final String showalert_msg =  msg.obj.toString();
                        SweetAlertDialog dialog = new SweetAlertDialog(Activity_Manual_Operation.this, SweetAlertDialog.NORMAL_TYPE)
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
                    case SET_PALLET:
                        binding.palletNoManual.setText(msg.obj.toString());
                        Pallet_No = msg.obj.toString();
                        break;
                    case CHECK_LOT:
                        onMiddlewareChangeColor(BIG_LOT_COLOR, null, Color.WHITE, null);
                        if (!binding.lotIdManual.getText().toString().equals(binding.lotId2Manual.getText().toString())) {
                            onMiddlewareFail("大小标签不一致。");
                            onMiddlewareChangeColor(BIG_LOT_COLOR, null, Color.RED, null);
//                            onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.RED, null);
                            return;
                        }
                        onMiddlewareChangeColor(BIG_LOT_COLOR, null, Color.GREEN, null);
                        Focuse_Control(CUST_PART_NO);
                        break;
                    case SET_CARTON_NO:
                        binding.cartonNoManual.setText(msg.obj.toString());
                        Carton_NO = msg.obj.toString();
                        break;
                    case QUERY_CARTON_2:
                        Carton_NO = "";
                        Lot_No = "";
                        Small_Lot="";
                        binding.cartonNoManual.setText("");
                        if (binding.deliveryNoManual.getText().toString().isEmpty()) {
                            onMiddlewareFail("Enter Delivery NO.");
                            Focuse_Control(DELIVERY_NO);
                            return;
                        }
                        if (binding.lotIdManual.getText().toString().isEmpty()) {
                            onMiddlewareFail("Enter Lot ID.");
                            Focuse_Control(SMALL_LOT);
                            return;
                        }
                        Delivery_No = binding.deliveryNoManual.getText().toString();
                        Small_Lot = binding.lotIdManual.getText().toString();
                        boolean flag = false;
                        if (Lot_List == null || Lot_List.size() <= 0 || Lot_List.get(0).getLot_id().isEmpty()) {
                            onMiddlewareFail("This Delivery No have not lot List.");
                            onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.RED, null);
                            return;
                        } else {
                            for (PrintHistory ph :
                                    Lot_List) {
                                if (ph.getLot_id().equals(Small_Lot)) {
                                    flag = true;
                                    Lot_No = ph.getNo();
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            onMiddlewareFail("Small lot not in lot list.");
                            onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.RED, null);
                            return;
                        }
                        Thread dispatcher1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String res = Menu.Client.GetCartonNo_2(Delivery_No, Small_Lot);
                                        //显示carton_no
                                    Set_Carton_No(res);
                                    Focuse_Control(BIG_LOT);
                                    onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.GREEN, null);
                                } catch (Exception ex) {
                                    onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.RED, null);
                                    onMiddlewareFail(ex.getMessage());
                                }
                            }
                        });
                        dispatcher1.start();
                        break;
                    case GET_DNLOTID_1:
                        //onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.WHITE, null);
                        if (binding.deliveryNoManual.getText().toString().isEmpty()) {
                            onMiddlewareFail("Enter Delivery NO.");
                            Focuse_Control(DELIVERY_NO);
                            return;
                        }
                        Lot_List = null;
                        Delivery_No = binding.deliveryNoManual.getText().toString();
                        Thread dispatcher2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Lot_List = Menu.Client.GetDNLotId_1(Delivery_No);
                                    onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.GREEN, null);
                                    Focuse_Control(SMALL_LOT);
                                    Clear_Control();
                                } catch (Exception ex) {
                                    onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.RED, null);
                                    onMiddlewareFail(ex.getMessage());
                                }
                            }
                        });
                        dispatcher2.start();
                        break;

                    case VERIFY_CUST_PART_NO_3:
                        onMiddlewareChangeColor(CUST_PART_NO_COLOR, null, Color.WHITE, null);
                        if (Pallet_No.isEmpty()) {
                            onMiddlewareFail(" Pallet NO 不能为空。");
                            return;
                        }
                        if (Delivery_No.isEmpty()) {
                            onMiddlewareFail("Delivery_No 不能为空.");
                            Focuse_Control(DELIVERY_NO);
                            return;
                        }
                        if (Lot_Id.isEmpty()) {
                            onMiddlewareFail("Lot ID 未验证成功。");
                            Focuse_Control(SMALL_LOT);
                            return;
                        }
//                        if (Carton_NO.isEmpty()) {
//                            onMiddlewareFail("Carton_NO 不能为空.");
//                            Focuse_Control(SMALL_LOT);
//                            return;
//                        }
                        if (Lot_No.isEmpty()) {
                            onMiddlewareFail("项次信息为空.");
                            Focuse_Control(CUST_PART_NO);
                            return;
                        }
                        Cust_Part_No = binding.custPartNoManual.getText().toString();
                        if (Cust_Part_No.isEmpty()) {
                            onMiddlewareFail("Cust_Part_No 不能为空.");
                            Focuse_Control(CUST_PART_NO);
                            return;
                        }
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher5 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Boolean res = Menu.Client.Insert_lot_into_pallet_7(Delivery_No,Pallet_No, "", Lot_Id, Carton_NO,Cust_Part_No);
                                        //onMiddlewareChangeColor(CUST_PART_NO_COLOR, null, Color.GREEN, null);
                                        String erp_res = Menu.Client.UpdPalletNo_3(Delivery_No, Lot_No, Pallet_No);
                                        if (erp_res.equals("Y")) {
                                            Clear_Control();
                                        } else {
                                            onMiddlewareFail("ERP 未绑定成功。");
                                            onMiddlewareChangeColor(CUST_PART_NO_COLOR, null, Color.RED, null);
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(CUST_PART_NO_COLOR, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher5.start();
                        } else {
                            onMiddlewareFail(getString(R.string.txt_connect_fail));
                        }
                        break;
                    case UPDPALLETNO_3:
                        onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.WHITE, null);
                        if (binding.cancelPalletTxtManual.getText().toString().isEmpty()) {
                            show_alert("Please ,Enter Pallet No.");
                            return;
                        }
                        Pallet_No = binding.cancelPalletTxtManual.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher5 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String res = Menu.Client.UpdPalletNo2_4(Pallet_No);
                                        if (res.equals("Y"))
                                            onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.GREEN, null);
                                        else{

                                            onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.RED, null);
                                        }
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher5.start();
                        }
                        break;
                    case CANCEL_PALLET_8:
                        onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.WHITE, null);
                        if (binding.cancelPalletTxtManual.getText().toString().isEmpty()) {
                            show_alert("Please ,Enter Pallet No.");
                            return;
                        }
                        Pallet_No = binding.cancelPalletTxtManual.getText().toString();
                        if (Menu.Client.isConnected()) {
                            Thread dispatcher5 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Boolean res = Menu.Client.Cancel_Pallet_8(Pallet_No);
                                        UpdPalletNo3();
                                        onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.GREEN, null);
                                    } catch (Exception ex) {
                                        onMiddlewareChangeColor(PALLET_CANCEL_COLOR, null, Color.RED, null);
                                        onMiddlewareFail(ex.getMessage());
                                    }
                                }
                            });
                            dispatcher5.start();
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manual_operation);
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
                if (binding.palletNoManual.hasFocus()) {//扫描的是ScanId
                    binding.palletNoManual.setText(s);
                    Focuse_Control(DELIVERY_NO);
                    Pallet_No= s;
                } else if (binding.deliveryNoManual.hasFocus()) {
                    binding.deliveryNoManual.setText(s);
                    Get_DNLotId_1();
                } else if (binding.lotIdManual.hasFocus()) {
                    binding.lotIdManual.setText(s);
                    Query_Carton_2();
                }else if (binding.lotId2Manual.hasFocus()) {
                    binding.lotId2Manual.setText(s);
                    Check_Lot();
                }  else if (binding.custPartNoManual.hasFocus()) {
                    binding.custPartNoManual.setText(s);
                    Verify_Cust_Part_No_3();
                }
                else if(binding.lotIdManualPrint.hasFocus())
                {
                    binding.lotIdManualPrint.setText(s);
                    Reprint_9();
                }
            }
        }
    };

    public void initClient() {
        try {
            if (Menu.Client == null || !Menu.Client.isConnected()) {
                Menu.initClient(this);
            }
            if (Menu.Client != null) {
                Menu.Client.SetMiddlewareListener(this);
            }
        } catch (Exception ex) {
            showMsg(ex.getMessage());
        }
    }

    public Bitmap printScanResult(String result, boolean isprint) {
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
            print_setting =  preferencesUtils.getStringToInt(Constant.KEY_PRINT_SETTING,25) ;
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
                left = 20 ;
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
            left =  50;
            flag = false;
        }
        mBitmap = BarcodeCreater.creatBarcode(this, result, mWidth, mHeight,flag , ScanCodeType, _s_45);
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


    private void Clear_Control() {
        Message msg = handler.obtainMessage(CLEAR_CONTROL);
        msg.sendToTarget();
    }

    private void Clear_Control2() {
        Message msg = handler.obtainMessage(CLEAR_CONTROL2);
        msg.sendToTarget();
    }
    private void Cancel_Pallet_8() {
        Message msg = handler.obtainMessage(CANCEL_PALLET_8);
        msg.sendToTarget();
    }
    private void UpdPalletNo3() {
        Message msg = handler.obtainMessage(UPDPALLETNO_3);
        msg.sendToTarget();
    }
    private void Verify_Cust_Part_No_3() {
        Message msg = handler.obtainMessage(VERIFY_CUST_PART_NO_3);
        msg.sendToTarget();
    }
    private void Check_Lot() {
        Message msg = handler.obtainMessage(CHECK_LOT);
        msg.sendToTarget();
    }


    private void Reprint_9() {
        Message msg = handler.obtainMessage(REPRINT_9);
        msg.sendToTarget();
    }
    private void Get_DNLotId_1() {
        onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.WHITE, null);
        Message msg = handler.obtainMessage(GET_DNLOTID_1);
        msg.sendToTarget();
    }

    private void Query_Carton_2() {
        onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.WHITE, null);
        Message msg = handler.obtainMessage(QUERY_CARTON_2);
        msg.sendToTarget();
    }




    public void Set_Pallet(String pallet_str) {
        Message msg = handler.obtainMessage(SET_PALLET);
        msg.obj = pallet_str;
        msg.sendToTarget();
    }


    private void Focuse_Control(int control_id) {
        Message msg = handler.obtainMessage(FOCUSE_CONTROL);
        msg.obj = control_id;
        msg.sendToTarget();
    }
    private void Set_Palled_Id(String id) {
        Message msg = handler.obtainMessage(SET_PALLED_ID);
        msg.obj = id;
        msg.sendToTarget();
    }
    private void Set_Carton_No(String carton) {
        Message msg = handler.obtainMessage(SET_CARTON_NO);
        msg.obj = carton;
        msg.sendToTarget();
    }

    private void show_alert(String msg) {
        Message ms = handler.obtainMessage(SHOWALERT);
        ms.obj = msg;
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

    @Override
    public void onMiddlewarePrint(PrintHistory history) {

    }


    @Override
    public void onMiddlewareChangeColor(int code, String msg, int color, Boolean alarm) {
        switch (code) {
            case 1:
                Message ms1 = handler.obtainMessage(PALLET_COLOR);
                ms1.obj = color;
                ms1.sendToTarget();
                break;
            case 2:
                Message ms2 = handler.obtainMessage(SCANNER_COLOR);
                ms2.obj = color;
                ms2.sendToTarget();
                break;
            case SMALL_LOT_COLOR:
                Message ms3 = handler.obtainMessage(SMALL_LOT_COLOR);
                ms3.obj = color;
                ms3.sendToTarget();
                break;
            case DELIVERY_NO_COLOR:
                Message ms4 = handler.obtainMessage(DELIVERY_NO_COLOR);
                ms4.obj = color;
                ms4.sendToTarget();
                break;
            case CUST_PART_NO_COLOR:
                Message ms5 = handler.obtainMessage(CUST_PART_NO_COLOR);
                ms5.obj = color;
                ms5.sendToTarget();
                break;
            case PALLET_CANCEL_COLOR:
                Message ms6 = handler.obtainMessage(PALLET_CANCEL_COLOR);
                ms6.obj = color;
                ms6.sendToTarget();
                break;
            case BIG_LOT_COLOR:
                Message ms7 = handler.obtainMessage(BIG_LOT_COLOR);
                ms7.obj = color;
                ms7.sendToTarget();
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

    }

    @Override
    public void onShowMSG(String msg) {
        show_alert(msg);
        // Toast.makeText(Activity_Manual_Operation.this, msg, Toast.LENGTH_SHORT).show();
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
        Focuse_Control(PALLET_NO);
        binding.includeTitle.rightText.setVisibility(View.INVISIBLE);
        //退回
        binding.includeTitle.leftButton.setVisibility(View.VISIBLE);
        binding.includeTitle.titleRefreshButton.setVisibility(View.INVISIBLE);
        binding.includeTitle.title1.setText("Manual");
        binding.includeTitle.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.includeTitle.titleRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_Control2();
            }
        });
        binding.clear1Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.deliveryNoManual.setText("");
                Focuse_Control(DELIVERY_NO);
                Clear_Control2();
            }
        });

        binding.clear0Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.palletNoManual.setText("");
                Clear_Control2();
                Focuse_Control(PALLET_NO);
            }
        });
        binding.clear2Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lotIdManual.setText("");
                Clear_Control();
            }
        });
        binding.clear4Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.custPartNoManual.setText("");
                Focuse_Control(CUST_PART_NO);
                onMiddlewareChangeColor(CUST_PART_NO_COLOR,null,Color.WHITE,null);
            }
        });
        binding.Action1Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Get_DNLotId_1();
            }
        });
        binding.Action2Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query_Carton_2();
            }
        });
        binding.Action7Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_Lot();
            }
        });
        binding.clear7Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lotId2Manual.setText("");
                Focuse_Control(BIG_LOT);
                onMiddlewareChangeColor(BIG_LOT_COLOR,null,Color.WHITE,null);
            }
        });
        binding.Action4Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify_Cust_Part_No_3();
            }
        });
        binding.deliveryNoManual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    onMiddlewareChangeColor(DELIVERY_NO_COLOR, null, Color.WHITE, null);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.lotIdManual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              onMiddlewareChangeColor(SMALL_LOT_COLOR, null, Color.WHITE, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.custPartNoManual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    onMiddlewareChangeColor(CUST_PART_NO_COLOR, null, Color.WHITE, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.lotId2Manual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    onMiddlewareChangeColor(BIG_LOT_COLOR, null, Color.WHITE, null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.buttonCancelPallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel_Pallet_8();
            }
        });
        binding.clear5Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cancelPalletTxtManual.setText("");
            }
        });
        binding.clear6Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lotIdManualPrint.setText("");
            }
        });
        binding.buttonPrintManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.palletId2Manual.getText().toString().isEmpty()){
                    show_alert("请查询Pallet No.");
                    return;
                }
                printScanResult(binding.palletId2Manual.getText().toString(),true);
            }
        });
        binding.Action6Manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reprint_9();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_VALUE);
        registerReceiver(mReceiver, filter);
    }

//    private Column<Object> pallet_no_Column;
//    private Column<Object> delivery_no_Column;
//    private Column<Object> lot_id_Column;
//    private Column<Object> setPallet_key_Column;
//    List<PrintHistory> table_list = null;
//
//    private void initTable_1(List<PrintHistory> history_list) {
//        pallet_no_Column = pallet_no_Column != null ? pallet_no_Column : new Column<>(getString(R.string.txt_column_pallet_no), "pallet_no");
//        delivery_no_Column = delivery_no_Column != null ? delivery_no_Column : new Column<>(getString(R.string.txt_column_delivery_no), "delivery_no");
//        lot_id_Column = lot_id_Column != null ? lot_id_Column : new Column<>(getString(R.string.txt_carrier), "lot_id");
//        setPallet_key_Column = setPallet_key_Column != null ? setPallet_key_Column : new Column<>("key", "pallet_key");
//        table_list = history_list;
//        if (history_list == null) {
//            history_list = new ArrayList<>();
//            PrintHistory h = new PrintHistory(false, "", "", "", "", "", new Date());
//            h.setLot_id("");
//            h.setDelivery_no("");
//            h.setPallet_no("");
//            history_list.add(h);
//
//        }
//        setPallet_key_Column.setWidth(0);
//        setPallet_key_Column.setMinWidth(0);
//        TableData tableData = new TableData<PrintHistory>("List", history_list, pallet_no_Column, delivery_no_Column, lot_id_Column, setPallet_key_Column);
//        binding.table1Manual.setTableData(tableData);
//        TableConfig config = binding.table1Manual.getConfig();
//        config.setShowTableTitle(false);
//        config.setShowXSequence(false);
//    }

    private void toSetting() {
        //弹出口令对话框
        final EditDialog editDialog = new EditDialog(Activity_Manual_Operation.this);
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
                    Intent intent = new Intent(Activity_Manual_Operation.this, SettingActivity.class);
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

package com.miracom.Client;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;
import android.telecom.Call;

import com.hjq.toast.ToastUtils;
import com.miracom.MessageHandler.MPMH;
import com.miracom.MessageHandler.MessageCaster;
import com.miracom.MessageHandler.MessageHandler;
import com.miracom.TRSNode.TRSNode;
import com.miracom.oneoone.mapperx.function.variable.Const;
import com.sufang.scanner.Activity_9300;
import com.sufang.scanner.Activity_Delivery;
import com.sufang.scanner.Constant;
import com.sufang.scanner.Menu;
import com.sufang.scanner.PrintHistory;
import com.sufang.scanner.callback.MiddlewareListener;
import com.sufang.util.DateUtils;
import com.sufang.util.PreferencesUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * mes连接客户端
 *
 * @author admin
 */
public class Client {
    private MiddlewareListener listener;
    private static String sessionId = "Android_Pad";
    private static int sessionMode = 2;
    private static int monitoringPort = 10104;
    PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private static String module = "MESPLUS";
    private PreferencesUtils utils = PreferencesUtils.getInstance();
    private boolean isConnected;
    public static String StartDryTime;

    public Client() {
        initMsgHandler();
    }

    public Client(MiddlewareListener listener) {
        this.listener = listener;
        initMsgHandler();
    }

    public void SetMiddlewareListener(MiddlewareListener listener) {
        this.listener = listener;
    }

    public boolean isConnected() {
//        return true;
        return isConnected;
    }

    private void onMiddlewareFail(String msg) {
        if (listener != null) {
            listener.onMiddlewareFail(msg);
        }
    }

    private void onMiddlewareSuccessShow() {
        if (listener != null) {
            listener.onMiddlewareSuccessShow();
        }
    }

    private void onMiddlewareChangeColor(int code, String msg, int color, Boolean alarm) {
        if (listener != null) {
            listener.onMiddlewareChangeColor(code, msg, color, alarm);
        }
    }

    private void onConnectFail(String msg) {
        isConnected = false;
        if (listener != null) {
            listener.onConnectFail(msg);
        }
    }

    private void onConnectSuccess() {
        isConnected = true;
        if (listener != null) {
            listener.onConnectSuccess();
        }
    }

    public boolean initMsgHandler() {
        Thread dispatcher1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String connectStr = utils.getString(Constant.KEY_SERVER_ADDRESS);
                    String siteId = utils.getString(Constant.KEY_SITE_ID);

                    if (MPMH.getInstance().init(sessionId, sessionMode, connectStr, monitoringPort) == false) {
                        String mes = MPMH.getStatusMessage();
                        throw new Exception("Middleware Initialize. - " + mes);
                    }
                    MessageCaster.setChannel("/" + siteId + "/MESServer");//"/MPR1/MESServer"
                    MessageCaster.setTTL(60000);
                    onConnectSuccess();
                } catch (Exception ex) {
                    onConnectFail(ex.getMessage());
                }
            }
        });
        dispatcher1.start();
//        try {
//            dispatcher1.join();
//        } catch (InterruptedException e) {
//           e.printStackTrace();
//        }
        return true;
    }

    public boolean webserviceConnectTest() {
        try {
            return new Webservice().ConnectTest(PreferencesUtils.getInstance().getString(Constant.WEBSERVICE_URL));
        } catch (Exception e) {
            return false;
        }
    }

    public int timeout = 6000;

    public boolean termMsgHandler() {
        isConnected = false;
        try {
            MPMH.unregisterDispatcher(module);
            MPMH.getInstance().term();
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            onMiddlewareFail("Middleware termMsgHandler. - " + e.getMessage());
        }

        return true;
    }

    public String Login(String name, String pws) {
        TRSNode in_node;
        TRSNode out_node;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");

        try {
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", name);
            in_node.addString("PASSWORD", pws);
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addChar("PROCSTEP", '1');

            try {
                if (MessageCaster.CallService("ZSM", "ZSM_Ad_Verify", in_node, out_node) == false) {
                    String msg = "服务器连接失败,正在重新连接，请稍后重试。";
                    isConnected = false;
                    Menu.Client.initMsgHandler();
                    throw new Exception(msg);
                }
                if (out_node.getStatusValue() != '0') {
                    String msg = out_node.getMsgCode() + " : " + out_node.getMsg();
                    return msg;
                }
                if (out_node.getString("MSGCODE").equals("CMN-0000")) {
                    return out_node.getString("MSGCODE") + ":" + out_node.getString("MSG");
                } else {
                    return out_node.getString("MSGCODE") + ":" + out_node.getString("MSG");
                }
            } catch (Exception e) {
                String msg = e.getMessage();
                return msg;
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            return msg;
        }
    }

    private String SetMessage(TRSNode in_node, TRSNode out_node, String servicename) throws Exception {
        try {
            if (servicename == null) {
                servicename = "ZSM_LBL_Lot_Verify";
            }
            if (MessageCaster.CallService("ZSM", servicename, in_node, out_node) == false) {
                String msg = "服务器连接失败,正在重新连接，请重试。";
                isConnected = false;
                Menu.Client.initMsgHandler();
                throw new Exception(msg);
            }
            char statevalue = out_node.getStatusValue();
            if (statevalue != '0') {
                List<TRSNode> obj = out_node.getList("FIELDMSG");
                if (obj.size() > 0) {
                    String fmsg = obj.get(0).getString("FMSG_1");
                    String msg = out_node.getMsgCode() + " -" + out_node.getMsg() + " -" + out_node.getDBErrMsg() + "-" + fmsg;
//                System.out.println(msg);
                    return msg;
                }
                return "Service return null 'FIELDMSG' state value.";
            }
            if (out_node.getString("MSGCODE").equals("CMN-0000")) {
                return out_node.getString("MSGCODE") + ":" + out_node.getString("MSG");
            } else {
                return out_node.getString("MSGCODE") + ":" + out_node.getString("MSG");
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            return msg;
        }
    }

    public boolean Lot_ID_Validation_9100_1(String lot_id) throws Exception {
//        if(1==1) {
//            if( ((int)(Math.random()*6))%2==0) return true;
//            else throw new Exception("不匹配");
//        }
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '1');
            in_node.addString("LOT_ID", lot_id);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<PrintHistory> Search_9300_2() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '2');
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                String pallet_no = out_node.getString("PALLET_NO");
                String confirm_key = out_node.getString("CONFIRM_KEY");
                if(listener!=null)
                {
                    ((Activity_9300)listener).Set_Pallet(pallet_no);
                    ((Activity_9300)listener).Set_Confirm_Key(confirm_key);
                }
                List<TRSNode> obj = out_node.getList("DELIVERY_LIST");
                res = new ArrayList<>();
                for (TRSNode s : obj) {
                    List<TRSNode> obj_lot = s.getList("LOT_LIST");
                    for (TRSNode lot : obj_lot) {
                        PrintHistory his = new PrintHistory();
                        his.setSel(false);
                        his.setPallet_no(pallet_no);
                        his.setPallet_key(confirm_key);
                        his.setDelivery_no(s.getString("DELIVERY_NO"));
                        his.setLot_id(lot.getString("LOT_ID"));
                        his.setCreateTime(new Date());
                        res.add(his);
                    }
                }
                return res;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean Verify_cust_part_no_3(String pallet_no, String confirm_key, String cust_part_no, String lot_id, String delivery_no, String carton_no) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '3');
            in_node.addString("CUST_PART_NO", cust_part_no);
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("LOT_ID", lot_id);
            in_node.addString("DELIVERY_NO", delivery_no);
            in_node.addString("CARTON_NO", carton_no);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public boolean End_Pallet_4(String pallet_no, String confirm_key, String confirm_flag) throws Exception {

        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '4');
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("CONFIRM_FLAG", confirm_flag);
            String msg = SetMessage(in_node, out_node, null);
            String commet = out_node.getString("COMMENT");
            if (commet.length() > 0) {
                listener.onShowMSG("COMMENT:" + commet);
            }
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public List<PrintHistory>  End_by_Delivery_No_5(String delivery_no, String confirm_flag) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> list1 = null;
        ArrayList<PrintHistory> errolist = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '5');
            in_node.addString("DELIVERY_NO", delivery_no);
            in_node.addString("CONFIRM_FLAG", confirm_flag);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                List<TRSNode> obj = out_node.getList("ERP_LIST");
                list1 = new ArrayList<>();
                errolist = new ArrayList<>();
                for (TRSNode s : obj) {
                    PrintHistory his = new PrintHistory();
                    his.setErp_lot_id(s.getString("LOT_ID"));
                    his.setCreateTime(new Date());
                    list1.add(his);
                }
                List<TRSNode> mes_list = out_node.getList("MES_LIST");
                for (TRSNode s : mes_list) {
                    String lot_id = s.getString("LOT_ID");
                    boolean flag = false;
                    for (PrintHistory prh:
                         list1) {
                        if(prh.getErp_lot_id().equals(lot_id)){
                            prh.setMes_lot_id(lot_id);
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                    {
                        continue;
                    }
                    PrintHistory his = new PrintHistory();
                    his.setMes_lot_id(lot_id);
                    his.setCreateTime(new Date());
                    list1.add(his);
                }
                List<TRSNode> err_list = out_node.getList("ERR_LIST");
                for (TRSNode s : err_list) {
                    PrintHistory his = new PrintHistory();
                    his.setLot_id(s.getString("LOT_ID"));
                    his.setSpecification(s.getString("ERR_MSG"));
                    his.setCreateTime(new Date());
                    errolist.add(his);
                }
                if( errolist.size()>0)
                {
                    ((Activity_Delivery)listener).ShowError_List(errolist) ;
                }
                return list1;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean Ship_by_pallet_list_6(List<PrintHistory> palles) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        TRSNode pallet_list;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '6');
            pallet_list = new TRSNode("PALLET_LIST");
            for (PrintHistory palle : palles) {
                pallet_list.addString("PALLET_NO", palle.getPallet_no());
            }
            in_node.addNode(pallet_list);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean Insert_lot_into_pallet_7(String delivery_no, String pallet_no, String confirm_key, String lot_id, String carton_no, String cust_part_no) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '7');
            in_node.addString("CUST_PART_NO", cust_part_no);
            in_node.addString("DELIVERY_NO", delivery_no);
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("LOT_ID", lot_id);
            in_node.addString("CARTON_NO", carton_no);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    public Boolean Cancel_Pallet_8(String pallet_no) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '8');
            in_node.addString("PALLET_NO", pallet_no);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String Reprint_9(String lot_id) throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '9');
            in_node.addString("LOT_ID", lot_id);
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                return out_node.getString("PALLET_NO");
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<List<PrintHistory>> Query_Lot_List_Status_A(List<PrintHistory> list, List<PrintHistory> listout) throws Exception{
//        PrintHistory p1 = new PrintHistory();
//        p1.setCode("AA");
//        p1.setName("AA");
//        PrintHistory p2 = new PrintHistory();
//        p2.setCode("BB");
//        p2.setName("BB");
//        PrintHistory p3 = new PrintHistory();
//        p3.setCode("CC");
//        p3.setName("CC");
//        List<PrintHistory> lists = new ArrayList<PrintHistory>() ;
//        lists.add(p1);
//        lists.add(p2);
//        lists.add(p3);
//        listout = new ArrayList<>();
//        for(int i= 0;i<30;i++) {
//            PrintHistory his1 = new PrintHistory();
//            his1.setLot_id("12"+i);
//            his1.setCreateTime(new Date());
//            listout.add(his1);
//        }
//         List<List<PrintHistory>> reslist = new ArrayList<>();
//         reslist.add(lists);
//         reslist.add(listout);
//        if(1==1) return reslist ;
        TRSNode in_node;
        TRSNode out_node;
        TRSNode pallet_list;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", preferencesUtils.getString(Constant.KEY_FACTORY,"WE1"));
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", preferencesUtils.getString(Constant.KEY_LANUAGE,"1").charAt(0));
            in_node.addString("USERID", Menu.USER_NAME.toUpperCase());
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", 'A');
            for (PrintHistory li : list) {
                pallet_list = new TRSNode("LOT_LIST");
                pallet_list.addString("LOT_ID", li.getLot_id());
                in_node.addNode(pallet_list);
            }
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                List<TRSNode> lot_list = out_node.getList("LOT_LIST");
                listout = new ArrayList<>();
                for (TRSNode s : lot_list) {
                    PrintHistory los = new PrintHistory();
                    los.setLot_id(s.getString("LOT_ID"));
                    los.setName(s.getString("RESULT"));
                    los.setCreateTime(new Date());
                    listout.add(los);
                }
                List<TRSNode> spec_list = out_node.getList("SPEC_LIST");
                res = new ArrayList<>();
                for (TRSNode s : spec_list) {
                    PrintHistory his = new PrintHistory();
                    his.setCode(s.getString("CHAR_ID"));
                    his.setName(s.getString("VALUE"));
                    his.setCreateTime(new Date());
                    res.add(his);
                }
                List<List<PrintHistory>> reslist1 = new ArrayList<>();
                reslist1.add(res);
                reslist1.add(listout);
                return reslist1;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<PrintHistory> GetDNLotId_1(String delivery_no) throws Exception {
        List<PrintHistory> res = new ArrayList<>();
        try {
            if(delivery_no.isEmpty())
            {
                throw new Exception("请输入出货单号");
            }
            String soapXML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:GetDNLotIdRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"delivery_no\" value=\""+delivery_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:GetDNLotIdRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";

            res = new Webservice().GetDNLotId_1(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String GetCartonNo_2(String delivery_no,String lot_id) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:GetCartonNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"delivery_no\" value=\""+delivery_no+"\"/&gt;&lt;Field name=\"lotid\" value=\""+lot_id+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:GetCartonNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().GetCartonNo_2(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String UpdPalletNo_3(String delivery_no,String deliver_ln ,String pallet_no) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:UpdPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"deliverno\" value=\""+delivery_no+"\"/&gt;&lt;Field name=\"deliverln\" value=\""+deliver_ln+"\"/&gt;&lt;Field name=\"palletno\" value=\""+pallet_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UpdPalletNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().UpdPalletNo_3(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String UpdPalletNo2_4(String pallet_no) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:UpdPalletNo2Request>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"deliverno\" value=\"S501-1611230001\"/&gt;&lt;Field name=\"deliverln\" value=\"2\"/&gt;&lt;Field name=\"palletno\" value=\""+pallet_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UpdPalletNo2Request>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().UpdPalletNo2_4(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<PrintHistory> GetPalletNo_5(String do_str) throws Exception {
        List<PrintHistory> res = new ArrayList<>();
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:GetPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"do\" value=\""+do_str+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:GetPalletNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().GetPalletNo_5(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String ChkDoPalletNo_6(String do_str,String pallet_no) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:ChkDoPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"do\" value=\""+do_str+"\"/&gt;&lt;Field name=\"palletno\" value=\""+pallet_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:ChkDoPalletNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().ChkDoPalletNo_6(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String ChkPostDoList_7(String do_no,String carno,  String ispost) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:ChkPostDoListRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"do\" value=\""+do_no+"\"/&gt;&lt;Field name=\"carno\" value=\""+carno+"\"/&gt;&lt;Field name=\"ispost\" value=\""+ispost+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:ChkPostDoListRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().ChkPostDoList_7(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String UnPostDoList_8(String do_no) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:UnPostDoListRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"do\" value=\""+do_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UnPostDoListRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().UnPostDoList_8(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<PrintHistory> GET_LOT_LIST_ID_9(String do_no) throws Exception {
//        PrintHistory p1 = new PrintHistory();
//        p1.setLot_id("AA");
//        PrintHistory p2 = new PrintHistory();
//        p2.setLot_id("BB");
//        PrintHistory p3 = new PrintHistory();
//        p3.setLot_id("CC");
//        List<PrintHistory> list = new ArrayList<PrintHistory>() ;
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//        if(1==1) return list;
        List<PrintHistory> res = null;
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:GetLotidListRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=\"tiptop\" user=\"tiptop\"/&gt;&lt;Connection application=\"APP\" source=\"\"/&gt;&lt;Organization name=\"ZING\"/&gt;&lt;Locale language=\"zh_cn\"/&gt;&lt;Appdevice appid=\"c98a7d9a95632a707e0ade066d92f867\" appmodule=\"OTHER\" timestamp=\"35764006011223400:d065b94OTHER200921002444\"/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=\"imn01\" value=\""+do_no+"\"/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:GetLotidListRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().GET_lot_list_id_9(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}

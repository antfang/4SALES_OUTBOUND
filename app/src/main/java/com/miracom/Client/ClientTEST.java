package com.miracom.Client;

import com.miracom.TRSNode.*;
import com.miracom.MessageHandler.*;
import com.sufang.scanner.Constant;
import com.sufang.scanner.Menu;
import com.sufang.scanner.PrintHistory;
import com.sufang.util.PreferencesUtils;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientTEST {

    PreferencesUtils preferencesUtils = null;

    public static void main(String[] args) {
        ClientTEST ct = new ClientTEST();
//        ct.preferencesUtils = new PreferencesUtils();
//        if (ct.InitMsgHandler("10.10.17.25:10101","MPR1") == false)
            if (ct.InitMsgHandler() == false)
            return;
        try {

            try {
                System.out.println("1");
                ct.Get_Carr_Detail();//1
            } catch (Exception ex) {
               System.out.println(ex.getMessage());
            }
//            try {
//                System.out.println("2");
//                ct.Search_9300_2();//2
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//            try {
//                System.out.println("3");
//                ct.Verify_cust_part_no_3();//3
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//            try {
//                System.out.println("4");
//                ct.End_Pallet_4();//4
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }

//            try {
//                System.out.println("5");
//                ct.End_by_Delivery_No_5();//5
//            } catch (Exception ex) {
//                //System.out.println(ex.getMessage());
//            }
//            try {
//                System.out.println("6");
//                ct.Ship_by_pallet_list_6();//6
//            } catch (Exception ex) {
//              //  System.out.println(ex.getMessage());
//            }
//            try {
//                System.out.println("8");
//                ct.Cancel_Pallet_8();//8
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//            try {
//                System.out.println("9");
//               String res = ct.Reprint_9();//9
//                System.out.println(res);
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean Get_Carr_Detail() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '1');
            in_node.addString("CRR_ID", "CF0107");
            String msg = SetMessage(in_node, out_node, "ZSM_EQS_View_Carrier");
            if (msg.trim().contains(Constant.SUCCESS)) {
                StringBuffer str = new StringBuffer(100);
                str.append(out_node.getString(""));
                str.append("\nCRR_DESC:"+out_node.getString("CRR_DESC"));
                str.append("\nFACTORY:"+out_node.getString("FACTORY"));
                str.append("\nCRR_GROUP:"+out_node.getString("CRR_GROUP"));
                str.append("\nCRR_STATUS:"+out_node.getString("CRR_STATUS"));
                str.append("\nCRR_TYPE1:"+out_node.getString("CRR_TYPE1"));
                str.append("\nCRR_TYPE2:"+out_node.getString("CRR_TYPE2"));
                str.append("\nCRR_TYPE3:"+out_node.getString("CRR_TYPE3"));
                str.append("\nUSAGE_LIMIT_COUNT:"+out_node.getInt("USAGE_LIMIT_COUNT"));
                str.append("\nUSAGE_LIMIT_TIME:"+out_node.getInt("USAGE_LIMIT_TIME"));
                str.append("\nUSAGE_COUNT:"+out_node.getInt("USAGE_COUNT"));
                str.append("\nCLEAN_LIMIT_COUNT:"+out_node.getInt("CLEAN_LIMIT_COUNT"));
                str.append("\nCLEAN_COUNT:"+out_node.getInt("CLEAN_COUNT"));
                str.append("\nMOVE_FLAG:"+out_node.getChar("MOVE_FLAG"));
                str.append("\nEMPTY_FLAG:"+out_node.getChar("EMPTY_FLAG"));
                str.append("\nRES_ID:"+out_node.getString("RES_ID"));
                str.append("\nPORT_ID:"+out_node.getString("PORT_ID"));
                str.append("\nLOCATION_1:"+out_node.getString("LOCATION_1"));
                str.append("\nLOCATION_2:"+out_node.getString("LOCATION_2"));
                str.append("\nLOCATION_3:"+out_node.getString("LOCATION_3"));
                str.append("\nLOCATION_4:"+out_node.getString("LOCATION_4"));
                str.append("\nLOCATION_5:"+out_node.getString("LOCATION_5"));
                str.append("\nFINISH_CLEAN_FLAG:"+out_node.getChar("FINISH_CLEAN_FLAG"));
                str.append("\nLAST_CLEAN_TIME:"+out_node.getString("LAST_CLEAN_TIME"));
                str.append("\nBATCH_ID:"+out_node.getString("BATCH_ID"));
                List<TRSNode> obj = out_node.getList("LOT_LIST");
                for( TRSNode node:obj)
                {
                    str.append("\nLOT_ID:"+node.getString("LOT_ID"));
                    str.append("\nQTY:"+node.getDouble("QTY"));
                }
                List<TRSNode> obj1= out_node.getList("WAFER_LIST");
                for( TRSNode node:obj1)
                {
                    str.append("\nWAFER_ID:"+node.getString("WAFER_ID"));
                    str.append("\nSLOT_NO:"+node.getString("SLOT_NO"));
                    str.append("\nPHY_WAFER_ID:"+node.getString("PHY_WAFER_ID"));
                    str.append("\nLASER_MARK_TYPE:"+node.getString("LASER_MARK_TYPE"));
                }
                System.out.print(str);
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    String pallet_no = "";
    String confirm_key = "";

    public boolean Lot_ID_Validation_9100_1() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '1');
            in_node.addString("LOT_ID", "91614015337");
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
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", Menu.USER_NAME);
            in_node.addString("PASSWORD", Menu.PASSWORD);
            in_node.addChar("PROCSTEP", '2');
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                pallet_no = out_node.getString("PALLET_NO");
                confirm_key = out_node.getString("CONFIRM_KEY");
                List<TRSNode> obj = out_node.getList("DELIVERY_LIST");
                res = new ArrayList<>();
                for (TRSNode s : obj) {
                    List<TRSNode> obj_lot = s.getList("LOT_LIST");
                    for (TRSNode lot : obj_lot) {
                        PrintHistory his = new PrintHistory();
                        his.setSel(false);
                        his.setPallet_no(pallet_no);
                        his.setPallet_key(confirm_key);
                        System.out.println(pallet_no);
                        System.out.println(confirm_key);
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
    public boolean Verify_cust_part_no_3() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '3');
            in_node.addString("CUST_PART_NO", "WNPB300Z");
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("LOT_ID", "80636044316");
            in_node.addString("DELIVERY_NO", "S501-1812240001");
            in_node.addString("CARTON_NO", "1234");
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
    public boolean End_Pallet_4() throws Exception {

        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '4');
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("CONFIRM_FLAG", "Y");
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                String commet = out_node.getString("COMMENT");
                if (commet.length() > 0) {
                    throw new Exception("COMMENT:" + commet);
                }
                return true;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
    public List<PrintHistory> End_by_Delivery_No_5() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '5');
            in_node.addString("DELIVERY_NO", "S501-1812240001");
            in_node.addString("CONFIRM_FLAG", "Y");
            String msg = SetMessage(in_node, out_node, null);
            if (msg.trim().contains(Constant.SUCCESS)) {
                List<TRSNode> obj = out_node.getList("ERP_LIST");
                res = new ArrayList<>();
                for (TRSNode s : obj) {
                    PrintHistory his = new PrintHistory();
                    his.setLot_id(s.getString("LOT_ID"));
                    his.setCreateTime(new Date());
                    res.add(his);
                }
                List<TRSNode> mes_list = out_node.getList("MES_LIST");
                for (TRSNode s : mes_list) {
                    PrintHistory his = new PrintHistory();
                    his.setLot_id(s.getString("LOT_ID"));
                    his.setCreateTime(new Date());
                    res.add(his);
                }
                List<TRSNode> err_list = out_node.getList("ERR_LIST");
                for (TRSNode s : err_list) {
                    PrintHistory his = new PrintHistory();
                    his.setLot_id(s.getString("LOT_ID"));
                    his.setOrder_no(s.getString("ERR_MSG"));
                    his.setCreateTime(new Date());
                    res.add(his);
                }
                return res;
            } else {
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public Boolean Ship_by_pallet_list_6() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        TRSNode pallet_list;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '6');
            pallet_list = new TRSNode("PALLET_LIST");
            pallet_list.addString("PALLET_NO", "P20201117009");
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
    public Boolean Insert_lot_into_pallet_7() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;

        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '3');
            in_node.addString("CUST_PART_NO", "WNPB300Z");
            in_node.addString("PALLET_NO", pallet_no);
            in_node.addString("CONFIRM_KEY", confirm_key);
            in_node.addString("LOT_ID", "80636044316");
            in_node.addString("DELIVERY_NO", "S501-1812240001");
            in_node.addString("CARTON_NO", "1234");
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
    public Boolean Cancel_Pallet_8() throws Exception{
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
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
    public String Reprint_9() throws Exception {
        TRSNode in_node;
        TRSNode out_node;
        ArrayList<PrintHistory> res = null;
        in_node = new TRSNode("ViewLotIn");
        out_node = new TRSNode("ViewLotOut");
        try {
            in_node.addString("FACTORY", "WE1");
            in_node.addString("PASSPORT", "");
            in_node.addChar("LANGUAGE", '1');
            in_node.addString("USERID", "ADMIN");
            in_node.addString("PASSWORD", "");
            in_node.addChar("PROCSTEP", '9');
            in_node.addString("LOT_ID", "80636044316");
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
    private String SetMessage(TRSNode in_node, TRSNode out_node, String servicename) throws Exception {
        try {
            if (servicename == null) {
                servicename = "ZSM_LBL_Lot_Verify";
            }
            if (MessageCaster.CallService("ZSM", servicename, in_node, out_node,"",600000,DeliveryMode.RReply) == false) {
                String msg = "服务器连接失败,正在重新连接，请重试。";
//				isConnected = false;
                InitMsgHandler();
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
    public boolean InitMsgHandler() {
        if (MPMH.getInstance().init("Android_Pad", 2, "10.10.17.25:10101", 10104) == false) {
            System.out.println("Middleware Initialize. - " + MPMH.getStatusMessage());
            return false;
        }

        MessageCaster.setChannel("/MPR1/MESServer");
        MessageCaster.setTTL(60000);

        return true;
    }
    public boolean InitMsgHandler(String service,String channel) {
        if (MPMH.getInstance().init("Android_Pad", 2, service, 10104) == false) {
            System.out.println("Middleware Initialize. - " + MPMH.getStatusMessage());
            return false;
        }

        MessageCaster.setChannel("/"+channel+"/MESServer");
        MessageCaster.setTTL(10000);

        return true;
    }
    public boolean TermMsgHandler() {
        try {
            MPMH.unregisterDispatcher("MESPLUS");
            MPMH.getInstance().term();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
    public List<PrintHistory> GetDNLotId_1(String delivery_no) throws Exception {
        List<PrintHistory> res = new ArrayList<>();
        try {
            String soapXML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:GetDNLotIdRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;delivery_no&quot; value=&quot;" + delivery_no + "&quot;/&gt; &lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
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
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;delivery_no&quot; value=&quot;"+delivery_no+"&quot;/&gt;&lt;Field name=&quot;lotid&quot; value=&quot;"+lot_id+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
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
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;deliverno&quot; value=&quot;"+delivery_no+"&quot;/&gt;&lt;Field name=&quot;deliverln&quot; value=&quot;"+deliver_ln+"&quot;/&gt;&lt;Field name=&quot;palletno&quot; value=&quot;"+pallet_no+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
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
                            "    <q0:UpdPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;deliverno&quot; value=&quot;S501-1611230001&quot;/&gt;&lt;Field name=&quot;deliverln&quot; value=&quot;2&quot;/&gt;&lt;Field name=&quot;palletno&quot; value=&quot;"+pallet_no+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UpdPalletNoRequest>\n" +
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
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;do&quot; value=&quot;"+do_str+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:GetPalletNoRequest>";
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
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;do&quot; value=&quot;"+do_str+"&quot;/&gt;&lt;Field name=&quot;palletno&quot; value=&quot;"+pallet_no+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:ChkDoPalletNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().ChkDoPalletNo_6(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public String ChkPostDoList_7(String do_no,String carno,String palletno,String lotid,String ispost) throws Exception {
        String res = "";
        try {
            String soapXML =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                            "  <soapenv:Body>\n" +
                            "    <q0:UpdPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;do&quot; value=&quot;"+do_no+"&quot;/&gt;&lt;Field name=&quot;carno&quot; value=&quot;"+carno+"&quot;/&gt;&lt;Field name=&quot;palletno&quot; value=&quot;"+palletno+"&quot;/&gt;&lt;Field name=&quot;lotid&quot; value=&quot;"+lotid+"&quot;/&gt;&lt;Field name=&quot;ispost&quot; value=&quot;"+ispost+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UpdPalletNoRequest>\n" +
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
                            "    <q0:UpdPalletNoRequest>\n" +
                            "      <q0:request>&lt;Request&gt;&lt;Access&gt;&lt;Authentication password=&quot;tiptop&quot; user=&quot;tiptop&quot;/&gt;&lt;Connection application=&quot;APP&quot; source=&quot;&quot;/&gt;&lt;Organization name=&quot;ZING&quot;/&gt;&lt;Locale language=&quot;zh_cn&quot;/&gt;&lt;Appdevice appid=&quot;c98a7d9a95632a707e0ade066d92f867&quot; appmodule=&quot;OTHER&quot; timestamp=&quot;35764006011223400:d065b94OTHER200921002444&quot;/&gt;&lt;/Access&gt;&lt;RequestContent&gt;&lt;Parameter&gt;&lt;Record&gt;&lt;Field name=&quot;do&quot; value=&quot;"+do_no+"&quot;/&gt;&lt;/Record&gt;&lt;/Parameter&gt;&lt;Document/&gt;&lt;/RequestContent&gt;&lt;/Request&gt;</q0:request>\n" +
                            "    </q0:UpdPalletNoRequest>\n" +
                            "  </soapenv:Body>\n" +
                            "</soapenv:Envelope>";
            res = new Webservice().UnPostDoList_8(preferencesUtils.getString(Constant.WEBSERVICE_URL), soapXML);
            return res;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}


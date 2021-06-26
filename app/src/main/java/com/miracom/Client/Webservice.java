package com.miracom.Client;


import android.util.Xml;

import com.miracom.Client.network.Callback;
import com.sufang.scanner.PrintHistory;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webservice {
    private String targetNameSpace;
    private String getSupportProvince;
    private String WSDL;

    public static void main(String[] args) {
        Object[] xml = new Object[]{""};
        try {
            //  new Webservice().GetSNData_1("http://localhost:8082/web/ws/r/aws_ttsrv2_toptest?WSDL", "");
            Webservice wb = new Webservice();
            List<PrintHistory> list = wb.GetDNLotId_1("", "");
            System.out.print(list.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean ConnectTest(String url_str) throws Exception {
        OutputStream os = null;
        try {
            URL url = new URL(url_str);
            //第二步：打开一个通向服务地址的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String heander = connection.getHeaderField(0);
            if (heander != null && heander.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<PrintHistory> GetDNLotId_1(String url_str, String soapXML) throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML); //get_xml_test_1();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_DNLotId_1(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }
    public String  GetCartonNo_2(String url_str, String soapXML) throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_2();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_CartonNo_2(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        throw new Exception("No Carton data.");

    }
    public String  UpdPalletNo_3(String url_str, String soapXML) throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_3();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_Success(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }
    public String  UpdPalletNo2_4(String url_str, String soapXML) throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_4();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_Success(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }
    public List<PrintHistory> GetPalletNo_5(String url_str, String soapXML)  throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_5();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_GetPalletNo_5(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }

    public String ChkDoPalletNo_6(String url_str, String soapXML)  throws Exception {
        String xml_str =  GetSNDataString(url_str, soapXML);//get_xml_test_6();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_Success(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }
    public String ChkPostDoList_7(String url_str, String soapXML) throws Exception {
        String xml_str =  GetSNDataString(url_str, soapXML);//get_xml_test_3();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_Success(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }

    public String UnPostDoList_8(String url_str, String soapXML)  throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_3();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return GetData_Success(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }

    public List<PrintHistory> GET_lot_list_id_9(String url_str, String soapXML)  throws Exception {
        String xml_str = GetSNDataString(url_str, soapXML);//get_xml_test_3();
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String name = xml.getName();
                    if (name.equals("response")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
//                        String datavalue = StringEscapeUtils.unescapeXml(xml.nextText());
                        String datavalue = xml.nextText().replace("&lt;", "<").replace("&gt;", ">");

                        return get_lot_list_id_9(datavalue);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return null;
    }

    private List<PrintHistory> GetData_DNLotId_1(String xml_str) throws Exception {
        PrintHistory part = null;
        List<PrintHistory> parts = null;
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    parts = new ArrayList<PrintHistory>();
                    break;
                case XmlPullParser.START_TAG:
                    String value = xml.getName();
                    if (value.equals("RecordSet")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
                        part = new PrintHistory();
                        //获取属性值
                        part.setId(new Integer(xml.getAttributeValue(0)));
                    } else if (value.equals("Status")) {
                        String code = xml.getAttributeValue("", "code");
                        String value_ = xml.getAttributeValue("", "description");
                        if (code.equals("-1")) {
                            throw new Exception(value_);
                        }
                    } else if (value.equals("Field")) {
                        String name_ = xml.getAttributeValue("", "name");
                        String value_ = xml.getAttributeValue("", "value");
                        if (name_.equals("ogb01"))//出货单号
                        {
                            part.setOrder_no(value_);
                        } else if (name_.equals("ogb03"))//项次
                        {
                            part.setNo(value_);
                        } else if (name_.equals("ogb04"))//料件编号
                        {
                            part.setCode(value_);
                        } else if (name_.equals("ogb06"))//品名
                        {
                            part.setName(value_);
                        } else if (name_.equals("ima021"))//规格
                        {
                            part.setSpecification(value_);
                        } else if (name_.equals("ogb092"))//lot_id
                        {
                            part.setLot_id(value_);
                        } else if (name_.equals("ogb12"))//数量
                        {
                            part.setQty(value_);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xml.getName().equals("RecordSet")) {
                        parts.add(part);
                        part = null;
                    }
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return parts;
    }
    private String GetData_CartonNo_2(String xml_str) throws Exception {
        String  part = "";
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String value = xml.getName();
                    if (value.equals("RecordSet")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象

                    } else if (value.equals("Status")) {
                        String code = xml.getAttributeValue("", "code");
                        String value_ = xml.getAttributeValue("", "description");
                        if (code.equals("-1")) {
                            throw new Exception(value_);
                        }
                    } else if (value.equals("Field")) {
                        String name_ = xml.getAttributeValue("", "name");
                        String value_ = xml.getAttributeValue("", "value");
                        if (name_.equals("ogbud05"))//CartonNo
                        {
                            part = value_;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xml.getName().equals("RecordSet")) {

                    }
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return part;
    }
    private String GetData_Success(String xml_str) throws Exception {
        String  part = "";
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    String value = xml.getName();
                    if (value.equals("RecordSet")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象

                    } else if (value.equals("Status")) {
                        String code = xml.getAttributeValue("", "code");
                        String value_ = xml.getAttributeValue("", "description");
                        if (code.equals("-1")) {
                            throw new Exception(value_);
                        }
                    } else if (value.equals("Field")) {
                        String name_ = xml.getAttributeValue("", "name");
                        String value_ = xml.getAttributeValue("", "value");
                        if (name_.equals("success"))//success
                        {
                            part = value_;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xml.getName().equals("RecordSet")) {

                    }
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return part;
    }
    private List<PrintHistory> GetData_GetPalletNo_5(String xml_str) throws Exception {
        PrintHistory part = null;
        List<PrintHistory> parts = null;
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    parts = new ArrayList<PrintHistory>();
                    break;
                case XmlPullParser.START_TAG:
                    String value = xml.getName();
                    if (value.equals("RecordSet")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
                        part = new PrintHistory();
                        //获取属性值
                        part.setId(new Integer(xml.getAttributeValue(0)));
                    } else if (value.equals("Status")) {
                        String code = xml.getAttributeValue("", "code");
                        String value_ = xml.getAttributeValue("", "description");
                        if (code.equals("-1")) {
                            throw new Exception(value_);
                        }
                    } else if (value.equals("Field")) {
                        String name_ = xml.getAttributeValue("", "name");
                        String value_ = xml.getAttributeValue("", "value");
                        if (name_.equals("ogbud03"))//托号
                        {
                            part.setPallet_no(value_);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xml.getName().equals("RecordSet")) {
                        parts.add(part);
                        part = null;
                    }
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return parts;
    }
    private List<PrintHistory> get_lot_list_id_9(String xml_str) throws Exception {
        PrintHistory part = null;
        List<PrintHistory> parts = null;
        InputStream inputstream = new ByteArrayInputStream(xml_str.getBytes());
        XmlPullParser xml = Xml.newPullParser();
        xml.setInput(inputstream, "UTF-8");
        int event = xml.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                //开始解析文档
                case XmlPullParser.START_DOCUMENT:
                    parts = new ArrayList<PrintHistory>();
                    break;
                case XmlPullParser.START_TAG:
                    String value = xml.getName();
                    if (value.equals("RecordSet")) {//person对象的初始化必须在这里初始化不然可能出现为null的现象
                        part = new PrintHistory();
                        //获取属性值
                        part.setId(new Integer(xml.getAttributeValue(0)));
                    } else if (value.equals("Status")) {
                        String code = xml.getAttributeValue("", "code");
                        String value_ = xml.getAttributeValue("", "description");
                        if (code.equals("-1")) {
                            throw new Exception(value_);
                        }
                    } else if (value.equals("Field")) {
                        String name_ = xml.getAttributeValue("", "name");
                        String value_ = xml.getAttributeValue("", "value");
                        if (name_.equals("imn17"))//托号
                        {
                            part.setLot_id(value_);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xml.getName().equals("RecordSet")) {
                        parts.add(part);
                        part = null;
                    }
                    break;
            }
            //解析下一个对象
            event = xml.next();
        }
        return parts;
    }


    private String GetSNDataString(String url_str, String soapXML) throws Exception {
        //第一步：创建服务地址，不是WSDL地址
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(url_str);
            //第二步：打开一个通向服务地址的连接
            connection = (HttpURLConnection) url.openConnection();
            byte[] data = soapXML.getBytes();
            //第三步：设置参数
            //3.1发送方式设置：POST必须大写
            connection.setRequestMethod("POST");
            //3.2设置数据格式：content-type
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setRequestProperty("SOAPAction", "\"\"");
            connection.setRequestProperty("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
            //3.3设置输入输出，因为默认新创建的connection没有读写权限，
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //第四步：组织SOAP数据，发送请求
            connection.connect();
            os = connection.getOutputStream();
            os.write(data);
            //第五步：接收服务端响应，打印（xml格式数据）
            int responseCode = connection.getResponseCode();
            if (200 == responseCode) {//表示服务端响应成功
                InputStream is = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                StringBuilder sb = null;
                try {
                    is = connection.getInputStream();
                    String message = connection.getResponseMessage();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    sb = new StringBuilder();
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        sb.append(temp);
                     System.out.println(temp);
                    }
                    String res = sb.toString();
                    return res;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    br.close();
                    isr.close();
                    is.close();
                }
            } else {
                String mesg = connection.getResponseMessage();
                throw new Exception(mesg);
                //如果服务器返回的HTTP状态不是HTTP_OK，则表示发生了错误，此时可以通过如下方法了解错误原因。
//            InputStream is = connection.getErrorStream();    //通过getErrorStream了解错误的详情，因为错误详情也以XML格式返回，因此也可以用JDOM来获取。
//            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//            BufferedReader in = new BufferedReader(isr);
//            String inputLine;
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream("result.xml")));// 将结果存放的位置
//            while ((inputLine = in.readLine()) != null) {
//                System.out.println(inputLine);
//                bw.write(inputLine);
//                bw.newLine();
//                bw.close();
//            }
//            in.close();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

//    private String get_xml_test_1() {
//        String xml = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:GetDNLotIdResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter/&gt;\n" +
//                "    &lt;Document&gt;\n" +
//                "      &lt;RecordSet id=\"1\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"1\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"2\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"2\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"3\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"3\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n";
//        xml += "      &lt;RecordSet id=\"4\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"4\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"5\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"5\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044308\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"6\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"6\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088314\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"7\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"7\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"8\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"8\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044314\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"9\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"9\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044315\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"10\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"10\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"11\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"11\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80250044320\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"12\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"12\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80355044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"13\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"13\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"14\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"14\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803550D2313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n";
//        xml += "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"15\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"15\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"16\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"16\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044311\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"17\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"17\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2320\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"18\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"18\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560H6306\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"19\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"19\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"20\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"20\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018301\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"21\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"21\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803570H6302\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"22\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"22\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"23\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"23\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"24\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"24\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816018306\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"25\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"25\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018303\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"26\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"26\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088312\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"27\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"27\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n";
//        xml += "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"28\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"28\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018304\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"29\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"29\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"30\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"30\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"31\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"31\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"32\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"32\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"33\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"33\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"34\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"34\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80914037316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"35\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"35\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"36\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"36\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"37\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"37\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"38\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"38\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"39\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"39\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2321\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"40\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"40\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"41\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"41\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560H6305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"42\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"42\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"43\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"43\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"44\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"44\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "    &lt;/Document&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:GetDNLotIdResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>\n" +
//                "HTTP/1.1 200 OK\n" +
//                "Date: Thu, 19 Nov 2020 02:41:21 GMT\n" +
//                "Server: Apache/2.2.15 (Red Hat)\n" +
//                "X-FourJs-Server: GAS/2.40.29-127866\n" +
//                "Connection: close\n" +
//                "Content-Length: 28764\n" +
//                "Content-Type: text/xml; charset=UTF-8\n" +
//                "\n" +
//                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:GetDNLotIdResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter/&gt;\n" +
//                "    &lt;Document&gt;\n" +
//                "      &lt;RecordSet id=\"1\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"1\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"2\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"2\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n";
//        xml += "      &lt;RecordSet id=\"3\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"3\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"4\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"4\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"5\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"5\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044308\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"6\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"6\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088314\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"7\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"7\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"8\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"8\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044314\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"9\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"9\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044315\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"10\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"10\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"11\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"11\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80250044320\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"12\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"12\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80355044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"13\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"13\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"14\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"14\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803550D2313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"15\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"15\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80356044313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"16\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"16\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044311\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"17\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"17\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2320\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"18\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"18\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560H6306\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"19\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"19\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"20\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"20\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018301\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"21\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"21\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803570H6302\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"22\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"22\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"23\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"23\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n";
//        xml += "      &lt;RecordSet id=\"24\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"24\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816018306\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"25\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"25\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018303\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"26\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"26\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088312\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"27\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"27\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"28\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"28\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80357018304\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"29\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"29\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80636044318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"30\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"30\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251088318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"31\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"31\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"32\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"32\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"33\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"33\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"34\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"34\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80914037316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"35\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"35\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80251044313\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"36\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"36\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044316\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"37\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"37\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"38\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"38\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"39\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"39\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560D2321\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"40\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"40\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"41\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"41\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560H6305\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"42\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"42\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80816040319\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"43\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"43\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"80546044317\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"44\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogb01\" value=\"S501-1812240001\"/&gt;\n" +
//                "            &lt;Field name=\"ogb03\" value=\"44\"/&gt;\n" +
//                "            &lt;Field name=\"ogb04\" value=\"3T38XPP140B\"/&gt;\n" +
//                "            &lt;Field name=\"ogb06\" value=\"38-PTC_Res(10-25)_NTC(110)\"/&gt;\n" +
//                "            &lt;Field name=\"ima021\" value=\"Customer Spec No(Spec-QRA-002 Rev.:01);Part No(WNPB300Z)\"/&gt;\n" +
//                "            &lt;Field name=\"ogb092\" value=\"803560J8318\"/&gt;\n" +
//                "            &lt;Field name=\"ogb12\" value=\"25.000\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "    &lt;/Document&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:GetDNLotIdResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml;
//    }
//
//    private String get_xml_test_2()
//    {
//        String xml_str = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml_str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:GetCartonNoResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter/&gt;\n" +
//                "    &lt;Document&gt;\n" +
//                "      &lt;RecordSet id=\"1\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogbud05\" value=\"12345\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "    &lt;/Document&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:GetCartonNoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml_str;
//    }
//    private String get_xml_test_3()
//    {
//        String xml_str = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml_str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:UpdPalletNoResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter&gt;\n" +
//                "      &lt;Record&gt;\n" +
//                "        &lt;Field name=\"success\" value=\"Y\"/&gt;\n" +
//                "      &lt;/Record&gt;\n" +
//                "    &lt;/Parameter&gt;\n" +
//                "    &lt;Document/&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:UpdPalletNoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml_str;
//    }
//    private String get_xml_test_4()
//    {
//        String xml_str = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml_str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:UpdPalletNoResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter&gt;\n" +
//                "      &lt;Record&gt;\n" +
//                "        &lt;Field name=\"success\" value=\"Y\"/&gt;\n" +
//                "      &lt;/Record&gt;\n" +
//                "    &lt;/Parameter&gt;\n" +
//                "    &lt;Document/&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:UpdPalletNoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml_str;
//    }
//
//    private String get_xml_test_5()
//    {
//        String xml_str = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml_str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:GetPalletNoResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter/&gt;\n" +
//                "    &lt;Document&gt;\n" +
//                "      &lt;RecordSet id=\"1\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogbud04\" value=\"123456789\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"2\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogbud04\" value=\"P161027C02\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "      &lt;RecordSet id=\"3\"&gt;\n" +
//                "        &lt;Master name=\"Master\"&gt;\n" +
//                "          &lt;Record&gt;\n" +
//                "            &lt;Field name=\"ogbud04\" value=\"P161027C01\"/&gt;\n" +
//                "          &lt;/Record&gt;\n" +
//                "        &lt;/Master&gt;\n" +
//                "      &lt;/RecordSet&gt;\n" +
//                "    &lt;/Document&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:GetPalletNoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml_str;
//    }
//
//    private String get_xml_test_6()
//    {
//        String xml_str = "";
//        //  xml = GetSNDataString(url_str, soapXML);
//        xml_str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:UpdPalletNoResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n" +
//                "  &lt;Execution&gt;\n" +
//                "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"\"/&gt;\n" +
//                "  &lt;/Execution&gt;\n" +
//                "  &lt;ResponseContent&gt;\n" +
//                "    &lt;Parameter&gt;\n" +
//                "      &lt;Record&gt;\n" +
//                "        &lt;Field name=\"success\" value=\"Y\"/&gt;\n" +
//                "      &lt;/Record&gt;\n" +
//                "    &lt;/Parameter&gt;\n" +
//                "    &lt;Document/&gt;\n" +
//                "  &lt;/ResponseContent&gt;\n" +
//                "&lt;/Response&gt;</fjs1:response></fjs1:UpdPalletNoResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
//        return xml_str;
//    }



}



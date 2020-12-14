package com.miracom.Client;

import com.miracom.Client.network.Callback;
import com.miracom.Client.network.SoapClient;
import com.miracom.Client.network.SoapRequest;
import com.miracom.Client.tool.SoapEnvelopeUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by LiuShuai on 2017/3/6.
 */

public class SoapUtil {
    private static final String TAG = "SoapUtil";
    private static SoapUtil mInstance;
    private SoapClient mSoapClient;

    public static final String mWeatherEndPoint = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";

    public static final String mNameSpace = "http://WebXml.com.cn/";
    public int mSOAPVersion = SoapEnvelope.VER11;

    private SoapUtil(boolean isdebug) {
        mSoapClient = new SoapClient();
        //设置是否是调试模式
        mSoapClient.setDebug(isdebug);
    }

    public static synchronized SoapUtil getInstance(boolean isdebug) {
        if (mInstance == null) {
            mInstance = new SoapUtil(isdebug);
        }
        return mInstance;
    }


    /**
     * 异步调用
     *
     * @param cityName
     * @param callback
     */
    public void getSupportCity(String cityName, Callback callback) {
        SoapRequest request = new SoapRequest.Builder().endPoint(mWeatherEndPoint)
                .methodName("getSupportCity")
                .soapAction(mNameSpace + "getSupportCity")
                .addParam("byProvinceName", cityName)
                .nameSpace(mNameSpace)
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        mSoapClient.newCall(request).enqueue(callback);
    }

    public SoapEnvelope getTranslateReturnXMLLocalTest( ) {
        SoapRequest request = new SoapRequest.Builder().endPoint("http://172.17.1.100:8080/CXFDemo/translateService?wsdl")
                .methodName("translateReturnXML")
                .soapAction("")
                .addParam("arg0", "1")
                .nameSpace("http://service/")
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        return mSoapClient.newCall(request).execute();
    }
    public SoapEnvelope GetSNData( ) {
        SoapRequest request = new SoapRequest.Builder().endPoint("http://10.10.16.17/web/ws/r/aws_ttsrv2_toptest?WSDL")
                .methodName("GetSNData")
                .soapAction("")
                .addParam("request", "")
                .nameSpace("http://service/")
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        return mSoapClient.newCall(request).execute();
    }
    /**
     * 同步调用
     *
     * @param cityName
     * @return
     */
    public SoapEnvelope getSupportCity(String cityName) {
        SoapRequest request = new SoapRequest.Builder().endPoint(mWeatherEndPoint)
                .methodName("getSupportCity")
                .soapAction(mNameSpace + "getSupportCity")
                .addParam("byProvinceName", cityName)
                .nameSpace(mNameSpace)
                .setVersion(mSOAPVersion)
                .setDotNet(true)
                .build();
        return mSoapClient.newCall(request).execute();
    }

}

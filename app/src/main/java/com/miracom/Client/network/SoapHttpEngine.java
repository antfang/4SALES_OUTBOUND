package com.miracom.Client.network;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.HeaderProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuShuai on 2017/3/6.
 */

public class SoapHttpEngine implements HttpEngine {

    private static final String TAG = "SoapHttpEngine";
    private SoapRequest mSoapRequest;
    private boolean isDebug = false;

    public SoapHttpEngine(SoapRequest soapRequest) {
        mSoapRequest = soapRequest;
    }

    @Override
    public SoapEnvelope doGet() throws Exception {
        return null;
    }

    @Override
    public SoapEnvelope doPost() throws Exception {
        //设置Soap对象
        SoapObject rpc = new SoapObject(mSoapRequest.getNameSpace(), mSoapRequest.getMethodName());
        //设置参数
        if (mSoapRequest.getParamsMap() != null) {
            for (Map.Entry<String, Object> e : mSoapRequest.getParamsMap().entrySet()) {
                rpc.addProperty(e.getKey(), e.getValue());
            }
        }
        //设置Soap消息
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(mSoapRequest.getVersion());
        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = mSoapRequest.isDotNet();

        HttpTransportSE transport = new HttpTransportSE(mSoapRequest.getEndPoint());
        transport.debug = isDebug;
        List<HeaderProperty> plist = new ArrayList<>();
//        plist.add(new HeaderProperty("Accept-Encoding", "gzip,deflate"));
//        plist.add(new HeaderProperty("Content-Length", "1"));
        plist.add(new HeaderProperty("Content-Type", "application/xml;rrcharset;:utf2-8;"));
//        plist.add(new HeaderProperty("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\""));
//        transport.call(mSoapRequest.getSoapAction(), envelope,plist);
        transport.call(mSoapRequest.getSoapAction(), envelope,plist);
//        if (envelope.getResponse() != null) {
//            SoapObject result = (SoapObject) envelope.bodyIn;
//            Log.e(TAG, result.toString());
//            Log.e(TAG, String.valueOf(result.getProperty("GetMp3Result")));
//        }
        return envelope;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}

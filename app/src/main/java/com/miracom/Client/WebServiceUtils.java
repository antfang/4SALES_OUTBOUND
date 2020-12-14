package com.miracom.Client;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author ${M.xang}
 * 创建时间：2018-8-25 上午10:00:37
 * 类描述 ：
 *		WebService工具类
 */
public class WebServiceUtils {

    // 含有3个线程的线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);


    public static String WEB_SERVER_URL = "http://192.168.191.1:8080/WT600WebService/wtwebservice?wsdl";
    // 命名空间
    public static String NAMESPACE = "http://serviceimpl.app.wonder.com/";

    /**
     * @param methodName         WebService的调用方法名
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void callWebService(final String methodName,
                                      HashMap<String, String> properties, final WebServiceCallBack webServiceCallBack) {

        // 创建HttpTransportSE对象，传递WebService服务器地址，设置超时时间为30s
        final HttpTransportSE httpTransportSE = new HttpTransportSE(WEB_SERVER_URL, 30 * 1000);
        // 创建SoapObject对象
        SoapObject soapObject = new SoapObject(NAMESPACE, methodName);
        // SoapObject添加参数，如果properties传null，表示调用无参函数
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                // 往soapObject中保存上传的参数数据
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }

        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapEnvelope.bodyOut = soapObject;
        soapEnvelope.dotNet = true;// 设置是否调用的是.Net开发的WebService,一定要设为false
        soapEnvelope.encodingStyle = "UTF-8";
        httpTransportSE.debug = true;
        soapEnvelope.setOutputSoapObject(soapObject);

        //  Looper.prepare();
        // 用于子线程与主线程通信的Handler
        @SuppressLint("HandlerLeak") final Handler mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        // 将返回值回调到callBack的参数中
                        webServiceCallBack.callBack((SoapObject) msg.obj, "");
                        break;
                    case 1:
                        // 将返回值回调到callBack的参数中
                        webServiceCallBack.callBack(null, (String) msg.obj);
                        break;
                    default:
                        break;
                }
            }

        };
        //  Looper.loop(); //这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
        // 开启线程去访问WebService
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                SoapObject resultSoapObject = null;
                try {
                    httpTransportSE.call("", soapEnvelope);
                    if (soapEnvelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                    }
                    // 将获取的消息利用Handler发送到主线程
                    mHandler.sendMessage(mHandler.obtainMessage(0, resultSoapObject));
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(1, e.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(1, e.toString()));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(1, e.toString()));
                }
            }
        });
    }

    /**
     * 数据返回结果
     */
    public interface WebServiceCallBack {
        public void callBack(SoapObject result, String errorInfo);
    }
}
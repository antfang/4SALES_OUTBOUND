package com.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.sufang.scanner.Constant;
import com.sufang.scanner.R;
import com.sufang.util.PreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

    private Context mContext;
    //提示语
    private String updateMsg = "有新的软件包哦，请快下载吧。";;
    private String serviceName ="122.51.33.156";
    private static final String apkName = "sales_outbound_release.apk";
    //返回的安装包url
    private String apkUrl = "http://"+serviceName+"/"+apkName+"";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/MyFavorite/";
    private static final String saveFileName = savePath + ""+apkName;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int GET_VERSION = 3;
    private static final int OPEN_DAILOG = 4;
    PreferencesUtils preferencesUtils = PreferencesUtils.getInstance();
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                case GET_VERSION:
                    showNoticeDialog();
                    break;
                case OPEN_DAILOG:
                    String m = (String)msg.obj;
                    Builder builder = new Builder(mContext);
                    builder.setTitle("Warm");
                    builder.setMessage(m);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    break;
                default:
                    break;
            }
        };
    };

    public UpdateManager(Context context) {

        this.mContext = context;
        this.serviceName = preferencesUtils.getString(Constant.KEY_UPDATE_SERVICE);
        getAPKUrl();
    }
    private String getAPKUrl()
    {
        this.apkUrl = "http://"+serviceName+"/"+apkName+"";;
        return apkUrl;
    }
    private void openDailog(String msg_str)
    {
        Message msg = mHandler.obtainMessage(OPEN_DAILOG);
        msg.obj = msg_str;
        msg.sendToTarget();
    }
    //外部接口让主Activity调用
    public boolean checkUpdateInfo(){
        URL realUrl;
        try {
            realUrl = new URL(apkUrl);
        } catch (MalformedURLException e) {
            openDailog(mContext.getString(R.string.txt_check_version_fail)+":"+apkUrl);
            return false;
        }
        // 打开和URL之间zhi的连接
        HttpURLConnection connection;
        int code ;
        try {
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            code = connection.getResponseCode() ;
        } catch (IOException e) {
            openDailog(mContext.getString(R.string.txt_check_version_fail)+":"+apkUrl+"  "+e.getMessage());
            return false ;
        }
        if(code == 200){
            mHandler.sendEmptyMessage(GET_VERSION);
            return true;
        }else{
            openDailog(mContext.getString(R.string.txt_check_version_fail)+":"+apkUrl+"   "+code);
            return false;
        }
    }


    private void showNoticeDialog(){
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog(){
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if(!file.exists()){
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do{
                    int numread = is.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                }while(!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk(){
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    /**
     * 安装apk
     */
    private void installApk(){
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }
}
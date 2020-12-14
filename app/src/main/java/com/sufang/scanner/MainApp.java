package com.sufang.scanner;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.hjq.toast.ToastUtils;
import com.sufang.util.LogUtil;
import com.sufang.util.MySQLiteOpenHelper;

import org.greenrobot.greendao.database.Database;

/**
 */
public class MainApp extends Application {

    private static String TAG = "scanner";
    public static Context appContext;
    public static boolean isDebugMode = true;
    public static volatile boolean haveInit = false;
    private MainApp _instance;
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    private void initDebugMode() {
        int appFlags = getApplicationInfo().flags;
        if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            isDebugMode = true;
        } else {
            isDebugMode = false;
        }
    }

    @Override
    public void onCreate() {
        initDebugMode();
        super.onCreate();

        appContext = getApplicationContext();
        LogUtil.d(TAG, "get curprocess name = " + getCurProcessName(appContext) + ", packageName = " + getPackageName());
        if (!haveInit && _instance == null
                && getCurProcessName(appContext).equals(getPackageName())) {
            _instance = (MainApp) getApplicationContext();
            LogUtil.d(TAG, "main app init...");
            haveInit = true;
            ToastUtils.init(this);
            initGreenDao();
        }
    }


    /**
     * 初始化greenDao
     */
    private void initGreenDao(){
        MigrationHelper.DEBUG = true;
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this,
                ENCRYPTED ? "secretary-db-encrypted" : "secretary-db", null);
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb(
                "super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }


    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess
                : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

}

package com.sufang.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.sufang.scanner.MainApp;

public class PreferencesUtils {

    private static volatile PreferencesUtils preferenceUtil;
    private volatile SharedPreferences getPreferences;

    private PreferencesUtils(Context context) {
        if (this.getPreferences == null) {
            synchronized (PreferencesUtils.this) {
                this.getPreferences = context.getSharedPreferences(getPackageName(context),
                        Activity.MODE_PRIVATE);
            }
        }
    }

    /**
     * 获得应用版本名
     */
    public static String getPackageName(final Context ctx) {
        try {
            return ctx.getPackageName();
        } catch (Exception e) {
            return "com.sufang.scanner.Sales_Outbound";
        }
    }

    public synchronized static PreferencesUtils getInstance() {
        if (preferenceUtil == null) {
            preferenceUtil = new PreferencesUtils(MainApp.appContext);
        }

        return preferenceUtil;
    }


    public String getString(String key) {
        try {
            return preferenceUtil.getPreferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getInt(String key) {
        try {
            return preferenceUtil.getPreferences.getInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return preferenceUtil.getPreferences.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getStringToInt(String key, int defaultValue) {
        try {
            String val = preferenceUtil.getPreferences.getString(key, defaultValue + "");
            return Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public void putInt(String key, int value) {
        try {
            preferenceUtil.getPreferences.edit().putInt(key, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putLong(String key, long value) {
        try {
            preferenceUtil.getPreferences.edit().putLong(key, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key) {
        try {
            return preferenceUtil.getPreferences.getLong(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void putString(String key, String value) {
        try {
            preferenceUtil.getPreferences.edit().putString(key, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitString(String key, String value) {
        try {
            preferenceUtil.getPreferences.edit().putString(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            preferenceUtil.getPreferences.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key) {
        try {
            return preferenceUtil.getPreferences.getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void commitBoolean(String key, boolean value) {
        try {
            preferenceUtil.getPreferences.edit().putBoolean(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putBoolean(String key, boolean value) {
        try {
            preferenceUtil.getPreferences.edit().putBoolean(key, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

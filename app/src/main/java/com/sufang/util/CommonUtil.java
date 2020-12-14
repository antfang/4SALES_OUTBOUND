package com.sufang.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 检查传入的str是否是null，"","null"
     */
    public static boolean isNull(String str) {
        return (str == null || "".equals(str.trim())
                || "null".equalsIgnoreCase(str.trim()));
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean isNull(List list) {
        return (list == null || list.size() == 0);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 检查应用程序是否安装
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * url网址验证
     *
     * @return 返回true：验证通过，false：错误Url
     */
    public static boolean IsUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    public static boolean IsImageUrl(String str) {
        return (str.startsWith("http://") || str.startsWith("https://"));
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 手机号验证
     *
     * @return 返回true：验证通过，false：错误手机
     */
    public static boolean isMobile(String str) {

        Pattern p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isEmpty(List data) {
        return data == null || data.size() == 0;
    }

    public static boolean isNotEmpty(List data) {
        return !isEmpty(data);
    }

    /**
     * 电话号码验证
     *
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {

        Matcher m;
        boolean b;
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static String formatString(Object strValue) {
        String ret;
        if (strValue != null && !"null".equalsIgnoreCase(String.valueOf(strValue))) {
            ret = (String) strValue;
        } else {
            ret = "";
        }
        return ret;
    }

    public static String formatString(Object strValue, String def) {
        String ret = formatString(strValue);
        if (CommonUtil.isNull(ret)) {
            return def;
        }
        return ret;
    }

    /**
     * 处理Integer为null报错的问题
     */
    public static int FormatInteger(Integer intValue) {
        int ret = 0;
        if (intValue != null)
            ret = intValue.intValue();
        return ret;
    }

    /**
     * 处理Double为null报错的问题
     */
    public static double FormatDouble(Double dValue) {
        double ret = 0.0;
        if (dValue != null)
            ret = dValue.doubleValue();
        return ret;
    }

    /**
     * 转换排名
     */
    public static String formatMingci(Integer mingci) {
        if (mingci == null) {
            return "名次未知";
        } else if (mingci > 1000) {
            return "1000+";
        } else {
            return "第" + String.valueOf(mingci) + "名";
        }
    }

    /**
     * 转换排名数字类
     */
    public static String formatMingciNum(Integer mingci) {
        if (mingci == null || mingci == 0) {
            return "暂无";
        } else if (mingci > 1000) {
            return "1000+";
        } else {
            return String.valueOf(mingci);
        }
    }

    /**
     * 判断str有多少个英文字符数
     * @param str
     * @return
     */
    public static int getStrCnt(String str){
        int cnt = 0;
        if(str != null){
            char[] chars = str.toCharArray();
            for(char c : chars){
                if(isChinese(c)){
                    cnt += 2;
                } else {
                    cnt += 1;
                }
            }
        }
        return cnt;
    }
    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 赋值时处理空值
     *
     * @param tv        --TextView控件
     * @param content   --填入内容，将进行非空判断
     * @param strReturn --填入内容为空时你想显示的内容
     */
    public static void setText(TextView tv, String content, String strReturn) {
        try {
            if (isNull(strReturn))
                strReturn = "";
            if (!isNull(content))
                strReturn = content;
            tv.setText(strReturn);
        } catch (Exception e) {
            e.printStackTrace();
            tv.setText(strReturn);
        }
    }

    public static void setText(TextView tv, String content) {
        setText(tv, content, "未知");
    }

    /**
     * 转换InputStream 成 string
     *
     * @return String
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 转换距离
     */
    public static String formatDistance(Integer distance) {
        if (distance == null) {
            return "";
        } else if (distance > 10000) {
            return ">10km";
        } else if (distance > 1000) {
            return String.format("%.2fkm", Float.valueOf(distance) / 1000);
        } else {
            return String.format("%dm", distance);
        }
    }

    /**
     * 半角转换为全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 手机号码隐藏中间4位数
     *
     * @param phone 手机号码
     * @param def   当传入空值时，需要返回的值
     */
    public static String formatPhone(String phone, String def) {
        if (isNull(phone)) {
            if (isNull(def)) {
                return "";
            } else {
                return def;
            }
        } else if (phone.length() != 11) {
            return phone;
        } else {
            return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        }
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":").replace("，", ",")
                .replace("。", ".");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 用小括号包起来
     */
    public static String stringWrappedByParentheses(String str) {
        if (isNull(str))
            return "";

        return "(" + str + ")";
    }


    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getCityCode(String cityCode) {
        if (cityCode.startsWith("11") || cityCode.startsWith("12") || cityCode.startsWith("31") || cityCode.startsWith("50")) {
            return cityCode.substring(0, 2) + "0000";
        } else {
            return cityCode.substring(0, 4) + "00";
        }
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context 视图对象
     * @return 手机状态栏(电量栏)高度
     */
    public static int getStatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取指定长度的随机数
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
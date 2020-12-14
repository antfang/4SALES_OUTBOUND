package com.sufang.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class DateUtils implements Serializable {

    private static final long serialVersionUID = -3098985139095632110L;
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String FORMAT_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String FORMAT_CN_M_D = "M月d日";
    public static final String FORMAT_MM_SS = "HH:mm";
    public static final String dateFormat = "yyyy-MM-dd";
    public static final String dateFormatYMD = "yyyy年MM月dd日";
    public static final String timeFormat = "HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_CN_MM_DD = "MM月dd日";
    public static final String FORMAT_MM_DD = "MM-dd";
    public static final String FORMAT_YYYYMMDD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    /**
     * 计算发布时间和当前时间的差
     * 刚刚
     * 30秒前
     * 1分钟前
     * 1小时前
     */
    public static String formatTimeStr(long target) {
        //时间差转换成秒
        int wtime = (int) ((System.currentTimeMillis() - target) / 1000);
        if (wtime <= 10) {
            return "刚刚";
        } else if (wtime < 60) {
            return wtime + "秒前";
        } else if (wtime < 60 * 60) {
            return wtime / 60 + "分钟前";
        } else if (wtime < 60 * 60 * 24) {
            return wtime / 60 / 60 + "小时前";
        } else if (wtime <= 60 * 60 * 24 * 3) {
            return wtime / 60 / 60 / 24 + "天前";
        }
        return formatDate(target, "MM-dd HH:mm");
    }

    /**
     * 自定义格式
     */
    public static String formatDate(String date, String format) {
        return formatDate(getDate(date), format);
    }

    /**
     * 自定义格式
     * */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dFormat = new SimpleDateFormat(format, Locale.CHINESE);
        return dFormat.format(date);
    }

    public static String formatDate(long date, String format) {
        return formatDate(new Date(date), format);
    }

    public static Date getDate(String date, String format) {
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat(format, Locale.CHINESE);
            return dFormat.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * 返回当前时间
     * @return String 当前时间，格式MM月dd日 HH:mm
     */
    public static String getDateString() {
        String label = formatDate(new Date(), "MM月dd日 HH:mm");
        return label;
    }

    /**
     * 格式化date转string， "yyyy-MM-dd HH:mm:ss"
     * */
    public static String formatDatetime(Date date) {
        return formatDate(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化date转string， "yyyy-MM-dd HH:mm:ss"
     * */
    public static String formatDatetime1(Date date) {
        return formatDate(date, FORMAT_YYYYMMDD_HH_MM_SS);
    }

    /**
     * 返回当前日期时分秒 "yyyy-MM-dd HH:mm:ss"
     * @return 格式"yyyy-MM-dd HH:mm:ss"
     */
    public static String currentDatetime() {
        return formatDatetime(new Date());
    }

    /**
     * 返回当前日期时分秒 "yyyy/MM/dd HH:mm:ss"
     * @return 格式"yyyy/MM/dd HH:mm:ss"
     */
    public static String currentDatetime1() {
        return formatDatetime1(new Date());
    }

    /**
     * 返回当前日期时分秒 "20181219114938"
     * @return 格式"yyyyMMddHHmmss"
     */
    public static String currentDatetimeEx() {
        return formatDate(new Date(), FORMAT_YYYYMMDDHHMMSS);
    }

    /**
     * long转date，格式 "yyyy-MM-dd HH:mm:ss"
     * @return 格式"yyyy-MM-dd HH:mm:ss"
     */
    public static String dateTimeFormat(long dateTime) {
        return formatDatetime(new Date(dateTime));
    }

    /**
     * 格式"yyyy-MM-dd HH:mm:ss"
     * @return 格式"yyyy-MM-dd HH:mm:ss"
     */
    public static Date getDatetime(String datetime) {
        return getDate(datetime, FORMAT_YYYY_MM_DD_HH_MM_SS);
    }
    //end



    /**
     * 格式"yyyy-MM-dd"
     * */
    public static String formatDate(Date date) {
        return formatDate(date, dateFormat);
    }
    public static String currentDate() {
        return formatDate(new Date());
    }

    public static String currentDate(String dateFormat) {
        return formatDate(new Date(), dateFormat);
    }
    public static String dateFormat(long date) {
        return formatDate(new Date(date));
    }
    public static Date getDate(String datetime) {
        return getDate(datetime, dateFormat);
    }
    //end



    /**
     * 格式"HH:mm:ss"
     * */
    public static String formatTime(Date date) {
        return formatDate(date, timeFormat);
    }
    public static String currentTime() {
        return formatTime(new Date());
    }
    public static Date getTime(String datetime) {
        return getDate(datetime, timeFormat);
    }
    //end


    /**
     * 格式yyyyMMdd 转成 "yyyy-MM-dd"
     * */
    public static String formatDate(String date) {
        if (StringUtil.isEmpty(date)) {
            return "";
        }
        if (date.length()==8) {
            return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6);
        }
        return date;
    }



    public static Date getPreviousMonthEnd(Date date) {

        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        return lastDate.getTime();
    }

    public static int getLastDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, getLastDaysOfMonth(date));
        return calendar.getTime();
    }

    public static Date getNextMonthEnd(Date date) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        return lastDate.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {

        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        return lastDate.getTime();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    public static boolean between(Date beginDate, Date endDate, Date src) {
        Calendar dateTime = Calendar.getInstance();
        Calendar tempDate = Calendar.getInstance();
        Calendar tempDate2 = Calendar.getInstance();
        dateTime.setTime(beginDate);
        tempDate.setTime(endDate);
        tempDate2.setTime(src);
        dateTime.set(Calendar.SECOND, 0);
        tempDate.set(Calendar.SECOND, 0);
        tempDate2.set(Calendar.SECOND, 0);
        dateTime.set(Calendar.MILLISECOND, 0);
        tempDate.set(Calendar.MILLISECOND, 0);
        tempDate2.set(Calendar.MILLISECOND, 0);
        return (tempDate2.getTime().getTime() - dateTime.getTime().getTime() >= 0) && (tempDate.getTime().getTime() - tempDate2.getTime().getTime() >= 0);
    }

    public static String getCurrentYear() {
        return getCurrentTime("yyyy");
    }

    public static String getCurrentMonth() {
        return getCurrentTime("MM");
    }

    public static String getCurrentDay() {
        return getCurrentTime("dd");
    }
    
    public static String getCurrentTime(String format) {
        return getFormatDateTime(new Date(), format);
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }



    public static String getChinaDayOfWeek(Date date) {
        String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }

    public static String getChinaOfWeek(Date date) {
        String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }

    public static String getChinaOfWeek(String date) {
        String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int week = getDayOfWeek(getDate(date));
        return weeks[week - 1];
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 当月最后一天
     */
    public static Date getLastDay() {
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /**
     * 当月第一天
     */
    public static Date getFirstDay() {
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    public static Date lastDayOfPreMonth() {
        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        return cale.getTime();
    }

    public static Date firstDayOfPreMonth() {
        //获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return cal_1.getTime();
    }

    public static Date getApartMonth(Date date, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }


    /**
     * 两个日期相差天数
     * */
    public static int daysOfTwo(Date fDate, Date oDate) {

        fDate = getDate(formatDate(fDate));//去除时分秒
        oDate = getDate(formatDate(oDate));

        Calendar cal = Calendar.getInstance();
        cal.setTime(fDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(oDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }
    //指定日期与今天的差
    public static int daysOfTwo(String tDate, String format) {
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat(format, Locale.CHINESE);
            return daysOfTwo(new Date(), dFormat.parse(tDate));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 和今天相比的天数差描述，大于7天小于0返回本日期的格式化
     * */
    public static String daysBetween(Date tDate) {
        int plus = daysOfTwo(new Date(), tDate);
        if (plus == 0) {
            return "今天";
        }
        else if (plus > 0 && plus < 7) {
            return plus+"天后";
        }
        else if (plus == 7) {
            return "一周后";
        }
        else {
            return formatDate(tDate);
        }
    }
    public static String daysBetween(String tDate, String desc) {
        return daysBetween(getDate(tDate, desc));
    }

    public static int getMonthSpace(Date date1, Date date2) {
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);
        c.setTime(date2);
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);
        int result;
        if (year1 == year2) {
            result = month1 - month2;
        } else {
            result = 12 * (year1 - year2) + month1 - month2;
        }
        return result;
    }

    public static boolean isBeforeIgnoreSeconds(Date beforeDate, Date afterDate) {
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar afterCalendar = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        afterCalendar.setTime(afterDate);
        beforeCalendar.set(Calendar.SECOND, 0);
        beforeCalendar.set(Calendar.MILLISECOND, 0);
        afterCalendar.set(Calendar.SECOND, 0);
        afterCalendar.set(Calendar.MILLISECOND, 0);
        return afterCalendar.getTime().getTime() > beforeCalendar.getTime().getTime();
    }

    public static boolean isBeforeIgnoreDayAndSeconds(Date beforeDate, Date afterDate) {
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar afterCalendar = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        afterCalendar.setTime(afterDate);
        beforeCalendar.set(Calendar.YEAR, afterCalendar.get(Calendar.YEAR));
        beforeCalendar.set(Calendar.MONTH, afterCalendar.get(Calendar.MONTH));
        beforeCalendar.set(Calendar.DAY_OF_MONTH, afterCalendar.get(Calendar.DAY_OF_MONTH));
        beforeCalendar.set(Calendar.SECOND, 0);
        beforeCalendar.set(Calendar.MILLISECOND, 0);
        afterCalendar.set(Calendar.SECOND, 0);
        afterCalendar.set(Calendar.MILLISECOND, 0);
        return afterCalendar.getTime().getTime() > beforeCalendar.getTime().getTime();
    }

    public static long beforeIgnoreDayAndSeconds(Date beforeDate, Date afterDate) {
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar afterCalendar = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        afterCalendar.setTime(afterDate);
        beforeCalendar.set(Calendar.YEAR, afterCalendar.get(Calendar.YEAR));
        beforeCalendar.set(Calendar.MONTH, afterCalendar.get(Calendar.MONTH));
        beforeCalendar.set(Calendar.DAY_OF_MONTH, afterCalendar.get(Calendar.DAY_OF_MONTH));
        beforeCalendar.set(Calendar.SECOND, 0);
        beforeCalendar.set(Calendar.MILLISECOND, 0);
        afterCalendar.set(Calendar.SECOND, 0);
        afterCalendar.set(Calendar.MILLISECOND, 0);
        return afterCalendar.getTime().getTime() - beforeCalendar.getTime().getTime();
    }

    public static long getBetweenIgnoreSeconds(Date beforeDate, Date afterDate) {
        Calendar beforeCalendar = Calendar.getInstance();
        Calendar afterCalendar = Calendar.getInstance();
        beforeCalendar.setTime(beforeDate);
        afterCalendar.setTime(afterDate);
        beforeCalendar.set(Calendar.SECOND, 0);
        beforeCalendar.set(Calendar.MILLISECOND, 0);
        afterCalendar.set(Calendar.SECOND, 0);
        afterCalendar.set(Calendar.MILLISECOND, 0);
        return afterCalendar.getTime().getTime() - beforeCalendar.getTime().getTime();
    }

    /**
     * Android 音乐播放器应用里，读出的音乐时长为 long 类型以毫秒数为单位，例如：将 234736 转化为分钟和秒应为 03:55 （包含四舍五入）
     * @param duration 音乐时长
     * @return
     */
    public static String formatMusicTime(long duration) {
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;
        if( second < 10 ){
            time += "0" ;
        }
        time += second ;
        return time ;
    }

    /**
     * 将 234736 转化为分钟和秒应为 03分55秒 （包含四舍五入）
     * @param duration 时长
     * @return
     */
    public static String formatMusicTimeEx(long duration) {
        if (duration == 0) {
            return "0秒";
        }
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 && minute > 0){
            time += "0" ;
        }
        if (minute > 0) {
            time += minute+"分" ;
        }
        if( second < 10 && second > 0){
            time += "0" ;
        }
        if (second > 0) {
            time += second + "秒" ;
        }
        return time ;
    }

    /**
     * 给指定日期增加指定的天数
     *
     * @param strCurrDate 日期
     * @param format      日期格式
     * @param iDay        要增加的天数，做减法就传负数
     * @return eg.2008-10
     */
    public static String addDay(String strCurrDate, String format, int iDay) {
        if (iDay == 0 || strCurrDate == null || strCurrDate.equals("")) {
            return strCurrDate;
        } else {
            if (format == null) {
                format = FORMAT_YYYY_MM_DD;
            }
            SimpleDateFormat objStdFormat = new SimpleDateFormat(format);
            try {
                Date dCurrDate = objStdFormat.parse(strCurrDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dCurrDate);
                calendar.add(Calendar.DATE, iDay);
                return objStdFormat.format(calendar.getTime());
            } catch (Exception e) {
                return strCurrDate;
            }
        }
    }

    /**
     * 给指定日期增加指定的小时
     *
     * @param date 日期
     * @param hour 要增加的小时，做减法就传负数
     * @return eg.2008-10
     */
    public static Date addHour(Date date, int hour) {
        if (hour == 0 || date == null) {
            return date;
        } else {

            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, hour);
                return calendar.getTime();
            } catch (Exception e) {
                return date;
            }
        }
    }

    /**
     * 转换时间格式
     * yyyyMMdd --> yyyy-MM-dd
     */
    public static String setDateMask(String strCurrDate) {
        if (StringUtil.isEmpty(strCurrDate)) {
            return "";
        } else if (strCurrDate.length() == 8) {
            return strCurrDate.substring(0, 4) + "-" + strCurrDate.substring(4, 6)
                    + "-" + strCurrDate.substring(6, 8);
        } else {
            return strCurrDate;
        }
    }

    /**
     * 转换时间格式
     * yyyy-MM-dd --> yyyyMMdd
     */
    public static String setDateStr(String strCurrDate) {
        if (StringUtil.isEmpty(strCurrDate)) {
            return "";
        } else if (strCurrDate.length() == 10) {
            return strCurrDate.substring(0, 4) + strCurrDate.substring(5, 7)
                    + strCurrDate.substring(8, 10);
        } else {
            return strCurrDate;
        }
    }

}

package com.carlt.doride.utils;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by liu on 2017/10/20 0020.
 * 显示时间的工具类
 */

public class MyTimeUtils {

    public static final SimpleDateFormat FORMAT        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat FORMAT_DAY    = new SimpleDateFormat("yyyy-MM-dd");
    public static final String           DEFULE_STRING = StringUtils.EMPTY;

    /**
     * View 显示时间
     * @param input
     * @return
     */
    public static void viewPutDateSecend(TextView v, long input) {
        if (null != v) {
            if (input != 0) {
                v.setText(formatDateSecend(input));
            }
        }
    }

    /**
     * 获取系统当前日期 格式：XXXX-0X-0X
     * @return
     */
    public static String getDateFormat3() {
        Calendar mCalendar = Calendar.getInstance();
        return FORMAT_DAY.format(mCalendar.getTime());
    }

    /**
     * 秒数转为 格式化时间，精确到秒
     * @param input
     * @return
     */
    public static String formatDateSecend(long input) {
        if (input == 0) {
            return DEFULE_STRING;
        }
        long times = input * 1000;
        return formatDateMills(times);
    }

    /**
     * 秒数转为 格式化时间 精确到天
     * @param input
     * @return
     */
    public synchronized static String formatDateGetDaySecend(long input) {
        if (input == 0) {
            return DEFULE_STRING;
        }
        long times = input * 1000;
        Date date2 = new Date();
        date2.setTime(times);
        return FORMAT_DAY.format(date2);
    }
    public synchronized static String formatDateGetDaySecend1(long input) {
        if (input == 0) {
            return DEFULE_STRING;
        }
        long times = input * 1000;
        Date date2 = new Date();
        date2.setTime(times);
        return FORMAT.format(date2);
    }
    /**
     * 毫秒数数转为 格式化时间
     * @param times
     * @return
     */
    public static String formatDateMills(long times) {
        if (times == 0) {
            return DEFULE_STRING;
        }
        Date date2 = new Date();
        date2.setTime(times);
        return getFormatTime(date2);
    }


    /**
     * 日期转换
     * @param date
     * @return
     */
    public static String getFormatTime(Date date) {
        return FORMAT.format(date);
    }

    /**
     * 获取系统当前日期 格式：XXXX年XX月
     * @return
     */
    public static String getDateFormat6() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        month++;
        StringBuffer mStringBuffer = new StringBuffer();
        mStringBuffer.append(year);
        mStringBuffer.append("年");
        if (month < 10) {
            mStringBuffer.append("0");
        }
        mStringBuffer.append(month);
        mStringBuffer.append("月");

        return mStringBuffer.toString();
    }

}

package com.carlt.sesame.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyTimeUtil {

	public static SimpleDateFormat commonFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
	public static SimpleDateFormat commonFormat_m = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.CHINESE);
	public static SimpleDateFormat DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINESE);
	public static SimpleDateFormat format_hhmm = new SimpleDateFormat("HH:mm",
			Locale.CHINESE);
	public static final SimpleDateFormat FORMAT3 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	public static final String ENDTIME = "2037-01-01 00:00:00";
	/**
	 * 获取系统当前日期 格式：XX时xx分
	 *
	 * @return
	 */
	public static String getHM(Date date) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime(date);
		mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = mCalendar.get(Calendar.MINUTE);
		StringBuffer mStringBuffer = new StringBuffer();
		mStringBuffer.append(hour);
		mStringBuffer.append("时");
		mStringBuffer.append(minute);
		mStringBuffer.append("分");

		return mStringBuffer.toString();
	}
	/**
	 * 获取系统当前日期 格式：XXXX年XX月XX日
	 *
	 * @return
	 */
	public static String getDateYMD(Date date) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime(date);
		mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		month++;
		StringBuffer mStringBuffer = new StringBuffer();
		mStringBuffer.append(year);
		mStringBuffer.append("年");
		mStringBuffer.append(month);
		mStringBuffer.append("月");
		mStringBuffer.append(day);
		mStringBuffer.append("日");

		return mStringBuffer.toString();
	}
	/**
	 * 获取系统当前日期 格式：XXXX-XX-XX
	 * 
	 * @return
	 */
	public static String getDateFormat1() {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		month++;
		StringBuffer mStringBuffer = new StringBuffer();
		mStringBuffer.append(year);
		mStringBuffer.append("-");
		mStringBuffer.append(month);
		mStringBuffer.append("-");
		mStringBuffer.append(day);

		return mStringBuffer.toString();
	}

	/**
	 * 获取系统当前日期 格式：XXXX年XX月XX日
	 * 
	 * @return
	 */
	public static String getDateFormat2() {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		month++;
		StringBuffer mStringBuffer = new StringBuffer();
		mStringBuffer.append(year);
		mStringBuffer.append("年");
		mStringBuffer.append(month);
		mStringBuffer.append("月");
		mStringBuffer.append(day);
		mStringBuffer.append("日");

		return mStringBuffer.toString();
	}

	/**
	 * 获取系统当前日期 格式：XXXX-0X-0X
	 * 
	 * @return
	 */
	public static String getDateFormat3() {
		Calendar mCalendar = Calendar.getInstance();
		return DateFormat.format(mCalendar.getTime());
	}

	public static String getDateFormat4() {
		Calendar mCalendar = Calendar.getInstance();
		return commonFormat_m.format(mCalendar.getTime());
	}

	public static String getDateFormat5() {
		Calendar mCalendar = Calendar.getInstance();
		return format_hhmm.format(mCalendar.getTime());
	}
	
	public static String getDateFormat5(long time) {
		time=time*1000;
		Calendar mCalendar = Calendar.getInstance();
		Date date=new Date(time);
		mCalendar.setTime(date);
		return format_hhmm.format(mCalendar.getTime());
	}

	public static String getTime(String time) {
		try {
			Date date = commonFormat.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int hour = c.get(Calendar.HOUR);
			int minute = c.get(Calendar.MINUTE);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(hour);
			stringBuffer.append(":");
			stringBuffer.append(minute);

			return stringBuffer.toString();
		} catch (ParseException e) {
		}
		return time;
	}

	/**
	 * 获取当前日期属于第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfMonth(String date) {
		int number = 0;
		Calendar mCalendar = Calendar.getInstance();
		String firstdayOfWeek = getFirstdayOfWeek(date);

		if (firstdayOfWeek != null && date.length() > 0) {
			String[] strings = firstdayOfWeek.split("-");
			if (strings != null && strings.length > 2) {
				int year = MyParse.parseInt(strings[0]);
				int month = MyParse.parseInt(strings[1]);
				int day = MyParse.parseInt(strings[2]);
				mCalendar.set(year, month - 1, day);

				Calendar mCalendarFirstdayOfMonth = Calendar.getInstance();
				mCalendarFirstdayOfMonth.set(year, month - 1, 1);
				int i = mCalendarFirstdayOfMonth.get(Calendar.DAY_OF_WEEK);
				if (i == 1) {
					// 本月的第一天为周天
					number = mCalendar.get(Calendar.WEEK_OF_MONTH);
				} else {
					// 本月的第一天不为周天
					number = mCalendar.get(Calendar.WEEK_OF_MONTH) - 1;
				}

				// if (day > 1) {
				// number = mCalendar.get(Calendar.WEEK_OF_MONTH) - 1;
				// } else {
				// number = mCalendar.get(Calendar.WEEK_OF_MONTH);
				// }
				return number;
			}
		} else {
			return number;
		}

		return number;
	}

	/**
	 * 获取当前日期所属周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstdayOfWeek(String date) {
		String firstdayOfWeek = null;
		Calendar mCalendar = Calendar.getInstance();

		if (date == null || date.length() <= 0) {
			return firstdayOfWeek;
		} else {
			String[] strings = date.split("-");
			if (strings != null && strings.length > 2) {
				int year = MyParse.parseInt(strings[0]);
				int month = MyParse.parseInt(strings[1]);
				int day = MyParse.parseInt(strings[2]);

				mCalendar.set(year, month - 1, day);
				// 当前日期是周的第几天（周日为第一天）
				int number = mCalendar.get(Calendar.DAY_OF_WEEK);
				if (number == 1) {
					firstdayOfWeek = date;
					return firstdayOfWeek;
				}
				int offset = number - 1;
				mCalendar.add(Calendar.DATE, -offset);

				year = mCalendar.get(Calendar.YEAR);
				month = mCalendar.get(Calendar.MONTH) + 1;
				day = mCalendar.get(Calendar.DAY_OF_MONTH);

				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(year);
				stringBuffer.append("-");
				stringBuffer.append(month);
				stringBuffer.append("-");
				stringBuffer.append(day);

				firstdayOfWeek = stringBuffer.toString();
				return firstdayOfWeek;
			}
		}

		return firstdayOfWeek;
	}

	/**
	 * 获取当前日期所属周的最后一天的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDayOfWeek(String date) {
		String sundayOfWeek = null;
		Calendar mCalendar = Calendar.getInstance();

		if (date == null || date.length() <= 0) {
			return sundayOfWeek;
		} else {
			String[] strings = date.split("-");
			if (strings != null && strings.length > 2) {
				int year = MyParse.parseInt(strings[0]);
				int month = MyParse.parseInt(strings[1]);
				int day = MyParse.parseInt(strings[2]);

				mCalendar.set(year, month - 1, day);
				// 当前日期是周的第几天（系统返回的周日为第一天）
				int number = mCalendar.get(Calendar.DAY_OF_WEEK);
				int offset = 7 - number;
				mCalendar.add(Calendar.DATE, offset);

				year = mCalendar.get(Calendar.YEAR);
				month = mCalendar.get(Calendar.MONTH) + 1;
				day = mCalendar.get(Calendar.DAY_OF_MONTH);

				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(year);
				stringBuffer.append("-");
				stringBuffer.append(month);
				stringBuffer.append("-");
				stringBuffer.append(day);

				sundayOfWeek = stringBuffer.toString();
				return sundayOfWeek;
			}
		}
		return sundayOfWeek;
	}

	/**
	 * 获取两个日期之间的天数差
	 * 
	 * @param date1
	 *            最近的日期
	 * @param date2
	 * @return
	 */
	public static int getDateDays(Date date1, Date date2) {
		return (int) (date1.getTime() - date2.getTime())
				/ (1000 * 60 * 60 * 24);
	}

	/**
	 * 判断给到的时间是否是今天
	 * 
	 * @param day
	 * @return
	 */
	public static boolean getIsToday(long day) {
		boolean isToday;
		Log.e("info", "day==" + day);
		day = day * 1000;
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		date.setTime(day);
		Log.e("info", "date==" + date);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);
			if (diffDay == 0) {
				Log.e("info", "diffDay==" + diffDay);
				isToday=true;
			} else {
				isToday=false;
			}
		} else {
			isToday=false;
		}
		
		return isToday;
	}

	/**
	 * 判断给到的时间是否是明天
	 * 
	 * @param day
	 * @return
	 */
	public static boolean getIsTomorrow(long day) {
		boolean isTomorrow;
		day = day * 1000;
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);
		Calendar cal = Calendar.getInstance();
		Date date = new Date(day);
		cal.setTime(date);
		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR)
					- pre.get(Calendar.DAY_OF_YEAR);

			if (diffDay == 1) {
				isTomorrow= true;
			}else{
				isTomorrow=false;
			}
		}else{
			isTomorrow=false;
		}
		return isTomorrow;
	}

	/**
	 * 获取明天日期
	 * 
	 * @param
	 * @return
	 */
	public static String getTomorrowDate() {
		long time = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
		Calendar mCalendar = Calendar.getInstance();
		Date predate = new Date(time);
		mCalendar.setTime(predate);
		return DateFormat.format(mCalendar.getTime());
	}


	/**
	 * 回放开始时间 传入日期转换
	 *
	 * @param date
	 * @return
	 */
	public static String getFormatTime(Date date) {
		return commonFormat.format(date);
	}

	/**
	 * 根据协议解析时间
	 stream_year				8		全局时间年，1表示2001年，后面类推，不得出现0
	 stream_month			4		全局时间月，不得出现0
	 stream_date				5		全局时间日，不得出现0
	 stream_hour				5		全局时间时，24小时制
	 stream_minute			6		全局时间分
	 stream_second			6		全局时间秒
	 * @param buf
	 * @return
	 */
	public static String paserFramData(byte[] buf){

		int year = buf[0] + 2000;

		int mouth = (byte) ((buf[1] & 0xf0) >> 4);

		int day = (byte) ((byte) ((buf[1] & 0x0f) * 2) + (byte) ((buf[2] & 0x80) >> 7));

		int hours = (byte) ((buf[2] & 0x7c) >> 2);

		int min = (byte) ((byte) ((buf[2] & 0x03) * 16) + (byte) ((buf[3] & 0xf0) >> 4));

		int sece = (byte) ((byte) ((buf[3] & 0x0f) * 4) + (byte) ((buf[4] & 0xc0) >> 6));

		StringBuilder sb = new StringBuilder();
		sb.append(year).append("-").append(formartData1(mouth)).append("-").append(formartData1(day)).append(" ").append(formartData1(hours)).append(":").append(formartData1(min)).append(":").append(formartData1(sece));
		Date parseDate = parseDate(sb.toString());
		long times = parseDate.getTime() + 8 * 60 * 60 *1000;
//		long times = parseDate.getTime();
		String string = commonFormat.format(new Date(times));
		return string;
	}
	private static SimpleDateFormat formatterMMSS = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。
	public static String formartTime1(long i){

		String hms = formatterMMSS.format(i);
		return hms;
	}
	private static String formartData1(int i){

		StringBuilder sb2 = new StringBuilder();
		if(i<10){
			sb2.append("0").append(i);
		}else{
			sb2.append(i);
		}
		return sb2.toString();
	}
	public static Date parseDate(String date){
		try {
			return commonFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static long paserFramData2(byte[] buf){

		int year = buf[0] + 2000;

		int mouth = (byte) ((buf[1] & 0xf0) >> 4);

		int day = (byte) ((byte) ((buf[1] & 0x0f) * 2) + (byte) ((buf[2] & 0x80) >> 7));

		int hours = (byte) ((buf[2] & 0x7c) >> 2);

		int min = (byte) ((byte) ((buf[2] & 0x03) * 16) + (byte) ((buf[3] & 0xf0) >> 4));

		int sece = (byte) ((byte) ((buf[3] & 0x0f) * 4) + (byte) ((buf[4] & 0xc0) >> 6));

		StringBuilder sb = new StringBuilder();
		sb.append(year).append("-").append(formartData1(mouth)).append("-").append(formartData1(day)).append(" ").append(formartData1(hours)).append(":").append(formartData1(min)).append(":").append(formartData1(sece));
		Date parseDate = parseDate(sb.toString());
		long times = parseDate.getTime() + 8 * 60 * 60 *1000;
//		long times = parseDate.getTime();
		return times;
	}
	/**
	 * 获取回放结束时间，当前时间加一年
	 *
	 * @return
	 */
	public static String getPlayBackEndTime() {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.add(Calendar.YEAR, 1);
		return commonFormat.format(mCalendar.getTime());
	}
	//返回期（HH:mm:ss）
	public static String parseDate3(String date){
		try {
			return FORMAT3.format(commonFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	//返回期（yyyyMMdd）
	public static String parseDate2(String date){
		try {
			return DateFormat.format(commonFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *
	 * 获取文件名称时间
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFileName(String preffex, String date) {
		date = date.replace(" ", "_").replace("-", "").replace(":", "");
		return preffex + date;
	}
}

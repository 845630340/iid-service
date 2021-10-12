package com.inspur.cloud.common.utils.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理通用类
 */
public final class DateUtils
{
    /**
     * 格式化日期 ： yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 日期字符串，如果date为空返回空串("")
     */
    public static String toString(Date date)
    {
        return DateFormatter.toString(date, DateSymbols.YYYY_MM_DD_HH_MM_SS);
    }


    public static String getCurrentDateString()
    {
        return toString(new Date());
    }
    public static String getCurrentGmt8String(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,8);
        Date time = calendar.getTime();
        return toString(time);
    }

    public static String getCurrentGmt8String(String dateFormatter){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,8);
        return DateFormatter.toString(date, dateFormatter);
    }


    public static Date getDateByHourInterval(Date date, int interval) {
        return getDateByInterval(date, Calendar.HOUR_OF_DAY, interval);
    }


    public static Date getDateByInterval(Date date, int unit, int interval)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(unit, interval);
        return calendar.getTime();
    }


    public static String getDateByInterval(String dateString,String format,int unit, int interval)
    {
        Date date=DateParser.toDate(dateString,format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(unit, interval);
        return DateFormatter.toString(calendar.getTime(),format);
    }

    public static boolean isValidDate(String dateString ,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            sdf.setLenient(false);
            sdf.parse(dateString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean isBeforeDate(String startDay,String endDay ,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date start=sdf.parse(startDay);
            Date end =sdf.parse(endDay);
            //开始时间小于等于结束时间
            if(end.before(start)){
                return false;
            }
            else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * String格式 :yyyy-MM-dd HH:mm:ss
     * String 转 date
     */
    public static Date parseStringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}

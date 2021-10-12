package com.inspur.cloud.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期处理通用类
 *
 */
@SuppressWarnings("unused")
public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 返回date1 - date2 的分钟数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔分钟数[如果入参有一个为null，则返回一年的分钟数意为无穷大]
     */
    public static long getMinites(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 525600L;
        }
        long millSec = date2.getTime() - date1.getTime();

        return (millSec / 1000) / 60;
    }

    /**
     * 返回date1 - date2 的分钟数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相隔分钟数
     */
    public static long getMins(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long millSec;
        long diffMins = 0L;
        try {
            millSec = dfs.parse(date1).getTime() - dfs.parse(date2).getTime();
            millSec = Math.abs(millSec);
            diffMins = (millSec / 1000) / 60;
        } catch (ParseException e) {
            LOGGER.error("getMins exception:" , e);
        }
        return diffMins;
    }

    /**
     * 返回date1 - date2 的秒数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相隔秒数
     */
    public static long getDiffSeconds(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long millSec;
        long diffSeconds = 0L;
        try {
            millSec = dfs.parse(date1).getTime() - dfs.parse(date2).getTime();
            millSec = Math.abs(millSec);
            diffSeconds = millSec / 1000;
        } catch (ParseException e) {
            LOGGER.error("getDiffSeconds exception:" , e);
        }
        return diffSeconds;
    }

    /**
     * 格式化日期 ： yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 日期字符串，如果date为空返回空串("")
     */
    public static String getFormatDateString(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 格式化日期 ： yyyy-MM-dd HH:mm
     *
     * @param date 日期
     * @return 日期字符串，如果date为空返回空串("")
     */
    public static String getFormatDateTimeString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    /**
     * 格式化日期 ： yyyy-MM-dd
     *
     * @param date 日期
     * @return 日期字符串，如果为空返回空串("")
     */
    public static String getDateString(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 格式化时间 ： HH:mm:ss
     *
     * @param date 日期对象
     * @return 格式化日期字符串
     */
    public static String getTimeString(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 由字符串转换为日期类型
     *
     * @param str    日期字符串
     * @param format 格式化字符串
     * @return 日期对象
     */
    public static Date getDate(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException | RuntimeException e) {
            return null;
        }
    }

    /**
     * 获取日期对应的时间，其中年月日为当前的年月日，时间为参数中的时间
     *
     * @param time 时间
     * @return 日期
     */
    public static Date getDateFromTime(Time time) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("HH:mm:ss").parse(time.toString()));
        } catch (ParseException | RuntimeException e) {
            LOGGER.error("getDateFromTime exception:" , e);
            return null;
        }

        Calendar cal = Calendar.getInstance();
        c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE));

        return c.getTime();
    }

    /**
     * 返回两个日期之间的天数差，精确到秒，如不足一天，会忽略，建议传入整天更准确
     *
     * @param fDate 结束日期
     * @param oDate 开始日期
     */
    public static int getIntervalDays(Date fDate, Date oDate) {

        if (null == fDate || null == oDate) {
            return -1;
        }

        long intervalMilli = oDate.getTime() - fDate.getTime();

        return (int) (intervalMilli / (24 * 60 * 60 * 1000));

    }

    /**
     * 返回两个日期之间的天数差，精确到秒，如不足一天，会忽略，建议传入整天更准确
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     */
    public static int getIntervalDays(String beginTime, String endTime) {
        Date fDate = DateUtil.getDate(beginTime, "yyyy-MM-dd");
        Date tDate = DateUtil.getDate(endTime, "yyyy-MM-dd");

        if (null == fDate || null == tDate) {
            return -1;
        }

        long intervalMilli = tDate.getTime() - fDate.getTime();

        return (int) (intervalMilli / (24 * 60 * 60 * 1000));

    }

    /**
     * 获取两个日期相差的月数
     *
     * @param d1 较大的日期
     * @param d2 较小的日期
     * @return 如果d1>d2返回 月数差 否则返回0
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /****
     * 传入具体日期 ，返回具体日期增加或减少N个月。
     * @param date 日期(2017-04-13)
     * @return 2017-05-13
     */
    public static String subMonth(String date, Integer months)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, months);
        Date dt1 = rightNow.getTime();
        return sdf.format(dt1);
    }

    /**
     * 返回毫秒级别数据
     *
     * @param date 日期对象
     * @return mmssSSS 格式时间
     */
    public static String gammassss(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
    }

    /**
     * 日期格式化（业务包使用，勿删）
     *
     * @param sDate 原始串
     * @param sFmt  格式
     * @return 输出
     */
    public static Date toDate(String sDate, String sFmt) {
        Date dt ;
        try {
            dt = new SimpleDateFormat(sFmt).parse(sDate);
        } catch (ParseException e) {
            return null;
        }
        return dt;
    }

    /**
     * 将日期格式化为长格式（业务包使用，勿删）
     *
     * @param dateDate 原始日期
     * @return 输出
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateDate);
    }

    /**
     * 将日期格式化为长格式到毫秒（业务包使用，勿删）
     *
     * @param dateDate 原始日期
     * @return 输出
     */
    public static String dateToStrLongSSS(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ssSSS");
        return formatter.format(dateDate);
    }

    /**
     * FormatDateTime（业务包使用，勿删）
     *
     * @param strDateTime 原始日期
     * @param strFormat   转换格式
     * @return 转换结果
     */
    public static String formatDateTime(String strDateTime, String strFormat) {
        String sDateTime;
        Calendar cal = parseDateTime(strDateTime);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(strFormat);
        sDateTime = sdf.format(cal.getTime());
        return sDateTime;
    }

    /**
     * 将日期解析为calendar对象（业务包使用，勿删）
     *
     * @param baseDate 原始日期
     * @return calendar对象
     */
    public static Calendar parseDateTime(String baseDate) {
        Calendar cal;
        cal = new GregorianCalendar();
        int yy = Integer.parseInt(baseDate.substring(0, 4));
        int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
        int dd = Integer.parseInt(baseDate.substring(8, 10));
        int hh = 0;
        int mi = 0;
        int ss = 0;
        if (baseDate.length() > 12) {
            hh = Integer.parseInt(baseDate.substring(11, 13));
            mi = Integer.parseInt(baseDate.substring(14, 16));
            ss = Integer.parseInt(baseDate.substring(17, 19));
        }
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal;
    }

    /**
     * 获取指定格式的当前时间（业务包使用，勿删）
     *
     * @param sformat 日期格式
     * @return 格式化日期串
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        return formatter.format(currentTime);
    }

    /**
     * @param mss 要转换的毫秒数 单位为英文单位 days，hour，分钟，秒
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }

    /**
     * @param mss 要转换的毫秒数 单位为汉字 天，小时，分钟，秒
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuringCN(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + " 小时 " + minutes + " 分" + seconds + " 秒 ";
    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     */
    public static String formatDuring(Date begin, Date end) {
        return formatDuring(end.getTime() - begin.getTime());
    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     */
    public static String formatDuringCN(Date begin, Date end) {
        return formatDuringCN(end.getTime() - begin.getTime());
    }

    /**
     * 格式化日期 ： yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 返回年 yyyy
     */
    public static String getFormatDateYear(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("yyyy").format(date);
    }

    /**
     * 格式化日期 ： yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 返回年 yyyy
     */
    public static String getFormatDateMonth(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("yyyy-MM").format(date);
    }

    /**
     * 返回上一个月的日期 yyyy-MM
     *
     * @param date 日期
     * @return 返回年 yyyy
     */
    public static String getFormatDateLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM").format(newDate);
    }

    /**
     * 返回下一个月的日期 yyyy-MM
     *
     * @param date 日期
     * @return 返回年 yyyy
     */
    public static String getFormatDateNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM").format(newDate);
    }

    public static String getLastNYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -years);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(newDate);
    }

    public static String getLastNMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -months);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(newDate);
    }

    public static String getLastNWeeks(Date date, int weeks) {
        return getLastNDays(date, weeks * 7);
    }

    public static String getLastNDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    /**
     * 获取传入时间N天后的时间值
     *
     * @param date 传入的时间
     * @param days 传入的天数N
     */
    public static Date getNextDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String getLastNHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hours);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    public static String getLastNHalfHours(Date date, int hhours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -hhours * 30);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    public static String getLastNFiveMins(Date date, int fiveminutes) {
        return getLastNMins(date, fiveminutes * 5);
    }

    public static String getLastNMins(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -minutes);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    public static String getLastNSecs(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, -seconds);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
    }

    public static String getLastNMinsWithParse(Date date, int minutes,
                                               String parse) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -minutes);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat(parse).format(newDate);
    }

    /**
     * 获取最近的正好5分钟的时间
     *
     */
    public static Date getLastJust5Min() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)
                - calendar.get(Calendar.MINUTE) % 5);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取最近的正好5分钟的时间字符串
     *
     */
    public static String getLastJust5MinStr() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)
                - calendar.get(Calendar.MINUTE) % 5);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(newDate);
    }

    /**
     * 获取最近的正好15分钟的时间字符串
     *
     */
    public static String getLastJust15MinStr() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)
                - calendar.get(Calendar.MINUTE) % 15);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(newDate);
    }

    public static int dayForWeek(String pTime) {

        int dayForWeek = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Calendar c = Calendar.getInstance();

            c.setTime(format.parse(pTime));

            dayForWeek = 0;

            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                dayForWeek = 7;

            } else {

                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

            }
        } catch (Exception e) {
            LOGGER.error("dayForWeek "+e.getMessage(),e);
        }

        return dayForWeek;

    }

    /**
     * 获取年月日 [默认取当前时间]
     *
     */
    public static String getYYYYMMDDStr() {
        return getYYYYMMDDStr(new Date());
    }

    /**
     * 获取年月日[格式：yyyyMMdd]
     *
     * @param date 传入的日期
     */
    public static String getYYYYMMDDStr(Date date) {
        if (null == date) {
            return "";
        }

        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 获取时分秒 [默认取当前的日期]
     *
     */
    public static String getHHMMSSStr() {
        return getHHMMSSStr(new Date());
    }

    /**
     * 获取时分秒[格式HHmmss]
     *
     * @param date 传入的日期；传入null默认取当前日期
     */
    public static String getHHMMSSStr(Date date) {
        if (null == date) {
            return "";
        }

        return new SimpleDateFormat("HHmmss").format(date);
    }

    /**
     * 获取年月 [默认取当前的日期]
     *
     */
    public static String getYYYYMMStr() {
        return getYYYYMMStr(new Date());
    }

    /**
     * 获取年月[格式yyyyMM]
     *
     * @param date 传入日期；传入null则默认当前日期
     */
    public static String getYYYYMMStr(Date date) {
        if (null == date) {
            return "";
        }

        return new SimpleDateFormat("yyyyMM").format(date);
    }

    /**
     * 时间计算
     *
     * @param time         原始时间
     * @param calendardate 时长单位
     * @param number       时长
     * @return 计算后时间
     * @author wanzhicheng 2018/11/20
     */
    public static Date getTime(String time, int calendardate, int number)
            throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(time));
        calendar.add(calendardate, number);
        return calendar.getTime();

    }

    /**
     * 获取账期开始时间 [当前时间的前一个小时 整分整秒]
     *
     * @return 账期开始时间
     */
    public static Date getCostCalculationStartTime() {
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(new Date());
        startTimeCalendar.set(Calendar.HOUR,
                startTimeCalendar.get(Calendar.HOUR) - 1);
        startTimeCalendar.set(Calendar.MINUTE, 0);
        startTimeCalendar.set(Calendar.SECOND, 0);
        return startTimeCalendar.getTime();
    }

    /**
     * 获取账期结束时间 [当前时间 整分整秒]
     *
     * @return 账期结束时间
     */
    public static Date getCostCalculationEndTime() {
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(new Date());
        endTimeCalendar.set(Calendar.MINUTE, 0);
        endTimeCalendar.set(Calendar.SECOND, 0);
        return endTimeCalendar.getTime();
    }

    /**
     * 获取时间字符串 Date 转Str
     *
     * @param format 时间格式
     * @param date   时间
     * @return Str
     */
    public static String getStrTime(String format, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * Str转Date
     *
     * @param format  时间格式
     * @param timeStr 时间字符串
     * @return Date
     * @throws Exception 时间转换异常
     */
    public static Date getTimeFromStr(String format, String timeStr)
            throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(timeStr);
    }

    /**
     * 返回date1 - date2 的秒数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相隔秒数带正负数
     */
    public static long getDiffSecond(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long millSec;
        long diffSeconds = 0L;
        try {
            millSec = dfs.parse(date1).getTime() - dfs.parse(date2).getTime();
            diffSeconds = millSec / 1000;
        } catch (ParseException e) {
            LOGGER.error("getDiffSeconds exception:" , e);
        }
        return diffSeconds;
    }

    /**
     *    取得当月天数   
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天  
        return a.get(Calendar.DATE);
    }
}

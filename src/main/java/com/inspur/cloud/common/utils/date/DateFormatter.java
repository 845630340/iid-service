package com.inspur.cloud.common.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * utility method for formatter date
 *
 * @author sunguangtao
 * @date 2019/1/1
 */
public class DateFormatter
{
    public static String toString(Date date, String format)
    {
        return new SimpleDateFormat(format).format(date);
    }

}

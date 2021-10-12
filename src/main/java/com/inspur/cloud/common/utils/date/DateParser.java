package com.inspur.cloud.common.utils.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * utility method for parsing date
 *
 * @author sunguangtao
 * @date 2019/1/1
 */
@Slf4j
@SuppressWarnings("all")
public class DateParser {
    public static Date toDate(String dateString, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateString);
        }
        catch (ParseException ignore) {
            log.error(ExceptionUtils.getStackTrace(ignore));
        }
        return null;
    }

    public static boolean match(String dateString, String dateFormat) {
        try {
            Date date =  new SimpleDateFormat(dateFormat).parse(dateString);
            return dateString.equals(DateFormatUtils.format(date,dateFormat));
        }
        catch (ParseException ignore) {
            log.error(ExceptionUtils.getStackTrace(ignore));
            return false;
        }
    }
}

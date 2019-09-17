package com.weChatCard.utils.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 *
 * @author: yupeng
 **/
public class DateTimeUtil {
    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    private static String timeZone = "GMT+8";

    /**
     * 当前日期是否在日期指定范围内<br>
     * 起始日期和结束日期可以互换
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @return 是否在范围内
     * @since 3.0.8
     */
    public static boolean isIn(Date beginDate, Date endDate, Date theDate) {
        long beginMills = beginDate.getTime();
        long endMills = endDate.getTime();
        long thisMills = theDate.getTime();

        return thisMills >= Math.min(beginMills, endMills) && thisMills <= Math.max(beginMills, endMills);
    }

    public static boolean isIn(String beginDate, String endDate, String theDate) {
        long beginMills = formatDate(beginDate).getTime();
        long endMills = formatDate(endDate).getTime();
        long thisMills = formatDate(theDate).getTime();

        return thisMills >= Math.min(beginMills, endMills) && thisMills <= Math.max(beginMills, endMills);
    }

    /**
     * Get Date or DateTime formatting pattern
     *
     * @param dateString Date String
     * @return Format Pattern
     */
    private static String getDatePattern(String dateString) {
        if (isDateTime(dateString)) {
            return (dateString.contains("/")) ? DateTimeFormat.DATE_TIME_PATTERN_2 : DateTimeFormat.DATE_TIME_PATTERN_1;
        } else {
            return (dateString.contains("/")) ? DateTimeFormat.DATE_PATTERN_2 : DateTimeFormat.DATE_PATTERN_1;
        }
    }

    /**
     * Convert a Java Date object to String
     *
     * @param date   Date Object
     * @param locale Locale
     * @return Date Object string representation
     */
    public static String formatDate(Date date, Locale locale) {
        if (date == null && log.isDebugEnabled()) {
            log.error("formatDate >> Supplied date is null");
        }
        SimpleDateFormat iso8601Format = new SimpleDateFormat(DateTimeFormat.DATE_TIME_PATTERN_1, locale);
        iso8601Format.setTimeZone(TimeZone.getTimeZone(timeZone));
        return iso8601Format.format(date);
    }

    /**
     * Convert a date string to Java Date Object
     *
     * @param dateString Date String
     * @param locale     Locale
     * @return Java Date Object
     */
    public static Date formatDate(String dateString, Locale locale) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat(getDatePattern(dateString), locale);
        iso8601Format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = null;
        if (dateString != null) {
            try {
                date = iso8601Format.parse(dateString.trim());
            } catch (ParseException e) {
                if (log.isDebugEnabled()) {
                    log.error("formatDate >> Fail to parse supplied date string >> " + dateString);
                    e.printStackTrace();
                }
            }
        }
        return date;
    }

    /**
     * Convert a Java Date object to String
     *
     * @param date Date Object
     * @return Date Object string representation
     */
    public static String formatDate(Date date) {
        return formatDate(date, Locale.getDefault());
    }

    /**
     * Convert a date string to Java Date Object
     *
     * @param date Date String
     * @return Java Date Object
     */
    public static Date formatDate(String date) {
        return formatDate(date, Locale.getDefault());
    }

    /**
     * Convert a timeStamp into a date object
     *
     * @param timeStamp TimeStamp
     * @param units     Witch unit is whether seconds or milliseconds
     * @return Date object
     */
    public static Date formatDate(long timeStamp, DateTimeFormat.DateTimeUnits units) {
        if (units.equals(DateTimeFormat.DateTimeUnits.SECONDS)) {
            return new Date(timeStamp * 1000L);
        } else {
            return new Date(timeStamp);
        }
    }

    /**
     * Convert a timeStamp into a date considering given timeStamp in milliseconds
     *
     * @param timeStamp TimeStamp
     * @return Date object
     * @see DateTimeFormat.DateTimeUnits#MILLISECONDS
     */
    public static Date formatDate(long timeStamp) {
        return formatDate(timeStamp, DateTimeFormat.DateTimeUnits.MILLISECONDS);
    }

    /**
     * Format date using a given pattern
     * and apply supplied locale
     *
     * @param date    Date Object
     * @param pattern Pattern
     * @param locale  Locale
     * @return Formatted date
     */
    public static String formatWithPattern(Date date, String pattern, Locale locale) {
        if (date == null && log.isDebugEnabled()) {
            log.error("FormatWithPattern >> Supplied date is null");
        }
        SimpleDateFormat iso8601Format = new SimpleDateFormat(pattern, locale);
        iso8601Format.setTimeZone(TimeZone.getTimeZone(timeZone));
        return iso8601Format.format(date);
    }

    /**
     * Format date using a given pattern
     * and apply supplied locale
     *
     * @param date    Date String
     * @param pattern Pattern
     * @param locale  Locale
     * @return Formatted date
     */
    public static String formatWithPattern(String date, String pattern, Locale locale) {
        return formatWithPattern(formatDate(date), pattern, locale);
    }

    /**
     * Format date using a given pattern
     * apply default locale
     *
     * @param date    Date Object
     * @param pattern Pattern
     * @return Formatted date
     */
    public static String formatWithPattern(Date date, String pattern) {
        return formatWithPattern(date, pattern, Locale.getDefault());
    }

    /**
     * Format date using a given pattern
     * apply default locale
     *
     * @param date    Date String
     * @param pattern Pattern
     * @return Formatted date
     */
    public static String formatWithPattern(String date, String pattern) {
        return formatWithPattern(date, pattern, Locale.getDefault());
    }

    /**
     * Build a pattern for given style
     *
     * @param style DateTimeFormat.DateTimeStyle
     * @return Pattern
     */
    private static String getPatternForStyle(DateTimeFormat.DateTimeStyle style) {
        String pattern;
        if (style.equals(DateTimeFormat.DateTimeStyle.LONG)) {
            pattern = "MMMM dd, yyyy";
        } else if (style.equals(DateTimeFormat.DateTimeStyle.MEDIUM)) {
            pattern = "MMM dd, yyyy";
        } else if (style.equals(DateTimeFormat.DateTimeStyle.SHORT)) {
            pattern = "MM/dd/yy";
        } else {
            pattern = "EEEE, MMMM dd, yyyy";
        }
        return pattern;
    }

    /**
     * Get localized date string
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    public static String formatWithStyle(Date date, DateTimeFormat.DateTimeStyle style, Locale locale) {
        if (date == null && log.isDebugEnabled()) {
            log.error("FormatWithPattern >> Supplied date is null");
        }
        return formatWithPattern(date, getPatternForStyle(style), locale);
    }

    /**
     * Get localized date string (Using default locale)
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    public static String formatWithStyle(String date, DateTimeFormat.DateTimeStyle style, Locale locale) {
        return formatWithStyle(formatDate(date), style, locale);
    }

    /**
     * Get localized date string (Using default locale)
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    public static String formatWithStyle(Date date, DateTimeFormat.DateTimeStyle style) {
        return formatWithStyle(date, style, Locale.getDefault());
    }

    /**
     * Get localized date string (Using default locale)
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    public static String formatWithStyle(String date, DateTimeFormat.DateTimeStyle style) {
        return formatWithStyle(date, style, Locale.getDefault());
    }

    /**
     * Extract time from date without seconds
     *
     * @param date Date object
     * @return Time String
     * @see DateTimeFormat#TIME_PATTERN_1
     */
    public static String formatTime(Date date, boolean forceShowHours) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat(DateTimeFormat.TIME_PATTERN_1, Locale.getDefault());
        iso8601Format.setTimeZone(TimeZone.getTimeZone(timeZone));
        String time = iso8601Format.format(date);
        String[] hhmmss = time.split(":");
        int hours = Integer.parseInt(hhmmss[0]);
        int minutes = Integer.parseInt(hhmmss[1]);
        int seconds = Integer.parseInt(hhmmss[2]);
        return (hours == 0 && !forceShowHours ? "" : hours < 10 ? String.valueOf("0" + hours) + ":" : String.valueOf(hours) + ":") + (minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes)) + ":" + (seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
    }

    /**
     * Extract time from date without seconds
     *
     * @param date Date object
     * @return Time string
     */
    public static String formatTime(String date, boolean forceShowHours) {
        return formatTime(formatDate(date), forceShowHours);
    }

    /**
     * Extract time from date without seconds
     *
     * @param date Date object
     * @return Time string
     */
    public static String formatTime(Date date) {
        return formatTime(date, false);
    }

    /**
     * Extract time from date without seconds
     *
     * @param date Date object
     * @return Time string
     */
    public static String formatTime(String date) {
        return formatTime(date, false);
    }

    /**
     * Convert millis to human readable time
     *
     * @param millis TimeStamp
     * @return Time String
     */
    public static String millisToTime(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long hours = TimeUnit.MILLISECONDS.toHours(millis);

        return (hours == 0 ? "" : hours < 10 ? String.valueOf("0" + hours) + ":" : String.valueOf(hours) + ":") + (minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes)) + ":" + (seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));

    }

    /**
     * Convert millis to human readable time
     *
     * @param time Time string
     * @return Time String
     */
    public static long timeToMillis(String time) {
        String[] hhmmss = time.split(":");
        int hours = 0;
        int minutes;
        int seconds;
        if (hhmmss.length == 3) {
            hours = Integer.parseInt(hhmmss[0]);
            minutes = Integer.parseInt(hhmmss[1]);
            seconds = Integer.parseInt(hhmmss[2]);
        } else {
            minutes = Integer.parseInt(hhmmss[0]);
            seconds = Integer.parseInt(hhmmss[1]);
        }
        return (((hours * 60) + (minutes * 60) + seconds) * 1000);
    }

    /**
     * Tell whether or not a given string represent a date time string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a date time False otherwise
     */
    public static boolean isDateTime(String dateString) {
        return (dateString != null) && (dateString.trim().split(" ").length > 1);
    }

    /**
     * Tell whether or not a given date is yesterday
     *
     * @param date Date Object
     * @return True if the date is yesterday False otherwise
     */
    public static boolean isYesterday(Date date) {
        // Check if yesterday
        // today
        Calendar c1 = Calendar.getInstance();
        // yesterday
        c1.add(Calendar.DAY_OF_YEAR, -1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Tell whether or not a given date is yesterday
     *
     * @param dateString Date String
     * @return True if the date is yesterday False otherwise
     */
    public static boolean isYesterday(String dateString) {
        return isYesterday(formatDate(dateString));
    }

    /**
     * Tell whether or not a given date is today date
     *
     * @param date Date object
     * @return True if date is today False otherwise
     */
    public static boolean isToday(Date date) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Tell whether or not a given date is today date
     *
     * @param dateString Date string
     * @return True if date is today False otherwise
     */
    public static boolean isToday(String dateString) {
        return isToday(formatDate(dateString));
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    public static int getDateDiff(Date nowDate, Date oldDate, DateTimeFormat.DateTimeUnits dateDiff) {
        long diffInMs = nowDate.getTime() - oldDate.getTime();
        int days = (int) TimeUnit.MILLISECONDS.toDays(diffInMs);
        int hours = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(days));
        int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        switch (dateDiff) {
            case DAYS:
                return days;
            case SECONDS:
                return seconds;
            case MINUTES:
                return minutes;
            case HOURS:
                return hours;
            case MILLISECONDS:
            default:
                return (int) diffInMs;
        }
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    public static int getDateDiff(String nowDate, Date oldDate, DateTimeFormat.DateTimeUnits dateDiff) {
        return getDateDiff(formatDate(nowDate), oldDate, dateDiff);
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    public static int getDateDiff(Date nowDate, String oldDate, DateTimeFormat.DateTimeUnits dateDiff) {
        return getDateDiff(nowDate, formatDate(oldDate), dateDiff);
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    public static int getDateDiff(String nowDate, String oldDate, DateTimeFormat.DateTimeUnits dateDiff) {
        return getDateDiff(nowDate, formatDate(oldDate), dateDiff);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date      基准日期
     * @param dateField 偏移的粒度大小（小时、天、月等）
     * @param offset    偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    public static Date offset(Date date, DateTimeFormat.DateField dateField, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(date);
        cal.add(dateField.getValue(), offset);
        return cal.getTime();
    }

    public static Date offset(String date, DateTimeFormat.DateField dateField, int offset) {
        return offset(formatDate(date), dateField, offset);
    }

    /**
     * 判断一个日期是否为周末
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     * 通过字典设置读取
     *
     * @param date
     * @return
     */
    public static boolean isHoliday(Date date) {
        return false;
    }

    /**
     * 是否在给定日期之前
     *
     * @param date 日期
     * @return 是否在给定日期之前或与给定日期相等
     */
    public static boolean isBefore(Date date,Date dDate) {
        if (null == date) {
            throw new NullPointerException("Date to compare is null !");
        }
        return dDate.compareTo(date) < 0;
    }

    /**
     * 是否在给定日期之前或与给定日期相等
     *
     * @param date 日期
     * @return 是否在给定日期之前或与给定日期相等
     */
    public static boolean isBeforeOrEquals(Date date,Date dDate) {
        if (null == date) {
            throw new NullPointerException("Date to compare is null !");
        }
        return dDate.compareTo(date) <= 0;
    }

    /**
     * 是否在给定日期之后或与给定日期相等
     *
     * @param date 日期
     * @return 是否在给定日期之后或与给定日期相等
     */
    public static boolean isAfter(Date date,Date dDate) {
        if (null == date) {
            throw new NullPointerException("Date to compare is null !");
        }
        return dDate.compareTo(date) > 0;
    }

    /**
     * 是否在给定日期之后或与给定日期相等
     *
     * @param date 日期
     * @return 是否在给定日期之后或与给定日期相等
     */
    public static boolean isAfterOrEquals(Date date,Date dDate) {
        if (null == date) {
            throw new NullPointerException("Date to compare is null !");
        }
        return dDate.compareTo(date) >= 0;
    }

    /**
     * 是否和给定日期相等
     *
     * @param date 日期
     * @return 是否在给定日期之后或与给定日期相等
     */
    public static boolean isEqualsDate(Date date,Date dDate) {
        date = formatDate(formatWithPattern(date,DateTimeFormat.DATE_PATTERN_1));
        dDate = formatDate(formatWithPattern(dDate,DateTimeFormat.DATE_PATTERN_1));
        if (null == date) {
            throw new NullPointerException("Date to compare is null !");
        }
        return dDate.compareTo(date) == 0;
    }


    /**
     * @method_name: UDateToLocalDate
     * @param: []
     * @return: void
     * @describe: java.util.Date --> java.time.LocalDate
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:41
     * @version: V1.0
     **/
    public static LocalDate udatetolocaldate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * @method_name: UDateToLocalTime
     * @param: [date]
     * @return: java.time.LocalTime
     * @describe: java.util.Date --> java.time.LocalTime
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:48
     * @version: V1.0
     **/
    public static LocalTime udateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    /**
     * @method_name: UDateToLocalDateTime
     * @param: [date]
     * @return: java.time.LocalDateTime
     * @describe: java.util.Date --> java.time.LocalDateTime
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:48
     * @version: V1.0
     **/
    public static LocalDateTime udateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * @method_name: LocalDateToUdate
     * @param: [localDate]
     * @return: java.util.Date
     * @describe:  java.time.LocalDate --> java.util.Date
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:43
     * @version: V1.0
     **/
    public static Date localDateToUdate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * @method_name: LocalTimeToUdate
     * @param: [localTime, localDate]
     * @return: java.util.Date
     * @describe: java.time.LocalTime --> java.util.Date
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:45
     * @version: V1.0
     **/
    public static Date localTimeToUdate(LocalTime localTime,LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * @method_name: LocalDateTimeToUdate
     * @param: [localDateTime]
     * @return: java.util.Date
     * @describe: java.time.LocalDateTime --> java.util.Date
     * @author: liaoxm(xiafish201@hotmail.com)
     * @creat_date: 2018/9/26 下午4:46
     * @version: V1.0
     **/
    public static Date localDateTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static String getTimeZone() {
        return timeZone;
    }

    public static void setTimeZone(String timeZone) {
        DateTimeUtil.timeZone = timeZone;
    }
}

package com.alex.commons.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式
     */
    public static final String DEFAULT_DATETIME_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 年月日时分
     */
    public static final String DEFAULT_TIME_HM_FORMAT = "yyyy-MM-dd HH:mm";

    public static Calendar calendar = Calendar.getInstance();

    /**
     * 将<tt>Date</tt>转换成<tt>String</tt>,使用年月日小时分钟秒格式.
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DateUtil.DEFAULT_DATETIME_FORMAT);
        return format.format(date);
    }

    public static String format(Date date, String formar) {
        SimpleDateFormat format = new SimpleDateFormat(formar);
        return format.format(date);
    }

    /**
     * 将<tt>String</tt>转换成<tt>Date</tt>,使用年月日小时分钟秒格式.
     *
     * @param source
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Date parse(String source) {
        SimpleDateFormat format = new SimpleDateFormat(DateUtil.DEFAULT_DATETIME_FORMAT);
        try {
            return format.parse(source);
        } catch (ParseException e) {
            format = new SimpleDateFormat(DateUtil.DEFAULT_DATE_FORMAT);
            ;
            try {
                return format.parse(source);
            } catch (ParseException e1) {
                return null;
            }
        }
    }

    /**
     * 将<tt>String</tt>转换成<tt>Date</tt>,使用指定格式. 如果转换异常,返回null.
     *
     * @param source
     * @param formar
     * @return
     */
    public static Date parse(String source, String formar) {
        SimpleDateFormat format = new SimpleDateFormat(formar);
        try {
            return format.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将日期增加/减少指定天数.
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     * 将日期增加/减少指定小时.
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /**
     * 将日期增加/减少指定分钟.
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * 将日期增加/减少指定秒.
     *
     * @param date
     * @param second
     * @return
     */
    public static Date addSecond(Date date, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 以友好的方式显示时间信息.
     *
     * @param time
     * @return
     */
    public static String friendlyDate(Date time) {
        if (time == null)
            return "未知";

        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);

        if (ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟以前";
        }

        if (ct >= 3600 && ct < 86400) {
            return (ct / 3600) + "小时以前";
        }

        if (ct >= 86400 && ct < 2592000) { // 86400 * 30
            int day = ct / 86400;
            if (day > 1) {
                return day + "天以前";
            }
            return "昨天";
        }

        if (ct >= 2592000 && ct < 31104000) { // 86400 * 30
            return (ct / 2592000) + "月以前";
        }

        return (ct / 31104000) + "年以前";
    }

    /**
     * @return 返回当前年
     */
    public static int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @return 返回当前月
     */
    public static int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * @return 返回当前天
     */
    public static int getDay() {
        return calendar.get(Calendar.DATE);
    }

    /**
     * @return 返回当前小时
     */
    public static int get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @return 返回当前分钟
     */
    public static int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * @return 返回当前秒
     */
    public static int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * @return 返回当前时间星期几.
     */
    public static int getWeek() {
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return (week == 1) ? 7 : week - 1;
    }

    /**
     * @return 返回当天指定时分秒时间.
     */
    public static Date getToday(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        // calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimeStamp() {
        return (new Date().getTime() / 1000);
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合(按天)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return List<String>
     */
    public static List<String> getDatesBetweenToDate(String startTime, String endTime) throws Exception {

        Date beginDate;
        Date endDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        beginDate = format.parse(startTime);
        endDate = format.parse(endTime);
        List<String> dateList = new ArrayList<String>();
        dateList.add(startTime);// 把开始时间加入集合
        if (startTime.equalsIgnoreCase(endTime))
            return dateList;
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        if (cal.getTime().after(endDate)) {
            throw new RuntimeException("开始时间大于结束时间");
        }
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateList.add(format.format(cal.getTime()));
            } else {
                break;
            }
        }
        dateList.add(endTime);// 把结束时间加入集合
        return dateList;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合  以小时为单位
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public static List<String> getDateHoursBetweenToDate(String startTime, String endTime) throws Exception {
        Date beginDate;
        Date endDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:00");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        startTime = startTime + " 0:00";
        if (fmt.format(new Date()).equals(endTime)) {
            endTime = format.format(new Date());
        } else {
            endTime = endTime + " 23:00";
        }
        beginDate = format.parse(startTime);
        endDate = format.parse(endTime);
        List<String> dateList = new ArrayList<String>();
        dateList.add(startTime);// 把开始时间加入集合
        if (startTime.equalsIgnoreCase(endTime))
            return dateList;
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        if (cal.getTime().after(endDate)) {
            throw new RuntimeException("开始时间大于结束时间");
        }

        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.HOUR_OF_DAY, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateList.add(format.format(cal.getTime()));
            } else {
                break;
            }
        }
        dateList.add(endTime);// 把结束时间加入集合
        return dateList;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合(按月)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @throws Exception
     */
    public static List<String> getDatesBetweenToMon(String startTime, String endTime) throws Exception {
        Date beginDate;
        Date endDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        beginDate = format.parse(startTime);
        endDate = format.parse(endTime);
        List<String> dateList = new ArrayList<String>();
        dateList.add(format.format(beginDate));// 把开始月份加入集合
        if (format.format(beginDate).equalsIgnoreCase(format.format(endDate)))// 开始月份等于结束月份，直接返回开始月份
            return dateList;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(beginDate);
        cal2.setTime(endDate);
        while (cal1.compareTo(cal2) < 0) {
            cal1.add(Calendar.MONTH, 1);// 开始月份加一个月直到等于结束日期为止
            Date date = cal1.getTime();
            String temp = format.format(date);
            dateList.add(temp);
        }
        return dateList;
    }

    /**
     * 计算小时间隔
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getTimeSub(String startTime, String endTime) throws Exception {
        List<String> subTime = new ArrayList<String>();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(date);
        String day = dateString.substring(0, 10);
        String hour = dateString.substring(11, 13);
        //返回小时
        if (endTime.equals(day)) {
            //今天
            for (int i = 0; i <= Integer.parseInt(hour); i++) {
                subTime.add(String.valueOf(i));
            }
        } else {
            //非今天
            for (int i = 0; i < 24; i++) {
                subTime.add(String.valueOf(i));
            }
        }
        return subTime;
    }

    public static void main(String[] args) throws Exception {

    }

    public static Date now() {
        return new Date();
    }
}

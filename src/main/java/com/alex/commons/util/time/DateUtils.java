package com.alex.commons.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 获取时间
     *
     * @param num +今天后某天  -今天前某几天
     * @return
     */
    public static String getNextDateStr(int num) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num);
        date = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取时间
     *
     * @param num +今天后某天  -今天前某几天
     * @return
     */
    public static String getNextDateStr(String day, int num) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        date = cal.getTime();
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取本年第一天
     *
     * @return
     */
    public static String getYear() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(date) + "-01-01";
        return dateString;
    }

    /**
     * 获取本月第一天
     *
     * @return
     */
    public static String getMonth() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(date) + "-01";
        return dateString;
    }

    /**
     * 获取两个时间相差的天数
     *
     * @param BT
     * @param ET
     * @return
     * @throws ParseException
     */
    public static long getDiffDay(String BT, String ET) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = sdf.parse(BT);
        Date endTime = sdf.parse(ET);
        long l = endTime.getTime() - startTime.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        return day;
    }

    public static void main(String[] args) throws Exception {
        String today = DateUtils.getNextDateStr(0);
        String Yesterday = DateUtils.getNextDateStr(-1);
        String last7day = DateUtils.getNextDateStr(-6);
        String last30day = DateUtils.getNextDateStr(-29);
        String beginyear = DateUtils.getYear();

        System.out.println(today);
        System.out.println(Yesterday);
        System.out.println(last7day);
        System.out.println(last30day);
        System.out.println(beginyear);

        System.out.println(getDiffDay("2016-02-01", "2017-02-11"));
        System.out.println(getNextDateStr("2016-02-01", 1));
    }
}

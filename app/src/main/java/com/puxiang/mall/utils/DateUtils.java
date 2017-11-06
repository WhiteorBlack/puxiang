package com.puxiang.mall.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.finalteam.toolsfinal.StringUtils;

public class DateUtils {
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_FORMAT_DAY = "yyyy-MM-dd";
    public static long day = 86400000;
    public static long hour = 3600000;
    public static long minute = 60000;
    public static long second = 1000;
    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat sdf;

    public static String getPostTime(String date) {
        long time = System.currentTimeMillis() - convert2long(date);
        String postTime = "";
        long days = time / day;
        if (days >= 1) {
            postTime = days + "天前";
        } else {
            long hours = time / hour;
            if (hours >= 1) {
                postTime = hours + "小时前";
            } else {
                long minutes = time / minute;
                if (minutes >= 1) {
                    postTime = minutes + "分钟前";
                } else {
                    postTime = "刚刚";
                }
            }
        }
        return postTime;
    }

    /**
     * 要转换的毫秒数
     *
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
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
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     * @author fy.zhang
     */
    public static String formatDuring(Date begin, Date end) {
        return formatDuring(end.getTime() - begin.getTime());
    }

    public static long convert2long(String date) {
        try {
            if (!StringUtils.isEmpty(date)) {
                if (sdf == null) {
                    sdf = new SimpleDateFormat(TIME_FORMAT);
                }
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(date));
                return c.getTimeInMillis();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static long dateConvertLong(String date) {
        try {
            if (!StringUtils.isEmpty(date)) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(TIME_FORMAT_DAY);
                }
                Calendar c = Calendar.getInstance();
                c.setTime(dateFormat.parse(date));
                return c.getTimeInMillis();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}

package com.yeyanxiang.util.gitv;

import java.text.DecimalFormat;

/**
 * Created by yeyanxiang on 15-7-27.
 */
public class DateTimeUtils {
    private static DecimalFormat decimalFormat = new DecimalFormat("00");

    /**
     * @param time   单位ms
     * @param format hh/mm/ss
     * @return
     */
    public static String formatTime(long time, String format) {
        String hh = decimalFormat.format(time / 1000 / 60 / 60);
        String mm = decimalFormat.format(time / 1000 / 60 % 60);
        String ss = decimalFormat.format(time / 1000 % 60);
        if (format == null) return hh + ":" + mm + ":" + ss;
        if (time <= 0) {
            hh = "00";
            mm = "00";
            ss = "00";
        }
        if (format.contains("hh")) format = format.replace("hh", hh);
        if (format.contains("mm")) format = format.replace("mm", mm);
        if (format.contains("ss")) format = format.replace("ss", ss);
        return format;
    }
}

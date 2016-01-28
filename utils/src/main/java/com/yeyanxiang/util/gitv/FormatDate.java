package com.yeyanxiang.util.gitv;

import android.text.TextUtils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yeyanxiang on 15-5-18.
 */
public class FormatDate {
    private String dateString;

    private SimpleDateFormat simpleDateFormat = new GMTDateFormat("yyyy.MM.dd hh:mm");
    private String date;
    private String time;

    public FormatDate(String datestr) {
        this.dateString = datestr;
        if (!TextUtils.isEmpty(dateString)) {
            try {
                this.dateString = simpleDateFormat.format(new Date(Long.parseLong(datestr)));
                if (this.dateString.contains(" ")) {
                    int index = this.dateString.indexOf(" ");
                    date = this.dateString.substring(0, index);
                    time = this.dateString.substring(index + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.loge("FormatDate", datestr + " format fail");
            }
        }
    }

    public FormatDate(String datestr, String pattern) {
        this.dateString = datestr;
        simpleDateFormat = new GMTDateFormat(pattern);
        if (!TextUtils.isEmpty(dateString)) {
            try {
                this.dateString = simpleDateFormat.format(new Date(Long.parseLong(datestr)));
                if (this.dateString.contains(" ")) {
                    int index = this.dateString.indexOf(" ");
                    date = this.dateString.substring(0, index);
                    time = this.dateString.substring(index + 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.loge("FormatDate", datestr + " format fail");
            }
        }
    }

    public FormatDate(long dateStr) {
        this.dateString = dateStr + "";
        try {
            this.dateString = simpleDateFormat.format(new Date(dateStr));
            if (this.dateString.contains(" ")) {
                int index = this.dateString.indexOf(" ");
                date = this.dateString.substring(0, index);
                time = this.dateString.substring(index + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.loge("FormatDate", dateStr + " format fail");
        }
    }

    public FormatDate(long dateStr, String pattern) {
        this.dateString = dateStr + "";
        simpleDateFormat = new GMTDateFormat(pattern);
        try {
            this.dateString = simpleDateFormat.format(new Date(dateStr));
            if (this.dateString.contains(" ")) {
                int index = this.dateString.indexOf(" ");
                date = this.dateString.substring(0, index);
                time = this.dateString.substring(index + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.loge("FormatDate", dateStr + " format fail");
        }
    }

    public String getDateString() {
        return dateString;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

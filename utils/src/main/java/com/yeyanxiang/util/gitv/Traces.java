/*
 * Copyright (c) 2015 银河互联网电视有限公司. All rights reserved.
 */

package com.yeyanxiang.util.gitv;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

public final class Traces {
    private static String TRACE_TAG = "Traces";
    private static boolean sEnableTrace = true;
    public static final int SUPPRESS = Log.ASSERT + 1;
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;
    // VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS
    private static int sTraceLevel = SUPPRESS;

    private Traces() {
        throw new UnsupportedOperationException();
    }

    public static void enable() {
        sEnableTrace = true;
    }

    public static void disable() {
        sEnableTrace = false;
    }

    public static void setTraceLevel(int level) {
        if (VERBOSE <= level && level <= SUPPRESS) {
            sTraceLevel = level;
        }
    }

    public static int getTraceLevel() {
        return sTraceLevel;
    }

    public static void setTraceTag(String tag) {
        if (tag != null && !TextUtils.isEmpty(tag)) {
            TRACE_TAG = tag;
        }
    }

    public static String getTraceTag() {
        return TRACE_TAG;
    }

    public static boolean isEnabled() {
        return sEnableTrace;
    }

    public static void d(String msg) {
        trace(Log.DEBUG, msg);
    }

    public static void d(String format, Object... args) {
        trace(Log.DEBUG, format, args);
    }

    public static void e(String msg) {
        trace(Log.ERROR, msg);
    }

    public static void e(String format, Object... args) {
        trace(Log.ERROR, format, args);
    }

    public static void i(String msg) {
        trace(Log.INFO, msg);
    }

    public static void i(String format, Object... args) {
        trace(Log.INFO, format, args);
    }

    public static void v(String msg) {
        trace(Log.VERBOSE, msg);
    }

    public static void v(String format, Object... args) {
        trace(Log.VERBOSE, format, args);
    }

    public static void w(String msg) {
        trace(Log.WARN, msg);
    }

    public static void w(String format, Object... args) {
        trace(Log.WARN, format, args);
    }

    private static void trace(int level, String format, Object... args) {
        if (!isEnabled()) return;

        println(TRACE_TAG, level, format, args);
    }

    private static StackTraceElement getCurrentElement() {
        // dynamic change index of stack trace array
        return Thread.currentThread().getStackTrace()[6];
    }

    private static void println(String tag, int level, String format, Object... args) {
        if (!isLoggable(level)) return;

        Log.println(level, tag, String.format(Locale.US, "[%d] %s: %s", android.os.Process.myPid(), buildMessage(getCurrentElement()), String.format(format, args)));
    }

    private static String getSimpleClsName(String clsName) {
        String cName = clsName;
        // FIXME 如何显示当前路径
        if (false && !TextUtils.isEmpty(cName)) {
            cName = cName.substring(cName.lastIndexOf(".") + 1);
            cName = cName.substring(cName.lastIndexOf("$") + 1);
        }
        return cName;
    }

    private static String buildMessage(StackTraceElement element) {
        final String className = getSimpleClsName(element.getClassName());
        final String methodName = element.getMethodName();
        final int lineNumber = element.getLineNumber();
        return String.format(Locale.US, "%s.%s(%d)", className, methodName, lineNumber);
    }

    private static boolean isTraceEnable(int level) {
        return sTraceLevel <= level;
    }

    private static boolean isLoggable(int level) {
        return isTraceEnable(level) || Log.isLoggable(TRACE_TAG, level);
    }
}
/*
 * Copyright (c) 2015 银河互联网电视有限公司. All rights reserved.
 */

package com.yeyanxiang.util.gitv;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public final class HttpUtils {
    private static final HashMap<String, String> DEFAULT_PROPERTIES = new HashMap<>();

    static {
        DEFAULT_PROPERTIES.put("accept", "*/*");
        DEFAULT_PROPERTIES.put("connection", "Keep-Alive");
        DEFAULT_PROPERTIES.put("Content-Type", "application/json");
    }

    private enum Method {
        GET, POST;
    }

    private HttpUtils() {
    }

    private static void setRequestProperties(HttpURLConnection httpURLConnection, HashMap<String, String> headers) {
        if (httpURLConnection != null && headers != null && !headers.isEmpty()) {
            for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void executeRequest(@NonNull String url, @NonNull Method method) {
        executeRequest(url, method, null, null, null);
    }

    private static void executeRequest(@NonNull String url, @NonNull Method method, HashMap<String, String> properties, String data, HttpCallback callback) {
        int len = url.length();
        if (len > 0) {
            if ("&".equals(url.substring(len - 1, len))) {
                url = url.substring(0, len - 1);
            }
        }

        Traces.i("executeRequest url = %s\n,method = %s, properties = %s, data = %s, callback = %s", url, method, properties, data, callback);

        if (TextUtils.isEmpty(url) || method == null) {
            Traces.w("executeRequest url = %s, method = %s", url, method);
            return;
        }

        HttpURLConnection httpURLConnection = null;
        try {
            URL targetUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) targetUrl.openConnection();
            final HashMap<String, String> requestProperties = new HashMap<>(DEFAULT_PROPERTIES);
            // 设置通用的请求属性
            if (properties != null && !properties.isEmpty()) {
                requestProperties.putAll(properties);
            }

            // 发送请求参数
            byte[] dataBytes = null;
            if (method == Method.POST && data != null && !data.isEmpty()) {
                dataBytes = data.getBytes();
                requestProperties.put("Content-Length", String.valueOf(dataBytes.length));
            }
            setRequestProperties(httpURLConnection, requestProperties);
            httpURLConnection.setRequestMethod(method.name());

            // 发送POST请求必须设置如下两行
            if (method == Method.POST && dataBytes != null && dataBytes.length > 0) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                DataOutputStream dop = new DataOutputStream(httpURLConnection.getOutputStream());
                dop.write(dataBytes);
                dop.flush();
                closeIgnoreException(dop);
            }

            final int responseCode = httpURLConnection.getResponseCode();
            final String responseMessage = httpURLConnection.getResponseMessage();
            Traces.i("executeRequest responseCode = %d, responseMessage = %s", responseCode, responseMessage);
            if (callback != null) callback.onResponse(responseCode, responseMessage);

        } catch (MalformedURLException e) {
            Traces.e("executeRequest exception = %s", e);
            if (callback != null) callback.onError(e);
        } catch (IOException e) {
            Traces.e("executeRequest exception = %s", e);
            if (callback != null) callback.onError(e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private static void closeIgnoreException(@NonNull Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) { /** ignored */}
        }
    }

    public static void executeGet(String url) {
        executeRequest(url, Method.GET);
    }

    public static void executeGet(String url, HttpCallback callback) {
        executeRequest(url, Method.GET, null, null, callback);
    }

    private void postExecute(String url, String data) {
        executeRequest(url, Method.POST, null, data, null);
    }

    public interface HttpCallback {
        /**
         * @param responseCode
         * @param responseMessage 响应数据，参见{@link java.net.HttpURLConnection#responseMessage}
         */
        void onResponse(int responseCode, String responseMessage);

        void onError(Throwable error);
    }
}

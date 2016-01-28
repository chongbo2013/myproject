package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yezi on 15-11-10.
 */
public class LogcatHelper {

    private static final String TAG = "LogcatHelper";

    private static final int maxLine = 5000;
    private static final String userName = "upload_gitv";
    private static final String passWord = "AYOkj96M";
    private static final String method = "uploadfile";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
    private static final Handler mHandler = new Handler();

    private static String directoryPath;
    private static String macNum;

    public interface OnSendLogListener {
        void logSendSucc();

        void logSendFail(Throwable tr);
    }

    public static String getDirectoryPath() {
        return directoryPath;
    }

    public static void init(Context context) {
        directoryPath = context.getCacheDir().getPath() + "/log/";
        macNum = DeviceUtils.getMacAddress(context);
        Log.i(TAG, "init directory " + directoryPath + " mac " + macNum);
        clearLog();
    }

    public static void clearLog() {
        if (TextUtils.isEmpty(directoryPath)) {
            Log.e(TAG, "clearLog error not init");
            return;
        }

        File direc = new File(directoryPath);
        if (direc.exists()) {
            Log.i(TAG, "clearLog " + directoryPath);
            if (direc.isDirectory()) {
                for (File file : direc.listFiles()) {
                    Log.i(TAG, "delete log file " + file.getName() + " " + file.delete());
                }
            }
        } else {
            direc.mkdirs();
        }
    }

    public static String getLog(String logName) {
        if (TextUtils.isEmpty(directoryPath)) {
            Log.e(TAG, "getLog error not init");
            return null;
        }
        final String name = dateFormat.format(new Date()) + "_yezi_" + macNum + "_" + logName + ".log";
        final String path = directoryPath + name;
        Log.i(TAG, "getLog " + name);
        new LogcatThread(path).start();
        return path;
    }

    public static void sendLog(final String path, final OnSendLogListener onSendLogListener) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG, "send log error " + path + " not exists");
            if (onSendLogListener != null)
                onSendLogListener.logSendFail(new Throwable("log not exists"));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                File sendFile = ZipUtil.zip(path);
                Log.i(TAG, "send log start " + sendFile.getName());

                if (sendFile == null || !sendFile.exists()) {
                    Log.e(TAG, "send log error isn't existed");
                    if (onSendLogListener != null)
                        onSendLogListener.logSendFail(new Throwable("zip file not exists"));
                    return;
                }

                DefaultHttpClient httpClient = new DefaultHttpClient();

                AuthScope authScope = new AuthScope(AuthScope.ANY_HOST,
                        AuthScope.ANY_PORT);
                UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
                        userName, passWord);
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

                credentialsProvider.setCredentials(authScope,
                        usernamePasswordCredentials);
                httpClient.setCredentialsProvider(credentialsProvider);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart(method, new FileBody(sendFile));
                HttpEntity httpEntity = builder.build();

                HttpEntityEnclosingRequestBase requestBase = getRequestBase(false);
                requestBase.setEntity(httpEntity);
                HttpResponse httpResponse;
                try {
                    httpResponse = httpClient.execute(requestBase);
                    final int statusCode = httpResponse.getStatusLine()
                            .getStatusCode();
                    String response = EntityUtils.toString(
                            httpResponse.getEntity(), HTTP.UTF_8);
                    Log.d(TAG, "send log finish statusCode=" + statusCode + " response: " + response);

                    if (statusCode == HttpStatus.SC_OK) {
                        Log.i(TAG, "send log success ");
//                      Log.i(TAG, "delete logFile " + sendFile.delete());
                        if (onSendLogListener != null) onSendLogListener.logSendSucc();
                    } else {
                        Log.e(TAG, "send log error " + statusCode);
                        if (onSendLogListener != null)
                            onSendLogListener.logSendFail(new Throwable("Server error " + statusCode));
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.e(TAG, "send log error " + e);
                    if (onSendLogListener != null)
                        onSendLogListener.logSendFail(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "send log error " + e);
                    if (onSendLogListener != null)
                        onSendLogListener.logSendFail(e);
                }
            }
        }).start();
    }

    private static HttpEntityEnclosingRequestBase getRequestBase(boolean put) {
        HttpEntityEnclosingRequestBase requestBase;
        String server;
        if (put) {
            server = "";
            requestBase = new HttpPut(server);
        } else {
            server = "/";
            requestBase = new HttpPost(server);
        }
        Log.d(TAG, "send log " + (put ? "HttpPut" : "HttpPost") + " server = " + server);
        return requestBase;
    }

    public static boolean deleteLogFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            Log.i(TAG, "deleteLogFile " + path);
            return file.delete();
        }
        return false;
    }

    private static class LogcatThread extends Thread {
        String path;

        /**
         * Constructs a new {@code Thread} with no {@code Runnable} object and a
         * newly generated name. The new {@code Thread} will belong to the same
         * {@code ThreadGroup} as the {@code Thread} calling this constructor.
         *
         * @see ThreadGroup
         * @see Runnable
         */
        public LogcatThread(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            super.run();
            try {
                Runtime.getRuntime().exec("logcat -v threadtime -t " + maxLine + " -f " + path);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

}

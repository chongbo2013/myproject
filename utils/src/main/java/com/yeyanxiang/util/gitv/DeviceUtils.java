package com.yeyanxiang.util.gitv;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DeviceUtils {

    /**
     * Get version code
     *
     * @param ctx
     * @return
     */
    public static int getVersionCode(Context ctx) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Throwable t) {
            return 0;
        }
    }

    /**
     * Get version name
     *
     * @param ctx
     * @return
     */
    public static String getVersionName(Context ctx) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionName;
        } catch (Throwable t) {
            return "";
        }
    }

    public static String getVersionNameFromApk(Context context, String filePath) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            LogUtils.logi("DeviceUtils", "getVersionNameFromApk " + packageInfo);
            return packageInfo.versionName;
        } catch (Throwable t) {
            return "";
        }
    }

    /**
     * Ger os version info
     *
     * @return
     */
    public static String getOSVersionInfo() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Get mac address
     *
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = getStringFromFile("/sys/class/net/eth0/address");

        if (TextUtils.isEmpty(mac)) {
            mac = getStringFromFile("/sys/class/net/wlan0/address");
        }
        if (TextUtils.isEmpty(mac)) {
            mac = getWifiMacAddress(context);
        }
        return mac;
    }

    private static String getWifiMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    private static String getStringFromFile(String path) {
        Reader reader = null;
        StringBuffer sbuffer = new StringBuffer();
        try {
            char[] buffer = new char[20];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(path));
            while ((charread = reader.read(buffer)) != -1) {
                if ((charread == buffer.length) && (buffer[buffer.length - 1] != '\r')) {
                    System.out.print(buffer);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (buffer[i] == '\r') {
                            continue;
                        } else {
                            sbuffer.append(buffer[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                }
            }
        }
        return sbuffer.toString().trim();
    }

    /**
     * Get local ip
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            String ipv4;
            List<NetworkInterface> list = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : list) {
                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.loge("getLocalIP", ex.toString());
        }
        return "";
    }

    /**
     * Get dns
     *
     * @return
     */
    public static String getDNS() {
        String dns = "";
        Process process = null;
        try {
            process = new ProcessBuilder("getprop").start();
            Properties pr = new Properties();
            pr.load(process.getInputStream());
            dns = pr.getProperty("[net.dns1]", "");
            if (!TextUtils.isEmpty(dns) && dns.length() > 6) {
                dns = dns.substring(1, dns.length() - 1);
                return dns;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }

        return dns;
    }

    /**
     * Get resolution
     *
     * @return
     */
    public static String getResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        String resolution = dm.widthPixels + "*" + dm.heightPixels;
        return resolution;
    }

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (className.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}

package com.yeyanxiang.project.xmpp;

import android.util.Log;

import com.yeyanxiang.project.config.Config;
import com.yeyanxiang.util.SharedPfsUtil;

import org.jivesoftware.smack.ConnectionListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 连接监听类
 *
 * @author Administrator
 */
public class TaxiConnectionListener implements ConnectionListener {
    private Timer tExit;
    private String username;
    private String password;
    private int loginTime = 2000;
    private SharedPfsUtil sharedPfsUtil;

    public TaxiConnectionListener() {
        super();
        // TODO Auto-generated constructor stub
        sharedPfsUtil = new SharedPfsUtil(FormLogin.context, Config.USER_DATA);
    }

    @Override
    public void connectionClosed() {
        Log.i("TaxiConnectionListener", "連接關閉");
        // 關閉連接
        XmppUtil.getInstance().closeConnection();
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new timetask(), loginTime);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.i("TaxiConnectionListener", "連接關閉異常");
        // 判斷為帳號已被登錄
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            // 關閉連接
            XmppUtil.getInstance().closeConnection();
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new timetask(), loginTime);
        }
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            username = sharedPfsUtil.getValue("account", "");
            password = sharedPfsUtil.getValue("password", "");
            if (username != null && password != null) {
                Log.i("TaxiConnectionListener", "嘗試登錄");
                // 连接服务器
                if (XmppUtil.getInstance().login(username, password)) {
                    Log.i("TaxiConnectionListener", "登錄成功");
                } else {
                    Log.i("TaxiConnectionListener", "重新登錄");
                    tExit.schedule(new timetask(), loginTime);
                }
            }
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
    }

    @Override
    public void reconnectionSuccessful() {
    }

}

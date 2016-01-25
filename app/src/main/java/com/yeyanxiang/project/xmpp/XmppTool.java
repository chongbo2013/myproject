package com.yeyanxiang.project.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.animation.AnimatorSet.Builder;

public class XmppTool {

	private static XMPPConnection con = null;
	private static String serverip = "198.9.9.188";
	private static int serverport = 5222;

	private static void openConnection() {
		try {
			// url、端口，也可以设置连接的服务器名字，地址，端口，用户。
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					serverip, serverport);
			con = new XMPPConnection(connConfig);
			con.connect();
			System.out.println(serverip + "---------" + serverport);
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
	}

	public static String getServerip() {
		return serverip;
	}

	public static void setServerip(String serverip) {
		XmppTool.serverip = serverip;
	}

	public static int getServerport() {
		return serverport;
	}

	public static void setServerport(int serverport) {
		XmppTool.serverport = serverport;
		openConnection();
	}

	public static XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
		con = null;
	}
}

package com.yeyanxiang.project.phoneinfo;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.yeyanxiang.util.HttpUtil;
import com.yeyanxiang.util.PhoneInfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月13日
 * 
 * @简介
 */
public class PhoneInfoActivity extends Activity {
	private final String TAG = "PhoneInfo";
	private SpannableStringBuilder stytle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ScrollView scrollView = new ScrollView(this);
		scrollView.setBackgroundColor(Color.WHITE);
		LinearLayout linearLayout = new LinearLayout(this);

		TextView textView = new TextView(this);
		textView.setTextSize(20);
		textView.setTextColor(Color.BLUE);
		textView.getPaint().setFakeBoldText(true);
		textView.setPadding(20, 0, 0, 0);

		linearLayout.addView(textView);
		scrollView.addView(linearLayout);
		setContentView(scrollView);

		stytle = new SpannableStringBuilder();

		String cpuabi = android.os.Build.CPU_ABI;
		int numcores = getNumCores();
		if (numcores > 0) {
			cpuabi += "(" + getNumCores() + "核)";
		}

		append("BOARD", android.os.Build.BOARD);
		append("BOOTLOADER", android.os.Build.BOOTLOADER);
		append("BRAND", android.os.Build.BRAND);
		append("CPU_ABI", cpuabi);
		append("CPU_ABI2", android.os.Build.CPU_ABI2);
		append("DEVICE", android.os.Build.DEVICE);
		append("DISPLAY", android.os.Build.DISPLAY);
		append("FINGERPRINT", android.os.Build.FINGERPRINT);
		append("HARDWARE", android.os.Build.HARDWARE);
		append("HOST", android.os.Build.HOST);
		append("ID", android.os.Build.ID);
		append("MANUFACTURER", android.os.Build.MANUFACTURER);
		append("MODEL", android.os.Build.MODEL);
		append("PRODUCT", android.os.Build.PRODUCT);
		append("RADIO", android.os.Build.RADIO);
		if (android.os.Build.VERSION.SDK_INT >= 14) {
			append("SERIAL", android.os.Build.SERIAL);
		}
		append("TAGS", android.os.Build.TAGS);
		append("TYPE", android.os.Build.TYPE);
		append("USER", android.os.Build.USER);
		append("TIME",
				new SimpleDateFormat("yyyy-MM-DD HH:mm:ss").format(new Date(
						android.os.Build.TIME)));
		append("CODENAME", android.os.Build.VERSION.CODENAME);
		append("INCREMENTAL", android.os.Build.VERSION.INCREMENTAL);
		append("RELEASE", android.os.Build.VERSION.RELEASE);
		append("SDK", android.os.Build.VERSION.SDK);
		append("SDK_INT", android.os.Build.VERSION.SDK_INT + "");
		append("BASE", android.os.Build.VERSION_CODES.BASE + "");
		append("BASE_1_1", android.os.Build.VERSION_CODES.BASE_1_1 + "");
		append("CUPCAKE", android.os.Build.VERSION_CODES.CUPCAKE + "");
		append("CUR_DEVELOPMENT",
				android.os.Build.VERSION_CODES.CUR_DEVELOPMENT + "");
		append("DONUT", android.os.Build.VERSION_CODES.DONUT + "");
		append("ECLAIR", android.os.Build.VERSION_CODES.ECLAIR + "");
		append("ECLAIR_0_1", android.os.Build.VERSION_CODES.ECLAIR_0_1 + "");
		append("ECLAIR_MR1", android.os.Build.VERSION_CODES.ECLAIR_MR1 + "");
		append("FROYO", android.os.Build.VERSION_CODES.FROYO + "");
		append("GINGERBREAD", android.os.Build.VERSION_CODES.GINGERBREAD + "");
		append("GINGERBREAD_MR1",
				android.os.Build.VERSION_CODES.GINGERBREAD_MR1 + "");
		append("HONEYCOMB", android.os.Build.VERSION_CODES.HONEYCOMB + "");
		append("HONEYCOMB_MR1", android.os.Build.VERSION_CODES.HONEYCOMB_MR1
				+ "");
		append("HONEYCOMB_MR2", android.os.Build.VERSION_CODES.HONEYCOMB_MR2
				+ "");
		append("ICE_CREAM_SANDWICH",
				android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH + "");
		append("ICE_CREAM_SANDWICH_MR1",
				android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 + "");
		append("JELLY_BEAN", android.os.Build.VERSION_CODES.JELLY_BEAN + "");
		append("JELLY_BEAN_MR1", android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
				+ "");
		append("JELLY_BEAN_MR2", android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
				+ "");
		append("KITKAT", android.os.Build.VERSION_CODES.KITKAT + "");

		append("CPUInfo", PhoneInfo.getCpuInfo());
		append("内存使用情况", "总内存：" + PhoneInfo.getTotalMemory(this) + " \t 可用内存："
				+ PhoneInfo.getAvailMemory(this));
		append("序列号", PhoneInfo.getImei(this));

		String netstate = "";
		if (HttpUtil.IsNetworkConnect(this)) {
			if (HttpUtil.IsWifiConnect(this)) {
				netstate = "wifi连接";
			} else {
				netstate = "3G或其它方式连接";
			}
		} else {
			netstate = "未连接";
		}

		append("网络连接状态", netstate);

		append("本地IP(可用来取3G或机顶盒IP)", PhoneInfo.getLocalIpAddress());
		append("本地IP(可用来取Wifi模式下的IP)",
				PhoneInfo.getLocalIpAddressFromWifi(this));
		append("Mac地址", PhoneInfo.getLocalMacAddress(this));
		append("存储信息", PhoneInfo.getSDCardInfo());

		textView.setText(stytle);

	}

	private void append(String string, String value) {
		if (value.contains("\n")) {
			value = value.replace("\n", "\n\t\t");
		}
		stytle.append("\n" + string + "：\n\t\t" + value + "\n");
		String parsestring = stytle.toString();
		// 用颜色标记字体
		stytle.setSpan(new ForegroundColorSpan(Color.BLACK),
				parsestring.indexOf(string) + string.length() + 1,
				parsestring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
		stytle.setSpan(new RelativeSizeSpan(0.8f), parsestring.indexOf(string)
				+ string.length(), parsestring.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 属性值字体正常
		stytle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL),
				parsestring.indexOf(string) + string.length(),
				parsestring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	private int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			Log.d(TAG, "CPU Count: " + files.length);
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			// Print exception
			Log.d(TAG, "CPU Count: Failed.");
			e.printStackTrace();
			// Default to return 1 core
			return 0;
		}
	}

}

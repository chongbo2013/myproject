package com.yeyanxiang.project.mediaplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.yeyanxiang.project.R;

import android.os.Handler;

/**
 * 
 * Create on 2013-5-7 上午10:22:17 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 文件检索工具
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public final class FileUtil {
	public static String[] videoSuffixes = new String[] { "3gp", "mp4", "flv",
			"avi", "mov", "wmv", "rmvb", "rm", "asf" };

	public static String[] audioSuffixes = new String[] { "mp3", "wav", "wma",
			"ape" };

	public static String[] imageSuffixes = new String[] { "png", "gif", "jpeg",
			"jpg", "bmp" };

	public static String[] docSuffixes = new String[] { "txt", "doc", "docx",
			"pdf" };

	public static void filterfile(String filepath, Handler handler,
			ArrayList<HashMap<String, Object>> filearray) {
		File[] files = new File(filepath).listFiles();
		try {
			for (File file : files) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (file.isDirectory()) {
					map.put("picid", R.drawable.folder);
					map.put("file", file);
					filearray.add(map);
					handler.sendEmptyMessage(0);
				} else {
					if (isInType(file.getAbsolutePath(), imageSuffixes)) {
						map.put("picid", R.drawable.pic);
						map.put("file", file);
						filearray.add(map);
						handler.sendEmptyMessage(0);
					} else if (isInType(file.getAbsolutePath(), videoSuffixes)) {
						map.put("picid", R.drawable.videoicon);
						map.put("file", file);
						filearray.add(map);
						handler.sendEmptyMessage(0);
					} else if (isInType(file.getAbsolutePath(), audioSuffixes)) {
						map.put("picid", R.drawable.audioicon);
						map.put("file", file);
						filearray.add(map);
						handler.sendEmptyMessage(0);
					} else {
						map.put("picid", R.drawable.otherpic);
						map.put("file", file);
						filearray.add(map);
						handler.sendEmptyMessage(0);
					}
				}
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

	}

	public static boolean isInType(String path, String[] suffixes) {
		final String end = path.substring(path.lastIndexOf(".") + 1,
				path.length()).toLowerCase();
		for (int i = 0, length = suffixes.length; i < length; i++) {
			if (end.equals(suffixes[i]))
				return true;
		}
		return false;
	}
}

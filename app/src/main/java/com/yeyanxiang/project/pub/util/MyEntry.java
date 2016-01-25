package com.yeyanxiang.project.pub.util;

import android.content.Intent;

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
public class MyEntry {
	private String key;
	private Class<?> value;
	private Intent intent;
	private boolean isapp;

	public MyEntry(String key, Class<?> value) {
		super();
		this.key = key;
		this.value = value;
	}

	public MyEntry(String key, Intent intent) {
		super();
		this.key = key;
		this.intent = intent;
	}

	public MyEntry(String key, Intent intent, boolean isapp) {
		super();
		this.key = key;
		this.intent = intent;
		this.isapp = isapp;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Class<?> getValue() {
		return value;
	}

	public void setValue(Class<?> value) {
		this.value = value;
	}

	public boolean Isapp() {
		return isapp;
	}

	public void setIsapp(boolean isapp) {
		this.isapp = isapp;
	}

	@Override
	public String toString() {
		return "MyEntry [key=" + key + ", value=" + value + ", intent="
				+ intent + ", isapp=" + isapp + "]";
	}
}

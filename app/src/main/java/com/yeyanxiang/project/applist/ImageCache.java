package com.yeyanxiang.project.applist;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @data 2014年1月12日 下午4:07:19
 * 
 * @简介
 */
public class ImageCache {
	private LoadThread thread;
	private Handler handler;
	public static final int NOTIFY = 20000;
	private int defaultpicid;
	private PackageManager pm;

	public ImageCache(Handler handler, int defaultpicid, PackageManager pm) {
		super();
		this.handler = handler;
		this.defaultpicid = defaultpicid;
		this.pm = pm;
	}

	// 软引用
	private final int softcachesize = 80;
	private final LinkedHashMap<String, SoftReference<APPRes>> imgLinkedHashMap = new LinkedHashMap<String, SoftReference<APPRes>>(
			softcachesize, 0.75f, true) {

		@Override
		public SoftReference<APPRes> get(Object key) {
			// TODO Auto-generated method stub
			return super.get(key);
		}

		@Override
		public boolean containsValue(Object value) {
			// TODO Auto-generated method stub
			return super.containsValue(value);
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			super.clear();
		}

		@Override
		protected boolean removeEldestEntry(
				Entry<String, SoftReference<APPRes>> eldest) {
			// TODO Auto-generated method stub
			return super.removeEldestEntry(eldest);
		}

		@Override
		public SoftReference<APPRes> put(String key, SoftReference<APPRes> value) {
			// TODO Auto-generated method stub
			return super.put(key, value);
		}
	};

	private final int hardcachesize = 8 * 1024 * 1024;

	private final LruCache<String, APPRes> lruCache = new LruCache<String, APPRes>(
			hardcachesize) {

		@Override
		protected APPRes create(String key) {
			// TODO Auto-generated method stub
			return super.create(key);
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				APPRes oldValue, APPRes newValue) {
			// TODO Auto-generated method stub
			super.entryRemoved(evicted, key, oldValue, newValue);
			// 硬缓存区已存满，将一个老的图片存到软缓存区中
			imgLinkedHashMap.put(key, new SoftReference<APPRes>(oldValue));
		}

		@Override
		protected int sizeOf(String key, APPRes value) {
			// TODO Auto-generated method stub
			return super.sizeOf(key, value);
		}
	};

	// 将图片加入缓存中
	public boolean putAPPRes(String key, APPRes value) {
		if (value != null) {
			synchronized (lruCache) {
				lruCache.put(key, value);
				return true;
			}
		}
		return false;
	}

	// 从缓存中取图片

	public APPRes getAppRes(String key) {
		synchronized (lruCache) {
			APPRes appRes = lruCache.get(key);
			if (appRes != null) {
				return appRes;
			}
		}
		// 硬缓存区得不到图片，从软缓存区取
		synchronized (imgLinkedHashMap) {
			SoftReference<APPRes> softReference = imgLinkedHashMap.get(key);
			if (softReference != null) {
				APPRes appres = softReference.get();
				if (appres != null) {
					return appres;
				}
			}
		}
		return null;
	}

	public void release() {
		if (lruCache != null) {
			lruCache.evictAll();
		}
		if (imgLinkedHashMap != null) {
			for (Entry<String, SoftReference<APPRes>> entry : imgLinkedHashMap
					.entrySet()) {
				Bitmap bitmap = entry.getValue().get().getBitmap();
				if (bitmap != null) {
					bitmap.recycle();
				}
			}
			imgLinkedHashMap.clear();
		}
	}

	public APPRes setbm(String key, PackageInfo packageInfo) {
		APPRes appRes = new APPRes();
		Bitmap bitmap = ((BitmapDrawable) packageInfo.applicationInfo
				.loadIcon(pm)).getBitmap();
		if (bitmap != null) {
			appRes.setBitmap(bitmap);
		}

		String name = packageInfo.applicationInfo.loadLabel(pm).toString()
				.trim();
		appRes.setName(name);
		putAPPRes(key, appRes);
		return appRes;
	}

	public void loadBitmap(ViewHolder viewHolder) {
		String key = null;
		try {
			key = viewHolder.getPackageInfo().packageName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(key)) {
			APPRes appRes = getAppRes(key);
			if (appRes != null) {
				viewHolder.appicon.setImageBitmap(appRes.getBitmap());
				viewHolder.appname.setText(getAppRes(key).getName());
			} else {
				viewHolder.appicon.setImageResource(defaultpicid);
				if (thread == null) {
					thread = new LoadThread();
					thread.addTast(key, viewHolder);
					thread.start();
				} else {
					thread.addTast(key, viewHolder);
				}
			}
		}
	}

	private class LoadThread extends Thread {
		private LinkedHashMap<String, ViewHolder> taskMap;
		private boolean iswait;

		public LoadThread() {
			super();
			iswait = false;
			taskMap = new LinkedHashMap<String, ViewHolder>();
		}

		private void addTast(String key, ViewHolder imageView) {
			// 任务链中可能有，得先删除
			taskMap.remove(key);
			taskMap.put(key, imageView);
			// 如果线程此时处于等待得唤醒线程去处理任务队列中待处理的任务
			if (iswait) {
				// 调用对象的notify()时必须同步
				synchronized (this) {
					this.notify();
				}
			}
		}

		@Override
		public void run() {
			Looper.prepare();
			if (taskMap != null) {
				while (taskMap.size() > 0) {
					iswait = false;
					try {
						final String key = taskMap.keySet().iterator().next();
						final ViewHolder viewholder = taskMap.remove(key);
						if (getAppRes(key) != null) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									viewholder.appicon
											.setImageBitmap(getAppRes(key)
													.getBitmap());
									viewholder.appname.setText(getAppRes(key)
											.getName());
									handler.sendEmptyMessage(NOTIFY);
								}
							});
						} else {
							final APPRes appRes = setbm(key,
									viewholder.getPackageInfo());
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (appRes.getBitmap() != null) {
										viewholder.appicon
												.setImageBitmap(appRes
														.getBitmap());
										viewholder.appname.setText(appRes
												.getName());
									}
									handler.sendEmptyMessage(NOTIFY);
								}
							});
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					// 当任务队列中没有待处理的任务时，线程进入等待状态
					if (taskMap.isEmpty()) {
						iswait = true;
						synchronized (this) {
							try {
								this.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}

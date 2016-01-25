package com.yeyanxiang.project.mediaplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

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
public class FileListActivity extends Activity {
	private Context context;
	private ListView listView;
	private FileListadapter adapter;
	private String currentpath = "/mnt/sdcard";
	private ProgressDialog progressDialog;
	private LayoutAnimationController controller;
	private List<HashMap<String, Object>> list;
	private List<HashMap<String, Object>> searchlist;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				searchEditText.setText(null);
				listView.setAdapter(adapter);
				listView.setLayoutAnimation(controller);
				if (progressDialog != null) {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelist);
		context = this;
		init();
		currentpath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		progressDialog = ProgressDialog.show(context, null, "加载中...");
		new FilterTask().execute("");
	}

	private EditText searchEditText;

	private void init() {
		controller = PubUtil.getListAnim();
		listView = (ListView) findViewById(R.id.filelist);
		searchEditText = (EditText) findViewById(R.id.function);
		listView.setCacheColorHint(0);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(onItemClickListener);

		list = new ArrayList<HashMap<String, Object>>();
		searchlist = new ArrayList<HashMap<String, Object>>();
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String search = s.toString().trim().toLowerCase();
				if (TextUtils.isEmpty(search)) {
					adapter = new FileListadapter(context, list, null);
				} else {
					searchlist.clear();
					for (int i = 0; i < list.size(); i++) {
						if (((File) list.get(i).get("file")).getName()
								.toLowerCase().contains(search)) {
							searchlist.add(list.get(i));
						}
					}
					adapter = new FileListadapter(context, searchlist, search);
				}
				listView.setAdapter(adapter);
			}
		});
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			// TODO Auto-generated method stub
			final File file = (File) adapter.getItem(position).get("file");
			if (file.canRead()) {
				final String filepath = file.getAbsolutePath();
				if (file.isDirectory()) {
					if (file.listFiles().length > 0) {
						currentpath = file.getAbsolutePath();
						progressDialog.show();
						new FilterTask().execute("");
					} else {
						PubUtil.showText(context, "空文件夹");
					}
					// new AlertDialog.Builder(context)
					// .setItems(new String[] { "打开", "删除" },
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(
					// DialogInterface dialog,
					// int which) {
					// // TODO Auto-generated method
					// // stub
					// switch (which) {
					// case 0:
					// if (file.listFiles().length > 0) {
					// currentpath = file
					// .getAbsolutePath();
					// filearray.clear();
					// handler.sendEmptyMessage(0);
					// progressDialog.show();
					// new FilterTask()
					// .execute(new String[] {});
					// } else {
					// PubUtil.showText(context,
					// "空文件夹");
					// }
					// break;
					// case 1:
					// file.delete();
					// filearray.remove(position);
					// adapter.notifyDataSetChanged();
					// break;
					// default:
					// break;
					// }
					// }
					// }).create().show();
				} else {
					final int picid = (Integer) adapter.getItem(position).get(
							"picid");
					if (picid == R.drawable.pic) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse("file://" + filepath),
								"image/*");
						startActivity(intent);
					} else if (picid == R.drawable.videoicon
							|| picid == R.drawable.audioicon) {
						new AlertDialog.Builder(context)
								.setItems(
										new String[] { "默认播放器",
												"MediaPlayer 播放", "Vitamio 播放" },
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												switch (which) {
												case 0:
													Intent intent = new Intent(
															Intent.ACTION_VIEW);
													intent.setDataAndType(
															Uri.parse("file://"
																	+ filepath),
															getfileType(filepath
																	.substring(filepath
																			.lastIndexOf(".") + 1)));
													startActivity(intent);
													break;
												case 1:
													Intent intent2 = new Intent(
															context,
															MediaPlayerActivity.class);
													intent2.putExtra(
															MediaPlayerActivity.MEDIA_PATH,
															filepath);
													if (picid == R.drawable.audioicon) {
														intent2.putExtra(
																MediaPlayerActivity.MEDIA_TYPE,
																"audio");
													} else if (picid == R.drawable.videoicon) {
														intent2.putExtra(
																MediaPlayerActivity.MEDIA_TYPE,
																"video");
													} else {
														intent2.putExtra(
																MediaPlayerActivity.MEDIA_TYPE,
																"other");
													}
													startActivity(intent2);
													break;
												case 2:

													if (picid == R.drawable.audioicon) {
														Intent intent3 = new Intent(
																context,
																VitamioAudioPlayerActivity.class);
														intent3.putExtra(
																VitamioAudioPlayerActivity.MEDIA_PATH,
																filepath);
														startActivity(intent3);
													} else if (picid == R.drawable.videoicon) {
														Intent intent3 = new Intent(
																context,
																VitamioVideoPlayerActivity.class);
														intent3.putExtra(
																VitamioVideoPlayerActivity.VIDEOSTREAM,
																filepath);
														startActivity(intent3);
													}
													break;
												case 4:
													file.delete();
													adapter.remove(position);
													break;
												default:
													break;
												}
											}
										}).create().show();
					} else {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse("file://" + filepath),
								getfileType(filepath.substring(filepath
										.lastIndexOf(".") + 1)));
						startActivity(intent);
					}
				}
			} else {
				PubUtil.showText(context, "权限不足");
			}
		}
	};

	private String getfileType(String end) {
		String type = "*/*";
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		type = mimeTypeMap.getMimeTypeFromExtension(end);
		if (type == null) {
			type = "*/*";
		}
		return type;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (Environment.getExternalStorageDirectory().getAbsolutePath()
				.equals(currentpath)) {
			super.onBackPressed();
			return;
		}
		File file = new File(currentpath);
		if (file.getParentFile() != null) {
			currentpath = file.getParentFile().getAbsolutePath();
			progressDialog.show();
			new FilterTask().execute("");
		} else {
			super.onBackPressed();
		}
	}

	public static String[] videoSuffixes = new String[] { "3gp", "mp4", "flv",
			"avi", "mov", "wmv", "rmvb", "rm", "asf" };

	public static String[] audioSuffixes = new String[] { "mp3", "wav", "wma",
			"ape" };

	public static String[] imageSuffixes = new String[] { "png", "gif", "jpeg",
			"jpg", "bmp" };

	public static String[] docSuffixes = new String[] { "txt", "doc", "docx",
			"pdf" };

	private class FilterTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<HashMap<String, Object>> filearray = new ArrayList<HashMap<String, Object>>();
			File[] files = new File(currentpath).listFiles();
			try {
				for (File file : files) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					if (file.getName().length() > 1) {
						if (".".equals(file.getName().substring(0, 1))) {
							continue;
						}
					}
					map.put("file", file);
					if (file.isDirectory()) {
						map.put("picid", R.drawable.folder);
					} else {
						if (isInType(file.getAbsolutePath(), imageSuffixes)) {
							map.put("picid", R.drawable.pic);
						} else if (isInType(file.getAbsolutePath(),
								videoSuffixes)) {
							map.put("picid", R.drawable.videoicon);
						} else if (isInType(file.getAbsolutePath(),
								audioSuffixes)) {
							map.put("picid", R.drawable.audioicon);
						} else {
							map.put("picid", R.drawable.otherpic);
						}
					}
					filearray.add(map);
				}
				list = filearray;
				adapter = new FileListadapter(context, filearray, null);
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			handler.sendEmptyMessage(0);
		}

		public boolean isInType(String path, String[] suffixes) {
			final String end = path.substring(path.lastIndexOf(".") + 1,
					path.length()).toLowerCase();
			for (int i = 0, length = suffixes.length; i < length; i++) {
				if (end.equals(suffixes[i]))
					return true;
			}
			return false;
		}

	}
}

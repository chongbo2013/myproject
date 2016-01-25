package com.yeyanxiang.project.xmpp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FormClient extends Activity {

	private MyAdapter adapter;
	private List<Msg> listMsg = new ArrayList<Msg>();
	private String pUSERID;
	private EditText msgText;
	private ProgressBar pb;
	private String SERVER = "198.9.9.188";

	public class Msg {
		String userid;
		String msg;
		String date;
		String from;

		public Msg(String userid, String msg, String date, String from) {
			this.userid = userid;
			this.msg = msg;
			this.date = date;
			this.from = from;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formclient);

		// 获取Intent传过来的用户名
		this.pUSERID = getIntent().getStringExtra("USERID");

		ListView listview = (ListView) findViewById(R.id.formclient_listview);
		listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		this.adapter = new MyAdapter(this);
		listview.setAdapter(adapter);

		// 获取文本信息
		this.msgText = (EditText) findViewById(R.id.formclient_text);
		this.pb = (ProgressBar) findViewById(R.id.formclient_pb);

		// 消息监听
		ChatManager cm = XmppTool.getConnection().getChatManager();
		// 发送消息给SERVER服务器water（获取自己的服务器，和好友）
		// final Chat newchat = cm.createChat(this.pUSERID+"@SERVER", null);
		final Chat newchat = cm.createChat("student1@" + SERVER, null);
		final Chat newchat1 = cm.createChat("jetsen22@" + SERVER, null);
		final Chat newchat2 = cm.createChat("student2@" + SERVER, null);
		final Chat newchat3 = cm.createChat("admin@" + SERVER, null);
		// final Chat newchat2 = cm.createChat("huang@yeyanxiang-pc", null);

		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message) {
						Log.v("--tags--", "--tags-form--" + message.getFrom());
						Log.v("--tags--",
								"--tags-message--" + message.getBody());
						// 收到来自SERVER服务器water的消息（获取自己的服务器，和好友）
						if (message.getFrom().contains(pUSERID + "@" + SERVER)) {
							// 获取用户、消息、时间、IN
							String[] args = new String[] { pUSERID,
									message.getBody(), TimeRender.getDate(),
									"IN" };

							// 在handler里取出来显示消息
							android.os.Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = args;
							msg.sendToTarget();
						} else {
							// message.getFrom().cantatins(获取列表上的用户，组，管理消息);
							// 获取用户、消息、时间、IN
							String[] args = new String[] { message.getFrom(),
									message.getBody(), TimeRender.getDate(),
									"IN" };

							// 在handler里取出来显示消息
							android.os.Message msg = handler.obtainMessage();
							msg.what = 1;
							msg.obj = args;
							msg.sendToTarget();
						}
					}
				});
			}
		});

		// 附件
		Button btattach = (Button) findViewById(R.id.formclient_btattach);
		btattach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FormClient.this, FormFiles.class);
				startActivityForResult(intent, 2);
			}
		});
		// 发送消息
		Button btsend = (Button) findViewById(R.id.formclient_btsend);
		btsend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取text文本
				String msg = msgText.getText().toString();

				if (msg.length() > 0) {
					// 发送消息
					listMsg.add(new Msg(pUSERID, msg, TimeRender.getDate(),
							"OUT"));
					// 刷新适配器
					adapter.notifyDataSetChanged();

					try {
						// 发送消息给xiaowang
						newchat.sendMessage(msg);
						newchat1.sendMessage(msg);
						newchat2.sendMessage(msg);
						newchat3.sendMessage(msg);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(FormClient.this, "请输入信息", Toast.LENGTH_SHORT)
							.show();
				}
				// 清空text
				msgText.setText("");
			}
		});

		// 接受文件
		FileTransferManager fileTransferManager = new FileTransferManager(
				XmppTool.getConnection());
		fileTransferManager
				.addFileTransferListener(new RecFileTransferListener());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 发送附件
		if (requestCode == 2 && resultCode == 2 && data != null) {

			String filepath = data.getStringExtra("filepath");
			if (filepath.length() > 0) {
				sendFile(filepath);
			}
		}
	}

	private void sendFile(String filepath) {
		// ServiceDiscoveryManager sdm = new
		// ServiceDiscoveryManager(connection);

		final FileTransferManager fileTransferManager = new FileTransferManager(
				XmppTool.getConnection());
		// 发送给SERVER服务器，water（获取自己的服务器，和好友）
		// final OutgoingFileTransfer fileTransfer = fileTransferManager
		// .createOutgoingFileTransfer(this.pUSERID + "@" + SERVER
		// + "/Spark 2.6.3");
		final OutgoingFileTransfer fileTransfer = fileTransferManager
				.createOutgoingFileTransfer("admin@" + SERVER + "/Spark 2.6.3");

		final File file = new File(filepath);

		try {
			fileTransfer.sendFile(file, "Sending");
		} catch (Exception e) {
			Toast.makeText(FormClient.this, "发送失败!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					while (true) {
						Thread.sleep(500L);

						Status status = fileTransfer.getStatus();
						if ((status == FileTransfer.Status.error)
								|| (status == FileTransfer.Status.complete)
								|| (status == FileTransfer.Status.cancelled)
								|| (status == FileTransfer.Status.refused)) {
							handler.sendEmptyMessage(4);
							break;
						} else if (status == FileTransfer.Status.negotiating_transfer) {
							// ..
						} else if (status == FileTransfer.Status.negotiated) {
							// ..
						} else if (status == FileTransfer.Status.initial) {
							// ..
						} else if (status == FileTransfer.Status.negotiating_stream) {
							// ..
						} else if (status == FileTransfer.Status.in_progress) {
							// 进度条显示
							handler.sendEmptyMessage(2);

							long p = fileTransfer.getBytesSent() * 100L
									/ fileTransfer.getFileSize();

							android.os.Message message = handler
									.obtainMessage();
							message.arg1 = Math.round((float) p);
							message.what = 3;
							message.sendToTarget();
							Toast.makeText(FormClient.this, "发送成功!",
									Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(FormClient.this, "发送失败!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}).start();
	}

	private FileTransferRequest request;
	private File file;

	class RecFileTransferListener implements FileTransferListener {
		@Override
		public void fileTransferRequest(FileTransferRequest prequest) {
			// 接受附件
			// System.out.println("The file received from: " +
			// prequest.getRequestor());

			file = new File("mnt/sdcard/" + prequest.getFileName());
			System.out.println(file.getAbsolutePath() + "----------------");
			request = prequest;
			handler.sendEmptyMessage(5);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 10:
				break;
			case 1:
				// 获取消息并显示
				String[] args = (String[]) msg.obj;
				listMsg.add(new Msg(args[0], args[1], args[2], args[3]));
				// 刷新适配器
				adapter.notifyDataSetChanged();
				break;
			case 2:
				// 附件进度条
				if (pb.getVisibility() == View.GONE) {
					pb.setMax(100);
					pb.setProgress(1);
					pb.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				pb.setProgress(msg.arg1);
				break;
			case 4:
				pb.setVisibility(View.GONE);
				break;
			case 5:
				final IncomingFileTransfer infiletransfer = request.accept();

				// 提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FormClient.this);

				builder.setTitle("附件：")
						.setCancelable(false)
						.setMessage("是否接收文件：" + file.getName() + "?")
						.setPositiveButton("接受",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											infiletransfer.recieveFile(file);
										} catch (XMPPException e) {
											Toast.makeText(FormClient.this,
													"接收失败!", Toast.LENGTH_SHORT)
													.show();
											e.printStackTrace();
										}

										handler.sendEmptyMessage(2);

										Timer timer = new Timer();
										TimerTask updateProgessBar = new TimerTask() {
											public void run() {
												if ((infiletransfer
														.getAmountWritten() >= request
														.getFileSize())
														|| (infiletransfer
																.getStatus() == FileTransfer.Status.error)
														|| (infiletransfer
																.getStatus() == FileTransfer.Status.refused)
														|| (infiletransfer
																.getStatus() == FileTransfer.Status.cancelled)
														|| (infiletransfer
																.getStatus() == FileTransfer.Status.complete)) {
													cancel();
													handler.sendEmptyMessage(4);
												} else {
													long p = infiletransfer
															.getAmountWritten()
															* 100L
															/ infiletransfer
																	.getFileSize();

													android.os.Message message = handler
															.obtainMessage();
													message.arg1 = Math
															.round((float) p);
													message.what = 3;
													message.sendToTarget();
													handler.sendEmptyMessage(10);
												}
											}
										};
										timer.scheduleAtFixedRate(
												updateProgessBar, 10L, 10L);
										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										request.reject();
										dialog.cancel();
									}
								}).show();
				break;
			default:
				break;
			}
		};
	};

	// 退出
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		XmppTool.closeConnection();
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	class MyAdapter extends BaseAdapter {

		private Context cxt;
		private LayoutInflater inflater;

		public MyAdapter(FormClient formClient) {
			this.cxt = formClient;
		}

		@Override
		public int getCount() {
			return listMsg.size();
		}

		@Override
		public Object getItem(int position) {
			return listMsg.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 显示消息的布局：内容、背景、用户、时间
			this.inflater = (LayoutInflater) this.cxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// IN,OUT的图片
			if (listMsg.get(position).from.equals("IN")) {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_in, null);
			} else {
				convertView = this.inflater.inflate(
						R.layout.formclient_chat_out, null);
			}

			TextView useridView = (TextView) convertView
					.findViewById(R.id.formclient_row_userid);
			TextView dateView = (TextView) convertView
					.findViewById(R.id.formclient_row_date);
			TextView msgView = (TextView) convertView
					.findViewById(R.id.formclient_row_msg);

			useridView.setText(listMsg.get(position).userid);
			dateView.setText(listMsg.get(position).date);
			msgView.setText(listMsg.get(position).msg);

			return convertView;
		}
	}
}
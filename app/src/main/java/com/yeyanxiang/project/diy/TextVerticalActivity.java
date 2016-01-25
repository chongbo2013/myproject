package com.yeyanxiang.project.diy;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.TextViewVertical;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年4月3日
 * 
 * @简介
 */
public class TextVerticalActivity extends Activity {
	private HorizontalScrollView sv;
	private TextViewVertical tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textverticallayout);

		tv = (TextViewVertical) findViewById(R.id.tv);
		sv = (HorizontalScrollView) findViewById(R.id.sv);

		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		tv.setTextSize(30 * dMetrics.density);

		// 设置接口事件接收
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case TextViewVertical.LAYOUT_CHANGED:
					sv.scrollBy(tv.getTextWidth(), 0);// 滚动到最右边
					// Toast.makeText(TestFontActivity.this, "位置",
					// Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		tv.setHandler(handler);// 将Handler绑定到TextViewVertical

		// 创建并设置字体（这里只是为了效果好看一些，但为了让网友们更容易下载，字体库并没有一同打包
		// 如果需要体验下效果的朋友可以自行在网络上搜索stxingkai.ttf并放入assets/fonts/中）
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/stxingkai.ttf");
		tv.setTypeface(face);

		// 设置文字内容
		tv.setText("满江红·怒发冲冠 南宋·岳飞\n\n怒发冲冠，凭栏处，潇潇雨歇。\n\n抬望眼，仰天长啸，壮怀激烈。\n\n三十功名尘与土，八千里路云和月。\n\n莫等闲，白了少年头，空悲切！\n\n靖康耻，犹未雪；臣子恨，何时灭？\n\n驾长车，踏破贺兰山缺。\n\n壮志饥餐胡虏肉，笑谈渴饮匈奴血。\n\n待从头，收拾旧山河，朝天阙！");
	}

}
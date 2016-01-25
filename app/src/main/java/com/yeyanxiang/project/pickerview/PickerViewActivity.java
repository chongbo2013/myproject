package com.yeyanxiang.project.pickerview;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.pickerview.ArrayWheelAdapter;
import com.yeyanxiang.view.pickerview.NumericWheelAdapter;
import com.yeyanxiang.view.pickerview.OnWheelChangedListener;
import com.yeyanxiang.view.pickerview.OnWheelScrollListener;
import com.yeyanxiang.view.pickerview.WheelView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
public class PickerViewActivity extends Activity implements
		OnWheelChangedListener, OnWheelScrollListener {
	private TextView textView;
	private WheelView province;
	private WheelView city;
	private WheelView password1;
	private WheelView password2;
	private WheelView password3;
	private WheelView password4;
	private Button mixButton;

	private String[] provinces = new String[] { "河南", "河北", "湖南", "湖北" };

	private String[][] cities = new String[][] {
			new String[] { "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作",
					"濮阳", "许昌", "漯河", "三门峡", "商丘", "周口", "驻马店", "南阳", "信阳",
					"济源", }, new String[] { "石家庄", "唐山", "秦皇岛", "承德", "张家口" },
			new String[] { "长沙 ", "常德", "株洲", "湘潭", "邵东" },
			new String[] { "武汉", "武昌", "黄石", "襄阳", "宜昌" }, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pickerviewmain);

		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		textView = (TextView) findViewById(R.id.pickercitytext);
		mixButton = (Button) findViewById(R.id.pickerbtn_mix);
		province = (WheelView) findViewById(R.id.pickerprovince);
		city = (WheelView) findViewById(R.id.pickercity);
		password1 = (WheelView) findViewById(R.id.pickerpassw_1);
		password2 = (WheelView) findViewById(R.id.pickerpassw_2);
		password3 = (WheelView) findViewById(R.id.pickerpassw_3);
		password4 = (WheelView) findViewById(R.id.pickerpassw_4);

		password1.setTextSize(60);
		password2.setTextSize(60);
		password3.setTextSize(60);
		password4.setTextSize(60);

		province.setAdapter(new ArrayWheelAdapter(provinces));

		password1.setAdapter(new NumericWheelAdapter(0, 9));
		password1.setCurrentItem((int) (Math.random() * 10));

		password2.setAdapter(new NumericWheelAdapter(0, 9));
		password2.setCurrentItem((int) (Math.random() * 10));

		password3.setAdapter(new NumericWheelAdapter(0, 9));
		password3.setCurrentItem((int) (Math.random() * 10));

		password4.setAdapter(new NumericWheelAdapter(0, 9));
		password4.setCurrentItem((int) (Math.random() * 10));

		mixButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				password1.scroll(-25 + (int) (Math.random() * 50), 2000);
				password2.scroll(-25 + (int) (Math.random() * 50), 2000);
				password3.scroll(-25 + (int) (Math.random() * 50), 2000);
				password4.scroll(-25 + (int) (Math.random() * 50), 2000);
			}
		});

		province.addChangingListener(this);
		city.addChangingListener(this);
		password1.addChangingListener(this);
		password2.addChangingListener(this);
		password3.addChangingListener(this);
		password4.addChangingListener(this);

		province.addScrollingListener(this);
		province.setCurrentItem(0);
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		// TODO Auto-generated method stub
		System.out.println("--------start----------");
	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		// TODO Auto-generated method stub
		System.out.println("--------finish----------");
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		switch (wheel.getId()) {
		case R.id.pickerprovince:
			city.setAdapter(new ArrayWheelAdapter(cities[newValue]));
			textView.setText(province.getAdapter().getItem(newValue) + ","
					+ city.getAdapter().getItem(city.getCurrentItem()));
			break;
		case R.id.pickercity:
			textView.setText(province.getAdapter().getItem(
					province.getCurrentItem())
					+ "," + city.getAdapter().getItem(newValue));
			break;

		default:
			textView.setText(password1.getAdapter().getItem(
					password1.getCurrentItem())
					+ "\t"
					+ password2.getAdapter()
							.getItem(password2.getCurrentItem())
					+ "\t"
					+ password3.getAdapter()
							.getItem(password3.getCurrentItem())
					+ "\t"
					+ password4.getAdapter()
							.getItem(password4.getCurrentItem()) + "\t");
			break;
		}
	}
}

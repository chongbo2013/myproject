package com.yeyanxiang.project.mylist;

import java.util.List;
import com.yeyanxiang.project.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * 
 * Create on 2013-5-7 上午10:25:45 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介:分层list
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class ExpandableListViewActivity extends Activity {

	ExpandableListView expandableListView;
	TreeViewAdapter adapter;
	SuperTreeViewAdapter superAdapter;
	Button double_btn, treble_btn;

	public String[] groups = { "friends", "family" };
	public String[][] childs = { { "A", "AA", "AAA" }, { "B", "BB", "BBB" } };

	public String[] parent = { "boys", "girls" };
	public String[][][] child_grandchild = { { { "Toms" }, { "A", "AA" } },
			{ { "Kelly" }, { "B", "BBB" } } };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandablelist);
		double_btn = (Button) findViewById(R.id.button1);
		treble_btn = (Button) findViewById(R.id.button2);
		expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);
		double_btn.setOnClickListener(listener);
		treble_btn.setOnClickListener(listener);
		adapter = new TreeViewAdapter(this, 38);
		superAdapter = new SuperTreeViewAdapter(this, stvClickEvent);

	}

	public OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			adapter.removeAll();
			adapter.notifyDataSetChanged();
			superAdapter.RemoveAll();
			superAdapter.notifyDataSetChanged();
			if (v == double_btn) {
				List<TreeNode> treeNode = adapter.getTreeNode();
				for (int i = 0; i < groups.length; i++) {
					TreeNode node = new TreeNode();
					node.parent = groups[i];
					for (int j = 0; j < childs[i].length; j++) {
						node.childs.add(childs[i][j]);
					}
					treeNode.add(node);
				}
				adapter.updateTreeNode(treeNode);
				expandableListView.setAdapter(adapter);
				expandableListView
						.setOnChildClickListener(new OnChildClickListener() {

							@Override
							public boolean onChildClick(
									ExpandableListView parent, View v,
									int groupPosition, int childPosition,
									long id) {
								// TODO Auto-generated method stub
								String str = "parent_id = " + groupPosition
										+ " child_id = " + childPosition;
								Toast.makeText(ExpandableListViewActivity.this,
										str, Toast.LENGTH_SHORT).show();
								return false;
							}
						});

			} else if (v == treble_btn) {
				List<SuperTreeViewAdapter.SuperTreeNode> superNodeTree = superAdapter
						.GetTreeNode();
				for (int i = 0; i < parent.length; i++) {
					SuperTreeViewAdapter.SuperTreeNode superNode = new SuperTreeViewAdapter.SuperTreeNode();
					superNode.parent = parent[i];

					for (int j = 0; j < child_grandchild.length; j++) {
						TreeNode node = new TreeNode();
						node.parent = child_grandchild[j][0][0];
						for (int k = 0; k < child_grandchild[j][1].length; k++) {
							node.childs.add(child_grandchild[j][1][k]);
						}
						superNode.childs.add(node);
					}
					superNodeTree.add(superNode);
				}
				superAdapter.UpdateTreeNode(superNodeTree);
				expandableListView.setAdapter(superAdapter);
			}
		}

	};

	OnChildClickListener stvClickEvent = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			String msg = "parent_id = " + groupPosition + " child_id = "
					+ childPosition;
			Toast.makeText(ExpandableListViewActivity.this, msg,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	};
}
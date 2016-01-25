package com.yeyanxiang.project.dlna;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.cybergarage.upnp.Device;
import com.yeyanxiang.util.dlna.Item;

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
public class TreeNode {
	public Device device;
	public List<Item> items = new ArrayList<Item>();
	public Stack<List<Item>> stack = new Stack<List<Item>>();
}

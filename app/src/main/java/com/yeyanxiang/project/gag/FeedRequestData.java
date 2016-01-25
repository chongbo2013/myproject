package com.yeyanxiang.project.gag;

import java.util.ArrayList;

public class FeedRequestData {
	public ArrayList<Feed> data;
	public Paging paging;

	public String getPage() {
		return paging.next;
	}

	private class Paging {
		public String next;
	}
}

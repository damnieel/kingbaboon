package com.crevice.book;

import java.io.IOException;

public class CrawRunnable {
	public static void main(String[] args) {
		try {
			CrawlText.getText(true, true, "http://www.biquge.com.tw/1_1958/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

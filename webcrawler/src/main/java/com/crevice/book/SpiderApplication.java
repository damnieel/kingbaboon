package com.crevice.book;

import java.io.IOException;

public class SpiderApplication {
	public static void main(String[] args) {
		try {
			SpiderText.getText(true, true, "http://www.biquge.com.tw/1_1958/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

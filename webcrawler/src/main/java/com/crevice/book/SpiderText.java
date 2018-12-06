package com.crevice.book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderText {
	 public static void getText(boolean autoDownloadFile, boolean Multithreading, String Url) throws IOException {

	        String rule = "abs:href";
	        
	        List<String> urlList = new ArrayList<String>();
	            
	        Document document = Jsoup.connect(Url)
	                .timeout(4000)
	                .ignoreContentType(true)
	                .userAgent("Mozilla\" to \"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0)")
	                .get();
	        
	        System.out.println(document.toString());
	        Elements urlNode = document.select("a[href$=.html]");
	        
	        for (Element element : urlNode) {
	            urlList.add(element.attr(rule));
	        }
	        
	        SpiderTextThread crawTextThread = new SpiderTextThread(urlList);
	        crawTextThread.start();
	    }
}

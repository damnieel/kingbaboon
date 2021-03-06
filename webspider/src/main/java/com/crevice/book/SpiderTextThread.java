package com.crevice.book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SpiderTextThread extends Thread {

    List<String> UrlList;

    public SpiderTextThread(List<String> urlList) {
        this.UrlList = urlList;
    }

    String rule = "";
    String rule_title = "h1";
    String rule_content = "content";
    
    public static String PATH = "E:\\斗罗大陆\\";

    /**
     * 创建文件
     * 
     * @param fileName
     * @return
     */
    public static void createFile(File fileName) throws Exception {
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTxtFile(String content, File fileName) throws Exception {
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("UTF-8"));
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void run() {
        currentThread().setName("斗罗大陆爬虫运行:");
        
        String title;
        
        String content;
        
        for (String url : UrlList) {
            try {
                Document document = Jsoup.connect(url).timeout(6000).get();
                title = document.select("h1").toString();
                content = document.select("#content").html();
                
                System.out.println("线程:"+currentThread().getName()+"爬取URL—>"+url);
                File file = new File(PATH+title.replaceAll("<h1>", "").replaceAll("</h1>", "")+".txt");
                createFile(file);
                System.out.println("创建文件:"+file.getPath());
                writeTxtFile(fileterHtml(content), file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static String fileterHtml(String str) {
        return str.replaceAll(" ", "").replaceAll("&nbsp;", "").replaceAll("<br>", "\r\n");
    }
}

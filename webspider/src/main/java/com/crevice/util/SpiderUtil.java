package com.crevice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpiderUtil {
	/**
	 * xiaohui
	 * 2018/12/6
	 * @param filePath 文件所要存放的路径
	 * @param fileName 文件的所要取得名称
	 * @param sourceUrl 源文件url
	 */
	public static void downloadFile(String filePath,String fileName,String sourceUrl) {
		//创建文件的目录结构
        File files = new File(filePath);
        if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
            files.mkdirs();
        }
        try {
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3*1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            connection.setRequestProperty("referer", "http://i.meizitu.net");
            InputStream is = connection.getInputStream();
            // 创建文件
            File file = new File(filePath+fileName);
            FileOutputStream out = new FileOutputStream(file);
            int i = 0;
            while((i = is.read()) != -1){
                out.write(i);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static String getFileNameByUrl(String url) {
		return url.substring(url.lastIndexOf("/"));
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\CM20180419\\Desktop";
		String url = "http://mirrors.hust.edu.cn/apache/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35-windows-x86.zip";
		String fileName = getFileNameByUrl(url);
		long startTime = System.currentTimeMillis();
		downloadFile(filePath,fileName,url);
		long endTime = System.currentTimeMillis();
		System.out.println("用时"+(endTime-startTime)/1000+"s");
	}
}

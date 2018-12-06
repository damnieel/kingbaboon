package com.crevice.picture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crevice.util.SpiderUtil;

/**
 * 通过HTML解析实现图片批量下载
 * @author DorraChen
 * @version v1.0
 * @date 2017年10月22日 上午11:36:55
 */

public class SpiderPicture {
    /**
     * 第一步：获取页面的源代码；
     * 第二步：解析源代码，含有图片的标签，再找到图片标签里面的src；
     * 第三步：利用Java里面的net包，网络编程
     * */
    
    /**
     * 根据网页和编码获取网页内容和源代码
     * @param url
     * @param encoding
     */
    public static String getHtmlResourceByUrl(String url,String encoding){
        StringBuffer buffer   = new StringBuffer();
        URL urlObj            = null;
        URLConnection uc      = null;
        InputStreamReader in  = null;
        BufferedReader reader = null;
        
        try {
            // 建立网络连接
            urlObj = new URL(url);
            // 打开网络连接
            uc     = urlObj.openConnection();
            // 创建输入流
            in     = new InputStreamReader(uc.getInputStream(),encoding);
            // 创建一个缓冲写入流
            reader = new BufferedReader(in);
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                // 一行一行追加
                buffer.append(line+"\r\n");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }
    
    /**
     * 根据图片的URL下载的图片到本地的filePath
     * @param filePath 文件夹
     * @param imageUrl 图片的网址
     */
    public static void downImages(String filePath,String imageUrl){
        // 截取图片的名称
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));
        
        SpiderUtil.downloadFile(filePath, fileName, imageUrl);
    }

	
    
    //执行测试程序代码
    public static void main(String[] args) throws IOException {
        String url = "https://www.mzitu.com/tag/youhuo/";
        System.out.println("网页地址："+url);
        String encoding = "utf-8";
        System.out.println("编码方式："+encoding);
        String filePath = "C:\\Users\\CM20180419\\Desktop\\beauty";
        System.out.println("下载到电脑的位置："+filePath);
        // 解析网页源代码
        /*Document document = Jsoup.parse(htmlResource);*/
        Document document = Jsoup.connect(url)
					        	 .timeout(6000)
					        	 .userAgent("Mozilla\" to \"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0)")
					        	 .get();
        // 获取所有图片的地址
        Elements elements = document.getElementsByTag("img");
        
        for(Element element : elements){
            String imageUrl = element.attr("data-original");
            if (!"".equals(imageUrl)) {
                // 判断imgSrc是否为空且是否以"http://"开头\
            	if(!(imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))){
            		imageUrl = "http:"+imageUrl;
            	}
                System.out.println("正在下载的图片的地址：" + imageUrl);
                
                // 截取图片的名称
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));
                
                SpiderUtil.downloadFile(filePath, fileName, imageUrl);
            }
        }
        System.out.println("-------------------------下载完毕！----------------------------");
    }
}

package com.crevice.picture;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crevice.util.Constant;
import com.crevice.util.SpiderUtil;

/**
 * 通过HTML解析实现图片批量下载
 * @author xiaohui
 * @version v1.0
 * @date 2018年12月11日 上午11:36:55
 */

public class SpiderPicture {
    /**
     * 第一步：获取页面的源代码；
     * 第二步：解析源代码，含有图片的标签，再找到图片标签里面的src；
     * 第三步：利用Java里面的net包，网络编程
     * */
    
    //执行测试程序代码
    public static void main(String[] args) throws IOException {
        String url = "https://www.mzitu.com/tag/youhuo/";
        System.out.println("网页地址："+url);
        String encoding = "utf-8";
        System.out.println("编码方式："+encoding);
        String filePath = "C:\\Users\\CM20180419\\Desktop\\beauty\\";
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
                String fileName = SpiderUtil.getFileNameByUrl(imageUrl);
                
                SpiderUtil.downloadFile(filePath, fileName, imageUrl,Constant.UA_CHROME,Constant.REFERER_MZ,null);
            }
        }
        System.out.println("-------------------------下载完毕！----------------------------");
    }
}

package com.crevice.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class SpiderUtil {
	/*private static String filePath = "C:\\Users\\CM20180419\\Desktop\\";*/
	public static int runningThread = 0;
	public static int totalThread = 0;
	/**
	 * xiaohui
	 * 2018/12/6
	 * @param filePath 文件所要存放的路径
	 * @param fileName 文件的所要取得名称
	 * @param sourceUrl 源文件url
	 */
	public static void downloadFile(String filePath,String fileName,String sourceUrl,String userAgent,String referer,String cookie) {
		long startTime = System.currentTimeMillis();
		
		//创建文件的目录结构
        File files = new File(filePath);
        if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
            files.mkdirs();
        }
        try {
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            if(!Util.isEmpty(userAgent)){
            	connection.setRequestProperty("User-Agent", userAgent);
            }
            if(!Util.isEmpty(referer)){
            	connection.setRequestProperty("referer", referer);
            }
            if(!Util.isEmpty(cookie)){
            	connection.setRequestProperty("cookie", cookie);
            }
            int code = connection.getResponseCode();
            if(code == 200) {
                int fileLength = connection.getContentLength();
                System.out.println("文件总长度:"+fileLength);
	            //获取数据流
	            InputStream is = connection.getInputStream();
	            // 创建文件
	            File file = new File(filePath+fileName);
	            FileOutputStream out = new FileOutputStream(file);
	            //将数据流 转化为 byte数组
	            byte[] byteArray = inputStream2ByteArray(is);
	            //将流数组写入文件
	            out.write(byteArray);
	            
	            is.close();
	            out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
		System.out.println("用时"+(endTime-startTime)/1000+"s");
	}
	
	public static void downloadFile(String filePath,String fileName,String sourceUrl){
		downloadFile( filePath, fileName, sourceUrl, Constant.UA_CHROME, null, null);
	}
	
	public static String getFileNameByUrl(String url) {
		return url.substring(url.lastIndexOf("/")+1);
	}
	
	/**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStream2ByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
	
	/**
	 * xiaohui
     * 多线程下载
     * 1.本地创建一个大小跟要获取的文件相同大小的 临时文件
     * 2.计算分配几个线程去下载服务器上的资源，知道每个线程下载文件的位置
     * 3.开启多个 n 线程，每一个线程下载的对应位置的文件
     * 4.如果所有的线程，都把自己的数据下载完毕了，就成功
     * 
     * @param filePath 文件所要存放的路径
	 * @param fileName 文件的所要取得名称
	 * @param sourceUrl 源文件url
	 * @param threadCount 线程数
     * @return
	 * @throws InterruptedException 
     * @throws IOException
     */
	public static void multiThreadDownloadFile(String filePath,String sourceUrl,int threadCount) throws InterruptedException{
    	long startTime = System.currentTimeMillis();
    	//计算运行的线程
    	runningThread = threadCount;
    	totalThread =  threadCount;
    	//用来把线程装进去 用join方法让子线程全部执行完  用于计算时间
    	Vector<DownloadThread> threadVector = new Vector<DownloadThread>();
    	
    	try {
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            int code = connection.getResponseCode();
            if(code == 200) {
                int fileLength = connection.getContentLength();
                System.out.println("文件总长度:"+fileLength);
                
                //创建一个可随机访问的可读取和写入的文件
                RandomAccessFile raf = new RandomAccessFile(new File(filePath+getFileNameByUrl(sourceUrl)), "rwd");
                raf.setLength(fileLength);
                raf.close();
                
                int blockSize = fileLength / threadCount;
                for(int threadId = 1; threadId <= threadCount; threadId ++) {
                    int startIndex = (threadId - 1) * blockSize;
                    int endIndex = threadId * blockSize - 1;
                    if(threadId == threadCount) { 	//最后一个线程要长一点
                    	endIndex = fileLength;
                    }
                    System.out.println("理论线程:"+threadId+",开始位置:"+startIndex+",结束位置:"+endIndex);
                    DownloadThread dt = new DownloadThread(threadId, startIndex, endIndex,filePath,sourceUrl);
                    threadVector.add(dt);
                    dt.start();
                }
            }else{
            	System.out.println("服务器错误.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	for(DownloadThread tv :threadVector){
    		tv.join();
    	}
    	
    	long endTime = System.currentTimeMillis();
    	System.out.println("用时"+(endTime-startTime)/1000+"s");
    }
    /**
     * 下载文件的子线程 每一个线程下载对应的文件
     * @author xiaohui
     *
     * @param threadId 文件所要存放的路径
	 * @param startIndex 线程下载的开始位置
	 * @param endIndex 线程下载的结束位置
	 * @param sourceUrl 源文件url
     */
    public static class DownloadThread extends Thread{
    	private int threadId;
    	private int startIndex;
    	private int endIndex;
    	private String filePath;
    	private String sourceUrl;

		public DownloadThread(int threadId, int startIndex, int endIndex,
				String filePath, String sourceUrl) {
			super();
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.filePath = filePath;
			this.sourceUrl = sourceUrl;
		}

		@Override
		public void run() {
			try {
				
				//检查是否存下载长度的文件 存在读取
				
				File tempFile = new File(filePath+threadId + ".txt");
				if(tempFile.exists()&&tempFile.length()>0){
					FileInputStream fis  = new FileInputStream(tempFile);
					byte[] temp = new byte[1024];
					int leng = fis.read(temp);
					String downloadlen = new String(temp,0,leng);
					startIndex = Integer.valueOf(downloadlen);//修改真实的开始位置
					fis.close();
				}
				
	            URL url = new URL(sourceUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setConnectTimeout(5 * 1000);
	            //设置分段下载的头信息  Range:做分段
	            connection.setRequestProperty("Range", "bytes="+startIndex+"-" + endIndex);
	            System.out.println("真实线程:"+threadId+",开始位置:"+startIndex+",结束位置:"+endIndex);
	            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
	            int code = connection.getResponseCode();
	            System.out.println("code:"+code);
	            if(code == 206){
	            	int fileLength = connection.getContentLength();
	                System.out.println("线程"+threadId+"下载文件总长度:"+fileLength);
	            	
		            InputStream is = connection.getInputStream();//返回当前位置对应的输入流
		            RandomAccessFile raf = new RandomAccessFile(new File(filePath+getFileNameByUrl(sourceUrl)), "rwd");
		            //随机写文件时 从哪个位置开始写
		            raf.seek(startIndex);
		            
		            byte[] buffer = new byte[1024];
		            int len = 0;
		            int total  = 0;//已经下载的长度
		            
		            while((len = is.read(buffer)) != -1) {
		            	RandomAccessFile file = new RandomAccessFile(filePath+threadId+".txt", "rwd");
		            	raf.write(buffer, 0, len);
		            	total += len;
		            	file.write((total+startIndex+"").getBytes());//记录下载位置
		            	file.close();
		            }
		            is.close();
		            raf.close();
		            System.out.println("线程:"+threadId+"下载完毕");
	            }else{
	            	System.out.println("线程:"+threadId+"下载失败");
	            }
			}catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	runningThread --;
	        	if(runningThread == 0){
	        		for (int threadId = 1; threadId <= totalThread; threadId++) {
						File file = new File(filePath+threadId+".txt");
						file.delete();
					}
	        		System.out.println("下载完毕删除所有记录");
	        	}
	        }
		}
    }
    
	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\CM20180419\\Desktop\\";
		String url = "http://mirrors.hust.edu.cn/apache/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35-windows-x86.zip";
		String fileName = getFileNameByUrl(url);
		downloadFile(filePath,fileName,url);
		/*multiThreadDownloadFile(filePath,url,3);*/
	}
}

package com.crevice.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpiderUtil {
	private static String filePath = "C:\\Users\\CM20180419\\Desktop\\";
	private static int threadCount = 3; //线程数
	private static int blockSize; //分块大小
	private static int currentRunThreadCount = 0; //当前线程
	
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
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3*1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            /*connection.setRequestProperty("referer", "http://i.meizitu.net");*/
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * @param filePath 文件所要存放的路径
	 * @param fileName 文件的所要取得名称
	 * @param sourceUrl 源文件url
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
	public static void multiThreadDownloadFile(String filePath,String fileName,String sourceUrl){
    	try {
            URL url = new URL(sourceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            int code = connection.getResponseCode();
            if(code == 200) {
                int fileLength = connection.getContentLength();
                RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePath+fileName), "rw");
                randomAccessFile.setLength(fileLength);
                blockSize = fileLength / threadCount;
                for(int i = 0; i < threadCount; i ++) {
                    int startThread = i * blockSize;
                    int endThread = (i + 1) * blockSize - 1;
                    if( i == blockSize - 1) endThread = fileLength -1;
                    new DownloadThread(i, startThread, endThread,sourceUrl).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class DownloadThread extends Thread {
        private int threadId;
        private int endThread;
        private int startThred;
        private String sourceUrl;
        
        public DownloadThread(int threadId, int endThread, int startThred,String sourceUrl) {
			super();
			this.threadId = threadId;
			this.endThread = endThread;
			this.startThred = startThred;
			this.sourceUrl = sourceUrl;
		}

		public void run() {    
            synchronized (DownloadThread.class) {
                currentRunThreadCount += 1;
            }
            //分段请求网络连接，分段保存在本地
            try {
                System.err.println("理论线程:"+threadId+",开始位置:"+startThred+",结束位置:"+endThread);
                URL url = new URL(sourceUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3 * 1000);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
                File file = new File(filePath+threadId+".zip");
                if(file.exists()) {    //是否断点
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String lastPostion_str = bufferedReader.readLine();
                    startThred = Integer.parseInt(lastPostion_str);
                    bufferedReader.close();
                }
                //设置分段下载的头信息  Range:做分段
                connection.setRequestProperty("Range", "bytes:"+startThred+"-" + endThread);
                int code = connection.getResponseCode();
                System.out.println(code);
                if(code == 200) {    //200:请求全部资源成功  206:代表部分资源请求成功
                    InputStream inputStream = connection.getInputStream();
                    System.out.println(getFileNameByUrl(sourceUrl));
                    RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePath+getFileNameByUrl(sourceUrl)), "rw");
                    randomAccessFile.seek(startThred);
                    byte[] buffer = new byte[1024*10];
                    int length = -1;
                    int total = 0;//记录下载的总量
                    System.err.println("实际线程:"+threadId+",开始位置:"+startThred+",结束位置:"+endThread);
                    while((length = inputStream.read(buffer)) != -1) {
                        randomAccessFile.write(buffer, 0, length);
                        total += length;
                        int currentThreadPostion = startThred + total;
                        RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rwd");
                        randomAccessFile2.write(String.valueOf(currentThreadPostion).getBytes());
                        randomAccessFile2.close();
                    }
                    randomAccessFile.close();
                    inputStream.close();
                    System.err.println("线程:"+threadId+"下载完毕");
                    synchronized (DownloadThread.class) {
                        currentRunThreadCount -= 1;
                        if(currentRunThreadCount == 0){
                            for(int i = 0; i < threadCount; i ++) {
                                File file2 = new File(i+".txt");
                                file2.delete();
                            }
                        }
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }
    }
    /**
     * 多线程下载
     */
    
    
	public static void main(String[] args) throws Exception {
		String url = "http://mirrors.hust.edu.cn/apache/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35-windows-x86.zip";
		String fileName = getFileNameByUrl(url);
		long startTime = System.currentTimeMillis();
		downloadFile(filePath,fileName,url);
		/*multiThreadDownloadFile(filePath,fileName,url);*/
		long endTime = System.currentTimeMillis();
		System.out.println("用时"+(endTime-startTime)/1000+"s");
	}
}

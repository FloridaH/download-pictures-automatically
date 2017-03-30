package http.baidupic_download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class Download {
	private static int index = 0;
	private static String end = "ending";
	
	class Dlpic implements Runnable{
	     private String uri;
	     private BlockingQueue<String> queue;
	     private String dish_url;
	     
	     public Dlpic(BlockingQueue<String> queue, String dish_url){
	    	 this.queue = queue;
	    	 this.dish_url = dish_url;
	     }
	        //线程任务 判断是否到队尾 到则结束 否则下载url
			@Override
			public void run() {
				// TODO Auto-generated method stub
			
				try 
			      {  
			         boolean done = false;  
			         while (!done)  
			         {  
			            uri = queue.take();  
			            if (uri == "ending")  
			            {  
			               queue.put(end);//使得每一个线程都可以检测到结束标志	
			               done = true;  
			            }  
			            else single_download(uri, index++, dish_url);              
			         }  
			      } 
			      catch (InterruptedException e)  
			      {  
			    	  System.out.println("error: " + e.getMessage());
			      }        
			   } 
			}
	
	public static void single_download(String url, int index, String dish_url) {
		HttpClient httpclient = new DefaultHttpClient();
		//设置连接页面超时时间3s
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,3000);
		//设置读取页面超时时间3s
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);
		
		try {
		HttpGet httpget = new HttpGet(url);

		
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		// Execute HTTP request
		System.out.println("executing request " + httpget.getURI());
		HttpResponse response = httpclient.execute(httpget);

		File storeFile = new File(dish_url+index+".jpg");
		FileOutputStream output = new FileOutputStream(storeFile);

		// 得到网络资源的字节数组,并写入文件
		HttpEntity entity = response.getEntity();
		if (entity != null) {
		InputStream instream = entity.getContent();
		try {
		byte b[] = new byte[1024];
		int j = 0;
		while( (j = instream.read(b))!=-1){
		output.write(b,0,j);
		}
		output.flush();
		output.close();
		} catch (IOException ex) {
			System.out.println("error: " + ex.getMessage());
		throw ex;
		} catch (RuntimeException ex) {
			System.out.println("error: " + ex.getMessage());
		httpget.abort();
		throw ex;
		} finally {
		// Closing the input stream will trigger connection release
		try { instream.close(); } catch (Exception ignore) {
			System.out.println("error: " + ignore.getMessage());
		}
		}
		}

		} catch (Exception e) {
			System.out.println("下载失败！");
			System.out.println("error: " + e.getMessage());
		} finally {
		httpclient.getConnectionManager().shutdown();
		 System.out.println("下载完成...");
		}
		}
	
	public static void all_download(ArrayList<String> imgList , int search_threads , String dish_url){
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		//将url从数组转移到队列
	       for(int i=0 ; i<imgList.size();i++){
			   queue.offer(imgList.get(i));
		   }
	     //队尾放入结束标志 
	       queue.offer(end);
		   
	     //开启SEARCH_THREADS个线程下载图片
	       for (int i = 1; i <= search_threads; i++)  
	           new Thread(new Download().new Dlpic(queue, dish_url)).start();  
	       
	     //ArrayList数组 非多线程方法
//	       for (String urlString : imgList) { 
//		       single_download(urlString, index, dish_url);
//		       index++;
//		        }
	}
}

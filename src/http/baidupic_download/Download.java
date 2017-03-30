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
	        //�߳����� �ж��Ƿ񵽶�β ������� ��������url
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
			               queue.put(end);//ʹ��ÿһ���̶߳����Լ�⵽������־	
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
		//��������ҳ�泬ʱʱ��3s
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,3000);
		//���ö�ȡҳ�泬ʱʱ��3s
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);
		
		try {
		HttpGet httpget = new HttpGet(url);

		
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		// Execute HTTP request
		System.out.println("executing request " + httpget.getURI());
		HttpResponse response = httpclient.execute(httpget);

		File storeFile = new File(dish_url+index+".jpg");
		FileOutputStream output = new FileOutputStream(storeFile);

		// �õ�������Դ���ֽ�����,��д���ļ�
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
			System.out.println("����ʧ�ܣ�");
			System.out.println("error: " + e.getMessage());
		} finally {
		httpclient.getConnectionManager().shutdown();
		 System.out.println("�������...");
		}
		}
	
	public static void all_download(ArrayList<String> imgList , int search_threads , String dish_url){
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		//��url������ת�Ƶ�����
	       for(int i=0 ; i<imgList.size();i++){
			   queue.offer(imgList.get(i));
		   }
	     //��β���������־ 
	       queue.offer(end);
		   
	     //����SEARCH_THREADS���߳�����ͼƬ
	       for (int i = 1; i <= search_threads; i++)  
	           new Thread(new Download().new Dlpic(queue, dish_url)).start();  
	       
	     //ArrayList���� �Ƕ��̷߳���
//	       for (String urlString : imgList) { 
//		       single_download(urlString, index, dish_url);
//		       index++;
//		        }
	}
}

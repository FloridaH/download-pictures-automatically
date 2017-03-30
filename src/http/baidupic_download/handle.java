package http.baidupic_download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class handle {
	public static int page_number;//页面的总页数
	
	//方法 用于对url处理 截取完整的图片链接字段 返回ArrayList<String>类型的数组
		public static ArrayList<String> imgurl_handle(ArrayList<String> imgurl , int index){
			int flag;
			String res = "";
			List<String> list = new ArrayList<String>(imgurl);
	        //根据图片属性截取图片的url字段
	        for(int i=0;i<imgurl.size();i++){
	     	 if(list.get(i).contains(".jpg")){
	          flag = list.get(i).indexOf(".jpg");
	     	  res = list.get(i).substring(index, flag+4);
	     	  imgurl.set(i, res);
	     	 }
	     	 else
	     		 if(list.get(i).contains("=jpg")){
	     	          flag = list.get(i).indexOf("=jpg");
	     	     	  res = list.get(i).substring(index, flag+4);
	     	     	  imgurl.set(i, res);
	     	     	 }
	     	 else 
	     		 if(list.get(i).contains("gif")){
	            flag = list.get(i).indexOf("gif");
	       	  res = list.get(i).substring(index, flag+3);
	       	  imgurl.set(i, res);
	       	 }
	     		 else 
	     			 if(list.get(i).contains("jpeg")){
	     	      flag = list.get(i).indexOf("jpeg"); 
	     	 	  res = list.get(i).substring(index, flag+4);
	     	 	  imgurl.set(i, res);
	     	 	 }
	     			 else 
	     				 if(list.get(i).contains("png")){
	    	      flag = list.get(i).indexOf("png"); 
	    	 	  res = list.get(i).substring(index, flag+3);
	    	 	  imgurl.set(i, res);
	    	 	 }
	     				 else 
	     					 if(list.get(i).contains("PNG")){
	    	      flag = list.get(i).indexOf("PNG"); 
	    	 	  res = list.get(i).substring(index, flag+3);
	    	 	  imgurl.set(i, res);
	    	 	 }
	     					 else 
	     						 if(list.get(i).contains("JPEG")){
	    	      flag = list.get(i).indexOf("JPEG"); 
	    	 	  res = list.get(i).substring(index, flag+4);
	    	 	  imgurl.set(i, res);
	    	 	 }
	     						 else 
	     							 if(list.get(i).contains("JPG")){
	    	      flag = list.get(i).indexOf("JPG");
	    	 	  res = list.get(i).substring(index, flag+3);
	    	 	  imgurl.set(i, res);
	    	 	 }
	     							 else 
	     								 if(list.get(i).contains("bmp")){
	  	      flag = list.get(i).indexOf("bmp");
	  	 	  res = list.get(i).substring(index, flag+3);
	  	 	  imgurl.set(i, res);
	  	 	 }
	     								 else 
	     									 if(list.get(i).contains("jpe")){
	  	      flag = list.get(i).indexOf("jpe");
	  	 	  res = list.get(i).substring(index, flag+3);
	  	 	  imgurl.set(i, res);
	  	 	 }
	        }
	        return imgurl;
		}
		
		/*处理"data-objurl=\"(.*?\\.(jpeg|jpg|png|PNG|JPEG|JPG))"
		 *和"objurl=(.*?(\\.|%3D)(jpeg|jpg|png|PNG|JPEG|JPG|gif|jpe|bmp))"形式的字段
		*/
		public static ArrayList<String> handling(String html , String para){
			Pattern p = Pattern.compile(para);
		    Matcher m = p.matcher(html);
		    ArrayList<String> imgurl = new ArrayList();
		    while(m.find()){
		    	imgurl.add(m.group());
		    }
		    return imgurl;
		}   
		
		//判断是html否匹配regex
		public static boolean Matcher(String regex , String html){
			Pattern p = Pattern.compile(regex);
		    Matcher m = p.matcher(html);
		    if(m.find()){
		    	return true;
		    }
		    else 
		    	return false;
		}
		/*
		 * 从网页源代码找到listNum的值 除以20来得到总页数（经过多个网页的尝试 结果和
		 * 总页数十分相近）
		 */
		public static int page_counter(String url) throws NoSuchMethodException{
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpGet httpGet=new HttpGet(url);
	        httpGet.setHeader(
	       		 "User-Agent",
	       		 "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko"
	       		 );
	        HttpEntity entity=null;
	        String htmls = null;
	        
	        try{
	       	 HttpResponse response = httpClient.execute(httpGet);
	       	 
	       	 if(HttpStatus.SC_OK== response.getStatusLine().getStatusCode()){
	       		 entity=response.getEntity();        
	       		 htmls = EntityUtils.toString(entity);//取出应答字符串 
	       		 
	       		Pattern p = Pattern.compile("listNum.*");
	    	    Matcher m = p.matcher(htmls);
	    	    
	    	    if(m.find()){
	    	    	int f = m.start()+11;//首字符的下标
	    	    	int e = m.end()-2;//尾字符下标的后一个
	    	    	//从f开始取，到e结束，从0开始数，其中不包括e位置的字符
	    	    	htmls = htmls.substring(f, e);
	    	    	
	    	    	page_number = Integer.parseInt(htmls)/20;    	    	
	    	    }
	       	 }
	        }        
	        catch(ClientProtocolException e){
	       	 e.printStackTrace();
	        }
	        catch(IOException e){
	       	 e.printStackTrace();
	        }
			return page_number; 
		}
		
		//对html里的内容进行处理	
		public static ArrayList<String> img_url() throws IOException{
			ArrayList<String> imgurl = new ArrayList();
			String fir = "" , res = "", element = "", reader;
			int flag;
			
			Socket sc = new Socket();
		    sc.setSoTimeout(5000);
		      
		  	File file = new File("html-body.html");
		  	FileInputStream fs = new FileInputStream(file);
		    InputStreamReader read = new InputStreamReader(fs,"UTF-8");
		    //InputStreamReader read = new InputStreamReader(fs);
		    BufferedReader bufferedReader = new BufferedReader(read);
		    //对于需要不断改变的字符串 StringBuffer效率更高
		    StringBuffer html = new StringBuffer();
		    while((reader = bufferedReader.readLine()) != null){
		        html.append(reader);
		    }
		    bufferedReader.close();
		    read.close();
		    fs.close();

		    if(Matcher("data-objurl=\"(.*?\\.(jpeg|jpg|png|PNG|JPEG|JPG))",html.toString())){
		    	imgurl = handling(html.toString(),"data-objurl=\"(.*?\\.(jpeg|jpg|png|PNG|JPEG|JPG))");
		    	imgurl = imgurl_handle(imgurl , 13);
		    }
		    else 
		    	if(Matcher("objurl=(.*?(\\.|%3D)(jpeg|jpg|png|PNG|JPEG|JPG|gif|jpe|bmp))",html.toString())){
		    	imgurl = handling(html.toString(),"objurl=(.*?(\\.|%3D)(jpeg|jpg|png|PNG|JPEG|JPG|gif|jpe|bmp))");//配对 objurl =的形式
		    	imgurl = imgurl_handle(imgurl , 7);
		    
		    
		    	for(int i=0;i<imgurl.size();i++){
		    		while(imgurl.get(i).contains("%")){
		    		fir = decode.unescape(imgurl.get(i));	
		    		imgurl.set(i, fir);
		    		}
		        }
		    
		    for(int i=0;i<imgurl.size();i++){
		    	if(imgurl.get(i).contains("bdtype")){
		    		flag = imgurl.get(i).indexOf("bdtype");
		    		res = imgurl.get(i).substring(0, flag-5);
		    		imgurl.set(i, res);
		    	}
		    	if(imgurl.get(i).contains("&src")){
		    		imgurl.remove(i);
		    	}
		    	if(imgurl.get(i).contains("url")){
		 			flag = imgurl.get(i).indexOf("url");
		 			res = imgurl.get(i).substring(flag+3);
		 			imgurl.set(i, res);
		 		}
		    	if(imgurl.get(i).contains("data-thumburl"))//判断是否有“data-thumburl”字段
		    	   {
		    		   flag = imgurl.get(i).indexOf("data-thumburl"); //确定下标位置
		    		   res = imgurl.get(i).substring(0, flag-2)+".jpg";//截取字段并添加.jpg
		    		   imgurl.set(i, res);
		    	   }
		    }
		    	}
		    return imgurl;
			}
}

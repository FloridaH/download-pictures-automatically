package Qzone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class visit_and_store {
	//����url��ַ ����ҳ��Ϣ��html��ʽ�����ڱ���
		public static void VistPage(String url , int num) throws InterruptedException {
		        int page_number=0;
		        final Dimension DISPLAY_SIZE = new Dimension(1920, 1080);
		        //Chrome�����
			    System.getProperties().setProperty("webdriver.chrome.driver", "E:/Java/seleniumdriver/chromedriver.exe");
		        ChromeDriver webDriver = new ChromeDriver();
			    	       
		        /*
		         *��IE����
		         *1.�ȵ���������IEDriverServer.exe
		         *2.�ڴ����������� 
		         *3.�ڴ������ú���IE����ģʽ
		         *4.import org.openqa.selenium.remote.DesiredCapabilities;
		         */
//		        System.getProperties().setProperty("webdriver.ie.driver", "E://Java//seleniumdriver//IEDriverServer.exe");
//		        DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//		        ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//		        WebDriver webDriver = new InternetExplorerDriver(ieCapabilities);
			    
		      /*  Date date1 = new Date("Fri 02-Jan-1970 00:00:00 GMT");
				Date date2 = new Date("Mon 26 Jul 1997 05:00:00 GMT");
				Date date3 = new Date("Tue 17-May-2016 16:00:00 GMT");
				Cookie ck1 = new Cookie("" ,"" ,"qq.com" ,"/" ,date1); 
				Cookie ck2 = new Cookie("" ,"" ,"qq.com" ,"/" ,date2);
				Cookie ck3 = new Cookie("" ,"" ,"qzone.qq.com" ,"/" ,date3);
		        
				webDriver.manage().addCookie(ck1);
				webDriver.manage().addCookie(ck2);
				webDriver.manage().addCookie(ck3);
				*/
		        
		        //��������ڵĴ�СҲ�ǲ��ԵĹؼ�����֮һ����Ϊ��ҳ�ڴ�С��ͬ�Ĵ����б��ֿ��ܲ�һ��
		        webDriver.manage().window().setSize(DISPLAY_SIZE);
		        webDriver.get(url);
		        
		        webDriver.switchTo().frame("login_frame");
		        webDriver.findElement(By.id("switcher_plogin")).click();
		        webDriver.findElement(By.id("u")).clear();
		        webDriver.findElement(By.id("u")).sendKeys("694123149");
		        webDriver.findElement(By.id("p")).clear();
		        webDriver.findElement(By.id("p")).sendKeys("rxh243853721..");
		        webDriver.findElement(By.id("login_button")).click();
		        
		        Thread.sleep(10000);
		        //WebElement webElement = webDriver.findElement(By.xpath("//*[@id=\"pageMore\"]"));
		      //�õ���ǰ���ڵľ��
		        String currentWindow = webDriver.getWindowHandle();
		        //�õ����д��ڵľ��
		        Set<String> handles = webDriver.getWindowHandles();
		        Iterator<String> it = handles.iterator();
		        while(it.hasNext()){
		        String handle = it.next();
		        if(currentWindow.equals(handle)) continue;
		        WebDriver window = webDriver.switchTo().window(handle);
		        System.out.println("title,url = "+window.getTitle()+","+window.getCurrentUrl());
		        }
		        
		        JavascriptExecutor jse=(JavascriptExecutor) webDriver;
		        //page_number ��ǰҳ���ҳ��
		        while(page_number<=num){
		        	//while(webElement.getAttribute("style")!="visibility: visible;"){
		        	
		        		//���������ϵ��ײ�
		        		//jse.executeScript("scrollTo(0,document.body.scrollHeight);");
		        	//}
		        //ģ���û���Ϊ������ظ��ఴ�� 
		      /*Actions click_action = new Actions(webDriver);
		        click_action.moveToElement(webElement);
		        click_action.click().perform();
		        */
		        //���������ϵ��ײ�	        
		        jse.executeScript("scrollTo(0,document.body.scrollHeight);");
		        //��ͣ0.5s
		        Thread.sleep(500);
		        
		        //System.out.println(webElement.getAttribute("style"));
		        page_number++;
		        }
		        //JS��ʽ��ȡҳ��body����
		        String html = (String) jse.executeScript("return document.getElementsByTagName('body')[0].innerHTML");
		        webDriver.close();
		        //return html;
		        try{
		        	//File��ʽ
		           // File file = new File("get_html.html");
		           // if(!file.exists()){
		            //	file.createNewFile();
		            //}  
		           // FileWriter fw = new FileWriter(file.getName(),true);
		            //BufferedWriter bw = new BufferedWriter(fw,);
		            //FileOutputStream��ʽ
		            FileOutputStream file = new FileOutputStream("html-body.html");
		            //FileWriter fw = new FileWriter(file.getName(),true);
		            
		            OutputStreamWriter ow = new OutputStreamWriter(file);
		            //OutputStreamWriter ow = new OutputStreamWriter(file);
		            BufferedWriter bw = new BufferedWriter(ow);
		            bw.write(html);
		            bw.close();
		            System.out.print("Done");
		            }catch(IOException e){
		            	   e.printStackTrace();
		            	  }
		        
		    }
		
		public static void download(String url, int index) {
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

			File storeFile = new File("E:\\ͼƬ\\����\\"+index+".jpg");
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
			// In case of an IOException the connection will be released
			// back to the connection manager automatically
			throw ex;
			} catch (RuntimeException ex) {
			// In case of an unexpected exception you may want to abort
			// the HTTP request in order to shut down the underlying
			// connection immediately.
			httpget.abort();
			throw ex;
			} finally {
			// Closing the input stream will trigger connection release
			try { instream.close(); } catch (Exception ignore) {}
			}
			}

			} catch (Exception e) {
				System.out.println("����ʧ�ܣ�");
	            e.printStackTrace();
			} finally {
			httpclient.getConnectionManager().shutdown();
			 System.out.println("�������...");
			}
			}
}

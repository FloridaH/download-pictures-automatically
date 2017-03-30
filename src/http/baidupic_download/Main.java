package http.baidupic_download;

import java.io.IOException;
import java.util.Date;

import org.openqa.selenium.Cookie;

public class Main {

	public static void main(String[] args) throws NoSuchMethodException, InterruptedException, IOException {
		// TODO Auto-generated method stub
		String url = "http://image.baidu.com/search/index?tn=baiduimage&ipn=r"
				+ "&ct=201326592&cl=2&lm=-1&st=-1&fm=result&fr=&sf=1&fmq=14908"
				+ "58374726_R&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&heigh"
				+ "t=&face=0&istype=2&ie=utf-8&word=%E5%BE%AE%E8%B7%9D%E6%91%84"
				+ "%E5%BD%B1";
		String dish_url = "E:\\Í¼Æ¬\\¸ßÇå±ÚÖ½\\";
		int threads_num = 10;
		 
		
		int pagenumber = handle.page_counter(url);
		visit_and_store.VistPage(url, pagenumber);
		Download.all_download(handle.img_url(), threads_num, dish_url);
		
		System.out.print("Finished");
	}

}

package data.service;
import java.io.IOException;
import java.util.ArrayList;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Crawler {
	
	private static Crawler signletonInstanceCrawler ;
	
	private Crawler() {
		
	}
	
	private void log(String str) {
		System.out.println(str);
	}
	
	public static Crawler getInstance() {
		if(signletonInstanceCrawler == null) {
			return new Crawler();
		}
		
		return signletonInstanceCrawler;
	}
	
	
	public ArrayList<String> crawlDownloadLink(String url) {
		
        ArrayList<String> downloadLinks = new ArrayList<String>();
        
        Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements links =  (Elements) doc.getElementsByTag("a");

			for(Element link: links) {
				String downloadLinkString = link.attr("href");
				if(downloadLinkString.indexOf("zip") > -1) {
					downloadLinks.add(downloadLinkString);
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	      

		return downloadLinks;
	}

}

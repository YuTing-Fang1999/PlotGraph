package plotgraph;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class SearchPlan {
	WebClient webClient;
	HtmlButtonInput search;
	HtmlPage orignPage;
	HtmlSelect pubSchoSelect;
	HtmlSelect priSchoSelect;
	HtmlSelect pubDepartSelect;
	HtmlSelect priDepartSelect;
	int pubSchooNum;
	int priSchooNum;
	Infos infos;
	public SearchPlan() {
		
		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10 * 1000);

		
		try {
			orignPage = webClient.getPage(
					"https://arsp.most.gov.tw/NSCWebFront/modules/talentSearch/talentSearch.do");
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//取得參考
		search =(HtmlButtonInput)orignPage.getElementByName("send");
		pubSchoSelect = (HtmlSelect) orignPage.getElementById("organ_2_1");
		pubDepartSelect = (HtmlSelect) orignPage.getElementById("organ_3_1");
		priSchoSelect = (HtmlSelect) orignPage.getElementById("organ_2_2");
		priDepartSelect = (HtmlSelect) orignPage.getElementById("organ_3_2");
		//取得學校總數
		pubSchooNum = pubSchoSelect.getOptionSize();
		priSchooNum = priSchoSelect.getOptionSize();			
		
	}
	//回傳Infos
	public Infos crawlPubInfos(int i) {
		String schoName = pubSchoSelect.getOption(i).asText();
		infos = new Infos(schoName);
		infos.schoolNum = i;
		//選擇學校
		HtmlPage page = selectPubscho(i);
		if(page!=null) {
			System.out.println("開始解析 "+schoName+" 資工系");
			infos.infoarray = crawlInfoArray(page, i);
			return infos;
		}else {
			System.out.println("找不到 "+schoName+" 資工系" );
		}
		return null;
	}
	
	public Infos crawlPriInfos(int i) {
		String schoName = priSchoSelect.getOption(i).asText();
		infos = new Infos(schoName);
		infos.schoolNum = i;
		//選擇學校
		HtmlPage page = selectPrischo(i);
		if(page!=null) {
			System.out.println("開始解析 "+schoName+" 資工系");
			infos.infoarray = crawlInfoArray(page, i);
			return infos;
		}else {
			System.out.println("找不到 "+schoName+" 資工系" );
		}
		return null;
	}
	
		
	//回傳整所老師的資料
	public ArrayList<Info> crawlInfoArray(HtmlPage page,int scho) {
		ArrayList<Info> infoarray = new ArrayList<Info>();
		DomElement div = page.getFirstByXPath("//div[@class='c30Tblist2']");
		DomElement tbody = div.getFirstElementChild().getFirstElementChild();
		DomNodeList<DomNode> trs =  tbody.getChildNodes();
		DomNodeList<DomElement> domElements=page.getElementsByTagName("input");
		
		HtmlButtonInput btnSubmit=null;
		HtmlPage page2 = null;
		int i=2;
		for(DomElement temp:domElements){
			if(temp.getAttribute("value").equals("計畫總覽")){	
				try {
					btnSubmit=  (HtmlButtonInput) temp;	
					String name = crawlName(trs.get(i));
					page2 = btnSubmit.click();
					ArrayList<String> plan = crawlPlan(page2);
					if(plan!=null) {
						Info info = new Info();
						info.name = name;
						info.plan = plan;
						infoarray.add(info);
						System.out.println("已儲存"+name+"的計畫");
					}else {
						System.out.println("找不到"+name+"的計畫");
					}
					webClient.getWebWindows().get(0).getHistory().back();
					i = i+2;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}
		}
			
		System.out.println("儲存完畢");
		return infoarray;
	}

	//回傳個人名字
	public String crawlName(DomNode tr) {
		org.jsoup.nodes.Document document = Jsoup.parse(tr.asXml());
		Element link = document.select("a") .first();	
		return link.childNodes().get(0).toString();		
	}
	//回傳個人計畫
	public ArrayList<String> crawlPlan(HtmlPage page) {
		org.jsoup.nodes.Document document = Jsoup.parse(page.asXml());
		Element c30Tblist2 = document.selectFirst("div.c30Tblist2");
		if(c30Tblist2!=null) {
			ArrayList<String> plans = new ArrayList<String>();
			Element tboty = c30Tblist2.selectFirst("tbody");
			Elements trs = tboty.select("tr");
			String plan = new String();
			for (int i = 1; i < trs.size(); i++) {
				if (!plan.equals(trs.get(i).select("td").get(3).text())) {
					plans.add(trs.get(i).select("td").get(3).text());
				}
				plan = trs.get(i).select("td").get(3).text();
			}
			return plans;
		}
		return null;		
	}
	
	//回傳學校的page
	public HtmlPage selectPubscho(int i) {
		try {
			HtmlOption CSIE = null;
			pubSchoSelect.getOption(i).click();
			webClient.waitForBackgroundJavaScript(10*1000);
			//點完後再取選項		
			List<HtmlOption> pubDepartOpts = new ArrayList<HtmlOption>();
			pubDepartOpts = pubDepartSelect.getOptions();
			//找資工系
			for (int j = 0; j < pubDepartOpts.size(); j++) {
				String s = pubDepartOpts.get(j).getText();
				if(s.contains("資訊") && (s.contains("工程"))&& s.contains("系")) {
					CSIE = pubDepartOpts.get(j);
					break;
				}				
			}
			if(CSIE==null) {
				for (int j = 0; j < pubDepartOpts.size(); j++) {
					String s = pubDepartOpts.get(j).getText();
					if(s.contains("資訊") && (s.contains("科學"))&& s.contains("系")) {
						CSIE = pubDepartOpts.get(j);
						break;
					}				
				}
			}
			if(CSIE!=null) {
				infos.department = CSIE.asText();
				CSIE.click();
				HtmlPage page = search.click();
				webClient.waitForBackgroundJavaScript(10*1000);
				if(page.getFirstByXPath("//div[@class='c30Tblist2']")==null) {
					System.out.println("查無資料");
					return null;
				}
				HtmlSelect pageSize = (HtmlSelect) page.getElementsByName("pageSize").get(1);
				page = pageSize.getOptionByValue("100").click();
				webClient.waitForBackgroundJavaScript(10*1000);
				return page;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public HtmlPage selectPrischo(int i) {
		try {
			HtmlOption CSIE = null;
			priSchoSelect.getOption(i).click();
			webClient.waitForBackgroundJavaScript(10*1000);
			//點完後再取選項		
			List<HtmlOption> priDepartOpts = new ArrayList<HtmlOption>();
			priDepartOpts = priDepartSelect.getOptions();
			//找資工系
			for (int j = 0; j < priDepartOpts.size(); j++) {
				String s = priDepartOpts.get(j).getText();
				if(s.contains("資訊") && (s.contains("工程"))&& s.contains("系")) {
					CSIE = priDepartOpts.get(j);
					break;
				}
			}
			
			if(CSIE==null) {
				for (int j = 0; j < priDepartOpts.size(); j++) {
					String s = priDepartOpts.get(j).getText();
					if (s.contains("資訊") && (s.contains("科學"))&& s.contains("系")) {
						CSIE = priDepartOpts.get(j);
						break;
					}
				}
			}
			if(CSIE!=null) {
				infos.department = CSIE.asText();
				CSIE.click();
				HtmlPage page = search.click();
				webClient.waitForBackgroundJavaScript(10*1000);
				if(page.getFirstByXPath("//div[@class='c30Tblist2']")==null) {
					System.out.println("查無資料");
					return null;
				}
				HtmlSelect pageSize = (HtmlSelect) page.getElementsByName("pageSize").get(1);
				page = pageSize.getOptionByValue("100").click();
				webClient.waitForBackgroundJavaScript(10*1000);
				return page;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

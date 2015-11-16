package yun.spider;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import yun.helper.Browser;
import yun.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 通过公众号昵称 公众号号 从搜索页进入进行抓取
 */

public class ArticlesByWordSpider {
	//默认文章没有找到
	public Boolean articleIsFound = false;
	//是否还有下一页
	public Boolean hasNextPage = true;
	public WebDriver driver;
	//搜狗首页
	public  String weChatUrl = "http://weixin.sogou.com/";
	//输入框
	public String searchInputSelector = "#query";
	//没找到的时的元素css
	public String noResSelector = "#noresult_part1_container";
	//下一页按钮选择器
	public String nextPageSelector = "#sogou_next";
	//分页的连接
	public String pageSelector = "#pagebar_container a";
	//页数 默认10页
	public int pageNum = 10;

	/**
	 * 爬取程序入口,用来控制爬取的流程
	 * @param word
	 * @param lastdocid
	 * @return
	 */
	public List<Article> spider(String word,String lastdocid){
		List<Article> articles = new ArrayList<Article>();
		//初始化浏览器
		driver = Browser.init();
		//初始化搜狗首页
		driver.get(weChatUrl);
		//在搜索框中写入搜索词并且提交
		Browser.inputAndSubmit(searchInputSelector, word);
		//解析第一页获取文章
		List firstPageArticles = parse(lastdocid);
		articles = firstPageArticles;
		//如果文章没有找到 从第二页开始循环 获取剩下页面的文章
		while(hasNextPage==true && articleIsFound==false){
			//进入下一页
			Boolean gotoNext = gotoNextPage();
			if(gotoNext == true){
				List nextPageArticles = parse(lastdocid);
				articles.addAll(nextPageArticles);
			}else{
				hasNextPage = false;
				System.out.println("进入下一页失败");
			}
		}
		driver.quit();
		return articles;
	}
	/**
	 * 解析当前页的文章，当获取到最新的文章docid就停止，不再往下面继续抓取了
	 * @param lastdocid
	 * @return
	 */
	public List<Article> parse(String lastdocid){
		try {
			WebElement noResWe = driver.findElement(By.cssSelector(noResSelector));
			System.out.println(noResWe.getText());
			if(noResWe != null){
				return null;
			}
		}catch (org.openqa.selenium.NoSuchElementException ex) {
			System.out.println("这个词有文章，程序继续执行");
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div
		List articles = new ArrayList();
		System.out.println(divEles.size());
		for (int i = 0; i < divEles.size(); i++) {
			WebElement divElement = (WebElement) divEles.get(i);
			Article article = new Article();
			String docid = divElement.getAttribute("d");// docid 文档id
			System.out.println("docid:" + docid);
			article.setDocid(docid);
			if (docid.equals(lastdocid)) {
				//找到最新文章了文章了
				articleIsFound = true;
				System.out.println("找到最后的文章了");
				break;
			}
			WebElement aElement = divElement.findElement(By.cssSelector(".txt-box h4 a"));//标题a
			String title = aElement.getText().toString();        //获取标题
			String aUrl = aElement.getAttribute("href");//获取跳转前的url
			System.out.println("标题:" + title);
			System.out.println("链接:" + aUrl);
			article.setTitle(title);

			WebElement pElement = divElement.findElement(By.cssSelector(".txt-box p"));//描述p
			String description = pElement.getText().toString();
			System.out.println("描述:" + description);
			article.setDescription(description);

			WebElement tDivElement = divElement.findElement(By.cssSelector(".s-p"));//时间div
			int last_modified = Integer.parseInt(tDivElement.getAttribute("t"));
			System.out.println("最后修改时间:" + last_modified);
			article.setLast_modified(last_modified);

			WebElement imgElement = divElement.findElement(By.cssSelector("img"));//图片img
			String imgSrc = imgElement.getAttribute("src");
			System.out.println("图片src:" + imgSrc);
			article.setImglink(imgSrc);

			WebElement appElement = divElement.findElement(By.cssSelector("#weixin_account"));//微信公众号
			String openid = appElement.getAttribute("i");
			System.out.println("openid:" + openid);
			article.setOpenid(openid);


			String app_name = appElement.getText();
			System.out.println("app_name："+app_name);
			article.setApp_name(app_name);
			String currentHandle = driver.getWindowHandle();

			aElement.click();
			// 获取所有浏览器窗口标识
			Set<String> handles = driver.getWindowHandles();
			for (String handle : handles) {
				// System.out.println(handle);
				if (handle.equals(currentHandle)) {
					continue;
				} else {
					driver.switchTo().window(handle);
					aUrl = driver.getCurrentUrl();
					if(aUrl!=null||aUrl.length()>0){
						article.setUrl(aUrl);
						String htmlStr = driver.getPageSource();
						Document doc = Jsoup.parse(htmlStr);
						Element content = doc.select("#js_content").first();
						String contentHtml = "空，没找到js_content的元素";
						if(content != null){
							contentHtml = content.html();
						}
						article.setContent(contentHtml);
//						System.out.println(htmlStr);
						System.out.println("重定向后的:" + aUrl);
						System.out.println("\n");
						driver.close();
					}
				}
				// 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
			}
			driver.switchTo().window(currentHandle);
			articles.add(article);
		}
		return articles;
	}
	/**
	 * 获取下一个的点击按钮，并且进入下一页
	 * @return
	 */
	public Boolean gotoNextPage(){
		try {
			//如果出现没有结果 获取到的页面数量为0
			WebElement nextButton = driver.findElement(By.cssSelector(nextPageSelector));
			if(nextButton != null){
				nextButton.click();
				return true;
			}else{
				hasNextPage = false;
				return false;
			}
		}catch (org.openqa.selenium.NoSuchElementException ex) {
			System.out.println("没有下一页了");
			hasNextPage = false;
			return false;
		}
	}
	/**
	 * 获取搜索页数量
	 * @return
	 */
	public int getPageNum(){
		try {
			//如果出现没有结果 获取到的页面数量为0
			WebElement noResWe = driver.findElement(By.cssSelector(noResSelector));
			if(noResWe != null){
				pageNum = 0;
				return pageNum;
			}
		}catch (org.openqa.selenium.NoSuchElementException ex) {
			System.out.println("这个词有页数，程序继续执行");
		}
		try {
			//如果能找到分页的a标签
			List<WebElement> pages = driver.findElements(By.cssSelector(pageSelector));
			if(pages.size()>=1){
				pageNum = pages.size();
			}
			return pageNum;
		}catch (org.openqa.selenium.NoSuchElementException ex){
			System.out.println("没找到分页元素a标签");
		}
		//默认返回10页
		return pageNum;
	}
}

package com.crevice.test;

import com.crevice.test.TestPhantomJsDriver;
import com.crevice.util.Util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author zhuangj
 * @date 2017/11/13
 */
public class TestTaoBaoPageProcessor implements PageProcessor {

    private String keyWord;

    private static final String TAO_BAO_DETAIL_URL_START="https://item.taobao.com/item.htm";
    
    private static final String TIAN_MAO_DETAIL_URL_START="https://detail.tmall.com/item.htm";

    private Site site = Site
            .me()
            .setCharset("UTF-8")
            .setCycleRetryTimes(3)
            .setSleepTime(3 * 1000)
            .addHeader("Connection", "keep-alive")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public TestTaoBaoPageProcessor() {
    }

    public TestTaoBaoPageProcessor(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public Site getSite() {
        return site;
    }




    @Override
    public void process(Page page) {
        WebDriver driver= TestPhantomJsDriver.getPhantomJSDriver();
//        WebDriver driver= null;
//        try {
//            driver = TestChromeDriver.getChromeDriver();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        driver.get(page.getRequest().getUrl());
        WebElement webElement = driver.findElement(By.id("page"));
        String str = webElement.getAttribute("outerHTML");

        Html html = new Html(str);
        if(isFirstPage(html)){
            analysisPagination(page,html);
        }else if(isListPage(html)){
            analysisListPage(page,html,driver);
        }else {
            analysisDetailPage(page,html,driver);
        }
    }

    private void analysisListPage(Page page, Html html, WebDriver driver) {
        List<String> detailPageList= html.xpath("//*[@id=\"mainsrp-itemlist\"]").$("a[id^='J_Itemlist_TLink_']").xpath("//a/@href").all();
        page.addTargetRequests(detailPageList);
    }

    /**
     * 分析分页规则
     * @param page
     * @param html
     */
    private void analysisPagination(Page page,Html html){
        List<String> pageList= html.xpath("//*[@id=\"mainsrp-pager\"]/div/div/div/ul/li/a/@data-value").all();
        pageList = new ArrayList(new HashSet(pageList));

        List<String> pageParameterList=new ArrayList<>();
        for(String  value:pageList){
            pageParameterList.add("https://s.taobao.com/search?q="+getKeyWord()+"&s="+value);
        }
        page.addTargetRequests(pageParameterList);
    }

    /**
     * 分析详情页
     * @param page
     * @param html
     * @param driver
     */
    private void analysisDetailPage(Page page,Html html,WebDriver driver){
       String url=page.getUrl().toString();
        if(url.startsWith(TAO_BAO_DETAIL_URL_START)){
            analysisTaoBaoDetailPage(page,html,driver);
        }else if(url.startsWith(TIAN_MAO_DETAIL_URL_START)){
            analysisTianMaoDetailPage(page,html,driver);
        }
    }

    /**
     * 分析淘宝详情页
     * @param page
     * @param html
     * @param driver
     */
    private void analysisTaoBaoDetailPage(Page page,Html html,WebDriver driver){
        page.putField("price", html.xpath("//[@id=\"J_StrPrice\"]/em[2]/text()").toString());
        page.putField("shopName", html.xpath("//*[@id=\"J_ShopInfo\"]/div/div[1]/div[1]/dl/dd/strong/a/text()").toString());
        page.putField("title", html.xpath("////*[@id=\"J_Title\"]/h3/text()").toString());

    }

    /**
     * 分析天猫详情页
     * @param page
     * @param html
     * @param driver
     */
    private void analysisTianMaoDetailPage(Page page,Html html,WebDriver driver){
        page.putField("price", html.xpath("//[@id=\"J_StrPriceModBox\"]/dd/span/text()").toString());
        page.putField("shopName", driver.findElement(By.name("seller_nickname")).getAttribute("value"));
        page.putField("name", html.xpath("//[@id=\"J_DetailMeta\"]/div[1]/div[1]/div/div[1]/h1/text()").toString());
    }



    /**
     * 是否为列表页
     * @param html
     * @return
     */
    private boolean isListPage(Html html) {
        String tmp = html.$("#mainsrp-pager").get();
        if (!Util.isEmpty((tmp))) {
            return true;
        }
        return false;
    }

    /**
     * 列表页获取当前页码
     * @param html
     * @return
     */
    private String getCurrentPageNo(Html html){
        return html.xpath("//*[@id=\"mainsrp-pager\"]/div/div/div/ul/li[contains(@class,'active')]/span/text()").toString();
    }

    /**
     * 判断是否列表页的第一页
     * @param html
     * @return
     */
    private Boolean isFirstPage(Html html){
        return isListPage(html)&&getCurrentPageNo(html).equals("1");
    }
}
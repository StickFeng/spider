package com.study.cn.spider;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.study.cn.model.SinaAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/6/1 9:43
 * @see
 * @since 1.0
 */
@Configuration
@Cacheable("loginSina")
public class Login {

    private Logger log = LoggerFactory.getLogger(Login.class);

    @Value("classpath:sinaAccount.txt")
    private Resource accountFile;

    private List<SinaAccount> sinaAccountList;

    private int accountIndex;

    public void init(){
        File file = null;
        try {
            if (accountFile != null){
                file = accountFile.getFile();
            }
            BufferedReader bff = new BufferedReader(new FileReader(file));
            String lineTxt = null;
            sinaAccountList = new ArrayList<>();
            while ((lineTxt = bff.readLine()) != null){
                String[] contents = lineTxt.split("&");
                if (contents != null && contents.length > 0){
                    SinaAccount sinaAccount = SinaAccount.builder().name(contents[0]).password(contents[1]).build();
                    sinaAccountList.add(sinaAccount);
                }
            }
            bff.close();
        }catch (Exception e){

        }
    }


    @Cacheable("loginSina")
    public WebClient getWebClient(){
        if (this.sinaAccountList == null){
            init();
        }
        WebClient webClient =  new WebClient(BrowserVersion.CHROME);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getCookieManager().setCookiesEnabled(true);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiesEnabled(true);
        //依次循环拿取队列中的新浪账户
        if (accountIndex >= sinaAccountList.size()){
            log.error("新浪微博登陆失败所有账户都无法登陆");
            return null;
        }
        try {
            HtmlPage page1 = webClient.getPage("https://passport.weibo.cn/signin/login?entry=mweibo&r=http%3A%2F%2Fweibo.cn%2F&backTitle=%CE%A2%B2%A9&vt=");
            HtmlInput in = page1.getHtmlElementById("loginName");
            HtmlInput pwd = page1.getHtmlElementById("loginPassword");
            // HtmlInput btn = page1.getFirstByXPath(".//*[@id='vForm']/div[2]/div[1]/ul/li[7]/div[1]/input");
            HtmlAnchor btn = page1.getHtmlElementById("loginAction");
            in.setAttribute("value", sinaAccountList.get(accountIndex).getName());
            pwd.setAttribute("value", sinaAccountList.get(accountIndex).getPassword());
            HtmlPage page2 = btn.click();
            webClient.setJavaScriptTimeout(60 * 1000);
            String logo = "";
            try {
                logo = page2.getHtmlElementById("top").getTextContent();
            }catch (ElementNotFoundException e){
                System.err.println(String.format("登陆失败重新登陆,异常账号%s",sinaAccountList.get(accountIndex).getName()));
                accountIndex ++;
                getWebClient();
            }
            if ("广场".equals(logo)){
                System.err.println("微博登陆成功!");
            }else {
                System.err.println(String.format("登陆失败重新登陆,失败账号%s", sinaAccountList.get(accountIndex).getName()));
                accountIndex ++;
                getWebClient();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return webClient;
    }

}

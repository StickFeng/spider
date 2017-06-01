package com.study.cn.spider;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.study.cn.util.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/5/24 10:03
 * @see
 * @since 1.0
 */
@Component
public class SinaSpider {

    @Autowired
    private Login loginSina;

    private WebClient webClient;

    public String spiderUserTopic(String nickname) {
        this.webClient = loginSina.getWebClient();
        if (this.webClient == null){
            return null;
        }
        try {
            HtmlPage page1 = this.webClient.getPage("https://weibo.cn/find/");
            webClient.waitForBackgroundJavaScript(1 * 60 * 1000);
            HtmlInput ipt = null;
            HtmlInput btn = null;
            //新浪微博手机端登陆节点会改变，暂时只发现两种情况
            HtmlInput ipt1 = page1.getFirstByXPath("/html/body/div[3]/form/div[1]/input[1]");
            HtmlInput btn1 = page1.getFirstByXPath("/html/body/div[3]/form/div[1]/input[3]");
            HtmlInput ipt2 = page1.getFirstByXPath("/html/body/div[4]/form/div[1]/input[1]");
            HtmlInput btn2 = page1.getFirstByXPath("/html/body/div[4]/form/div[1]/input[3]");
            if ((ipt1 == null || btn1 == null) && (ipt2 == null || btn2 == null)) {
                return null;
            }
            if (ipt1 != null && ipt1 != null) {
                ipt = ipt1;
                btn = btn1;
            }
            if (ipt2 != null && ipt2 != null) {
                ipt = ipt2;
                btn = btn2;
            }
            ipt.setAttribute("value", nickname);
            HtmlPage page2 = btn.click();
            webClient.waitForBackgroundJavaScript(1 * 60 * 1000);
            HtmlAnchor anchor = page2.getFirstByXPath("/html/body/table/tbody/tr/td[2]/a");
            if (anchor == null) {
                return null;
            }
            String finder = anchor.getTextContent();
            if (StringUtils.isNotEmpty(finder) && nickname.equals(finder.trim())) {
                String uid = anchor.getAttributesMap().get("href").getValue();
                return getSingleTopic(webClient, uid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单个微博内容
     * @param webClient
     * @param uid
     * @return
     */
    private String getSingleTopic(WebClient webClient, String uid) {
        if (uid == null) {
            return null;
        }
        try {
            HtmlPage htmlPage = webClient.getPage("https://weibo.cn" + uid);
            webClient.waitForBackgroundJavaScript(10000);
            for (DomNode temp : getDivs(htmlPage)) {
                DomNodeList<DomNode> result = temp.querySelectorAll(".ctt");
                if (result != null && result.getLength() > 0) {
                    return StrUtils.filterEmoji(result.get(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 截取uid
     * @param xml
     * @return
     */
    public String uidFormat(String xml) {
        if (xml == null) {
            return null;
        }
        String[] str = xml.split("uid=\"");
        String result = "";
        if (str != null) {
            result = str[1].substring(0, str[1].indexOf("\""));
        }
        return result;
    }

    public List<DomNode> getDivs(HtmlPage htmlPage) {
        if (htmlPage == null) {
            return null;
        }
        List<DomNode> divs = new ArrayList<>();
        divs = htmlPage.querySelectorAll(".c");
        return divs;
    }
}

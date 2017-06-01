# 使用前必读


该项目基于gradle搭建的springboot项目，使用时需要导入gradle（任意版本即可）

项目爬取的是新浪手机端内容，暂时项目中只取了最新一条内容，需要多条可以在SinaSpider.java类中放开对第一条的限制，
项目使用的是htmlunit插件，实现了模拟登陆，所有的用于模拟登陆的账号皆放在sinaAccount.txt文件中，
项目使用了定时任务，使用者可以自行调整启动时间并且开启了多线程任务，使用者可根据实际情况调整线程数。爬取的内容暂时放于本地F盘中。

## lombok安装
    * setting--->plugins--->搜索lombok，安装重启
    
    启用lombok：
    setting--->annotation processors 勾选 enable annotation processing
    
## 消除spring警告
    setting--->Inspections--->spring--->Severity--->Warning

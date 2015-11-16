package yun.run;

import yun.model.Article;
import yun.model.WeApp;
import yun.mysql.dao.WeAppDao;
import yun.mysql.db.MysqlDB;
import yun.spider.ArticlesByAppSpider;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/22.
 */
public class TestArticlesByAppSpider {

    public static void main(String[] args) throws SQLException {
        List<WeApp> apps = WeApp.getAll();
        WeApp app =  apps.get(0);
        ArticlesByAppSpider sp = new ArticlesByAppSpider();
        sp.crawlArticles(app);
//        for (int i = 0; i < apps.size(); i++) {
//            WeApp app =  apps.get(i);
//            ArticlesByAppSpider sp = new ArticlesByAppSpider();
//            sp.crawlArticles(app);
//        }
//        List apps = WeAppDao.getAll();
//        //遍历所有公众号
//        for (int i = 0; i < apps.size(); i++) {
//            Map<String, Object> app = (Map<String, Object>) apps.get(i);
//            String appName = (String) app.get("nick_name");
//            String appCode = (String) app.get("alias");
//            appName = "hellodd";
//            appCode = "123";
//            //过滤空的数据
//            if (!appCode.equals("") && !appCode.equals("") && !appCode.equals(appName)) {
//                String lastDocid = WeAppDao.getLastDocidByApp(appName,appCode);
//                ArticlesByAppSpider sp = new ArticlesByAppSpider();
////                List<Article> articles = sp.spider(appName, appCode, lastDocid);
////                if (articles.size() > 0) {
////                    //文章存入数据库
////                    for (Article article : articles) {
////                        ArticleDao articleDao = new ArticleDao();
////                        article.setApp_name(appName);
////                        article.setApp_code(appCode);
////                        int res = articleDao.save(article);
////                        System.out.println(res);
////                    }
////                    //记录日志
////                    Log log = new Log();
////                    log.setApp_name(appName);
////                    log.setApp_code(appCode);
////                    log.setDocid(articles.get(0).getDocid());
////                    log.setCol_num(articles.size());
////                    LogDao.save(log);
////                }
//            } else {
//                System.out.println(appName + appCode + "appName或者appCode为空");
//            }
//        }
////        MysqlDB.closeAll();
    }
}

import yun.model.Article;
import yun.model.WeApp;

import yun.mongodb.dao.ArticleDao;
import yun.spider.ArticlesByAppSpider;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Administrator on 2015/9/23.
 */
public class SpByNickToMongo {
    public static void main(String[] args) throws IllegalAccessException, UnknownHostException, InterruptedException {

//
//        ArticleDao articleDao = new ArticleDao();
//        Article article = new Article();
//        article.setDocid("jsjskjakjsak");
//        article.setTitle("这是标题");
//        article.setOpenid("OPENID");
//        article.setWx_app_id("WX_APP_ID");
//        int res = articleDao.save(article);
//        System.out.println(res);
//        System.exit(0);


        List apps = WeApp.getAll();
        for(int i=0;i<apps.size();i++){
            //抓取数据
            ArticlesByAppSpider sp = new ArticlesByAppSpider();
            WeApp app = (WeApp)apps.get(i);
            System.out.println(app.nick);
            String appNick = app.nick;
            String appCode = app.appcode;
            String last_article_docid = "";
//            List<Article> articles = sp.spider(appNick, appCode,last_article_docid);
            String openid = app.openid;
            String wx_app_id = app.wx_app_id;
//            //文章存入数据库
//            for(Article article:articles){
//                ArticleDao articleDao = new ArticleDao();
//                article.setOpenid(openid);
//                article.setWx_app_id(wx_app_id);
//                int res = articleDao.save(article);
//                System.out.println(res);
//            }
        }
    }
}

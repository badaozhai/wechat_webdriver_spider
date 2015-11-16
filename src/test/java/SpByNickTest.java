import yun.spider.ArticlesByAppSpider;
import yun.model.Article;
import yun.model.WeApp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class SpByNickTest {

    public static void main(String[] args) throws SQLException, InterruptedException, IOException {

        List apps = WeApp.getAll();
        for(int i=0;i<apps.size();i++){
            //抓取数据
            ArticlesByAppSpider sp = new ArticlesByAppSpider();
            WeApp app = (WeApp)apps.get(i);
            System.out.println(app.nick);
            String appNick = app.nick;
            String appCode = app.appcode;
            String last_article_docid = "";
//            List<Article> articles = sp.spiderByProxy(appNick, appCode, last_article_docid);
//            String openid = app.openid;
//            String wx_app_id = app.wx_app_id;
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

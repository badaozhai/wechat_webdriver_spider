package yun.run;

import yun.model.Article;
import yun.mysql.dao.TagDao;
import yun.spider.ArticlesByWordSpider;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/12.
 */
public class TestArticlesByWordSpider {

    public static void main(String[] args) throws SQLException {
        List tags = TagDao.getAll();
        for (int i = 0; i < tags.size(); i++) {
            Map<String, Object> app = (Map<String, Object>) tags.get(i);
            String tag = (String) app.get("tag_name");
            String lastdocid = "";
            yun.spider.ArticlesByWordSpider sp = new yun.spider.ArticlesByWordSpider();
            List<Article> articles = sp.spider(tag,lastdocid);
            System.out.println(articles.size());
            //文章存入数据库
//            for (Article article : articles) {
//                article.setKeyWords(tag);
//                ArticleDao articleDao = new ArticleDao();
//                int res = articleDao.save(article);
//                System.out.println(res);
//            }
        }
    }
}

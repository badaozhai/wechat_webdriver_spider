package yun.run;

import yun.model.Article;
import yun.mysql.dao.ArticleDao;
import yun.mysql.dao.ArticleTypeDao;
import yun.mysql.db.MysqlDB;
import yun.spider.SougouIndexSpider;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 */
public class TestSougouIndexSpider {

    public static void main(String args[]) throws SQLException, IOException, InterruptedException, ParseException {
        SougouIndexSpider sp = new SougouIndexSpider();
        ArticleTypeDao typeDao = new ArticleTypeDao();
        for(int i = 0;i<20;i++){
            //等待0.5秒
            Thread.sleep(500);
            //类别id
            int typeId = i+1;
            //获取该类别下采集到的最新的文章docid
            String lastDocid = typeDao.getLastDocid(typeId);
            System.out.println("类别id:"+typeId+"最后docid:"+lastDocid);
            //通过索引拼接url用get方式抓取文章数据
            List<Article> articles = sp.getArticlesByUrl(i,lastDocid);
            if(articles.size()>0){
                //文章存入数据库
                for(Article article:articles){
                    ArticleDao articleDao = new ArticleDao();
                    int res = articleDao.save(article);
                    if(res == 0){
                        System.out.println("main:插入失败");
                        System.out.println("失败文章"+article.getUrl());
                        System.out.println("文章长度"+article.getContent().length());
                    }else{
                        System.out.println("插入文章--"+article.getTitle()+"---成功");
                    }
                    //todo 将文章上的公众号昵称 openid 存入微信号基本信息表

                    /**
                     * 如果openid存在就不存 如果openid不存在就存
                     */
                }
                //将最新一条的文章docid写到采集类别去
                Article firstArticle = articles.get(0);
                String docid = firstArticle.getDocid();
                int res = typeDao.saveLastDocid(typeId,docid);
                System.out.println("更新类别"+typeId+"最后的docid:"+docid);
                System.out.println("更新类别"+typeId+"受影响的行:"+res);
            }else{
                System.out.println("类别id:"+typeId+"没有抓到数据");
                System.out.println("类别id:"+typeId+"最新的docid是:"+lastDocid);
                System.out.println("");
                System.out.println("");
            }
        }
        MysqlDB.closeAll();
    }
}

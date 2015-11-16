package yun.mysql.dao;

import yun.model.Article;
import yun.mysql.db.MysqlDB;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ArticleTypeDao {
    //通过id
    public String getLastDocid(int id){
        MysqlDB db = new MysqlDB();
        String sql = "select last_coll_article_docid from tg_collect_article_type where id = "+id;
        Object obj = db.executeQuerySingle(sql,null);
        String docid = obj.toString();
        return docid;
    }

    public int saveLastDocid(int id,String docid){
        MysqlDB db = new MysqlDB();
        String sql = "update tg_collect_article_type set last_coll_article_docid = '"+docid+"' where id = "+id;
        System.out.println(sql);
        int res = db.executeUpdate(sql,null);
        return res;
    }
}

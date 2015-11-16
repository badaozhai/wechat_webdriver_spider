package yun.mysql.dao;


import yun.model.Article;
import yun.mysql.db.MysqlDB;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
       //保存到数据库
    public int save(Article article) throws SQLException {
        MysqlDB db = new MysqlDB();
        String findSql = "select count(1) from tg_collect_article_temp where docid = ?";
        String insertSql = "insert into tg_collect_article_temp " +
                "(docid,wx_app_id,app_name,app_code,openid,title,url,imglink,description,last_modified,create_time,content,typeid,read_num,keywords) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //判断数据库中是否存在
        ArrayList findParam = new ArrayList();

        findParam.add(article.getDocid());
        Object obj = db.executeQuerySingle(findSql, findParam.toArray());
        int count ;
        if(obj == null){
            count = 0;
        }else{
            count = Integer.parseInt(String.valueOf(obj));
        }
        //插入数据库
        int i = 0;
        if(0 == count){
            ArrayList insertValue = new ArrayList();
            insertValue.add(article.getDocid());
            insertValue.add(article.getWx_app_id());
            insertValue.add(article.getApp_name());
            insertValue.add(article.getApp_code());
            insertValue.add(article.getOpenid());
            insertValue.add(article.getTitle());
            insertValue.add(article.getUrl());
            insertValue.add(article.getImglink());
            insertValue.add(article.getDescription());
            insertValue.add(article.getLast_modified());
            insertValue.add(new Timestamp(System.currentTimeMillis()));
            insertValue.add(article.getContent());
            insertValue.add(article.getTypeid());
            insertValue.add(article.getRead_num());
            insertValue.add(article.getKeyWords());
            i = db.executeUpdate(insertSql,insertValue.toArray());
            if(i == 0){
                System.out.println("插入文章失败");
                System.out.println(article.getUrl());
                System.out.println(article.getContent().length());
            }
        }else{
            System.out.println("文章--"+article.getTitle()+"--已经存在");
        }
        return i;
    }

    /**
     * 获取所有不正常的文章
     * @return
     */

    public List findNormalArticles(int status){

        return null;
    }
}

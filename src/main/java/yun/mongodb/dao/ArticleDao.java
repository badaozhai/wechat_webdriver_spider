package yun.mongodb.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import yun.model.Article;
import yun.mongodb.db.MongoDbDB;

import java.lang.reflect.Field;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2015/9/23.
 */
public class ArticleDao {
    private static DBCollection articles;
    public int save(Article article) throws UnknownHostException, IllegalAccessException {
        int k = 1;
        MongoDbDB mongoDbDB = new MongoDbDB();
        articles = mongoDbDB.getColl("articles");
        DBCursor cur = articles.find(new BasicDBObject("docid", article.getDocid()));
        //判断数据库中是否存在
        if(cur.hasNext() == false){

            DBObject artObj = new BasicDBObject();

            Class cls = article.getClass();
            System.out.println(cls.getName());
            Field[] flds = cls.getDeclaredFields();
            if ( flds != null )
            {
                for ( int i = 1; i < flds.length; i++ )
                {
                    Field f = flds[i];
                    f.setAccessible(true);

                    System.out.println(f.getName() + " - " + f.get(article));
                    artObj.put(f.getName(),f.get(article));
                }
            }

            k = articles.insert(artObj).getN();
        }
        return k;
    }

}

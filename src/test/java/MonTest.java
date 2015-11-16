import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import yun.mongodb.db.MongoDbDB;

import java.net.UnknownHostException;

/**
 * Created by Administrator on 2015/9/24.
 */
public class MonTest {
    private static DBCollection articles;
    public static void main(String args[]) throws UnknownHostException {
        MongoDbDB mongoDbDB = new MongoDbDB();
        articles = mongoDbDB.getColl("articles");
        DBCursor cur1 = articles.find();
        DBCursor cur = articles.find(new BasicDBObject("docid","hhh"));
        while (cur.hasNext()) {
            System.out.println(cur.next());
        }
    }
}

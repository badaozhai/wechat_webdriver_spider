import yun.mongodb.db.MongoDbDB;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/9/21.
 */
public class MongoDemo2 {
    private static DBCollection users;
    public static void main( String args[] ){
        try{
            // 连接到数据库
            MongoDbDB mongoDbDB = new MongoDbDB();
            users = mongoDbDB.getColl("users");
            //users集合 就是 mysql中的users表
            add();
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static void queryAll() {
        //db游标
        DBCursor cur = users.find();
        while (cur.hasNext()) {
            System.out.println(cur.next());
        }
    }
    public static void add() {
        //先查询所有数据
        System.out.println("count: " + users.count());
        queryAll();
        //插入一条数据
//        DBObject user = new BasicDBObject();
//        user.put("name", "hoojo");
//        user.put("age", 24);
//        int row = users.save(user).getN();//保存，getN()获取影响行数
//        System.out.println(row);
//        //扩展字段，随意添加字段，不影响现有数据
//        user.put("sex", "男");
//        System.out.println(users.save(user).getN());


        //添加多条数据
        List list = new ArrayList();


        DBObject user = new BasicDBObject();
        user.put("name", "hahaha");
        user.put("age", 333);
        list.add(user);


        DBObject user2 = new BasicDBObject("name", "lucy");
        user.put("age", 22);
        list.add(user2);
        //添加List集合
        System.out.println(users.insert(list).getN());
        //查询下数据，看看是否添加成功
        System.out.println("count: " + users.count());
        queryAll();
    }

    public static void remove() {
        queryAll();
        System.out.println("删除id = 55ffab267491d3ab35c742fa：" + users.remove(new BasicDBObject("_id", new ObjectId("55ffab267491d3ab35c742fa"))).getN());
        System.out.println("remove age >= 23: " + users.remove(new BasicDBObject("age", new BasicDBObject("$gte", 23))).getN());
        queryAll();
    }
}

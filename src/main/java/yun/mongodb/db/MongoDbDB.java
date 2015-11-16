package yun.mongodb.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.net.UnknownHostException;
import java.util.Collection;

/**
 * Created by Administrator on 2015/9/23.
 */
public class MongoDbDB {
    private DB db = null;
    private Mongo mongo = null;
    private DBCollection coll = null;
    public Mongo getMongo() throws UnknownHostException {
        mongo = new Mongo("127.0.0.1",27017);
        return mongo;
    }
    public DB getDb(String dbName) throws UnknownHostException {
        mongo = this.getMongo();
        db = mongo.getDB(dbName);
        return db;
    }
    public DBCollection getColl(String collName) throws UnknownHostException {
        mongo = new Mongo("127.0.0.1",27017);
        db = mongo.getDB("test");
        coll =  db.getCollection(collName);
        return coll;
    }
}

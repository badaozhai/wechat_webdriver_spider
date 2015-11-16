package yun.mysql.dao;

import yun.model.Article;
import yun.model.Log;
import yun.mysql.db.MysqlDB;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/16.
 */
public class LogDao {
    //保存到数据库
    public static int save(Log log) throws SQLException {
        MysqlDB db = new MysqlDB();
        String insertSql = "insert into tg_collect_log " +
                "(app_name,app_code,docid,col_time,col_num) " +
                "values(?,?,?,?,?)";
        //插入数据库
        int i = 0;
        ArrayList insertValue = new ArrayList();
        insertValue.add(log.getApp_name());
        insertValue.add(log.getApp_code());
        insertValue.add(log.getDocid());
        insertValue.add(new Timestamp(System.currentTimeMillis()));
        insertValue.add(log.getCol_num());
        i = db.executeUpdate(insertSql,insertValue.toArray());
        if(i == 0){
            System.out.println("log插入失败");
        }
        return i;
    }
}

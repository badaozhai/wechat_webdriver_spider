package yun.mysql.dao;

import yun.mysql.db.MysqlDB;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/23.
 * 微信号数据处理类
 */
public class TagDao {
    public static List getAll(){
        MysqlDB db = new MysqlDB();
        String sql = "select * from tg_tags";
        List list = db.excuteQuery(sql,null);
        return list;
    }
}

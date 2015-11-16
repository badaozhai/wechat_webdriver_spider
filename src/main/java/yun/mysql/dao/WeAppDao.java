package yun.mysql.dao;

import yun.mysql.db.MysqlDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/23.
 * 微信号数据处理类
 */
public class WeAppDao {
    public static List getAll(){
        MysqlDB db = new MysqlDB();
        String sql = "select * from tg_wechat_app_info where openid is not null and nick_name is not null and nick_name != '' and alias is not null and alias != ''";
        List appList = db.excuteQuery(sql,null);
        return appList;
    }

//    public static String getLastDocidByApp(String appName,String appCode){
//        String docid = "";
//        MysqlDB db = new MysqlDB();
//        String sql = "select * from tg_collect_log where app_name = '"+appName+"' and app_code = '"+appCode+"' order by col_time desc limit 1";
//        List li = db.excuteQuery(sql, null);
//        if(li.size()>0){
//            Object obj = li.get(0);
//            Map<String, Object> map = (Map<String, Object>)obj;
//            docid = (String) map.get("docid");
//        }
//        return docid;
//    }

    public static int updateLastDocid(String openid,String lastdocid){
        MysqlDB db = new MysqlDB();
        String sql = "update tg_wechat_app_info set last_docid = '"+lastdocid+"' where openid = '"+openid+"'";
        int res = db.executeUpdate(sql,null);
        return res;
    }
}

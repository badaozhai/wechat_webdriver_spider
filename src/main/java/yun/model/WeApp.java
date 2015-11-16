package yun.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class WeApp {
    public String nick;
    public String appcode;
    public String wx_app_id;
    public String openid;
    public String last_docid;
    public WeApp(String _nick,String _appcode){
        nick = _nick;
        appcode = _appcode;

    }
    public WeApp(String _nick,String _appcode,String _openid,String _last_docid){
        this.nick = _nick;
        this.appcode = _appcode;
        this.openid = _openid;
        this.last_docid = _last_docid;
    }
    public static List<WeApp> getAll(){
        List<WeApp> apps = new ArrayList<WeApp>();
        WeApp app;

        app = new WeApp("创意DIY","ideadiy","wxd577298732e4bb62","oIWsFt2-sRAkWPTO6Aa9JNaCBdq8");
        apps.add(app);

        app = new WeApp("XY500W","xy500wjd");
        apps.add(app);

        app = new WeApp("hellodd","123");
        apps.add(app);

        app = new WeApp("玖拾度智能衣柜橱柜","jiushidujiaju","wxcbe10d357ef9d027","oIWsFt1QOPjqclNGixTja8fTEE5Q");
        apps.add(app);

        app = new WeApp("师兄网","vipshixiong","wx301b65f4b691805a","");
        apps.add(app);

        app = new WeApp("芙蓉王","fu-rongwang","","");
        apps.add(app);

        app = new WeApp("长沙里手","changshatong","","");
        apps.add(app);

        app = new WeApp("长沙吃货","cschwx","","");
        apps.add(app);

        app = new WeApp("赣州晚报","ganzhouwanbao","","");
        apps.add(app);

        app = new WeApp("笑到你岔气","xdcq2012","","");
        apps.add(app);

        app = new WeApp("精致佳人","jznr987","","");
        apps.add(app);

        app = new WeApp("做个会搭配的女子","vopdapei","","");
        apps.add(app);

        app = new WeApp("大风哥","dfsws520","","");
        apps.add(app);

        app = new WeApp("悦读","yuedu58","","");
        apps.add(app);

        app = new WeApp("花花酱","dululu531","","");
        apps.add(app);

        app = new WeApp("嗨小冷","sogaad","","");
        apps.add(app);

        app = new WeApp("男人装","nrz200405","","");
        apps.add(app);

        app = new WeApp("每日头条新闻","xinwen258","","");
        apps.add(app);

        app = new WeApp("娱乐扒皮王","yulebpw","","");
        apps.add(app);

        app = new WeApp("互联网情报网","iqingbao","","");
        apps.add(app);

        app = new WeApp("石家庄新鲜事","SjzXinXianShi","","");
        apps.add(app);

        app = new WeApp("考研教育网","cnedu_cn","","");
        apps.add(app);

        app = new WeApp("巢湖发布","chaohufabu","","");
        apps.add(app);

        app = new WeApp("观察者网","guanchacn","","");
        apps.add(app);

        app = new WeApp("交通91.8","hzfm918","","");
        apps.add(app);

        app = new WeApp("腾讯财经","financeapp","","");
        apps.add(app);

        app = new WeApp("汽车迷","iqcfans","","");
        apps.add(app);

        app = new WeApp("环球老虎财经","laohucaijing01","","");
        apps.add(app);

        app = new WeApp("SPG","SPG-Starwood","","");
        apps.add(app);

        app = new WeApp("西雅图生活","WeSeattle","","");
        apps.add(app);

        app = new WeApp("证券之星","stockstarnews","","");
        apps.add(app);

        app = new WeApp("政商内参","zsnc-ok","","");
        apps.add(app);

        app = new WeApp("App运营之家","app888app","","");
        apps.add(app);

        app = new WeApp("月光宝盒MB","MoonBox2015","","");
        apps.add(app);

        app = new WeApp("每日股市精选","stock22","","");
        apps.add(app);

        return apps;
    }

}

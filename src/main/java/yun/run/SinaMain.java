package yun.run;

import yun.post.sina.Sina;


/**
 * Created by Administrator on 2015/10/12.
 */
public class SinaMain {
    public static void main(String[] args) {
        Sina sn = new Sina();
        sn.init();
        //模拟登录
        String username = "18652064585";
        String password = "chenjian007";
        String title = "天好";
        String content = "今天天气不错";
        sn.login(username,password);
        sn.post(title,content);
    }
}

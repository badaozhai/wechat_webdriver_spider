import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/24.
 */
public class spiderIndexTest {
    public static void main(String args[]) throws ParseException {
        String os = System.getProperty("os.name");
        System.out.println(os);
        System.out.println(os.contains("Windows"));
        System.exit(0);
        String str = "分享 收藏 您确定要取消该收藏？确定再想想 收藏成功！ 在\"我的收藏\"可查看所有收藏内容。 阅读 100000+   11:46";
        String url = "http://weixin.sogou.com/gzh?openid=oIWsFt_hovRnK2DvtKSNDtmuf2jU&ext=rgljaGmlw7Af1NAOK6ce5hPpMdDGaSuYimhgKxB0izw-KQ42q-vsyMQMyrWPMU_i";

        Pattern p = Pattern.compile("\\d\\d*");
        Matcher m = p.matcher(str);
        m.find();
        int redNum = Integer.parseInt(m.group());
        System.out.println("阅读数:" + redNum);

        Pattern p2 = Pattern.compile("\\d{2}:\\d{2}");
        Matcher m2 = p2.matcher(str);
        m2.find();
        String t = m2.group();
        System.out.println("时间:" + t);
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String timeString = String.format("%s-%s-%s %s", year, month,day,t);
        System.out.println(timeString);
        int time = 0;
        try {
            String format = "yyyy-MM-dd HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(timeString);
            String strTime = date.getTime() + "";
            strTime = strTime.substring(0, 10);
            time = Integer.parseInt(strTime);
            System.out.println(time);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
//        return time;
//        Pattern p3 = Pattern.compile("(?<=openid=).*(?=&)");
//        Matcher m3 = p3.matcher(url);
//        m3.find();
//        String openid = m3.group();
//        System.out.println("openid:"+openid);


//        Calendar now = Calendar.getInstance();
//        System.out.println("年: " + now.get(Calendar.YEAR));
//        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
//        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
//        System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));
//        System.out.println("分: " + now.get(Calendar.MINUTE));
//        System.out.println("秒: " + now.get(Calendar.SECOND));
//        System.out.println("当前时间毫秒数：" + now.getTimeInMillis());
//        System.out.println(now.getTime());
//        Date d = new Date();
//        System.out.println(d);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateNowStr = sdf.format(d);
//        System.out.println("格式化后的日期：" + dateNowStr);
//
//        String str2 = "2012-1-13 17:26:33";  //要跟上面sdf定义的格式一样
//        Date today = sdf.parse(str2);
//        System.out.println("字符串转成日期：" + today);

//        return time;
//        ArticleTypeDao adao = new ArticleTypeDao();
//        int id = 1;

//        String docid = "abc";
//        int res = adao.saveLastDocid(id, docid);
//        System.out.println(res);

//        String docid = adao.getLastDocid(id);
//        System.out.println(docid);
    }
}

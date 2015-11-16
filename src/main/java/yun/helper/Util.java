package yun.helper;

/**
 * 通用工具类
 */
public class Util {
    /**
     * 随机时间睡眠 1秒到10秒之间
     */
    public static void sleepRandom(){
        int start = 1000;
        int end = 1000*10;
        sleepRandom(start, end);
    }
    /**
     * 随机时间睡眠 多少毫秒到多少毫秒之间
     */
    public static void sleepRandom(int start,int end){
        int mims = (int) Math.round(Math.random() * (end - start) + start);
        try {
            System.out.println("等待:"+mims+"毫秒");
            Thread.sleep(mims);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

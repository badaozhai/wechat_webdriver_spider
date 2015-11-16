package yun.model;

/**
 * Created by Administrator on 2015/10/16.
 */
public class Log {
    private int id;
    private String app_name;
    private String app_code;
    private String docid;
    private int col_time;
    private int col_num;

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public int getCol_time() {
        return col_time;
    }

    public void setCol_time(int col_time) {
        this.col_time = col_time;
    }

    public int getCol_num() {
        return col_num;
    }

    public void setCol_num(int col_num) {
        this.col_num = col_num;
    }
}

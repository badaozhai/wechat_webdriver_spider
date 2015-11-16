package yun.model;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ArticleType {
    private int id;
    private String type_name;
    private String collect_url;
    private int num;
    private int collect_time;
    private String last_coll_article_docid;

    public int getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(int collect_time) {
        this.collect_time = collect_time;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCollect_url() {
        return collect_url;
    }

    public void setCollect_url(String collect_url) {
        this.collect_url = collect_url;
    }

    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getLast_coll_article_docid() {
        return last_coll_article_docid;
    }
    public void setLast_coll_article_docid(String last_coll_article_docid) {
        this.last_coll_article_docid = last_coll_article_docid;
    }
}

package yun.model;

public class Article {
	private int id;



	private int typeid;
	private String docid;
	private String wx_app_id;
	public  String app_code;
	private String app_name;
	private String openid;
	private String title;
	private String url;
	private String imglink;
	private String description;
	private String create_time;
	private String content;
	private int last_modified;
	private int read_num;
	private int like_num;
	private int is_collect;

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	private String keyWords;
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


	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getWx_app_id() {
		return wx_app_id;
	}
	public void setWx_app_id(String wx_app_id) {
		this.wx_app_id = wx_app_id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImglink() {
		return imglink;
	}
	public void setImglink(String imglink) {
		this.imglink = imglink;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(int last_modified) {
		this.last_modified = last_modified;
	}
	public int getRead_num() {
		return read_num;
	}
	public void setRead_num(int read_num) {
		this.read_num = read_num;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public int getIs_collect() {
		return is_collect;
	}
	public void setIs_collect(int is_collect) {
		this.is_collect = is_collect;
	}

}

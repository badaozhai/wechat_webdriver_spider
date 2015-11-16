
import yun.mysql.db.MysqlDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDemo {



	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String docid = "ab735a258a90e8e1-6bee54fcbd896b2a-47f854d9619061f7a787c69ff7402b1b";
		// 使用数据库小工具 --自己封装的
		MysqlDB db = new MysqlDB();
		Connection conn = db.getConnection();
		// 3.操作数据库，实现增删改查
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT count(1) FROM tg_media_send_article where docid = '" +docid+"'");
		System.out.println(rs);
		// 如果有数据，rs.next()返回true
		while (rs.next()) {
			System.out.println(rs.getInt(1));
		}
	}

}

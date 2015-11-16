package yun.mysql.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlDB {

	private static final String DRIVER = "com.mysql.jdbc.Driver";

	/**
	 * 本地环境
	 */
//	private static final String HOST = "localhost:3306";
//	private static final String DBname = "tuiguang";
//	private static final String USER = "root";
//	private static final String PASSWORD = "123456";


	/**
	 * 创建数据库连接对象
	 */
	private static Connection conn = null;
	/**
	 * 创建PreparedStatement对象
	 */
	private static PreparedStatement preparedStatement = null;

	/**
	 * 创建CallableStatement对象
	 */
	private static CallableStatement callableStatement = null;

	/**
	 * 创建结果集对象
	 */
	private static ResultSet resultSet = null;

	static {
		try {
			// 加载驱动程序
			Class.forName(DRIVER);
			System.out.println("加载数据库驱动程序");
		} catch (ClassNotFoundException e) {
			System.out.println("数据库驱动加载出问题啦啦"+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 建立数据库连接
	 * @return 数据库连接
	 */
	public  Connection getConnection() {
		if(conn !=null){
			System.out.println("获取已经存在的数据库连接");
			return conn;
		}else {
			try {
				String conStr = "jdbc:mysql://"+HOST +"/"+DBname+"?user="+USER+"&password="+PASSWORD+"&useUnicode=true&characterEncoding=utf-8";
				System.out.println("获取新的数据库连接");
				conn = DriverManager.getConnection(conStr);
			} catch (SQLException e) {
				System.out.println("获取库连接获取出问题啦" + e.getMessage());
				e.printStackTrace();
			}
			return conn;
		}
	}
	/**
	 * insert update delete SQL语句的执行的统一方法
	 * @param sql SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return 受影响的行数
	 */
	public int executeUpdate(String sql, Object[] params) {
		// 受影响的行数
		int affectedLine = 0;
		try {
			// 获得连接
			conn = this.getConnection();
			// 调用SQL
			preparedStatement = conn.prepareStatement(sql);

			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			// 执行
			affectedLine = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 释放资源
//			closeAll();
		}
		return affectedLine;
	}
	/**
	 * SQL 查询将查询结果直接放入ResultSet中
	 * @param sql SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return 结果集
	 */
	private ResultSet executeQueryRS(String sql, Object[] params) {
		try {
			// 获得连接
			conn = this.getConnection();

			// 调用SQL
			preparedStatement = conn.prepareStatement(sql);

			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}

			// 执行
			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return resultSet;
	}
	/**
	 * SQL 查询将查询结果：一行一列
	 * @param sql SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return 结果集
	 */
	public Object executeQuerySingle(String sql, Object[] params) {
		Object object = null;
		try {
			// 获得连接
			conn = this.getConnection();
			// 调用SQL
			preparedStatement = conn.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}

			// 执行
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				object = resultSet.getObject(1);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
//			closeAll();
		}

		return object;
	}
	/**
	 * 获取结果集，并将结果放在List中
	 *
	 * @param sql
	 *            SQL语句
	 * @return List
	 *                       结果集
	 */
	public List<Object> excuteQuery(String sql, Object[] params) {
		// 执行SQL获得结果集
		ResultSet rs = executeQueryRS(sql, params);

		// 创建ResultSetMetaData对象
		ResultSetMetaData rsmd = null;

		// 结果集列数
		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();

			// 获得结果集列数
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}

		// 创建List
		List<Object> list = new ArrayList<Object>();

		try {
			// 将ResultSet的结果保存到List中
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// 关闭所有资源
//			closeAll();
		}

		return list;
	}
	/**
	 * 关闭所有资源
	 */
	public static void closeAll() {
		// 关闭结果集对象
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		// 关闭PreparedStatement对象
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		// 关闭CallableStatement 对象
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		// 关闭Connection 对象
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println("数据库资源关闭");
	}
}

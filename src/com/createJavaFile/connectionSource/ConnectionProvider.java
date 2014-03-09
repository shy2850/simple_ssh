package com.createJavaFile.connectionSource;

import java.io.PrintStream;
import java.sql.SQLException;

import com.createJavaFile.Main.ConnectionImpl;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.IConnectionProvider;

/** ϵͳ��һ�޶������Ӳ����� */
class ConnectionProvider implements IConnectionProvider{
	
	/**���ݿ�����Դ֮--JDBC����·��*/
	private static String JDBC_DRIVER;
	/**���ݿ�����Դ֮--���ݿ�·��*/
	private static String DB_URL;
	/**���ݿ�����Դ֮--���ݿ��û���*/
	private static String DB_USER;
	/**���ݿ�����Դ֮--���ݿ�����*/
	private static String DB_PASSWORD;

	private PrintStream out = System.out;
		
	/**<pre>
	 * ͨ��db.conf�е������ֶ�
	 * JDBC_DRIVER|DB_URL|DB_USER|DB_PASSWORD
	 * ��������ļ���
	 * </pre>
	 * */
	ConnectionProvider() {
		JDBC_DRIVER = PropertyReader.get("JDBC_DRIVER");
		DB_URL = PropertyReader.get("DB_URL");
		DB_USER = PropertyReader.get("DB_USER");
		DB_PASSWORD = PropertyReader.get("DB_PASSWORD");
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			out.println("�����ļ�·������");
		}
	}

	/**ͨ����ǰ(�������ļ���)��õ�����Դ��������*/
	public Connection getConnection() {
		return new ConnectionImpl(getSQLConnection());
	}
	
	java.sql.Connection getSQLConnection(){
		java.sql.Connection con = null;
		try {
			con = java.sql.DriverManager.getConnection(DB_URL, DB_USER,
					DB_PASSWORD);
			out.println("�õ�����:Connection " + (ConnectionPool.connections.size()+1));
		} catch (SQLException e) {
			out.println("���ݿ����ӽ����쳣��\n@shy2850@" + e.getMessage()
					+ e.getCause());
		}
		return con;
	}
	
}

package com.createJavaFile.connectionSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.createJavaFile.createModel.Model;
import com.createJavaFile.createModel.ModelDao;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**
 * �����Զ���ɵ�ǰ���ݿ��е����б����Զ�����ӳ��java�ࡣ
 */
public class DBAutoRun {
	/**Derby���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog DERBY = new SQLDialog("DERBY","org.apache.derby.jdbc.ClientDriver", "jdbc:derby://<hostName>:<port>/<databaseName>",1527);
	/**MySql���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog MYSQL = new SQLDialog("MYSQL","com.mysql.jdbc.Driver","jdbc:mysql://<hostName>:<port>/<databaseName>",3306);
	/**SqlServer���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog SQLSERVER = new SQLDialog("SQLSERVER","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://<hostName>:<port>;DatabaseName=<databaseName>",1433);
	/**DB2���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog DB2 = new SQLDialog("DB2","com.ibm.db2.jcc.DB2Driver","jdbc:db2://<hostName>:<port>/<databaseName>",5000);
	/**Oracle���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog ORACLE = new SQLDialog("ORACLE","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@<hostName>:<port>:<databaseNamename>",1521);
	/**Windows-Access���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog ACCESS = new SQLDialog("ACCESS","sun.jdbc.odbc.JdbcOdbcDriver","jdbc:odbc:<databaseName>",0);
	/**PostgreSql���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog POSTGRE = new SQLDialog("POSTGRE","org.postgresql.Driver","jdbc:postgresql://<hostName>:<port>/<databaseName>",5432);
	/**SybaseSql���ݿ��JDBC���ӹ淶*/
	public static final SQLDialog SYBASE = new SQLDialog("SYBASE","com.sybase.jdbc3.jdbc.SybDriver","jdbc:sybase:Tds:<hostName>:<port>/<databaseName>",2638);
	
	
	private Connection conn = new ConnectionProvider().getSQLConnection();
	/**ʵ�����ļ������ַ*/
	private String poURL = "com.bean.po";
	/**DAO���ļ������ַ*/
	private String daoURL= "com.bean.dao";
	
	/**���ݿ�����Ҫ�������ļ��Ĺ���������*/
	private List<String> tables = new ArrayList<String>();
	/**���ݿ�����Ҫ�������ļ��Ĺ����������������*/
	private List<String> pks = new ArrayList<String>();
	{init();}
	
	/**
	 * ��ʼ����ǰ���ݿ����ӣ�������ɶ�ȡ�������ݿ���Լ�����
	 * */
	private void init(){
		DatabaseMetaData mtdt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			mtdt = conn.getMetaData();
			String[] types = {"TABLE"};
			rs = mtdt.getTables(null, null, null, types);
			while(rs.next()){
				String s = rs.getString("TABLE_NAME");
				tables.add(s);
				rs2 = mtdt.getPrimaryKeys(null, null, s);
				while(rs2.next()){
					String pk = rs2.getString(4);
					pks.add(null==pk?"shy2850":pk);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				rs2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * �����Զ���ɵ�ǰ���ݿ��е����б����Զ�����ӳ��java�ࡣ
	 */
	public DBAutoRun() {
		
	}
	/**
	 * �����Զ���ɵ�ǰ���ݿ��е����б����Զ�����ӳ��java�ࡣ
	 * @param poURL		ʵ�����java�ļ����ɵ�ַ
	 * @param daoURL	DAO���java�ļ����ɵ�ַ
	 */
	public DBAutoRun(String poURL,String daoURL) {
		this.poURL = poURL;
		this.daoURL = daoURL;
	}
	
	/**�����ļ������Ժ������Զ���������java�ļ�*/
	public void autoRun() {
		if(!new File(Util.contextPath+Util.DB_CONFIG).isFile())throw new RuntimeException("�����ļ������ڣ�");
		for (int i = 0; i < tables.size(); i++) {
			Model model = new Model(tables.get(i), poURL, pks.get(i));
			model.saveModel();
			ModelDao dao = new ModelDao(model, daoURL);
			dao.saveModelDao();
		}
	}
	 
	/**
	 * ���ձ����Ƴ�����Ҫ����ӳ��������ݿ��
	 * @param tableNames ���ݿ������ 
	 */
	public void remove(String...tableNames){
		for (int i = 0; i < tableNames.length; i++) {
			String name = tableNames[i];
			for (int j = 0; j < tables.size(); j++) {
				if(name.equals(tables.get(j))){
					tables.remove(j);
					pks.remove(j);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param JDBC_DRIVER
	 *            JDBC�������� ��: com.mysql.jdbc.Driver
	 * @param DB_URL
	 *            ���ݿ�����URL ��:jdbc:mysql://localhost:3306/u-disk
	 * @param DB_USER
	 *            ���ݿ��û���
	 * @param DB_PASSWORD
	 *            ���ݿ��Ӧ����
	 */
	private static void setConfig(String JDBC_DRIVER, String DB_URL, String DB_USER,
			String DB_PASSWORD) {
		File file;
		file = new File(Util.contextPath+Util.DB_CONFIG);
		try {
			if (!file.exists()) {
				new File(Util.contextPath+"conf").mkdirs();
				file.createNewFile();
				PropertyReader.addProperties(Util.DB_USER, DB_USER);
				PropertyReader.addProperties(Util.DB_PASSWORD, DB_PASSWORD);
				PropertyReader.addProperties(Util.DB_URL, DB_URL);
				PropertyReader.addProperties(Util.JDBC_DRIVER, JDBC_DRIVER);
				PropertyReader.addProperties(Util.SHOW_SQL, "FALSE");
				PropertyReader.addProperties(Util.BEAN_CONF,
						"conf/ApplicationBeans.properties");
				PropertyReader.addProperties(Util.FORWARD_CONF,
						"conf/ApplicationForwards.properties");
				PropertyReader
						.addProperties(Util.CONNECTION_POOL,
								"com.createJavaFile.connectionSource.ConnectionPoolImpl");
				PropertyReader
						.addProperties(ConnectionPool.CONNECTION_PROVIDER,
								"com.createJavaFile.connectionSource.ConnectionProvider");
				PropertyReader.addProperties(
						ApplicationContext.ANNOTATION_INJECT, "false");
				PropertyReader.addProperties(
						ApplicationContext.SET_METHOD_INJECT, "true");
				PropertyReader.addProperties(Util.DIVID_CONFIG, "$");
				PropertyReader.addProperties(Util.PERSIST_CONF, "conf/PersistObj.properties");
				new File(Util.contextPath+"conf/ApplicationForwards.properties").createNewFile();
				System.out.println("�����ļ��Ѿ�����");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * ��ܵ���ʼ�㣺���ɻ��������ļ�
	 * @param dialog		���õ����ݿ���������Ӧ�����ӹ淶
	 * @param hostName		���ݿ�����IP
	 * @param databaseName	��Ҫ���������ݿ�����
	 * @param user			���ݿ��û���
	 * @param password		���ݿ�����
	 */
	public static void setConfig(SQLDialog dialog, String hostName, String databaseName, String user, String password){
		setConfig(dialog.driver, dialog.getUrl(hostName, databaseName), user, password);
	}
	
	/**ָ�������ݿ�����JDBC��ʽ*/
	public static class SQLDialog{
		private String name;
		private String driver;
		private String urlPattern;
		
		/**
		 * @param name		��ǰ�淶������
		 * @param driver	�����ݿ��JDBC������·������
		 *@param urlPattern	��ǰ���ݿ��ʽ�е�URL���ù淶
		 * @param port		���ݿ����Ķ˿ں�
		 */
		private SQLDialog(String name, String driver,String urlPattern,int port) {
			this.name = name;
			this.driver = driver;
			this.urlPattern = urlPattern.replace("<port>", port+"");
		}
		
		/**
		 * ��ȡ���ϵ�ǰ�淶��URL
		 * @param hostName		���ݿ�����IP
		 * @param databaseName	���ݿ�����
		 * @return
		 */
		public String getUrl(String hostName,String databaseName) {
			if(null == hostName)hostName = "localhost";
			if(null == databaseName)databaseName = "";
			return urlPattern.replace("<hostName>", hostName).replace("<databaseName>", databaseName);
		}

		public String toString() {
			return "SQLDialog:" + name + " \ndriver : " + driver
					+ "\nurlPattern : " + urlPattern;
		}

		public void setDriver(String driver) {
			this.driver = driver;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}

package com.createJavaFile.Main;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.createJavaFile.myutil.Util.close;

import com.createJavaFile.connectionSource.ConnectionPool;
import com.createJavaFile.createModel.ParseResultSetable;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.PreparedStatement;
import com.myInterface.ResultSet;
import com.myInterface.ResultSetMetaData;
import com.shy2850.misc.LogOutputStream;

/**
 * <pre>
 * SQL����ִ�п�����
 * ֧��ԭ��̬��׼SQL���
 * ��ϵͳ��һ���ͨ��SuperClassDao(����������)�����
 * 
 * <pre>
 * */
public class DBManager {

	/** JDBC���� */
	private Connection conn;
	/** Ĭ�ϵ����Զ��ύ */
	private boolean isHandleCommit;
	/** SQL���ִ�е�Ӱ������ */
	private int rows;

	/** ��ConnectionPool�л�����ӳ� */
	private static ConnectionPool connectionPool = ConnectionPool.connectionPoolImpl;

	/** ��ӡ��־��PrintStream out */
	private static PrintStream out = getLogPrinter();

	/** ���ô�ӡSQL����PrintStream out */
	public static void setOut(PrintStream out) {
		DBManager.out = out;
	}

	private static PrintStream getLogPrinter() {
		String logPrinter = PropertyReader.get("log");
		if(null != logPrinter){
			LogOutputStream.setFileName(logPrinter);
			try {
				return new LogOutputStream();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return System.out;
		
	}

	public static PrintStream getOut() {
		return out;
	}

	public boolean isAutoCommit() {
		try {
			isHandleCommit = !getConn().getAutoCommit();
		} catch (SQLException e) {
			out.println(e);
		}
		return !isHandleCommit;
	}

	public void setHandleCommit(boolean isHandleCommit) {
		try {
			getConn().setAutoCommit(!isHandleCommit);
		} catch (SQLException e) {
			out.println(e);
		}
		this.isHandleCommit = isHandleCommit;
	}

	
	DBManager() {
		getConn();
	}

	public Connection getConn() {
		Connection con = conn;
		try {
			if (null != con && !con.isClosed())
				return con;
			else {
				con = connectionPool.getConnection();
				conn = con;
			}
		} catch (SQLException e) {
			out.println("���ӻ�ȡʧ�ܣ�" + e);
		}
		return con;
	}

	/**
	 * ִ�и������
	 * 
	 * @param sql
	 *            ԭ��̬SQL
	 * @param printSQL
	 *            �Ƿ��ӡSQL���
	 * @param objects
	 *            ռλ��������
	 * @return SQL���ִ�е�Ӱ������
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, boolean printSQL, Object... objects)
			throws SQLException {
		Connection con = getConn();
		con.setBeingUsed(true);
		PreparedStatement pstmt = con.prepareStatement(sql);
		for (int i = 0; i < objects.length; i++) {
			pstmt.setObject((i + 1), objects[i]);
		}// �� sql ���Ĳ�����ֵ

		if (printSQL)printSQL(pstmt);
		
		int n = pstmt.executeUpdate();
		close(pstmt);
		con.setBeingUsed(false);
		if (!isHandleCommit) {
			connectionPool.releaseConnection(conn);
			rows += n;
			return rows;
		} else {
			rows += n;
			return 0;
		}
	}// executeUpdate()

	/**
	 * ִ�в�ѯ��䣬�м�����con.setReadOnly(true)�����Ч�ʣ��뿪ʱ����
	 * 
	 * @param sql
	 *            ԭ��̬SQL
	 * @param printSQL
	 *            �Ƿ��ӡSQL���
	 * @param obj
	 *            ռλ��������
	 * @return ִ��SQL���Ľ����
	 * @throws SQLException
	 */
	public List<Object> executeQuery(String sql, boolean printSQL,
			ParseResultSetable pr, Object... obj) throws SQLException {
		List<Object> list = new ArrayList<Object>();
		Connection con = getConn();
		con.setBeingUsed(true);
		con.setReadOnly(true);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			if (null != obj)
				for (int i = 0; i < obj.length; i++) {
					pstmt.setObject((i + 1), obj[i]);
				}// �� sql ���Ĳ�����ֵ
			
			if (printSQL)printSQL(pstmt);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (null == pr) {
					ResultSetMetaData rsmd = rs.getMetaData();
					Object[] objs = new Object[rsmd.getColumnCount()];
					for (int i = 0; i < objs.length; i++) {
						objs[i] = rs.getObject(i + 1);
					}
					list.add(objs);
				} else {
					Object ent = pr.parseOf(rs);
					list.add(ent);
				}
			}// while
			close(rs);
			close(pstmt);
		} catch (SQLException e) {
			out.println(e);
		} finally {
			con.setReadOnly(false);
			con.setBeingUsed(false);
			connectionPool.releaseConnection(conn);
		}
		return list;
	}// executeQuery()

	/** ��ѯ�ύ */
	public int commit() {
		try {
			getConn().commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException("�ع��쳣��", e1);
			}
			return 0;
		} finally {
			connectionPool.releaseConnection(conn);
			rows = 0;
		}
		return rows;

	}
	
	/**��ӡԤ�����SQL���*/
	private void printSQL(PreparedStatement pstmt){
		String sqlString = pstmt.toString();
		int index =sqlString.lastIndexOf("PreparedStatement");
		sqlString = index > -1 ? sqlString.substring(index + 25) : sqlString;
		out.println("SQL: "+ sqlString);
	}
	
	

}// class

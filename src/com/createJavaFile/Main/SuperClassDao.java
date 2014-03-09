package com.createJavaFile.Main;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.createJavaFile.createModel.ParseResultSetable;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;

public class SuperClassDao {

	/**����ʵ������б��Ƿ��Ѿ�����*/
	public boolean sortChanged;
	/**����ʵ������б��Ƿ���Ҫ����*/
	public boolean needUpdate;
	/**�Ƿ��ӡSQL��䣺�������ļ��л��*/
	public static final boolean show_sql = "true".equalsIgnoreCase(PropertyReader.get(Util.SHOW_SQL));
	
	/**DBManager����(�ɱ���˽�еľ�̬��Ա)*/
	private static DBManager manager = new DBManager();
	
	/**DBManager����(�ɱ���Ķ�����������ʹ��)*/
	protected DBManager dbmanager = manager;
	protected static PrintStream out = DBManager.getOut();
	
	public void setDbmanager(DBManager dbmanager) {
		this.dbmanager = dbmanager;
	}
	public DBManager getDbmanager() {
		return dbmanager;
	}
	/**�ύ��ѯ*/
	public void commit() throws SQLException{
		int n = dbmanager.commit();
		if(n>0)needUpdate = true;
	}
	
	/**ϵͳͨ�õ�ʵ�屣�淽��*/
	public static void persist(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("insert into "+Util.lowerFirst(bean.getClass().getSimpleName())+" values("); 
		String[] columns =  bean.getMemberList();
		for (int i = 0; i < columns.length; i++) {
			sb.append("?");
			if(i != columns.length-1)sb.append(",");
			else sb.append(")");
		}
		String sql = sb.toString();
		Object[] values = new Object[columns.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = bean.get(i);
		}
		try {
			if(!manager.isAutoCommit()){
				out.println("��ǰ���Ӳ����Զ��ύ���ӣ�");
				throw new SQLException("��ǰ���Ӳ����Զ��ύ���ӣ�");
			}
			manager.executeUpdate(sql,show_sql,values);
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"����ʧ�ܣ�");
		}
	}
	
	/**ϵͳͨ�õ�ʵ��ɾ������*/
	public static void delete(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("delete from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
		String[] columns =  bean.getMemberList();
		Object[] values = new Object[columns.length];
		for (int i = 0; i < values.length; i++) {
			sb.append("and "+columns[i]+" = ? ");
			values[i] = bean.get(i);
		}
		String sql = sb.toString();
		try {
			if(!manager.isAutoCommit()){
				out.println("��ǰ���Ӳ����Զ��ύ���ӣ�");
				throw new SQLException("��ǰ���Ӳ����Զ��ύ���ӣ�");
			}
			manager.executeUpdate(sql,show_sql,values);
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"ɾ��ʧ�ܣ�");
		}
	}
	
	/**ϵͳͨ�õ�ʵ����ҷ���*/
	public static List<?> getBeansByModel(ParseResultSetable bean){
		StringBuffer sb = new StringBuffer("select * from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
		String[] columns =  bean.getMemberList();
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < columns.length; i++) {
			if(null != bean.get(i)){
				sb.append("and "+columns[i]+" = ? ");
				values.add(bean.get(i));
			}
		}
		String sql = sb.toString();
		List<?> resultList = null;
		try {
			resultList = manager.executeQuery(sql, show_sql, bean, values.toArray());
		} catch (SQLException e) {
			out.println(e);
			out.println(bean+"�����쳣��");
		}
		return resultList;
	}
	
		public static List<?> getAllBeansOf(ParseResultSetable bean){
			String sql = "select * from "+Util.lowerFirst(bean.getClass().getSimpleName());
			try {
				return manager.executeQuery(sql, show_sql, bean);
			} catch (SQLException e) {
				out.println(e);
				out.println(bean+"�����쳣��");
				return null;
			}
		}
		/**ϵͳͨ�õ�ʵ����ҷ���*/
		public static List<?> findBeansByModel(ParseResultSetable bean){
			StringBuffer sb = new StringBuffer("select * from "+Util.lowerFirst(bean.getClass().getSimpleName())+" where 1=1 "); 
			String[] columns =  bean.getMemberList();
			for (int i = 0; i < columns.length; i++) {
				if(null != bean.get(i)){
					sb.append("and "+columns[i]+" like '%"+bean.get(i)+"%' ");
				}
			}
			String sql = sb.toString();
			List<?> resultList = null;
			try {
				resultList = manager.executeQuery(sql, show_sql, bean);
			} catch (SQLException e) {
				out.println(e);
				out.println(bean+"�����쳣��");
			}
			return resultList;
		}
}

package com.shy2850.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.Main.SuperClassDao;
import com.createJavaFile.createModel.ParseResultSetable;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.convertor.ConvertorUtils;

/**�������ݿ��ʹ�õ���*/
public class Backup {
	private static final String dataProp = Util.contextPath+fileUrl();
	
	private static Properties propPOs;
	private static String sql;
	
	static{
	    	try {
	    		propPOs = new Properties();
	    		File file = new File(dataProp);
	    		if(!file.exists()){
	    			file.createNewFile();
	    		}
				InputStream poInput = new FileInputStream(dataProp);
				propPOs.load(poInput);
				poInput.close();
			} catch (Exception e) {
				DBManager.getOut().print(e);
			} 
	  }
	
	private static String fileUrl(){
		String s = PropertyReader.get(Util.PERSIST_CONF);
		return null==s ? "conf/PersistObj.properties" : s; 
	}
	
	/**��ȡ�������ݿ���T-SQL���*/
	public static String getSql() {
		if(null == sql){
			StringBuilder sb = new StringBuilder();
			for (Entry<String, Object> entry : ApplicationContext.allBeans.entrySet()) {
				if(entry.getValue() instanceof ParseResultSetable){
					String table = entry.getKey();
					ParseResultSetable prs = (ParseResultSetable) entry.getValue();
					sb.append("create table " + table+"(");
					String[] strings = prs.getMemberList();
					for (int i = 0; i < strings.length; i++) {
						String type = null;
						try {
							type = prs.getClass().getDeclaredField(strings[i]).getType().getSimpleName();
						} catch (Exception e) {
							DBManager.getOut().print(e);
						}
						sb.append("" + strings[i] + " "+convertType(type));
						if(i != strings.length-1)sb.append(',');
					}
					if(prs.PrimaryKey() > -1){
						sb.append(",primary key("+strings[prs.PrimaryKey()]+")");
					}
					sb.append(");\n");
				}
			}
			sql = sb.toString();
		}
		return sql;
	}
	
	/**
	 * ����������ݿ��־û�����ı���
	 * @param prs 	�־û�����	�ؼ���Ϊ���־û�����+����ֵ
	 */
	public static void backupPo(ParseResultSetable prs){
		backupPo(prs.getClass().getSimpleName()+prs.get(prs.PrimaryKey()), prs);
	}
	/**
	 * ����������ݿ��־û�����ı���
	 * @param key	�ؼ���
	 * @param prs 	�־û�����
	 */
	public static void backupPo(String key,ParseResultSetable prs){
		propPOs.put(key, prs.toString());
		try {
			OutputStream out = new FileOutputStream(dataProp);
			propPOs.store(out, key);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**�������м����ݿ��*/
	public static void rebuildTables() throws SQLException{
		String[] strings = getSql().split(";\n");
		for (int i = 0; i < strings.length; i++) {
			new SuperClassDao().getDbmanager().executeUpdate(strings[i],SuperClassDao.show_sql);
		}
		System.out.println("���ָ�" + strings.length + "�����ݿ��");
	}
	
	/**�Ѷ����toString���سɶ���*/
	public static Object reBuildBeanFromToString(String s){
		String clazzString = s.substring(0, s.indexOf(" ["));
		String[] valueStrings = s.substring(s.indexOf('[')+1, s.lastIndexOf(']')).split(",");
		
		Object obj = ApplicationContext.getBean(Util.lowerFirst(clazzString));
		Class<?> clazz = obj.getClass(); 
		for (int i = 0; i < valueStrings.length; i++) {
			String column = valueStrings[i].split("=")[0];
			String value  = "";
			try {
				value  = valueStrings[i].split("=")[1];
				if("null".equals(value)) value = null;
			} catch (Exception e) {
			}
			Field field = null;
			Method setMethod = null;
			try {
				field = clazz.getDeclaredField(column);
				setMethod = clazz.getMethod("set"
						+ Util.upperFirst(column), field.getType());
			} catch (Exception e) {
				DBManager.getOut().print(e);
			}
		try {
			setMethod.invoke(obj, ConvertorUtils.parseString(field.getType(), value));
		} catch (Exception e) {
			throw new RuntimeException("û��" + field.getType() + "��ת������");
		}
		}
		return (ParseResultSetable) obj;
	}
	
	/**��java���е��ֶ����Ծ���ƥ�䵽T-SQL��*/
	private static String convertType(String javaType){
		if("Integer".equals(javaType) || "Boolean".equals(javaType) || "Long".equals(javaType))return "int";
		if("Double".equals(javaType) || "Float".equals(javaType))return "float";
		if("Blob".equals(javaType))return "blob";
		if("Date".equals(javaType))return "date";
		else return "varchar(32)";
	}
	
	/**�����������е��������ö���ԭ�����ݿ���*/
	public static void rebuildPOs(){
		for (Entry<Object, Object> entry : propPOs.entrySet()) {
			try {
				SuperClassDao.persist((ParseResultSetable) reBuildBeanFromToString(entry.getValue().toString()));
			} catch (Exception e) {
			}
		}
	}
	
	/**ͨ����ʶ��ȡ���������е������ö���*/
	public static ParseResultSetable getPersistObj(String key){
		return (ParseResultSetable) reBuildBeanFromToString(propPOs.getProperty(key));
	}
	
	public static void backupAllPos(){
		List<?> list = null;
		int num = 0;
		for (Entry<String, Object> entry : ApplicationContext.allBeans.entrySet()) {
			if(entry.getValue() instanceof ParseResultSetable){
				list = SuperClassDao.getAllBeansOf((ParseResultSetable) entry.getValue());
				for (int i = 0; i < list.size(); i++) {
					backupPo((ParseResultSetable) list.get(i));
					num++;
				}
			}
		}
		System.out.println(num+"�������Ѿ��������");
	}
	
}

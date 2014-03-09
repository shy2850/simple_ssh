package com.createJavaFile.myutil;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**ORM��ܵĹ�����*/
public class Util {
	
	/**�����ַ���*/
	public static final String IMPORT_DATE = "import java.util.Date;\nimport com.shy2850.filter.DateFormat;";
	/**�����ַ���*/
	public static final String IMPORT_BLOB = "import java.sql.Blob;";
	/**�������ļ���*/
	public static final String DB_CONFIG = "conf/db_conf.properties";
	/**JDBC��������*/
	public static final String JDBC_DRIVER = "JDBC_DRIVER";
	/**���ݿ�����URL�ֶ�*/
	public static final String DB_URL = "DB_URL";
	/**���ݿ��û����ֶ�*/
	public static final String DB_USER = "DB_USER";
	/**���ݿ��Ӧ�����ֶ�*/
	public static final String DB_PASSWORD = "DB_PASSWORD";
	/**ѡ�����ӳ������ֶ�*/
	public static final String CONNECTION_POOL = "CONNECTION_POOL";
	/**Ĭ�ϵĴ�ӡSQL��������ֶ�*/
	public static final String SHOW_SQL = "SHOW_SQL";
	/**��ת�����ļ���ָ�������ֶ�*/
	public static final String FORWARD_CONF = "ApplicationForwards";
	/**Ӧ�ó����������ļ���ָ�������ֶ�*/
	public static final String BEAN_CONF = "ApplicationBeans";
	/**����������Ҫ����ĵ�ַ����*/
	public static final String PERSIST_CONF = "persist_conf";
	/**����ַ������뷽��֮��ķָ�������*/
	public static final String DIVID_CONFIG = "divid";
	/**Ĭ�ϵ������ļ�λ��*/
	public static String contextPath = "WebRoot/";
	
	/**����ת����*/
	public static String getType(String type) {
		if ("java.lang.Integer".equals(type))
			return "Integer";
		if ("java.lang.Double".equals(type)
				|| "java.math.BigDecimal".equals(type))
			return "Double";
		if ("java.lang.Float".equals(type))
			return "Float";
		if ("java.lang.String".equals(type))
			return "String";
		if ("java.lang.Boolean".equals(type))
			return "Boolean";
		if ("java.sql.Date".equals(type) || "java.sql.Timestamp".equals(type))
			return "Date";
		if ("java.math.BigInteger".equals(type))
			return "Long";
		if ("[B".equals(type))
			return "Blob";
		if ("[C".equals(type))
			return "Clob";
		return type;
	}

	/**��ORM��ܵ�����ĸ��д���÷���*/
	public static String upperFirst(String s) {
		if (null == s)
			return null;
		String string = new String(s);
		if ("Integer".equals(string))
			return "Int";
		if (isUpperCase(string.charAt(0)))
			return string;
		else {
			return string.replaceFirst(string.charAt(0) + "", (char) (string
					.charAt(0) - 32)
					+ "");
		}
	}

	/**�ַ�������ĸ�Ƿ��д*/
	public static boolean isUpperCase(char first) {
		if (first >= 'A' && first <= 'Z')
			return true;
		return false;
	}

	/**����ĸСд�Ĺ��÷���*/
	public static String lowerFirst(String s) {
		if (null == s)
			return null;
		String string = new String(s);
		if ("Integer".equals(string))
			return "Int";
		if (isLowerCase(string.charAt(0)))
			return string;
		else {
			return string.replaceFirst(string.charAt(0) + "", (char) (string
					.charAt(0) + 32)
					+ "");
		}
	}

	/**�ַ�������ĸ�Ƿ�Сд*/
	public static boolean isLowerCase(char first) {
		if (first >= 'a' && first <= 'z')
			return true;
		return false;
	}
	
	/**
	 * �ַ���д���ļ��Ĺ���
	 * @param info   Ҫд����ַ���
	 * @param url    Ҫд����ļ���(������ʱ���Զ�����)
	 * @param fileName  �ļ���
	 * @throws IOException
	 */
	public static void write(String info, String url, String fileName)
			throws IOException {
		FileWriter fw;
		File file = new File(url.replace(".", "/"));
		if (!file.exists())
			file.mkdirs();
		fw = new FileWriter(url.replace(".", "/") + "/" + fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(info);
		bw.flush();
		close(bw);
		close(fw);
	}

	/**<pre>
	 * һ����ͨ�õ�close����
	 * ���Թر�sql���е�Connection��ResultSet��Statement
	 * �Լ�Closeable�ӿڵĶ���
	 * </pre>
	 * */
	public static void close(Object o) {
		if (null == o)
			return;
		try {
			if (o instanceof Connection)
				((Connection) o).close();
			else if (o instanceof ResultSet)
				((ResultSet) o).close();
			else if (o instanceof Statement)
				((Statement) o).close();
			else if (o instanceof Closeable)
				((Closeable) o).close();
			else
				;
		} catch (SQLException e) {
			System.out.println("SQL�ر��쳣��");
		} catch (IOException e) {
			System.out.println("IO�ر��쳣��");
		} finally {
			o = null;
		}
	}
	
	/**���ַ������й涨�������͵�MD5ת��*/
	public static String md5(String s){
		return md5(s, "UTF-8" , false);
	}
	
	/**���ַ������й涨�������͵�MD5ת��*/
	public static String md5(String s,String Encode){
		return md5(s, Encode, false);
	}
	
	/**���ַ������й涨�������͵�MD5ת��*/
	public static String md5(String s,String Encode,boolean b) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] byteArray = s.getBytes(Encode);
			byte[] md5Bytes = md5.digest(byteArray);

			StringBuffer hexValue = new StringBuffer();

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			if(b)return hexValue.toString().substring(8, 24);
			return hexValue.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}

package com.createJavaFile.myutil;

import java.util.*;
import java.io.*;

/**�������ļ�src/db_conf.properties�Ķ�ȡר����*/
public class PropertyReader {
	
	static private Properties ps;
	static private String fileName = Util.contextPath+Util.DB_CONFIG;
  
	/**����Ĭ�ϵ���Դ�ļ����ã����Ҹ�����Դ�ļ�*/
	public void setFileName(String fileName) {
		PropertyReader.fileName = fileName;
		init();
	}
	public static String getFileName() {
		return fileName;
	}
  
	static{
		init();
	}
	
	/**������Դ�ļ�*/
	private static void init(){
		ps=new Properties();
		try{
			InputStream in = new FileInputStream(fileName);
			ps.load(in);
			in.close();
		}catch(Exception e){System.out.println("�����ļ���"+fileName+"�����ڣ�");}
	}

	/**���ָ���ַ�������*/
	public static String get(String key){
		return (String)ps.get(key);
	}
	/**���������ļ����������*/
	public static void addProperties(String key,String value){
		ps.put(key, value);
			try {
				OutputStream out = new FileOutputStream(fileName);
				ps.store(out, key);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	  	}
}


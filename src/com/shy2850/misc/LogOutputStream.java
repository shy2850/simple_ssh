package com.shy2850.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import com.createJavaFile.myutil.Util;
import com.shy2850.filter.DateFormat;

/**��־OutputStream����*/
public class LogOutputStream extends PrintStream{
	
	/**Ĭ�ϵ���־����ļ���*/
	private static String fileName = "conf/easyWebSqlBean.log";
	/**Ĭ�ϵ���־����ļ�*/
	private static File file = initLogFile(); 
	
	public LogOutputStream() throws FileNotFoundException{
		super(file);
	}

	/**��ʼ����־�ļ�����*/
	private static File initLogFile() {
		File f = new File(Util.contextPath + fileName);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
	
	/**������־�ļ���*/
	public static void setFileName(String fileName) {
		if(null == fileName || fileName.length() == 0)return;
		LogOutputStream.fileName = fileName;
		file = initLogFile();
	}
	
	public void println(String x) {
		super.println(DateFormat.format(new Date())+"-----"+x);
	}
	
	public void println(Object x){
		super.println(DateFormat.format(new Date())+"-----"+x);
	}
	
}

package com.shy2850.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;


public class ActionManager {
	
	/**��ת�ö����Mapping*/
	static private Properties propForwards;
	/**Ӧ�ó����������б�ӳ����ת����ĵ�ʵ��Mapping*/
	static private Map<String, ActionForward> allForwards;
	
	  static{
		  propForwards=new Properties();
		  String forwardProp = PropertyReader.get(Util.FORWARD_CONF); 
		  if(null != forwardProp){
	    	try{
	    		InputStream in = new FileInputStream(Util.contextPath+forwardProp);
	    		propForwards.load(in);
	    		in.close();
	    	}catch(Exception e){System.out.println("�����ļ���"+forwardProp+"�����ڣ�");}
		  }
	  }
	  
	  static{
		  allForwards = new HashMap<String, ActionForward>();
		  Set<Entry<Object, Object>> set = propForwards.entrySet();
		  for (Entry<Object, Object> entry : set) {
			  String name = entry.getKey().toString();
			  ActionForward  forward = new ActionForward(entry.getValue().toString());
			  forward.setRedirect("true".equalsIgnoreCase(propForwards.getProperty(name+".isRedirect")));
			  allForwards.put(name, forward);
		}
	  }
	  
	  /**Ӧ�ó���������ת����ͨ���ַ������
	   * <br>
	   * name+".isRedirect" = true;��ʾ��ת��ʽΪ�ض���
	   * </br>
	   * */
	  public static ActionForward getForward(String key){
		 return allForwards.get(key); 
	  }
	
}

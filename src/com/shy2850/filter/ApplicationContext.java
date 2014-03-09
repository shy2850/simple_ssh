package com.shy2850.filter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.injection.AnnotationInjection;
import com.shy2850.injection.SetMethodInjection;

/**Ӧ�ó���ȫ�ֹ����ࣺ
 * ��Ҫ������ȡ�����ļ���ö����Լ�ע������Ա
 * */
public class ApplicationContext {
	/**�Ƿ�ʹ��set����ע��������ֶΣ�Ĭ��Ϊtrue*/
	public static final String SET_METHOD_INJECT = "SET_METHOD_INJECT";
	/**�Ƿ�ʹ��ע�ⷽʽע��������ֶΣ�Ĭ��Ϊfalse*/
	public static final String ANNOTATION_INJECT = "ANNOTATION_INJECT";
	
	/**�����ö����Mapping*/
	static protected Properties propBeans;
	/**Ӧ�ó����������б�ӳ�������ĵ�ʵ��Mapping*/
	static protected Map<String, Object> allBeans;
	  static{
		  propBeans=new Properties();
		  String beanProp = PropertyReader.get(Util.BEAN_CONF); 
		  if(null != beanProp){
	    	try{
	    		InputStream in = new FileInputStream(Util.contextPath+beanProp);
	    		propBeans.load(in);
	    		in.close();
	    	}catch(Exception e){System.out.println("�����ļ���"+beanProp+"�����ڣ�");}
		  }
	  }
	  
	  static{
		  allBeans = new HashMap<String, Object>();
		  Set<Entry<Object, Object>> beans = propBeans.entrySet();
		  for (Entry<Object, Object> entry : beans) {
			String key = entry.getKey().toString();
			String bean = entry.getValue().toString();
			try {
				Object value = Class.forName(bean).newInstance();
				allBeans.put(key, value);
			} catch (Exception e) {
				System.err.println(key + "�������಻���ڻ���"+bean+"���췽�����Ϸ���");
			}
		}
	  }
	  
	  static{
			if("true".equalsIgnoreCase(PropertyReader.get(ANNOTATION_INJECT))){
				AnnotationInjection.inject();
			}
			if(!"false".equalsIgnoreCase(PropertyReader.get(SET_METHOD_INJECT))){
		    	SetMethodInjection.inject();
		    }
		}
	  
	  /**Ӧ�ó������ĵ�ʵ������ͨ���ַ������*/
	  public static Object getBean(String key){
	     return allBeans.get(key);
	  }
	  
	  /**�������ʵ��������ӳ��*/
	  public static void addProperties(String key,String value){
			propBeans.put(key, value);
			try {
				OutputStream out = new FileOutputStream(Util.contextPath+PropertyReader.get(Util.BEAN_CONF));
				propBeans.store(out, key);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
}

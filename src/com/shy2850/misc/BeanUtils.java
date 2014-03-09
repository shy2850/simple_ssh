package com.shy2850.misc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.Util;

/**
 * ���������õ���javaBean��ع���
 */

public class BeanUtils {
	/**
	 * ʵ�ִ�һ�������л�ȡ��Ч����copy��ָ���������Ӧ����</br>
	 * (Ҫ��Դ�����copy�ֶ���Ҫget������Ŀ�������Ҫset����)
	 * 
	 * @param target  	Ŀ�����
	 * @param root		��copy��Դ����
	 */
	public static void copy(Object target,Object root){
		Class<?> targetClass = target.getClass();
		Class<?> rootClass = root.getClass();
		
		Field[] fields = rootClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				boolean isBoolean = field.getType().getSimpleName().equals("Boolean")|| field.getType().getSimpleName().equals("boolean");
				String getMethodName = isBoolean ? "is"+Util.upperFirst(fieldName) : "get"+Util.upperFirst(fieldName);
				Method getMethod = rootClass.getMethod(getMethodName);
				Object value = getMethod.invoke(root);
				
//				Field targetField = targetClass.getDeclaredField(fieldName);
//				Object obj = targetField.get(target);
//				targetField.set(obj, value);
				
				String setMethodName = "set"+Util.upperFirst(fieldName);
				Method setMethod = targetClass.getMethod(setMethodName, value.getClass());
				setMethod.invoke(target, value);
				
			} catch (SecurityException e) {
				DBManager.getOut().println(e);
			} catch (IllegalArgumentException e) {
				DBManager.getOut().println(e); 
			} catch (IllegalAccessException e) {
				DBManager.getOut().println(e); 
			} catch (NoSuchMethodException e) {
//				DBManager.getOut().println(e); 
			} catch (InvocationTargetException e) {
				DBManager.getOut().println(e); 
			}
			
		}
		
	}

}

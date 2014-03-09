package com.shy2850.convertor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**
 * @author shy2850
 *         <pre>
 *         ת�����Ŀ������� ��������ϵͳ�Ѿ��ṩ��ת�������� �����û��Զ����ת����ʵ��(��ע�����͵�)
 *         </pre>
 */
@SuppressWarnings("unchecked")
public class ConvertorUtils {
	
	/**�յ�Enumeration����*/
	private static final Enumeration<String> EMPTY_ENUMERATIONS = new Enumeration<String>() {
		public String nextElement() {
			return null;
		}

		public boolean hasMoreElements() {
			return false;
		}
	}; 

	/** ϵͳ���ܹ�������е�ʵ��ӳ����� */
	public static Map<String, Object> beans = new HashMap<String, Object>();

	/** ϵͳ�Ѿ�ע�������ת���� */
	public static Map<Class<?>, Convertor> convertors = new HashMap<Class<?>, Convertor>();
	static {
		convertors.put(String.class, new StringConvertor());
		convertors.put(Integer.class, new IntegerConvertor());
		convertors.put(Double.class, new DoubleConvertor());
		convertors.put(Boolean.class, new BooleanConvertor());
		convertors.put(Date.class, new DateConvertor());
		convertors.put(Float.class, new FloatConvertor());
		convertors.put(Long.class, new LongConvertor());
		convertors.put(int.class, new IntegerConvertor());
		convertors.put(boolean.class, new BooleanConvertor());
		convertors.put(double.class, new DoubleConvertor());
		convertors.put(float.class, new FloatConvertor());
		convertors.put(long.class, new LongConvertor());
	}

	/** ��request����Ĳ����������Ե��޸�ϵͳ����ĵ�ʵ��ӳ���� */
	private static void convertBeans(HttpServletRequest request, Object action)
			throws SecurityException, NoSuchFieldException {
		beans.clear();
		Enumeration<String> params = (null == request.getParameterNames()) ? EMPTY_ENUMERATIONS
				: request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if (param.indexOf('.') != -1) {
				String value = request.getParameter(param);
				String bean = param.substring(0, param.indexOf('.'));
				if (!beans.containsKey(bean)) {
					Object o;
					try {
						o = ApplicationContext.getBean(bean);
						beans.put(bean, o);
					} catch (Exception e) {
						DBManager.getOut().println(bean + "�������ļ���δ�ҵ�����������!\n" + e);
					}
				}
				String fieldName = param.substring(param.indexOf('.') + 1);
				{
					Object o = beans.get(bean);
					Field field = null;
					field = o.getClass().getDeclaredField(fieldName);
					Method setMethod = null;
					try {
						setMethod = o.getClass().getMethod(
								"set" + Util.upperFirst(field.getName()),
								field.getType());
					} catch (Exception e1) {
						throw new RuntimeException(o.getClass() + "��û��" + field
								+ "��set����");
					}
					try {
						setMethod.invoke(o, convertors.get(field.getType())
								.parse(value));
					} catch (Exception e) {
						throw new RuntimeException("û��" + field.getType()
								+ "��ת������", e);
					}
				}
			}
		}

	}

	/** ת������ʵ��ָ�����͵��ַ��������Ͷ����ת�� */
	public static Object parseString(Class<?> clazz, String param) {
		return convertors.get(clazz).parse(param);
	}

	/** �Զ���ת������Ҫ�ľ�̬ע�᷽�� */
	public static void regist(Convertor convertor) {
		if (null == convertor || null == convertor.getType()) {
			System.out
					.println("ע������" + convertor + "ע��ʧ��\n������Ϊ�ö��󲻴��ڻ���ע������Ϊ�գ�");
		}
		convertors.put(convertor.getType(), convertor);
	}

	/** ����ַ���ָ��action��ʱ������׼����������Ҫ��ƥ��(��ͬ��)��action��Ա&�������ͬ�� */
	public static void convertToAction(HttpServletRequest request, Object action)
			throws SecurityException, NoSuchFieldException {
		// System.out.println(action.getClass());
		Class<?> clazz = action.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String value = request.getParameter(field.getName());
			if (null == value)
				continue;
			Method setMethod = null;
				try {
					setMethod = clazz.getMethod("set"
							+ Util.upperFirst(field.getName()), field.getType());
				} catch (NoSuchMethodException e1) {
					throw new RuntimeException(clazz + "��û��" + field + "��set����");
				}
			try {
				setMethod.invoke(action, convertors.get(field.getType()).parse(
						value));
			} catch (Exception e) {
				throw new RuntimeException("û��" + field.getType() + "��ת������");
			}
		}
		convertBeans(request, action);
	}

	/**
	 * ��ָ��λ�����format�ַ���
	 * @param index		���format��λ�ã�nullʱ��ӵ���β
	 * @param strings	Ҫ��ӵ��ַ���
	 */
	public static void addDateFomarts(Integer index, String...strings){
		DateConvertor.addDateformats(index, strings);
	}
	
	
}

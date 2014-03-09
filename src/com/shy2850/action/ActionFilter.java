package com.shy2850.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.PropertyReader;
import com.createJavaFile.myutil.Util;
import com.shy2850.convertor.ConvertorUtils;
import com.shy2850.filter.ApplicationContext;
import com.shy2850.filter.WebContext;

/**
 * MVC��ܵ�ת����������
 * 
 * <pre>
 * 	��Filter��ͨ����ȡ�����ַ�����
 * 	�ֱ��ϵͳ�л�ö�Ӧ�������������Ƕ��󷽷���
 * 	Ȼ��ͨ��ִ�з������(String)ͨ�������ļ�Ѱ�Ҷ�Ӧ������·��
 * </pre>
 * */
public class ActionFilter implements Filter{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static String divid = "$";

	/** ҳ�������Լ��ַ�����Ҫ���� */
	private void intercept(HttpServletRequest request,
			HttpServletResponse response, String urlEndding) throws ServletException, IOException {
		PrintStream out = DBManager.getOut();
		/** ����ǰ����request��������̬ȫ����WebContext�� */
		WebContext.setRequest(request);
		WebContext.setResponse(response);
		try {
			
			/** �ػ������е� ����actionName �� ������methodName */
			String actionName = urlEndding.substring(1,urlEndding.indexOf(divid));
			String methodName = urlEndding.substring(urlEndding.indexOf(divid) + 1);
			/** ������󷽷���Ϊdo���Զ�ת��Ϊexecute */
			if ("do".equals(methodName) || "".equals(methodName))
				methodName = "execute";

			String forwardUrl = null;
			ActionForward next = null;

			/** ��������action���ϵͳ��ʵ�� */
			Object action = ApplicationContext.getBean(actionName); // ������

			/** ��ȡʧ��ʱ���׳���ʾ */
			if (null == action)
				out.println("Action��" + actionName + "δ�ҵ���");

			try {
				/** ʹ����������Ϊ����ָ��action����׼�� */
				ConvertorUtils.convertToAction(request, action);// ��������ʹ��
			} catch (Exception e2) {
				out.println("������ת���쳣��");
				throw new Exception(e2);
			}

			Method method = null;
			try {
				/** ��ȡָ��action�еķ��� */
				method = action.getClass().getMethod(methodName);
			} catch (Exception e1) {
				out.println(action.getClass() + "��û�з�����"
						+ methodName);
			}

			try {
				/** ִ��ָ��action�еķ��� */
				forwardUrl = (String) method.invoke(action);
			} catch (Exception e1) {
				out.println(actionName+"."+methodName+"����ִ���쳣��");
				throw new RuntimeException(e1);
			}

			/** ������ת�����װ���� */
			next = ActionManager.getForward(forwardUrl);

			if (null == next.getUrl()){
				out.println("�����ļ���û��" + forwardUrl+ "��forward���ã�");
				throw new Exception("�����ļ���û��" + forwardUrl
						+ "��forward���ã�");
			}
						
			try {
				if (next.isRedirect()) {
					response.sendRedirect(request.getContextPath()
							+ next.getUrl());
				} else {
					request.getRequestDispatcher(next.getUrl()).forward(
							request, response);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e.getCause());
			}

		} catch (Exception ex) {
			out.println(ex.getMessage() + "\n" + ex.getCause());
		}

	}//intercept

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		request = (HttpServletRequest) req;
		response= (HttpServletResponse) resp;
		String url = request.getRequestURI();
		String urlEndding = url.substring(url.lastIndexOf('/'));
		
		if(urlEndding.contains(divid)){
			intercept(request, response, urlEndding);
		}
		else{
			chain.doFilter(req, resp);
		}
		
	}

	public void init(FilterConfig config) throws ServletException {
		
		if(!new File(Util.DB_CONFIG).isFile()){
			Util.contextPath = config.getServletContext().getRealPath("/");
		}
		else{
			shy2850();
		}
		System.out.println("contextPath:"+Util.contextPath);
		try {
			Class.forName("com.shy2850.filter.ApplicationContext");
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		
		String s = PropertyReader.get(Util.DIVID_CONFIG);
		if(null != s && s.length() > 0){
			divid = s;
			System.out.println("�ָ�������:"+divid);
		}
	}
	

	/**������߱�ʶ��*/
	public static void shy2850(){
		System.err.println("@DEVELOPED BY SHY2850@");
	}

}// class


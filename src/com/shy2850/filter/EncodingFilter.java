package com.shy2850.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**ת��Filter*/
public class EncodingFilter implements Filter {
	
	/**�������*/
	private String reqEnc = "utf-8";
	private String reqType= "text/html;charset=utf-8";

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if(null==request.getCharacterEncoding())request.setCharacterEncoding(reqEnc);
		if(null==response.getContentType())response.setContentType(reqType);
		filterChain.doFilter(request, response);
	}//

	public void init(FilterConfig config) throws ServletException {
		System.out.println("=========��ʼ�� EncodingFilter========");
		System.out.println("reqEnc = \"utf-8\";\nreqType= \"text/html;charset=utf-8\";");
		if(null!=config.getInitParameter("reqEnc"))reqEnc = config.getInitParameter("reqEnc");
		if(null!=config.getInitParameter("reqType"))reqType=config.getInitParameter("reqType");
	}

}

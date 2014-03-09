package com.shy2850.action;

/**��һ���ַ��������װ�ɴ���ת��ʽ�����Ͷ���
 * <br>
 * �����ļ���name+".isRedirect" = true;��ʾ��ת��ʽΪ�ض���
 * </br>
 * */
public class ActionForward {
	
	/**<pre>
	 * �������ͣ�
	 * true �ض���
	 * false ת��
	 * </pre>
	 * */
	private boolean redirect = false;	//true Ϊ ������(�ض���) false Ϊ ת��
	/**��ת��URL*/
	private String url;				    //��ת��url
	
	/**
	 * @param url 	��ת��URL
	 */
	public ActionForward(String url) {
		this(url, false);
	}
	/**
	 * @param redirect 	�������ͣ� true �ض���; false ת��
	 * @param url		��ת��URL
	 */
	public ActionForward(String url,boolean redirect) {
		this.redirect = redirect;
		this.url = url;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean type) {
		this.redirect = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}

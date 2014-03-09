package com.createJavaFile.createModel;

import com.createJavaFile.myutil.Util;

/**<pre>
 * ����ORM��������򹤳̵�������
 * �������ݿ���е�ÿ���ð�װ����ı�ʾ��
 * </pre>
 * */
class Member {
	
	/**���е�����*/
	private String name;
	/**���е�����(����ĸ��д���)*/
	private String Name;
	/**������java�е���������*/
	private String type;
	/** �����Ƿ��Զ�����*/
	boolean isAutoIncrement;
	
	/**
	 * @param name   ���е�����
	 * @param type   ������java�е���������
	 * @param isAutoIncrement   �����Ƿ��Զ�����
	 * @param table  ���������ı���
	 */
	public Member(String name, String type, boolean isAutoIncrement) {
		super();
		this.name = name;
		this.type = type;
		this.isAutoIncrement = isAutoIncrement;
		this.Name = Util.upperFirst(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	/**�������磺private Type name;�ַ���*/
	public String creatMem(){
		return "\n\tprivate "+this.type+" "+this.name;
	} 
	/**</pre>
	 * �������磺
	 * public void setName(Type name){
	 * 	this.name = name; 
	 * }
	 * �ַ���
	 * </pre>
	 * */
	public String creatSetFun(){ 
			return "\n\t" +
					"public void set" +Name+
					"("+type+" "+name+")"+"{\n\t\tthis."+name+" = "+name+";\n\t}";
	}
	
	/**<pre>
	 * �������磺
	 * public Type getName(){
	 * 	return name;
	 * }
	 * �ַ���
	 * </pre>
	 * */
	public String creatGetFun(){
		if("boolean".equals(type)) return "\n\tpublic boolean is"+Name+"(){\n\t\treturn "+name+";\n\t}";
		else 
			return "\n\tpublic "+type+" get"+Name+"(){\n\t\treturn "+name+";\n\t}";
	}
	
	/**�������磺psetName(name)�ַ���*/
	public String set(){
		return "set"+Name+"("+name+")";
	}
	
	/**�������磺getName()�ַ���*/
	public String get(){
		if("boolean".equals(type))return "is"+Name+"()";
		else
			return "get"+Name+"()";
	}
	
	/**<pre>
	 * �������磺
	 * Type name = rs.getType("name");
	 * �ַ���
	 * </pre>
	 * */
	public String getParseResultSet(){
		return type+" "+name+" = rs.get"+Util.upperFirst(type)+"(\""+name+"\");";
	}
	
}

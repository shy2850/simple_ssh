package com.createJavaFile.createModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import com.createJavaFile.connectionSource.ConnectionPool;
import com.createJavaFile.myutil.Util;
import com.myInterface.Connection;
import com.myInterface.ResultSet;
import com.myInterface.ResultSetMetaData;
import com.myInterface.Statement;
import com.shy2850.filter.ApplicationContext;

/**</pre>
 * ͨ��ģ�黯��ƴ���ַ���
 * ����java�ļ�
 * ʵ�����ݿ����javaʵ������ӳ���ϵ
 * </pre>
 * */
public class Model{
	
	/**������������İ�װ��Member����ʵ������ɵ�����*/
	private LinkedList<Member> members = new LinkedList<Member>();
	
	public LinkedList<Member> getMembers() {
		return members;
	}
	
	
	/**��������ֶ��Ƿ���Date���͵�����*/
	boolean hasDate;
	/**��������ֶ��Ƿ���Blob���͵�����*/
	boolean hasBlob;
	/**���ݿ����*/
	String table;
	/**���ݿ������д*/
	String Table;
	/**���ɵ�java�ļ��ı����ַ*/
	String url;
	/**�������ڵ������±�*/
	int pkIndex = -1;
	/**�����ֶε�����*/
	private String pk;
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getPk() {
		return pk;
	}
	
	/**
	 * @param table ���ݿ����
	 * @param url   java�ļ��ı����ַ:ֻ������src��,��(com.java.util)
	 */
	public Model(String table, String url, String pk) {
		this.table = table;
		this.Table = Util.upperFirst(table);
		this.url = url;
		this.pk = pk;
		Connection con = ConnectionPool.connectionPoolImpl.getConnection();
		try {
            Statement stmt = con.createStatement();				
			ResultSet rs = stmt.executeQuery("select * from "+table);
			ResultSetMetaData rsm = rs.getMetaData(); 
			for (int i = 0; i < rsm.getColumnCount(); i++) {
				String name = rsm.getColumnName(i+1);
				String type = Util.getType(rsm.getColumnClassName(i+1));
				boolean isAutoIncrement = rsm.isAutoIncrement(i+1);
				if("Date".equals(type))hasDate = true;
				if("Blob".equals(type))hasBlob = true;
				System.out.println(name+":"+type);
				members.add(new Member(name, type, isAutoIncrement));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**ѭ������private��Ա*/
	private String def(){   //����private��Ա
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).creatMem()+";");
		}
		return sb.toString();
	}
	
	/**ѭ�����ɸ�����Ա��get-set����*/
	private String getSet(){  //����get-set����
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).creatSetFun());
			sb.append(members.get(i).creatGetFun());
		}
		return sb.toString();
	}
	/**<pre>
	 * ���ɹ��췽����
	 * ����һ���յĹ��췽���Լ�ȫ�����Ĺ��췽��
	 * </pre>
	 * */
	private String conStructor(){
		StringBuffer sb = new StringBuffer("\n\tpublic "+Util.upperFirst(table)+"(){}\n\tpublic "+Util.upperFirst(table)+"(");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).getType()+" "+members.get(i).getName());
			if(i!=members.size()-1)sb.append(", ");
		}
		sb.append("){\n\t\tsuper();\n\t\t");
		for (int i = 0; i < members.size(); i++) {
			sb.append("this."+members.get(i).getName()+" = "+members.get(i).getName()+";\n\t\t");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**��д��equals����*/
	private String Equals(Member member){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t@Override\n\tpublic boolean equals(Object o) {\n\t");
		sb.append("\treturn (o instanceof "+Table+")&&(("+Table+")o)."+member.get()+"=="+member.get()+";\n\t}");
		return sb.toString();
	}
	/**��д��toString����*/
	private String ToString() {  //���toString()����
		StringBuffer sb = new StringBuffer("\n\t@Override\n\tpublic String toString(){\n");
		sb.append("\t\treturn \""+Util.upperFirst(table)+" [");
		for (int i = 0; i < members.size(); i++) {
			if(Util.getType(members.get(i).getType()).equals("Date")){
				sb.append(members.get(i).getName()+"=\" + DateFormat.format("+members.get(i).getName()+")+\"");
			}
			else{
				sb.append(members.get(i).getName()+"=\" +"+members.get(i).getName()+"+\"");
			}
			if(i!=members.size()-1)sb.append(",");else sb.append("]\";\n\t}");
		}
		return sb.toString();
	}
	/**ʵ��ParseResultable�ӿڷ���parseOf���ַ���ƴ��*/
	private String ParseOf(){ //ʵ��ParseResultable�ӿڵ��ַ���ƴ��
		StringBuffer sb = new StringBuffer("\n\tpublic Object parseOf(ResultSet rs) throws SQLException{\n\t\tif(null==rs)return null;");
		for (int i = 0; i < members.size(); i++) {
		sb.append("\n\t\t"+members.get(i).getParseResultSet());
		}
		sb.append("\n\t\t"+Util.upperFirst(table)+" "+table+" = new "+Util.upperFirst(table)+"(");
		for (int i = 0; i < members.size(); i++) {
		sb.append(members.get(i).getName());
		if(i!=members.size()-1)sb.append(", ");else sb.append(");");
		}
		sb.append("\n\t\treturn "+table+";\n\t}");
		return sb.toString();
	}
	/**������Ա�б���*/
	private String MemberList(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic static final String[] memberList = {");
		for (int i = 0; i < members.size(); i++) {
			sb.append("\""+members.get(i).getName()+"\"");
			if(i!=members.size()-1)sb.append(",");
			else sb.append("};\n");
		}
		sb.append("\tpublic String[] getMemberList(){");
		sb.append("\n\t\treturn memberList;\n\t}");
		return sb.toString();
	}
	
	
	
	/**��������Ż������ֵ�ķ���*/
	private String Get(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic Object get(int i){");
		sb.append("\n\t\tObject[] members = {");
		for (int i = 0; i < members.size(); i++) {
			sb.append(members.get(i).getName());
			if(i!=members.size()-1)sb.append(",");
			else sb.append("};\n");
		}
		sb.append("\t\tif(i<members.length)");
		sb.append("return members[i];\n\t\telse \n\t\t\treturn null;\n\t}");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer allInfo = new StringBuffer("package "+url+";\n");
		if(hasDate)allInfo.append(Util.IMPORT_DATE+"\n");
		if(hasBlob)allInfo.append(Util.IMPORT_BLOB+"\n");
		allInfo.append("\nimport java.io.Serializable;\nimport com.myInterface.ResultSet;\nimport java.sql.SQLException;\n");
		allInfo.append("\nimport com.createJavaFile.createModel.ParseResultSetable;\n\n");
		allInfo.append("public class "+Util.upperFirst(table)+" implements ParseResultSetable,Serializable{\n\t");
		allInfo.append("\n\tprivate static final long serialVersionUID = 1L;\n\t");
		allInfo.append(def());
		allInfo.append(conStructor());
		allInfo.append(getSet());
		for (int i = 0; i < members.size(); i++) {
			if(members.get(i).getName().equals(pk)){
				pkIndex = i;
				allInfo.append(Equals(members.get(i)));
			}
		}
		allInfo.append(ToString());
		allInfo.append(ParseOf());
		allInfo.append(MemberList());
		allInfo.append(Get());
		allInfo.append("\n\tpublic final int primaryKey = "+pkIndex+";");
		allInfo.append("\n\tpublic int PrimaryKey(){return primaryKey;}");
		allInfo.append("\n}");
		return allInfo.toString();
	}
	
	/**�����ɵ��ַ���д���ļ�*/
	public void saveModel(){
		try {
			Util.write(toString(),"src."+url,Table+".java");
			System.out.println("ʵ����:"+Table+".java �Ѿ����� "+url);
			ApplicationContext.addProperties(table, url+"."+Table);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�ļ�д���쳣��");
		}
	}

	
	
}

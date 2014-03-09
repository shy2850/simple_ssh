package com.createJavaFile.createModel;

import java.io.IOException;
import java.util.LinkedList;

import com.createJavaFile.myutil.Util;
import com.shy2850.filter.ApplicationContext;

/**<pre>
 * ͨ��ģ�黯��ƴ���ַ���
 * ����java�ļ�
 * ʵ�����ݿ���Ӧ��ʵ�����ɾ�Ĳ�ȷ���
 * </pre>
 * */
public class ModelDao {
	
	/**������������İ�װ��Member����ʵ������ɵ�����*/
	LinkedList<Member> members;
	
	/**���ݿ����*/
	String table;
	/**���ݿ������д*/
	String Table;
	/**���ɵ�java�ļ��ı����ַ*/
	String modelUrl;
	/**��Ӧ��ʵ�����ļ��ı����ַ*/
	String url;
	/**�����ֶε�����*/
	private String pk;
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getPk() {
		return pk;
	}
	/**
	 * @param model  Model�����ɵ���Чʵ��
	 * @param url    java�ļ��ı����ַ:Ĭ����src��,��(com.java.util)
	 */
	public ModelDao(Model model,String url) {
		this.table = model.table;
		this.url = url;
		this.modelUrl = model.url;
		this.members = model.getMembers();
		this.pk = model.getPk();
		Table = Util.upperFirst(table);
	}
	/**
	 * @param model Model�����ɵ���Чʵ��
	 * @param url   java�ļ��ı����ַ:Ĭ����src��,��(com.java.util)
	 * @param pk	��������
	 */
	public ModelDao(Model model,String url,String pk) {
		this.table = model.table;
		this.url = url;
		this.modelUrl = model.url;
		this.members = model.getMembers();
		this.pk = pk;
		Table = Util.upperFirst(table);
	}
	

	/**�����ģ���д��*/
	String include(){
		StringBuffer sb = new StringBuffer();
		sb.append("package "+url+";\n");
		sb.append("\nimport java.sql.SQLException;\n");
		sb.append("import java.util.ArrayList;\n");
		sb.append("import java.util.Collections;\n");
		sb.append("import java.util.List;\n");
		sb.append("\nimport com.createJavaFile.Main.SuperClassDao;\n");
		sb.append("import com.createJavaFile.createModel.ParseResultSetable;\n");
		sb.append("import com.createJavaFile.createModel.SqlColumn;\nimport "+modelUrl+"."+Table+";\n");
		return sb.toString();
	}  //����������ĵ��� 
	
	/**����ǰ�����б�����*/
	String reverse(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic List<"+Table+"> reverse() throws SQLException{//����");
		sb.append("\n\t\tif(needUpdate||null=="+table+"List)find"+Table+"s();");
		sb.append("\n\t\tCollections.reverse("+table+"List);");
		sb.append("\n\t\tsortChanged = !sortChanged;");
		sb.append("\n\t\treturn "+table+"List;");
		sb.append("\n\t}//reverse()\n");
		return sb.toString();
	}
	/**���ʵ������б�*/
	String tableList(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> "+table+"List;");
		sb.append("\n\tpublic List<"+Table+"> get"+Table+"List(){");
		sb.append("\n\t\tif(needUpdate||null=="+table+"List)try {find"+Table+"s();}catch (SQLException e) {throw new RuntimeException(\"���ݿ��쳣\",e);}");
		sb.append("\n\t\treturn "+table+"List;");
		sb.append("\n\t}//get"+Table+"List()\n");
		return sb.toString();
	}
	
	/**�õ���ǰ�����б�ĳ���*/
	String getCount(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic int getCount(){//����ܳ�");
		sb.append("\n\t\tif(null=="+table+"List)");
		sb.append("\n\t\t\ttry {return find"+Table+"s().size();");
		sb.append("\n\t\t\t} catch (SQLException e) {");
		sb.append("\n\t\t\te.printStackTrace();return 0;}");
		sb.append("\n\t\treturn "+table+"List.size();");
		sb.append("\n\t}//getCount()\n");
		return sb.toString();
	}
	
	/**
	 * pageSize ��ҳ�б��ҳ���С
	 * pageNum	��ҳ�б�ĵ�ǰҳ��
	 * 			���ط�ҳ���õĶ����б�(��ȡ��)
	 */
	String pageOf(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic List<"+Table+"> pageOf(int pageSize,int pageNum){//��ҳ");
		sb.append("\n\t\tif(pageSize<=1||pageNum<1)return null;");
		sb.append("\n\t\tList<"+Table+"> list = new ArrayList<"+Table+">();");
		sb.append("\n\t\tint index = (pageNum-1)*pageSize;");
		sb.append("\n\t\tfor (int i = index; i < index+pageSize; i++) {");
		sb.append("\n\t\t\tif(i>=getCount())break;");
		sb.append("\n\t\t\tlist.add("+table+"List.get(i));");
		sb.append("\n\t\t}\n\t\treturn list;\n\t}//pageOf()\n");
		return sb.toString();
	}
	
	/** ģ����ѯʵ�ֵõ��б�(�ڲ����ð�) */
	private String privateFind(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> find"+Table+"(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tParseResultSetable "+table+" = new "+Table+"();");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"select * from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\tif(null != s.getColumnName()){");
		sb.append("\n\t\t\t\tif(null == s.getColumnValue())sql.append(\"and \"+s.getColumnName()+\" is null \");");
		sb.append("\n\t\t\t\telse if(\"\".equals(s.getColumnValue()))sql.append(\"and \"+s.getColumnName()+\" is not null \");");
		sb.append("\n\t\t\t\telse {");
		sb.append("\n\t\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" like '%\"+s.getColumnValue()+\"%' \");");
		sb.append("\n\t\t\t\t\telse  sql.append(\"and \"+s.getColumnName()+\" != '%\"+s.getColumnValue()+\"%' \");");
		sb.append("\n\t\t\t\t}\n\t\t\t}");
		sb.append("\n\t\t\tif(i==sqlColumns.length-1&&null==s.getColumnName())sql.append(\"order by \"+s.getColumnValue());");
		sb.append("\n\t\t}\n\t\tList<Object> list = dbmanager.executeQuery(sql.toString(),");
		sb.append(" show_sql, ");
		sb.append(table+");");
		sb.append("\n\t\tList<"+Table+"> "+table+"List = new ArrayList<"+Table+">();");
		sb.append("\n\t\tfor(int i=0;i<list.size();i++){");
		sb.append("\n\t\t\t"+table+"List.add(("+Table+")list.get(i));");
		sb.append("\n\t\t\t}\n\t\treturn "+table+"List;\n\t}//find"+Table+"()\n");
		return sb.toString();
	}
	
	/** ��ȷ��ѯʵ�ֵõ��б�(�ڲ����ð�)*/
	private String privateGet(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tprivate List<"+Table+"> get"+Table+"(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tParseResultSetable "+table+" = new "+Table+"();");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"select * from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\tif(null != s.getColumnName()){");
		sb.append("\n\t\t\t\tif(null == s.getColumnValue())sql.append(\"and \"+s.getColumnName()+\" is null \");");
//		sb.append("\n\t\t\t\telse if(\"\".equals(s.getColumnValue()))sql.append(\"and \"+s.getColumnName()+\" is not null \");");
		sb.append("\n\t\t\t\telse {");
		sb.append("\n\t\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" = '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t\t\telse  sql.append(\"and \"+s.getColumnName()+\" != '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t\t}\n\t\t\t}");
		sb.append("\n\t\t\tif(i==sqlColumns.length-1&&null==s.getColumnName())sql.append(\"order by \"+s.getColumnValue());");
		sb.append("\n\t\t}\n\t\tList<Object> list = dbmanager.executeQuery(sql.toString(),");
		sb.append(" show_sql, ");
		sb.append(table+");");
		sb.append("\n\t\tList<"+Table+"> "+table+"List = new ArrayList<"+Table+">();");
		sb.append("\n\t\tfor(int i=0;i<list.size();i++){");
		sb.append("\n\t\t\t"+table+"List.add(("+Table+")list.get(i));");
		sb.append("\n\t\t\t}\n\t\treturn "+table+"List;\n\t}//get"+Table+"()\n");
		return sb.toString();
	}
	
	/** ģ����ѯʵ�ֵõ��б�(�ⲿ���ð�)*/
    String publicFind(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t/** ģ����ѯʵ�ֵõ��б�");
		sb.append("\n\t  * @param sqlColumns �����ѯ�Ĳ����������һ�������name����Ϊ�գ���value��toString��Ϊorder_by����");
		sb.append("\n\t  * @return   ����һ������б�");
		sb.append("\n\t  * @throws SQLException �����׳�SQL�쳣");
		sb.append("\n\t  */");
		sb.append("\n\tpublic List<"+Table+"> find"+Table+"s(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\t"+table+"List = find"+Table+"(sqlColumns);");
		sb.append("\n\t\treturn "+table+"List;\n\t}//find"+Table+"s()\n");
		return sb.toString();
	}
    
    /** ��ȷ��ѯʵ�ֵõ��б�(�ⲿ���ð�) */
    String publicGet(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\t/** ��ȷ��ѯʵ�ֵõ��б�");
		sb.append("\n\t  * @param sqlColumns �����ѯ�Ĳ����������һ�������name����Ϊ�գ���value��toString��Ϊorder_by����");
		sb.append("\n\t  * @return   ����һ������б�");
		sb.append("\n\t  * @throws SQLException �����׳�SQL�쳣");
		sb.append("\n\t  */");
		sb.append("\n\tpublic List<"+Table+"> get"+Table+"s(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\t"+table+"List = get"+Table+"(sqlColumns);");
		sb.append("\n\t\treturn "+table+"List;\n\t}//get"+Table+"s()\n");
		return sb.toString();
	}
	
    /**��������*/
	String getByPK(Member m){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic "+Table+" get"+Table+"ByPK("+m.getType()+" "+m.getName()+") throws SQLException{");
		sb.append("\n\t\tList<"+Table+"> list = get"+Table+"(new SqlColumn(\""+m.getName()+"\","+m.getName()+"));");
		sb.append("\n\t\tif(list.size() == 0)return null;");
		sb.append("\n\t\telse return list.get(0);\n\t}\n");
		return sb.toString();
	}
	
	String deleteByPK(Member m){
		return "\n" +
				"\tpublic void deleteByBK("+m.getType()+" "+m.getName()+") throws SQLException{" +
				"\n\t\tString sql = new String(\"delete from "+table+" where "+m.getName()+" = \" + "+m.getName()+");" +
				"\n\t\tint n = dbmanager.executeUpdate(sql, show_sql);" +
				"\n\t\tif(n>0)needUpdate = true;" +
				"\n\t}";
	}
	
	/**����ʵ��ģ���д��*/
	String saveModel(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic void save("+Table+" "+table+") throws SQLException{\n\t");
		sb.append("int n = dbmanager.executeUpdate(\"insert into "+table+" values(");
		for (int i = 0; i < members.size(); i++) {
			sb.append("?");
			if(i!=members.size()-1)sb.append(",");
			else sb.append(")\",");
		}
		sb.append(" show_sql, ");
		for (int i = 0; i < members.size(); i++) {
			sb.append(table+"."+members.get(i).get());
			if(i!=members.size()-1)sb.append(",");
			else sb.append(");");
		}
		sb.append("\n\tif(n>0)needUpdate = true;");
		sb.append("\n\t}//save()\n");
		return sb.toString();
	}
	
	/**ɾ��ʵ��ģ���д��*/
	String deleteModel(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic void delete(SqlColumn...sqlColumns)throws SQLException{");
		sb.append("\n\t\tStringBuffer sql = new StringBuffer(\"delete from "+table+" where 1=1 \");");
		sb.append("\n\t\tfor (int i = 0; i < sqlColumns.length; i++) {");
		sb.append("\n\t\t\tSqlColumn s = sqlColumns[i];");
		sb.append("\n\t\t\tif(null!=s.getColumnValue()&&!\"\".equals(s.getColumnValue())){" +
				"\n\t\t\t\tif(s.isExist())sql.append(\"and \"+s.getColumnName()+\" = '\"+s.getColumnValue()+\"' \");" +
				"\n\t\t\t\telse sql.append(\"and \"+s.getColumnName()+\" != '\"+s.getColumnValue()+\"' \");");
		sb.append("\n\t\t\t}\n\t\t}\n\t\tint n = dbmanager.executeUpdate(sql.toString()");
		sb.append(", show_sql");
		sb.append(");");
		sb.append("\n\t\tif(n>0)needUpdate = true;\n\t}//delete()\n");
		return sb.toString();
	}
	
	/**����ʵ��ģ���д��*/
	String updateModel(Member m){
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tpublic void update("+Table+" "+table+") throws SQLException{");
		sb.append("\n\t\tint n = dbmanager.executeUpdate(\"update "+table+" set ");
		for (int i = 0; i < members.size(); i++) {
			if(m!=members.get(i)){
				sb.append(members.get(i).getName()+"=?");
				if(i!=members.size()-1)sb.append(",");
			}
			if(i==members.size()-1) sb.append(" where "+m.getName()+"=?\",");
		}
		sb.append(" show_sql, ");
		for (int i = 0; i < members.size(); i++) {
			if(m!=members.get(i)){
				sb.append(table+"."+members.get(i).get()+",");
			}
			if(i==members.size()-1)sb.append(table+"."+m.get()+");");
		}
		sb.append("\n\tif(n>0)needUpdate = true;");
		sb.append("\n\t}//update()\n");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(include());
		sb.append("\npublic class "+Table+"Dao extends SuperClassDao{\n");
		sb.append(tableList());
		sb.append(reverse());
		sb.append(getCount());
		sb.append(pageOf());
		sb.append(privateFind());
		sb.append(publicFind());
		sb.append(privateGet());
		sb.append(publicGet());
		sb.append(saveModel());
		sb.append(deleteModel());
		for (int i = 0; i < members.size(); i++) {
			Member m = members.get(i);
			if(m.isAutoIncrement && (null != (pk = m.getName())) || m.getName().equals(pk)){
				sb.append(getByPK(m));
				sb.append(deleteByPK(m));
				sb.append(updateModel(members.get(i)));
			}
		}
		sb.append("\n}//class "+Table+"Dao");
		return sb.toString();
	}
	
	/**�����ɵ��ַ���д���ļ�*/
	public void saveModelDao(){
		try {
			Util.write(toString(),"src."+url,Table+"Dao.java");
			System.out.println("DAO������:"+Table+"Dao.java �Ѿ����� "+url);
			ApplicationContext.addProperties(table+"Dao", url+"."+Table+"Dao");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�ļ�д���쳣��");
		}
	}
	
}

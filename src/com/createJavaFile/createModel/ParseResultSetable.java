package com.createJavaFile.createModel;

import java.sql.SQLException;

import com.myInterface.ResultSet;

/**ϵͳ�е����ݿ��ӳ��ʵ���඼����ʵ�ָýӿڵ�*/
public interface ParseResultSetable {

	/**�����ת������Ӧ��ʵ�����*/
	Object parseOf(ResultSet rs) throws SQLException;
	/**�������ݿ���е��ֶ��б�*/
	String[] getMemberList();
	/**���ֶ��б��Ӧ���е����Եķ���ֵ*/
	Object get(int i);
	/**���ݿ�������±꣬-1��ʾû�ж���������*/
	int PrimaryKey();
}

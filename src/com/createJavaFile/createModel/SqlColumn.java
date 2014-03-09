package com.createJavaFile.createModel;

/**<pre>
 * �������ݿ���а�װ��
 * ���ڲ�ѯ��
 * exist��ʾ��ѯ���Ǵ����¼����ǲ������¼���Ĭ��Ϊ�����¼�
 * </pre>
 * */
public class SqlColumn {
	
	private String columnName;
	private Object columnValue;
	private boolean exist;
	
	public boolean isExist() {
		return !exist;
	}
	public void setExist(boolean exist) {
		this.exist = !exist;
	}
	/**
	 * @param columnName  ����
	 * @param columnValue ��ֵ
	 */
	public SqlColumn(String columnName, Object columnValue) {
		this.columnName = columnName;
		this.columnValue = columnValue;
	}
	/**
	 * @param columnValue ��ֵ
	 */
	public SqlColumn(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	
	

}

package com.myInterface;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Savepoint;

/**��ϵͳʹ�õİ�װ�����ݿ�����*/
public interface Connection extends Serializable {
	/**��ǰConnection�Ƿ����ڱ�ʹ��*/
	boolean isBeingUsed();
	/**���õ�ǰConnectionΪ����ʹ����*/
	void setBeingUsed(boolean beingUsed);
	/**���õ�ǰConnection�Ƿ��ֶ��ύ*/
	void setAutoCommit(boolean autoCommit) throws SQLException;
	/**���õ�ǰConnection�Ƿ�ֻ����*/
	void setReadOnly(boolean readOnly) throws SQLException;
	/**���õ�ǰConnection�Ļع������*/
	Savepoint setSavepoint(String name) throws SQLException;
	/**��ǰConnection�ع���ָ���ı����*/
	void rollback(Savepoint savepoint) throws SQLException;
	/**��ǰConnection�ع�*/
	void rollback() throws SQLException;
	/**�ͷŵ�ǰConnection�Ļع������*/
	void releaseSavepoint(Savepoint savepoint) throws SQLException;
	/**����һ�� PreparedStatement ���������������� SQL ��䷢�͵����ݿ⡣*/
	PreparedStatement prepareStatement(String sql) throws SQLException;
	/**������ Connection �����Ƿ��Ѿ����رա�*/
	boolean isClosed() throws SQLException;
	/**������ Connection ����ĵ�ǰ�Զ��ύģʽ��*/
	boolean getAutoCommit() throws SQLException;
	/***/
	NClob createNClob() throws SQLException;
	/***/
	Clob createClob() throws SQLException;
	/***/
	Blob createBlob() throws SQLException;
	/***/
	void commit() throws SQLException;
	/**����һ�� Statement �������� SQL ��䷢�͵����ݿ⡣*/
	Statement createStatement() throws SQLException;
	/**���ô洢����*/
	public CallableStatement prepareCall(String sql) throws SQLException;
}

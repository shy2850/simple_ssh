package com.createJavaFile.connectionSource;


import com.createJavaFile.connectionSource.ConnectionPool;
import com.myInterface.Connection;

/**���ӳص�һ����򵥵�ʵ������*/
public class ConnectionPoolImpl extends ConnectionPool {
	private int index;
	public static int MAXCONNECTIONS = 5;
	{
		for (int i = 0; i < MAXCONNECTIONS; i++) {
			connections.add(provider.getConnection());
		}
	}
	
	/** �����ӳ���ȡ������ */
	public Connection getConnection() {
		index ++;
		if(index >= connections.size())index=0;
		return connections.get(index);
	}

	/** �������ͷ� */
	public void releaseConnection(Connection con) {
		con = null;
	}

}

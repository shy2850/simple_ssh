package com.createJavaFile.connectionSource;

import java.sql.SQLException;

import com.createJavaFile.Main.ConnectionImpl;

/**ͨ���̳�com.createJavaFile.Main.ConnectionImplʵ�ֹر��Ѿ�����������*/
class ConnectionClosed extends ConnectionImpl {

	private static final long serialVersionUID = -4856393312002742870L;

	/**�ر��Ѿ�����������*/
	public ConnectionClosed(ConnectionImpl conn) throws SQLException{
		super(conn);
		this.conn.close();
	}
	
	
	
}

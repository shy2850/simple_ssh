/**
 * ��װJDBC�ӿ�
 */
package com.myInterface;

import java.sql.SQLException;

public interface CallableStatement extends PreparedStatement {
	
	void setObject(String parameterName, Object x) throws SQLException;

}

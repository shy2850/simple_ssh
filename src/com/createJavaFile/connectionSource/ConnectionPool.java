package com.createJavaFile.connectionSource;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.createJavaFile.Main.ConnectionImpl;
import com.createJavaFile.Main.DBManager;
import com.createJavaFile.myutil.PropertyReader;
import com.myInterface.Connection;
import com.myInterface.IConnectionProvider;


/**<pre>
 * ConnectionPool:���ӳػ���
 * ϵͳ����ʵ�ֵ����ӳر����Ǽ̳��Դ����(���չ��췽����)�ǳ�����
 * ��������ѡȡϵͳ�����õ��̳߳���(ͨ���������ļ��е�CONNECTION_POOL������ֵ(������)ʵ��)
 * ���໹�����ṩ����ϵͳ��������
 * </pre>
 * */
public abstract class ConnectionPool{
	
	public static final String CONNECTION_PROVIDER = "CONNECTION_PROVIDER";
	
	/**connections����ǰ���ӳ��е���������������*/
	protected static final ArrayList<Connection> connections = new ArrayList<Connection>();

	/** ��ӡSQL����PrintStream out */
	private static PrintStream out = DBManager.getOut();
	
	/**��ѡ�����ӳأ�ͨ��getConnectionPool()������ʵ��*/
	public static ConnectionPool  connectionPoolImpl = getConnectionPool();
	
	/**����һ�����Ӳ���������*/
	protected IConnectionProvider provider = getConnectionProvider();
	
	/** �����ӳ���ȡ������ */
	public abstract Connection getConnection(); 
    
	/** �������ͷ� */
	public abstract void releaseConnection(Connection con);
	
	/**<pre>
	 * ͨ��db.conf�����ļ���CONNECTION_PROVIDER������ֵ(������)��ȡ
	 * û������ʱ��ʹ��ϵͳ�Դ���ConnectionProvider��ʵ��
	 * </pre>
	 * */
	private static IConnectionProvider getConnectionProvider(){
		String CONNECTION_PROVIDER = PropertyReader.get("CONNECTION_PROVIDER");
		if(null == CONNECTION_PROVIDER){
			CONNECTION_PROVIDER = "com.createJavaFile.connectionSource.ConnectionProvider";
		}
		IConnectionProvider  icp = null; 
		try {
			icp= (IConnectionProvider) Class.forName(CONNECTION_PROVIDER).newInstance();
		} catch (InstantiationException e) {
			out.print(e);
		} catch (IllegalAccessException e) {
			out.print(e);
		} catch (ClassNotFoundException e) {
			out.print(e);
		}
		return icp;
	}
	
	/**<pre>
	 * ͨ��db.conf�����ļ���CONNECTION_POOL������ֵ(������)��ȡ
	 * û������ʱ��ʹ��ϵͳ�Դ���ConnectionPoolImpl��ʵ��
	 * </pre>
	 * */
    static ConnectionPool getConnectionPool(){
    	
		String CONNECTION_POOL = PropertyReader.get("CONNECTION_POOL");
		if(null == CONNECTION_POOL){
			out.print("���ӳ�Ϊ�գ�ʹ��Ĭ�����ӳأ�");
			CONNECTION_POOL = "com.createJavaFile.connectionSource.ConnectionPoolImpl";
		}
		ConnectionPool connectionPool = null;
		try {
			connectionPool = (ConnectionPool) (Class.forName(CONNECTION_POOL).newInstance());
		} catch (InstantiationException e) {
			out.print(e);
		} catch (IllegalAccessException e) {
			out.print(e);
		} catch (ClassNotFoundException e) {
			out.print(e);
		}
		return connectionPool;
	}
    
    /**��дObject��finalize()������ʵ��������ǰ�ر����г��е�����*/
    protected void finalize() {
    	out.println("ConnectionPool.finalize()");
        close();
      }

    /** �ر����ӳ�*/	
    public void close() {
        Iterator<Connection> iter = connections.iterator();
        while ( iter.hasNext()) {
          try {
        	  Connection conn = iter.next();
              new ConnectionClosed((ConnectionImpl) conn);
              conn = null;
          }catch (SQLException e){out.print(e);}
        }
        connections.clear();		
      }
}




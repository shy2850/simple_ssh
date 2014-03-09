package com.shy2850.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**<pre>
 * ����ĳ����ǣ�
 * ��Ҫ��cookie�д�Ŷ�������
 * ���Խ����л�����������ת��Ϊ�ַ���
 * ����ת������ַ����ж�ȡ����
 * ע����Ҫrt.jar��
 * </pre>
 * */
public class EncodeObjectToString {

	/**�����л�����ת��Ϊ�ַ���*/
	public static String EncodeToString(Serializable obj,String unicode){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			BASE64Encoder base64 = new BASE64Encoder();
			String str = base64.encode(baos.toByteArray());
			String finalStr = URLEncoder.encode(str,unicode);
			return finalStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**���ַ�����ԭΪ����*/
	public static Object DecodeToObject(String str,String unicode){
		try {
			String baseStr = URLDecoder.decode(str,unicode);
			BASE64Decoder base64 = new BASE64Decoder();
			byte[] b = base64.decodeBuffer(baseStr);
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (UnsupportedEncodingException e) {
			System.out.println("ת�����Ͳ�֧�֣�"+e);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}

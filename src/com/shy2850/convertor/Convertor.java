package com.shy2850.convertor;


/**<pre>
 * ת������
 * �û��Զ����ת��������Ҫͨ��ConvertorUtils.registע����Ч
 * ��ǰtypeΪ�������Ͱ�װ�����String/Dateʱ�Ḳ��ԭ�е�����ת����
 * ÿ��ת����������󶼱���ͨ������Ĺ��췽������type��set������������
 * </pre>
 * */
public abstract class Convertor{
	
	/**��ǰת������������*/
	public abstract Class<?> getType();

	public Convertor() {
	}
	
	/**����ת������*/
	public abstract Object parse(String convertorString);
	
}

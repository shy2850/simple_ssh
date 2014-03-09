package com.shy2850.convertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.createJavaFile.Main.DBManager;

/**ϵͳת����DateConvertor*/
class DateConvertor extends Convertor {
	
	/**ϵͳ���Ѿ��ṩ��Date�ַ���ת����ʽ */
	public static final String[] DATEFORMATs = {
		"yyyy-MM-dd hh:mm:ss",
		"yyyy-MM-dd",
		"yyyy��MM��dd�� hhʱmm��ss��",
		"yyyy��MM��dd��",
		"MM/dd/yyyy"
		};
	
	private final static List<String> formats = new ArrayList<String>();
	static{
		for (int i = 0; i < DATEFORMATs.length; i++) {
			formats.add(DATEFORMATs[i]);
		}
	}
	
	/**
	 * ��ָ��λ�����format�ַ���
	 * @param index		���format��λ�ã�nullʱ��ӵ���β
	 * @param strings	Ҫ��ӵ��ַ���
	 */
	static final void addDateformats(Integer index, String...strings){
		for (int i = 0; i < strings.length; i++) {
			if(null == index)formats.add(strings[i]);
			else{
				formats.add(index, strings[i]);
			}
		}
	}
	
	public DateConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		Date date = null;
		for (int i = 0; i < formats.size(); i++) {
			date = parse(formats.get(i), convertorString);
			if(null != date)return date;
		}
		DBManager.getOut().println("String:"+convertorString+" �޷�ת����java.util.Date");
		return null;
	}

	public Class<?> getType() {
		return Date.class;
	}
	
	/**
	 * ����ָ���ַ���ǿ��ת��string��java.util.Date
	 * @param format			ָ���ַ���
	 * @param convertorString	Ҫǿ��ת����string����
	 * @return
	 */
	private Date parse(String format,String convertorString){
		try {
			return new SimpleDateFormat(format).parse(convertorString);
		} catch (ParseException e) {
			return null;
		}
	}
}

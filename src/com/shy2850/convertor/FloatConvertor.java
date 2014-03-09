package com.shy2850.convertor;

import com.createJavaFile.Main.DBManager;

/**ϵͳת����FloatConvertor*/
class FloatConvertor extends Convertor{

	public FloatConvertor() {
		super();
	}
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Float.parseFloat(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" �޷�ת����java.lang.Float");
			return null;
		}
	}

	public Class<?> getType() {
		return null;
	}
	
}

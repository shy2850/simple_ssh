package com.shy2850.convertor;

import com.createJavaFile.Main.DBManager;

/**ϵͳת����IntegerConvertor*/
class IntegerConvertor extends Convertor {

	public IntegerConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Integer.parseInt(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" �޷�ת����java.lang.Ingeter");
			return null;
		}
	}

	public Class<?> getType() {
		return Integer.class;
	}

}

package com.shy2850.convertor;

import com.createJavaFile.Main.DBManager;

/**ϵͳת����LongConvertor*/
class LongConvertor extends Convertor {
	
	public LongConvertor() {
		super();
	}
	
	public Object parse(String convertorString) {
		if(null == convertorString || "null".equals(convertorString))return null;
		try {
			return Long.parseLong(convertorString);
		} catch (NumberFormatException e) {
			DBManager.getOut().println("String:"+convertorString+" �޷�ת����java.lang.Long");
			return null;
		}
	}

	public Class<?> getType() {
		return Long.class;
	}

}

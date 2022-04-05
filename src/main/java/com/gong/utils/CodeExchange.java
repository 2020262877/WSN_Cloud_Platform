package com.gong.utils;

public class CodeExchange {
	public static String chinese(String str) {
		if (str == null)
			str = "";
		try {
			str = new String(str.getBytes("ISO-8859-1"), "utf-8");
		} catch (Exception e) {
			str = "";
			e.printStackTrace();
		}
		return str;
	}

	public static String specilSymbol(String str) {
		str = str.replace("&", "&amp;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace("'", "&quot;");
		str = str.replace("\0x0d", "<br>");
		return str;
	}
}

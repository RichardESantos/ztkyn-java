package org.gitee.ztkyn.core.date;

/**
 * 时间格式枚举
 */
public enum DateStyle {

	yyyy_MM_dd("yyyy_MM_dd"),

	yyyy_MM_dd_HH_mm_ss("yyyy_MM_dd HH:mm:ss"),

	;

	private String formatter;

	DateStyle(String formatter) {
		this.formatter = formatter;
	}

}

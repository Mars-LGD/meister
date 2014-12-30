package com.xiaotian.exception;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * 通过ErrorNumber获取错误message<BR>
 * 
 * @author xiaotian15
 * 
 */
public class ExceptionMessages {
	private static final String BUNDLE_NAME = "exception_messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ExceptionMessages() {
	}

	/**
	 * 通过ErrorNumber获取错误message
	 * 
	 * @param _nErrorNumber	错误编码
	 * @return
	 */
	public static String getString(int _nErrorNumber) {
		String key = "ExNum." + String.valueOf(_nErrorNumber);
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
/**
 * 
 */
package com.tinyhomeutil.homedisk.i18n;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动加载资源包
 * @author jack
 */
public class LocaleToolkit {
	
	private final static String LANGUAGE_RESOURCE = "language.properties";
	private final static String DEFAULT = "default";
	private final static String DEFAULT_LANGUAGE = "en";
	private final static String BASE_RESOURCE = "com.tinyhomeutil.homedisk.i18n.messages";
	private static Map<String,ResourceBundle> bundles;
	
	static {
		bundles = new HashMap<String,ResourceBundle>();
		Properties prop = new Properties();
		try {
			prop.load(LocaleToolkit.class.getResourceAsStream(LANGUAGE_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Locale locale = null;
		ResourceBundle messages = null;
		Set<Object> set = prop.keySet();
		for ( Object obj : set ) {
			locale = new Locale(obj.toString());
			messages = ResourceBundle.getBundle(BASE_RESOURCE, locale);
			bundles.put(obj.toString(), messages);
		}
		//由于ResourceBundle无法指向默认的资源，在这里手动设置默认资源
		locale = new Locale(DEFAULT_LANGUAGE);
		messages = ResourceBundle.getBundle(BASE_RESOURCE, locale);
		bundles.put(DEFAULT, messages);
	}
	
	/**
	 * 获取指定地区所使用的语言
	 * @param locale 指定区域对象
	 * @param input 内容标识
	 * @return 指定国家的语言
	 */
	public static String getLocaleString(Locale locale,String input) {
		ResourceBundle messages = getBundle(locale);
		if ( messages != null ) {
			return messages.getString(input);
		} 
		return null;
	}

	/**
	 * 获取指定地区的资源文件对象
	 * @param locale 指定区域对象
	 * @return 资源文件对象
	 */
	public static ResourceBundle getBundle(Locale locale) {
		if ( locale != null ) {
			ResourceBundle messages = bundles.get(locale.getLanguage());
			if ( messages == null ) {
				messages = bundles.get(DEFAULT);
			}
			return messages;
		}
		return null;
	}

	/**
	 * 从request中取得parameter值，再根据此值取出国际化字符串
	 * @param messages
	 * @param request
	 * @param parameter
	 * @return
	 */
	public static String getString(ResourceBundle messages,HttpServletRequest request,String parameter) {
		if ( messages != null && request != null ) {
			return messages.getString((String)request.getParameter(parameter));
		}
		return null;
	}
}

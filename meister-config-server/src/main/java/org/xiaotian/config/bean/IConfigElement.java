package org.xiaotian.config.bean;

import org.xiaotian.config.ConfigException;

/**
 * castor xml标签对应实体标记接口
 * 
 * @author xiaotian15
 * 
 */
public interface IConfigElement {
	
	/**
	 * xml标签对应实体有效性验证
	 * 
	 * @return	验证是否成功，成功返回true，不成功返回false
	 */
	public boolean valid() throws ConfigException;

}
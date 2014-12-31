/*
 * IResource.java created on 2006-9-27 下午05:11:06 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery;

import java.net.URL;

/**
 * 资源的抽象描述
 * 
 * @author		Martin (付成睿)
 */
public interface IResource
{

	/**
	 * 返回资源的名称
	 * 
	 * @return	资源的名称
	 */
	public String getName();

	/**
	 * 返回资源的路径
	 * 
	 * 资源的路径，相对于资源所在库文件根的路径。
	 * 
	 * @return	资源的路径
	 */
	public String getPath();

	/**
	 * 返回资源是否只读
	 * 
	 * @return	资源是否只读的
	 */
	public boolean isReadOnly();

	/**
	 * 是否Java的类型（Class或者Interface），如果返回<code>true</code>，
	 * 则该资源必须也是<code>com.trs.infra.discover.IType</code>的实例，
	 * 即可以强制转换类型为<code>com.trs.infra.discover.IType</code>。
	 * 
	 * @return	是否Java的类型
	 */
	public boolean isJavaType();

	/**
	 * 返回资源的URL
	 * 
	 * @return	资源的URL
	 */
	public URL getURL();

}
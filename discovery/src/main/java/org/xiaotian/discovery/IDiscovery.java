/*
 * IDiscovery.java created on 2006-9-27 下午03:25:21 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;

/**
 * 分析、查找指定的Java Class库文件集合中的Class继承关系等
 * 
 * @author		Martin (付成睿)
 */
public interface IDiscovery
{

	/**
	 * 释放持有的全部资源
	 */
	public void cleanup();

	/**
	 * 以树状结构输出全部的资源到指定的输出流
	 */
	public void tree(PrintWriter pw);

	//常规的查找资源的方法

	/**
	 * 返回指定名称的资源，如果资源不存在，则返回<code>null</code>，如果存在同名的多个资源，
	 * 只返回找到的第一个。
	 * 
	 * 如果指定的资源名称只包括文件名部分，则在全部库文件中所有的package下搜索资源。
	 * 如果指定的资源名称是以“/”分割的限定形式，则不管名称是否以“/”开始，都认为是相对于库文件根的
	 * 绝对路径，所以只在全部库文件中的指定路径下搜索资源。
	 * 
	 * @param	name	资源的名称
	 * @return	指定资源的URL，或者<code>null</code>
	 */
	public URL getResource(String name);

	/**
	 * 返回所有具有指定名称的资源，如果资源不存在，则返回空的迭代器。
	 * 
	 * 如果指定的资源名称只包括文件名部分，则在全部库文件中所有的package下搜索资源。
	 * 如果指定的资源名称是以“/”分割的限定形式，则不管名称是否以“/”开始，都认为是相对于库文件根的
	 * 绝对路径，所以只在全部库文件中的指定路径下搜索资源。
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.net.URL</code>的形式返回资源的URL，
	 * 迭代器的<code>remove()</code>方法不会被实现。
	 * 
	 * @param	name	资源的名称
	 * @return	全部符合模式的资源的迭代器
	 */
	public Iterator getResources(String name);

	/**
	 * 返回指定类型的直接子类型，如果指定的资源不存在或者不是Java类型，则返回<code>null</code>；如果
	 * 指定类型是final类型，也返回<code>null</code>；如果不存在直接子类型，则返回空的迭代器。<BR>
	 * 
	 * 类型的名称可以是“.”分的形式，也可以是“/”分的形式，是否以“/”开始均可，但是不可以包括结尾的
	 * “.class”部分。<BR>
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.lang.String</code>的形式返回类型的名称，类型名称
	 * 采用“.”分的形式。
	 * 迭代器的<code>remove()</code>方法不会被实现。<BR>
	 * 
	 * 如果指定的类型是类，则返回其直接子类，包括抽象类。
	 * 如果指定的类型是接口，则返回其直接子接口和直接实现了该接口的类型，包括抽象类。
	 * 
	 * @param	name	指定类型的名称
	 * @return	全部直接子类型的迭代器
	 */
	public Iterator getSubTypes(String name);

	/**
	 * 返回指定类型的所有实现。如果指定的资源不存在或者不是Java类型，则返回<code>null</code>；如果
	 * 指定类型是final类型，也返回<code>null</code>；如果不存在任何实现，则返回空的迭代器。<BR>
	 * 
	 * 类型的名称可以是“.”分的形式，也可以是“/”分的形式，是否以“/”开始均可，但是不可以包括结尾的
	 * “.class”部分。<BR>
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.lang.String</code>的形式返回类型的名称，类型名称
	 * 采用“.”分的形式。
	 * 迭代器的<code>remove()</code>方法不会被实现。<BR>
	 * 
	 * 如果指定的类型是类，则返回其直接和间接子类，不包括抽象类。
	 * 如果指定的类型是接口，则返回其直接实现和间接实现，不包括抽象类。
	 * 间接实现包括子接口的实现、及其子类和直接实现的子类。
	 * 
	 * @param	name	指定类型的名称
	 * @return	全部实现的迭代器
	 */
	public Iterator getImplementors(String name);

	/**
	 * 返回所有冲突类型的迭代器，如果不存在冲突类型，则返回空的迭代器。<BR>
	 * 
	 * 冲突类型，指在两个或者多个jar文件中都包含同一个Class文件的情形，这种情况多是因为把不同版本的库
	 * 文件都放在CLASSPATH中造成的，因为库的版本不同，往往会引起混乱，所以需要排查这些冲突类型。<BR>
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.lang.String</code>的形式返回类型的名称，类型名称
	 * 采用“.”分的形式。
	 * 迭代器的<code>remove()</code>方法不会被实现。
	 * 
	 * @return	所有冲突类型的迭代器
	 */
	public Iterator getCollision();

	/**
	 * 返回指定冲突类型的所有冲突位置的迭代器。如果指定的资源不存在或者不是Java类型，
	 * 则返回<code>null</code>；如果指定的类型不存在冲突，则返回空的迭代器。<BR>
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.net.URL</code>的形式返回冲突类型的位置，
	 * 迭代器的<code>remove()</code>方法不会被实现。
	 * 
	 * 关于以<code>java.net.URL</code>描述的冲突类型的位置，在目前的行为定义中，定义为冲突类型
	 * 自身的完整位置，而不仅仅是冲突类型所在jar文件的位置。但是不排除将来只返回jar文件位置的可能。
	 * 
	 * @return	冲突类型的所有冲突位置的迭代器
	 */
	public Iterator getCollision(String name);

	//高级的查找资源的方法

	/**
	 * 返回所有符合模式的资源，如果找不到符合模式的资源，则返回空的迭代器。
	 * 
	 * 在模式中只支持使用“*”作为通配符，不支持正则表达式，如果使用正则表达式作为模式，
	 * 则该方法的行为不能确定。
	 * 
	 * 迭代器的<code>next()</code>方法以<code>java.net.URL</code>的形式返回资源的URL，
	 * 迭代器的<code>remove()</code>方法不会被实现。
	 * 
	 * @param	pattern	资源名称的模式，只支持使用“*”作为通配符
	 * @return	全部符合模式的资源的迭代器
	 */
	public Iterator findResources(String pattern);

}
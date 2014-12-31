/*
 * DiscoveryFactory.java created on 2006-9-28 上午09:53:34 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery;

import java.io.File;

import javax.servlet.ServletContext;

import org.xiaotian.discovery.impl.FactoryByClassPath;
import org.xiaotian.discovery.impl.FactoryByFiles;
import org.xiaotian.discovery.impl.FactoryByServlet;

/**
 * 用于构造<code>com.trs.infra.discover.IDiscovery</code>的抽象工厂类
 * 
 * @author Martin (付成睿)
 */
public abstract class DiscoveryFactory {

    /**
     * 使用指定的库文件目录直接构造<code>com.trs.infra.discover.IDiscovery</code>实例
     * 
     * @param libs
     *            库文件目录数组
     * @return <code>com.trs.infra.discover.IDiscovery</code>实例
     */
    public final static IDiscovery create(File[] libs) {
        return create(libs, null);
    }

    /**
     * 使用指定的库文件目录和Class文件目录直接构造<code>com.trs.infra.discover.IDiscovery</code>实例
     * 
     * @param libs
     *            库文件目录数组
     * @param dirs
     *            Class文件目录数组
     * @return <code>com.trs.infra.discover.IDiscovery</code>实例
     */
    public final static IDiscovery create(File[] libs, File[] dirs) {
        return getInstance(libs, dirs).build();
    }

    /**
     * 根据指定的<code>javax.servlet.ServletContext</code>直接构造
     * <code>com.trs.infra.discover.IDiscovery</code>实例。
     * 
     * @param sc
     *            指定的<code>javax.servlet.ServletContext</code>实例
     * @return <code>com.trs.infra.discover.IDiscovery</code>实例
     */
    public final static IDiscovery create(ServletContext sc) {
        return getInstance(sc).build();
    }

    /**
     * 使用指定的库文件目录和Class文件目录构造工厂类实例
     * 
     * @param libs
     *            库文件目录数组
     * @param dirs
     *            Class文件目录数组
     * @return 工厂类实例
     */
    public final static DiscoveryFactory getInstance(File[] libs, File[] dirs) {
        return new FactoryByFiles(libs, dirs);
    }

    /**
     * 根据指定的<code>javax.servlet.ServletContext</code>构造工厂类实例
     * 
     * @param sc
     *            指定的<code>javax.servlet.ServletContext</code>实例
     * @return 工厂类实例
     */
    public final static DiscoveryFactory getInstance(ServletContext sc) {
        return new FactoryByServlet(sc);
    }

    /**
     * 使用<code>java.class.path</code>的内容直接构造
     * <code>com.trs.infra.discover.IDiscovery</code>实例
     * 
     * @return <code>com.trs.infra.discover.IDiscovery</code>实例
     */
    public final static IDiscovery create() {
        return getInstance().build();
    }

    /**
     * 根据<code>java.class.path</code>的内容构造工厂类实例
     * 
     * @return 工厂类实例
     */
    public final static DiscoveryFactory getInstance() {
        return new FactoryByClassPath();
    }

    /**
     * 构造方法，只有子类可以访问
     */
    protected DiscoveryFactory() {
    }

    /**
     * 根据当前环境生成<code>IDiscovery</code>实例
     * 
     * @return <code>IDiscovery</code>实例
     */
    public abstract IDiscovery build();

}
/*
 * DiscoveryServletHelper.java created on 2006-9-27 下午03:53:20 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.utils;

import javax.servlet.ServletContext;

import org.xiaotian.discovery.DiscoveryFactory;
import org.xiaotian.discovery.IDiscovery;

/**
 * 结合J2EE环境使用的辅助类，主要定义了一些常量和两个存取<code>IDiscovery</code>对象的方法。
 * 
 * @author      Martin (付成睿)
 * @see         DiscoveryStartupListener
 */
public final class DiscoveryServletHelper {

    /**
     * 需要被分析的库文件列表参数名称
     */
    public final static String IncludeLibrary = "DiscoveryIncludeLibrary";

    /**
     * 不需要分析的库文件列表参数名称
     */
    public final static String ExcludeLibrary = "DiscoveryExcludeLibrary";

    /**
     * 需要被分析的目录列表参数名称
     */
    public final static String IncludeDirectory = "DiscoveryIncludeDirectory";

    /**
     * 不需要分析的目录列表参数名称
     */
    public final static String ExcludeDirectory = "DiscoveryExcludeDirectory";

    /**
     * 全局的IDiscovery实例
     */
    private static IDiscovery s_oGlobalDiscovery = null;

    /**
     * 取回绑定的IDiscovery的对象
     * 
     * 绑定的对象通常是在应用系统启动时由通用Listener（即DiscoveryStartupListener）设定的。
     * 
     * @return  绑定的IDiscovery的对象，或者<code>null</code>
     * @see     #setDiscovery(IDiscovery)
     * @see     DiscoveryStartupListener
     */
    public static IDiscovery getDiscovery() {
        return s_oGlobalDiscovery;
    }

    /**
     * 绑定IDiscovery对象实例
     * 
     * 该方法通常是由通用Listener（即DiscoveryStartupListener）在应用启动时调用。在应用启动以后，
     * 需要使用IDiscovery对象的地方，使用{@link #getDiscovery()}方法取回。
     * 
     * @param  ds  指定的IDiscovery对象
     * @see     #getDiscovery()
     * @see     DiscoveryStartupListener
     */
    public static void setDiscovery(IDiscovery ds) {
        s_oGlobalDiscovery = ds;
    }

    synchronized final static void initDiscovery(ServletContext sc) {
        if (s_oGlobalDiscovery != null) {
            return;
        }

        IDiscovery ds = DiscoveryFactory.create(sc);
        if (ds != null) {
            s_oGlobalDiscovery = ds;
        }
    }

    synchronized final static void destroyDiscovery() {
        if (s_oGlobalDiscovery == null) {
            return;
        }

        s_oGlobalDiscovery.cleanup();
        s_oGlobalDiscovery = null;
    }

    /**
     * 不得外部实例化
     */
    private DiscoveryServletHelper() {
    }

}

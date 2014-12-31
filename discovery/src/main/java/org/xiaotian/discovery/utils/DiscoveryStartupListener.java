/*
 * DiscoveryStartupListener.java created on 2006-9-27 下午03:25:57 by Martin (Fu Chengrui)
 */

package org.xiaotian.discovery.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 实现<code>javax.servlet.ServletContextListener</code>接口，并配置在应的web.xml文件中，以在应用
 * 启动的时候，自动扫描分析指定的库文件，并把分析结果放到{@link DiscoveryServletHelper}中，以方便应
 * 用代码取回分析结果。以及在应用停止时，自动释放所持有的资源。
 * 
 * <pre>
 * web.xml文件的示例：
 * 	&lt;context-param&gt;
 * 	&lt;param-name&gt;DiscoveryIncludeLibrary&lt;/param-name&gt;
 * 	&lt;param-value&gt;trs*.jar,trs*.zip&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * &lt;context-param&gt;
 * 	&lt;param-name&gt;DiscoveryExcludeLibrary&lt;/param-name&gt;
 * 	&lt;param-value&gt;log4j*.jar&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * &lt;context-param&gt;
 * 	&lt;param-name&gt;DiscoveryIncludeDirectory&lt;/param-name&gt;
 * 	&lt;param-value&gt;TRSConfig,hbm,classes&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * &lt;context-param&gt;
 * 	&lt;param-name&gt;DiscoveryExcludeDirectory&lt;/param-name&gt;
 * 	&lt;param-value&gt;test*&lt;/param-value&gt;
 * &lt;/context-param&gt;
 * &lt;listener&gt;
 * 	&lt;listener-class&gt;com.trs.infra.discover.util.DiscoveryStartupListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 * 其中&lt;listener&gt;项的配置是必须，只有配置了该listener才能在应用启动时，自动扫描分析库文件。<BR>
 * 参数“DiscoveryIncludeLibrary”，需要分析的库文件名列表，以逗号分割，可以使用“*”作为通配符，
 * 在进行匹配时文件名是忽略大小写的。在示例中的配置是分析所有以“trs”开始的.jar文件和.zip文件。
 * 如果没有配置，该参数项的缺省值是“*.jar,*.zip”。<BR>
 * 参数“DiscoveryExcludeLibrary”，不需分析的库文件名列表，以逗号分割，可以使用“*”作为通配符，
 * 在进行匹配时文件名是忽略大小写的。在示例中的配置是排除所有以“log4j”开始的.jar文件。如果没有 配置，该参数项美有缺省值。<BR>
 * 参数“DiscoveryIncludeDirectory”，需要分析的目录名列表，以逗号分割，可以使用“*”作为通配符，
 * 在进行匹配时文件名是忽略大小写的，目录是指位于“WEB-INF”下的子文件夹。在示例中的配置是分析
 * “WEB-INF/TRSConfig”、“WEB-INF/hbm”和“WEB-INF/classes”三个子目录。如果没有配置，该参数
 * 项的缺省值是“classes”。<BR>
 * 参数“DiscoveryIncludeDirectory”，不需分析的目录名列表，以逗号分割，可以使用“*”作为通配符，
 * 在进行匹配时文件名是忽略大小写的，目录是指位于“WEB-INF”下的子文件夹。在示例中的配置是排除在
 * “WEB-INF”下所有以“test”开始的子文件夹。该参数没有缺省值。
 * 
 * @author Martin (付成睿)
 * @see DiscoveryServletHelper
 */
public class DiscoveryStartupListener implements ServletContextListener {

	/**
	 * 缺省构造方法
	 */
	public DiscoveryStartupListener() {
	}

	public void contextDestroyed(ServletContextEvent sce) {
		DiscoveryServletHelper.destroyDiscovery();
	}

	public void contextInitialized(ServletContextEvent sce) {
		DiscoveryServletHelper.initDiscovery(sce.getServletContext());
	}

}

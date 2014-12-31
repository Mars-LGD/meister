package org.xiaotian.base.server;

import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyErrors;

/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * TODO <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

public interface IServer {

	/**
	 * 启动应用
	 * 
	 * @return 如果启动成功,返回true <br>
	 *         如果启动失败，返回false
	 */
	public boolean startup() throws WCMException;

	/**
	 * 重新启动应用
	 * 
	 * @return 如果启动成功,返回true <br>
	 *         如果启动失败，返回false
	 */
	public boolean restart();

	/**
	 * 关闭应用
	 */
	public void shutdown() throws WCMException;

	/**
	 * 获取错误信息
	 * 
	 * @return
	 * 
	 */
	public CMyErrors getErrors();

	/**
	 * ===================Application有关信息=====================
	 */

	/**
	 * 当前系统版本号
	 * 
	 * @return
	 */
	public String getVersion();

	/**
	 * 系统内部版本号
	 * 
	 * @return
	 */
	public String getServerBuild();

	/**
	 * 数据库版本号
	 * 
	 * @return
	 */
	public String getDbBuild();

	/**
	 * build时间
	 * 
	 * @return
	 */
	public String getBuildTime();

	/**
	 * 获取服务名称
	 * 
	 * @return
	 */
	public String getServerName();

	/**
	 * 设置服务名称
	 * 
	 * @param _sAppName
	 *            服务名称
	 */
	public void setServerName(String _sAppName);

}
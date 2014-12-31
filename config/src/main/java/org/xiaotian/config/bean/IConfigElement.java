/**
 * Created:         2005-5-11 8:59:47
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class ICfgEntity
 */
package org.xiaotian.config.bean;



/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * 所有配置对象的接口声明 <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

public interface IConfigElement extends IXmlPersistentObject {
    /**
     * 设置配置对象的名称
     * 
     * @param _sName
     *            配置对象的名称
     */
    public void setName(String _sName);

    /**
     * 获取配置对象的名称
     * 
     * @return 配置对象的名称
     */
    public String getName();
}
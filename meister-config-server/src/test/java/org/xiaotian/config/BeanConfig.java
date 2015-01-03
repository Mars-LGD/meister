/**
 * Created:         2005-5-11 10:12:20
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class BeanCfg
 */
package org.xiaotian.config;

import org.xiaotian.config.bean.IConfigElement;


/**
 * Title: TRS ����Э��ƽ̨��TRS WCM�� <BR>
 * Description: <BR>
 * TODO <BR>
 * Copyright: Copyright (c) 2004-2005 TRS��Ϣ�������޹�˾ <BR>
 * Company: TRS��Ϣ�������޹�˾(www.trs.com.cn) <BR>
 * 
 * @author TRS��Ϣ�������޹�˾
 * @version 1.0
 */

public class BeanConfig implements IConfigElement {
    private String id;

    private String className;

    private boolean isSingleton;

    private String name;

    public BeanConfig() {
    }

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public boolean isSingleton() {
        return isSingleton;
    }

    /**
     * @param isSingleton
     */
    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.config.entity.IConfigElement#setName(java.lang.String)
     */
    public void setName(String _sName) {
        name = _sName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.config.entity.IConfigElement#getName()
     */
    public String getName() {
        return name;
    }

}
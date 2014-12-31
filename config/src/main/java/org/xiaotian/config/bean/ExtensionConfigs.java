/**
 * Created:         2005-5-11 8:56:15
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class CacheConfig
 */
package org.xiaotian.config.bean;

import java.util.ArrayList;

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

public class ExtensionConfigs implements IConfigElement {
    private String id;

    private String className;

    private ArrayList configElements;

    public ExtensionConfigs() {
        this.configElements = new ArrayList();
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
     * @return IConfigElement List
     */
    public ArrayList getConfigElements() {
        return configElements;
    }

    /**
     * @param configElements
     */
    public void setConfigElements(ArrayList configElements) {
        this.configElements = configElements;
    }

    public void addConfigElements(IConfigElement element) {
        this.configElements.add(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.config.entity.IConfigElement#setName(java.lang.String)
     */
    public void setName(String _sName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.trs.infra.config.entity.IConfigElement#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
}
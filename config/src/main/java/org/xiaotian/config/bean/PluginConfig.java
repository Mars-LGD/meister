package org.xiaotian.config.bean;

import java.util.ArrayList;

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

public class PluginConfig {
    private String m_sHostFilePath = null;

    private String id;

    private String name;

    private String version;

    private ArrayList beans;

    private ArrayList extensions;

    public PluginConfig() {
        this.beans = new ArrayList();
        this.extensions = new ArrayList();
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
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return
     */
    public ArrayList getExtensions() {
        return extensions;
    }

    /**
     * @param cfgEntities
     */
    public void setExtensions(ArrayList cfgEntities) {
        this.extensions = cfgEntities;
    }

    public void addExtensions(ExtensionConfigs _cfg) {
        this.extensions.add(_cfg);
    }

    /**
     * @return
     */
    public ArrayList getBeans() {
        return beans;
    }

    /**
     * @param beans
     */
    public void setBeans(ArrayList beans) {
        this.beans = beans;
    }

    public void addBeans(BeanConfig _bean) {
        this.beans.add(_bean);
    }

    /**
     * @return
     */
    public String getHostFilePath() {
        return m_sHostFilePath;
    }

    /**
     * @param cofingFilePath
     */
    public void setHostFilePath(String cofingFilePath) {
        m_sHostFilePath = cofingFilePath;
    }
}
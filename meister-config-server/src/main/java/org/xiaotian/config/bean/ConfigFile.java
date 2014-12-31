/**
 * Created:         2005-5-24 15:42:05
 * Last Modified:   2005-5-24/2005-5-24
 * Description:
 *      class ConfigFile
 */
package org.xiaotian.config.bean;

import java.io.File;

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

public class ConfigFile {
    private File plugin;

    private File mapping;

    /**
     * @param _file
     * @param mapping2
     */
    public ConfigFile(File _plugin, File _mapping) {
        this.plugin = _plugin;
        this.mapping = _mapping;
    }

    /**
     * @return
     */
    public File getMapping() {
        return mapping;
    }

    /**
     * @param mapping
     */
    public void setMapping(File mapping) {
        this.mapping = mapping;
    }

    /**
     * @return
     */
    public File getPlugin() {
        return plugin;
    }

    /**
     * @param plugin
     */
    public void setPlugin(File plugin) {
        this.plugin = plugin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "plugin=" + this.plugin.getPath() + ", mapping="
                + mapping.getPath();
    }
}
package org.xiaotian.config.bean;

import java.util.ArrayList;

/**
 * castor xml <plugin>标签对应Config实体
 * 
 * @author xiaotian15
 * 
 */
public class PluginConfig {
	private String m_sHostFilePath = null;

	private String id;

	private String name;

	private String version;

	private ArrayList<IConfigElement> extensions;

	public PluginConfig() {
		this.extensions = new ArrayList<IConfigElement>();
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
	public ArrayList<IConfigElement> getExtensions() {
		return extensions;
	}

	/**
	 * @param cfgEntities
	 */
	public void setExtensions(ArrayList<IConfigElement> cfgEntities) {
		this.extensions = cfgEntities;
	}

	public void addExtensions(ExtensionConfig _cfg) {
		this.extensions.add(_cfg);
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
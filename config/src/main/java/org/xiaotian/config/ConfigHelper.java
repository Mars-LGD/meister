package org.xiaotian.config;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xiaotian.config.bean.ConfigConstants;
import org.xiaotian.config.bean.ConfigFiles;
import org.xiaotian.config.server.TRSConfigServer;
import org.xiaotian.constants.ExceptionNumber;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;
import org.xiaotian.utils.XmlDocWithDom4j;
import org.xml.sax.InputSource;

public class ConfigHelper {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConfigHelper.class);

	public static boolean RUN_IN_ECLIPSE = false;

	private static ArrayList<String> m_arDomainConfigPaths = new ArrayList<String>();

	private static ArrayList<String> m_arServiceConfigPaths = new ArrayList<String>();

	private static ArrayList<String> s_arClassPaths = new ArrayList<String>();

	public static ArrayList<String> getDomainConfigPaths() {
		Properties sysProperties = System.getProperties();

		// 需要进行初始化
		if (m_arDomainConfigPaths.isEmpty()) {
			synchronized (m_arDomainConfigPaths) {
				// DCL
				if (!m_arDomainConfigPaths.isEmpty()) {
					return m_arDomainConfigPaths;
				}

				ArrayList<String> arTemp = new ArrayList<String>();
				boolean bRunInEclipse = RUN_IN_ECLIPSE;
				String[] pClassPaths = sysProperties.getProperty("java.class.path").split(";");
				for (int i = 0; i < pClassPaths.length; i++) {
					String sClassPath = pClassPaths[i];
					File file = new File(sClassPath);
					if (file.isDirectory()) {
						sClassPath = file.getAbsolutePath();
						if (!bRunInEclipse && sClassPath.endsWith(".cp")) {
							bRunInEclipse = true;
							// System.out.println("Eclipse");
						} else {
							if (!sClassPath.endsWith(File.separator)) {
								sClassPath += File.separator;
							}
							sClassPath += "trsconfig" + File.separator + "domain" + File.separator;
							File path = new File(sClassPath);
							if (path.exists())
								arTemp.add(sClassPath);
						}
					}
				}

				// 如果不是在Eclipse中运行，按照老的规则进行
				if (!bRunInEclipse) {
					arTemp = new ArrayList<String>();
					arTemp.add(ConfigConstants.getConfigRootPath());
				}

				m_arDomainConfigPaths = arTemp;
			}
		}

		return m_arDomainConfigPaths;
	}

	public static ArrayList<String> getClassPaths() {
		Properties sysProperties = System.getProperties();

		// 需要进行初始化
		if (s_arClassPaths.isEmpty()) {
			synchronized (s_arClassPaths) {
				// DCL
				if (!s_arClassPaths.isEmpty()) {
					return s_arClassPaths;
				}

				ArrayList<String> arTemp = new ArrayList<String>();
				boolean bRunInEclipse = RUN_IN_ECLIPSE;
				String[] pClassPaths = sysProperties.getProperty("java.class.path").split(";");
				for (int i = 0; i < pClassPaths.length; i++) {
					String sClassPath = pClassPaths[i];
					File file = new File(sClassPath);
					if (file.isDirectory()) {
						sClassPath = file.getAbsolutePath();
						if (!bRunInEclipse && sClassPath.endsWith(".cp")) {
							bRunInEclipse = true;
							// System.out.println("Eclipse");
						} else {
							if (!sClassPath.endsWith(File.separator)) {
								sClassPath += File.separator;
							}
							arTemp.add(sClassPath);
						}
					}
				}

				// 如果不是在Eclipse中运行，按照老的规则进行
				if (!bRunInEclipse) {
					arTemp = new ArrayList<String>();
					arTemp.add(TRSConfigServer.getAppRootPath());
				}

				s_arClassPaths = arTemp;
			}
		}

		return s_arClassPaths;
	}

	public static ArrayList<String> getServiceConfigPaths() {
		Properties sysProperties = System.getProperties();

		// 需要进行初始化
		if (m_arServiceConfigPaths.isEmpty()) {
			synchronized (m_arServiceConfigPaths) {
				// DL
				if (!m_arServiceConfigPaths.isEmpty()) {
					return m_arServiceConfigPaths;
				}

				ArrayList<String> arTemp = new ArrayList<String>();
				boolean bRunInEclipse = RUN_IN_ECLIPSE;
				String[] pClassPaths = sysProperties.getProperty("java.class.path").split(";");
				for (int i = 0; i < pClassPaths.length; i++) {
					String sClassPath = pClassPaths[i];
					File file = new File(sClassPath);
					if (file.isDirectory()) {
						sClassPath = file.getAbsolutePath();
						if (!bRunInEclipse && sClassPath.endsWith(".cp")) {
							bRunInEclipse = true;
							// System.out.println("Eclipse");
						} else {
							if (!sClassPath.endsWith(File.separator)) {
								sClassPath += File.separator;
							}
							sClassPath += "trsconfig" + File.separator;
							File path = new File(sClassPath);
							if (path.exists())
								arTemp.add(sClassPath);
						}
					}
				}

				// 如果不是在Eclipse中运行，按照老的规则进行
				if (!bRunInEclipse) {
					arTemp = new ArrayList<String>();
					arTemp.add(TRSConfigServer.getConfigRootPath());
				}
				m_arServiceConfigPaths = arTemp;
			}
		}

		return m_arServiceConfigPaths;
	}

	/**
	 * 将指定的xml文件流转化文档模型
	 * 
	 * @param _file
	 *            指定要转换的文件流
	 * @return 转化后的文档对象
	 * @throws CMyException
	 */
	public static Document toXMLDocument(File _file) throws CMyException {
		if (_file == null) {
			throw new CMyException(ExceptionNumber.ERR_OBJ_NULL, I18NMessage.get(ConfigHelper.class, "ConfigHelper.label1", "指定的XML文件不存在!"));
		}

		// Parse XML Document
		try {
			// return new DOMBuilder().build(_file);

			// return DocumentHelper
			return XmlDocWithDom4j.parse(_file.getAbsolutePath());
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_XMLFILE_PARSE, I18NMessage.get(ConfigHelper.class, "ConfigHelper.label2",
					"建立XML Document对象失败!"), ex);
		}
	}

	/**
	 * 组织所有的映射文件，形成一个文件流，用于Mapping对象的创建
	 * 
	 * @param _configFiles
	 * 
	 * @return 所有的映射文件的文件流
	 * @throws CMyException
	 */
	public static InputSource getMappingSource(ConfigFiles _configFiles) throws CMyException {
		// Document coreMapDoc =
		// getMappingDoc(ConfigConstants.PATH_FILE_MAPPING_CORE);
		Document coreMapDoc = getMappingDoc(ConfigConstants.getAbsoluteFileNameOfCoreMapping());
		Document pluginMapDoc = null;

		for (int i = 0; i < _configFiles.size(); i++) {
			File fMapping = _configFiles.get(i).getMapping();
			if (fMapping == null)
				continue;
			if (fMapping.getPath().compareTo(ConfigConstants.getAbsoluteFileNameOfCoreMapping()) != 0) {
				pluginMapDoc = getMappingDoc(fMapping.getPath());
				List nodeList = pluginMapDoc.getRootElement().elements("class");
				for (int nodeIndex = nodeList.size(); nodeIndex > 0;) {
					Element node = (Element) nodeList.get(--nodeIndex);
					node.getParent().remove(node);
					coreMapDoc.getRootElement().add(node);
				}
			}
		}

		// if (s_logger.isDebugEnabled())
		// {
		// s_logger.debug(CMyXMLDoc.toString(coreMapDoc));
		// }

		return toFileInputSrcFromXmlDocument(coreMapDoc);
	}

	/**
	 * 通过文件名得到 Mapping 的输入流
	 * 
	 * @param _sMappingFile
	 *            Mapping 文件路径+文件名
	 * @return 转换后的输入流对象
	 * @throws CMyException
	 */
	public static InputSource getMappingSource(String _sMappingFile) throws CMyException {
		return toFileInputSrcFromXmlDocument(getMappingDoc(_sMappingFile));
	}

	/**
	 * 将指定的文档对象转为一个 InputSource
	 * 
	 * @param _oXmlDoc
	 *            指定的文档对象
	 * @return 转换后的输入流对象
	 */
	private static InputSource toFileInputSrcFromXmlDocument(Document _oXmlDoc) {
		// return new InputSource(new
		// StringBufferInputStream(CMyXMLDoc.toString(_oXmlDoc)));
		// return new InputSource(new
		// StringReader(CMyXMLDoc.toString(_oXmlDoc)));
		return new InputSource(new StringReader(_oXmlDoc.asXML()));
	}

	/**
	 * 通过文件名得到 Mapping 的文档对象
	 * 
	 * @param _sMappingFile
	 *            Mapping 文件路径+文件名
	 * @return 转换后的文档对象
	 * @throws CMyException
	 */
	private static Document getMappingDoc(String _sMappingFile) throws CMyException {
		Document result = null;
		try {
			logger.debug("Loading the mapping file[" + _sMappingFile + "]");

			// result = CMyXMLDoc.toXMLDocument(_sMappingFile);
			result = XmlDocWithDom4j.parse(_sMappingFile);
		} catch (Exception ex) {
			// if
			// (_sMappingFile.compareTo(ConfigConstants.PATH_FILE_MAPPING_CORE)
			// == 0) {
			if (_sMappingFile.compareTo(ConfigConstants.getAbsoluteFileNameOfCoreMapping()) == 0) {
				return getEmptyXMLDocument();
			}
			throw new CMyException(I18NMessage.get(ConfigHelper.class, "ConfigHelper.label3", "读取或解析文件[") + _sMappingFile
					+ I18NMessage.get(ConfigHelper.class, "ConfigHelper.label4", "]时出错"), ex);
		}
		return result;
	}

	/**
	 * 得到一个空的文档对象
	 * 
	 * @return
	 */
	private static Document getEmptyXMLDocument() {
		// return new Document(new Element("plugin"));
		return DocumentHelper.createDocument();
	}

}
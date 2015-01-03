package org.xiaotian.config;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.xiaotian.config.bean.ConfigFiles;
import org.xiaotian.constants.ExceptionNumber;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;
import org.xiaotian.utils.XmlDocWithDom4j;
import org.xml.sax.InputSource;

/**
 * config.xml编组与解组辅助类（验证文件的基本合法性）
 * 
 * @author xiaotian15
 * 
 */
public class ConfigHelper {

	private static org.apache.log4j.Logger m_logger = org.apache.log4j.Logger
			.getLogger(ConfigHelper.class);

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
			throw new CMyException(ExceptionNumber.ERR_OBJ_NULL,
					I18NMessage.get(ConfigHelper.class, "ConfigHelper.label1",
							"指定的XML文件不存在!"));
		}
		try {
			return XmlDocWithDom4j.parse(_file.getAbsolutePath());
		} catch (Exception ex) {
			throw new CMyException(ExceptionNumber.ERR_XMLFILE_PARSE,
					I18NMessage.get(ConfigHelper.class, "ConfigHelper.label2",
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
	@SuppressWarnings("unchecked")
	public static InputSource getMappingSource(ConfigFiles _configFiles)
			throws CMyException {
		if (_configFiles == null || _configFiles.size()==0)
			throw new CMyException("缺少编组解组的castor配置文件");

		// 所有mapping.xml文件合成的最终文件对象
		Document coreMapDoc = getMappingDoc(ConfigConstants.getRootMappingFilePath());

		// 各个mapping.xml文档对象
		Document pluginMapDoc = null;

		// 遍历读取_configFiles
		for (int i = 0; i < _configFiles.size(); i++) {
			File fMapping = _configFiles.get(i).getMapping();
			if (fMapping == null)
				continue;
			pluginMapDoc = getMappingDoc(fMapping.getPath());
			List<Element> nodeList = pluginMapDoc.getRootElement().elements(
					"class");
			for (int nodeIndex = nodeList.size(); nodeIndex > 0;) {
				Element node = nodeList.get(--nodeIndex);
				node.getParent().remove(node);
				coreMapDoc.getRootElement().add(node);
			}

		}

		// 将指定的文档对象转为一个 InputSource
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
	public static InputSource getMappingSource(String _sMappingFile)
			throws CMyException {
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
	private static Document getMappingDoc(String _sMappingFile)
			throws CMyException {
		Document result = null;
		try {
			m_logger.debug("Loading the mapping file[" + _sMappingFile + "]");
			result = XmlDocWithDom4j.parse(_sMappingFile);
		} catch (Exception ex) {
			throw new CMyException("读取或解析文件[" + _sMappingFile + "]时出错", ex);
		}
		return result;
	}

}
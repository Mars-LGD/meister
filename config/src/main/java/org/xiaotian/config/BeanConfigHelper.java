/**
 * Created:         2005-5-11 14:47:20
 * Last Modified:   2005-5-11/2005-5-11
 * Description:
 *      class BeanCfgStreamLoader
 */
package org.xiaotian.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocumentType;
import org.xiaotian.config.bean.BeanConfig;
import org.xiaotian.config.bean.ConfigConstants;
import org.xiaotian.config.server.BeanConfigServer;
import org.xiaotian.exception.WCMException;
import org.xiaotian.extend.CMyException;
import org.xiaotian.language.I18NMessage;

/**
 * Title: TRS 内容协作平台（TRS WCM） <BR>
 * Description: <BR>
 * SpringBean对象配置信息的管理类 <BR>
 * Copyright: Copyright (c) 2004-2005 TRS信息技术有限公司 <BR>
 * Company: TRS信息技术有限公司(www.trs.com.cn) <BR>
 * 
 * @author TRS信息技术有限公司
 * @version 1.0
 */

public class BeanConfigHelper {

    /**
     * the xml document model of root &lt;beans&gt;
     */
    private Document m_oBeansDocument;

    /**
     * the xml document model of element &lt;bean&gt;
     */
    private Element m_elBeansRoot;

    /**
     * SpringBean Config List &lt;BeanConfig&gt;
     */
    private List m_listBeanConfigCollection;

    public BeanConfigHelper(List _listBeanConfig) {
        this.m_listBeanConfigCollection = _listBeanConfig;

        // m_elBeansRoot = new Element(ConfigConstants.TAG_ELEMENT_BEANS);
        m_elBeansRoot = DocumentHelper
                .createElement(ConfigConstants.TAG_ELEMENT_BEANS);
        m_oBeansDocument = DocumentHelper.createDocument(m_elBeansRoot);// new
                                                                        // Document
                                                                        // (
                                                                        // m_elBeansRoot
                                                                        // );

        // DocType type = new DocType("beans", "-//SPRING//DTD BEAN//EN",
        // "http://www.springframework.org/dtd/spring-beans.dtd");
        DocumentType type = new DefaultDocumentType("beans",
                "-//SPRING//DTD BEAN//EN",
                "http://www.springframework.org/dtd/spring-beans.dtd");
        m_oBeansDocument.setDocType(type);

    }

    /**
     * 得到所有SpringBeand的inputStream，特用于SpringBean的创建
     * 
     * @return 所有SpringBeand的inputStream
     * @throws WCMException
     */
    public InputStream getBeanConfigXmlInputStream() throws CMyException {
        if (m_listBeanConfigCollection == null
                || m_listBeanConfigCollection.isEmpty())
            throw new CMyException(I18NMessage.get(BeanConfigHelper.class,
                    "BeanConfigHelper.label1", "尚未读取配置文件，初始化失败"));

        String beanTag = ConfigConstants.TAG_ELEMENT_BEAN;
        String beanAttrId = ConfigConstants.TAG_ATTRIBUTE_BEANID;
        String beanAttrClazz = ConfigConstants.TAG_ATTRIBUTE_BEANCLASSNAME;
        String beanAttrSigleton = ConfigConstants.TAG_ATTRIBUTE_BEANISSINGLETON;

        for (int i = 0; i < m_listBeanConfigCollection.size(); i++) {
            BeanConfig bean = (BeanConfig) m_listBeanConfigCollection.get(i);
            Element elm = DocumentHelper.createElement(beanTag);
            elm.addAttribute(beanAttrId, bean.getId());
            elm.addAttribute(beanAttrClazz, bean.getClassName());
            if (bean.isSingleton()) {
                elm.addAttribute(beanAttrSigleton, "true");
            } else {
                elm.addAttribute(beanAttrSigleton, "false");
            }

            m_elBeansRoot.add(elm);
        }

        // String sDocContent = CMyXMLDoc.toString(m_oBeansDocument);
        // s_logger.debug("stream content:" + sDocContent);

        m_oBeansDocument.setXMLEncoding("UTF-8");

        return getInputStream(m_oBeansDocument.asXML(), "UTF-8");
    }

    // private static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger
    // .getLogger(BeanConfigHelper.class);

    /**
     * 将String转换为InputStream
     * 
     * @param String
     * @return InputStream
     */
    private static InputStream getInputStream(String _sValue, String _sEncoding)
            throws CMyException {
        try {
            byte[] bytes = _sValue.getBytes(_sEncoding);
            return new ByteArrayInputStream(bytes);
        } catch (Exception ex) {
            throw new CMyException(
                    "Failed to convert string into UTF-8 stream.", ex);
        }
    }

    /**
     * 回收
     */
    public void unload() {
        this.m_elBeansRoot = null;
        this.m_oBeansDocument = null;
        this.m_listBeanConfigCollection.clear();
    }

    /**
     * 校验指定的beanId对应的SpringBean是否存在， <br>
     * 通常在设置和某一SpringBean相关联的配置对象属性时进行
     * 
     * @param _beanId
     *            指定关联的SpringBean的id标识
     * @param _sName
     *            在证该SpringBean不存在时， <br>
     *            显示详细信息时打印出配置对像给的名称
     */
    public static void checkIsBeanValid(String _beanId, String _sName) {
        // 1. 检验BeanConfigServer是否已启动
        if (!BeanConfigServer.started())
            return;

        // 2.验证该SpringBean是否存在
        if (!BeanConfigServer.getInstance().containsBean(_beanId))
            throw new RuntimeException("[name="
                    + _sName
                    + I18NMessage.get(BeanConfigHelper.class,
                            "BeanConfigHelper.label2", "]的配置对象，其[BeanId=")
                    + _beanId
                    + I18NMessage.get(BeanConfigHelper.class,
                            "BeanConfigHelper.label3", "]无效！"));
    }

    /**
     * 校验指定的beanId对应的SpringBean是否存在， <br>
     * 通常在设置和某一SpringBean相关联的配置对象属性时进行
     * 
     * @param _beanId
     *            指定关联的SpringBean的id标识
     */
    public static void checkIsBeanValid(String _beanId) {
        checkIsBeanValid(_beanId, "UNKOWN");
    }

}
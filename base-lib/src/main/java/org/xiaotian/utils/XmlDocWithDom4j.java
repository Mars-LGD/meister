/**
 * Created:         2006-4-12 10:44:52
 * Last Modified:   2006-4-12/2006-4-12
 * Description:
 *      class XmlDocWithDom4j
 */
package org.xiaotian.utils;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Title: TRS ����Э��ƽ̨��TRS WCM�� <BR>
 * Description: <BR>
 * Test dom4j <BR>
 * Copyright: Copyright (c) 2004-2005 TRS��Ϣ�������޹�˾ <BR>
 * Company: TRS��Ϣ�������޹�˾(www.trs.com.cn) <BR>
 * 
 * @author TRS��Ϣ�������޹�˾
 * @version 1.0
 */

public class XmlDocWithDom4j {

    private static final String DEFAUTL_ENCODING = "UTF-8";

    public static Document parse(String _sXmlFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(false);
        Document result = reader.read(_sXmlFile);
        return result;
    }

    public static Document parse(String _sXmlFile, String _sXslFile)
            throws DocumentException, TransformerException {
        Document result = parse(_sXmlFile);
        return styleDocument(result, _sXslFile);
    }

    public static void write(Document _document, String _sTargetFile)
            throws FileNotFoundException {
        try {
            write(_document, DEFAUTL_ENCODING, _sTargetFile);
        } catch (UnsupportedEncodingException e) {
            // Ignore. never?
        }
    }

    public static void write(Document _document, String _encoding,
            String _sTargetFile) throws UnsupportedEncodingException,
            FileNotFoundException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(
                _sTargetFile));
        OutputFormat format = OutputFormat.createPrettyPrint();
        if (!isEmpty(_encoding)) {
            format.setEncoding(_encoding);
        }

        XMLWriter writer = null;
        try {
            writer = new XMLWriter(os, format);
            writer.write(_document);
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
        } finally {
            try {
                os.close();
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // Ignore.
            }
        }
    }

    public static void write(Document _document, String _xslFile,
            String _encoding, String _result) throws TransformerException,
            UnsupportedEncodingException, FileNotFoundException {
        Document resultdoc = styleDocument(_document, _xslFile);
        write(resultdoc, _encoding, _result);
    }

    public static Document styleDocument(Document _document, String _stylesheet)
            throws TransformerException {
        if (isEmpty(_stylesheet)) {
            return _document;
        }

        StreamSource style = new StreamSource(_stylesheet);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(style);

        DocumentSource source = new DocumentSource(_document);
        DocumentResult result = new DocumentResult();
        transformer.transform(source, result);

        Document transformedDoc = result.getDocument();
        return transformedDoc;
    }

    public static void transform(String _sSourceXml, String _sSourceXsl,
            String _sTarget) throws TransformerException,
            FileNotFoundException, DocumentException {
        try {
            transform(_sSourceXml, _sSourceXsl, DEFAUTL_ENCODING, _sTarget);
        } catch (UnsupportedEncodingException e) {
            // Ignore never.
        }
    }

    public static void transform(String _sSourceXml, String _sSourceXsl,
            String _encoding, String _sTarget) throws FileNotFoundException,
            DocumentException, TransformerException,
            UnsupportedEncodingException {
        Document source = parse(_sSourceXml);
        Document result = styleDocument(source, _sSourceXsl);
        write(result, _encoding, _sTarget);
    }

    public static void main(String[] args) {
        try {
            String fn = "e:\\dom4j.xml";
            Document doc = parse(fn);
            System.out.println(doc.nodeCount());
            System.out.println(doc.getRootElement().getName());
            List list = doc.getRootElement().element("properties").elements();
            for (int i = 0; i < list.size(); i++) {
                Element ele = (Element) list.get(i);
                if (ele != null) {
                    System.out.println(ele.getName() + ":" + ele.getText()
                            + " hascon:" + ele.hasContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isEmpty(String s) {
        return (s == null) || (s.trim().length() == 0);
    }
}
/*
 *	Main.java
 *	History				Who				What
 *	2009-9-29 		 	wenyh			Created.
 */
package org.xiaotian.discovery;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.xiaotian.discovery.impl.ASMUtil;

/**
 * Title: TRS 内容协作平台（TRS WCM）<BR>
 * Description: <BR>
 * TODO <BR>
 * Copyright: Copyright (c) 2004-2009 北京拓尔思信息技术股份有限公司<BR>
 * Company: 北京拓尔思信息技术股份有限公司(www.trs.com.cn)<BR>
 * 
 * @author wenyh
 * @version 1.0
 * 
 */

public class Main {
    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fins = new FileInputStream("/c:/users/leafgray/desktop/sms.class");
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = fins.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        ASMUtil.getNodeFromData(baos.toByteArray());
    }
}

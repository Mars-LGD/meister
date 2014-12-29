/**
 * Created:         2004-12-11 14:44:17
 * Last Modified:   2004-12-11/2004-12-11
 * Description:
 *      class ExceptionMessages
 */
package com.xiaotian.exception;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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

public class ExceptionMessages {
    private static final String BUNDLE_NAME = "exception_messages";//$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private ExceptionMessages() {
    }

    public static String getString(int _nErrorNumber) {
        String key = "ExNum." + String.valueOf(_nErrorNumber);
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
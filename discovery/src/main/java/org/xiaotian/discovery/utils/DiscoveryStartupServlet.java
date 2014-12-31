/*
 * DiscoveryStartupServlet.java
 * Description:
 *		DiscoveryStartupServlet
 * Update Log:
 *	2008-12-16 下午05:44:21	FuChengrui
 *		Created
 */

package org.xiaotian.discovery.utils;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * 在 Servlet 2.3 (含)，及之前的版本中，用于确保 Discovery 先于其他初始化
 * 
 * @author	Martin (Fu Chengrui)
 * @see     DiscoveryStartupListener
 */
public class DiscoveryStartupServlet extends HttpServlet {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6075724559753109262L;

    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        // Nothing to to
    }

    public void init(ServletConfig sc) throws ServletException {
        DiscoveryServletHelper.initDiscovery(sc.getServletContext());
    }

    public void destroy() {
        DiscoveryServletHelper.destroyDiscovery();
    }

}

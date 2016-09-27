package nl.isaac.dotcms.plugin.configuration.web.servlet.admin;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
*
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.isaac.dotcms.plugin.configuration.ConfigurationDotCMSCacheGroupHandler;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService;

import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Logger;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

/**
 * A simple servlet that calls {@link ConfigurationService#clearCache()} and redirects to a specific <code>returnAddress</code>
 * @author maarten
 *
 */
@SuppressWarnings("serial")
public class ClearCacheServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (!WebAPILocator.getUserWebAPI().isLoggedToBackend(req)) {
				resp.sendError(404);
				return;
			}
		} catch (DotRuntimeException e) {
			throw new ServletException(e);
		} catch (PortalException e) {
			throw new ServletException(e);
		} catch (SystemException e) {
			throw new ServletException(e);
		}

		ConfigurationService.clearCache();
		ConfigurationDotCMSCacheGroupHandler.clearCache();

		Logger.debug(getClass(), "Cleared the Configuration cache");

		resp.sendRedirect(req.getParameter("returnAddress"));
	}
}

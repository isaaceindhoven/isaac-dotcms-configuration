package nl.isaac.dotcms.plugin.configuration.filter;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nl.isaac.dotcms.plugin.configuration.ConfigurationService;

/**
 * The only purpose of this class is to set and clear the default parameters for {@link ConfigurationService} while the request is active.
 * @author maarten
 *
 */
public class RequestStoringFilter implements Filter {

	@Override public void init(FilterConfig filterConfig) throws ServletException {}
	@Override public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ConfigurationService.storeDefaultParameters(new RequestBasedConfigurationParameters((HttpServletRequest) request));
		try {
			chain.doFilter(request, response);
		} finally {
			ConfigurationService.clearDefaultParameters();
		}
	}


}

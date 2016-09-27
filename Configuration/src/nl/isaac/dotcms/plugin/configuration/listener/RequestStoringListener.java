package nl.isaac.dotcms.plugin.configuration.listener;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService.ConfigurationParameters;

/**
 * The only purpose of this class is to set and clear the default parameters for {@link ConfigurationService} while the request is active.
 * @author maarten
 *
 */
public class RequestStoringListener implements ServletRequestListener {

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		if (sre.getServletRequest() instanceof HttpServletRequest) {
			final ConfigurationParameters previousParams = ConfigurationService.getDefaultParameters();
			ConfigurationService.storeDefaultParameters(new RequestBasedConfigurationParameters((HttpServletRequest) sre.getServletRequest(), previousParams));
		}
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		final ConfigurationParameters current = ConfigurationService.getDefaultParameters();
		if (current instanceof RequestBasedConfigurationParameters
		&& ((RequestBasedConfigurationParameters) current).getPrevious() != null) {
			ConfigurationService.storeDefaultParameters(((RequestBasedConfigurationParameters) current).getPrevious());
		} else {
			ConfigurationService.clearDefaultParameters();
		}
	}

}

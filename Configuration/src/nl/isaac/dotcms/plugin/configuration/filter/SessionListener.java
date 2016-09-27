package nl.isaac.dotcms.plugin.configuration.filter;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import nl.isaac.dotcms.plugin.configuration.ConfigurationService;

/**
 * This session listener will clear session/ip bound configurations when the session is destroyed.
 * It also initializes a Set on session creation.
 * @author maarten
 *
 */
public class SessionListener implements HttpSessionListener{
	public static final String configurationsSessionKey = ConfigurationService.class.getName() + ".DEV_SESSION_CONFIGURATION_STORAGE";

	@Override public void sessionCreated(HttpSessionEvent se) {
		if (ConfigurationService.cacheOnIp) {
			se.getSession().setAttribute(configurationsSessionKey, new HashSet<String>());
		}
	}

	@Override public void sessionDestroyed(HttpSessionEvent se) {
		if (ConfigurationService.cacheOnIp) {
			@SuppressWarnings("unchecked")
			Set<String> keys = (Set<String>) se.getSession().getAttribute(configurationsSessionKey);
			for (String key : keys) {
				ConfigurationService.removeConfigurationForKey(key);
			}
		}
	}

}

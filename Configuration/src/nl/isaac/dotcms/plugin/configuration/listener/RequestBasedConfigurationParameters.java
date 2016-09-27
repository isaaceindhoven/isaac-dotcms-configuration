package nl.isaac.dotcms.plugin.configuration.listener;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService.ConfigurationParameters;

import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;

/**
 * An implementation of {@link ConfigurationService.ConfigurationParameters} based on a HttpServletRequest to retrieve the HostName, SessionId and IpAddress, and it uses a Session attribute to store ip specific bound configurations.
 * @author maarten
 *
 */
public class RequestBasedConfigurationParameters implements ConfigurationService.ConfigurationParameters {

	private final HttpServletRequest request;
	private final ConfigurationParameters previous;
	public RequestBasedConfigurationParameters(HttpServletRequest request, ConfigurationService.ConfigurationParameters previous) {
		this.request = request;
		this.previous = previous;
	}
	public ConfigurationParameters getPrevious() {
		return previous;
	}

	@Override
	public String getHostName() {
		try {
			return WebAPILocator.getHostWebAPI().getCurrentHost(request).getHostname();
		} catch (PortalException e) {
			throw new IllegalArgumentException("Error while retrieving host", e);
		} catch (SystemException e) {
			throw new IllegalArgumentException("Error while retrieving host", e);
		} catch (DotDataException e) {
			throw new IllegalArgumentException("Error while retrieving host", e);
		} catch (DotSecurityException e) {
			throw new IllegalArgumentException("Error while retrieving host", e);
		}
	}

	@Override
	public String getIpAddress() {
		return request.getRemoteAddr();
	}

	@Override
	public String getSessionId() {
		return request.getSession().getId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addConfigurationToSession(String key) {
		if (ConfigurationService.cacheOnIp) {
			Set<String> configurations = ((Set<String>) request.getSession().getAttribute(SessionListener.configurationsSessionKey));
			if (configurations != null) {
				configurations.add(key);
			}
		}
	}

}

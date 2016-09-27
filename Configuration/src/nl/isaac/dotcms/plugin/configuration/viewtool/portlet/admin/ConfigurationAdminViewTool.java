package nl.isaac.dotcms.plugin.configuration.viewtool.portlet.admin;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;

import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.Logger;

/**
 * This viewtool provides several tools for use in the Configuration Admin page
 * @author maarten
 *
 */
public class ConfigurationAdminViewTool implements ViewTool {

	public List<String> getAllHosts() throws DotDataException, DotSecurityException {
		List<Host> hosts = APILocator.getHostAPI().findAll(APILocator.getUserAPI().getSystemUser(), false);
		List<String> hostNames = new ArrayList<String>(hosts.size());
		for (Host host : hosts) {
			hostNames.add(host.getHostname());
		}
		return hostNames;
	}

	public List<String> getAllPlugins(String hostName, HttpServletRequest request) {
		List<String> plugins = APILocator.getPluginAPI().getDeployedPluginOrder();
		List<String> pluginNames = new ArrayList<String>(plugins.size());
		for (String pluginName : plugins) {
			if (null != tryGetPluginConfiguration(hostName, pluginName, request)) {
				pluginNames.add(pluginName);
			}
		}
		return pluginNames;
	}

	/**
	 * This returns a String representation of a config, at the moment it's using a multiline approach for CustomConfiguration rendering.
	 * @param config
	 * @return
	 */
	public String printConfiguration(CustomConfiguration config) {
		return config.toStringMultiLine(true);
	}

	/**
	 * This attempts to retrieve a specific host configuration, if an exception occurs then null is returned
	 * @param hostName
	 * @param request
	 * @return
	 */
	public CustomConfiguration tryGetHostConfiguration(String hostName, HttpServletRequest request) {
		try {
			return ConfigurationService.getHostConfiguration(hostName, request.getRemoteAddr(), request.getSession().getId());
		} catch (ConfigurationException e) {
			Logger.debug(getClass(), "Failed to retrieve host configuration: " + hostName);
			return null;
		}
	}
	/**
	 * This attempts to retrieve a specific plugin configuration, if an exception occurs then null is returned
	 * @param hostName
	 * @param pluginName
	 * @param request
	 * @return
	 */
	public CustomConfiguration tryGetPluginConfiguration(String hostName, String pluginName, HttpServletRequest request) {
		try {
			return ConfigurationService.getPluginConfiguration(pluginName, hostName, request.getRemoteAddr(), request.getSession().getId());
		} catch (ConfigurationException e) {
			Logger.debug(getClass(), "Failed to retrieve plugin configuration: " + hostName + ":" + pluginName);
			return null;
		}
	}

	@Override
	public void init(Object arg0) {
	}
}

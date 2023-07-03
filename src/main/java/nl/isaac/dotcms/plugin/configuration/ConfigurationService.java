package nl.isaac.dotcms.plugin.configuration;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
*
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder.ConfigurationProvider;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.lang.text.StrLookup;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.dotmarketing.util.Logger;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.comp.configuration.types.EnvironmentType;
import nl.isaac.dotcms.plugin.configuration.dotcms.DotCMSFileConfigurationProvider;
import nl.isaac.dotcms.plugin.configuration.exception.ConfigurationNotFoundException;
import nl.isaac.dotcms.plugin.configuration.listener.RequestStoringListener;
import nl.isaac.dotcms.plugin.configuration.osgi.OSGiFileConfigurationProvider;
import nl.isaac.dotcms.plugin.configuration.util.EmptyConfiguration;

/**
 * The class to retrieve your configurations from.
 * It look under {@link #serverConfigXmlLocation} for the general configuration, such as what environment type the server is, and where to find the host and plugin configuration stacks.
 * It also contains the server side, in memory, configuration cache.
 *
 * The hierarchy of configuration loading is primairly defined by {@link #locationConfigHosts} for host configurations and {@link #locationConfigPlugins} for plugin configurations.
 * At the time of writing the order for hosts is:
 * The serverConfig.xml
 * The file located at ${sys:catalina.home}/conf/applications/${isaac:hostName}/${isaac:ClientIPAddress}/hostConfig.xml
 * The file located at ${sys:catalina.home}/conf/applications/${isaac:hostName}/hostConfig.xml
 * And lastly, the file, for a specific host, located inside dotCMS itself at /config/hostConfig.xml
 *
 * At the time of writing the order for plugins is:
 * The serverConfig.xml
 * The file located at ${sys:catalina.home}/conf/applications/${isaac:hostName}/${isaac:pluginName}/${isaac:ClientIPAddress}/pluginConfig.xml
 * The file located at ${sys:catalina.home}/conf/applications/${isaac:hostName}/pluginConfig.xml
 * The file, for a specific host, located inside dotCMS itself at /config/${isaac:pluginName}/pluginConfig.xml
 * The file located at ${sys:catalina.home}/conf/applications/${isaac:pluginName}/pluginConfig.xml
 *
 * @author maarten
 *
 */
public class ConfigurationService {
	private static final String serverConfigXmlLocation = "${sys:catalina.home}/conf/applications/configurationPluginConfig.xml";
	private static final String locationConfigHosts = "${sys:catalina.home}/conf/applications/locationConfigHosts.xml";
	private static final String locationConfigPlugins = "${sys:catalina.home}/conf/applications/locationConfigPlugins.xml";

	private static final ConcurrentHashMap<String, CustomConfiguration> serverCache = new ConcurrentHashMap<>();
	private static final Set<String> ipForwardCache = new HashSet<>();

	/**
	 * The type of environment
	 */
	public final static EnvironmentType environment;
	/**
	 * Whether some external cache management should take place.
	 */
	public final static boolean cacheOnIp;

	private final static CustomConfiguration serverConf;
	static {
		try {
			serverConf = ConfigurationFactory.createConfiguration("Server level", serverConfigXmlLocation);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Failed to load system config", e);
		}

		environment = serverConf.getEnum(EnvironmentType.class, "environment");
		cacheOnIp = environment == EnvironmentType.DEV || environment == EnvironmentType.LOCAL;
	}

	/**
	 * Get the default configuration for the current host, the default is managed per thread through {@link #storeDefaultParameters(ConfigurationParameters)} and {@link #clearDefaultParameters()}.
	 *
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration getHostConfiguration() throws ConfigurationException {
		return retrieveFromCacheOrCreateConfiguration("host_", null, locationConfigHosts);
	}
	/**
	 * Should the default configuration parameters not be set, return an empty configuration
	 * @see {@link #getHostConfiguration()}
	 * @return
	 */
	public static CustomConfiguration tryGetHostConfiguration() {
		try {
			return getHostConfiguration();
		} catch (ConfigurationException e) {
			Logger.warn(ConfigurationService.class, "Something went wrong trying to retrieve the default HostConfiguration, returning an empty configuration", e);
			return new EmptyConfiguration();
		}
	}
	/**
	 * The same as {@link #getHostConfiguration()} except it retrieves a Configuration for the specified <code>pluginName</code>
	 * @param pluginName
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration getPluginConfiguration(String pluginName) throws ConfigurationException, ConfigurationNotFoundException {
		return retrieveFromCacheOrCreateConfiguration("plugin_" + pluginName + '_', pluginName, locationConfigPlugins);
	}
	/**
	 * Should the default configuration parameters not be set, return an empty configuration
	 * @see {@link #getPluginConfiguration(pluginName)}
	 * @return
	 */
	public static CustomConfiguration tryGetPluginConfiguration(String pluginName) {
		try {
			return getPluginConfiguration(pluginName);
		} catch (ConfigurationException e) {
			Logger.warn(ConfigurationService.class, "Something went wrong trying to retrieve the default PluginConfiguration for: " + pluginName + ", returning an empty configuration", e);
			return new EmptyConfiguration();
		}
	}
	/**
	 * Deals with getting the correct host specific parameters.
	 * @param pluginName
	 * @param configurationLocation
	 * @return
	 * @throws ConfigurationException
	 */
	private static CustomConfiguration retrieveFromCacheOrCreateConfiguration(String keyPrefix, String pluginName, String configurationLocation) throws ConfigurationException, ConfigurationNotFoundException {
		ConfigurationParameters params = defaultParameters.get();
		if (params != null) {
			return retrieveFromCacheOrCreateConfiguration(keyPrefix, params.getHostName(), params.getIpAddress(), params.getSessionId(), pluginName, configurationLocation);
		} else {
			Logger.warn(ConfigurationService.class, "The default configuration has not been set, therefore you cannot use this method. KeyPrefix=" + keyPrefix + " and plugin=" + pluginName);
			return retrieveFromCacheOrCreateConfiguration(keyPrefix, null, null, null, pluginName, configurationLocation);
		}
	}

	/**
	 * Get a configuration for a specific host
	 *
	 * @param hostName
	 * @param ipAddress
	 * @param sessionId
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration getHostConfiguration(String hostName, String ipAddress, String sessionId) throws ConfigurationException {
		return retrieveFromCacheOrCreateConfiguration("host_", hostName, ipAddress, sessionId, null, locationConfigHosts);
	}
	/**
	 * Get a configuration for a specific plugin
	 *
	 * @param pluginName
	 * @param hostName
	 * @param ipAddress
	 * @param sessionId
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration getPluginConfiguration(String pluginName, String hostName, String ipAddress, String sessionId) throws ConfigurationException {
		return retrieveFromCacheOrCreateConfiguration("plugin_" + pluginName + '_', hostName, ipAddress, sessionId, pluginName, locationConfigPlugins);
	}
	/**
	 * This method deals with constructing a key for the specified configuration, checking the cache and if it isn't in the cache constructing (and caching) it.
	 *
	 * @param hostName
	 * @param ipAddress
	 * @param sessionId
	 * @param pluginName
	 * @param configurationLocation
	 * @return
	 * @throws ConfigurationException
	 */
	private static CustomConfiguration retrieveFromCacheOrCreateConfiguration(String keyPrefix, String hostName, String ipAddress, String sessionId, String pluginName, String configurationLocation) throws ConfigurationException {

		if (ConfigurationDotCMSCacheGroupHandler.isFlushed()) {
			clearCache();
			ConfigurationDotCMSCacheGroupHandler.setLoaded();
		}

		String key;
		final String basicKey = keyPrefix + hostName;

		// On Windows the : character is not allowed, replace it with _
		if (ipAddress != null) {
			ipAddress = ipAddress.replace(':', '_');
		}

		if (cacheOnIp) {
			String ipKey = basicKey + '_' + ipAddress + '_' + sessionId;
			if (!ipForwardCache.contains(ipKey)) {
				key = ipKey;
			} else {
				key = basicKey;
			}
		} else {
			key = basicKey;
		}

		Logger.debug(ConfigurationService.class, "Looking up configuration under key: " + key);
		CustomConfiguration conf = serverCache.get(key);

		if (conf != null) {
			return conf;
		}

		Logger.debug(ConfigurationService.class, "Loading configuration for key: " + key);

		Map<String, String> interpolationValues = new HashMap<>();
		final Bundle bundle;

		interpolationValues.put("hostName", hostName);
		if (pluginName != null && pluginName.startsWith("osgi/")) {
			//This is an OSGi plugin
			String[] splitPluginName = pluginName.split("/");
			interpolationValues.put("pluginName", splitPluginName[1]);

			if (splitPluginName.length > 2) {
				long bundleId = Long.valueOf(splitPluginName[2]);
				bundle = FrameworkUtil.getBundle(ConfigurationService.class).getBundleContext().getBundle(bundleId);
			} else {
				// If we didn't get a bundleId we need to search for it, somewhat slower, but the result is cached
				bundle = searchBundles(splitPluginName[1]);
			}
		} else {
			if (pluginName != null) {
				interpolationValues.put("pluginName", pluginName);
			}
			bundle = null;
		}

		// We only do ip specific things on DEV (and local of course!)
		if (cacheOnIp && ipAddress != null) {
			interpolationValues.put("ClientIPAddress", ipAddress);
		}

		Map<String, StrLookup> interpolators = new HashMap<>();
		interpolators.put("param", StrLookup.mapLookup(interpolationValues));

		DotCMSFileConfigurationProvider dotCmsProvider = new DotCMSFileConfigurationProvider(hostName);
		Map<String, ConfigurationProvider> providers = new HashMap<>();
		providers.put("dotcms", dotCmsProvider);

		OSGiFileConfigurationProvider osgiProvider = new OSGiFileConfigurationProvider(bundle);
		providers.put("osgi", osgiProvider);

		conf = ConfigurationFactory.createConfiguration(key, configurationLocation, interpolators, providers);

		boolean isIpSpecific = false;
		if (cacheOnIp && ipAddress != null) {
			for (FileConfiguration fileConf : conf.getLoadedFileConfigurations()) {
				if (fileConf.getFileName() != null && fileConf.getFileName().contains(ipAddress)) {

					isIpSpecific = true;

					return conf;
				}
			}
		}
		if (isIpSpecific) {
			ConfigurationParameters params = defaultParameters.get();
			if (params != null) {
				params.addConfigurationToSession(key);
			}
		}
		if (cacheOnIp && !isIpSpecific) {
			ipForwardCache.add(key);
			if (serverCache.contains(basicKey)) {
				return serverCache.get(basicKey);
			} else {
				key = basicKey;
			}
		}
		Logger.debug(ConfigurationService.class, "Created a new configuration! " + key);

		serverCache.put(key, conf);

		return conf;
	}
	private static Bundle searchBundles(String pluginName) {
		for (Bundle bundle : FrameworkUtil.getBundle(ConfigurationService.class).getBundleContext().getBundles()) {
			if (pluginName.equals(bundle.getHeaders().get("Bundle-Name"))) {
				return bundle;
			}
		}
		return null;
	}

	/**
	 * This is, effectively, a tuple, being able to return the required parameters needed to construct a key.
	 * It also contains a hook to whatever system is outside of this class to register a configuration object (or at least it's key) so that the outside system can clean up when it's no-longer needed.
	 *
	 * @author maarten
	 *
	 */
	public interface ConfigurationParameters {
		String getHostName();
		String getSessionId();
		String getIpAddress();
		void addConfigurationToSession(String key);
	}

	private static final ThreadLocal<ConfigurationParameters> defaultParameters = new ThreadLocal<>();

	/**
	 * The hook for the outside system to deposit the default parameters
	 * @param params
	 */
	public static final void storeDefaultParameters(ConfigurationParameters params) {
		defaultParameters.set(params);
	}
	/**
	 * The hook for the outside system to notify {@link ConfigurationService} that the stored parameters are no longer needed
	 */
	public static final void clearDefaultParameters() {
		defaultParameters.remove();
	}
	/**
	 * Method to be used by {@link RequestStoringListener} to stack the parameters in nested requests
	 */
	public static final ConfigurationParameters getDefaultParameters() {
		return defaultParameters.get();
	}
	/**
	 * This will invalidate a specifically cached configuration.
	 * @param key
	 */
	public static final void removeConfigurationForKey(String key) {
		Logger.debug(ConfigurationService.class, "Removing configuration with key: " + key);
		serverCache.remove(key);
	}
	/**
	 * This will invalidate the entire cache.
	 */
	public static final void clearCache() {
		serverCache.clear();
		ipForwardCache.clear();
	}

	/**
	 * This determines the number of loaded configuration variants.
	 *
	 * @return
	 */
	public static int getNumberOfLoadedConfigurations() {
		return serverCache.size();
	}
}

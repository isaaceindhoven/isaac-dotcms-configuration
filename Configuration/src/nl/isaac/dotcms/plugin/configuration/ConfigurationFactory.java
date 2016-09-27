package nl.isaac.dotcms.plugin.configuration;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.Collections;
import java.util.Map;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.comp.configuration.CustomConfigurationBuilder;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.DefaultConfigurationBuilder.ConfigurationProvider;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.FileConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.PropertyConverter;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.lang.text.StrLookup;

import com.dotmarketing.util.Logger;

/**
 * This class contains helper methods for creating {@link CustomConfiguration} objects with properties as specified for the ISAAC Configuration plugin.
 * @author maarten
 *
 */
public class ConfigurationFactory {
	/**
	 * Create a configuration via an id (<code>key</code>, only used for logging), the files to load configuration (<code>main</code>), the various property providers (<code>propertyProviders</code>) and configuration providers (<code>configurationProviders</code>).
	 * @param key
	 * @param main
	 * @param propertyProviders
	 * @param configurationProviders
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration createConfiguration(String key, String main, Map<String, StrLookup> propertyProviders, Map<String, ConfigurationProvider> configurationProviders) throws ConfigurationException {
		CustomConfigurationBuilder confBuilder = new CustomConfigurationBuilder();
		confBuilder.setAutoSave(false);

		for (Map.Entry<String, ConfigurationProvider> provider : configurationProviders.entrySet()) {
			confBuilder.addConfigurationProvider(provider.getKey(), provider.getValue());
		}

		for (Map.Entry<String, StrLookup> provider : propertyProviders.entrySet()) {
			confBuilder.getInterpolator().registerLookup(provider.getKey(), provider.getValue());
		}

		main = String.valueOf(PropertyConverter.interpolate(main, confBuilder));
		confBuilder.load(main);

		CustomConfiguration conf = new CustomConfiguration(confBuilder.getConfiguration(false));
		for (FileConfiguration f : conf.getLoadedFileConfigurations()) {
			Logger.debug(ConfigurationService.class, "Loaded configuration file: " + f.getFile() + " for: " + key);
		}
		Logger.debug(ConfigurationService.class, "Loaded " + key + "configuration: " + conf.toStringTree());

		return conf;
	}
	/**
	 * Same as {@link #createConfiguration(String, String, Map, Map)} except it filles in empty maps for the two maps.
	 * @param key
	 * @param main
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration createConfiguration(String key, String main) throws ConfigurationException {
		return createConfiguration(key, main, Collections.<String, StrLookup>emptyMap(), Collections.<String, ConfigurationProvider>emptyMap());
	}
}

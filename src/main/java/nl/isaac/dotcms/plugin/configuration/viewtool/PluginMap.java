package nl.isaac.dotcms.plugin.configuration.viewtool;

import java.util.Map;

import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.util.BasicMap;

/**
 * Provides the [viewtool].plugins.[pluginname] and [viewtool].plugins.osgi.[pluginname] functionality
 * @author Maarten
 *
 */
public enum PluginMap {
	// We can't implement the Map interface, because the static values method of a enum hides the values() method a Map
	PLUGIN(""),
	OSGI_PLUGIN("osgi/");

	private final BasicMap<String, Object> impl;

	private PluginMap(final String keyPrefix) {
		impl = new BasicMap<String, Object>() {
			@Override public Object get(Object key) {
				if ("osgi".equals(key)) {
					return OSGI_PLUGIN.getMap();
				}

				final String pluginName = String.valueOf(key);
				final ConfigurationMap conf;
				try {
					conf = new ConfigurationMap(ConfigurationService.getPluginConfiguration(keyPrefix + pluginName));
				} catch (ConfigurationException e) {
					throw new RuntimeException("Failed to retrieve the configuration for plugin: " + keyPrefix + pluginName);
				}
				return conf;
			}
		};
	}

	public Map<String, Object> getMap() {
		return impl;
	}
}

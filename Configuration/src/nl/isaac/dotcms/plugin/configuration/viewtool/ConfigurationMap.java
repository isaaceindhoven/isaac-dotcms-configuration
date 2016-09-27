package nl.isaac.dotcms.plugin.configuration.viewtool;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.Iterator;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.dotcms.plugin.configuration.util.BasicMap;

import com.dotmarketing.util.Logger;

/**
 * A {@link java.util.Map} like wrapper for a {@link CustomConfiguration}, the {@link #get(Object)} will either return the String value of a specific key, or if no such key exists, it will attempt to match some prefix and return a new {@link ConfigurationMap} around that.
 * For more information see {@link #get(Object)}.
 *
 * @author maarten
 *
 */
public class ConfigurationMap extends BasicMap<String, Object> {

	private final CustomConfiguration conf;
	private final String prefix;
	public ConfigurationMap(CustomConfiguration conf) {
		this(conf, "");
	}
	private ConfigurationMap(CustomConfiguration conf, String prefix) {
		this.conf = conf;
		this.prefix = prefix;
	}

	/**
	 * This method will return either the String value of a specific key (the {@link #prefix} + the <code>key</code> parameter), or if that does not exist, then it will attempt to match a specific group of keys.
	 * If there is a configuration with the following keys:
	 * abc.def
	 * abc.ghi
	 * Then calling {@link #get(Object)} with 'abc.def' will return the {@link CustomConfiguration#getString(String)} value of that key.
	 * If it is called with 'abc' then it will return a new {@link ConfigurationMap} which will respond to the keys 'def' and 'ghi'.
	 * Calling this with any other key will result in <code>null</code>.
	 *
	 * @param key
	 */
	@Override
	public Object get(Object key) {
		final String prefixKey = prefix + key;
		if (conf.containsKey(prefixKey)) {
			return conf.getString(prefixKey);
		} else {
			Iterator<?> iter = conf.getKeys(prefixKey);
			if (iter.hasNext()) {
				return new ConfigurationMap(conf, prefixKey + '.');
			} else {
				Logger.debug(this.getClass(), prefixKey + " was not found as either a property or a prefix in the configuration.");
				return null;
			}
		}
	}

}

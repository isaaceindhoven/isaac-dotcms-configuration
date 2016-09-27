package com.dotmarketing.plugin.business;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.exception.ConfigurationNotFoundException;

import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

public class PluginAPIImpl extends PluginAPIImplDotCMS {
	@Override
	public String loadProperty(String pluginId, String key)
			throws DotDataException {
		try {
			CustomConfiguration cc = ConfigurationService.getPluginConfiguration(pluginId);
			if(cc != null && cc.containsKey(key) && UtilMethods.isSet(cc.getString(key))) {
				return cc.getString(key);
			}
		} catch (ConfigurationException e) {
			Logger.warn(this.getClass(), "Exception while trying to retrieve config with key '" + key + "' for plugin '" + pluginId + "' in ConfigurationService", e);
		} catch (NoSuchElementException e) {
			Logger.debug(this.getClass(), "The key '" + key + "' can't be found. It's probably not set yet in a configuration file");
		} catch (ConfigurationNotFoundException e) {
			Logger.error(this.getClass(), "Can't load configuration property '" + key + "'", e);
		}
		return super.loadProperty(pluginId, key);
	}
	@Override
	public List<String> loadPluginConfigKeys(String pluginId) throws DotDataException {
		try {
			CustomConfiguration cc = ConfigurationService.getPluginConfiguration(pluginId);
			@SuppressWarnings("unchecked")
			Iterator<String> iter = cc.getKeys();
			List<String> keys = new ArrayList<String>();
			while (iter.hasNext()) {
				keys.add(iter.next());
			}
			return keys;
		} catch (ConfigurationException e) {
			Logger.warn(this.getClass(), "Exception while trying to retrieve config keys for plugin '" + pluginId + "' in ConfigurationService", e);
		} catch (NoSuchElementException e) {
			Logger.debug(this.getClass(), "The keys couldn't be found. There probably is no configuration file");
		} catch (ConfigurationNotFoundException e) {
			Logger.error(this.getClass(), "Can't load configuration property", e);
		}
		return super.loadPluginConfigKeys(pluginId);
	}
}

package nl.isaac.dotcms.plugin.configuration;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import nl.isaac.dotcms.plugin.configuration.shared.CacheGroupHandler;

import com.dotmarketing.util.Logger;

public enum ConfigurationDotCMSCacheGroupHandler {;

	private static final CacheGroupHandler<String> cache = new CacheGroupHandler<String>("Configuration", String.class);
	
	public static boolean isFlushed() {
		return cache.get("trigger") == null;
	}

	public static void setLoaded() {
		cache.put("trigger", "cacheIsValid");
	}
	
	public static void clearCache() {
		Logger.debug(ConfigurationDotCMSCacheGroupHandler.class, "Clearing cache");
		cache.flush();
	}
}

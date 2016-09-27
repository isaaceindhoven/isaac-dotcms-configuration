package nl.isaac.dotcms.plugin.configuration.shared;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;

/**
 * Basic class that handles the dotCMS cache. You only need a (unique) name
 * that is used in the cache, and an itemhandler that can retrieve items that
 * need to be stored. 
 * 
 * @author Xander
 *
 * @param <T> the type of the objects to store in the cache
 */
public class CacheGroupHandler<T> {
	private String groupName;
	protected Class<T> clazz;

	public CacheGroupHandler(String groupName, Class<T> clazz) {
		this.groupName = groupName;
		this.clazz = clazz;
	}

	public T get(String key) {
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		Object o = null;
		try {
			o = cache.get(key, groupName);
		} catch (DotCacheException e) {
			Logger.info(this.getClass(), String.format("DotCacheException for Group '%s', key '%s', message: %s", groupName, key, e.getMessage()));
		}
		
		if(o == null) {
			return null;
		} else {
			return clazz.cast(o);
		}
	}
	
	public void put(String key, T t) {
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.put(key, t, groupName);
	}

	public void remove(String key) {
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.remove(key, groupName);
	}

	public void flush() {
		DotCacheAdministrator cache = CacheLocator.getCacheAdministrator();
		cache.flushGroup(groupName);
	}
}

package nl.isaac.comp.configuration.interpolator;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.lang.text.StrLookup;

/**
 * Lookup implementation for environment settings using the "env:" prefix
 * @author jan-willem
 *
 */
public class EnvironmentLookup extends StrLookup {

	@Override
	public String lookup(String key) {
		return System.getenv(key);
	}

}

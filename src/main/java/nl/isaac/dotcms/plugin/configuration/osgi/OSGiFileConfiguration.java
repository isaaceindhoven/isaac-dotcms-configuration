package nl.isaac.dotcms.plugin.configuration.osgi;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.dotcms.repackage.org.osgi.framework.Bundle;

/**
 * This changes the default {@link XMLConfiguration#load()} to use DotCMS APIs to load a file managed by DotCMS.
 * @author maarten
 *
 */
public class OSGiFileConfiguration extends PropertiesConfiguration {
	private Bundle bundle;
	
	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	@Override
	public void load() throws ConfigurationException {
		// And here we must read out dotCMS.
		final String fileName = getFileName();
		if(bundle != null) {
			URL url = bundle.getResource(fileName);
			load(url);
		}
	}
}

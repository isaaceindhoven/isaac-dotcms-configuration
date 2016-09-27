package nl.isaac.comp.configuration;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.AbstractConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.CombinedConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.FileConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.lang.exception.ExceptionUtils;

import org.xml.sax.SAXParseException;

import com.dotmarketing.util.Logger;

/**
 * A factory class with useful methods to load configurations
 * @author jan-willem
 *
 */
public class ConfigurationFactory {

	private ConfigurationFactory() {
	}
	
	/**
	 * Convenient method for initializing a {@link CustomConfiguration} Object.<br />
	 * Usage example:<br />
	 * <pre>
	 *    CustomConfiguration config = ConfigurationFactory.newConfiguration(MyConfigService.class.getClassLoader(), "META-INF/config.xml");
	 *    return new MyConfigPojo(config);
	 * </pre>
	 * In addition, each additional xml configuration location is parsed and added to the resulting configuration as well. 
	 * @param cl, the classLoader where config.xml is found
	 * @param configXmlLocation
	 * @param additionalXmlLocations
	 * @return
	 * @throws ConfigurationException
	 */
	public static CustomConfiguration newConfiguration(ClassLoader cl, String configXmlLocation, String... additionalXmlLocations) throws ConfigurationException {
		
		try {

			CustomConfigurationBuilder configurationBuilder = new CustomConfigurationBuilder(cl, configXmlLocation);
			CombinedConfiguration config = configurationBuilder.getConfiguration(true);
	
			if (additionalXmlLocations != null && additionalXmlLocations.length > 0) {
				
				// this is a reference to the latest configuration loaded
				// by adding the loaded configuration to the new configuration, previous loaded variables
				// can be used in the new configuration and in config.xml's
				AbstractConfiguration parentConfig = (AbstractConfiguration) config.clone();
				
				for (String additionalXmlLocation : additionalXmlLocations) {
					
					CustomConfigurationBuilder additionalConfigurationBuilder = new CustomConfigurationBuilder(cl, additionalXmlLocation);
					
					CombinedConfiguration additionalConfig = additionalConfigurationBuilder.getConfiguration(true, parentConfig);
	
					config.addConfiguration(additionalConfig);
					
					parentConfig = (AbstractConfiguration) additionalConfig.clone();
				}
			}
		
			// wrap it with a helper that contains convenient methods
			return new CustomConfiguration(config);
			
		} catch (ConfigurationException e) {
			
			Throwable rootCause = ExceptionUtils.getRootCause(e);

			if (rootCause instanceof SAXParseException) {
				
				/*
				 * When an SAXParseException is thrown, create a new Exception that gives all known problems in one message
				 * instead of several exceptions
				 */
				Throwable cause2 = null;

				int indexOfCause1 = ExceptionUtils.indexOfThrowable(e, SAXParseException.class);
				if (indexOfCause1 > 0) {
					int indexOfCause2 = ExceptionUtils.indexOfThrowable(e, ConfigurationException.class, indexOfCause1 - 1);
					if (indexOfCause2 >= 0) {
						cause2 = ExceptionUtils.getThrowables(e)[indexOfCause2];
					}
				}
				
				SAXParseException spe = (SAXParseException) rootCause;
				String message = "Error parsing configuration, line=" + spe.getLineNumber() + ", column=" + spe.getColumnNumber();
				if (cause2 != null) {
					message = cause2.getMessage() + ". " + message;
				}
				throw new ConfigurationException(message + ". " + rootCause.getMessage(), e.getCause());
			}
			throw e;
		}
	}
	
	public static CustomConfiguration newConfiguration(ClassLoader cl, boolean logConfigurationDetails, String configXmlLocation, String... additionalXmlLocations) throws ConfigurationException {
		
		CustomConfiguration result = newConfiguration(cl, configXmlLocation, additionalXmlLocations);
		logConfigurationDetails(result);
		
		return result;
	}
	
	protected static void logConfigurationDetails(CustomConfiguration configuration) {
		
		StringBuffer configFiles = new StringBuffer();
		for (FileConfiguration cf : configuration.getLoadedFileConfigurations()) {
			if (cf.getFile() != null) {
				configFiles.append("\n\t").append(cf.getFile());
			}
		}

		Logger.debug(ConfigurationFactory.class, "Used configuration files:" + configFiles.toString());
		Logger.debug(ConfigurationFactory.class, "Configuration result:\n" + configuration.toStringTree(true));
	}
}

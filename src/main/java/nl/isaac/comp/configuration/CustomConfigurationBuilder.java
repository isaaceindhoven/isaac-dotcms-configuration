package nl.isaac.comp.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.apache.commons.configuration.event.ConfigurationErrorListener;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.logging.LogFactory;

import com.dotmarketing.util.Logger;

import nl.isaac.comp.configuration.interpolator.EnvironmentLookup;

/**
 * Wrapper for DefaultConfigurationBuilder for the Apache Commons Configuration framework. The only reason is to
 * disable the default error listeners 
 * @author jan-willem
 *
 */
@SuppressWarnings("serial")
public class CustomConfigurationBuilder extends DefaultConfigurationBuilder {
	
    private static final ConfigurationProvider INLINE_ROPERTIES_PROVIDER = new InlinePropertiesConfigurationProvider();

	/**
	 * There is a problem when redeploying jboss applications and loading configurations from a jar file.
	 * The work around this problem we use this flag to prevent apache commons configuration from loading url's
	 * on calling {@link #getConfiguration(true)}
	 */
	public boolean loadedOnInstantiation = false;
	
	/**
	 * The parent configuration to use while loading config.xml. When set, variables from this configuration
	 * can be used in config.xml
	 */
	private AbstractConfiguration parentConfigurationWhileParsing;
	
	static {
		ConfigurationInterpolator.registerGlobalLookup("env", new EnvironmentLookup());
	}
	
	public CustomConfigurationBuilder() {
		
		super();
		
		// set the logger of the class we are extending to it's original value
		// this to prevent non interesting debug logging from apache-commons-configuration
		setLogger(LogFactory.getLog(DefaultConfigurationBuilder.class));
		
		// Remove the default logger, it prints an exception when an optional log does not exist.
		// Add a customer error listener if you want to catch messages
		super.clearErrorListeners();
		
		super.addErrorListener(new ConfigurationErrorListener() {
			public void configurationError(ConfigurationErrorEvent event) {
				String message = "" + event.getType() + " : ";
				if (event.getCause() != null) {
					message += event.getCause().getMessage();
				}
				if (event.getType() == DefaultConfigurationBuilder.EVENT_ERR_LOAD_OPTIONAL) {
					// make the log message more friendly
					message = message.replace("Cannot locate configuration source", "Looking for optional configuration file:");
					Logger.debug(this,"LOAD_OPTIONAL: " + message);
				} else {
					Logger.info(this,message);
				}
			}
		});

		super.addConfigurationProvider("inlineproperties", INLINE_ROPERTIES_PROVIDER);
	}
	
    /**
     * @see DefaultConfigurationBuilder#DefaultConfigurationBuilder(URL)
     */
	public CustomConfigurationBuilder(URL url) throws ConfigurationException {
		this();
		setURL(url);
    }
    
	/**
	 * Load's the configuration from the specified resource
	 *  
	 * @param cl, the classloader which is able to find the specified resource
	 * @param resource, the resource to load
	 * @throws ConfigurationException, @see {@link DefaultConfigurationBuilder#getConfiguration(boolean)}
	 */
    public CustomConfigurationBuilder(ClassLoader cl, String resource) throws ConfigurationException {
        this();
        
        InputStream is = cl.getResourceAsStream(resource);
        
        if (is == null) throw new ConfigurationException("Unable to locate resource '" + resource + "'");

        try {
	        try {
	        	load(is);

	        	loadedOnInstantiation = true;
	        	
	        } finally {
				is.close();
	        }
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}
    }
    
    /**
     * Creates the resulting combined configuration. This method is called by
     * <code>getConfiguration()</code>. It checks whether the
     * <code>header</code> section of the configuration definition file
     * contains a <code>result</code> element. If this is the case, it will be
     * used to initialize the properties of the newly created configuration
     * object.
     *
     * @return the resulting configuration object
     * @throws ConfigurationException if an error occurs
     */
    @Override
    protected CombinedConfiguration createResultConfiguration() throws ConfigurationException {
    	
    	CombinedConfiguration result = super.createResultConfiguration();

    	if (parentConfigurationWhileParsing != null) {
    		result.addConfiguration(parentConfigurationWhileParsing);
    	}
    	
    	return result;
    }

    @Override
    public CombinedConfiguration getConfiguration(boolean load) throws ConfigurationException {
    	
    	return getConfiguration(load, null);
    }
    
    /**
     * @see #getConfiguration(boolean)
     * @param load
     * @param parentConfigurationWhileParsing the configuration to use while processing config.xml. The configuration is not part of the actual configuration when loaded
     * @return
     * @throws ConfigurationException
     */
    public CombinedConfiguration getConfiguration(boolean load, AbstractConfiguration parentConfigurationWhileParsing) throws ConfigurationException {
    	
    	if (load && loadedOnInstantiation) {
    		load = false;
    	}
    	
    	if (parentConfigurationWhileParsing != null) {
    		
	    	// this is used in createResultConfiguration();
	    	this.parentConfigurationWhileParsing = parentConfigurationWhileParsing;
    	}
    	
    	CombinedConfiguration combinedConfiguration = super.getConfiguration(load);

    	if (parentConfigurationWhileParsing != null) {
    		
    		// and remove the configuration again since we only used it for parsing
    		combinedConfiguration.removeConfiguration(parentConfigurationWhileParsing);
    	}
    	
    	return combinedConfiguration;
    }
    
    /**
     * @see #getConfiguration(boolean, AbstractConfiguration)
     * @param parentConfigurationWhileParsing the configuration to use while processing config.xml. The configuration is not part of the actual configuration when loaded
     * @return
     * @throws ConfigurationException
     */
    public Configuration getConfiguration(AbstractConfiguration parentConfigurationWhileParsing) throws ConfigurationException {
    	return getConfiguration(true, parentConfigurationWhileParsing);
    }
}

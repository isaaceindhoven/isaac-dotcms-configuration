package nl.isaac.comp.configuration;

import java.util.List;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.AbstractConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.DefaultConfigurationBuilder.ConfigurationDeclaration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.DefaultConfigurationBuilder.ConfigurationProvider;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.PropertiesConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.ConfigurationNode;



/**
 * A specialized provider implementation that deals with inline property
 * configurations.
 */
public class InlinePropertiesConfigurationProvider extends ConfigurationProvider {

	/**
	 * Creates a new instance of <code>InlineConfigurationProvider</code>.
	 */
	public InlinePropertiesConfigurationProvider() {
		super(PropertiesConfiguration.class);
	}

    /**
     * Creates the configuration. After that <code>load()</code> will be
     * called. If this configuration is marked as optional, exceptions will
     * be ignored.
     *
     * @param decl the declaration
     * @return the new configuration
     * @throws Exception if an error occurs
     */
	public AbstractConfiguration getConfiguration(ConfigurationDeclaration decl) throws Exception {
    	
    	PropertiesConfiguration config = new PropertiesConfiguration();

		for (Object o : decl.getNode().getChildren()) {
			
			ConfigurationNode node = (ConfigurationNode) o;
			
			if (node.getName().equals("property")) {

				List<?> keys = node.getAttributes("key");
				
				if (keys.size() != 1) throw new RuntimeException(InlinePropertiesConfigurationProvider.class.getSimpleName() + ": missing 'key' attribute");
				String key = (String) ((ConfigurationNode) keys.get(0)).getValue();
				Object value = node.getValue();
				
				if (value != null) {
					config.addProperty(key, value);
				}
			}
		}
		
		return config;
    }

}


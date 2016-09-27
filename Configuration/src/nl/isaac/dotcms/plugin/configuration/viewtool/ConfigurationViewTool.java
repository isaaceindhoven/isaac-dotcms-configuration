package nl.isaac.dotcms.plugin.configuration.viewtool;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.isaac.comp.configuration.CustomConfiguration;
import nl.isaac.dotcms.plugin.configuration.ConfigurationService;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.filter.RequestStoringFilter;
import nl.isaac.dotcms.plugin.configuration.util.BasicMap;

import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.util.Logger;

/**
 * This view tool will always use {@link ConfigurationService#getHostConfiguration()} for host configuration and {@link ConfigurationService#getPluginConfiguration(String)} for plugin configurations.
 * So ensure that something like {@link RequestStoringFilter} is deployed!
 *
 * @author maarten
 *
 */
public class ConfigurationViewTool extends BasicMap<Object, Object> implements ViewTool {

	private CustomConfiguration hostConf;
	private ConfigurationMap hostConfMap;

	/**
	 * This constructor can only be called if the {@link #init(Object)} is called later, only used by DotCMS.
	 */
	public ConfigurationViewTool() {
		// This will be filled in by #init(Object)
	}
	/**
	 * This constructor can be called by third party code (not DotCMS) for use in Velocity.
	 * @param hostConf
	 */
	public ConfigurationViewTool(CustomConfiguration hostConf) {
		this.hostConf = hostConf;
		this.hostConfMap = new ConfigurationMap(hostConf);
	}

	@Override
	public void init(Object req) {
		if (req instanceof ViewContext && ((ViewContext) req).getRequest() == null) {
			Logger.info(getClass(), "Application start for the ConfigurationViewTool, ignored.");
			// Ignoring server context
			return;
		}
		if (!(req instanceof HttpServletRequest) && !(req instanceof ViewContext)) {
			throw new IllegalArgumentException("Expected a HttpServletRequest, got: " + (req == null ? "<null>" : req.getClass()));
		}

		try {
			hostConf = ConfigurationService.getHostConfiguration();
			hostConfMap = new ConfigurationMap(hostConf);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Failed to load the configuration!", e);
		}

	}

	/**
	 * A method to retrieve the configuration object for a specific plugin based on the default host configuration
	 *
	 * @param pluginName
	 * @return
	 * @throws ConfigurationException
	 */
	public CustomConfiguration getPluginConfiguration(String pluginName) throws ConfigurationException {
		return ConfigurationService.getPluginConfiguration(pluginName);
	}
	/**
	 * A method to retrieve the configuration object for the current, default, host. (Default for this request! Not the default DotCMS Host!)
	 * @return
	 * @throws ConfigurationException
	 */
	public CustomConfiguration getHostConfiguration() throws ConfigurationException {
		return hostConf;
	}


	/**
	 * See {@link ConfigurationMap#get(Object)} for the general details.
	 * One aspect that is different from the parent class implementation is that the key 'plugins' will return a special {@link BasicMap} that returns {@link ConfigurationMap}s based on a plugin configuration.
	 * This enables the syntax of <code>$configuration.plugins.<plugin name>.pluginProperty</code>
	 * If the key is not 'plugins', then the {@link #hostConfMap} is consulted.
	 *
	 * @param key
	 */
	@Override
	public Object get(Object oKey) {
		String key = String.valueOf(oKey);
		if (key.equals("plugins")) {
			return new BasicMap<String, ConfigurationMap>() {
				private final Map<String, ConfigurationMap> cache = new HashMap<String, ConfigurationMap>();
				@Override public ConfigurationMap get(Object key) {
					if (cache.containsKey(key)) {
						return cache.get(key);
					}
					final String pluginName = String.valueOf(key);
					final ConfigurationMap conf;
					try {
						conf = new ConfigurationMap(getPluginConfiguration(pluginName));
					} catch (ConfigurationException e) {
						throw new RuntimeException("Failed to retrieve the configuration for plugin: " + pluginName);
					}
					cache.put(pluginName, conf);
					return conf;
				}
			};
		}
		return hostConfMap.get(key);
	}

	// The remainder of this class are delegate methods for easy access to the host configuration!
	/**
	 * @see CustomConfiguration#containsKey(String)
	 */
	public boolean containsKey(String key) {
		return hostConf.containsKey(key);
	}
	/**
	 * @see CustomConfiguration#getKeys()
	 */
	public Iterator<?> getKeys() {
		return hostConf.getKeys();
	}
	/**
	 * @see CustomConfiguration#getKeys(String)
	 */
	public Iterator<?> getKeys(String prefix) {
		return hostConf.getKeys(prefix);
	}
	/**
	 * @see CustomConfiguration#getBoolean(String)
	 */
	public boolean getBoolean(String key) {
		return hostConf.getBoolean(key);
	}
	/**
	 * @see CustomConfiguration#getBoolean(String, boolean)
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return hostConf.getBoolean(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getBoolean(String, Boolean)
	 */
	public Boolean getBoolean(String key, Boolean defaultValue) {
		return hostConf.getBoolean(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getByte(String)
	 */
	public byte getByte(String key) {
		return hostConf.getByte(key);
	}
	/**
	 * @see CustomConfiguration#getByte(String, byte)
	 */
	public byte getByte(String key, byte defaultValue) {
		return hostConf.getByte(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getByte(String, Byte)
	 */
	public Byte getByte(String key, Byte defaultValue) {
		return hostConf.getByte(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getDouble(String)
	 */
	public double getDouble(String key) {
		return hostConf.getDouble(key);
	}
	/**
	 * @see CustomConfiguration#getDouble(String, double)
	 */
	public double getDouble(String key, double defaultValue) {
		return hostConf.getDouble(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getDouble(String, Double)
	 */
	public Double getDouble(String key, Double defaultValue) {
		return hostConf.getDouble(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getEnum(Class, String)
	 */
	public <T extends Enum<T>> T getEnum(Class<T> enumType, String key) {
		return hostConf.getEnum(enumType, key);
	}
	/**
	 * @see CustomConfiguration#getEnum(Class, String, Enum)
	 */
	public <T extends Enum<T>> T getEnum(Class<T> enumType, String key, T defaultValue) {
		return hostConf.getEnum(enumType, key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getFloat(String)
	 */
	public float getFloat(String key) {
		return hostConf.getFloat(key);
	}
	/**
	 * @see CustomConfiguration#getFloat(String, float)
	 */
	public float getFloat(String key, float defaultValue) {
		return hostConf.getFloat(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getFloat(String, Float)
	 */
	public Float getFloat(String key, Float defaultValue) {
		return hostConf.getFloat(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getInt(String)
	 */
	public int getInt(String key) {
		return hostConf.getInt(key);
	}
	/**
	 * @see CustomConfiguration#getInt(String, int)
	 */
	public int getInt(String key, int defaultValue) {
		return hostConf.getInt(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getInteger(String, Integer)
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		return hostConf.getInteger(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getLong(String)
	 */
	public long getLong(String key) {
		return hostConf.getLong(key);
	}
	/**
	 * @see CustomConfiguration#getLong(String, long)
	 */
	public long getLong(String key, long defaultValue) {
		return hostConf.getLong(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getLong(String, Long)
	 */
	public Long getLong(String key, Long defaultValue) {
		return hostConf.getLong(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getShort(String)
	 */
	public short getShort(String key) {
		return hostConf.getShort(key);
	}

	/**
	 * @see CustomConfiguration#getShort(String, short)
	 */
	public short getShort(String key, short defaultValue) {
		return hostConf.getShort(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getShort(String, Short)
	 */
	public Short getShort(String key, Short defaultValue) {
		return hostConf.getShort(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getBigDecimal(String)
	 */
	public BigDecimal getBigDecimal(String key) {
		return hostConf.getBigDecimal(key);
	}
	/**
	 * @see CustomConfiguration#getBigDecimal(String, BigDecimal)
	 */
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return hostConf.getBigDecimal(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getBigInteger(String)
	 */
	public BigInteger getBigInteger(String key) {
		return hostConf.getBigInteger(key);
	}
	/**
	 * @see CustomConfiguration#getBigInteger(String, BigInteger)
	 */
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return hostConf.getBigInteger(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getString(String)
	 */
	public String getString(String key) {
		return hostConf.getString(key);
	}
	/**
	 * @see CustomConfiguration#getString(String, String)
	 */
	public String getString(String key, String defaultValue) {
		return hostConf.getString(key, defaultValue);
	}
	/**
	 * @see CustomConfiguration#getStringArray(String)
	 */
	public String[] getStringArray(String key) {
		return hostConf.getStringArray(key);
	}
	/**
	 * @see CustomConfiguration#getList(String)
	 */
	public List<?> getList(String key) {
		return hostConf.getList(key);
	}
	/**
	 * @see CustomConfiguration#getList(String, List)
	 */
	public List<?> getList(String key, List<?> defaultValue) {
		return hostConf.getList(key, defaultValue);
	}

}

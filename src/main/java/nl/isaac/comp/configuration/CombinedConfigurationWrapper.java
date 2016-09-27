package nl.isaac.comp.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.AbstractConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.CombinedConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.Configuration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.HierarchicalConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.HierarchicalConfiguration.Node;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.SubnodeConfiguration;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.event.ConfigurationErrorListener;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.event.ConfigurationEvent;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.event.ConfigurationListener;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.ConfigurationNode;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.ExpressionEngine;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.tree.NodeCombiner;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.lang.text.StrSubstitutor;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.logging.Log;


/**
 * A wrapper class for {@link CombinedConfiguration}
 * @author jan-willem
 *
 */
public class CombinedConfigurationWrapper implements Configuration {

	private final CombinedConfiguration configuration;
	
	public CombinedConfigurationWrapper(CombinedConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the delegated instance which was passed to {@link #HierarchicalConfigurationWrapper(HierarchicalConfiguration)}
	 * @return
	 */
	public CombinedConfiguration getDelegate() {
		return configuration;
	}

	public void addConfiguration(AbstractConfiguration config, String name,
			String at) {
		configuration.addConfiguration(config, name, at);
	}

	public void addConfiguration(AbstractConfiguration config, String name) {
		configuration.addConfiguration(config, name);
	}

	public void addConfiguration(AbstractConfiguration config) {
		configuration.addConfiguration(config);
	}

	public void addConfigurationListener(ConfigurationListener l) {
		configuration.addConfigurationListener(l);
	}

	public void addErrorListener(ConfigurationErrorListener l) {
		configuration.addErrorListener(l);
	}

	public void addErrorLogListener() {
		configuration.addErrorLogListener();
	}

	public void addNodes(String key, Collection nodes) {
		configuration.addNodes(key, nodes);
	}

	public void addProperty(String key, Object value) {
		configuration.addProperty(key, value);
	}

	public void append(Configuration c) {
		configuration.append(c);
	}

	public void clear() {
		configuration.clear();
	}

	public void clearConfigurationListeners() {
		configuration.clearConfigurationListeners();
	}

	public void clearErrorListeners() {
		configuration.clearErrorListeners();
	}

	public void clearProperty(String key) {
		configuration.clearProperty(key);
	}

	public void clearTree(String key) {
		configuration.clearTree(key);
	}

	public Object clone() {
		return configuration.clone();
	}

	public SubnodeConfiguration configurationAt(String key,
			boolean supportUpdates) {
		return configuration.configurationAt(key, supportUpdates);
	}

	public SubnodeConfiguration configurationAt(String key) {
		return configuration.configurationAt(key);
	}

	public void configurationChanged(ConfigurationEvent event) {
		configuration.configurationChanged(event);
	}

	public List configurationsAt(String key) {
		return configuration.configurationsAt(key);
	}

	public boolean containsKey(String key) {
		return configuration.containsKey(key);
	}

	public void copy(Configuration c) {
		configuration.copy(c);
	}

	public boolean equals(Object obj) {
		return configuration.equals(obj);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	public BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public byte getByte(String key) {
		return configuration.getByte(key);
	}

	public Configuration getConfiguration(int index) {
		return configuration.getConfiguration(index);
	}

	public Configuration getConfiguration(String name) {
		return configuration.getConfiguration(name);
	}

	public Collection getConfigurationListeners() {
		return configuration.getConfigurationListeners();
	}

	public Set getConfigurationNames() {
		return configuration.getConfigurationNames();
	}

	public ExpressionEngine getConversionExpressionEngine() {
		return configuration.getConversionExpressionEngine();
	}

	public double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public double getDouble(String key) {
		return configuration.getDouble(key);
	}

	public Collection getErrorListeners() {
		return configuration.getErrorListeners();
	}

	public ExpressionEngine getExpressionEngine() {
		return configuration.getExpressionEngine();
	}

	public float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public float getFloat(String key) {
		return configuration.getFloat(key);
	}

	public int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	public int getInt(String key) {
		return configuration.getInt(key);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return configuration.getInteger(key, defaultValue);
	}

	public ConfigurationInterpolator getInterpolator() {
		return configuration.getInterpolator();
	}

	public Iterator getKeys() {
		return configuration.getKeys();
	}

	public Iterator getKeys(String prefix) {
		return configuration.getKeys(prefix);
	}

	public List getList(String key, List defaultValue) {
		return configuration.getList(key, defaultValue);
	}

	public List getList(String key) {
		return configuration.getList(key);
	}

	public char getListDelimiter() {
		return configuration.getListDelimiter();
	}

	public Log getLogger() {
		return configuration.getLogger();
	}

	public long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public long getLong(String key) {
		return configuration.getLong(key);
	}

	public int getMaxIndex(String key) {
		return configuration.getMaxIndex(key);
	}

	public NodeCombiner getNodeCombiner() {
		return configuration.getNodeCombiner();
	}

	public int getNumberOfConfigurations() {
		return configuration.getNumberOfConfigurations();
	}

	public Properties getProperties(String key, Properties defaults) {
		return configuration.getProperties(key, defaults);
	}

	public Properties getProperties(String key) {
		return configuration.getProperties(key);
	}

	public Object getProperty(String key) {
		return configuration.getProperty(key);
	}

	public Node getRoot() {
		return configuration.getRoot();
	}

	public ConfigurationNode getRootNode() {
		return configuration.getRootNode();
	}

	public short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public short getShort(String key) {
		return configuration.getShort(key);
	}

	public Configuration getSource(String key) {
		return configuration.getSource(key);
	}

	public String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	public String getString(String key) {
		return configuration.getString(key);
	}

	public String[] getStringArray(String key) {
		return configuration.getStringArray(key);
	}

	public StrSubstitutor getSubstitutor() {
		return configuration.getSubstitutor();
	}

	public int hashCode() {
		return configuration.hashCode();
	}

	public Configuration interpolatedConfiguration() {
		return configuration.interpolatedConfiguration();
	}

	public void invalidate() {
		configuration.invalidate();
	}

	public boolean isDelimiterParsingDisabled() {
		return configuration.isDelimiterParsingDisabled();
	}

	public boolean isDetailEvents() {
		return configuration.isDetailEvents();
	}

	public boolean isEmpty() {
		return configuration.isEmpty();
	}

	public boolean isForceReloadCheck() {
		return configuration.isForceReloadCheck();
	}

	public boolean isThrowExceptionOnMissing() {
		return configuration.isThrowExceptionOnMissing();
	}

	public boolean removeConfiguration(Configuration config) {
		return configuration.removeConfiguration(config);
	}

	public Configuration removeConfiguration(String name) {
		return configuration.removeConfiguration(name);
	}

	public Configuration removeConfigurationAt(int index) {
		return configuration.removeConfigurationAt(index);
	}

	public boolean removeConfigurationListener(ConfigurationListener l) {
		return configuration.removeConfigurationListener(l);
	}

	public boolean removeErrorListener(ConfigurationErrorListener l) {
		return configuration.removeErrorListener(l);
	}

	public void setConversionExpressionEngine(
			ExpressionEngine conversionExpressionEngine) {
		configuration.setConversionExpressionEngine(conversionExpressionEngine);
	}

	public void setDelimiterParsingDisabled(boolean delimiterParsingDisabled) {
		configuration.setDelimiterParsingDisabled(delimiterParsingDisabled);
	}

	public void setDetailEvents(boolean enable) {
		configuration.setDetailEvents(enable);
	}

	public void setExpressionEngine(ExpressionEngine expressionEngine) {
		configuration.setExpressionEngine(expressionEngine);
	}

	public void setForceReloadCheck(boolean forceReloadCheck) {
		configuration.setForceReloadCheck(forceReloadCheck);
	}

	public void setListDelimiter(char listDelimiter) {
		configuration.setListDelimiter(listDelimiter);
	}

	public void setLogger(Log log) {
		configuration.setLogger(log);
	}

	public void setNodeCombiner(NodeCombiner nodeCombiner) {
		configuration.setNodeCombiner(nodeCombiner);
	}

	public void setProperty(String key, Object value) {
		configuration.setProperty(key, value);
	}

	public void setRoot(Node node) {
		configuration.setRoot(node);
	}

	public void setRootNode(ConfigurationNode rootNode) {
		configuration.setRootNode(rootNode);
	}

	public void setThrowExceptionOnMissing(boolean throwExceptionOnMissing) {
		configuration.setThrowExceptionOnMissing(throwExceptionOnMissing);
	}

	public Configuration subset(String prefix) {
		return configuration.subset(prefix);
	}

	public String toString() {
		return configuration.toString();
	}

	
}

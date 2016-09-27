package nl.isaac.comp.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNodeVisitor;

import nl.isaac.comp.configuration.visitor.ToStringMultiLineVisitor;
import nl.isaac.comp.configuration.visitor.ToStringSingleLineVisitor;
import nl.isaac.comp.configuration.visitor.ToStringTreeVisitor;


/**
 * A ISAAC configuration class with additional helper methods compared to {@link CombinedConfiguration} <br />
 * Also, {@link #setThrowExceptionOnMissing(boolean)} is set to true which means exceptions are thrown by default when a property does not exist.
 * This ensures a valid existing configuration file.
 * @author jan-willem
 *
 */
public class CustomConfiguration extends CombinedConfigurationWrapper {

	public CustomConfiguration(CombinedConfiguration configuration) {
		super(configuration);
		
		// by default setThrowExceptionOnMissing is false.
		// for ISAAC related projects this is on all the times
		setThrowExceptionOnMissing(true);
	}
	
	/**
	 * Get the configured file and converts it to a {@link File}. <br />
	 * Uses {@link Configuration#getString(String)} internally
	 * @param key
	 * @return
	 * @throws IllegalArgumentException is the file does not exist
	 */
	public File getFile(String key) {
		
		String value = getString(key);
		File f = new File(value);
		
		if (f.exists() && !f.isFile()) {
			throw new IllegalArgumentException("'" + key + "' resolves to '" + value + "' which is not a file");
		}
		
		return f;
	}
	
	/**
	 * Get the configured file, converts it to a {@link File} and checks if it exists. <br />
	 * Uses {@link Configuration#getString(String)} internally
	 * @param key
	 * @return
	 * @throws IllegalArgumentException is the file does not exist
	 */
	public File getExistingFile(String key) {
		
		String value = getString(key);
		File f = new File(value);
		
		if (!f.isFile()) {
			throw new IllegalArgumentException("'" + key + "' resolves to '" + value + "' which is not a valid file");
		}
		
		return f;
	}
	
	/**
	 * Get the configured directory and converts it to a {@link File}. <br />
	 * Uses {@link Configuration#getString(String)} internally
	 * @param key
	 * @return
	 * @throws IllegalArgumentException is the resolved directory does exists but is not a directory
	 */
	public File getDirectory(String key) throws IllegalArgumentException {
		
		String value = getString(key);
		File f = new File(value);
		
		if (f.exists() && !f.isDirectory()) {
			throw new IllegalArgumentException("'" + key + "' resolves to '" + value + "' which is not a directory");
		}
		
		return f;
	}
	
	/**
	 * Get the configured directory, converts it to a {@link File} and checks if it exists. <br />
	 * Uses {@link Configuration#getString(String)} internally
	 * @param key
	 * @return
	 * @throws IllegalArgumentException is the directory does not exist
	 */
	public File getExistingDirectory(String key) throws IllegalArgumentException {
		
		String value = getString(key);
		File f = new File(value);
		
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("'" + key + "' resolves to '" + value + "' which is not a valid directory");
		}
		
		return f;
	}
	
	/**
	 * Get the configured String value and converts it to the provided enum type
	 * @param enumClass
	 * @param key
	 * @return
	 * @throws IllegalArgumentException if the value is not a valid enum type
	 */
	public <T extends Enum<T>> T getEnum(Class<T> enumType, String key) throws IllegalArgumentException {
		
		String value = getString(key);

		T res = Enum.valueOf(enumType, value);
		
		return res;
	}
	
	/**
	 * Get the configured String value and converts it to the provided enum type. If the string is not set
	 * defaultValue is returned
	 * @param enumType
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws IllegalArgumentException if the value is not null and not a valid enum type
	 */
	public <T extends Enum<T>> T getEnum(Class<T> enumType, String key, T defaultValue) throws IllegalArgumentException {
		
		String value = getString(key, null);
		T res = defaultValue;
		if (value != null) {
			res = Enum.valueOf(enumType, value);
		}
		
		return res;
	}
	
	/**
	 * Returns a list of FileConfiguration's (in order) that the combined configuration was loaded from
	 * @return
	 */
	public List<FileConfiguration> getLoadedFileConfigurations() {
		
		List<FileConfiguration> result = new ArrayList<FileConfiguration>();
		processLoadedConfigurationFiles(result, getDelegate());
		
		return result;
	}
	
	protected void processLoadedConfigurationFiles(List<FileConfiguration> result, CombinedConfiguration configuration) {

		for (int i = 0; i < configuration.getNumberOfConfigurations(); i++) {
			Configuration c = configuration.getConfiguration(i);
			
			if (c instanceof FileConfiguration) {
				FileConfiguration fc = (FileConfiguration) c;
				result.add(fc);
			} else if (c instanceof CombinedConfiguration) {
				processLoadedConfigurationFiles(result, (CombinedConfiguration) c);
			}
			
		}
	}
	
	/**
	 * @see {@link #visit(ConfigurationNodeVisitor, boolean)}
	 * @param visitor
	 * @param interpolate when set, a interpolated copy of the configuration is used
	 */
	private void visit(ConfigurationNodeVisitor visitor, boolean interpolate) {
		
		if (interpolate) {
			
			HierarchicalConfiguration hc = (HierarchicalConfiguration) interpolatedConfiguration();
			hc.getRootNode().visit(visitor);
		} else {
			getRootNode().visit(visitor);
		}
	}
	
	/**
	 * Returns the entire non-interpolated configuration as a tree representation.
	 * @return
	 */
	public String toStringTree() {
	
		return toStringTree(false);
	}

	/**
	 * Returns the entire configuration as a tree representation
	 * @param interpolate when set, the interpolated result is printed
	 * @return
	 */
	public String toStringTree(boolean interpolate) {
		
		ConfigurationNodeVisitor visitor = new ToStringTreeVisitor();
		visit(visitor, interpolate);
		
		return visitor.toString();
	}
	
	/**
	 * Returns the entire non-interpolated configuration as a multiline representation.
	 * @return
	 */
	public String toStringMultiLine() {

		return toStringMultiLine(false);
	}
	
	/**
	 * Returns the entire configuration as a multiline representation
	 * @param interpolate when set, the interpolated result is printed
	 * @return
	 */
	public String toStringMultiLine(boolean interpolate) {
	
		ConfigurationNodeVisitor visitor = new ToStringMultiLineVisitor();
		getRootNode().visit(visitor);
		
		return visitor.toString();
	}
	
	@Override
	public String toString() {
	
		ConfigurationNodeVisitor visitor = new ToStringSingleLineVisitor();
		getRootNode().visit(visitor);
		
		return visitor.toString();
	}
	
	
}

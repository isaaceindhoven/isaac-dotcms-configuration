package nl.isaac.dotcms.plugin.configuration.dotcms;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.DefaultConfigurationBuilder.FileConfigurationProvider;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.beanutils.BeanDeclaration;

/**
 * A slightly augmented {@link FileConfigurationProvider}, it just defaults the bean class to {@link DotCMSFileConfiguration} and it sets a field on the instances created by this class.
 * @author maarten
 *
 */
public class DotCMSFileConfigurationProvider extends FileConfigurationProvider {

	private final String hostName;

	public DotCMSFileConfigurationProvider(String hostName) {
		super(DotCMSFileConfiguration.class);
		this.hostName = hostName;
	}

	/**
	 * @see FileConfigurationProvider#createBean(Class, BeanDeclaration, Object)
	 * One change is that it sets the field {@link DotCMSFileConfiguration#setHostName(String)}.
	 */
	@Override
	public Object createBean(@SuppressWarnings("rawtypes") Class beanClass, BeanDeclaration data, Object parameter) throws Exception {
		Object createBean = super.createBean(beanClass, data, parameter);
		((DotCMSFileConfiguration) createBean).setHostName(hostName);
		return createBean;
	}
	/**
	 * @see FileConfigurationProvider#createBeanInstance(Class, BeanDeclaration)
	 * One change is that it sets the field {@link DotCMSFileConfiguration#setHostName(String)}.
	 */
	@Override
	protected Object createBeanInstance(@SuppressWarnings("rawtypes") Class beanClass, BeanDeclaration data) throws Exception {
		Object createBeanInstance = super.createBeanInstance(beanClass, data);
		((DotCMSFileConfiguration) createBeanInstance).setHostName(hostName);
		return createBeanInstance;
	}
}

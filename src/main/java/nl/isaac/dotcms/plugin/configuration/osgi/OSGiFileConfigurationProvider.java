package nl.isaac.dotcms.plugin.configuration.osgi;

import org.apache.commons.configuration.DefaultConfigurationBuilder.FileConfigurationProvider;
import org.apache.commons.configuration.beanutils.BeanDeclaration;

import com.dotcms.repackage.org.osgi.framework.Bundle;

import nl.isaac.dotcms.plugin.configuration.dotcms.DotCMSFileConfiguration;


public class OSGiFileConfigurationProvider extends FileConfigurationProvider {
	
	private final Bundle bundle;

	public OSGiFileConfigurationProvider(Bundle bundle) {
		super(OSGiFileConfiguration.class);
		this.bundle = bundle;
	}

	/**
	 * @see FileConfigurationProvider#createBean(Class, BeanDeclaration, Object)
	 * One change is that it sets the field {@link DotCMSFileConfiguration#setHostName(String)}.
	 */
	@Override
	public Object createBean(@SuppressWarnings("rawtypes") Class beanClass, BeanDeclaration data, Object parameter) throws Exception {
		Object createBean = super.createBean(beanClass, data, parameter);
		((OSGiFileConfiguration) createBean).setBundle(bundle);
		return createBean;
	}
	/**
	 * @see FileConfigurationProvider#createBeanInstance(Class, BeanDeclaration)
	 * One change is that it sets the field {@link DotCMSFileConfiguration#setHostName(String)}.
	 */
	@Override
	protected Object createBeanInstance(@SuppressWarnings("rawtypes") Class beanClass, BeanDeclaration data) throws Exception {
		Object createBeanInstance = super.createBeanInstance(beanClass, data);
		((OSGiFileConfiguration) createBeanInstance).setBundle(bundle);
		return createBeanInstance;
	}

}

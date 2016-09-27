package nl.isaac.dotcms.plugin.configuration.dotcms;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.io.IOException;

import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException;
import nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.XMLConfiguration;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.files.factories.FileFactory;
import com.dotmarketing.portlets.files.model.File;
import com.liferay.portal.model.User;

/**
 * This changes the default {@link XMLConfiguration#load()} to use DotCMS APIs to load a file managed by DotCMS.
 * @author maarten
 *
 */
@SuppressWarnings("serial")
public class DotCMSFileConfiguration extends XMLConfiguration {
	private String hostName;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public void load() throws ConfigurationException {
		// And here we must read out dotCMS.
		final String fileName = getFileName();
		Host host;
		User systemUser;
		try {
			systemUser = APILocator.getUserAPI().getSystemUser();
			host = APILocator.getHostAPI().findByName(hostName, systemUser, false);
			if (host == null) {
				host = APILocator.getHostAPI().findByAlias(hostName, systemUser, false);
			}
		} catch (DotDataException e) {
			throw new ConfigurationException(e);
		} catch (DotSecurityException e) {
			throw new ConfigurationException(e);
		}
		File file = FileFactory.getFileByURI(fileName, host, true);
		try {
			java.io.File ioFile = APILocator.getFileAPI().getAssetIOFile(file);
			load(ioFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
	}
}

package com.dotmarketing.plugin.business;
/**
* dotCMS Configuration plugin by ISAAC - The Full Service Internet Agency is licensed 
* under a Creative Commons Attribution 3.0 Unported License
* - http://creativecommons.org/licenses/by/3.0/
* - http://www.geekyplugins.com/
* 
* @copyright Copyright (c) 2011 ISAAC Software Solutions B.V. (http://www.isaac.nl)
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.FactoryLocator;
import com.dotmarketing.cache.FileCache;
import com.dotmarketing.cache.IdentifierCache;
import com.dotmarketing.cache.LiveCache;
import com.dotmarketing.cache.WorkingCache;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.factories.PublishFactory;
import com.dotmarketing.plugin.model.Plugin;
import com.dotmarketing.plugin.model.PluginProperty;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.portlets.files.factories.FileFactory;
import com.dotmarketing.portlets.folders.factories.FolderFactory;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;

/**
 * @author Jason Tesser
 * @author Andres Olarte
 * @since 1.6.5c
 *
 */
public class PluginAPIImplDotCMS implements PluginAPI {

	private final PluginFactory pluginFac;
	private File pluginJarDir;
	private List<String> deployedPluginOrder;

	public PluginAPIImplDotCMS() {
		pluginFac = FactoryLocator.getPluginFactory();
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#delete(com.dotmarketing.plugin.model.Plugin)
	 */
	@Override
	public void delete(final Plugin plugin) throws DotDataException {
		pluginFac.delete(plugin);
	}

	@Override
	public void deletePluginProperties(final String pluginId) throws DotDataException {
		pluginFac.deletePluginProperties(pluginId);
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#loadPlugin(java.lang.String)
	 */
	@Override
	public Plugin loadPlugin(final String id) throws DotDataException {
		return pluginFac.loadPlugin(id);
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#loadPlugins()
	 */
	@Override
	public List<Plugin> findPlugins() throws DotDataException {
		return pluginFac.findPlugins();
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#loadProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String loadProperty(final String pluginId, final String key)	throws DotDataException {
		final PluginProperty pp = pluginFac.loadProperty(pluginId, key);
		if(pp!= null){
			return pp.getCurrentValue();
		}else{
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#save(com.dotmarketing.plugin.model.Plugin)
	 */
	@Override
	public void save(final Plugin plugin) throws DotDataException {
		pluginFac.save(plugin);
	}

	/* (non-Javadoc)
	 * @see com.dotmarketing.plugin.business.PluginAPI#saveProperty(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveProperty(final String pluginId, final String key, final String value)	throws DotDataException {
		PluginProperty pp = pluginFac.loadProperty(pluginId, key);
		if(pp != null && UtilMethods.isSet(pp.getPluginId())){
			pp.setOriginalValue(pp.getCurrentValue());
			pp.setCurrentValue(value);
		}else{
			pp = new PluginProperty();
			pp.setPropkey(key);
			pp.setPluginId(pluginId);
			pp.setOriginalValue(value);
			pp.setCurrentValue(value);
		}
		pluginFac.saveProperty(pp);
	}

	@Override
	public List<String> loadPluginConfigKeys(final String pluginId) throws DotDataException {
		final List<String> result = new ArrayList<String>();
		try{
			final JarFile jar = new JarFile(new File(pluginJarDir.getPath() + File.separator + "plugin-" + pluginId));
			final JarEntry entry = jar.getJarEntry("conf/plugin-controller.properties");
			final Properties props = new Properties();
			final InputStream in = jar.getInputStream(entry);
			props.load(in);
			final Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				final String key =  en.nextElement().toString().trim();
				result.add(key);
			}
			return result;
		}catch (final NullPointerException e){
			return result;
		}catch (final Exception e) {
			Logger.error(this, e.getMessage(), e);
			throw new DotDataException(e.getMessage(),e);
		}
	}

	@Override
	public String loadPluginConfigProperty(final String pluginId, final String key)	throws DotDataException {
		try{
			final JarFile jar = new JarFile(new File(pluginJarDir.getPath() + File.separator + "plugin-" + pluginId + ".jar"));
			final JarEntry entry = jar.getJarEntry("conf/plugin-controller.properties");
			final Properties props = new Properties();
			final InputStream in = jar.getInputStream(entry);
			props.load(in);
			return props.get(key).toString();
		}catch (final NullPointerException e){
			return "";
		}catch (final Exception e) {
			Logger.error(this, e.getMessage(), e);
			throw new DotDataException(e.getMessage(),e);
		}
	}

	@Override
	public List<String> getDeployedPluginOrder() {
		return deployedPluginOrder;
	}

	@Override
	public File getPluginJarDir() {
		return pluginJarDir;
	}

	@Override
	public void setDeployedPluginOrder(final List<String> pluginIds) {
		this.deployedPluginOrder = pluginIds;
	}

	@Override
	public void setPluginJarDir(final File directory) throws IOException {
		if(!directory.exists()){
			throw new IOException("The directory doesn't exist");
		}
		this.pluginJarDir = directory;
	}

	@Override
	public void loadBackEndFiles(final String pluginId) throws IOException, DotDataException{
		try{

			final HostAPI hostAPI = APILocator.getHostAPI();

			final User systemUser = APILocator.getUserAPI().getSystemUser();
			final JarFile jar = new JarFile(new File(pluginJarDir.getPath() + File.separator + "plugin-" + pluginId + ".jar"));
			final List<Host> hostList = new ArrayList<Host>();

			final String hosts = loadPluginConfigProperty(pluginId, "hosts.name");
			if(UtilMethods.isSet(hosts)){
				for(final String hostname : hosts.split(",")){
					final Host host = hostAPI.findByName(hostname, systemUser, false);
					hostList.add(host);
				}
			}else{
				final Host host = hostAPI.findDefaultHost(systemUser, false);
				hostList.add(host);
			}

			final Enumeration<?> resources = jar.entries();
			while(resources.hasMoreElements()){

				final JarEntry entry = (JarEntry) resources.nextElement();
				// find the files inside the dotcms folder in the jar to copy on backend with this reg expression ("dotcms\\/.*\\.([^\\.]+)$")
				if(entry.getName().matches("dotcms\\/.*\\.([^\\.]+)$") ){

					final String filePathAndName=entry.getName().substring(7);
					String filePath = "";
					if(filePathAndName.lastIndexOf("/") != -1){
						filePath = filePathAndName.substring(0, filePathAndName.lastIndexOf("/"));
					}
					final String fileName = filePathAndName.substring(filePathAndName.lastIndexOf("/")+1);
					final String pluginFolderPath = "/plugins/"+pluginId;

					Logger.debug(this,"files in dotcms:"+filePathAndName+"\n");
					//Create temporary file with the inputstream to be used in the FileFactory
					final InputStream input = jar.getInputStream(entry);
					final File temporaryFile = new File("file.temp");
					final OutputStream output=new FileOutputStream(temporaryFile);
					final byte buf[]=new byte[1024];
					int len;
					while((len=input.read(buf))>0){
						output.write(buf,0,len);
					}
					output.close();
					input.close();

					for(final Host host : hostList){

						Folder folder = FolderFactory.getFolderByPath(pluginFolderPath + "/" + filePath,host);
						if( !InodeUtils.isSet(folder.getInode())){
							folder = FolderFactory.createFolders(pluginFolderPath + "/" + filePath, host);
						}
						//GetPrevious version if exists
						final com.dotmarketing.portlets.files.model.File currentFile = FileFactory.getFileByURI(pluginFolderPath+"/"+filePathAndName, host, true);
						//Create the new file version
						final com.dotmarketing.portlets.files.model.File file = new com.dotmarketing.portlets.files.model.File();
						file.setFileName(fileName);
						file.setFriendlyName(UtilMethods.getFileName(fileName));
						file.setTitle(UtilMethods.getFileName(fileName));
						file.setMimeType(FileFactory.getMimeType(fileName));
						file.setOwner(systemUser.getUserId());
						file.setModUser(systemUser.getUserId());
						file.setModDate(new Date());
						file.setLive(true);
						file.setWorking(true);
						file.setParent(folder.getIdentifier());
						file.setSize((int)temporaryFile.length());

						InodeFactory.saveInode(file);
						FileCache.removeFile(file);
						// get the file Identifier
						Identifier ident = null;
						if (InodeUtils.isSet(currentFile.getInode())){
							ident = IdentifierCache.getIdentifierFromIdentifierCache(currentFile);
							FileCache.removeFile(currentFile);
						}else{
							ident = new Identifier();
						}
						//Saving the file, this creates the new version and save the new data
						com.dotmarketing.portlets.files.model.File workingFile = null;
						workingFile = FileFactory.saveFile(file, temporaryFile, folder, ident, systemUser);

						FileCache.removeFile(workingFile);
						ident = IdentifierCache.getIdentifierFromIdentifierCache(workingFile);

						//updating caches
						if (workingFile.isLive()){
							LiveCache.removeAssetFromCache(workingFile);
							LiveCache.addToLiveAssetToCache(workingFile);
						}else{
							LiveCache.removeAssetFromCache(file);
							LiveCache.addToLiveAssetToCache(file);
						}
						WorkingCache.removeAssetFromCache(workingFile);
						WorkingCache.addToWorkingAssetToCache(workingFile);

						//Publish the File
						PublishFactory.publishAsset(workingFile, systemUser, false);


					}

					temporaryFile.delete();

				}
			}

		}catch (final IOException e) {
			Logger.error(this, e.getMessage(), e);
			//throw new IOException("The directory doesn't exist");
		}catch (final Exception e) {
			Logger.error(this, e.getMessage(), e);
			//throw new DotDataException(e.getMessage(),e);
		}
	}

}

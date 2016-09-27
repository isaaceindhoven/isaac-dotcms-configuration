# dotCMS Configuration

A limitation of how the [dotCMS](https://www.dotcms.com) (non dynamic) plugin framework works is that a plugin's configuration can only be defined system wide and only inside the plugin's "conf" folder. This can be problematic for a number of reasons. First because as a developer you sometimes would like to be able to define different config values per dotCMS host. Second because when you deploy the same plugin in different dotCMS instances you might need different config values for each instance. This makes upgrading installed plugins error prone, because you have to make sure to use the right config values for each instance. This "dotCMS configuration plugin" fixes these issues in a transparent way, making it compatible with most of your existing plugins. It also adds some other handy configuration utilities that are explained below.

Previous releases of this plugin for older dotCMS versions can be found [here](../../releases).

## Features

*  [Instance specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Features#problem-1-instance-specific-plugin-configuration)
*  [Host specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Features#problem-2-host-specific-plugin-configuration)
*  [Client specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Features#problem-3-client-specific-plugin-configuration)
*  [Complex plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Features#problem-4-complex-plugin-configuration)
*  [Host configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Features#problem-5-host-configuration)

## Installation

To use it, you obviously first need to install the plugin. To install it take these steps:

* Clone this repository.
* Open the console and go the the folder containing pom.xml
* Execute the following maven command: **mvn clean package**
* The build should succeed and a folder named "target" will be created.
* Open the "target" folder and check if the **.zip** file exists.
* [Install the plugin](https://dotcms.com/plugins/plugin-faq.dot#HowInstall), **but don't restart dotCMS yet**.
* [Put the configuration plugin's config files in the correct place.](#configFiles)
* Restart dotCMS
* To check if the plugin is installed correctly, visit the following URL and replace **your-url-here** with your own site address: **http://your-url-here/app/servlets/monitoring/isaac-dotcms-configuration**
* [Check if the configuration plugin's portlet is working](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#check-if-the-configuration-plugins-portlet-is-working)

That's it, you've installed the plugin.

More information about the sixth and last step can be found by clicking on the links. Below we explain the other steps separately.

#### <a name="configFiles"></a> Put the configuration plugin's config files in the correct place

Inside the configuration plugin's root folder there is a subfolder "server\tomcat\conf\applications". Copy that "applications" subfolder into the "\tomcat\conf" folder located in the dotCMS root. The copied folder contains a number of files that tell the configuration plugin where to look for the files containing the overrides and also defines a few system wide configuration settings that you can change later. How this works in detail is explained in the [Tips and Tricks](#tipsAndTricks) section below. For now just copy the files and don't change them.

#### Restart dotCMS

Now restart dotCMS. If you restarted dotCMS before you copied the files in the previous step you would have seen that dotCMS would not start correctly. If you'd surf to the dotCMS admin you'd see a really big "nl.isaac.dotcms.plugin.configuration.dependencies.org.apache.commons.configuration.ConfigurationException" exception pointing out that you misconfigured the configuration plugin. (Failing fast like this usually creates more stable systems)

## Usage

* [Define the overrides you'd like to make](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#define-the-overrides-youd-like-to-make)
* [Instance specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#problem-1-instance-specific-plugin-configuration)
* [Host specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#problem-2-host-specific-plugin-configuration)
* [Client specific plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#problem-3-client-specific-plugin-configuration)
* [Complex plugin configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#problem-4-complex-plugin-configuration)
* [Host configuration](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#problem-5-host-configuration)
* [Configuration files inside of the dotCMS admin](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#configuration-files-inside-of-the-dotcms-admin)
* [Override order](https://github.com/isaaceindhoven/DotCMS-Configuration/wiki/Usage#override-order)

## <a name="tipsAndTricks"></a> Tips and Tricks

#### Changing the override order or file locations

You can change the override order and even the location of the override files. The configuration plugin is based on the wonderful [apache commons configuration](https://commons.apache.org/configuration) library. This library uses config files that define the location and the override order of all the other configuration files that we actually want to read and merge. The configuration plugin also uses these config files. Their location is hardcoded into the configuraton plugin code and cannot be easily changed. There are three of these config files:

* /tomcat/conf/applications/configurationPluginConfig.xml
	* This file contains the location of the serverConfig.xml file.
* /tomcat/conf/applications/locationConfigHosts.xml
	* This file lists all host configuration files. You can reorder them and change their location. The higer a file is in this list the higer it will be in the override order. So the first file will override all other files below it.
* /tomcat/conf/applications/locationConfigPlugins.xml
	* This file lists all plugin configuration files. You can reorder them and change their location.

#### Java filters and plugin order

If you use filters in your own plugin make sure that the configuration plugin is build before your own plugins. You can do this by putting your plugin above the configuration plugin in the [plugins.xml](https://www.dotcms.com/docs/latest/DirectoryStructure) file located in the plugins folder.

## Meta

[ISAAC - 100% Handcrafted Internet Solutions](https://www.isaac.nl) – [@ISAAC](https://twitter.com/isaaceindhoven) – [info@isaac.nl](mailto:info@isaac.nl)

Distributed under the [Creative Commons Attribution 3.0 Unported License](https://creativecommons.org/licenses/by/3.0/).
